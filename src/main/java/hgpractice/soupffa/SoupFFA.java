package hgpractice.soupffa;

import Commands.StatsCmd;
import Commands.setSpawnCmd;
import Listeners.JoinListeners;
import Listeners.PlayerListeners;
import Listeners.SoupListener;
import Utils.MySQLCoins;
import Utils.MySQLStats;
import org.bukkit.plugin.java.JavaPlugin;

public final class SoupFFA extends JavaPlugin {

    public static String prefix = "§cSoup-FFA §8| §7";
    public static String noperms = "§cSoup-FFA §8| §cDazu hast du keine Rechte!";
    public static SoupFFA instance;

    public static SoupFFA getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new JoinListeners(), this);
        getServer().getPluginManager().registerEvents(new SoupListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
        getCommand("stats").setExecutor(new StatsCmd());
        getCommand("setspawn").setExecutor(new setSpawnCmd());
        MySQLStats.connect();
        MySQLStats.createTables();
        MySQLCoins.connect();
        MySQLCoins.createTables();
    }

    @Override
    public void onDisable() {
        if (MySQLStats.isConnected()) {
            MySQLStats.close();
        }
        if (MySQLCoins.isConnected()){
            MySQLCoins.close();
        }
    }
}
