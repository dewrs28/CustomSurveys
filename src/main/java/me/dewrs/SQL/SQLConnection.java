package me.dewrs.SQL;

import me.dewrs.Utils.SetColor;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {
    private Connection connection;
    private String host;
    private int port;
    private String database;
    private String user;
    private String password;

    public SQLConnection(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
        try{
            synchronized (this){
                if(connection != null && !connection.isClosed()){
                    Bukkit.getConsoleSender().sendMessage(SetColor.setColor("&8[&eCustomSurveys&8] &cError! Could not connect to MySQL"));
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                try {
                    this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database, this.user, this.password);
                    Bukkit.getConsoleSender().sendMessage(SetColor.setColor("&8[&eCustomSurveys&8] &aConnected with MySQL"));
                } catch (SQLException ex){
                    Bukkit.getConsoleSender().sendMessage(SetColor.setColor("&8[&eCustomSurveys&8] &cError! Could not connect to MySQL"));
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
