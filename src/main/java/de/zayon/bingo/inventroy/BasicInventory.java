package de.zayon.bingo.inventroy;

import de.zayon.bingo.Bingo;
import de.zayon.bingo.data.GameData;
import de.zayon.bingo.data.GameState;
import de.zayon.bingo.data.StringData;
import de.zayon.bingo.data.TeamData;
import de.zayon.bingo.data.helper.Team;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class BasicInventory implements Listener {

    private final Bingo bingo;

    public BasicInventory(Bingo bingo) {
        this.bingo = bingo;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();

        if(GameState.state == GameState.LOBBY) {
            e.setCancelled(true);

            if(e.getView().getTitle().equalsIgnoreCase(StringData.getHighlightColor() +"Team auswahl")) {
                Integer team = Integer.parseInt(e.getCurrentItem().getItemMeta().getDisplayName().split("-")[1])-1;
                Team t = TeamData.getTeamCache().get(team);

                if(t.getMates().contains(p)) {
                    return;
                }

                if(t.getSize() >= t.getMaxSize()) {
                    p.sendMessage(StringData.getPrefix() + "§cLeider ist dieses Team schon voll.");
                } else {
                    if(TeamData.getPlayerTeamCache().containsKey(p)) {
                        TeamData.teamCache.get(TeamData.getPlayerTeamCache().get(p)).removeMate(p);
                    }

                    TeamData.playerTeamCache.put(p, t.getTeamID());
                    TeamData.teamCache.get(team).addMate(p);

                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    p.closeInventory();
                    p.sendMessage(StringData.getPrefix() + "Du bist dem "+StringData.getHighlightColor()+"Team-" + (team+1) + " §7beigereten.");
//                    Scoreboard.updateScoreboard(p);
                }
            }
        } else if(!GameData.getIngame().contains(p)) {
            e.setCancelled(true);
        } else {
            e.setCancelled(false);
        }

        if (e.getView().getTitle().contains("Diese Items musst du Sammeln")) {
            e.setCancelled(true);
        }
    }
}
