package de.zayon.bingo.data.helper;

import de.zayon.bingo.data.helper.PickList;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Team {

    private ArrayList<Player> mates;
    private Integer maxSize;
    private Color teamColor;
    private Integer TeamID;
    private Location spawnLoc;
    private PickList pickList;

    public Team() {
        this.mates = new ArrayList<Player>();
        this.maxSize = 2;
        this.teamColor = Color.WHITE;
        this.spawnLoc = null;
        this.pickList = new PickList();
    }

    public Color getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(Color teamColor) {
        teamColor = teamColor;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public Integer getSize() {
        return mates.size();
    }

    public ArrayList<Player> getMates() {
        return mates;
    }

    public void addMate(Player mate) {
        this.mates.add(mate);
    }

    public void removeMate(Player mate) {
        this.mates.remove(mate);
    }

    public Integer getTeamID() {
        return TeamID;
    }

    public void setTeamID(Integer teamID) {
        TeamID = teamID;
    }

    public Location getSpawnLoc() {
        return spawnLoc;
    }

    public void setSpawnLoc(Location spawnLoc) {
        this.spawnLoc = spawnLoc;
    }

    public PickList getPickList() {
        return pickList;
    }

    public void setPickList(PickList pickList) {
        this.pickList = pickList;
    }
}
