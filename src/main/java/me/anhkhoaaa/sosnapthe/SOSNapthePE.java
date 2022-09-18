package me.anhkhoaaa.sosnapthe;

import lombok.Getter;
import me.anhkhoaaa.sosnapthe.command.NapTheCommand;
import me.anhkhoaaa.sosnapthe.listener.PlayerCommandPreprocessListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.Objects;
import java.util.logging.Level;

public final class SOSNapthePE extends JavaPlugin {
    @Getter
    private FloodgateApi floodgateApi;

    @Override
    public void onEnable() {
        if (!Bukkit.getPluginManager().isPluginEnabled("Thesieutoc") && Bukkit.getPluginManager().isPluginEnabled("Floodgate")) {
            Bukkit.getPluginManager().disablePlugin(this);
            getLogger().log(Level.WARNING, ChatColor.GREEN+"Please Install TheSieuToc + Floodgate!");
            return;

        }
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        reloadConfig();
        Objects.requireNonNull(getCommand("napthepe")).setExecutor(new NapTheCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerCommandPreprocessListener(this), this);
        floodgateApi = FloodgateApi.getInstance();
        getLogger().log(Level.INFO, ChatColor.GREEN+"Enable!");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, ChatColor.RED+"Disable!");
        saveConfig();
        floodgateApi = null;
    }



}


