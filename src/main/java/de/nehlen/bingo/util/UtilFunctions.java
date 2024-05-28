package de.nehlen.bingo.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.nehlen.bingo.data.GameData;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class UtilFunctions {

    public static void sendActionBar(Player player, Component message) {
        player.sendActionBar(message);
    }

    public static String getRandomStringOutList(List<String> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    public static Location getRandomLocation(String w) {
        World world = Bukkit.getWorld(w);
        Random rand = new Random();
        // Adjust the range max for the maximum X and Z value, and the range min for the minimum X and Z value.
        int rangeMax = GameData.getPlayerSpawnSize()/2;
        int rangeMin = makeIntNegative(GameData.getPlayerSpawnSize()/2);

        int X = rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
        int Z = rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
        int Y = world.getHighestBlockYAt(X, Z);

        return new Location(world, X, Y, Z).add(0.5, 0, 0.5);
    }

    public static Integer makeIntNegative(Integer value) {
        return 0-value;
    }
    public static String formatTime(Integer seconds) {
        LocalTime timeOfDay = LocalTime.ofSecondOfDay(seconds);
        String time = timeOfDay.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return time;
    }

    public static int getTeamInventorySize() {
        return (int)Math.min(1, Math.ceil((double) GameData.getTeamAmount() / 9));
    }
}
