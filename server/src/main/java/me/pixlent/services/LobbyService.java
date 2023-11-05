package me.pixlent.services;

import me.pixlent.common.ColorPresets;
import me.pixlent.phasemachine.Service;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.entity.EntitySpawnEvent;

// todo add a scoreboard and stuff
public class LobbyService implements Service {
    @Override
    public void start(Context context) {
        // Listener checking when a player joins
        // Check the total player-count and compare it to the minimum player-count to start the game

        final var tempMaxPlayers = 8;

        context.node().addListener(EntitySpawnEvent.class, event -> {
            if (!(event.getEntity() instanceof Player player)) {
                return;
            }

            // Teleport player to the lobby
            final var players = context.level().getPlayers();
            players.forEach(player1 -> player1.sendMessage(
                    Component.text(player.getUsername())
                            .color(ColorPresets.BRIGHT_BLUE.toTextColor()).append(
                                    Component.text(" has joined (" + players.size() + "/" + tempMaxPlayers + ")")
                                            .color(ColorPresets.LIGHT_BLUE.toTextColor()))));
        });
    }

    @Override
    public void stop() {
        //
    }
}
