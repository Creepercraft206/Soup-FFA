package Utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    public static void onJoin(Player p){
        File ordner = new File("plugins//SoupFFA//Stats//");
        File file = new File("plugins//SoupFFA//Stats//" + p.getUniqueId() + ".yml");
        if(!ordner.exists()){
            ordner.mkdir();
        }
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addKills(Player p){
        File file = new File("plugins//SoupFFA//Stats//" + p.getUniqueId() + ".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("kills",cfg.getInt("kills")+1);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addDeaths(Player p){
        File file = new File("plugins//SoupFFA//Stats//" + p.getUniqueId() + ".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        cfg.set("deaths",cfg.getInt("deaths")+1);
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getKills(Player p){
        File file = new File("plugins//SoupFFA//Stats//" + p.getUniqueId() + ".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        int kills = cfg.getInt("kills");
        return kills;
    }

    public static int getDeaths(Player p){
        File file = new File("plugins//SoupFFA//Stats//" + p.getUniqueId() + ".yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        int deaths = cfg.getInt("deaths");
        return deaths;
    }
}
