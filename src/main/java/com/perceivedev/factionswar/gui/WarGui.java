/**
 * 
 */
package com.perceivedev.factionswar.gui;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.perceivedev.factionswar.FactionsWar;
import com.perceivedev.factionswar.data.WarGroup;
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

        FPlayer[] fplayers = faction.getFPlayers().stream().filter(p -> p.isOnline()).collect(Collectors.toList()).toArray(new FPlayer[0]);
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

        List<Player> players = selected.stream()
                .map(button -> Bukkit.getPlayer(UUID.fromString(button.getPlayer().getId())))
                .filter(Objects::nonNull).collect(Collectors.toList());

        if (selected.size() == 3) {
            FactionsWar plugin = FactionsWar.getInstance();
            close();
            if (players.size() < 3) {
                selected.forEach(p -> p.getPlayer().sendMessage(plugin.tr("war.cancelled", plugin.tr("war.cancelled.reason.offline"))));
                return;
            }
            players.forEach(p -> p.sendMessage(plugin.tr("war.queue")));
            plugin.getWarManager().add(new WarGroup(players.stream().map(p -> p.getUniqueId()).collect(Collectors.toList())));
        }
    }

}
