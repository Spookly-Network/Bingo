package de.spookly.bingo.commands;

import de.spookly.bingo.SpooklyBingoPlugin;
import de.spookly.bingo.data.GameData;
import de.spookly.bingo.data.GameState;
import de.spookly.bingo.data.StringData;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    private final SpooklyBingoPlugin spooklyBingoPlugin;

    public StartCommand(SpooklyBingoPlugin spooklyBingoPlugin) {
        this.spooklyBingoPlugin = spooklyBingoPlugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {

            Player player = ((Player) commandSender);
            int startCount = 0;


            if (player.hasPermission("bingo.command.start-plus")) {
                startCount = 15;
            } else if (player.hasPermission("bingo.command.start")) {
                startCount = 30;
            } else {
                player.sendMessage(StringData.getNoPerm());
                return false;
            }


            if (GameState.state == GameState.LOBBY) {
                if (Bukkit.getOnlinePlayers().size() >= GameData.getMinPlayerToStartGame()) {
                    if (spooklyBingoPlugin.getLobbyPhase().getCounter() > startCount) {
                        this.spooklyBingoPlugin.getLobbyPhase().endPhase();
                        this.spooklyBingoPlugin.getLobbyPhase().startPhase();
                        this.spooklyBingoPlugin.getLobbyPhase().setCounter(startCount);
                    } else {
                        player.sendMessage(StringData.getPrefix().append(Component.text("Das Spiel ist bereits gestartet!").color(NamedTextColor.GRAY)));
                    }

                } else {
                    player.sendMessage(StringData.getPrefix().append(Component.text("Es sind nicht genügen Spieler Online!")));
                }
            } else {
                player.sendMessage(StringData.getPrefix().append(Component.text("Das Spiel hat bereits gestartet!")));
            }

        } else {
            commandSender.sendMessage("Dieser Befehl kann nur Ingame ausgeführt werden.");
        }

        return false;
    }
}
