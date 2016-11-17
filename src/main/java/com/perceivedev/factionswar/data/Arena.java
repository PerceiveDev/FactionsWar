package com.perceivedev.factionswar.data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.perceivedev.perceivecore.config.ConfigSerializable;

/**
 * @author Rayzr
 *
 */
public class Arena implements ConfigSerializable {

    private transient Set<UUID>               players        = new LinkedHashSet<UUID>();
    private transient HashMap<UUID, Location> originalSpawns = new LinkedHashMap<UUID, Location>();

    private String                            name;
    private Location                          spawn;

    Arena() {
        // Empty constructor for SerializationManager to use
    }

    public Arena(String name, Location spawn) {
        this.name = name;
        this.spawn = spawn;
    }

    /**
     * @return the name of the arena
     */
    public String getName() {
        return name;
    }

    /**
     * @return the spawn of the arena
     */
    public Location getSpawn() {
        return spawn;
    }

    /**
     * @param spawn the spawn of the arena to set
     */
    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public boolean hasPlayer(Player player) {
        return players.contains(player.getUniqueId());
    }

    public boolean removePlayer(Player player) {
        return players.remove(player.getUniqueId());
    }

    public boolean addPlayer(Player player) {
        return players.add(player.getUniqueId());
    }

    public Set<UUID> getPlayers() {
        return players;
    }

    public Optional<Player> getPlayer(UUID id) {
        return Optional.ofNullable(Bukkit.getPlayer(id));
    }

    /**
     * @return
     */
    public void kickAll() {
        players.forEach(id -> {
            getPlayer(id).ifPresent(player -> {
                Optional.ofNullable(originalSpawns.get(player)).ifPresent(loc -> {
                    player.teleport(loc);
                });
            });
        });
    }

}
