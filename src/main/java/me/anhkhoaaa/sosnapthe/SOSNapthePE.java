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
        reloadConfig();
        saveDefaultConfig();
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
        if (command.getName().equalsIgnoreCase("sosnapthe")) {
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
                            .dropdown(getStuffConfig("chonLoaiThe"), "Viettel","Mobifone","Vinaphone","Vietnamobile","Zing","Gate","Garena")
                            .dropdown(getStuffConfig("chonGiaThe"), "10.000VND", "20.000VND","50.000VND","100.000VND","200.000VND","500.000VND","1.000.000VND")
                            .input(getStuffConfig("input1"),getStuffConfig("placeholderInput1"))
                            .input(getStuffConfig("input2"),getStuffConfig("placeholderInput2"))
                            .toggle(chatConfigColor(getStuffConfig("xacnhan")))
                            .responseHandler((form, responseData) -> {
                                CustomFormResponse myForm = form.parseResponse(responseData);
                                if (!(myForm.isCorrect())) {
                                    player.sendMessage(getStuffConfig("huyNap"));
                                } else {
                                    if (myForm.getToggle(4)) {
                                        String cardName = getCardName(myForm.getDropdown(0));
                                        String cardPrice = getCardPrice(myForm.getDropdown(1));
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

    public static String getCardName(int index) {
        if (index == 0){
            return "Viettel";
        } else if (index == 1) {
            return "Mobifone";
        } else if (index == 2){
            return "Vinaphone";
        } else if (index == 3){
            return "Vietnamobile";
        } else if (index == 4){
            return "Zing";
        } else if (index == 5){
            return "Gate";
        } else {
            return "Garena";
        }
    }

    public static String getCardPrice(int index) {
        if (index == 0){
            return "10000";
        } else if (index == 1) {
            return "20000";
        } else if (index == 2) {
            return "50000";
        } else if (index == 3) {
            return "100000";
        } else if (index == 4) {
            return "200000";
        } else if (index == 5) {
            return "500000";
        } else {
            return "1000000";
        }
    }
    public String getStuffConfig(String key){
        StringBuilder allMessage = new StringBuilder();
        for (String message : getConfig().getStringList(key)){
            allMessage.append(message).append("\n");
        }
        return chatConfigColor(allMessage.toString());
    }

    public String chatConfigColor(String fullString){
        return fullString.replace("&", "§");
    }


}


