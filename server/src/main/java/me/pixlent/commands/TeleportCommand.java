package me.pixlent.commands;

import me.pixlent.ColorPresets;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;

public class TeleportCommand extends Command {

    public TeleportCommand() {
        super("teleport", "tp");

        setDefaultExecutor((sender, context) -> sender.sendMessage("Missing arguments! Usage: /teleport <player>"));

        final var playerArgument = ArgumentType.String("Player");

        playerArgument.setSuggestionCallback((sender, context, suggestion)
                -> MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player
                -> suggestion.addEntry(new SuggestionEntry(player.getUsername()))));

        playerArgument.setCallback((sender, exception) -> {
            final var message = Component
                    .text(exception.getInput())
                    .color(ColorPresets.OLD_RED.toTextColor())
                    .append(Component.text(" is not a player"))
                            .color(ColorPresets.OLD_YELLOW.toTextColor());

            sender.sendMessage(message);
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player player)) return;
            final var targetUsername = context.get(playerArgument);
            final var target = MinecraftServer.getConnectionManager().getPlayer(targetUsername);

            if (target == null) {
                final var message = Component
                        .text(targetUsername)
                        .color(ColorPresets.OLD_RED.toTextColor())
                        .append(Component.text(" is not a player"))
                        .color(ColorPresets.OLD_YELLOW.toTextColor());
                player.sendMessage(message);
                return;
            }

            if (targetUsername.equals(player.getUsername())) {
                final var message = Component
                        .text("You cannot teleport to yourself")
                        .color(ColorPresets.OLD_RED.toTextColor());
                player.sendMessage(message);
                return;
            }

            player.teleport(target.getPosition());
            final var message = Component.text("You teleported to ")
                            .color(ColorPresets.OLD_YELLOW.toTextColor())
                                    .append(Component.text(targetUsername))
                                            .color(ColorPresets.OLD_RED.toTextColor());

            player.sendMessage(message);

        }, playerArgument);
    }
}
