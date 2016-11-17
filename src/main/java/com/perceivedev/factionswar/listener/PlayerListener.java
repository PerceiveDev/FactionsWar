package com.perceivedev.factionswar.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.perceivedev.factionswar.FactionsWar;

/**
 * @author Rayzr
 *
 */
public class PlayerListener implements Listener {

    private FactionsWar plugin;

    public PlayerListener(FactionsWar plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        plugin.getArenaManager().getArenas().forEach(arena -> {
            arena.getPlayers().forEach(id -> {
                if (id.equals(e.getPlayer().getUniqueId())) {
                    arena.kick(id);
                    return;
                }
            });
        });
    }

}