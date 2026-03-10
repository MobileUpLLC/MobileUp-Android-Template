#!/usr/bin/env bash
set -euo pipefail

usage() {
  cat <<'EOF'
Usage:
  mr-comments.sh <MR_IID> [CONTEXT_LINES]

Examples:
  mr-comments.sh 754
  mr-comments.sh 754 5

Notes:
  - Reads only from GitLab API via glab (does not read source files from local repo).
  - Uses the current `glab` host and authenticated account context.
EOF
}

require_cmd() {
  if ! command -v "$1" >/dev/null 2>&1; then
    echo "Missing required command: $1" >&2
    exit 1
  fi
}

if [ "${1:-}" = "-h" ] || [ "${1:-}" = "--help" ]; then
  usage
  exit 0
fi

if [ $# -lt 1 ]; then
  usage
  exit 1
fi

MR_IID="$1"
CTX="${2:-3}"

if ! [[ "$MR_IID" =~ ^[0-9]+$ ]]; then
  echo "MR_IID must be a number, got: $MR_IID" >&2
  exit 1
fi

if ! [[ "$CTX" =~ ^[0-9]+$ ]]; then
  echo "CONTEXT_LINES must be a non-negative number, got: $CTX" >&2
  exit 1
fi

require_cmd glab
require_cmd jq
require_cmd nl
require_cmd sed
require_cmd awk
require_cmd cksum
require_cmd sort

tmp="$(mktemp -d)"
trap 'rm -rf "$tmp"' EXIT

glab api "projects/:fullpath/merge_requests/$MR_IID/discussions?per_page=100" --paginate > "$tmp/discussions.json"

FETCH_JOBS=6

jq -r '
  .[] | .notes[] |
  select(.type == "DiffNote" and .position.position_type == "text") |
  (.position.new_path // .position.old_path // "") as $path |
  (if .position.new_line then "new" else "old" end) as $side |
  (if .position.new_line
    then (.position.head_sha // .position.start_sha // .position.base_sha // "")
    else (.position.base_sha // .position.start_sha // .position.head_sha // "")
  end) as $ref |
  [
    .author.username,
    (.body | gsub("\r"; " ") | gsub("\n"; "\\n")),
    $path,
    ((.position.new_line // .position.old_line // 0) | tostring),
    $side,
    $ref,
    ($path | @uri)
  ] | @tsv
' "$tmp/discussions.json" > "$tmp/notes.tsv"

found=0
> "$tmp/notes_with_key.tsv"
> "$tmp/fetch_candidates.tsv"
while IFS=$'\t' read -r author body path line side ref path_enc; do
  cache_key=""
  if [ -n "$path" ] && [ -n "$ref" ] && [[ "$line" =~ ^[0-9]+$ ]]; then
    cache_key="$(printf '%s|%s' "$ref" "$path" | cksum | awk '{print $1}')"
    printf '%s\t%s\t%s\n' "$cache_key" "$path_enc" "$ref" >> "$tmp/fetch_candidates.tsv"
  fi

  printf '%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n' \
    "$author" "$body" "$path" "$line" "$side" "$ref" "$path_enc" "$cache_key" >> "$tmp/notes_with_key.tsv"
done < "$tmp/notes.tsv"

if [ -s "$tmp/fetch_candidates.tsv" ]; then
  sort -u "$tmp/fetch_candidates.tsv" > "$tmp/fetch.tsv"

  batch_count=0
  while IFS=$'\t' read -r cache_key path_enc ref; do
    (
      raw_file="$tmp/$cache_key.raw"
      nl_file="$tmp/$cache_key.nl"

      glab api "projects/:fullpath/repository/files/$path_enc/raw?ref=$ref" \
        > "$raw_file" 2>/dev/null || rm -f "$raw_file"

      if [ -f "$raw_file" ]; then
        nl -ba "$raw_file" > "$nl_file"
      fi
    ) &

    batch_count=$((batch_count + 1))
    if [ "$batch_count" -ge "$FETCH_JOBS" ]; then
      wait
      batch_count=0
    fi
  done < "$tmp/fetch.tsv"
  wait
fi

while IFS=$'\t' read -r author body path line side ref path_enc cache_key; do
  found=1

  if [ -z "$path" ] || [ -z "$ref" ] || ! [[ "$line" =~ ^[0-9]+$ ]]; then
    echo "=== skipped invalid note payload ==="
    echo
    continue
  fi

  nl_file="$tmp/$cache_key.nl"

  if [ ! -f "$nl_file" ]; then
    echo "=== $path:$line [$side] @$author ==="
    echo "comment: $body"
    echo "[failed to load file from GitLab API for ref=$ref]"
    echo
    continue
  fi

  start=$((line - CTX))
  if [ "$start" -lt 1 ]; then
    start=1
  fi
  end=$((line + CTX))

  echo "=== $path:$line [$side] @$author ==="
  echo "comment: $body"
  sed -n "${start},${end}p" "$nl_file" | awk -v target="$line" '
    {
      if ($1 == target) {
        print ">> " $0
      } else {
        print "   " $0
      }
    }
  '
  echo
done < "$tmp/notes_with_key.tsv"

if [ "$found" -eq 0 ]; then
  echo "No inline diff comments found for MR !${MR_IID}."
fi
