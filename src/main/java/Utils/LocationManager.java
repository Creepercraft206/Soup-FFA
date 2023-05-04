package Utils;

import hgpractice.soupffa.SoupFFA;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class LocationManager {

    public static void setLocation(String name, Player p){
        File ordner = new File("plugins//SoupFFA//");
        File file = new File("plugins//SoupFFA//" + name + ".yml");

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

        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        Location loc = p.getLocation();

        cfg.set("X", loc.getX());
        cfg.set("Y", loc.getY());
        cfg.set("Z", loc.getZ());
        cfg.set("Welt", loc.getWorld().getName());
        cfg.set("Yaw", loc.getYaw());
        cfg.set("Pitch", loc.getPitch());

        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void useLocation(Player p, String name){
        File file = new File("plugins//SoupFFA//" + name + ".yml");

        if(!file.exists()){
            p.sendMessage(SoupFFA.prefix + "Die Location wurde nicht gefunden");
        }

        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        World welt = Bukkit.getWorld(cfg.getString("Welt"));
        double yaw = cfg.getDouble("Yaw");
        double pitch = cfg.getDouble("Pitch");

        p.teleport(new Location(welt, cfg.getDouble("X"), cfg.getDouble("Y"), cfg.getDouble("Z"), (float) yaw, (float) pitch));
    }

}
