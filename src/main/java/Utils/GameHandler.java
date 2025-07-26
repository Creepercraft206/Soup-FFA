package Utils;

import de.hgpractice.hgpgameapi.FileConfiguration.ConfigHandler;
import de.hgpractice.hgpgameapi.Items.ItemCreator;
import de.hgpractice.hgpgameapi.Player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;

public class GameHandler {

    private final String scoreBoardTitel;
    private final HashMap<Integer, String> scoreBoardLines = new HashMap<Integer, String>();

    private final HashMap<Integer, ItemCreator> equip = new HashMap<Integer, ItemCreator>();
    private Material helmet;
    private Material chestplate;
    private Material leggings;
    private Material boots;
    private Material fillItem;

    public GameHandler(ConfigHandler configHandler) {
        // Load Scoreboard
        scoreBoardTitel = configHandler.getSetting("Scoreboard.Title").toString().replace("&", "§");
        int line = 1;
        while (configHandler.getSetting("Scoreboard.Line." + line) != null) {
            this.scoreBoardLines.put(line, configHandler.getSetting("Scoreboard.Line." + line).toString().replace("&", "§"));
            line++;
        }

        // Load Equip
        for (int i = 0; i < 36; i++) {
            if (configHandler.getSetting("Equip.Slot." + i) != null) {
                String slotSetting = (String) configHandler.getSetting("Equip.Slot." + i);
                if (slotSetting.contains("-")) {
                    String[] parts = slotSetting.split("-");
                    if (parts.length == 2) {
                        try {
                            int amount = Integer.parseInt(parts[1]);
                            this.equip.put(i, new ItemCreator(Material.getMaterial(parts[0]), amount, 0, null, false, null));
                        } catch (NumberFormatException e) {
                            Bukkit.getLogger().warning("Invalid amount for equip slot " + i + ": " + parts[1]);
                        }
                    } else {
                        Bukkit.getLogger().warning("Invalid format for equip slot " + i + ": " + slotSetting);
                    }
                } else {
                    this.equip.put(i, new ItemCreator(Material.getMaterial(slotSetting), 1, 0, null, false, null));
                }
            }
        }
        this.fillItem = configHandler.getSetting("Equip.Fill-Item") != "" ? Material.getMaterial((String) configHandler.getSetting("Equip.Fill-Item")) : Material.AIR;

        // Load Armor
        if (configHandler.getSetting("Equip.Armor.Helmet") != null) {
            this.helmet = Material.getMaterial((String) configHandler.getSetting("Equip.Armor.Helmet"));
        }
        if (configHandler.getSetting("Equip.Armor.Chestplate") != null) {
            this.chestplate = Material.getMaterial((String) configHandler.getSetting("Equip.Armor.Chestplate"));
        }
        if (configHandler.getSetting("Equip.Armor.Leggings") != null) {
            this.leggings = Material.getMaterial((String) configHandler.getSetting("Equip.Armor.Leggings"));
        }
        if (configHandler.getSetting("Equip.Armor.Boots") != null) {
            this.boots = Material.getMaterial((String) configHandler.getSetting("Equip.Armor.Boots"));
        }
    }

    public void setPvPInv(Player p) {
        for (int i = 0; i < 36; i++) {
            if (this.equip.get(i) != null) {
                ItemCreator itemCreator = this.equip.get(i);
                p.getInventory().addItem(itemCreator.get());
            } else if (this.fillItem != Material.AIR) {
                p.getInventory().addItem(new ItemCreator(this.fillItem, 1, 0, null, false, null).get());
            }
        }
        p.getInventory().setHelmet(this.helmet != null ? new ItemCreator(this.helmet, 1, 0, "§cHelmet", true, null).get() : null);
        p.getInventory().setChestplate(this.chestplate != null ? new ItemCreator(this.chestplate, 1, 0, "§cChestplate", true, null).get() : null);
        p.getInventory().setLeggings(this.leggings != null ? new ItemCreator(this.leggings, 1, 0, "§cLeggings", true, null).get() : null);
        p.getInventory().setBoots(this.boots != null ? new ItemCreator(this.boots, 1, 0, "§cBoots", true, null).get() : null);
    }

