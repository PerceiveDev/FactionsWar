package com.perceivedev.factionswar.data;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.perceivedev.factionswar.FactionsWar;
import com.perceivedev.perceivecore.config.util.DataFileManager;

/**
 * @author Rayzr
 *
 */
public class ArenaManager {

    @SuppressWarnings("unused")
    private FactionsWar                    plugin;
    private DataFileManager<String, Arena> arenas;

    public ArenaManager(FactionsWar plugin) {
        this.plugin = plugin;
        arenas = new DataFileManager<>(plugin.getDataFolder().toPath().resolve("arenas.yml"), String.class, Arena.class, new LinkedHashMap<>());
    }

    /**
     * Kicks all players from their current matches and then loads all arenas
     * from the config
     */
    public void load() {
        arenas.values().forEach(arena -> arena.kickAll());
        arenas.load();
    }

    public void save() {
        arenas.save();
    }

    /**
     * Adds an arena
     * 
     * @param arena The arena to add
     * @return {@code false} if there was already an arena present with that
     *         name, {@code true} if the arena was successfully added.
     */
    public Arena addArena(Arena arena) {
        return arenas.put(arena.getName(), arena);
    }

    public Optional<Arena> getArena(String name) {
        return Optional.ofNullable(arenas.get(name));
    }

    /**
     * Gets the arena the player is currently in
     * 
     * @param player the player
     * @return The {@link Arena} the player is currently in, or {@code null}
     */
    public Optional<Arena> getArena(UUID player) {
        return arenas.values().stream().filter(arena -> arena.getPlayers().contains(player))
                .findFirst();
    }

    public Collection<Arena> getArenas() {
        return arenas.values();
    }

    /**
     * Kicks a player if they're in an arena
     * 
     * @param player the player to kick
     */
    public void kick(UUID player) {
        getArena(player).ifPresent(a -> a.kick(player));
    }

    /**
     * @param name the name of the arena
     * @return If anything was removed, this returns {@code true}. Otherwise it
     *         returns {@code false}.
     */
    public boolean removeArena(String name) {
        return arenas.remove(name) != null;
    }

    public Optional<Arena> getRandomArena() {
        int place = (int) (Math.random() * (arenas.size() - 1));
        List<Arena> available = arenas.values().stream().filter(arena -> arena.getPlayers().size() < 1).collect(Collectors.toList());
        return available.size() < 1 ? Optional.empty() : Optional.of(available.toArray(new Arena[0])[place]);
    }

}
