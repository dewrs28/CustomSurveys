package me.dewrs.Utils;

import com.connorlinfoot.titleapi.TitleAPI;
import me.dewrs.CustomSurveys;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SetTitles {
    private static String name = SetColor.setColor("&8[&eCustomSurveys&8] ");

    public static void setTitle(Player player, String title, String subtitle, int in, int stay, int out){
        try{
            player.sendTitle(SetColor.setColor(title),SetColor.setColor(subtitle),in,stay,out);
        }catch (NoSuchMethodError ex) {
            try {
                TitleAPI.sendTitle(player,in,stay,out,SetColor.setColor(title),SetColor.setColor(subtitle));
            } catch (NoClassDefFoundError ex2) {
                Bukkit.getConsoleSender().sendMessage(SetColor.setColor(name + "&cError! Dependency &eTitleAPI &cnot found"));
            }
        }
    }
}
