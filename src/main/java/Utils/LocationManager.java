package Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LocationManager {

    public static void setLocation(String name, Location loc) {
        File folder = new File("plugins//SoupFFA//Locations//");
        File file = new File("plugins//SoupFFA//Locations//" + name + ".yml");

        if (!folder.exists()){
            folder.mkdir();
        }
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        cfg.set("X", loc.getX());
        cfg.set("Y", loc.getY());
        cfg.set("Z", loc.getZ());
        cfg.set("World", loc.getWorld().getName());
        cfg.set("Yaw", loc.getYaw());
        cfg.set("Pitch", loc.getPitch());

        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Location getLocation(String name) {
        File file = new File("plugins//SoupFFA//Locations//" + name + ".yml");
        if (file.exists()){
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

            World world = Bukkit.getWorld(cfg.getString("World"));
            double yaw = cfg.getDouble("Yaw");
            double pitch = cfg.getDouble("Pitch");
            return new Location(world, cfg.getDouble("X"), cfg.getDouble("Y"), cfg.getDouble("Z"), (float) yaw, (float) pitch);
        } else {
            return null;
        }
    }

    public static boolean locationExists(String name) {
        File file = new File("plugins//SoupFFA//Locations//" + name + ".yml");
        return file.exists();
    }

}
