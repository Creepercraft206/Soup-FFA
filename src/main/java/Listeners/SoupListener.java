package Listeners;

import hgpractice.soupffa.SoupFFA;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SoupListener implements Listener {

    @EventHandler
    public void onSoup(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (p.getItemInHand().getType() == Material.MUSHROOM_SOUP) {
                int heal = SoupFFA.soupHeal;
                if (p.getHealth() < 20) {
                    if (p.getHealth() + heal <= 20) {
                        p.setHealth(p.getHealth() + heal);
                    } else {
                        p.setHealth(20);
                    }
                    p.getItemInHand().setType(Material.BOWL);
                } else if (p.getFoodLevel() < 20) {
                    p.setFoodLevel(p.getFoodLevel() + heal);
                    p.getItemInHand().setType(Material.BOWL);
                }
            }
        }
    }
}
