package com.imdeity.whitelist;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class Whitelist extends JavaPlugin implements CommandExecutor {

    protected final Logger log = Logger.getLogger("Minecraft");

    private static WhitelistPlayerListener playerListener = null;
    public static MySQLConnection database = null;
    public static PermissionHandler permissions = null;

    @Override
    public void onEnable() {

        loadSettings();
        checkPlugins();
        database = new MySQLConnection(this);
        playerListener = new WhitelistPlayerListener(this);
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_LOGIN,
                playerListener, Priority.Low, this);

        getCommand("whitelist").setExecutor(this);
        out(this.getDescription().getVersion() + " Enabled");

    }

    @Override
    public void onDisable() {
        out(this.getDescription().getVersion() + " Disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String args[]) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (permissions.has(player, "whitelist")) {
                parseCommand(player, args);
                return true;
            }
            return false;
        } else {
            return false;
        }
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

    private void checkPlugins() {
        Plugin tmp;
        tmp = getServer().getPluginManager().getPlugin("Permissions");

        if (tmp != null)
            permissions = ((Permissions) tmp).getHandler();
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

    private void parseCommand(Player player, String[] args) {
        if (args.length == 1) {
            if (isOnWhitelist(args[0])) {
                player.sendMessage("[WhiteList] " + ChatColor.RED + args[0]
                        + " is already whitelisted.");
                return;

            }
            writeToMySQL(args[0]);
            getServer().broadcastMessage(
                    ChatColor.GRAY + "[" + ChatColor.RED + "*ImDeity*"
                            + ChatColor.GRAY + "] " + ChatColor.YELLOW
                            + args[0] + ChatColor.WHITE + " whitelisted by "
                            + ChatColor.AQUA + player.getName());
        } else {
            player.sendMessage("[Whitelist] " + ChatColor.RED
                    + "You need to enter a name.");
        }
    }

    private void writeToMySQL(String name) {
        String sql = "";

        sql = ("INSERT INTO `whitelist` " + " (`name`) " + " VALUES (?);");
        database.Write(sql, name);
    }
}
