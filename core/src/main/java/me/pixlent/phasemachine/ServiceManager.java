package me.pixlent.phasemachine;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ServiceManager {
    private final List<Service> services;

    ServiceManager(List<Service> services) {
        this.services = services;
    }

    public <T extends Service> T getService(Class<T> serviceOfT) {
        final var returnService = new AtomicReference<T>();

        services.forEach(service -> {
            if (service.getClass().equals(serviceOfT)) returnService.set(serviceOfT.cast(service));
        });

        return returnService.get();
    }

    public boolean hasService(Class<? extends Service> serviceOfT) {
        final var present = new AtomicBoolean();

        services.forEach(service -> {
            if (service.getClass().equals(serviceOfT)) present.set(true);
        });

        return present.get();
    }
}
