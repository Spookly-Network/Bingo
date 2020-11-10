package de.zayon.bingo.data;


import de.zayon.bingo.sidebar.SidebarPage;

import java.util.Arrays;
import java.util.List;

public enum ScoreboardData implements SidebarPage {
    PAGE_1("§7» §cBingo §7«", Arrays.asList("§r", "§8➤ §eKills:    ", "§f%kills%", "§r", "§8➤ §eTode:    ", "§f%deaths%", "§r", "§8➤ §eKD:    ", "§f%kd%"));

    private final List<String> lines;

    private final String displayName;

    ScoreboardData(String displayName, List<String> lines) {
        this.displayName = displayName;
        this.lines = lines;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public List<String> getLines() {
        return this.lines;
    }
    }
