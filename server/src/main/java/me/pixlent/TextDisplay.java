package me.pixlent;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.display.AbstractDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;

public final class TextDisplay {
    private final @NotNull Entity entity = new Entity(EntityType.TEXT_DISPLAY);
    private final @NotNull TextDisplayMeta meta = (TextDisplayMeta) entity.getEntityMeta();

    public TextDisplay text(String text) {
        meta.setText(Component.text(text));
        return this;
    }

    public TextDisplay text(Component text) {
        meta.setText(text);
        return this;
    }

    public TextDisplay billboardConstraints(AbstractDisplayMeta.BillboardConstraints billboardConstraints) {
        meta.setBillboardRenderConstraints(billboardConstraints);
        return this;
    }

    public TextDisplay lineWidth(int value) {
        meta.setLineWidth(value);
        return this;
    }

    public TextDisplay backgroundColor(int value) {
        meta.setBackgroundColor(value);
        return this;
    }

    public TextDisplay opacity(byte value) {
        meta.setTextOpacity(value);
        return this;
    }

    public TextDisplay shadow(boolean value) {
        meta.setShadow(value);
        return this;
    }

    public TextDisplay seeThrough(boolean value) {
        meta.setSeeThrough(value);
        return this;
    }

    public TextDisplay defaultBackground(boolean value) {
        meta.setUseDefaultBackground(value);
        return this;
    }

    public TextDisplay alignLeft(boolean value) {
        meta.setAlignLeft(value);
        return this;
    }

    public TextDisplay alignRight(boolean value) {
        meta.setAlignRight(value);
        return this;
    }

    public TextDisplay gravity(boolean value) {
        entity.setNoGravity(!value);
        return this;
    }

    public TextDisplay destroy(int seconds) {
        entity.scheduler().scheduleTask(entity::remove, TaskSchedule.seconds(seconds), TaskSchedule.stop());
        return this;
    }

    public TextDisplay destroy() {
        entity.remove();
        return this;
    }

    public @NotNull Entity build() {
        return entity;
    }

    public @NotNull Entity build(Instance instance, Pos pos) {
        entity.setInstance(instance, pos);
        return entity;
    }
}