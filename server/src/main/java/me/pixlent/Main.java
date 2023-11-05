package me.pixlent;

import me.pixlent.commands.GamemodeCommand;
import me.pixlent.commands.TeleportCommand;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.instance.block.Block;

public class Main {
    public static void main(String[] args) {
        /*
         * Initialize the minecraft server
         */
        final var server = MinecraftServer.init();

        registerCommands();

        final var instance = MinecraftServer.getInstanceManager().createInstanceContainer();

        instance.setGenerator(unit -> {
            unit.modifier().fillHeight(0, 10, Block.STONE);
        });

        MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, event -> {
            event.setSpawningInstance(instance);
            event.getPlayer().setRespawnPoint(new Pos(0, 10, 0));
        });

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
