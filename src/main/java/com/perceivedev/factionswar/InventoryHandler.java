
package com.perceivedev.factionswar;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.perceivedev.factionswar.utils.IconMenu.OptionClickEvent;

public class InventoryHandler implements Listener {

    FactionsWar core;

    public InventoryHandler(FactionsWar instance) {
        core = instance;
    }

    public static void onItemClick(OptionClickEvent e) {
        Player p = e.getPlayer();

        if (e.getItem().getType().equals(Material.SKULL_ITEM)) {
            int num = 0;
            e.setWillClose(false);

            ItemStack skull = e.getItem();
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            List<UUID> selected = new ArrayList<UUID>();

            for (Player s : Bukkit.getOnlinePlayers()) {
                if (s.getName().equalsIgnoreCase(meta.getOwner())) {
                    if (!(num == 3) && !(selected.contains(s.getUniqueId()))) {
                        selected.add(s.getUniqueId());
                        p.sendMessage(ChatColor.GREEN + "The player " + s.getName() + " has been added to the war");
                        meta.setOwner(ChatColor.RED + s.getName());
                        skull.setItemMeta(meta);
                        num++;
                    } else {
                        if (selected.contains(s.getUniqueId())) {
                            p.sendMessage(ChatColor.RED + "That player is already in the war!");
                        }
                    }
                }
            }
        }
    }
}