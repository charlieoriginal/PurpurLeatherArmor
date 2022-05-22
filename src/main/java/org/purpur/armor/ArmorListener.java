package org.purpur.armor;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

public class ArmorListener implements CommandExecutor, TabCompleter {

    private String[] valids = new String[]{"helmet", "chestplate", "leggings", "boots"};

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("purpurleatherarmor.use")) {
            if (args.length == 0) {
                sender.sendMessage(ColorUtils.t("&8[&d&lPurpur&8] &7/pparmor [helmet/chestplate/leggings/boots] [#color]"));
                return true;
            }

            if (args.length == 1) {
                sender.sendMessage(ColorUtils.t("&8[&d&lPurpur&8] &7/pparmor [helmet/chestplate/leggings/boots] [#color]"));
                sender.sendMessage(ColorUtils.t("&8[&d&lPurpur&8] &cNo color has been specified on this command."));
            }

            if (args.length == 2) {
                String armorPiece = args[0];
                String color = args[1];

                if (!isValidArmorPiece(armorPiece)) {
                    sender.sendMessage(ColorUtils.t("&8[&d&lPurpur&8] &cInvalid armor piece. Valid armor pieces are: " + String.join(", ", valids)));
                    return false;
                }

                if (!isValidHexColor(color)) {
                    sender.sendMessage(ColorUtils.t("&8[&d&lPurpur&8] &cInvalid color. Valid colors are: #000000, #0000FF, #00FF00, #FF0000, #FFFFFF"));
                    return false;
                }

                String hex = color.replace("#", "");
                Material material = armorPieceToMaterial(armorPiece);
                int hexInt = Integer.parseInt(hex, 16);

                ItemStack item = new ItemStack(material);
                LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
                assert meta != null;
                meta.setColor(Color.fromRGB(hexInt));
                item.setItemMeta(meta);

                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    PlayerInventory inventory = player.getInventory();
                    inventory.addItem(item);
                    player.sendMessage(ColorUtils.t("&8[&d&lPurpur&8] &7You have been given a " + armorPiece + " with the color " + color));
                } else {
                    sender.sendMessage(ColorUtils.t("&8[&d&lPurpur&8] &cYou must be a player to use this command."));
                }
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            list.add("helmet");
            list.add("chestplate");
            list.add("leggings");
            list.add("boots");
            return list;
        }

        if (args.length == 2) {
            List<String> tc = new ArrayList<>();
            tc.add("#FFFFFF");
            tc.add("#8899FF");
            tc.add("Any six digit hex color. (0-9, a-f)");
            return tc;
        }

        return null;
    }

    private boolean isValidArmorPiece(String s) {
        for (String valid : valids) {
            if (valid.equalsIgnoreCase(s)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidHexColor(String hex) {
        //Check if string is a valid hex color with a # or without a #
        if (hex.matches("#?[0-9a-fA-F]{6}")) {
            return true;
        }

        return false;
    }

    private Material armorPieceToMaterial(String a) {
        switch (a) {
            case "chestplate":
                return Material.LEATHER_CHESTPLATE;
            case "leggings":
                return Material.LEATHER_LEGGINGS;
            case "boots":
                return Material.LEATHER_BOOTS;
            default:
                return Material.LEATHER_HELMET;
        }
    }
}
