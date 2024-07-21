package de.spookly.bingo.data.helper;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import de.spookly.Spookly;
import de.spookly.bingo.SpooklyBingoPlugin;
import de.spookly.bingo.data.GameData;
import de.spookly.bingo.data.StringData;
import de.spookly.bingo.statistics.player.BingoPlayerStats;
import de.spookly.player.SpooklyPlayer;
import de.spookly.team.Team;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PickList {

	@Getter
	private List<Material> items;

	public PickList(List<Material> items) {
		this.items = new ArrayList<>(items);
	}

	public void completeMaterial(Player player, Team team, Material material) {
		SpooklyPlayer spooklyPlayer = Spookly.getPlayer(player);
		BingoPlayerStats statistics = SpooklyBingoPlugin.getSpooklyBingoPlugin().getPlayerStatisticsManager().getPlayerStatistics(spooklyPlayer);

		if (!GameData.firstItemFound) {
			spooklyPlayer.addPoints(20);
			player.sendMessage(TextComponentHelper.addPointsComponent(20));
			player.sendMessage(StringData.getPrefix()
					.append(Component.text("Du hast den ").color(NamedTextColor.GRAY)
							.append(Component.text("ersten Gegenstand").color(StringData.getHighlightColor()))
							.append(Component.text(" der Runde gefunden.").color(NamedTextColor.GRAY))));
			GameData.firstItemFound=true;
		}

		spooklyPlayer.addPoints(20);
		statistics.setItemsCompleted(statistics.getItemsCrafted()+1);
		player.sendMessage(TextComponentHelper.addPointsComponent(20));
		items.remove(material);

		Bukkit.broadcast(StringData.getPrefix()
				.append(Component.translatable("phase.ingame.itemFound",
						spooklyPlayer.nameTag(),
						Component.translatable(TranslatableHelper.getTranslationKey(material)).color(StringData.getHighlightColor()),
						Component.text(getAmountCompleted()),
						Component.text(GameData.getItemsAmount())
						).color(NamedTextColor.GRAY)));
		if (isComplete()) {
			SpooklyBingoPlugin.getSpooklyBingoPlugin().getIngamePhase().endPhase();
			SpooklyBingoPlugin.getSpooklyBingoPlugin().getEndingPhase().teamWin(team);
		}
	}

	public Integer materialsLeft() {
		return items.size();
	}

	public Integer getAmountCompleted() {
		return GameData.getItemsToFind().size() - items.size();
	}

	public Boolean isComplete() {
		return items.isEmpty();
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		items.forEach(material -> {
			stringBuilder.append(material.toString() + ",");
		});
		return "PickList[" + stringBuilder.toString() + "]";
	}

}
