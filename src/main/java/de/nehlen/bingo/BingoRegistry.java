package de.nehlen.bingo;

import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.helper.PickList;
import de.nehlen.spookly.Spookly;
import de.nehlen.spookly.configuration.ConfigurationWrapper;
import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.util.UTF8ResourceBundleControl;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class BingoRegistry {

    private Bingo bingo;

    @Getter private ConfigurationWrapper TEAMS_CONFIG;
    private static final List<TextColor> TEAM_COLORS = List.of(
            NamedTextColor.RED,
            TextColor.fromHexString("#448400"),
            TextColor.fromHexString("#00aeff"),
            TextColor.fromHexString("#ffb100"),
            NamedTextColor.LIGHT_PURPLE,
            TextColor.fromHexString("#ff5733"),
            TextColor.fromHexString("#009688"),
            TextColor.fromHexString("#9c27b0"),
            TextColor.fromHexString("#c62828"));

    public BingoRegistry(Bingo bingo) {
        this.bingo = bingo;
        this.TEAMS_CONFIG = Spookly.getServer().createConfiguration(new File(bingo.getDataFolder(), "team_settings.yml"));
    }

    public void registerTranslations() {
        TranslationRegistry registry = TranslationRegistry.create(Key.key("spookly_bingo:value"));
        ResourceBundle bundleUS = ResourceBundle.getBundle("spookly_bingo.Translations", Locale.US, UTF8ResourceBundleControl.get());
        ResourceBundle bundleDE = ResourceBundle.getBundle("spookly_bingo.Translations", Locale.GERMANY, UTF8ResourceBundleControl.get());

        registry.registerAll(Locale.US, bundleUS, true);
        registry.registerAll(Locale.GERMAN, bundleDE, true);

        GlobalTranslator.translator().addSource(registry);
    }

    public void registerTeams() {
        for (int i = 0; i < GameData.getTeamAmount(); i++) {
            Component prefix = MiniMessage.miniMessage().deserialize(TEAMS_CONFIG.getOrSetDefault(i + ".prefix", "prefix-team-" + i));
            Component icon = MiniMessage.miniMessage().deserialize(TEAMS_CONFIG.getOrSetDefault(i + ".icon", "icon-team-" + i));
            TextColor color = TextColor.fromHexString(TEAMS_CONFIG.getOrSetDefault(i + ".color", "#FFFFFF"));
            PickList picklist = new PickList(GameData.getItemsToFind());

            Spookly.getTeamManager().registerTeam(Spookly.buildTeam()
                    .display(Spookly.getServer().createTeamDisplay(icon, prefix, color))
                    .teamName(Component.text("Team-" + (i + 1))
                            .color(color))
                    .maxTeamSize(GameData.getTeamSize())
                    .tabSortId((i + 1))
                    .addToMemory("picklist", picklist).build());
        }
    }
}
