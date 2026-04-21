---
name: mr-comments
description: Fetch and present comments for a specific GitLab Merge Request. Use this skill when the user asks to show comments for a concrete Merge Request ID.
---

# MR Comments

Run `./.agents/skills/mr-comments/scripts/mr-comments.sh <MR_ID>` from the repository root to fetch
comments for the specified merge request ID.
Present the comments in a clean, readable format.

Important: do not run the command more than once, because it is slow, unless the user explicitly
asks to fetch comments again.

## Troubleshooting

If you get an error that `glab` is not installed, do not install `glab` as an agent.
Show the following help text to the user so they can install it manually:

For fetching MR comments, `glab` (GitLab CLI) is required.
Installation - https://gitlab.com/gitlab-org/cli#installation
Authorization (via personal access token) - https://gitlab.com/gitlab-org/cli#personal-access-token
