package de.nehlen.bingo.data;

import de.nehlen.bingo.Bingo;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class StringData {

    @Getter private final static String prefix_text = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.text.prefix.text", "Bingo");
    @Getter private final static String prefix_separator = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.text.prefix.separator", "◆");
    @Getter private final static TextColor prefix_color = TextColor.fromHexString(Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.text.prefix.color", NamedTextColor.DARK_AQUA.asHexString()));
    @Getter private final static Component prefix = Component.text(prefix_text).color(prefix_color).append(Component.text(" "))
            .append(Component.text(prefix_separator).color(NamedTextColor.DARK_GRAY)).append(Component.text(" "));
    @Getter private final static String noPerm = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.text.noPerms", "Darauf hast du keine Rechte.");
    @Getter private final static TextColor highlightColor = TextColor.fromHexString(Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.text.highlightColor", NamedTextColor.AQUA.asHexString()));
    @Getter private final static String scoreboardTitle = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.scoreboard.title", String.valueOf('\uE000'));


    public static Component combinate() { return prefix.append(Component.text(noPerm).color(NamedTextColor.RED)); }

//    public static Component playerTeamPrefix(Player player) {
//        Component teamPrefixComponent = Component.empty();
//        if(GameData.getTeamSize() > 1) {
//            if(GameData.getTeamCache().containsKey(player))
//                teamPrefixComponent = teamPrefixComponent.append(GameData.getTeamCache().get(player).prefix().font(TeamFont.KEY))
//                        .append(TextComponentHelper.spaceComponent());
//        }
//        return teamPrefixComponent;
//    }

//    public static Component playerListName(Player player) {
//        return Component.text(GroupManager.getGroupPrefix(player).replaceAll("&", "§")).append(player.displayName());
//    }
}
