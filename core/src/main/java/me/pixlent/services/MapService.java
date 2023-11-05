package me.pixlent.services;

import me.pixlent.phasemachine.Service;
import net.hollowcube.polar.PolarLoader;
import net.minestom.server.instance.IChunkLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

public class MapService implements Service {
    @Override
    public void start(Context context) {

    }

    public IChunkLoader pickRandomMap() {
        return loadMap();
    }

    private IChunkLoader loadMap() {
        final var path = Path.of("tectonic_plates.polar");
        final var polarLoaderAtom = new AtomicReference<PolarLoader>();

        try {
            polarLoaderAtom.set(new PolarLoader(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return polarLoaderAtom.get();
    }
}
