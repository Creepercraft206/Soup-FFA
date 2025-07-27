package Commands;

import Utils.LocationManager;
import Utils.Messages;
import hgpractice.soupffa.SoupFFA;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class setLocCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("soupffa.setpos")) {
                if (args.length == 0) {
                    p.sendMessage(Messages.setLocNoArgs);
                } else if (args[0].equalsIgnoreCase("Spawn") || args[0].equalsIgnoreCase("Pos1") || args[0].equalsIgnoreCase("Pos2")) {
                    LocationManager.setLocation(args[0].toLowerCase(), p.getLocation());
                    p.sendMessage(Messages.setLocation.replace("%location%", args[0].toLowerCase()));
                } else {
                    p.sendMessage(Messages.locationUnavailable);
                }
            } else {
                p.sendMessage(Messages.noPerms);
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String string, String[] args) {
        ArrayList<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("Spawn");
            completions.add("Pos1");
            completions.add("Pos2");
        }
        return completions;
    }
}
