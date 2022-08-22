package me.anhkhoaaa;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.geysermc.cumulus.*;

import java.util.Objects;

public final class SOSNapthePE extends JavaPlugin {


    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info(ChatColor.GREEN+"Enable!");

    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED+"Disable!");
        saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("sosnapthe")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("op")) {
                    reloadConfig();
                    sender.sendMessage("reload thành công");
                    return false;
                }
            }
            if (args.length == 0) {
                Player player = sender.getServer().getPlayer(sender.getName());
                if (FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())) {
                    FloodgatePlayer pePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
                    pePlayer.sendForm(CustomForm.builder()
                            .title(getStuffConfig("title"))
                            .dropdown("Chọn Loại Thẻ:", "Viettel","Mobifone","Vinaphone","Vietnamobile","Zing","Gate","Garena")
                            .dropdown("Chọn Giá Thẻ:\n§f§l(§4§l*§f§l) §r§aChọn sai mất thẻ đấy nha!", "10.000VND", "20.000VND","50.000VND","100.000VND","200.000VND","500.000VND","1.000.000VND")
                            .input("Nhập seri:","seri tại đây")
                            .input("Nhập mã thẻ:","mã thẻ tại đây")
                            .toggle(getStuffConfig("xacnhan"))
                            .responseHandler((form, responseData) -> {
                                if (!(form.parseResponse(responseData).isCorrect())) {
                                    player.sendMessage("Bạn đã hủy nạp thẻ!");
                                } else {
                                    if (form.parseResponse(responseData).getToggle(4)) {
                                        String cardName = getCardName(form.parseResponse(responseData).getDropdown(0));
                                        String cardPrice = getCardPrice(form.parseResponse(responseData).getDropdown(1));
                                        String seri = form.parseResponse(responseData).getInput(2);
                                        String maThe = form.parseResponse(responseData).getInput(3);
                                        if (Objects.equals(seri, "") || Objects.equals(maThe, "")) {
                                            player.sendMessage("Thiếu thông tin seri hoặc mã thẻ!");
                                        } else {
                                            Bukkit.dispatchCommand(player, "donate choosecard "+ cardName);
                                            Bukkit.dispatchCommand(player, "donate choosecardprice "+ cardPrice);
                                            player.chat(seri);
                                            player.chat(maThe);
                                        }
                                    } else {
                                        player.sendMessage("Bạn đã hủy nạp thẻ!");
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
        return getConfig().getString(key);
    }
}
