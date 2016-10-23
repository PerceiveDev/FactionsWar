
package me.endureblackout.factionswar;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.endureblackout.factionswar.utils.IconMenu.OptionClickEvent;

public class InventoryHandler implements Listener {
	FactionsWar core;
	
	public InventoryHandler(FactionsWar instance) {
		core = instance;
	}

	public static void onItemClick(OptionClickEvent e) {
		Player p = e.getPlayer();
		e.willClose();

	}
}