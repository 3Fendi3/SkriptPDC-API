---

<p align="center">
  <h1 align="center">SkriptPDC API</h1>
  <p align="center">
    <strong>Version 2.1</strong>
  </p>
  <p align="center">
    <em>Advanced Persistent Data Container API for Skript</em>
  </p>
</p>

---

## 📚 Overview

SkriptPDC is a powerful addon for Skript that provides comprehensive support for Minecraft's Persistent Data Container (PDC) system. Store custom data on **items, blocks, entities, and even inventories** with automatic type detection and easy-to-use syntax.

### Key Features
*   **🎯 Auto Type Detection**: Automatically detects and handles Integer, Double, Float, Long, Byte, and String types.
*   ** complex Types**: Serializes and deserializes any other Skript type like `location`, `itemstack`, etc.
*   **📋 List All Tags**: Retrieve all PDC keys from any object with a single expression.
*   ** inventory Support**: Store temporary data directly on custom inventories (GUIs).
*   **➕ Math Operations**: Add and remove numeric values directly from PDC tags.
*   **🧹 Clean Operations**: Clear all or specific PDC tags with easy-to-use effects.
*   **⚡ Performance**: Optimized code for fast and reliable data manipulation.

---

## 🚀 Installation & Setup

1.  **Download:** Get the latest version of `SkriptPDC.jar`.
2.  **Install:** Place the downloaded `.jar` file into your server's `/plugins/` directory.
3.  **Restart:** Restart your server completely.
4.  **Verify:** Check your server console for the startup message:

```
[SkriptPDC] SkriptPDC-API v2.1 Enabled
```

