package de.nehlen.bingo.data.helper;

import de.nehlen.bingo.data.StringData;
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
                .append(Component.text("Du hast ").color(NamedTextColor.GRAY))
                .append(Component.text(amount).color(NamedTextColor.RED))
                .append(Component.text(" Punkte erhalten.").color(NamedTextColor.GRAY));
    }

    public static Component seperator() {
        return Component.text(" | ").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD);
    }
}
