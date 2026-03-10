# Example: MessageService (Toast/Snackbar)

Shows how to use MessageService for displaying Toast/Snackbar messages in components.

---

## Overview

**MessageService** is a centralized service for showing Toast/Snackbar messages. It's injected into components and used to display success/error/info messages to users.

**Key feature:** MessageService is global - messages are observed and displayed at root level automatically. No UI rendering needed in components.

---

## 1. Basic Usage

### Success Message

```kotlin
class RealActionsComponent(
    componentContext: ComponentContext,
    private val messageService: MessageService
) : ComponentContext by componentContext, ActionsComponent {

    override fun onSaveClick() {
        componentScope.safeLaunch(errorHandler) {
            // ... perform save operation

            messageService.showMessage(
                Message(
                    text = StringDesc.Resource(R.string.actions_save_success),
                    type = MessageType.Positive
                )
            )
        }
    }
}
```

### Error Message with Icon

```kotlin
override fun onDeleteClick() {
    componentScope.safeLaunch(errorHandler) {
        // ... perform delete operation

        messageService.showMessage(
            Message(
                text = StringDesc.Resource(R.string.actions_delete_success),
                type = MessageType.Negative,
                iconRes = R.drawable.ic_delete
            )
        )
    }
}
```

### Info Message

```kotlin
override fun onShareClick() {
    componentScope.safeLaunch(errorHandler) {
        // ... perform share operation

        messageService.showMessage(
            Message(
                text = StringDesc.Resource(R.string.actions_link_copied),
                type = MessageType.Neutral,
                iconRes = R.drawable.ic_link
            )
        )
    }
}
```

---

## 2. Message with Action Button

```kotlin
fun onDismissNotification() {
    componentScope.safeLaunch(errorHandler) {
        // ... perform dismiss operation

        messageService.showMessage(
            Message(
                text = StringDesc.Resource(R.string.notification_dismissed),
                type = MessageType.Neutral,
                actionTitle = StringDesc.Resource(R.string.undo),
                action = {
                    // Undo action
                }
            )
        )
    }
}
```

---

## 3. Dynamic Message Text

### Using StringDesc.Raw

```kotlin
fun onCopyToClipboard(text: String) {
    // ... copy to clipboard

    messageService.showMessage(
        Message(
            text = StringDesc.Raw("Copied: $text"),
            type = MessageType.Neutral
        )
    )
}
```

### Using String Resource with Placeholder

```kotlin
fun onQuantityChanged(quantity: Int) {
    componentScope.safeLaunch(errorHandler) {
        // ... update quantity

        messageService.showMessage(
            Message(
                text = StringDesc.Resource(
                    R.string.item_quantity_updated,
                    quantity // String resource with placeholder: "Updated to %d items"
                ),
                type = MessageType.Positive
            )
        )
    }
}
```

---

## Key Points

### Message Types:
- **MessageType.Positive** - Success messages (green background)
- **MessageType.Negative** - Error/warning messages (red background)
- **MessageType.Neutral** - Info messages (gray background)

### Message Fields:
- `text: StringDesc` - Message text (required)
- `type: MessageType` - Visual style (required)
- `iconRes: Int?` - Optional icon drawable resource
- `actionTitle: StringDesc?` - Optional action button text
- `action: (() -> Unit)?` - Optional action button callback

### StringDesc Usage:
- `StringDesc.Resource(R.string.key)` - Localized string from resources
- `StringDesc.Resource(R.string.key, arg1, arg2)` - With placeholders
- `StringDesc.Raw("text")` - Dynamic non-localized text

### Best Practices:
- Show success messages for important user actions (save, delete, share)
- Show error messages with retry action when operations fail
- Keep messages short and clear
- Use appropriate icons to reinforce message type
- Provide undo/retry actions when applicable

### Fake Components:
- No MessageService needed in Fake implementations
- Messages aren't displayed in Composable previews

### Global Message Handling:
- Messages are observed at root level (RootComponent)
- Displayed as Snackbar or Toast depending on platform/design
- No UI rendering needed in individual components
- Just call `messageService.showMessage()` anywhere in the component
