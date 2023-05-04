package Listeners;

import Utils.*;
import hgpractice.soupffa.SoupFFA;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.util.List;

public class PlayerListeners implements Listener {

    @EventHandler
    private void onBreak(BlockBreakEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    private void onPlace(BlockPlaceEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    private void onDrop(PlayerDropItemEvent e){
        if (e.getItemDrop().getItemStack().getType() != Material.BOWL && e.getItemDrop().getItemStack().getType() != Material.MUSHROOM_SOUP){
            e.setCancelled(true);
        } else {
            e.getItemDrop().remove();
        }
        World world = e.getPlayer().getWorld();
        List<Entity> entList = world.getEntities();
        for(Entity current : entList){
            if (current instanceof Item){
                current.remove();
            }
        }
    }
    @EventHandler
    private void onDeath(PlayerDeathEvent e){
        e.setDeathMessage(null);
        Player killer = e.getEntity().getKiller();
        Player dead = e.getEntity().getPlayer();
        if (killer == null){
            dead.sendMessage(SoupFFA.prefix + "Du bist gestorben!");
        } else {
            dead.sendMessage(SoupFFA.prefix + "Du wurdest von §e" + killer.getName() + " §7getötet!");
            killer.sendMessage(SoupFFA.prefix + "Du hast §e" + dead.getName() + " §7getötet! §8[§e+10 Coins§8]");
            StatsHandler.addKills(killer.getUniqueId(),1);
            CoinsHandler.addCoins(killer.getUniqueId(),10);
        }
        StatsHandler.addTode(dead.getUniqueId(),1);
        e.getDrops().clear();
    }

    @EventHandler
    private void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        File file = new File("plugins//SoupFFA//Spawn.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        World welt = Bukkit.getWorld(cfg.getString("Welt"));
        double yaw = cfg.getDouble("Yaw");
        double pitch = cfg.getDouble("Pitch");
        e.setRespawnLocation(new Location(welt, cfg.getDouble("X"), cfg.getDouble("Y"), cfg.getDouble("Z"), (float) yaw, (float) pitch));
        for(int item = 1; item < 37; item++) {
            p.getInventory().addItem(ItemCreatorClass.createItemwithoutID(Material.MUSHROOM_SOUP, 1, 0, "§cSoup", true,null));
        }
        p.getInventory().setItem(0, ItemCreatorClass.createItemwithoutID(Material.IRON_SWORD,1,0,"§cSword",true,null));
        p.getInventory().setItem(13, ItemCreatorClass.createItemwithoutID(Material.BROWN_MUSHROOM,32,0,null,true,null));
        p.getInventory().setItem(14, ItemCreatorClass.createItemwithoutID(Material.RED_MUSHROOM,32,0,null,true,null));
        p.getInventory().setItem(15, ItemCreatorClass.createItemwithoutID(Material.BOWL,32,0,null,true,null));
        p.getInventory().setHelmet(ItemCreatorClass.createItemwithoutID(Material.IRON_HELMET,1,0,"§cHelmet",true,null));
        p.getInventory().setChestplate(ItemCreatorClass.createItemwithoutID(Material.IRON_CHESTPLATE,1,0,"§cChestplate",true,null));
        p.getInventory().setLeggings(ItemCreatorClass.createItemwithoutID(Material.IRON_LEGGINGS,1,0,"§cLeggings",true,null));
        p.getInventory().setBoots(ItemCreatorClass.createItemwithoutID(Material.IRON_BOOTS,1,0,"§cBoots",true,null));

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
}
