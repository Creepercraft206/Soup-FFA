package Listeners;

import Utils.*;
import de.hgpractice.hgpgameapi.Player.GamePlayer;
import hgpractice.soupffa.SoupFFA;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListeners implements Listener {

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Player p = e.getPlayer();

        // Check if positions were set
        if (LocationManager.locationExists("spawn")) {
            p.teleport(LocationManager.getLocation("spawn"));
        } else {
            p.sendMessage(Messages.locationNotSet.replace("%location%", "spawn"));
        }
        if (!LocationManager.locationExists("pos1")) p.sendMessage(Messages.locationNotSet.replace("%location%", "pos1"));
        if (!LocationManager.locationExists("pos2")) p.sendMessage(Messages.locationNotSet.replace("%location%", "pos2"));

        // Set player properties
        p.setGameMode(GameMode.SURVIVAL);
        SoupFFA.getGameHandler().setPvPInv(p);
        p.setHealth(20);
        p.setFoodLevel(20);
        GamePlayer gamePlayer = new GamePlayer(p);

        // Register player with stats and coins
        try {
            int existingKills = SoupFFA.getSqlStats().getStats(p.getUniqueId(), "kills");
            int existingDeaths = SoupFFA.getSqlStats().getStats(p.getUniqueId(), "deaths");
            gamePlayer.setAttribute("kills", existingKills);
            gamePlayer.setAttribute("deaths", existingDeaths);
        } catch (Exception ex) {
            try {
                SoupFFA.getSqlStats().query("INSERT IGNORE INTO stats (UUID, kills, deaths) VALUES ('" + p.getUniqueId() + "', '0', '0');");
                gamePlayer.setAttribute("kills", 0);
                gamePlayer.setAttribute("deaths", 0);
            } catch (Exception insertEx) {
                insertEx.printStackTrace();
            }
        }

        try {
            int existingCoins = SoupFFA.getSqlCoins().getCoins(p.getUniqueId());
            gamePlayer.setAttribute("coins", existingCoins);
        } catch (Exception ex) {
            try {
                SoupFFA.getSqlCoins().query("INSERT IGNORE INTO coins (UUID, coins) VALUES ('" + p.getUniqueId() + "', '0');");
                gamePlayer.setAttribute("coins", 0);
            } catch (Exception insertEx) {
                insertEx.printStackTrace();
            }
        }

        // Set scoreboard for player
        SoupFFA.getGameHandler().setScoreBoard(p);
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        GamePlayer.getPlayer(e.getPlayer()).remove();
    }
}
