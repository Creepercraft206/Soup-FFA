package Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class CoinsHandler {

    public static void setCoins(UUID player, int coins) {
        MySQLCoins.update("UPDATE coins SET coins='" + coins + "' WHERE UUID='" + player.toString() + "';");
    }

    public static int getCoins(UUID player) {
        int p = 0;

        try {
            PreparedStatement State = null;
            State = MySQLCoins.con.prepareStatement("SELECT * FROM coins WHERE UUID='" + player.toString() + "';");
            ResultSet Result = State.executeQuery();
            if (Result != null && Result.next()) {
                p = Integer.parseInt(Result.getString("coins"));
            }

            Result.close();
            State.close();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return p;
    }

    public static void addCoins(UUID player, int coins) {
        int c = getCoins(player);
        c += coins;
        setCoins(player, c);
    }

    public static void removeCoins(UUID player, int coins) {
        int c = getCoins(player);
        c -= coins;
        setCoins(player, c);
    }
}
