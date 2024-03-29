package de.nehlen.bingo.sidebar;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

final class SidebarLine {
    private final int index;

    private final Team team;

    private final String entry;

    private final ChatColor color;

    private Component text;

    SidebarLine(int index, Team team, String entry, ChatColor color) {
        this.index = index;
        this.team = team;
        this.entry = entry;
        this.color = color;
    }

    public int getIndex() {
        return this.index;
    }

    public Component getText() {
        return this.text;
    }

    public String getEntry() {
        return this.entry;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public Team getTeam() {
        return this.team;
    }

    public void setText(Component text) {
        this.text = text;
    }
}
