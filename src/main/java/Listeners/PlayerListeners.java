package Listeners;

import Utils.*;
import de.hgpractice.hgpgameapi.Player.GamePlayer;
import hgpractice.soupffa.SoupFFA;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListeners implements Listener {

    // Block events
    @EventHandler
    private void onBreak(BlockBreakEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    private void onPlace(BlockPlaceEvent e){
        e.setCancelled(true);
    }

    // Item dropping
    @EventHandler
    private void onDrop(PlayerDropItemEvent e) {
        ItemStack droppedItem = e.getItemDrop().getItemStack();

        for (ItemStack undroppableItem : SoupFFA.undroppableItems) {
            if (droppedItem.getType() == undroppableItem.getType()) {
                e.setCancelled(true);
                return;
            }
        }

        e.getItemDrop().remove();
    }
    @EventHandler
    private void onInv(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            if (e.getSlotType() == InventoryType.SlotType.ARMOR) {
                if (e.getCurrentItem() != null) {
                    for (ItemStack undroppableItem : SoupFFA.undroppableItems) {
                        if (e.getCurrentItem().getType() == undroppableItem.getType()) {
                            e.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
    }

    // Death events
    @EventHandler
    private void onDeath(PlayerDeathEvent e) {
        e.setDeathMessage(null);
        Player killer = e.getEntity().getKiller();
        Player dead = e.getEntity().getPlayer();
        if (killer == null){
            dead.sendMessage(Messages.playerDeath);
        } else {
            dead.sendMessage(Messages.playerDeathByPlayer.replace("%killer%", killer.getName()));
            killer.sendMessage(Messages.playerDeathKiller.replace("%dead%", dead.getName()).replace("%coins%", String.valueOf(SoupFFA.killCoins)));

            SoupFFA.getSqlStats().addStats(killer.getUniqueId(), "kills", 1);
            SoupFFA.getSqlCoins().addCoins(killer.getUniqueId(), SoupFFA.killCoins);

            GamePlayer killerGamePlayer = GamePlayer.getPlayer(killer);
            killerGamePlayer.setAttribute("kills", (int) killerGamePlayer.getAttribute("kills") + 1);
            killerGamePlayer.setAttribute("coins", (int) killerGamePlayer.getAttribute("coins") + SoupFFA.killCoins);

            SoupFFA.getGameHandler().updateScoreBoard(killer);
        }
        SoupFFA.getSqlStats().addStats(dead.getUniqueId(), "deaths", 1);

        GamePlayer deadGamePlayer = GamePlayer.getPlayer(dead);
        deadGamePlayer.setAttribute("deaths", (int) deadGamePlayer.getAttribute("deaths") + 1);

        e.getDrops().clear();
    }
    @EventHandler
    private void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (LocationManager.locationExists("spawn")) {
            e.setRespawnLocation(LocationManager.getLocation("spawn"));
            p.teleport(LocationManager.getLocation("spawn"));
        } else {
            p.sendMessage(Messages.locationNotSet.replace("%location%", "spawn"));
        }
        SoupFFA.getGameHandler().setPvPInv(p);
        SoupFFA.getGameHandler().setScoreBoard(p);
    }

    // Damage event
    @EventHandler
    private void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (LocationManager.locationExists("pos1") && LocationManager.locationExists("pos2")) {
                if (SoupFFA.getRegionHandler().checkRegion(p.getLocation(), LocationManager.getLocation("pos1"), LocationManager.getLocation("pos2"))) {
                    e.setCancelled(true);
                    return;
                }
            }
        }
        e.setCancelled(false);
    }
}
