package me.pixlent.services;

import me.pixlent.ColorPresets;
import me.pixlent.phasemachine.Service;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.*;
import net.minestom.server.entity.metadata.item.ItemEntityMeta;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.item.PickupItemEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
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


        context.node().addListener(PlayerBlockBreakEvent.class, event -> {
            final var player = event.getPlayer();

            player.sendMessage("You broke block " + event.getBlock().name());

            final var item = new Entity(EntityType.ITEM);
            item.setInstance(event.getInstance(), centerPos(event.getBlockPosition()));
            item.setVelocity(player.getPosition().direction().mul(3, 3, 3));
            final var meta = (ItemEntityMeta) item.getEntityMeta();

            meta.setItem(ItemStack.of(Material.SEA_LANTERN));
        });

        context.node().addListener(ItemDropEvent.class, event -> {
            final var player = event.getEntity();
            final var itemEntity = new ItemEntity(event.getItemStack());

            itemEntity.setPickupDelay(500, TimeUnit.MILLISECOND);
            itemEntity.setInstance(player.getInstance(), player.getPosition().add(0, 1.5f, 0));

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
