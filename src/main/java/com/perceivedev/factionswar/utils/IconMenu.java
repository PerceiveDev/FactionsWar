package com.perceivedev.factionswar.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class IconMenu implements Listener {

    private String                  name;
    private int                     size;
    private OptionClickEventHandler handler;
    private Plugin                  plugin;
    private Player                  player;

    private String[]                optionNames;
    private ItemStack[]             optionIcons;

    public IconMenu(String name, int size, OptionClickEventHandler handler, Plugin plugin) {
        this.name = name;
        this.size = size;
        this.handler = handler;
        this.plugin = plugin;
        this.optionNames = new String[size];
        this.optionIcons = new ItemStack[size];
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public IconMenu setOption(int position, ItemStack icon, String name, String... info) {
        optionNames[position] = name;
        optionIcons[position] = setItemNameAndLore(icon, name, info);
        return this;
    }

    public IconMenu setOption(int position, boolean color, ItemStack icon, String name, String... info) {
        if (color) {
            optionNames[position] = ChatColor.translateAlternateColorCodes('&', name);
            optionIcons[position] = setItemNameAndLoreWithColor(icon, name, info);
        } else {
            optionNames[position] = name;
            optionIcons[position] = setItemNameAndLore(icon, name, info);
        }
        return this;
    }

    public IconMenu setOption(int position, ItemStack icon) {
        optionNames[position] = icon.getItemMeta().getDisplayName();
        optionIcons[position] = icon;
        return this;
    }

    public IconMenu setOptions(int[] positions, ItemStack icon) {
        for (int i : positions) {
            optionNames[i] = icon.getItemMeta().getDisplayName();
            optionIcons[0] = icon;
        }
        return this;
    }

    public void setSpecificTo(Player player) {
        this.player = player;
    }

    public boolean isSpecific() {
        return player != null;
    }

    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(player, size, name);
        for (int i = 0; i < optionIcons.length; i++) {
            if (optionIcons[i] != null) {
                inventory.setItem(i, optionIcons[i]);
            }
        }
        player.openInventory(inventory);
    }

    public void destroy() {
        HandlerList.unregisterAll(this);
        handler = null;
        plugin = null;
        optionNames = null;
        optionIcons = null;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals(name) && (player == null || event.getWhoClicked() == player)) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < size && optionNames[slot] != null) {
                Plugin plugin = this.plugin;
                OptionClickEvent e = new OptionClickEvent((Player) event.getWhoClicked(), slot, optionNames[slot], optionIcons[slot], event.getClick());
                handler.onOptionClick(e);
                ((Player) event.getWhoClicked()).updateInventory();
                if (e.willClose()) {
                    final Player p = (Player) event.getWhoClicked();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        public void run() {
                            p.closeInventory();
                        }
                    });
                }
                if (e.willDestroy()) {
                    destroy();
                }
            }
        }
    }

    public interface OptionClickEventHandler {
        public void onOptionClick(OptionClickEvent event);
    }

    public class OptionClickEvent {
        private Player    player;
        private int       position;
        private String    name;
        private boolean   close;
        private boolean   destroy;
        private ItemStack item;
        private ClickType click;

        public OptionClickEvent(Player player, int position, String name, ItemStack item, ClickType click) {
            this.player = player;
            this.position = position;
            this.name = name;
            this.close = true;
            this.destroy = false;
            this.item = item;
            this.click = click;
        }

        public Player getPlayer() {
            return player;
        }

        public int getPosition() {
            return position;
        }

        public String getName() {
            return name;
        }

        public boolean willClose() {
            return close;
        }

        public boolean willDestroy() {
            return destroy;
        }

        public void setWillClose(boolean close) {
            this.close = close;
        }

        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }

        public ItemStack getItem() {
            return item;
        }

        public ClickType getClick() {
            return click;
        }
    }

    private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        im.setLore(Arrays.asList(lore));
        item.setItemMeta(im);
        return item;
    }

    private ItemStack setItemNameAndLoreWithColor(ItemStack item, String name, String[] lore) {
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        List<String> newLores = new ArrayList<String>();
        for (String s : lore) {
            newLores.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        im.setLore(newLores);
        item.setItemMeta(im);
        return item;
    }

}