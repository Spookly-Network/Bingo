package de.nehlen.bingo.data;


import de.nehlen.bingo.sidebar.SidebarPage;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;

@Getter
public enum ScoreboardData implements SidebarPage {
//    decorate(TextDecoration.BOLD)
    PAGE_1(List.of(
            Component.empty(),
            title("bingo.data.scoreboard.team"),
            Component.text("%team%"),
            Component.empty(),
            title("bingo.data.scoreboard.phase"),
            Component.text("%gamestatus%"),
            Component.empty(),
            title("bingo.data.scoreboard.time"),
            Component.text("%timer%"),
            Component.empty(),
            Component.empty(),
            MiniMessage.miniMessage().deserialize("<gradient:#FF5555:#FFAA00>spookly.de")
    )),
    PAGE_2(List.of(
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
            MiniMessage.miniMessage().deserialize("<gradient:#FF5555:#FFAA00>spookly.de")
    ));

    private final List<Component> lines;

    ScoreboardData(List<Component> lines) {
        this.lines = lines;
    }
    public Component getDisplayName() {
        return Component.text(StringData.getScoreboardTitle()).font(Key.key("scoreboard"));
    }

    private static Component title(String key) {
        return Component.translatable(key).color(StringData.getHighlightColor()).font(Key.key("small"));
    }
}
