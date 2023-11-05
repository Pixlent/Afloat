package me.pixlent.phasemachine;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerLoginEvent;

public final class LevelManager {
    private static LevelManager levelManager = null;

    private LevelManager() {
        handle(MinecraftServer.getGlobalEventHandler());
    }

    public static LevelManager hook() {
        if (levelManager == null) {
            levelManager = new LevelManager();
        }
        return levelManager;
    }

    private void handle(EventNode<Event> eventNode) {
        eventNode.addListener(PlayerLoginEvent.class, event -> {
            if (!(event.getSpawningInstance() instanceof Level level)) {
                return;
            }

            final var player = event.getPlayer();
            if (!(level.isJoinable())) {
                player.kick("The level instance isn't joinable");
            }
        });
    }
}
