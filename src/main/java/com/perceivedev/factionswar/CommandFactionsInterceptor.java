/**
 * 
 */
package com.perceivedev.factionswar;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.perceivedev.factionswar.data.Arena;
import com.perceivedev.factionswar.gui.WarGui;

/**
 * @author Rayzr
 *
 */
public class CommandFactionsInterceptor implements CommandExecutor {

    private CommandExecutor executor;
    private FactionsWar     plugin;

    /**
     * @param executor the current command executor
     * @param plugin the {@link FactionsWar} instance
     */
    public CommandFactionsInterceptor(CommandExecutor executor, FactionsWar plugin) {
        this.executor = executor;
        this.plugin = plugin;
    }

    /**
     * Shifts off the first item in an array
     * 
     * @param input The input array
     * @return The shifted array
     */
    private String[] shift(String... input) {
        return input.length < 1 ? input : (Arrays.copyOfRange(input, 1, input.length));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && args.length > 0) {

            Player player = (Player) sender;
            String sub = args[0].toLowerCase();

            if (args[0].equals("reload")) {
                plugin.reload();
                player.sendMessage(plugin.tr("command.reload.reloaded"));
            } else if (sub.equals("war")) {
                commandWar(player, shift(args));
                return true;
            } else if (sub.equals("setarena")) {
                commandSetArena(player, shift(args));
                return true;
            } else if (sub.equals("arenatp")) {
                commandArenaTP(player, shift(args));
                return true;
            }

        }

        return executor.onCommand(sender, command, label, args);
    }

    private void commandWar(Player player, String[] args) {
        FPlayer p = FPlayers.i.get(player);
        if (p == null) {
            player.sendMessage(plugin.tr("player.not.in.faction"));
            return;
        }
        new WarGui(p.getFaction()).open(player);
    }

    private void commandSetArena(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(plugin.tr("command.setarena.usage"));
            return;
        }

        Arena arena = plugin.getArenaManager().getArena(args[0]);
        if (arena == null) {
            arena = plugin.getArenaManager().addArena(new Arena(args[0], player.getLocation()));
        } else {
            arena.setSpawn(player.getLocation());
        }

        player.sendMessage(plugin.tr("command.setarena.set", args[0], player.getLocation().toVector().toString()));

    }

    private void commandArenaTP(Player player, String[] args) {
        // TODO: Teleport to the arena
    }

}
