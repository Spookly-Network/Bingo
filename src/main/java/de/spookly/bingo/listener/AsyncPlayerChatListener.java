package de.spookly.bingo.listener;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import de.spookly.Spookly;
import de.spookly.bingo.data.GameData;
import de.spookly.bingo.data.GameState;
import de.spookly.placeholder.PlaceholderContext;
import de.spookly.player.SpooklyPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AsyncPlayerChatListener implements Listener {

	public AsyncPlayerChatListener() {
	}

	@EventHandler
	public void onChat(AsyncChatEvent event) {
		Player player = event.getPlayer();
		SpooklyPlayer spooklyPlayer = Spookly.getPlayer(player);

		if (GameState.state != GameState.END) {
			if (!GameData.getIngame().contains(player)) {
				event.renderer(spectateChatRenderer(spooklyPlayer));
			} else {
				event.renderer(spooklyPlayer.getChatRenderer());
			}
		} else {
			event.renderer(spooklyPlayer.getChatRenderer());
		}
	}

	public ChatRenderer spectateChatRenderer(SpooklyPlayer player) {
		return (source, sourceDisplayName, message, viewer) -> {
			PlaceholderContext context = new PlaceholderContext(source, PlaceholderContext.PlaceholderType.CHAT);

			return (Component) Component.text()
					.append(Component.text("✘ ").color(NamedTextColor.DARK_RED))
					.append(player.prefix())
					.append(sourceDisplayName.color(player.nameColor()))
					.append(Component.text(" \u203A\u203A ").color(NamedTextColor.GRAY)) // Separator ››
					.append(Spookly.getPlaceholderManager().replacePlaceholder(message, context))
					.build();
		};
	}
}
