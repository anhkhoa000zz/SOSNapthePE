package me.anhkhoaaa.sosnapthe.command;

import lombok.Getter;
import lombok.NonNull;
import me.anhkhoaaa.sosnapthe.SOSNapthePE;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import javax.annotation.Nullable;
import java.util.ArrayList;

@SuppressWarnings("SpellCheckingInspection")
public class NapTheCommand implements CommandExecutor {
    @Getter
    private final SOSNapthePE plugin;

    public NapTheCommand(SOSNapthePE plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @Nullable String[] args) {
        if (args == null || args.length == 0) {
            if (!(sender instanceof Player)){
                sender.sendMessage(chatConfigColor("&cOnly BE/PE player can use this command!"));
                return false;
            }
            Player player = (Player) sender;
            if (plugin.getFloodgateApi().isFloodgatePlayer(player.getUniqueId())) {
                FloodgatePlayer pePlayer = plugin.getFloodgateApi().getPlayer(player.getUniqueId());
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
                                    if (seri == null || seri.isEmpty() || maThe == null || maThe.isEmpty()) {
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
        if (args != null && args.length > 0) {
            if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("op")) {
                plugin.reloadConfig();
                sender.sendMessage(getStuffConfig("reload"));
                return false;

            }
        }
        return false;
    }


    public String getStuffConfig(String key){
        StringBuilder allMessage = new StringBuilder();
        for (String message : plugin.getConfig().getStringList(key)){
            allMessage.append(message).append("\n");
        }
        return chatConfigColor(allMessage.toString());
    }

    public String chatConfigColor(String fullString){
        return fullString.replace("&", "§").replace(".", "").replace("VND", "");
    }


    public String[] getThe(String key) {
        ArrayList<String> loaiTheOrMaThe = new ArrayList<>(plugin.getConfig().getStringList(key));
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
