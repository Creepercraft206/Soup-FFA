package Utils;

import de.hgpractice.hgpgameapi.SQL.SQLHandler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class MySQLStats extends SQLHandler {

    public MySQLStats(String host, int port, String database, String username, String password) {
        super(host, port, database, username, password);
    }

    @Override
    public void createTable() {
        query("CREATE TABLE IF NOT EXISTS stats (UUID VARCHAR(100) UNIQUE, kills INT, deaths INT);");
    }

    public void setStats(UUID uuid, String column, int value) {
        query("UPDATE stats SET " + column + "='" + value + "' WHERE UUID='" + uuid + "';");
    }

    public int getStats(UUID uuid, String column) {
        int p = 0;
        PreparedStatement State = null;
        ResultSet rs = null;
        try {
            rs = getQueryResult("SELECT " + column + " FROM stats WHERE UUID='" + uuid + "';");
            if (rs != null && rs.next()) {
                p = Integer.parseInt(rs.getString(column));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (State != null) State.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return p;
    }

    public void addStats(UUID uuid, String column, int value) {
        int c = getStats(uuid, column);
        c += value;
        setStats(uuid, column, c);
    }
}
