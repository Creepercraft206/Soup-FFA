package Commands;

import Utils.Messages;
import de.hgpractice.hgpgameapi.Player.GamePlayer;
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
                getStats(p, p);
            } else {
                OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(args[0]);
                if (target == null || !target.hasPlayedBefore()) {
                    p.sendMessage(Messages.playerNotFound.replace("%player%", args[0]));
                    return false;
                }
                getStats(p, target.getPlayer());
            }
        }
        return false;
    }

    private void getStats(Player receiving, Player target) {
        GamePlayer gamePlayer = GamePlayer.getPlayer(target);
        int kills = (int) gamePlayer.getAttribute("kills");
        int deaths = (int) gamePlayer.getAttribute("deaths");
        double kd = deaths == 0 ? kills : (double) kills / deaths;
        String[] msgLines = Messages.statsMessage.split("\n");
        for (String msgLine : msgLines) {
            receiving.sendMessage(msgLine
                    .replace("%player%", target.getName())
                    .replace("%kills%", String.valueOf(SoupFFA.getSqlStats().getStats(target.getUniqueId(), "kills")))
                    .replace("%deaths%", String.valueOf(SoupFFA.getSqlStats().getStats(target.getUniqueId(), "deaths")))
                    .replace("%kd%", String.valueOf(Math.round(kd * 100.0) / 100.0)));
        }
    }
}