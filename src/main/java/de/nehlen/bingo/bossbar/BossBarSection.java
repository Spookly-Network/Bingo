package de.nehlen.bingo.bossbar;

import lombok.Data;

@Data
public class BossBarSection {

    private char icon;
    private String content;
    private int pixel;

    public BossBarSection(char icon, String content) {
        this.icon = icon;
        this.content = content;
        this.pixel=content.length()*5;
    }

}
