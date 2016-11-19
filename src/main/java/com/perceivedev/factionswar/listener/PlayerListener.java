package com.perceivedev.factionswar.listener;

import java.util.UUID;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
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
        UUID id = e.getPlayer().getUniqueId();
        plugin.getArenaManager().kick(id);
        plugin.getWarManager().getGroup(id).ifPresent(group -> {
            plugin.getWarManager().removeGroup(group, plugin.tr("war.cancelled.reason.logout", e.getPlayer().getName()));
        });
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        cancelIfPlaying(e, e.getPlayer());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        cancelIfPlaying(e, e.getPlayer());
    }

    @EventHandler
    public void onPlayerCraft(PrepareItemCraftEvent e) {
        if (e.getViewers().isEmpty()) {
            return;
        }
        HumanEntity entity = e.getViewers().get(0);
        if (!(entity instanceof Player)) {
            return;
        }
        Player player = (Player) entity;
        plugin.getArenaManager().getArena(player.getUniqueId()).ifPresent(a -> e.getInventory().setResult(null));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        UUID id = e.getEntity().getUniqueId();
        plugin.getArenaManager().getArena(id).ifPresent(a -> {
            a.kick(id);
            System.out.println((a.getPlayersOnTeam(1).size() < 1) + " || " + (a.getPlayersOnTeam(2).size() < 1));
            if (a.getPlayersOnTeam(1).size() < 1 || a.getPlayersOnTeam(2).size() < 1) {
                a.stop();
            }
        });
    }

    private void cancelIfPlaying(Cancellable e, Player player) {
        plugin.getArenaManager().getArena(player.getUniqueId()).ifPresent(a -> e.setCancelled(true));
    }

}
