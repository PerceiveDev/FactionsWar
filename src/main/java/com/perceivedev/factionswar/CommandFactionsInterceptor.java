/**
 * 
 */
package com.perceivedev.factionswar;

import java.util.Arrays;
import java.util.Optional;

import org.bukkit.Location;
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
            } else if (sub.equals("createarena")) {
                commandCreateArena(player, shift(args));
                return true;
            } else if (sub.equals("removearena")) {
                commandRemoveArena(player, shift(args));
                return true;
            } else if (sub.equals("setarenaspawn")) {
                commandSetArenaSpawn(player, shift(args));
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

    private void commandCreateArena(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(plugin.tr("command.createarena.usage"));
            return;
        }

        if (!plugin.getArenaManager().getArena(args[0]).isPresent()) {
            plugin.getArenaManager().addArena(new Arena(args[0]));
            player.sendMessage(plugin.tr("command.createarena.created", args[0]));
        } else {
            player.sendMessage(plugin.tr("arena.already.exists"));
        }

    }

    private void commandRemoveArena(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(plugin.tr("command.removearena.usage"));
            return;
        }

        if (plugin.getArenaManager().removeArena(args[0])) {
            player.sendMessage(plugin.tr("command.removearena.removed", args[0]));
        } else {
            player.sendMessage(plugin.tr("arena.invalid"));
        }

    }

    private void commandSetArenaSpawn(Player player, String[] args) {
        if (args.length < 2 || !args[1].matches("^\\d$")) {
            player.sendMessage(plugin.tr("command.setarenaspawn.usage"));
            return;
        }

        Optional<Arena> arena = plugin.getArenaManager().getArena(args[0]);
        if (!arena.isPresent()) {
            player.sendMessage(plugin.tr("arena.invalid", args[0]));
            return;
        }

        int i = Integer.parseInt(args[1]);
        if (i < 1 || i > 2) {
            player.sendMessage(plugin.tr("command.setarenaspawn.usage"));
            return;
        }

        Location loc = player.getLocation();

        switch (i) {
            case 1:
                arena.get().setSpawn1(loc);
                break;
            case 2:
                arena.get().setSpawn2(loc);
                break;
            default:
                throw new IllegalStateException("Team number is invalid? " + i);
        }

        player.sendMessage(plugin.tr("command.setarenaspawn.set" + i, args[0], loc.toVector().toBlockVector().toString()));

    }

    private void commandArenaTP(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(plugin.tr("command.arenatp.usage"));
            return;
        }

        Optional<Arena> arena = plugin.getArenaManager().getArena(args[0]);
        if (!arena.isPresent()) {
            player.sendMessage(plugin.tr("arena.invalid", args[0]));
            return;
        }

        int i = 1;
        if (args.length > 1 && args[1].matches("^\\d$")) {
            i = Integer.parseInt(args[1]);
            if (i < 1 || i > 2) {
                player.sendMessage(plugin.tr("command.arenatp.usage"));
                return;
            }
        }

        player.teleport(i == 1 ? arena.get().getSpawn1() : arena.get().getSpawn2());
        player.sendMessage(plugin.tr("command.arenatp.teleported", i, args[0]));
    }

}
