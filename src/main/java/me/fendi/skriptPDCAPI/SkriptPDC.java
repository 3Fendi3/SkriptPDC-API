package me.fendi.skriptPDCAPI;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
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

            addon.loadClasses("me.fendi.skriptPDCAPI", "conditions");

            addon.loadClasses("me.fendi.skriptPDCAPI", "effects");

            getLogger().info("SkriptPDC-API v2.0 has been enabled!");
        } catch (IOException e) {
            getLogger().severe("Failed to load SkriptPDC classes!");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("SkriptPDC-API has been disabled!");
    }

    public static SkriptPDC getInstance() {
        return instance;
    }

    public static SkriptAddon getAddonInstance() {
        return addon;
    }
}