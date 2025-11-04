package me.fendi.skriptPDCAPI.utils;

import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.util.slot.Slot;
import me.fendi.skriptPDCAPI.SkriptPDC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class PDCUtils {

    private static final Map<UUID, ItemMeta> INVENTORY_METADATA_STORAGE = new HashMap<>();
    private static final Map<Inventory, UUID> INVENTORY_UUID_MAP = new HashMap<>();

    @Nullable
    private static ItemMeta getOrCreateInventoryMeta(Inventory inventory) {
        UUID uuid = INVENTORY_UUID_MAP.computeIfAbsent(inventory, k -> UUID.randomUUID());

        return INVENTORY_METADATA_STORAGE.computeIfAbsent(uuid, k -> {
            ItemStack tempItem = new ItemStack(Material.STONE);
            ItemMeta meta = tempItem.getItemMeta();
            return meta;
        });
    }

    @Nullable
    public static PersistentDataContainer getContainer(Object holder) {
        return switch (holder) {
            case PersistentDataHolder pdh -> pdh.getPersistentDataContainer();
            case ItemType itemType -> {
                ItemMeta meta = itemType.getItemMeta();
                yield meta != null ? meta.getPersistentDataContainer() : null;
            }
            case ItemStack itemStack -> {
                if (!itemStack.hasItemMeta()) yield null;
                ItemMeta meta = itemStack.getItemMeta();
                yield meta != null ? meta.getPersistentDataContainer() : null;
            }
            case Slot slot -> {
                ItemStack item = slot.getItem();
                if (item == null || item.getType().isAir() || !item.hasItemMeta()) yield null;
                ItemMeta meta = item.getItemMeta();
                yield meta != null ? meta.getPersistentDataContainer() : null;
            }
            case Inventory inventory -> {
                ItemMeta meta = getOrCreateInventoryMeta(inventory);
                yield meta != null ? meta.getPersistentDataContainer() : null;
            }
            default -> null;
        };
    }

    public static void editPersistentDataContainer(Object holder, Consumer<PersistentDataContainer> consumer, boolean modify) {
        switch (holder) {
            case PersistentDataHolder persistentDataHolder ->
                    consumer.accept(persistentDataHolder.getPersistentDataContainer());
            case ItemType itemType -> {
                ItemMeta meta = itemType.getItemMeta();
                if (meta == null) return;
                consumer.accept(meta.getPersistentDataContainer());
                if (modify) itemType.setItemMeta(meta);
            }
            case ItemStack itemStack -> {
                ItemMeta meta = itemStack.hasItemMeta() ?
                        itemStack.getItemMeta() :
                        Bukkit.getItemFactory().getItemMeta(itemStack.getType());
                if (meta == null) return;
                consumer.accept(meta.getPersistentDataContainer());
                if (modify) itemStack.setItemMeta(meta);
            }
            case Slot slot -> {
                ItemStack item = slot.getItem();
                if (item == null || item.getType().isAir()) return;
                ItemMeta meta = item.hasItemMeta() ?
                        item.getItemMeta() :
                        Bukkit.getItemFactory().getItemMeta(item.getType());
                if (meta == null) return;
                consumer.accept(meta.getPersistentDataContainer());
                if (modify) {
                    item.setItemMeta(meta);
                    slot.setItem(item);
                }
            }
            case Inventory inventory -> {
                ItemMeta meta = getOrCreateInventoryMeta(inventory);
                if (meta != null) {
                    consumer.accept(meta.getPersistentDataContainer());
                }
            }
            default -> {}
        }
    }

    public static void clearInventoryPDC(Inventory inventory) {
        UUID uuid = INVENTORY_UUID_MAP.remove(inventory);
        if (uuid != null) {
            INVENTORY_METADATA_STORAGE.remove(uuid);
        }
    }

    public static void clearAllInventoryPDC() {
        INVENTORY_UUID_MAP.clear();
        INVENTORY_METADATA_STORAGE.clear();
    }

    @Nullable
    public static NamespacedKey createKey(String keyString) {
        if (keyString == null) return null;
        if (keyString.contains(":")) {
            return NamespacedKey.fromString(keyString);
        } else {
            return new NamespacedKey(SkriptPDC.getInstance(), keyString);
        }
    }
}