**Dependencies:** You must have [Skript](https://github.com/SkriptLang/Skript/releases) installed on your server.

---

## 📝 Expressions

### `Expression` PDC Tag
> `pdc tag %string% of %objects%`

**Description:** Gets or sets a PDC tag value with automatic type detection. Supports `get`, `set`, `delete`, `add`, and `remove` operations. Works with items, blocks, entities, players, inventories, and more.

**💡 Examples:**
```skript
# Set different data types on an item
set pdc tag "myplugin:level" of player's tool to 5
set pdc tag "myplugin:name" of player's tool to "Legendary Sword"
set pdc tag "myplugin:damage" of player's tool to 12.5

# Set data on an inventory
set {_inv} to chest inventory with 1 row
set pdc tag "gui:type" of {_inv} to "main_menu"

# Get values
set {_level} to pdc tag "myplugin:level" of player's tool
send "Item level: %{_level}%"

# Delete tag
delete pdc tag "myplugin:level" of player's tool
```

### `Expression` All PDC Tags
> `[all] [the] pdc tag[s] of %objects%`

**Description:** Returns a list of all PDC tag keys present on an object.

**💡 Examples:**
```skript
# Get all tags from an item
set {_tags::*} to all pdc tags of player's tool
send "This item has %size of {_tags::*}% tags"

# Loop through all tags of a player
loop all pdc tags of player:
    set {_value} to pdc tag loop-value of player
    send "%loop-value%: %{_value}%"
```

---

## 📝 Checking Tags (Conditions)

To check if a tag exists, use the `pdc tag` expression combined with Skript's built-in `is set` / `is not set` conditions. This is the universal and most stable way to perform checks.

### `Condition` Check if Tag Exists
> `pdc tag %string% of %objects% is set`
> `pdc tag %string% of %objects% is not set`

**Description:** Checks if an object has a specific PDC tag.

**💡 Examples:**
```skript
# Check if an item tag exists
if pdc tag "myplugin:special" of player's tool is set:
    send "This is a special item!"

# Check if an inventory tag does NOT exist
if pdc tag "gui:shop_menu" of player's top inventory is not set:
    send "This is not a shop menu."
```

### `Condition` Check if PDC is Empty
> `size of all pdc tags of %objects% is 0`
> `size of all pdc tags of %objects% is not 0`

**Description:** Checks if an object's PDC is empty (contains no tags).

**💡 Examples:**
```skript
# Check if an item's PDC is empty
if size of all pdc tags of player's tool is 0:
    send "This item has no custom data"

# Check if a block's PDC is not empty
if size of all pdc tags of clicked block is not 0:
    send "This block contains custom data"
```

---

## ⚙️ Effects

### `Effect` Clear PDC
> `clear [all] pdc (of|from) %objects%`

**Description:** Removes all PDC tags from an object.

**💡 Examples:**
```skript
# Clear all tags from an item
clear all pdc of player's tool

# Clear data from an inventory
clear pdc from {_my_gui}
```

### `Effect` Remove PDC Tags
> `(remove|delete) pdc tag[s] %strings% (of|from) %objects%`

**Description:** Removes one or more specific PDC tags from an object.

**💡 Examples:**
```skript
# Remove single tag
remove pdc tag "myplugin:temp" of player's tool

# Remove multiple tags
remove pdc tags "myplugin:level", "myplugin:exp" and "myplugin:kills" of player's tool
```

---

## 💡 Usage Examples

### Example 1: Item Leveling System
```skript
# Create leveling item
command /levelitem:
    trigger:
        give player diamond sword named "&6Legendary Sword"
        set pdc tag "weapon:level" of player's tool to 1
        set pdc tag "weapon:exp" of player's tool to 0

# Gain experience on kill
on death:
    if pdc tag "weapon:level" of attacker's tool is set:
        add 25 to pdc tag "weapon:exp" of attacker's tool
        
        # Check for level up
        set {_exp} to pdc tag "weapon:exp" of attacker's tool
        set {_level} to pdc tag "weapon:level" of attacker's tool
        if {_exp} >= {_level} * 100:
            add 1 to pdc tag "weapon:level" of attacker's tool
            set pdc tag "weapon:exp" of attacker's tool to 0
            send "&aLevel Up!" to attacker
```

### Example 2: Soulbound Items
```skript
# Command to bind an item to the player
command /soulbind:
    trigger:
        if player's tool is air:
            send "&cYou must hold an item to soulbind it!"
            stop
        
        if pdc tag "soulbound:owner" of player's tool is set:
            send "&cThis item is already soulbound!"
            stop
            
        set pdc tag "soulbound:owner" of player's tool to player's uuid
        add "&dSoulbound to %player%" to lore of player's tool
        send "&aYour item is now soulbound to you!"

# Prevent other players from picking up a soulbound item
on pickup:
    if pdc tag "soulbound:owner" of event-item is set:
        set {_ownerUUID} to pdc tag "soulbound:owner" of event-item
        if player's uuid is not {_ownerUUID}:
            send "&cThis item is soulbound to another player."
            cancel event
```

---

## 💾 Supported Data Types

SkriptPDC intelligently handles data types. When you set a tag, the addon automatically detects the type and stores it using the most appropriate format.

| Data Type       | Skript Equivalent | Example Value     |
|-----------------|-------------------|-------------------|
| String          | `text`            | `"Hello World"`   |
| Integer         | `integer`         | `42`              |
| Double / Float  | `number`          | `3.14159`         |
| Long            | `number`          | `999999999`       |
| Byte            | `number`          | `127`             |

### Complex Data Types
For any other Skript data type (like `location`, `itemstack`, `player`, etc.), SkriptPDC will use Skript's built-in serialization system. This converts the object into text, stores it, and deserializes it back into an object when you retrieve it.

```skript
# Storing a location on a player
set pdc tag "homes:home1" of player to player's location
# ... later
set {_home} to pdc tag "homes:home1" of player
teleport player to {_home}
```

---

## ❓ FAQ & Troubleshooting

### My PDC data on an item isn't saving! Why?
This is a common issue related to how Minecraft handles item data (ItemMeta). In older versions of this addon, modifying `player's tool` could fail. This has been fixed! SkriptPDC now correctly applies changes directly to `player's tool`.

However, if you manipulate items stored in **variables** or **inventory slots**, you still need to manually apply the modified item back to its original place.

```skript
# Correct way to update an item in a specific slot
set {_item} to slot 0 of player's inventory
set pdc tag "myplugin:tag" of {_item} to "value"
set slot 0 of player's inventory to {_item} # This line is crucial!
```

### What format should my tag string be?
Tags should ideally be in the format `namespace:key` (e.g., `myplugin:level`). The namespace prevents conflicts with other plugins. If you provide a key without a namespace (e.g., `"level"`), SkriptPDC will automatically use your plugin's name as the namespace.

### Can I store data on inventories permanently?
No. The inventory PDC support is designed for storing **temporary** data on GUIs while they are open. All PDC data associated with an inventory is automatically cleared from memory when the inventory is closed to prevent memory leaks.

---

<p align="center">
  SkriptPDC API v2.1 - Created by Fendi
</p>
