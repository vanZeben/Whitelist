package com.imdeity.whitelist;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Whitelist extends JavaPlugin {

    protected final Logger log = Logger.getLogger("Minecraft");

    private static WhitelistPlayerListener playerListener = null;
    public static MySQLConnection database = null;

    @Override
    public void onEnable() {

        loadSettings();

        database = new MySQLConnection(this);
        playerListener = new WhitelistPlayerListener(this);
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_LOGIN,
                playerListener, Priority.Low, this);

        out(this.getDescription().getVersion() + " Enabled");
    }

    @Override
    public void onDisable() {
        out(this.getDescription().getVersion() + " Disabled");
    }

    public void out(String message) {
        PluginDescriptionFile pdfFile = this.getDescription();
        log.info("[" + pdfFile.getName() + "] " + message);
    }

    public String getRootFolder() {
        if (this != null)
            return this.getDataFolder().getPath();
        else
            return "";
    }

    public boolean loadSettings() {
        try {
            FileMgmt.checkFolders(new String[] { getRootFolder(),
                    getRootFolder() + FileMgmt.fileSeparator() + "" });
            Settings.loadConfig(getRootFolder() + FileMgmt.fileSeparator()
                    + "config.yml", "/config.yml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean isOnWhitelist(String playerName) {
        String sql = "";

        sql = "SELECT * FROM `" + Settings.getMySQLDatabaseTableName()
                + "` WHERE `name` = '" + playerName + "'";

        HashMap<Integer, ArrayList<String>> check = database.Read(sql);

        if (!check.isEmpty()) {
            return true;
        }
        return false;
    }

}
