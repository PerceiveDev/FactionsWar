/**
 * 
 */
package com.perceivedev.factionswar.gui;

import java.util.List;
import java.util.stream.Collectors;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.perceivedev.factionswar.FactionsWar;
import com.perceivedev.perceivecore.gui.Gui;
import com.perceivedev.perceivecore.gui.components.panes.FlowPane;

/**
 * @author Rayzr
 *
 */
public class WarGui extends Gui {

    private Faction  faction;
    private FlowPane players;

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

        FPlayer[] fplayers = faction.getFPlayers().toArray(new FPlayer[0]);
        // This makes sure not to go over the size of the FlowPane
        for (int i = 0; i < Math.min(fplayers.length, 45); i++) {
            FPlayer player = fplayers[i];
            players.addComponent(new PlayerButton(player, this));
        }

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
