/**
 * 
 */
package com.perceivedev.factionswar.gui;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import com.massivecraft.factions.Faction;
import com.perceivedev.factionswar.FactionsWar;
import com.perceivedev.perceivecore.gui.Gui;
import com.perceivedev.perceivecore.gui.components.panes.FlowPane;
import com.perceivedev.perceivecore.util.ItemFactory;

/**
 * @author Rayzr
 *
 */
public class WarGui extends Gui {

    public static final ItemFactory PLAYER_ICON          = ItemFactory.builder(Material.SKULL_ITEM).setDurability((short) 3);
    public static final ItemFactory PLAYER_ICON_SELECTED = PLAYER_ICON.clone().addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);

    private Faction                 faction;
    private FlowPane                players;

    /**
     * 
     */
    public WarGui(Faction faction) {
        super(FactionsWar.getInstance().tr("gui.title"), 6, new FlowPane(9, 6));
        this.faction = faction;
        init();
    }

    /**
     * Adds all UI elements to the GUI
     */
    private void init() {
        players = new FlowPane(9, 5);
        faction.getFPlayers().forEach(player -> {
            players.addComponent(new PlayerButton(player, this));
        });
        addComponent(players);
    }

    public void checkPlayerCount() {
        List<PlayerButton> selected = players.getChildren().stream()
                .filter(c -> c instanceof PlayerButton).map(c -> (PlayerButton) c)
                .filter(c -> c.isSelected()).collect(Collectors.toList());

        if (selected.size() == 3) {
            selected.forEach(p -> p.getPlayer().sendMessage("&cYou have been selected for a war!"));
            close();
            // TODO: Add to queue
        }
    }

}
