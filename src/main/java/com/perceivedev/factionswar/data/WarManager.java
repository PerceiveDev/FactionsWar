package com.perceivedev.factionswar.data;

import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;

import com.perceivedev.factionswar.FactionsWar;

/**
 * @author Rayzr
 *
 */
public class WarManager {

    private FactionsWar     plugin;

    private Queue<WarGroup> pendingWars = new ArrayDeque<>();

    public WarManager(FactionsWar plugin) {
        this.plugin = plugin;
    }

    /**
     * @return the queue of pending wars
     */
    public Queue<WarGroup> getPendingWars() {
        return pendingWars;
    }

    public Optional<WarGroup> getGroup(UUID player) {
        return pendingWars.stream().filter(group -> group.getPlayers().contains(player)).findFirst();
    }

    public void removeGroup(WarGroup group, String reason) {
        if (pendingWars.remove(group)) {
            group.forEach(p -> p.sendMessage(plugin.tr("war.cancelled", reason)));
        }
    }

    public void removeGroup(WarGroup group) {
        removeGroup(group, "");
    }

    public boolean add(UUID... players) {
        return add(new WarGroup(players));
    }

    public boolean add(WarGroup warGroup) {
        pendingWars.offer(warGroup);
        return refresh();
    }

    private boolean refresh() {
        Optional<Arena> arena = plugin.getArenaManager().getRandomArena();
        if (!arena.isPresent()) {
            pendingWars.forEach(war -> war.forEach(p -> {
                p.sendMessage(plugin.tr("arenas.none"));
            }));
            pendingWars.clear();
            return false;
        }

        if (pendingWars.size() >= 2) {
            WarGroup team1 = pendingWars.poll();
            WarGroup team2 = pendingWars.poll();
            if (Math.round(Math.random()) == 1L) {
                team1.getPlayers().forEach(p -> arena.get().join(p, 1));
                team2.getPlayers().forEach(p -> arena.get().join(p, 2));
            } else {
                team1.getPlayers().forEach(p -> arena.get().join(p, 2));
                team2.getPlayers().forEach(p -> arena.get().join(p, 1));
            }
            team1.forEach(p -> p.sendMessage(plugin.tr("war.starting", team2.getName())));
            team2.forEach(p -> p.sendMessage(plugin.tr("war.starting", team1.getName())));
            arena.get().start();
        }

        return false;
    }

}
