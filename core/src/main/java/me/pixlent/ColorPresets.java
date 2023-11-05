package me.pixlent;

import net.kyori.adventure.text.format.TextColor;

public enum ColorPresets {
    //Color end string, color reset
    LIGHT_BLUE(TextColor.fromHexString("#00E0FF")),
    BRIGHT_BLUE(TextColor.fromHexString("#00BFFF")),
    FROST_BLUE(TextColor.fromHexString("#4680FF")),
    BRIGHT_GREEN(TextColor.fromHexString("#00C42A")),
    PALE_GREEN(TextColor.fromHexString("#80B05A")),
    DARK_GREEN(TextColor.fromHexString("#00A814")),
    RED(TextColor.fromHexString("#FF0000")),
    PALE_RED(TextColor.fromHexString("#FF6060")),
    ORANGE(TextColor.fromHexString("#FF8E00")),
    LIGHT_GRAY(TextColor.fromHexString("#99A9A9")),
    PURPLE(TextColor.fromHexString("#7000FF")),
    CYAN(TextColor.fromHexString("#009B7D")),
    YELLOW(TextColor.fromHexString("#FFD500")),
    PALE_YELLOW(TextColor.fromHexString("#FFC200")),
    WHITE(TextColor.fromHexString("#FFFFFF")),

    OLD_RED(TextColor.fromHexString("#f53333")),
    OLD_YELLOW(TextColor.fromHexString("#e2ed4a")),
    OLD_GREEN(TextColor.fromHexString("#3BDD3E"));

    final TextColor color;
    ColorPresets(TextColor color) {
        this.color = color;
    }

    public TextColor toTextColor() {
        return color;
    }
}
