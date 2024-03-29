package de.nehlen.bingo.data;


import de.nehlen.bingo.sidebar.SidebarPage;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Arrays;
import java.util.List;

public enum ScoreboardData implements SidebarPage {
    PAGE_1("§8| 1/2", List.of(
            Component.empty(),
            Component.text("Team").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD),
            Component.text("%team%"),
            Component.empty(),
            Component.text("Phase").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD),
            Component.text("%gamestatus%"),
            Component.empty(),
            Component.text("Zeit").color(NamedTextColor.AQUA).decorate(TextDecoration.BOLD),
            Component.text("%timer%"),
            Component.empty(),
            Component.empty(),
            MiniMessage.miniMessage().deserialize("<gradient:#FFAA00:#FFFF55>spookly.de")
    )),
    PAGE_2("§8| 2/2", List.of(
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

    @Getter
    private final List<Component> lines;

    private final String displayName;


    ScoreboardData(String displayName, List<Component> lines) {
        this.displayName = displayName;
        this.lines = lines;
    }
    public Component getDisplayName() {
        return Component.text(StringData.getPrefix_text()).color(StringData.getPrefix_color()).decorate(TextDecoration.BOLD)
                .append(Component.text(" " + displayName));
    }
}
