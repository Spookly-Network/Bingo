package de.nehlen.bingo.util.fonts;

import lombok.Getter;
import net.kyori.adventure.key.Key;

@Getter
public abstract class ComponentFont {

    public Key key;

    public ComponentFont(String name) {
        key = Key.key(name);
    }
}
