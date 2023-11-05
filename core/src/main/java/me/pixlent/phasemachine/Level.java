package me.pixlent.phasemachine;

import me.pixlent.ColorPresets;
import me.pixlent.Guard;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.InstanceEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.tag.Tag;
import net.minestom.server.tag.TagHandler;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.world.DimensionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.List;
import java.util.UUID;

public final class Level extends InstanceContainer {
    private final LevelFactory builder;
    @SuppressWarnings("UnstableApiUsage")
    private final TagHandler handler = TagHandler.newHandler();
    private int currentPhase = 0;
    private EventNode<InstanceEvent> eventNode = null;

    Level(LevelFactory builder) {
        super(UUID.randomUUID(), DimensionType.OVERWORLD);
        this.builder = builder;
        next();
    }

    @SuppressWarnings("UnstableApiUsage")
    private void next() {
        System.out.println("Next (" + builder.getPhases().size() + ")");
        if (eventNode != null) {
            // stop current phase
            Phase phase = builder.getPhases().get(currentPhase);
            phase.services().forEach(Service::stop);

            // remove event node
            this.eventNode().removeChild(eventNode);

            currentPhase++;
            System.out.println(currentPhase);
        }

        if (currentPhase >= builder.getPhases().size()) {
            terminate(Component.text("Instance out of scope; all phases complete")
                    .color(ColorPresets.RED.toTextColor()));
            return;
        }

        final var node = EventNode.type("phase-" + currentPhase, EventFilter.INSTANCE);
        eventNode = node;
        this.eventNode().addChild(node);

        Phase phase = builder.getPhases().get(currentPhase);
        int phaseIndex = currentPhase;
        var context =
                new ServiceContext(this, eventNode, phaseIndex, phase.services());
        phase.services().forEach(service -> service.start(context));
    }

    public void terminate(Component reason) {
        this.getPlayers().forEach(player -> player.kick(reason));
        Guard.tryCatch("Couldn't unregister instance", ()
                -> MinecraftServer.getSchedulerManager().scheduleTask(()
                        -> MinecraftServer.getInstanceManager().unregisterInstance(this),
                TaskSchedule.seconds(1), TaskSchedule.stop()));
    }

    private record ServiceContext(Level level,
                                  EventNode<InstanceEvent> node, int phaseIndex, List<Service> services)
            implements Service.Context {

        @Override
        public <T> @UnknownNullability T getTag(@NotNull Tag<T> tag) {
            return level.handler.getTag(tag);
        }

        @Override
        public <T> void setTag(@NotNull Tag<T> tag, @Nullable T value) {
            level.handler.setTag(tag, value);
        }

        @Override
        public ServiceManager serviceManager() {
            return new ServiceManager(services);
        }

        @Override
        public void completePhase() {
            level.next();
        }
    }
}
