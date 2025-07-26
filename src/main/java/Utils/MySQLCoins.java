package Utils;

import de.hgpractice.hgpgameapi.SQL.SQLHandler;

import java.sql.*;
import java.util.UUID;

public class MySQLCoins extends SQLHandler {

    public MySQLCoins(String host, int port, String database, String username, String password) {
        super(host, port, database, username, password);
    }

    @Override
    public void createTable() {
        query("CREATE TABLE IF NOT EXISTS coins (UUID VARCHAR(100) UNIQUE, coins VARCHAR(100));");
    }

    public void setCoins(UUID uuid, int coins) {
        query("UPDATE coins SET coins='" + coins + "' WHERE UUID='" + uuid + "';");
    }

    public int getCoins(UUID uuid) {
        int p = 0;
        try {
            ResultSet result = getQueryResult("SELECT coins FROM coins WHERE UUID='" + uuid + "';");
            if (result != null && result.next()) {
                p = Integer.parseInt(result.getString("coins"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    public void addCoins(UUID uuid, int coins) {
        int c = getCoins(uuid);
        c += coins;
        setCoins(uuid, c);
    }
}
