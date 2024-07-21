package de.spookly.bingo.bossbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

import de.spookly.Spookly;
import de.spookly.bingo.SpooklyBingoPlugin;
import de.spookly.bingo.data.helper.TextComponentHelper;
import de.spookly.placeholder.PlaceholderContext;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class BossBarManager {

	private final Map<Player, BossBar> bossBars = new HashMap<>();
	private final List<BossBarSection> sections = new ArrayList<>();
	private final BukkitTask bossBarRunnable;

	private BossBar bossBar;
	private List<Component> componentSections = new ArrayList<>();

	public BossBarManager() {
		bossBar = BossBar.bossBar(Component.empty(), 0, BossBar.Color.WHITE, BossBar.Overlay.NOTCHED_20);
		bossBarRunnable = new BukkitRunnable() {
			@Override
			public void run() {
				tick();
			}
		}.runTaskTimerAsynchronously(SpooklyBingoPlugin.getSpooklyBingoPlugin(), 0L, 5L);
	}

	public void addSection(BossBarSection section) {
		sections.add(section);
	}

	public void showUserBossbar(Player player) {
		if (this.bossBars.containsKey(player))
			return;
		BossBar bar = BossBar.bossBar(Component.empty(), 0, BossBar.Color.WHITE, BossBar.Overlay.NOTCHED_20);
		this.bossBars.put(player, bar);
		bar.addViewer(player);
	}

	public void tick() {
		for (Player player : bossBars.keySet()) {
			BossBar bossBar = bossBars.get(player);
			Component content = Component.empty();
			PlaceholderContext context = new PlaceholderContext(player, PlaceholderContext.PlaceholderType.BOSSBAR);
			for (BossBarSection section : sections) {
				Component iconComponent = Spookly.getPlaceholderManager().replacePlaceholder(section.getIcon(), context)
						.font(Key.key("hud"))
						.color(BossComponentHelper.getNoShadowColor());
				Component contentComponent = Spookly.getPlaceholderManager().replacePlaceholder(section.getContent(), context);
				Component sectionComponent = BossComponentHelper.container(section.getSize(), Component.empty()
						.append(iconComponent)
						.append(TextComponentHelper.spaceComponent())
						.append(contentComponent));

				content = content.append(sectionComponent);
				if (sections.indexOf(section) != sections.size() - 1) {
					content = content.append(Component.text("\uEFE5").font(Key.key("hud")));
				}
			}
			bossBar.name(content);
		}
	}
}
