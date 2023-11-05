package me.pixlent;

import me.pixlent.phasemachine.Service;
import net.minestom.server.entity.Player;
import net.minestom.server.event.entity.EntitySpawnEvent;

public class StartupService implements Service {
    @Override
    public void start(Context context) {

        context.node().addListener(EntitySpawnEvent.class, event -> {
            if (!(event.getEntity() instanceof Player player)) return;

            player.sendMessage("You spawned into this instance");

            context.completePhase();
        });
    }
}
