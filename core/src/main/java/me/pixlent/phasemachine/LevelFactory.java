package me.pixlent.phasemachine;

import lombok.Getter;
import net.minestom.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class LevelFactory {
    private final List<Phase> phases = new ArrayList<>();

    public @NotNull LevelFactory addPhase(List<Service> services) {
        phases.add(new Phase(services));
        return this;
    }

    public Level build() {
        final var level = new Level(this);

        MinecraftServer.getInstanceManager().registerInstance(level);

        return level;
    }
}