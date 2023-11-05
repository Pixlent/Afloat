package me.pixlent.services;

import me.pixlent.common.ColorPresets;
import me.pixlent.phasemachine.Service;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.*;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.time.TimeUnit;

public class StartupService implements Service {
    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void start(Context context) {
        final var mapService = context.serviceManager().getService(MapService.class);
        if (mapService == null) {
            final var reason = Component.text("Map Service unavailable")
                    .color(ColorPresets.RED.toTextColor());
            context.level().terminate(reason);
            return;
        }

        context.level().setChunkLoader(mapService.pickRandomMap());

        // place chests
        //

        context.level().setJoinable(false);

        MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, event -> {
            //context.level().setJoinable(true);
            MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                System.out.println("enabled joininmg");
                context.level().setJoinable(true);
            }, TaskSchedule.seconds(1), TaskSchedule.stop());
        });

        context.node().addListener(PlayerBlockBreakEvent.class, event -> {
            final var player = event.getPlayer();
            if (!player.getGameMode().equals(GameMode.SURVIVAL)) {
                return;
            }

            final var material = Material.fromNamespaceId(event.getBlock().name());
            if (material == null) {
                return;
            }

            final var itemEntity = new ItemEntity(ItemStack.of(material));
            itemEntity.setPickupDelay(1, TimeUnit.MILLISECOND);
            itemEntity.setInstance(event.getInstance(), centerPos(event.getBlockPosition()));
            itemEntity.setVelocity(player.getPosition().direction().mul(3, 3, 3));
        });

        context.node().addListener(ItemDropEvent.class, event -> {
            final var player = event.getEntity();
            final var itemEntity = new ItemEntity(event.getItemStack());

            itemEntity.setPickupDelay(200, TimeUnit.MILLISECOND);
            itemEntity.setInstance(player.getInstance(), player.getPosition().add(0, player.getEyeHeight(), 0));

            Vec velocity = player.getPosition().direction().mul(6);
            itemEntity.setVelocity(velocity);
        });

        context.node().addListener(PickupItemEvent.class, event -> {
            if (!(event.getEntity() instanceof Player player)) {
                return;
            }

            final var canAdd = player.getInventory().addItemStack(event.getItemStack());
            event.setCancelled(!canAdd);
        });
    }

    private Pos centerPos(Point point) {
        return new Pos(point.x() + .5, point.y(), point.z() + .5);
    }
}
