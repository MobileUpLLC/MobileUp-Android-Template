# Example: MessageService (Toast/Snackbar)

Shows how to use MessageService for displaying Toast/Snackbar messages in components.

---

## Overview

**MessageService** is a centralized service for showing Toast/Snackbar messages. It's injected into
components and used to display success/error/info messages to users.

**Key feature:** MessageService is global - messages are observed and displayed at root level
automatically. No UI rendering needed in components.

---

## 1. Basic Usage

### Message

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
                    text = Res.string.actions_save_success.resourceDesc(),
                    type = MessageType.Positive,
                    iconRes = Res.drawable.ic_success // optional icon
                )
            )
        }
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
                text = Res.string.string.notification_dismissed,
                type = MessageType.Neutral,
                actionTitle = Res.string.string.undo,
                action = {
                    // Undo action
                }
            )
        )
    }
}
```
---

## Key Points

### Message Types:
- **MessageType.Positive** - Success messages
- **MessageType.Negative** - Error messages 
- **MessageType.Neutral** - Info messages

### Message Fields:
- `text: StringDesc` - Message text (required)
- `type: MessageType` - Visual style (required)
- `iconRes: Int?` - Optional icon drawable resource
- `actionTitle: StringDesc?` - Optional action button text
- `action: (() -> Unit)?` - Optional action button callback

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
