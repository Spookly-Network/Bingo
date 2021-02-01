package de.zayon.bingo.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.zayon.bingo.data.GameData;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

public class UtilFunctions {

    public static void ActionBar(Player p, String message) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(message).create());
    }

    public static String getRandomStringOutList(List<String> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    public static Location getRandomLocation(String w) {
        World world = Bukkit.getWorld(w);
        Random rand = new Random();
        // Adjust the range max for the maximum X and Z value, and the range min for the minimum X and Z value.
        int rangeMax = GameData.getWorldSize()/2;
        int rangeMin = makeIntNegative(GameData.getWorldSize()/2);

        int X = rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
        int Z = rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
        int Y = world.getHighestBlockYAt(X, Z);

        return new Location(world, X, Y, Z).add(0.5, 0, 0.5);
    }

    public static Integer makeIntNegative(Integer value) {
        return 0-value;
    }

    public static void debug(Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(object));
    }


}
