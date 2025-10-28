<p align="center">
  <h1 align="center">SkriptPDC API</h1>
  <p align="center">
    <strong>Version 2.0</strong>
  </p>
  <p align="center">
    <em>Advanced Persistent Data Container API for Skript</em>
  </p>
</p>

---

## Table of Contents
*   [📚 Overview](#-overview)
*   [🚀 Installation](#-installation--setup)
*   [📝 Expressions](#-expressions)
*   [⚖ Conditions](#-conditions)
*   [⚙️ Effects](#️-effects)
*   [💡 Usage Examples](#-usage-examples)
*   [💾 Supported Data Types](#-supported-data-types)
*   [🔬 Testing & Debugging](#-testing--debugging)
*   [❓ FAQ & Troubleshooting](#-faq--troubleshooting)

---

## 📚 Overview

SkriptPDC is a powerful addon for Skript that provides comprehensive support for Minecraft's Persistent Data Container (PDC) system. Store custom data on items, blocks, and entities with automatic type detection and easy-to-use syntax.

### What's New in v2.0?
*   **🎯 Auto Type Detection**: Automatically detects and handles Integer, Double, Float, Long, Byte, and String types.
*   **📋 List All Tags**: Retrieve all PDC keys from any object with a single expression.
*   **&nbsp;✓ &nbsp; Tag Checking**: New conditions to check if tags exist or if PDC is empty.
*   **🧹 Clean Operations**: Clear all or specific PDC tags with easy-to-use effects.
*   **➕ Math Operations**: Add and remove numeric values directly from PDC tags.
*   **⚡ Performance**: Optimized code for faster operations and better error handling.

---

## 🚀 Installation & Setup

Installing SkriptPDC is quick and easy. Follow these steps:
1.  **Download:** Get the latest version of `SkriptPDC.jar`.
2.  **Install:** Place the downloaded `.jar` file into your server's `/plugins/` directory.
3.  **Restart:** Restart your server completely.
4.  **Verify:** Check your server console for the startup message:

```
[SkriptPDC] SkriptPDC-API v2.0 Enabled
```

**Dependencies:** You must have [Skript](https://github.com/SkriptLang/Skript/releases) installed on your server.

---

## 📝 Expressions

### `Expression` PDC Tag
> `pdc tag %string% of %itemtypes/blocks/entities%`

**Description:** Gets or sets a PDC tag value with automatic type detection. Supports GET, SET, DELETE, ADD, and REMOVE operations.

**💡 Examples:**
```skript
# Set different data types
set pdc tag "myplugin:level" of player's tool to 5
set pdc tag "myplugin:name" of player's tool to "Legendary Sword"
set pdc tag "myplugin:damage" of player's tool to 12.5

# Get values
set {_level} to pdc tag "myplugin:level" of player's tool
send "Item level: %{_level}%"

# Delete tag
delete pdc tag "myplugin:level" of player's tool
```

### `Expression` All PDC Tags
> `[all] [the] pdc tag[s] of %itemtypes/blocks/entities%`

**Description:** Returns a list of all PDC tag keys present on an object.

**💡 Examples:**
```skript
# Get all tags
set {_tags::*} to all pdc tags of player's tool
send "This item has %size of {_tags::*}% tags"

# Loop through all tags
loop all pdc tags of player's tool:
    set {_value} to pdc tag loop-value of player's tool
    send "%loop-value%: %{_value}%"
```

---

## ⚖ Conditions

### `Condition` Has PDC Tag
> `%itemtype/block/entity% has pdc [tag] %string%`
> `%itemtype/block/entity% doesn't have pdc [tag] %string%`

**Description:** Checks if an object has a specific PDC tag.

**💡 Examples:**
```skript
# Check if tag exists
if player's tool has pdc tag "myplugin:special":
    send "This is a special item!"

# Negative check
if player's tool doesn't have pdc tag "myplugin:cursed":
    send "This item is not cursed"
```

### `Condition` PDC Is Empty
> `[the] pdc of %itemtype/block/entity% is empty`
> `[the] pdc of %itemtype/block/entity% isn't empty`

**Description:** Checks if an object's PDC is empty (contains no tags).

**💡 Examples:**
```skript
# Check if empty
if pdc of player's tool is empty:
    send "This item has no custom data"

# Check if not empty
if pdc of clicked block isn't empty:
    send "This block contains custom data"
```

---

## ⚙️ Effects

### `Effect` Clear PDC
> `clear [all] pdc (of|from) %itemtypes/blocks/entities%`

**Description:** Removes all PDC tags from an object.

**💡 Examples:**
```skript
# Clear all tags from item
clear all pdc of player's tool

# Clear block data
clear pdc from clicked block
```

### `Effect` Remove PDC Tags
> `(remove|delete) pdc tag[s] %strings% (of|from) %itemtypes/blocks/entities%`

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
    if attacker's tool has pdc tag "weapon:level":
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
        
        if player's tool has pdc tag "soulbound:owner":
            send "&cThis item is already soulbound!"
            stop
            
        set pdc tag "soulbound:owner" of player's tool to player's uuid
        add "&dSoulbound to %player%" to lore of player's tool
        send "&aYour item is now soulbound to you!"

# Prevent other players from picking up a soulbound item
on pickup:
    if event-item has pdc tag "soulbound:owner":
        set {_ownerUUID} to pdc tag "soulbound:owner" of event-item
        if player's uuid is not {_ownerUUID}:
            send "&cThis item is soulbound to another player."
            cancel event
```

---

## 💾 Supported Data Types

SkriptPDC intelligently handles data types for you. When you set a tag, the addon automatically detects the type and stores it using the most appropriate format. When you retrieve the tag, it's returned as the correct Skript type.

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
# Storing a location
set pdc tag "homes:home1" of player to player's location
# ... later
set {_home} to pdc tag "homes:home1" of player
teleport player to {_home}
```

---

## 🔬 Testing & Debugging

To help you get started and verify that everything is working correctly, you can use the provided `pdctest.sk`. This file contains a suite of commands to test all features of the addon and monitor PDC data in real-time.

### Test Suite Command
The `/pdctest` command allows you to run various tests on an item you are holding.
*   `/pdctest all` - Run all tests
*   `/pdctest types` - Test different data types
*   `/pdctest conditions` - Test conditions
*   `/pdctest effects` - Test effects
*   `/pdctest math` - Test math operations
*   `/pdctest cleanup` - Clean up all test tags

### PDC Monitor Command
The `/pdcmonitor` command is an incredibly useful tool for debugging. Hold an item in your hand and run the command to see all of its PDC tags, values, and data types.

**`/pdcmonitor` Output Example:**
```
&6&l════════════════════════════════
&e&lPDC Monitor - Current Data
&6&l════════════════════════════════

&eTag count: &f2

&eAll tags:
  &7• &eweapon:level
    &7└─ &fValue: 5
    &7└─ &fType: integer

  &7• &eweapon:name
    &7└─ &fValue: Legendary Sword
    &7└─ &fType: text

&6&l════════════════════════════════
```

---

## ❓ FAQ & Troubleshooting

### My PDC data on an item isn't saving! Why?
This is a common issue related to how Minecraft handles item data (ItemMeta). When you modify an item's data, you are modifying a copy. You need to apply that copy back to the original item slot. SkriptPDC handles this for you when using `player's tool`, but if you're manipulating items in variables or inventories, you may need to manually update it.

```skript
# Correct way to update an item in a specific slot
set {_item} to slot 0 of player's inventory
set pdc tag "myplugin:tag" of {_item} to "value"
set slot 0 of player's inventory to {_item} # This line is crucial!
```

### What format should my tag string be?
Tags must be in the format `namespace:key`. For example, `myplugin:level`. The namespace (`myplugin`) prevents conflicts with other plugins. The key (`level`) is the specific name of your data. Using a unique namespace for your server or plugin is highly recommended.

### Can I store complex objects like a player or a location?
Yes. As mentioned in the Data Types section, SkriptPDC will automatically serialize any Skript type it doesn't handle natively. However, be aware that this stores a "snapshot" of the data at that moment. For things like players, it is often better to store their UUID as a string and retrieve the player object later.

---

<p align="center">
  SkriptPDC API v2.0 - Created by Fendi
</p>
