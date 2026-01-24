<p align="center">
  <h1 align="center">SkriptPDC API</h1>
  <p align="center">
    <strong>Version 3.0</strong>
  </p>
  <p align="center">
    <em>Modern Persistent Data Container API for Skript 2.14+</em>
  </p>
</p>

---

## 📚 Overview

SkriptPDC is a powerful addon for Skript that provides comprehensive support for Minecraft's Persistent Data Container (PDC) system. Store custom data on **items, entities, and chunks** with automatic type detection and modern Skript 2.14 API support.

### What's New in v3.0?
*   **🚀 Skript 2.14 API**: Fully modernized using Skript's new registration system for better performance and stability.
*   **🌍 Chunk Support**: Store persistent data directly on chunks for region-based systems and world management.
*   **🎯 Auto Type Detection**: Automatically handles Integer, Double, Float, Long, Byte, String, Boolean, and complex Skript types.
*   **📋 Tag Management**: List, check, clear, and manipulate PDC tags with intuitive expressions and effects.
*   **➕ Math Operations**: Add and remove numeric values directly from PDC tags without manual calculations.
*   **⚡ Optimized Performance**: Enhanced error handling and efficient code for production servers.

---

## 🚀 Installation & Setup

1.  **Requirements**: Ensure you have **Skript 2.14+** installed on your server.
2.  **Download**: Get the latest version of `SkriptPDC.jar`.
3.  **Install**: Place the downloaded `.jar` file into your server's `/plugins/` directory.
4.  **Restart**: Restart your server completely.
5.  **Verify**: Check your server console for the startup message:

```
╔══════════════════════════════╗
║  SkriptPDC-API v3.0 Enabled  ║
║  Full Modern 2.14 API        ║
╚══════════════════════════════╝
```

---

## 📝 Expressions

### `Expression` PDC Tag
> `pdc tag %string% of %objects%`

**Description:** Gets or sets a PDC tag value. Supports `get`, `set`, `delete`, `add`, and `remove` operations. Works with items, entities, and chunks.

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

# Math operations
add 1 to pdc tag "myplugin:kills" of player
remove 10 from pdc tag "myplugin:mana" of player

# Chunk support
set pdc tag "region:protected" of chunk at player to true
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
    
# Get chunk tags
set {_chunkTags::*} to pdc tags of chunk at player
```

---

## 📝 Checking Tags (Conditions)

### `Condition` Check if Tag Exists
> `pdc tag %string% of %objects% is set`
> `pdc tag %string% of %objects% is not set`

**Description:** The standard Skript way to check if an object has a specific PDC tag.

**💡 Examples:**
```skript
# Check if an item tag exists
if pdc tag "myplugin:special" of player's tool is set:
    send "This is a special item!"

# Check if a chunk tag is true
if pdc tag "region:protected" of chunk at event-block is true:
    cancel event
```

### `Condition` Check if PDC is Empty
> `size of all pdc tags of %objects% is 0`

**Description:** Checks if an object's PDC is empty (contains no tags).

**💡 Examples:**
```skript
# Check if an item's PDC is empty
if size of all pdc tags of player's tool is 0:
    send "This item has no custom data"
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

# Clear chunk data
clear pdc from chunk at player
```

### `Effect` Remove PDC Tags
> `(remove|delete) pdc tag[s] %strings% (of|from) %objects%`

**Description:** Removes one or more specific PDC tags from an object.

**💡 Examples:**
```skript
# Remove single tag
remove pdc tag "myplugin:temp" of player's tool

# Remove multiple tags
remove pdc tags "myplugin:level", "myplugin:exp" from player's tool
```

---

## 💡 Usage Examples

### Example 1: Item Leveling System
```skript
command /levelitem:
    trigger:
        give player diamond sword named "&6Legendary Sword"
        set pdc tag "weapon:level" of player's tool to 1
        set pdc tag "weapon:exp" of player's tool to 0

on death:
    if attacker is a player:
        if pdc tag "weapon:level" of attacker's tool is set:
            add 25 to pdc tag "weapon:exp" of attacker's tool
            
            set {_exp} to pdc tag "weapon:exp" of attacker's tool
            set {_level} to pdc tag "weapon:level" of attacker's tool
            if {_exp} >= {_level} * 100:
                add 1 to pdc tag "weapon:level" of attacker's tool
                set pdc tag "weapon:exp" of attacker's tool to 0
                send "&aLevel Up! Your weapon is now level %{_level} + 1%!" to attacker
