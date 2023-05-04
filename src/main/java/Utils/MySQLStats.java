package Utils;

import java.sql.*;

public class MySQLStats {
    public static Connection con = null;

    public MySQLStats() {
    }

    public static void connect() {
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" + "localhost" + ":" + 3306 + "/" + "ffa" + "?autoReconnect=true", "root", "!@2Ramonsora");
                System.out.println("Die MySQL Verbindung wurde erfolgreich aufgebaut.");
            } catch (SQLException var1) {
                var1.printStackTrace();
            }
        }

    }

    public static void close() {
        if (isConnected()) {
            try {
                con.close();
                System.out.println("Die MySQL Verbindung wurde erfolgreich geschlossen.");
            } catch (SQLException var1) {
                var1.printStackTrace();
            }
        }

    }

    public static boolean isConnected() {
        return con != null;
    }

    public static void update(String query) {
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException var11) {
            var11.printStackTrace();
        } finally {
            try {
                ps.close();
            } catch (SQLException var10) {
                var10.printStackTrace();
            }

        }

    }

    public static void insert(String table, String[] values) {
        try {
            String in = "?";

            for(int i = 1; i < values.length; ++i) {
                in = String.valueOf(String.valueOf(in)) + ", ?";
            }

            PreparedStatement State = con.prepareStatement("INSERT INTO " + table + " values(" + in + ");");

            for(int index = 1; index <= values.length; ++index) {
                State.setString(index, values[index - 1]);
            }

            State.execute();
            State.close();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public static ResultSet getResult(String query) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static boolean isRegistered(String query) {
        boolean bool = false;

        try {
            ResultSet rs = getResult(query);

            try {
                bool = rs.next();
            } catch (SQLException var8) {
                bool = false;
            } finally {
                rs.close();
            }
        } catch (SQLException var10) {
            var10.printStackTrace();
        }

        return bool;
    }

    public static void createTables() {
        if (isConnected()) {
            try {
                con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS kills (UUID VARCHAR(100) UNIQUE, name VARCHAR(100), kills VARCHAR(100));");
                con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS tode (UUID VARCHAR(100) UNIQUE, name VARCHAR(100), tode VARCHAR(100));");
            } catch (SQLException var1) {
                var1.printStackTrace();
            }
        }

    }
}
