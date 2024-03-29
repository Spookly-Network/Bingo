package de.nehlen.bingo.data;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.helper.TextComponentHelper;
import de.nehlen.spooklycloudnetutils.manager.GroupManager;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

public class StringData {

    @Getter private static String prefix_text = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.text.prefix.text", "Bingo");
    @Getter private static String prefix_separator = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.text.prefix.separator", "◆");
    @Getter private static TextColor prefix_color = TextColor.fromHexString(Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.text.prefix.color", NamedTextColor.DARK_AQUA.asHexString()));
    @Getter private static Component prefix = Component.text(prefix_text).color(prefix_color).append(Component.text(" "))
            .append(Component.text(prefix_separator).color(NamedTextColor.DARK_GRAY)).append(Component.text(" "));
    @Getter private static String noPerm = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.text.noPerms", "Darauf hast du keine Rechte.");
    @Getter private static TextColor highlightColor = TextColor.fromHexString(Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.text.highlightColor", NamedTextColor.AQUA.asHexString()));


    public static String combinate() { return prefix + noPerm; }

    public static Component playerTeamPrefix(Player player) {
        Component teamPrefixComponent = Component.empty();
        if(GameData.getTeamSize() > 1) {
            if(GameData.getTeamCache().containsKey(player))
                teamPrefixComponent = teamPrefixComponent.append(Component.text("[").color(NamedTextColor.GRAY))
                        .append(GameData.getTeamCache().get(player).teamName())
                        .append(Component.text("]").color(NamedTextColor.GRAY))
                        .append(TextComponentHelper.spaceComponent());
        }
        return teamPrefixComponent;
    }

    public static Component playerListName(Player player) {
        return Component.text(GroupManager.getGroupPrefix(player).replaceAll("&", "§")).append(player.displayName());
    }
}
