package me.dewrs.Managers;

import me.dewrs.CustomSurveys;
import me.dewrs.SQL.SQLManager;
import me.dewrs.Utils.CustomSound;
import me.dewrs.Utils.SetColor;
import me.dewrs.Utils.SetSounds;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ListingSurveys {
    private CustomSurveys plugin;

    public ListingSurveys(CustomSurveys plugin) {
        this.plugin = plugin;
    }
    public void viewSurvey(Player player, String[] args){
        SetSounds sound = new SetSounds(plugin);
        if(args.length > 1) {
            String survey = args[1];
            if (!plugin.getMainConfigManager().isDbEnabled()) {
                if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase())) {
                    if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase() + ".usable") &&
                            plugin.getSurveysManager().getConfig().getBoolean("Surveys." + survey.toLowerCase() + ".usable")) {
                        String message = plugin.getSurveysManager().getConfig().getString("Surveys." + survey.toLowerCase() + ".message");
                        sound.setSounds(player, CustomSound.COMMAND_SEND);
                        player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getListSurveyViewMessage().replace("%s%", survey.toLowerCase()).replace("%m%", message)));
                        for (String key : plugin.getSurveysManager().getConfig().getConfigurationSection("Surveys." + survey.toLowerCase()).getKeys(false)) {
                            if (key.startsWith("option_")) {
                                String currentOption = plugin.getSurveysManager().getConfig().getString("Surveys." + survey.toLowerCase() + "." + key);
                                player.sendMessage(SetColor.setColor("&8- &7" + currentOption));
                            }
                        }
                    } else {
                        sound.setSounds(player, CustomSound.ERROR);
                        player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getSurveyDisabled()));
                    }
                } else {
                    sound.setSounds(player, CustomSound.ERROR);
                    player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getViewSurveyNotFound()));
                }
            }else{
                try {
                    if (SQLManager.surveyExist(plugin.getMySQL(), survey.toLowerCase())) {
                        String message = SQLManager.getSQLMessage(plugin.getMySQL(), survey.toLowerCase());
                        sound.setSounds(player, CustomSound.COMMAND_SEND);
                        player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getListSurveyViewMessage().replace("%s%", survey.toLowerCase()).replace("%m%", message)));
                        ArrayList<String> options = SQLManager.getSQLStringOptions(plugin.getMySQL(), survey.toLowerCase());
                        for(String op : options){
                            player.sendMessage(SetColor.setColor("&8- &7" + op));
                        }
                    } else {
                        sound.setSounds(player, CustomSound.ERROR);
                        player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getViewSurveyNotFound()));
                    }
                }catch (NullPointerException ex){
                    if(plugin.getMainConfigManager().isDbEnabled()) {
                        player.sendMessage(SetColor.setColor(plugin.name + "&cError! Could not connect to MySQL"));
                    }else{
                        ex.printStackTrace();
                    }
                }
            }
        }else{
            sound.setSounds(player, CustomSound.ERROR);
            player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getViewSurveyNoName()));
        }
    }

    public void listSurveys(Player player) {
        SetSounds sound = new SetSounds(plugin);
        player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getListSurveyList()));
        sound.setSounds(player, CustomSound.COMMAND_SEND);
        if (!plugin.getMainConfigManager().isDbEnabled()) {
            if (plugin.getSurveysManager().getConfig().getConfigurationSection("Surveys") != null) {
                for (String key : plugin.getSurveysManager().getConfig().getConfigurationSection("Surveys").getKeys(false)) {
                    try {
                        if (plugin.getSurveysManager().getConfig().getBoolean("Surveys." + key + ".usable")) {
                            TextComponent survey, view, edit, remove, start;

                            survey = new TextComponent(SetColor.setColor("&8- &7" + key + " "));

                            view = new TextComponent(SetColor.setColor(plugin.getMessagesManager().getListSurveyView() + " "));
                            view.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/survey view " + key));
                            view.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(SetColor.setColor(plugin.getMessagesManager().getListSurveyView())).create()));

                            edit = new TextComponent(SetColor.setColor(plugin.getMessagesManager().getListSurveyEdit() + " "));
                            edit.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/survey edit " + key));
                            edit.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(SetColor.setColor(plugin.getMessagesManager().getListSurveyEdit())).create()));

                            remove = new TextComponent(SetColor.setColor(plugin.getMessagesManager().getListSurveysRemove() + " "));
                            remove.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/survey remove " + key));
                            remove.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(SetColor.setColor(plugin.getMessagesManager().getListSurveysRemove())).create()));

                            start = new TextComponent(SetColor.setColor(plugin.getMessagesManager().getListSurveyStart()));
                            start.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/survey start " + key));
                            start.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(SetColor.setColor(plugin.getMessagesManager().getListSurveyStart())).create()));

                            survey.addExtra(view);
                            survey.addExtra(edit);
                            survey.addExtra(remove);
                            survey.addExtra(start);

                            player.spigot().sendMessage(survey);
                        }
                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                player.sendMessage(SetColor.setColor("&7No Surveys"));
            }
        }else{
            try{
                ArrayList<String> surveys = SQLManager.getSQLSurveys(plugin.getMySQL(),plugin.getDatabase());
                if(!surveys.isEmpty()) {
                    for (String sur : surveys) {
                        TextComponent survey, view, edit, remove, start;

                        survey = new TextComponent(SetColor.setColor("&8- &7" + sur + " "));

                        view = new TextComponent(SetColor.setColor(plugin.getMessagesManager().getListSurveyView() + " "));
                        view.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/survey view " + sur));
                        view.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(SetColor.setColor(plugin.getMessagesManager().getListSurveyView())).create()));

                        edit = new TextComponent(SetColor.setColor(plugin.getMessagesManager().getListSurveyEdit() + " "));
                        edit.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/survey edit " + sur));
                        edit.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(SetColor.setColor(plugin.getMessagesManager().getListSurveyEdit())).create()));

                        remove = new TextComponent(SetColor.setColor(plugin.getMessagesManager().getListSurveysRemove() + " "));
                        remove.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/survey remove " + sur));
                        remove.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(SetColor.setColor(plugin.getMessagesManager().getListSurveysRemove())).create()));

                        start = new TextComponent(SetColor.setColor(plugin.getMessagesManager().getListSurveyStart()));
                        start.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/survey start " + sur));
                        start.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(SetColor.setColor(plugin.getMessagesManager().getListSurveyStart())).create()));

                        survey.addExtra(view);
                        survey.addExtra(edit);
                        survey.addExtra(remove);
                        survey.addExtra(start);

                        player.spigot().sendMessage(survey);
                    }
                }else{
                    player.sendMessage(SetColor.setColor("&7No Surveys"));
                }
            }catch (NullPointerException ex){
                if(plugin.getMainConfigManager().isDbEnabled()) {
                    player.sendMessage(SetColor.setColor(plugin.name + "&cError! Could not connect to MySQL"));
                }else{
                    ex.printStackTrace();
                }
            }
        }
    }
}
