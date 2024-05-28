package de.nehlen.bingo;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.util.UTF8ResourceBundleControl;

import java.util.Locale;
import java.util.ResourceBundle;

public class BingoRegistry {

    private Bingo bingo;

    public BingoRegistry(Bingo bingo) {
        this.bingo = bingo;
    }

    public void registerTranslatables() {
        TranslationRegistry registry = TranslationRegistry.create(Key.key("spookly_bingo:value"));
        ResourceBundle bundleUS = ResourceBundle.getBundle("spookly_bingo.Translations", Locale.US, UTF8ResourceBundleControl.get());
        ResourceBundle bundleDE = ResourceBundle.getBundle("spookly_bingo.Translations", Locale.GERMANY, UTF8ResourceBundleControl.get());

        registry.registerAll(Locale.US, bundleUS, true);
        registry.registerAll(Locale.GERMAN, bundleDE, true);

        GlobalTranslator.translator().addSource(registry);
    }
}
