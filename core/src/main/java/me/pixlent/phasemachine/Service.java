package me.pixlent.phasemachine;

import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.InstanceEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.tag.TagReadable;
import net.minestom.server.tag.TagWritable;

public interface Service {
    void start(Context context);

    interface Context extends TagReadable, TagWritable {
        Level level();
        EventNode<InstanceEvent> node();

        /**
         * Stops this phase (including stopping all other services) and starts the next phase
         */
        void completePhase();
    }

    default void stop() {
    }
}
