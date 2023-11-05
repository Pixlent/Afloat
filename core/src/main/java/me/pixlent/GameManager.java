package me.pixlent;

import me.pixlent.phasemachine.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameManager {
    private static GameManager gameManager = null;
    private final Map<UUID, Level> games = new HashMap<>();

    private GameManager() {}

    public static GameManager hook() {
        if (gameManager == null) {
            gameManager = new GameManager();
        }
        return gameManager;
    }

    public void createGame(Level level) {
        games.put(level.getUniqueId(), level);
    }

    public @Nullable Level getGame(UUID uuid) {
        return games.get(uuid);
    }
}
