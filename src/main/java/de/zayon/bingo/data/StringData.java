package de.zayon.bingo.data;

import de.zayon.bingo.Bingo;
import lombok.Getter;

public class StringData {

    @Getter private static String prefix = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.prefix", "§3Bingo §8◆ §7");

    @Getter private static String noPerms = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.noPerms", "Keine Rechte!");

    @Getter private static String noPerm = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.noPerms", "Darauf hast du keine Rechte.");

    @Getter private static String highlightColor = Bingo.getBingo().getGeneralConfig().getOrSetDefault("config.highlightColor", "§b");

    public static String combinate() { return prefix + noPerm; }
}
