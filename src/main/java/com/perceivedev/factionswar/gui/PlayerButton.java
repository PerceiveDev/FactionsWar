/**
 * 
 */
package com.perceivedev.factionswar.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

import com.massivecraft.factions.FPlayer;
import com.perceivedev.factionswar.FactionsWar;
import com.perceivedev.perceivecore.gui.ClickEvent;
import com.perceivedev.perceivecore.gui.components.simple.SimpleButton;

/**
 * @author Rayzr
 *
 */
public class PlayerButton extends SimpleButton {

    private FPlayer player;
    private WarGui  gui;

    private boolean selected = false;

    /**
     * @param player
     */
    public PlayerButton(FPlayer player, WarGui gui) {
        super("");
        this.player = player;
        this.gui = gui;
    }

    /**
     * @return If this button is selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected The selected value to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        gui.reRender();
    }

    /**
     * @return The player
     */
    public FPlayer getPlayer() {
        return player;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getClickType() == ClickType.RIGHT) {
            selected = false;
        } else {
            selected = !selected;
        }
        gui.reRender();
        gui.checkPlayerCount();
    }

    @Override
    public void render(Inventory inventory, Player ignored, int offsetX, int offsetY) {
        if (selected) {
            setDisplayType(c -> WarGui.PLAYER_ICON_SELECTED.clone().setSkullOwner(player.getName()));
            setName(FactionsWar.getInstance().tr("gui.head.name.selected", player.getName()));
        } else {
            setDisplayType(c -> WarGui.PLAYER_ICON.clone().setSkullOwner(player.getName()));
            setName(FactionsWar.getInstance().tr("gui.head.name", player.getName()));
        }
        super.render(inventory, ignored, offsetX, offsetY);
    }

}
