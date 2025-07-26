package hgpractice.soupffa;

import Commands.StatsCmd;
import Commands.setLocCmd;
import Listeners.JoinListeners;
import Listeners.PlayerListeners;
import Listeners.SoupListener;
import Utils.*;
import de.hgpractice.hgpgameapi.FileConfiguration.ConfigHandler;
import de.hgpractice.hgpgameapi.Items.ItemCreator;
import de.hgpractice.hgpgameapi.Player.DamageNerf;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class SoupFFA extends JavaPlugin {

    public static int soupHeal;
    public static ItemStack[] undroppableItems;
    public static int killCoins = 10;

    private static MySQLCoins sqlCoins;
    private static MySQLStats sqlStats;
    private static GameHandler gameHandler;
    private static RegionHandler regionHandler;

    private static ConfigHandler configHandler;
    private static ConfigHandler dbConfigHandler;

    public static MySQLCoins getSqlCoins() {
        return sqlCoins;
    }

    public static MySQLStats getSqlStats() {
        return sqlStats;
    }

    public static GameHandler getGameHandler() {
        return gameHandler;
    }

    public static RegionHandler getRegionHandler() {
        return regionHandler;
    }

    @Override
    public void onEnable() {
        System.out.print("\u001b[37m    Enabled\u001b[0m \u001b[31mSoupFFA\u001b[0m \u001b[33mV." + getDescription().getVersion() + " by " + getDescription().getAuthors().get(0) + "\u001b[0m");

        // Initialize settings config
        configHandler = new ConfigHandler("config", "plugins//SoupFFA");
        createConfig();

        // Initialize handlers
        gameHandler = new GameHandler(configHandler);
        regionHandler = new RegionHandler();

        // Initialize database config
        dbConfigHandler = new ConfigHandler("DatabaseSettings", "plugins//SoupFFA");
        createDBConfig();

        // Register events
        getServer().getPluginManager().registerEvents(new JoinListeners(), this);
        getServer().getPluginManager().registerEvents(new SoupListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);

        // Register commands
        getCommand("stats").setExecutor(new StatsCmd());
        getCommand("setloc").setExecutor(new setLocCmd());

        // Connect to databases
        try {
            sqlCoins = new MySQLCoins(
                    (String) dbConfigHandler.getSetting("Coins.Host"),
                    (int) dbConfigHandler.getSetting("Coins.Port"),
                    (String) dbConfigHandler.getSetting("Coins.Database"),
                    (String) dbConfigHandler.getSetting("Coins.Username"),
                    (String) dbConfigHandler.getSetting("Coins.Password")
            );
            sqlCoins.connect();
            sqlCoins.createTable();
            getLogger().info("Connected to coins database successfully.");
        } catch (Exception e) {
            getLogger().severe("Failed to connect to coins database: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            sqlStats = new MySQLStats(
                    (String) dbConfigHandler.getSetting("Stats.Host"),
                    (int) dbConfigHandler.getSetting("Stats.Port"),
                    (String) dbConfigHandler.getSetting("Stats.Database"),
                    (String) dbConfigHandler.getSetting("Stats.Username"),
                    (String) dbConfigHandler.getSetting("Stats.Password")
            );
            sqlStats.connect();
            sqlStats.createTable();
            getLogger().info("Connected to stats database successfully.");
        } catch (Exception e) {
            getLogger().severe("Failed to connect to stats database: " + e.getMessage());
            e.printStackTrace();
        }

        // Load settings
        soupHeal = (int) configHandler.getSetting("Soup-heal");
        killCoins = (int) configHandler.getSetting("Kill-Coins");

        Messages.prefix = configHandler.getSetting("Prefix").toString().replace("&", "§");
        Messages.noPerms = configHandler.getSetting("No-Permissions").toString().replace("&", "§").replace("%prefix%", Messages.prefix);

        Messages.setLocation = configHandler.getSetting("Set-Location").toString().replace("&", "§").replace("%prefix%", Messages.prefix);
        Messages.locationUnavailable = configHandler.getSetting("Location-Unavailable").toString().replace("&", "§").replace("%prefix%", Messages.prefix);
        Messages.playerNotFound = configHandler.getSetting("Player-Not-Found").toString().replace("&", "§").replace("%prefix%", Messages.prefix);
        Messages.locationNotSet = configHandler.getSetting("Location-Not-Set").toString().replace("&", "§").replace("%prefix%", Messages.prefix);
        Messages.playerDeath = configHandler.getSetting("Player-Death").toString().replace("&", "§").replace("%prefix%", Messages.prefix);
        Messages.playerDeathByPlayer = configHandler.getSetting("Player-Death-By-Player").toString().replace("&", "§").replace("%prefix%", Messages.prefix);
        Messages.playerDeathKiller = configHandler.getSetting("Player-Death-Killer").toString().replace("&", "§").replace("%prefix%", Messages.prefix);

        int statsMsgLine = 1;
        Messages.statsMessage = "";
        while (configHandler.getSetting("Stats-Message." + statsMsgLine) != null) {
            Messages.statsMessage += configHandler.getSetting("Stats-Message." + statsMsgLine).toString().replace("&", "§");
            if (configHandler.getSetting("Stats-Message." + (statsMsgLine + 1)) != null) {
                Messages.statsMessage += "\n";
            }
            statsMsgLine++;
        }

        Messages.ScoreboardTitle = configHandler.getSetting("Scoreboard.Title").toString().replace("&", "§");
        int scoreBoardLine = 1;
        while (configHandler.getSetting("Scoreboard.Line." + scoreBoardLine) != null) {
            Messages.ScoreboardLines[scoreBoardLine - 1] = configHandler.getSetting("Scoreboard.Line." + scoreBoardLine).toString().replace("&", "§");
            scoreBoardLine++;
        }

        String[] undroppableItemsArray = configHandler.getSetting("Undroppable.Items").toString().split(",");
        undroppableItems = new ItemStack[undroppableItemsArray.length];
        for (int i = 0; i < undroppableItemsArray.length; i++) {
            undroppableItems[i] = new ItemCreator(Material.getMaterial(undroppableItemsArray[i].trim()), 1, 0, null, false, null).get();
        }

        // Enable damage nerf
        DamageNerf.damageNerfEnabled = true;
    }

    @Override
    public void onDisable() {
        sqlCoins.close();
        sqlStats.close();
        System.out.print("\u001b[37m    Disabled\u001b[0m \u001b[31mSoupFFA\u001b[0m \u001b[33mV." + getDescription().getVersion() + " by " + getDescription().getAuthors().get(0) + "\u001b[0m");
    }

    private void createConfig() {
        // Settings
        configHandler.addSetting("Soup-heal", 7);
        configHandler.addSetting("Kill-Coins", 10);

        // Normal messages
        configHandler.addSetting("Prefix", "&cSoup-FFA &8|&7");
        configHandler.addSetting("No-Permissions", "%prefix% &cDazu hast du keine Rechte!");
        configHandler.addSetting("Set-Location", "%prefix% &7Du hast die Location &6%location% &7erfolgreich gesetzt!");
        configHandler.addSetting("Location-Unavailable", "%prefix% &cErlaubte Locations: Spawn, Pos1, Pos2");
        configHandler.addSetting("Player-Not-Found", "%prefix% &cDer Spieler &6%player% &cwurde nicht gefunden!");
        configHandler.addSetting("Location-Not-Set", "%prefix% &cDie Location &6%location% &cwurde noch nicht gesetzt! Melde dies bitte einem Admin!");
        configHandler.addSetting("Player-Death", "%prefix% &cDu bist gestorben!");
        configHandler.addSetting("Player-Death-By-Player", "%prefix% &cDu wurdest von &6%killer% &cgetötet!");
        configHandler.addSetting("Player-Death-Killer", "%prefix% &7Du hast &6%dead% &7getötet! &8[&e+%coins% Coins&8]");

        // Stats message
        configHandler.addSetting("Stats-Message.1", "&8&m-----------------------------------");
        configHandler.addSetting("Stats-Message.2", "&7» &cSoup-FFA &7Stats von &6%player%");
        configHandler.addSetting("Stats-Message.3", "&7Kills: &6%kills%");
        configHandler.addSetting("Stats-Message.4", "&7Tode: &6%deaths%");
        configHandler.addSetting("Stats-Message.5", "&7K/D: &6%kd%");
        configHandler.addSetting("Stats-Message.6", "&8&m-----------------------------------");

        // Scoreboard
        configHandler.addSetting("Scoreboard.Title", "&c&lSOUP-FFA");
        configHandler.addSetting("Scoreboard.Line.1", "&1");
        configHandler.addSetting("Scoreboard.Line.2", "&8» &7Kills");
        configHandler.addSetting("Scoreboard.Line.3", "&e%kills%");
        configHandler.addSetting("Scoreboard.Line.4", "&2");
        configHandler.addSetting("Scoreboard.Line.5", "&8» &7Deaths");
        configHandler.addSetting("Scoreboard.Line.6", "&e%deaths%");
        configHandler.addSetting("Scoreboard.Line.7", "&3");
        configHandler.addSetting("Scoreboard.Line.8", "&8» &7K/D");
        configHandler.addSetting("Scoreboard.Line.9", "&e%kd%");
        configHandler.addSetting("Scoreboard.Line.10", "&4");
        configHandler.addSetting("Scoreboard.Line.11", "&6HG-Practice.de");

        // Equip
        configHandler.addSetting("Equip.Armor.Helmet", "IRON_HELMET");
        configHandler.addSetting("Equip.Armor.Chestplate", "IRON_CHESTPLATE");
        configHandler.addSetting("Equip.Armor.Leggings", "IRON_LEGGINGS");
        configHandler.addSetting("Equip.Armor.Boots", "IRON_BOOTS");
        configHandler.addSetting("Equip.Slot.0", "IRON_SWORD");
        configHandler.addSetting("Equip.Slot.13", "BROWN_MUSHROOM-32");
        configHandler.addSetting("Equip.Slot.14", "RED_MUSHROOM-32");
        configHandler.addSetting("Equip.Slot.15", "BOWL-32");
        configHandler.addSetting("Equip.Fill-Item", "MUSHROOM_SOUP");

        // Undroppable items
        configHandler.addSetting("Undroppable.Items", "IRON_HELMET,IRON_CHESTPLATE,IRON_LEGGINGS,IRON_BOOTS,IRON_SWORD,BROWN_MUSHROOM,RED_MUSHROOM,BOWL");

        configHandler.createConfig();
    }

    private void createDBConfig() {
        dbConfigHandler.addSetting("Coins.Host", "localhost");
        dbConfigHandler.addSetting("Coins.Port", 3306);
        dbConfigHandler.addSetting("Coins.Database", "FFA-Stats");
        dbConfigHandler.addSetting("Coins.Username", "root");
        dbConfigHandler.addSetting("Coins.Password", "password");

        dbConfigHandler.addSetting("Stats.Host", "localhost");
        dbConfigHandler.addSetting("Stats.Port", 3306);
        dbConfigHandler.addSetting("Stats.Database", "FFA-Stats");
        dbConfigHandler.addSetting("Stats.Username", "root");
        dbConfigHandler.addSetting("Stats.Password", "password");

        dbConfigHandler.createConfig();
    }
}