    public void setScoreBoard(Player p) {
        // Get stats from local storage
        GamePlayer gamePlayer = GamePlayer.getPlayer(p);
        Object killsObj = gamePlayer.getAttribute("kills");
        Object deathsObj = gamePlayer.getAttribute("deaths");
        Object coinsObj = gamePlayer.getAttribute("coins");
        int kills = killsObj != null ? (int) killsObj : 0;
        int deaths = deathsObj != null ? (int) deathsObj : 0;
        double kd = deaths > 0 ? (double) kills / (double) deaths : 0.0;
        int coins = coinsObj != null ? (int) coinsObj : 0;

        // Create scoreboard
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        final Scoreboard board = manager.getNewScoreboard();
        final Objective objective = board.registerNewObjective("SOUP-FFA", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Set scoreboard content
        objective.setDisplayName(this.scoreBoardTitel);
        for (int i = 1; i <= scoreBoardLines.size(); i++) {
            String lineContent = scoreBoardLines.get(i);
            if (lineContent == null) continue;

            // Create teams for placeholders to provide smooth data switching
            if (
                    lineContent.contains("%kills%") ||
                    lineContent.contains("%deaths%") ||
                    lineContent.contains("%kd%") ||
                    lineContent.contains("%player%") ||
                    lineContent.contains("%coins%")
            ) {
                Team team = board.registerNewTeam("line" + i);
                // Replace placeholders
                String replacedLine = lineContent
                        .replace("%kills%", String.valueOf(kills))
                        .replace("%deaths%", String.valueOf(deaths))
                        .replace("%kd%", String.valueOf(Math.round(kd * 100.0) / 100.0))
                        .replace("%player%", p.getName())
                        .replace("%coins%", String.valueOf(coins));
                lineContent = "§" + Integer.toHexString(i % 16) + "§r";
                team.addEntry(lineContent);
                team.setPrefix(replacedLine);
            }

            objective.getScore(lineContent).setScore(scoreBoardLines.size() + 1 - i);
        }
        p.setScoreboard(board);
    }

    public void updateScoreBoard(Player p) {
        // Load stats from local storage
        GamePlayer gamePlayer = GamePlayer.getPlayer(p);
        Object killsObj = gamePlayer.getAttribute("kills");
        Object deathsObj = gamePlayer.getAttribute("deaths");
        Object coinsObj = gamePlayer.getAttribute("coins");
        int kills = killsObj != null ? (int) killsObj : 0;
        int deaths = deathsObj != null ? (int) deathsObj : 0;
        double kd = deaths > 0 ? (double) kills / (double) deaths : 0.0;
        int coins = coinsObj != null ? (int) coinsObj : 0;

        Scoreboard board = p.getScoreboard();

        for (Team team : board.getTeams()) {
            if (team == null) continue;
            scoreBoardLines.forEach((index, line) -> {
                if (line.contains("%kills%")) {
                    String replacedLine = line.replace("%kills%", String.valueOf(kills));
                    team.setPrefix(replacedLine);
                }
                if (line.contains("%deaths%")) {
                    String replacedLine = line.replace("%deaths%", String.valueOf(deaths));
                    team.setPrefix(replacedLine);
                }
                if (line.contains("%kd%")) {
                    String replacedLine = line.replace("%kd%", String.valueOf(kd));
                    team.setPrefix(replacedLine);
                }
                if (line.contains("%player%")) {
                    String replacedLine = line.replace("%player%", p.getName());
                    team.setPrefix(replacedLine);
                }
                if (line.contains("%coins%")) {
                    String replacedLine = line.replace("%coins%", String.valueOf(coins));
                    team.setPrefix(replacedLine);
                }
            });
        }
    }
}
