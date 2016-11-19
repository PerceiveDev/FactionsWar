package com.perceivedev.factionswar.data;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

/**
 * @author Rayzr
 *
 */
public class WarGroup {

    private List<UUID> players;

    public WarGroup(List<UUID> players) {
        this.players = players;
    }

    public WarGroup(UUID... players) {
        this(Arrays.asList(players));
    }

    /**
     * @return the players
     */
    public List<UUID> getPlayers() {
        return players;
    }

    public void forEach(Consumer<? super Player> action) {
        players.stream().map(Bukkit::getPlayer)
                .filter(Objects::nonNull).forEach(action);
    }

    /**
     * @return the name of this team/faction
     */
    public String getName() {
        String name = "N/A";
        if (players.size() > 0) {
            Optional<FPlayer> fplayer = Optional.ofNullable(FPlayers.i.get(Bukkit.getPlayer(players.get(0))));
            if (fplayer.isPresent()) {
                name = fplayer.get().getFaction().getTag();
            }
        }
        return name;
    }

    public void remove(UUID id) {
        players.remove(id);
    }

}
