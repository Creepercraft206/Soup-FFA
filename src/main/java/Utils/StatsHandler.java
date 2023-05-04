package Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class StatsHandler {

    public static void setKills(UUID player, int kills) {
        MySQLStats.update("UPDATE kills SET kills='" + kills + "' WHERE UUID='" + player.toString() + "';");
    }
    public static int getKills(UUID player) {
        int p = 0;

        try {
            PreparedStatement State = null;
            State = MySQLStats.con.prepareStatement("SELECT * FROM kills WHERE UUID='" + player.toString() + "';");
            ResultSet Result = State.executeQuery();
            if (Result != null && Result.next()) {
                p = Integer.parseInt(Result.getString("kills"));
            }

            Result.close();
            State.close();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return p;
    }

    public static void addKills(UUID player, int kills) {
        int c = getKills(player);
        c += kills;
        setKills(player, c);
    }

    public static void removeKills(UUID player, int kills) {
        int c = getKills(player);
        c -= kills;
        setKills(player, c);
    }

    public static void setTode(UUID player, int tode) {
        MySQLStats.update("UPDATE tode SET tode='" + tode + "' WHERE UUID='" + player.toString() + "';");
    }

    public static int getTode(UUID player) {
        int p = 0;

        try {
            PreparedStatement State = null;
            State = MySQLStats.con.prepareStatement("SELECT * FROM tode WHERE UUID='" + player.toString() + "';");
            ResultSet Result = State.executeQuery();
            if (Result != null && Result.next()) {
                p = Integer.parseInt(Result.getString("tode"));
            }

            Result.close();
            State.close();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return p;
    }

    public static void addTode(UUID player, int tode) {
        int c = getTode(player);
        c += tode;
        setTode(player, c);
    }

    public static void removeTode(UUID player, int tode) {
        int c = getTode(player);
        c -= tode;
        setTode(player, c);
    }
}
