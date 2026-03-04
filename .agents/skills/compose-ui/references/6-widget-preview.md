# Example: Standalone Widget with @Preview

Every reusable widget composable needs its own `@Preview` in the **same file**. Widgets receive only data and callbacks — no component reference.

---

## Feature widget (card)

```kotlin
// ItemCard.kt

@Composable
fun ItemCard(
    item: Item,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = CustomTheme.colors.fill.primary,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = CustomTheme.typography.body.bold,
                    color = CustomTheme.colors.text.primary,
                )
                Text(
                    text = item.subtitle,
                    style = CustomTheme.typography.caption.mRegular,
                    color = CustomTheme.colors.text.secondary,
                )
            }
            Icon(
                painter = painterResource(R.drawable.ic_arrow_right),
                contentDescription = null,
                tint = CustomTheme.colors.icon.secondary,
            )
        }
    }
}

@Preview
@Composable
private fun ItemCardPreview() {
    AppTheme {
        ItemCard(
            item = Item.MOCK,
            onClick = {},
        )
    }
}
```

---

## Multiple preview states

Add separate `@Preview` for each meaningful state (loading, empty, error, etc.):

```kotlin
@Preview(name = "Active")
@Composable
private fun ItemCardActivePreview() {
    AppTheme {
        ItemCard(item = Item.MOCK, onClick = {})
    }
}

@Preview(name = "Disabled")
@Composable
private fun ItemCardDisabledPreview() {
    AppTheme {
        ItemCard(item = Item.MOCK_DISABLED, onClick = {})
    }
}
```

---

## Preview rules summary

| Rule                      | Detail                                                     |
|---------------------------|------------------------------------------------------------|
| Always wrap in `AppTheme` | Not `CustomTheme`, not `MaterialTheme`                     |
| Always `private`          | Exception: core/shared widgets that expose public previews |
| Naming                    | `XxxPreview`                                               |
| Mock data                 | Use `Entity.MOCK` / `Entity.MOCKS` from companion object   |
| Location                  | Same file as the composable                                |
| Lambda events             | Use `{}` — no logic needed in previews                     |
| Multiple states           | Separate `@Preview` functions per state                    |
