
package com.perceivedev.factionswar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.perceivedev.factionswar.utils.IconMenu;

public class CommandHandler implements CommandExecutor {

	FactionsWar									core;
	private static IconMenu						menu;
	public static HashMap<String, List<UUID>>	queued	= new HashMap<String, List<UUID>>();

	public CommandHandler(FactionsWar instance) {
		core = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("faction")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("war")) {
					menu = new IconMenu(ChatColor.RED + "FactionsWar", 27, InventoryHandler::onItemClick, core);
					List<UUID> found = new ArrayList<UUID>();

					if (getFaction((Player) sender).getFPlayerLeader().getPlayer() == (Player) sender) {
						for (int i = 0; i != Bukkit.getOnlinePlayers().size(); i++) {
							for (Player p : Bukkit.getOnlinePlayers()) {
								if (!(p == (Player) sender)) {
									if (!found.contains(p.getUniqueId())) {
										if (getFaction(p) == getFaction((Player) sender)) {
											sender.sendMessage(p.getName());
											Player p1 = p.getPlayer();

											if (p1 == sender) {
												continue;
											}

											ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
											SkullMeta meta = (SkullMeta) skull.getItemMeta();
											meta.setOwner(p1.getName());
											skull.setItemMeta(meta);

											menu.setOption(i, skull, p1.getName());
											found.add(p1.getUniqueId());
										}
									}
								}
							}
						}

						menu.open((Player) sender);
					}
				}
			}
		}

		// if (cmd.getName().equalsIgnoreCase("faction")) {
		// if (sender instanceof Player) {
		// Player p = (Player) sender;
		//
		// p.sendMessage(ChatColor.GREEN + "Hi");
		// return true;
		// }
		// }
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
