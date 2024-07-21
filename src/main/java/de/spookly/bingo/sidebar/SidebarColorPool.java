package de.spookly.bingo.sidebar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;

final class SidebarColorPool {
	private final Iterable<ChatColor> colors = new ArrayList<>(Arrays.asList(ChatColor.values()));

	private final Collection<ChatColor> colorsUsed = new ArrayList<>();

	public ChatColor getFreeColor() {
		List<ChatColor> freeColors = new ArrayList<>();
		for (ChatColor color : this.colors) {
			if (!this.colorsUsed.contains(color))
				freeColors.add(color);
		}
		return freeColors.get(0);
	}

	public void clear() {
		this.colorsUsed.clear();
	}

	public void add(ChatColor chatColor) {
		this.colorsUsed.add(chatColor);
	}
}
