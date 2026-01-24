package me.fendi.skriptPDCAPI;

import org.bukkit.plugin.java.JavaPlugin;
import org.skriptlang.skript.addon.SkriptAddon;
import me.fendi.skriptPDCAPI.registration.PDCModule;

public final class SkriptPDC extends JavaPlugin {

    private static SkriptPDC instance;
    private static SkriptAddon addon;

    @Override
    public void onEnable() {
        instance = this;
        addon = ch.njol.skript.Skript.instance().registerAddon(getClass(), getName());
        addon.loadModules(new PDCModule());

        getLogger().info("╔══════════════════════════════╗");
        getLogger().info("║  SkriptPDC-API v3.0 Enabled  ║");
        getLogger().info("║  2.14 SUPPORT !!!            ║");
        getLogger().info("╚══════════════════════════════╝");
        logFeatures();
    }

    @Override
    public void onDisable() {
        getLogger().info("SkriptPDC-API has been disabled!");
    }

    private void logFeatures() {
        getLogger().info("Loaded features:");
        getLogger().info("  • Get/Set PDC tags with auto type detection");
        getLogger().info("  • List all PDC tags");
        getLogger().info("  • Check if tags exist");
        getLogger().info("  • Clear PDC data");
        getLogger().info("  • Add/Remove numeric values");
        getLogger().info("  • Support for Items, Entities AND Chunks");
    }

    public static SkriptPDC getInstance() {
        return instance;
    }

    public static SkriptAddon getAddonInstance() {
        return addon;
    }
}