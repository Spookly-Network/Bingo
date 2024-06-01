package de.nehlen.bingo.data.helper;

import de.nehlen.bingo.data.StringData;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class TextComponentHelper {

    public static Component newLineComponent() {
        return Component.newline();
    }

    public static Component spaceComponent() {
        return Component.text(" ");
    }

    public static Component addPointsComponent(int amount) {
        return StringData.getPrefix()
                .append(Component.translatable("general.points.add",
                        Component.text(amount).color(NamedTextColor.RED)).color(NamedTextColor.GRAY));
    }

    public static Component seperator() {
        return Component.text(" | ").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD);
    }

    public static Component customUiInventoryTitle(char c) {
        return Component.text(c)
                .font(Key.key("ui"))
                .color(NamedTextColor.WHITE);
    }
}
