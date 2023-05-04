package Listeners;

import Utils.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class JoinListeners implements Listener {

    @EventHandler
    private void onJoin(PlayerJoinEvent e){
        e.setJoinMessage(null);
        Player p = e.getPlayer();
        LocationManager.useLocation(p,"Spawn");
        p.setGameMode(GameMode.SURVIVAL);
        for(int item = 1; item < 36; item++) {
            p.getInventory().addItem(ItemCreatorClass.createItemwithoutID(Material.MUSHROOM_SOUP,1,0,"§cSoup",true,null));
        }
        p.getInventory().setItem(0, ItemCreatorClass.createItemwithoutID(Material.IRON_SWORD,1,0,"§cSword",true,null));
        p.getInventory().setItem(13, ItemCreatorClass.createItemwithoutID(Material.BROWN_MUSHROOM,32,0,null,false,null));
        p.getInventory().setItem(14, ItemCreatorClass.createItemwithoutID(Material.RED_MUSHROOM,32,0,null,false,null));
        p.getInventory().setItem(15, ItemCreatorClass.createItemwithoutID(Material.BOWL,32,0,null,false,null));
        p.getInventory().setHelmet(ItemCreatorClass.createItemwithoutID(Material.IRON_HELMET,1,0,"§cHelmet",true,null));
        p.getInventory().setChestplate(ItemCreatorClass.createItemwithoutID(Material.IRON_CHESTPLATE,1,0,"§cChestplate",true,null));
        p.getInventory().setLeggings(ItemCreatorClass.createItemwithoutID(Material.IRON_LEGGINGS,1,0,"§cLeggings",true,null));
        p.getInventory().setBoots(ItemCreatorClass.createItemwithoutID(Material.IRON_BOOTS,1,0,"§cBoots",true,null));
        p.setHealth(20);
        p.setFoodLevel(20);

        // Stats
        if (!MySQLStats.isRegistered("SELECT * FROM kills WHERE UUID='" + e.getPlayer().getUniqueId().toString() + "';")) {
            MySQLStats.insert("kills", new String[]{e.getPlayer().getUniqueId().toString(), e.getPlayer().getName(), "0"});
        }
        if (!MySQLStats.isRegistered("SELECT * FROM tode WHERE UUID='" + e.getPlayer().getUniqueId().toString() + "';")) {
            MySQLStats.insert("tode", new String[]{e.getPlayer().getUniqueId().toString(), e.getPlayer().getName(), "0"});
        }
        // Coins
        if (!MySQLCoins.isRegistered("SELECT * FROM coins WHERE UUID='" + e.getPlayer().getUniqueId().toString() + "';")) {
            MySQLCoins.insert("coins", new String[]{e.getPlayer().getUniqueId().toString(), e.getPlayer().getName(), "0"});
        }
        org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
        final Scoreboard board = manager.getNewScoreboard();
        final Objective objective = board.registerNewObjective("SOUP-FFA", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§c§lSOUP-FFA");
        objective.getScore("§1").setScore(10);
        objective.getScore("§8» §7Kills").setScore(9);
        objective.getScore("§e" + StatsHandler.getKills(p.getUniqueId())).setScore(8);
        objective.getScore("§2").setScore(7);
        objective.getScore("§8» §7Deaths").setScore(6);
        objective.getScore("§e" + StatsHandler.getTode(p.getUniqueId())).setScore(5);
        objective.getScore("§3").setScore(4);
        objective.getScore("§8» §7K/D").setScore(3);
        double kd = (double) StatsHandler.getKills(p.getUniqueId()) / (double) StatsHandler.getTode(p.getUniqueId());
        objective.getScore("§e" + Math.round(kd * 100.0) / 100.0).setScore(2);
        objective.getScore("§4").setScore(1);
        objective.getScore("§6HG-Practice.de").setScore(0);
        p.setScoreboard(board);
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent e){
        e.setQuitMessage(null);
    }
}
