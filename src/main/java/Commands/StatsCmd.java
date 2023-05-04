package Commands;

import Utils.ConfigManager;
import Utils.StatsHandler;
import hgpractice.soupffa.SoupFFA;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCmd implements CommandExecutor {

    @Deprecated
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                double kd = (double) StatsHandler.getKills(p.getUniqueId()) / (double) StatsHandler.getTode(p.getUniqueId());
                p.sendMessage("§8§m----------------------------");
                p.sendMessage("§7» §cSoup-FFA §7Stats von " + p.getName());
                p.sendMessage("§7Kills: §6" + StatsHandler.getKills(p.getUniqueId()));
                p.sendMessage("§7Tode: §6" + StatsHandler.getTode(p.getUniqueId()));
                p.sendMessage("§7K/D: §6" + Math.round(kd * 100.0) / 100.0);
                p.sendMessage("§8§m----------------------------");
            } else {
                OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
                double kd = (double) StatsHandler.getKills(target.getUniqueId()) / (double) StatsHandler.getTode(target.getUniqueId());
                p.sendMessage("§8§m----------------------------");
                p.sendMessage("§7» §cSoup-FFA §7Stats von " + target.getName());
                p.sendMessage("§7Kills: §6" + StatsHandler.getKills(target.getUniqueId()));
                p.sendMessage("§7Tode: §6" + StatsHandler.getTode(target.getUniqueId()));
                p.sendMessage("§7K/D: §6" + Math.round(kd * 100.0) / 100.0);
                p.sendMessage("§8§m----------------------------");
            }
        }
        return false;
    }
}