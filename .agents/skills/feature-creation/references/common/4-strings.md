# String Resources Organization

String resources naming conventions (applies to both Simple and Complex features).

## File Location

`src/values/{feature_name}_strings.xml`

## Naming Patterns

### Simple Feature Pattern

**Pattern:** `{feature_name}_{context}_{element}`

**Examples:**
- `item_details_title` - Screen title
- `item_details_loading` - Loading state message
- `item_details_error` - Error message
- `item_details_button_save` - Button text
- `user_profile_button_edit` - Button in user profile feature

### Complex Feature Pattern

**Pattern:** `{feature_name}_{subdomain}_{context}_{element}` (subdomain-specific)
**Pattern:** `{feature_name}_{context}_{element}` (common strings)

**Examples:**
- `feature_name_subdomain_a_title` - Subdomain A screen title
- `feature_name_subdomain_b_action` - Subdomain B action button
- `feature_name_option_first` - Common option (no subdomain prefix)
- `feature_name_error_loading` - Common error message

## Templates

### Simple Feature Template

```xml
<resources>
    <string name="item_details_title">Item Details</string>
    <string name="item_details_loading">Loading item…</string>
    <string name="item_details_error">Failed to load item</string>
    <string name="item_details_retry">Retry</string>
    <string name="item_details_button_save">Save</string>
    <string name="item_details_button_cancel">Cancel</string>
</resources>
```

### Complex Feature Template

```xml
<resources>
    <!-- Subdomain A -->
    <string name="feature_name_subdomain_a_title">Subdomain A Title</string>
    <string name="feature_name_subdomain_a_description">Description for subdomain A</string>
    <string name="feature_name_subdomain_a_action">Action A</string>

    <!-- Subdomain B -->
    <string name="feature_name_subdomain_b_title">Subdomain B Title</string>
    <string name="feature_name_subdomain_b_description">Description for subdomain B</string>
    <string name="feature_name_subdomain_b_action">Action B</string>

    <!-- Common -->
    <string name="feature_name_option_first">First Option</string>
    <string name="feature_name_option_second">Second Option</string>
    <string name="feature_name_option_third">Third Option</string>

    <!-- Common errors -->
    <string name="feature_name_error_loading">Failed to load data</string>
    <string name="feature_name_retry">Retry</string>
</resources>
```

## Rules

### Simple Features
- ✅ Always prefix with feature name
- ✅ Use underscores for separation
- ✅ Be descriptive but concise
- ❌ Don't use generic names without prefix

### Complex Features
- ✅ Prefix with feature name
- ✅ Add subdomain name for subdomain-specific strings
- ✅ Omit subdomain for common strings
- ✅ Group by subdomain with XML comments
- ❌ Don't mix subdomain-specific and common without clear separation

## Quick Reference

| Feature Type | Pattern | Example |
|-------------|---------|---------|
| **Simple** | `{feature}_{context}_{element}` | `item_details_title` |
| **Complex (subdomain)** | `{feature}_{subdomain}_{context}_{element}` | `dashboard_admin_title` |
| **Complex (common)** | `{feature}_{context}_{element}` | `dashboard_error_loading` |
