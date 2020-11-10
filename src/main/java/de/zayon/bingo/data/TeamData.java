package de.zayon.bingo.data;

import de.zayon.bingo.data.helper.Team;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeamData {

    @Getter public static ArrayList<Team> teamCache = new ArrayList<Team>();
    @Getter public static HashMap<Player, Integer> playerTeamCache = new HashMap<>();

}
