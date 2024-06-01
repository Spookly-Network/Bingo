package de.nehlen.bingo.phases;

import de.nehlen.bingo.Bingo;
import de.nehlen.bingo.data.GameData;
import de.nehlen.bingo.data.StringData;
import de.nehlen.bingo.util.AbstractGamePhase;
import de.nehlen.spookly.team.Team;
import de.nehlen.spooklycloudnetutils.helper.CloudStateHelper;
import de.nehlen.spooklycloudnetutils.helper.CloudWrapperHelper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class TeleportPhase extends AbstractGamePhase {

    private ArrayList<Player> teleportQueue = new ArrayList<>();
    private final Bingo bingo;
    int counter = teleportQueue.size() - 1;

    public TeleportPhase(Bingo bingo) {
        super(100000);
        this.bingo = bingo;
    }

    public void startPhase() {
        Bukkit.getScheduler().runTask(bingo, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 1000000, 100, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 1000000, 100, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 1000000, 255, true));
                this.teleportQueue.add(player);
            });
        });

        scheduler = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bingo.getBingo(), () -> {
            if (teleportQueue.isEmpty()) {
                endPhase();
                bingo.getIngameCountdown().startPhase();
                return;
            }
            Player player = teleportQueue.get(0);
            Team team = GameData.getTeamCache().get(player);
            Location loc = (Location) team.memory().get("spawnLoc");
            player.teleport(loc);

            Bukkit.getOnlinePlayers().forEach(p -> {
                p.sendActionBar(Component.translatable("phase.teleport.teleporting").color(NamedTextColor.GRAY));
            });
            teleportQueue.remove(0);
        }, 0, 10);
    }

    public void endPhase() {
        Bukkit.getScheduler().runTaskAsynchronously(bingo, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10.0F, 1.0F);
                Bukkit.getScheduler().runTask(bingo, () -> {
                    player.removePotionEffect(PotionEffectType.DARKNESS);
                    player.removePotionEffect(PotionEffectType.SLOWNESS);
                    player.removePotionEffect(PotionEffectType.JUMP_BOOST);
                });
            });
        });
        CloudStateHelper.changeServiceMaxPlayers(GameData.getTeamAmount() * GameData.getTeamSize() + 20);
        CloudWrapperHelper.publishServiceInfoUpdate();
        Bukkit.broadcast(
                StringData.getPrefix()
                        .append(Component.translatable("phase.teleport.starting",
                                        Component.translatable("phase.teleport.now")
                                                .color(StringData.getHighlightColor()))
                                .color(NamedTextColor.GRAY)));
        Bukkit.getScheduler().cancelTask(this.scheduler);
    }
}
