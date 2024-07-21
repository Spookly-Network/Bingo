package de.spookly.bingo.bossbar;

import lombok.Data;
import net.kyori.adventure.text.Component;


@Data
public class BossBarSection {
    private Component icon;
    private Component content;
    private BossComponentHelper.BossBackgroundSize size;

    public BossBarSection(BossComponentHelper.BossBackgroundSize containerSize, char icon, Component content) {
        this.icon = Component.text(icon);
        this.content = content;
        this.size=containerSize;
    }

    public BossBarSection(BossComponentHelper.BossBackgroundSize containerSize, String icon, Component content) {
        this.icon = Component.text(icon);
        this.content = content;
        this.size=containerSize;
    }
}
