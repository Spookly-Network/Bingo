package de.zayon.bingo.commands;

import de.zayon.bingo.Bingo;
import de.zayon.bingo.countdowns.LobbyCountdown;
import de.zayon.bingo.data.GameState;
import de.zayon.bingo.data.StringData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    private final Bingo bingo;
    public StartCommand(Bingo bingo) {
        this.bingo = bingo;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender instanceof Player) {

            Player player = ((Player) commandSender);

            if (player.hasPermission("bingo.start") || player.hasPermission("vip")) {
                if (GameState.state == GameState.LOBBY) {
                    if (Bukkit.getOnlinePlayers().size() >= 2) {

                        if (bingo.getLobbyCountdown().counter > 10) {
                            Bukkit.getScheduler().cancelTasks(this.bingo);
                            this.bingo.getLobbyCountdown().startLobbyCountdown(true);
                        } else {
                            player.sendMessage(StringData.getPrefix() + "§7Das Spiel ist bereits gestartet!");
                        }

                    } else {
                        player.sendMessage(StringData.getPrefix() + "§7Es sind nicht genügen Spieler Online!");
                    }
                } else {
                    player.sendMessage(StringData.getPrefix() + "§7Das Spiel hat bereits gestartet!");
                }
            } else {
                player.sendMessage(StringData.getNoPerm());
            }

        } else {
            commandSender.sendMessage("Dieser Befehl kann nur Ingame ausgeführt werden.");
        }

        return false;
    }
}
