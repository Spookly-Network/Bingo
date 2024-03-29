package de.nehlen.bingo.sidebar;

import net.kyori.adventure.text.Component;

import java.util.List;

public interface SidebarPage {
    Component getDisplayName();

    List<Component> getLines();
}
