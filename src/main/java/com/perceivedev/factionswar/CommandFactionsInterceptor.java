/**
 * 
 */
package com.perceivedev.factionswar;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Rayzr
 *
 */
public class CommandFactionsInterceptor implements CommandExecutor {

    private CommandExecutor executor;
    @SuppressWarnings("unused")
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

        if (sender instanceof Player && args.length > 1) {

            Player player = (Player) sender;
            String sub = args[0].toLowerCase();

            if (sub.equals("war")) {
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
        // TODO: Open war GUI
    }

    private void commandSetArena(Player player, String[] args) {
        // TODO: Set the arena spawn point
    }

    private void commandArenaTP(Player player, String[] args) {
        // TODO: Teleport to the arena
    }

}
