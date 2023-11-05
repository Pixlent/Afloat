package me.pixlent;

import me.pixlent.commands.GamemodeCommand;
import me.pixlent.commands.TeleportCommand;
import me.pixlent.phasemachine.Level;
import me.pixlent.phasemachine.LevelFactory;
import me.pixlent.phasemachine.LevelManager;
import me.pixlent.services.LobbyService;
import me.pixlent.services.MapService;
import me.pixlent.services.StartupService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extras.MojangAuth;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*
         * Initialize the minecraft server
         */
        final var server = MinecraftServer.init();

        registerCommands();

        final var levelManager = LevelManager.hook();

        final var hook = GameManager.hook();
        final var level = new LevelFactory()
                .addPhase(List.of(new MapService(), new StartupService()))
                .addPhase(List.of(new LobbyService()))
                .build();
        hook.createGame(level);

        MinecraftServer.getInstanceManager().registerInstance(level);

        Combat.handle(MinecraftServer.getGlobalEventHandler());

        MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, event -> {
            if (!level.isJoinable()) {
                event.getPlayer().kick("bye");
            }

            event.setSpawningInstance(level);
            event.getPlayer().setRespawnPoint(new Pos(146, 139, 277));
            event.getPlayer().setPermissionLevel(4);
        });

        MojangAuth.init();

        /*
         * Start and hook the minecraft server
         */
        server.start("0.0.0.0", 25565);
        AfloatServer.hook(server);
    }

    public static void registerCommands() {
        final var commandManager = MinecraftServer.getCommandManager();

        commandManager.register(new GamemodeCommand());
        commandManager.register(new TeleportCommand());
    }
}
