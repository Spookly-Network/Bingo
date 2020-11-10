package de.zayon.bingo.sidebar;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

final class SidebarLine {
    private final int index;

    private final Team team;

    private final String entry;

    private final ChatColor color;

    private String text;

    SidebarLine(int index, Team team, String entry, ChatColor color) {
        this.index = index;
        this.team = team;
        this.entry = entry;
        this.color = color;
    }

    public int getIndex() {
        return this.index;
    }

    public String getText() {
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

    public void setText(String text) {
        this.text = text;
    }
}
