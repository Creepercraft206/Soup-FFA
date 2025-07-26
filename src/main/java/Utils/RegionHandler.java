package Utils;

import org.bukkit.Location;

public class RegionHandler {

    public RegionHandler() {}

    public boolean checkRegion(Location checkLoc, Location loc1, Location loc2) {
        Location min = getMinLoc(loc1, loc2);
        Location max = getMaxLoc(loc1, loc2);

        if (min == null || max == null ||
            min.getWorld() == null || max.getWorld() == null ||
            min.getBlockY() > checkLoc.getBlockY() || max.getBlockY() < checkLoc.getBlockY() ||
            !checkLoc.getWorld().getName().equalsIgnoreCase(min.getWorld().getName())
        ) return false;

        int hx = checkLoc.getBlockX();
        int hz = checkLoc.getBlockZ();
        if (hx < min.getBlockX() || hx > max.getBlockX() || hz < min.getBlockZ() || hz > max.getBlockZ()) return false;

        return true;
    }

    private Location getMinLoc(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null || loc1.getWorld() == null || loc2.getWorld() == null) return null;

        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

        if (!loc1.getWorld().getName().equals(loc2.getWorld().getName())) return null;

        return new Location(loc1.getWorld(), minX, minY, minZ);
    }

    private Location getMaxLoc(Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null || loc1.getWorld() == null || loc2.getWorld() == null) return null;

        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        if (!loc1.getWorld().getName().equals(loc2.getWorld().getName())) return null;

        return new Location(loc1.getWorld(), maxX, maxY, maxZ);
    }
}
