package me.pixlent;

import lombok.Getter;
import net.minestom.server.MinecraftServer;

@Getter
public class AfloatServer {
    private static AfloatServer afloat = null;
    private final MinecraftServer server;

    private AfloatServer(MinecraftServer server) {
        this.server = server;
    }

    public static void hook(MinecraftServer server) {
        if (afloat == null) afloat = new AfloatServer(server);
    }
}