```

### Example 2: Soulbound Items
```skript
command /soulbind:
    trigger:
        if player's tool is air:
            send "&cYou must hold an item!"
            stop
        
        if pdc tag "soulbound:owner" of player's tool is set:
            send "&cThis item is already soulbound!"
            stop
            
        set pdc tag "soulbound:owner" of player's tool to player's uuid as string
        send "&aYour item is now soulbound!"

on pickup:
    if pdc tag "soulbound:owner" of event-item is set:
        set {_ownerUUID} to pdc tag "soulbound:owner" of event-item
        if player's uuid as string is not {_ownerUUID}:
            cancel event
            send "&cThis item belongs to someone else!"
```

### Example 3: Protected Regions with Chunks
```skript
command /protectchunk:
    trigger:
        set {_chunk} to chunk at player
        set pdc tag "region:protected" of {_chunk} to true
        set pdc tag "region:owner" of {_chunk} to player's uuid as string
        send "&aChunk protected!"

on break:
    set {_chunk} to chunk at event-block
    if pdc tag "region:protected" of {_chunk} is true:
        set {_owner} to pdc tag "region:owner" of {_chunk}
        if player's uuid as string is not {_owner}:
            cancel event
            send "&cThis chunk is protected!"
```

### Example 4: Player Statistics
```skript
on death of player:
    add 1 to pdc tag "stats:deaths" of victim

on death:
    if attacker is a player:
        add 1 to pdc tag "stats:kills" of attacker

command /stats:
    trigger:
        set {_kills} to pdc tag "stats:kills" of player ? 0
        set {_deaths} to pdc tag "stats:deaths" of player ? 0
        send "&eKills: &f%{_kills}% &7| &eDeaths: &f%{_deaths}%"
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
| Boolean         | `boolean`         | `true` / `false`  |

### Complex Data Types
For any other Skript data type (like `location`, `itemstack`, etc.), SkriptPDC will use Skript's built-in serialization system. This converts the object into text, stores it, and deserializes it back into an object when you retrieve it.

```skript
# Storing a location on a player
set pdc tag "homes:home1" of player to player's location
# ... later
set {_home} to pdc tag "homes:home1" of player
teleport player to {_home}
```

> **Important:** When storing complex objects like players, it's recommended to store their UUID as text (`player's uuid as string`) instead of the player object itself, as player objects are temporary.

---

## ❓ FAQ & Troubleshooting

### How do I use namespaced keys?
Tags should ideally be in the format `namespace:key` (e.g., `myplugin:level`). The namespace prevents conflicts with other plugins. If you provide a key without a namespace (e.g., `"level"`), SkriptPDC will automatically use your script's name as the namespace.

### Why isn't my item data saving?
When manipulating items stored in **variables** or **inventory slots**, you must manually apply the modified item back to its original place. Modifying `player's tool` handles this automatically.

```skript
# Correct way to update an item in a specific slot
set {_item} to slot 0 of player's inventory
set pdc tag "myplugin:tag" of {_item} to "value"
set slot 0 of player's inventory to {_item} # This line is crucial!
```

### What objects support PDC?
SkriptPDC supports all objects that implement Bukkit's `PersistentDataHolder` interface:
*   **Items**: `itemstack`, `itemtype`, inventory slots
*   **Entities**: All entity types (Players, Mobs, Armor Stands, etc.)
*   **Chunks**: World chunks for region-based data

### How do I check if a tag exists?
The best way is to use Skript's built-in `is set` condition.
```skript
if pdc tag "mydata" of player's tool is set:
    send "Tag exists!"
else:
    send "Tag does not exist."
```

### Can I store lists or multiple values?
PDC stores single values per tag. For lists, you have two main options:
1.  **Use Multiple Tags**: Create a naming convention like `homes:1`, `homes:2`, etc.
2.  **Serialize the List**: Join the list into a single string with a delimiter, and split it when you retrieve it. Skript handles this automatically if you set a list variable to a tag.

```skript
# Skript's serialization handles this automatically
set {_friends::*} to "Player1", "Player2" and "Player3"
set pdc tag "friends:list" of player to {_friends::*}

# Later...
set {_retrievedFriends::*} to pdc tag "friends:list" of player
# {_retrievedFriends::*} is now a list again
```

---

<p align="center">
  SkriptPDC API v3.0 - Created by Fendi
</p>
<p align="center" style="font-size: 0.9em;">
  <i>For convenience, the docs was generated by AI, I apologize for any errors in the examples.</i>
</p>
