package de.nehlen.bingo.data;

import de.nehlen.bingo.sidebar.SidebarPage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Arrays;
import java.util.List;

public enum IngameScoreboardData  implements SidebarPage {
    PAGE_1(List.of(
            Component.empty(),
            Component.text("%item1%"),
            Component.text("%item2%"),
            Component.text("%item3%"),
            Component.text("%item4%"),
            Component.text("%item5%"),
            Component.text("%item6%"),
            Component.text("%item7%"),
            Component.text("%item8%"),
            Component.text("%item9%"),
            Component.empty(),
            MiniMessage.miniMessage().deserialize("<gradient:#FFAA00:#FFFF55>spookly.de")

    ));

    private final List<Component> lines;

    IngameScoreboardData(List<Component> lines) {
        this.lines = lines;
    }

    public Component getDisplayName() {
        return Component.text(StringData.getPrefix_text()).color(StringData.getPrefix_color()).decorate(TextDecoration.BOLD);
    }

    public List<Component> getLines() {
        return this.lines;
    }
}
