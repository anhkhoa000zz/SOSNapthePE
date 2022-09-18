package me.anhkhoaaa.sosnapthe.listener;

import lombok.Getter;
import me.anhkhoaaa.sosnapthe.SOSNapthePE;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocessListener implements Listener {
    @Getter
    private final SOSNapthePE plugin;

    public PlayerCommandPreprocessListener(SOSNapthePE plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        String command  = event.getMessage().substring(1);
        if (!command.equalsIgnoreCase("napthe")){
            return;
        }
        if (!plugin.getFloodgateApi().isFloodgatePlayer(event.getPlayer().getUniqueId())){
            return;
        }
        event.setMessage("/napthepe");
    }
}
