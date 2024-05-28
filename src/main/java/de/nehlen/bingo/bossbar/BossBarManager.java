package de.nehlen.bingo.bossbar;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class BossBarManager {

    private BossBar bossBar;
    private List<Component> componentSections = new ArrayList<>();

    public BossBarManager() {
        bossBar = BossBar.bossBar(Component.empty(), 0, BossBar.Color.WHITE, BossBar.Overlay.NOTCHED_20);
    }

    public void addSection(Component component) {
        componentSections.add(component);
    }
}
