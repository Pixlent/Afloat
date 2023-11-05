package me.pixlent;

import me.pixlent.phasemachine.LevelFactory;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        final var server = MinecraftServer.init();

        MojangAuth.init();
        server.start("0.0.0.0", 25565);

        final var level = new LevelFactory()
                .addPhase(List.of(new StartupService()))
                .addPhase(List.of(new WaitingService()))
                .addPhase(List.of(new CleanupService()))
                .build();

        level.setGenerator(unit ->
                unit.modifier().fillHeight(0, 40, Block.STONE));
        level.setChunkSupplier(LightingChunk::new);

        MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, event -> {
            final var player = event.getPlayer();

            player.setPermissionLevel(4);
            player.setGameMode(GameMode.CREATIVE);
            player.setRespawnPoint(new Pos(0, 40, 0));

            event.setSpawningInstance(level);
        });

        AfloatServer.hook(server);
    }
}
