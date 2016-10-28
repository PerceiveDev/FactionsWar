
package com.perceivedev.factionswar;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class FactionsWar extends JavaPlugin {

    public void onEnable() {

        getCommand("faction").setExecutor(new CommandHandler(this));
        Bukkit.getPluginManager().registerEvents(new InventoryHandler(this), this);
    }
}
