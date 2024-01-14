package me.dewrs.Utils;

import org.bukkit.ChatColor;

public class SetColor {
    public static String setColor(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
