package com.perceivedev.factionswar.data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    private transient HashMap<UUID, Integer>          players   = new LinkedHashMap<>();
    private transient HashMap<UUID, Snapshot<Player>> snapshots = new LinkedHashMap<>();

    private String                                    name;
    private Location                                  spawn1;
    private Location                                  spawn2;

    Arena() {
        // Empty constructor for SerializationManager to use
    }

    public Arena(String name) {
        this.name = name;
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
    public Location getSpawn1() {
        return spawn1;
    }

    /**
     * @param spawn1 the spawn of the arena to set for team 1
     */
    public void setSpawn1(Location spawn1) {
        this.spawn1 = spawn1;
    }

    /**
     * @return the spawn of the arena
     */
    public Location getSpawn2() {
        return spawn2;
    }

    /**
     * @param spawn2 the spawn of the arena to set for team 2
     */
    public void setSpawn2(Location spawn2) {
        this.spawn2 = spawn2;
    }

    public boolean hasPlayer(Player player) {
        return players.containsKey(player.getUniqueId());
    }

    public boolean removePlayer(Player player) {
        return players.remove(player.getUniqueId()) != null;
    }

    public Set<UUID> getPlayers() {
        return players.keySet();
    }

    public Optional<Player> getPlayer(UUID id) {
        return Optional.ofNullable(Bukkit.getPlayer(id));
    }

    /**
     * @return
     */
    public void kickAll() {
        players.keySet().forEach(id -> {
            kick(id);
        });
        players.clear();
        snapshots.clear();
    }

    public boolean join(UUID player, int team) {
        return players.containsKey(player) ? false : players.put(player, team) == null;
    }

    public void start() {
        players.entrySet().stream().forEach(e -> {
            getPlayer(e.getKey()).ifPresent(player -> {
                snapshots.put(e.getKey(), Snapshot.ofPlayer(player));
                // --------------- CLEAR THEIR DATA ---------------
                player.getInventory().setStorageContents(null);
                player.getActivePotionEffects().clear();
                player.setHealth(player.getMaxHealth());
                player.setFoodLevel(20);
                player.setFireTicks(0);
                player.setFallDistance(0.0f);
                // ------------------------------------------------
                player.teleport(e.getValue() == 1 ? spawn1 : spawn2);
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
        if (!players.containsKey(id)) {
            return;
        }

        getPlayer(id).ifPresent(player -> {
            Optional.ofNullable(snapshots.get(player)).ifPresent(snapshot -> {
                snapshot.restore(player);
                snapshots.remove(id);
            });
        });
        players.remove(id);
    }

    public List<UUID> getPlayersOnTeam(int i) {
        return players.entrySet().stream().filter(e -> e.getValue() == i).map(e -> e.getKey()).collect(Collectors.toList());
    }

}
