package de.spookly.bingo.data.helper;

import de.spookly.bingo.data.StringData;
import de.spookly.bingo.util.fonts.TeamFont;

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

    public static Component teamPrefix(String c) {
        return Component.empty()
                .append(Component.text(c))
                .font(TeamFont.KEY)
                .color(NamedTextColor.WHITE);
    }

    public static Component teamIcon(String c) {
        return Component.empty()
                .append(Component.text(c))
                .font(Key.key("hud"))
                .color(NamedTextColor.WHITE);
    }

    @Deprecated(forRemoval = true)
    public static Component seperator() {
        return Component.text(" | ").color(NamedTextColor.GRAY).decorate(TextDecoration.BOLD);
    }

    public static MenuBuilder menuBuilder() {
        return new MenuBuilder();
    }

    public static class MenuBuilder {
        Component background;
        Component title;

        public MenuBuilder setBackground(char background) {
            this.background = Component.text(background);
            return this;
        }

        public MenuBuilder setBackground(String background) {
            this.background = Component.text(background);
            return this;
        }

        public MenuBuilder setTitle(char title) {
            this.title = Component.text(title);
            return this;
        }

        public MenuBuilder setTitle(String title) {
            this.title = Component.text(title);
            return this;
        }

        public MenuBuilder setTitle(Component title) {
            this.title = title;
            return this;
        }

        public Component build() {
            Component menu = Component.empty()
                    .font(Key.key("ui"))
                    .color(NamedTextColor.WHITE);

            if (background != null)
                menu = menu
                        .append(Component.text("\ue001"))
                        .append(this.background);
            if (title != null)
                menu = menu
                        .append(Component.text("\ue002"))
                        .append(this.title);
            return menu;
        }
    }
}
