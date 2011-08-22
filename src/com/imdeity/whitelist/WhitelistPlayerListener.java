package com.imdeity.whitelist;

import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;

public class WhitelistPlayerListener extends PlayerListener{

    private Whitelist plugin;
    
    public WhitelistPlayerListener(Whitelist instance) {
      plugin = instance;
    }
    
    @Override
    public void onPlayerLogin(PlayerLoginEvent event) {
        String playerName = event.getPlayer().getName();
        plugin.out(playerName + " is trying to join...");
        if (plugin.isOnWhitelist(playerName)) {
            plugin.out(playerName + " allowed");
        } else {
            plugin.out(playerName + " disconnected.");
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Settings.getWhitelistMessage());
        }
    }
}

