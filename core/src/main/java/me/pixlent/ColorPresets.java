package me.pixlent;

import net.kyori.adventure.text.format.TextColor;

public enum ColorPresets {
    //Color end string, color reset
    RED(TextColor.fromHexString("#f53333")),
    YELLOW(TextColor.fromHexString("#e2ed4a"));

    final TextColor color;
    ColorPresets(TextColor color) {
        this.color = color;
    }

    public TextColor toTextColor() {
        return color;
    }
}
