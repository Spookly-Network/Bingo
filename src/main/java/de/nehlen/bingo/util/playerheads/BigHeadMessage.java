package de.nehlen.bingo.util.playerheads;

import de.nehlen.bingo.util.fonts.BigPixelFont;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class BigHeadMessage {

    public static Component getComponent(Player player, Component... lines) {
        if (lines.length >= 4) return Component.empty();

        Component component = Component.newline();
        component = component.append(PlayerheadChatComponent.getHeadComponent(player.getPlayer(), BigPixelFont.class)
                .font(BigPixelFont.KEY));
        for (int i = 0; i < Math.floor((double) (7 - lines.length) / 2); i++) {
            component = component.append(Component.newline());
        }
        for (int i = 0; i < lines.length; i++) {
            List<Component> components = Arrays.stream(lines).toList();
            component = component
                    .append(Component.newline())
                    .append(Component.text("\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020" +
                            "\u0020\u0020\u0020\u0020\u0020\u0020\u0020\u0020"))
                    .append(components.get(i));
        }
        for (int i = 0; i < Math.ceil((double) (7 - lines.length) / 2); i++) {
            component = component.append(Component.newline());
        }
        return component;
    }
}