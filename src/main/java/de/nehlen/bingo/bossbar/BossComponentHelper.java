package de.nehlen.bingo.bossbar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class BossComponentHelper {

    @Getter private static final TextColor noShadowColor = TextColor.fromHexString("#fcfc04");
    private static final char minus_backspace = '\uEFF0';

    private static final Key bossFont = Key.key("bossbar_1");

    public static Component backgroundEnd() {
        return Component.text("ꈁ")
                .font(Key.key("bossbar_1"))
                .color(noShadowColor);
    }

    public static Component combiner() {
        return Component.text(minus_backspace)
                .font(Key.key("hud"));
    }

    public static Component background(BossBackgroundSize size) {
        return size.getComponent();
    }

    public static Component container(BossBackgroundSize size, Component component) {
        return Component.empty()
                .append(backgroundEnd())
                .append(combiner())
                .append(size.getComponent().color(noShadowColor))
                .append(combiner())
                .append(backgroundEnd())
                .append(Component.text(size.getMinusSpace()).font(Key.key("hud")))
                .append(component.font(Key.key("hud")));
    }

    @AllArgsConstructor @Getter
    public enum BossBackgroundSize {
        SIZE_1(Component.text("ꈂ").font(bossFont), '\uEFF0'),
        SIZE_2(Component.text("ꈃ").font(bossFont), '\uEFF1'),
        SIZE_4(Component.text("ꈄ").font(bossFont), '\uEFF2'),
        SIZE_8(Component.text("ꈅ").font(bossFont), '\uEFF3'),
        SIZE_16(Component.text("ꈆ").font(bossFont), '\uEFF4'),
        SIZE_32(Component.text("ꈇ").font(bossFont), '\uEFF5'),
        SIZE_64(Component.text("ꈈ").font(bossFont), '\uEFF6'),
        SIZE_128(Component.text("ꈉ").font(bossFont), '\uEFF7');


        private final Component component;
        private char minusSpace;
    }
}
