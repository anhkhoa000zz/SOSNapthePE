package me.anhkhoaaa.sosnapthe;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.geysermc.cumulus.*;
import org.geysermc.cumulus.response.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

public final class SOSNapthePE extends JavaPlugin {


    private static FloodgateApi floodgateApi;

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
        floodgateApi = FloodgateApi.getInstance();
        getLogger().log(Level.INFO, ChatColor.GREEN+"Enable!");

    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, ChatColor.RED+"Disable!");
        saveConfig();
        floodgateApi = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("napthepe")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("op")) {
                    reloadConfig();
                    sender.sendMessage(getStuffConfig("reload"));
                    return false;

                }
            }
            if (args.length == 0) {
                Player player = sender.getServer().getPlayer(sender.getName());
                if (floodgateApi.isFloodgatePlayer(player.getUniqueId())) {
                    FloodgatePlayer pePlayer = floodgateApi.getPlayer(player.getUniqueId());
                    pePlayer.sendForm(CustomForm.builder()
                            .title(getStuffConfig("title"))
                            .dropdown(getStuffConfig("chonLoaiThe"), getThe("loaiThe"))
                            .dropdown(getStuffConfig("chonGiaThe"), getThe("giaThe"))
                            .input(getStuffConfig("input1"),getStuffConfig("placeholderInput1"))
                            .input(getStuffConfig("input2"),getStuffConfig("placeholderInput2"))
                            .toggle(chatConfigColor(getStuffConfig("xacnhan")))
                            .responseHandler((form, responseData) -> {
                                CustomFormResponse myForm = form.parseResponse(responseData);
                                if (!(myForm.isCorrect())) {
                                    player.sendMessage(getStuffConfig("huyNap"));
                                } else {
                                    if (myForm.getToggle(4)) {
                                        String cardName = removeStuffInConfig("loaiThe")[myForm.getDropdown(0)];
                                        String cardPrice = removeStuffInConfig("giaThe")[myForm.getDropdown(1)];
                                        String seri = myForm.getInput(2);
                                        String maThe = myForm.getInput(3);
                                        if (seri.isEmpty() || maThe.isEmpty()) {
                                            player.sendMessage(getStuffConfig("thieuThongTin"));
                                        } else {
                                            // chua du trinh dung api nen lam tam nhu nay :)
                                            Bukkit.dispatchCommand(player, "donate choosecard "+ cardName);
                                            Bukkit.dispatchCommand(player, "donate choosecardprice "+ cardPrice);
                                            player.chat(seri);
                                            player.chat(maThe);
                                        }
                                    } else {
                                        player.sendMessage(getStuffConfig("huyNap"));
                                    }
                                }
                            })
                            .build());
                }
            }
        }
        return false;
    }

    public String getStuffConfig(String key){
        StringBuilder allMessage = new StringBuilder();
        for (String message : getConfig().getStringList(key)){
            allMessage.append(message).append("\n");
        }
        return chatConfigColor(allMessage.toString());
    }

    public String chatConfigColor(String fullString){
        return fullString.replace("&", "§").replace(".", "").replace("VND", "");
    }


    public String[] getThe(String key) {
        ArrayList<String> loaiTheOrMaThe = new ArrayList<String>();
        for (String loaiTheOrMaTheNe : getConfig().getStringList(key)) {
            loaiTheOrMaThe.add(loaiTheOrMaTheNe);
        }
        String[] array = new String[loaiTheOrMaThe.size()];
        for (int i = 0; i < loaiTheOrMaThe.size(); i++) {
            array[i] = loaiTheOrMaThe.get(i);
        }
        return array;
    }

    public String[] removeStuffInConfig(String key){
        String[] array = getThe(key);
        String[] newArray = new String[array.length];
        int i = 0;
        for
        (String item : array) {
            newArray[i] = chatConfigColor(item);
            i++;
        }
        return newArray;
    }

}


