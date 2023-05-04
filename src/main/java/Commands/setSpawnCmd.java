package Commands;

import Utils.LocationManager;
import hgpractice.soupffa.SoupFFA;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class setSpawnCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.isOp()){
                LocationManager.setLocation(args[0], p);
                p.sendMessage(SoupFFA.prefix + "Du hast die Location §c" + args[0] + " §fgesetzt");
            } else {
                p.sendMessage(SoupFFA.noperms);
            }
        }
        return false;
    }
}
