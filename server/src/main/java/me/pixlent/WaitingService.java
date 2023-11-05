package me.pixlent;

import me.pixlent.phasemachine.Service;
import net.minestom.server.event.player.PlayerChatEvent;

public class WaitingService implements Service {
    @Override
    public void start(Context context) {
        context.node().addListener(PlayerChatEvent.class, event -> {
            final var player = event.getPlayer();

            if (event.getMessage().contains("start")) {
                context.completePhase();
                player.sendMessage("Ominously completed waiting phase...");
            }
        });
    }
}
