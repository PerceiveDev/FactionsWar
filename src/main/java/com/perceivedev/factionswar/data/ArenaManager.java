package com.perceivedev.factionswar.data;

import java.util.Collection;
import java.util.LinkedHashMap;

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

    public Arena getArena(String name) {
        return arenas.get(name);
    }

    public Collection<Arena> getArenas() {
        return arenas.values();
    }

}
