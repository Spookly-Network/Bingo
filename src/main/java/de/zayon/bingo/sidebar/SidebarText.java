package de.zayon.bingo.sidebar;

import java.util.List;

public class SidebarText {
    private final List<String> text;

    private final String displayName;

    public SidebarText(List<String> text, String displayName) {
        this.text = text;
        this.displayName = displayName;
    }

    public List<String> getText() {
        return this.text;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
