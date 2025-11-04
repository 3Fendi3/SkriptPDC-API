package me.fendi.skriptPDCAPI.listeners;

import me.fendi.skriptPDCAPI.utils.PDCUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCleanupListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        PDCUtils.clearInventoryPDC(event.getInventory());
    }
}