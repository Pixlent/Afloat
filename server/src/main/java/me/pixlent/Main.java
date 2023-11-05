package me.pixlent;

import net.minestom.server.MinecraftServer;

public class Main {
    public static void main(String[] args) {
        final var server = MinecraftServer.init();

        server.start("0.0.0.0", 25565);
        AfloatServer.hook(server);
    }
}
