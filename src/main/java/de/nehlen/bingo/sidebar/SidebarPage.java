package de.nehlen.bingo.sidebar;

import java.util.List;

public interface SidebarPage {
    String getDisplayName();

    List<String> getLines();
}
