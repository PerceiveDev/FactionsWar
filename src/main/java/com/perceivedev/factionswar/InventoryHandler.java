
package com.perceivedev.factionswar;

import org.bukkit.event.Listener;

import com.perceivedev.factionswar.utils.IconMenu.OptionClickEvent;

public class InventoryHandler implements Listener {
    FactionsWar core;

    public InventoryHandler(FactionsWar instance) {
        core = instance;
    }

    public static void onItemClick(OptionClickEvent e) {
        // Player p = e.getPlayer();
        e.willClose();

    }
}