
package com.perceivedev.factionswar;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.perceivedev.factionswar.utils.IconMenu;

public class CommandHandler implements CommandExecutor {

    FactionsWar             core;
    private static IconMenu menu;

    public CommandHandler(FactionsWar instance) {
        core = instance;

        menu = new IconMenu(ChatColor.RED + "FactionsWar", 27, InventoryHandler::onItemClick, instance);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // if (cmd.getName().equalsIgnoreCase("faction")) {
        // sender.sendMessage("working");
        // if (args.length == 1) {
        // sender.sendMessage("working");
        // if (args[0].equalsIgnoreCase("war")) {
        // for (Player p : Bukkit.getOnlinePlayers()) {
        // if (getFaction(p) == getFaction((Player) sender)) {
        // Player p1 = p.getPlayer();
        // ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short)
        // SkullType.PLAYER.ordinal());
        // SkullMeta meta = (SkullMeta) skull.getItemMeta();
        // meta.setOwner(p1.getName());
        // skull.setItemMeta(meta);
        //
        // for (int i = 0; i < 18; i++) {
        // menu.setOption(i, skull, p.getName());
        // }
        // }
        // }
        // menu.open((Player) sender);
        // sender.sendMessage(ChatColor.DARK_PURPLE + "HI");
        // }
        // }
        // }

        if (cmd.getName().equalsIgnoreCase("faction")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;

                p.sendMessage(ChatColor.GREEN + "Hi");
                return true;
            }
        }
        return true;
    }

    public Faction getFPlayerFaction(FPlayer p) {
        Faction fac = p.getFaction();

        return fac;
    }

    public static Faction getFaction(Player p) {
        FPlayer fPlayer = FPlayers.i.get(p);
        Faction fac = fPlayer.getFaction();

        return fac;
    }

    public static IconMenu getMenu() {
        return menu;
    }
}
