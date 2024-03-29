package de.nehlen.bingo.commands;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.StringData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetspawnCommand implements CommandExecutor {
    private final Bingo bingo;

    public SetspawnCommand(Bingo bingo) {
        this.bingo = bingo;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player))
            return true;
        Player player = (Player)commandSender;
        if (!player.hasPermission("spookly.admin")) {
            player.sendMessage(StringData.combinate());
            return true;
        }
        if (args.length == 2 && args[0].equals("tp")) {
            Location location = Bukkit.getWorld(args[1]).getSpawnLocation();
            player.teleport(location);
            return false;
        }
        if (args.length == 0) {
            Location location = player.getLocation();
            this.bingo.getLocationConfig().getOrSetDefault("config.location.spawn", location);
            player.sendMessage(StringData.getPrefix() + "§7 Spawn gesetzt§8.");
        }
        return false;
    }
}
