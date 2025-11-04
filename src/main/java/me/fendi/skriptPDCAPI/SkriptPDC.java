package me.fendi.skriptPDCAPI;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import me.fendi.skriptPDCAPI.listeners.InventoryCleanupListener;
import me.fendi.skriptPDCAPI.utils.PDCUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class SkriptPDC extends JavaPlugin {

    private static SkriptPDC instance;
    private static SkriptAddon addon;

    @Override
    public void onEnable() {
        instance = this;
        addon = Skript.registerAddon(this);

        try {
            addon.loadClasses("me.fendi.skriptPDCAPI", "expressions");
            addon.loadClasses("me.fendi.skriptPDCAPI", "effects");

            getServer().getPluginManager().registerEvents(
                    new InventoryCleanupListener(),
                    this
            );

            getLogger().info("╔══════════════════════════════╗");
            getLogger().info("║  SkriptPDC-API v2.1 Enabled  ║");
            getLogger().info("║  Enhanced PDC Support        ║");
            getLogger().info("╚══════════════════════════════╝");
            logFeatures();

        } catch (IOException e) {
            getLogger().severe("Failed to load SkriptPDC classes!");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        PDCUtils.clearAllInventoryPDC();
        getLogger().info("SkriptPDC-API has been disabled!");
    }

    private void logFeatures() {
        getLogger().info("Loaded features:");
        getLogger().info("  • Get/Set PDC tags with auto type detection");
        getLogger().info("  • List all PDC tags");
        getLogger().info("  • Check if tags exist");
        getLogger().info("  • Clear PDC data");
        getLogger().info("  • Add/Remove numeric values");
        getLogger().info("  • Support for Items, Blocks, Entities AND Inventories");
    }

    public static SkriptPDC getInstance() {
        return instance;
    }

    public static SkriptAddon getAddonInstance() {
        return addon;
    }
}