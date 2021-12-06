package de.nehlen.bingo.commands;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.countdowns.LobbyCountdown;
import de.nehlen.bingo.data.GameState;
import de.nehlen.bingo.data.StringData;
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
            int startCount = 0;


            if (player.hasPermission("bingo.start") || player.hasPermission("zayon.vip+")) {
                startCount = 15;
            } else if (player.hasPermission("vip")) {
                startCount = 60;
            } else {
                player.sendMessage(StringData.getNoPerm());
                return false;
            }


            if (GameState.state == GameState.LOBBY) {
                if (Bukkit.getOnlinePlayers().size() >= 2) {
                    if (bingo.getLobbyCountdown().counter > startCount) {
                        Bukkit.getScheduler().cancelTasks(this.bingo);
                        this.bingo.getLobbyCountdown().startLobbyCountdown(false);
                        LobbyCountdown.counter = startCount;
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
            commandSender.sendMessage("Dieser Befehl kann nur Ingame ausgeführt werden.");
        }

        return false;
    }
}
