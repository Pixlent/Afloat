package me.pixlent.phasemachine;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ServiceManager {
    private final List<Service> services;

    ServiceManager(List<Service> services) {
        this.services = services;
    }

    /**
     * Get the service T
     *
     * @param serviceOfT The service requested
     * @return The service requested; may be null
     * @param <T> The service requested
     */
    public @Nullable <T extends Service> T getService(Class<T> serviceOfT) {
        final var returnService = new AtomicReference<T>();

        services.forEach(service -> {
            if (service.getClass().equals(serviceOfT)) returnService.set(serviceOfT.cast(service));
        });

        return returnService.get();
    }
}
