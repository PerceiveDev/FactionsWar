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
import com.perceivedev.perceivecore.util.snapshots.Snapshot;

/**
 * @author Rayzr
 *
 */
public class Arena implements ConfigSerializable {

    private transient Set<UUID>                       players   = new LinkedHashSet<>();
    private transient HashMap<UUID, Snapshot<Player>> snapshots = new LinkedHashMap<>();

    private String                                    name;
    private Location                                  spawn;

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
            kick(id);
        });
        players.clear();
        snapshots.clear();
    }

    public void join(Player player) {
        players.add(player.getUniqueId());
    }

    public void start() {
        players.stream().forEach(id -> {
            getPlayer(id).ifPresent(player -> {
                snapshots.put(id, Snapshot.ofPlayer(player));
                // --------------- CLEAR THEIR DATA ---------------
                player.getInventory().setStorageContents(null);
                player.getActivePotionEffects().clear();
                player.setHealth(player.getMaxHealth());
                player.setFoodLevel(20);
                player.setFireTicks(0);
                player.setFallDistance(0.0f);
                // ------------------------------------------------
                player.teleport(spawn);
            });
        });
    }

    public void stop() {
        kickAll();
    }

    /**
     * @param id The UUID of the player to kick
     */
    public void kick(UUID id) {
        if (!players.contains(id)) {
            return;
        }

        getPlayer(id).ifPresent(player -> {
            Optional.ofNullable(snapshots.get(player)).ifPresent(snapshot -> {
                snapshot.restore(player);
                snapshots.remove(id);
            });
        });
    }

}
