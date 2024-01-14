package me.dewrs.Managers;

import me.dewrs.CustomSurveys;
import me.dewrs.Utils.CustomSound;
import me.dewrs.Utils.SetColor;
import me.dewrs.Utils.SetSounds;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class EditSurvey implements Listener {
    private CustomSurveys plugin;

    public EditSurvey(CustomSurveys plugin) {
        this.plugin = plugin;
    }

    public boolean verifyOptions(String survey) {
        LinkedHashMap<String, Object> temp = new LinkedHashMap<>(plugin.getSurveysManager().getConfig().getConfigurationSection("Surveys." + survey.toLowerCase()).getValues(true));
        temp.entrySet().removeIf(e -> !e.getKey().startsWith("option_"));
        return temp.size() != 2;
    }

    public void orderOptions(String survey) {
        String tempMessage = plugin.getSurveysManager().getConfig().getString("Surveys." + survey.toLowerCase() + ".message");
        LinkedHashMap<String, Object> temp = new LinkedHashMap<>(plugin.getSurveysManager().getConfig().getConfigurationSection("Surveys." + survey.toLowerCase()).getValues(true));
        plugin.getSurveysManager().getConfig().set("Surveys." + survey.toLowerCase(), null);
        temp.entrySet().removeIf(e -> !e.getKey().startsWith("option_"));
        plugin.getSurveysManager().getConfig().set("Surveys." + survey.toLowerCase() + ".usable", true);
        plugin.getSurveysManager().getConfig().set("Surveys." + survey.toLowerCase() + ".message", tempMessage);
        int i = 1;
        for (Map.Entry<String, Object> entry : temp.entrySet()) {
            plugin.getSurveysManager().getConfig().set("Surveys." + survey.toLowerCase() + ".option_" + i, entry.getValue());
            i++;
        }
        plugin.getSurveysManager().saveConfig();
    }

    public void detectCommand(Player player, String[] args) {
        SetSounds sound = new SetSounds(plugin);
        if (args.length > 1) {
            String survey = args[1];
            if (args.length < 3) {
                survey = args[1];
                editSurvey(survey, player);
            } else {
                if (args.length < 4 && args[2].equalsIgnoreCase("message")) {
                    replaceMessage(survey, player);
                } else if (args.length < 4 && args[2].equalsIgnoreCase("add")) {
                    settingOption(survey, player);
                } else if (args.length < 5 && args[2].equalsIgnoreCase("option")) {
                    if (args.length == 4) {
                        try {
                            int option = Integer.parseInt(args[3]);
                            replaceOption(survey, player, option);
                        } catch (NumberFormatException e) {
                            sound.setSounds(player, CustomSound.ERROR);
                            player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getEditSurveyNoNumber()));
                        }
                    } else {
                        selectOption(survey, player);
                    }
                } else if (args.length < 5 && args[2].equalsIgnoreCase("remove")) {
                    if (args.length == 4) {
                        try {
                            int option = Integer.parseInt(args[3]);
                            removeSurvey(survey, player, option);
                        } catch (NumberFormatException e) {
                            sound.setSounds(player, CustomSound.ERROR);
                            player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getEditSurveyNoNumber()));
                        }
                    } else {
                        selectOption(survey, player);
                    }
                } else {
                    sound.setSounds(player, CustomSound.ERROR);
                    player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getIncorrectCommand()));
                }
            }
        } else {
            sound.setSounds(player, CustomSound.ERROR);
            player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getEditSurveyNoName()));
        }
    }


    public void settingOption(String survey, Player player){
        SetSounds sound = new SetSounds(plugin);
        if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase())) {
            if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase() + ".usable") &&
                    plugin.getSurveysManager().getConfig().getBoolean("Surveys." + survey.toLowerCase() + ".usable")) {
                sound.setSounds(player, CustomSound.COMMAND_SEND);
                player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getEditSurveyAddOption()));
                plugin.setEditing(player);
                plugin.surveyCreator.put(player.getName(), survey.toLowerCase());
                plugin.surveyEditing.put(player.getName(), 3);
            }else{
                sound.setSounds(player, CustomSound.ERROR);
                player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getSurveyDisabled()));
            }
        } else {
            sound.setSounds(player, CustomSound.ERROR);
            player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getEditSurveyNotFound()));
        }
    }
    public void removeSurvey(String survey, Player player, Integer option){
        SetSounds sound = new SetSounds(plugin);
        if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase())) {
            if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase() + ".usable") &&
                    plugin.getSurveysManager().getConfig().getBoolean("Surveys." + survey.toLowerCase() + ".usable")) {
                if (verifyOptions(survey)) {
                    //String removedOption = plugin.getSurveysManager().getConfig().getString("Surveys."+survey.toLowerCase()+"option_"+option);
                    player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getEditSurveyRemoveOption().replace("%i%", String.valueOf(option))));
                    plugin.getSurveysManager().getConfig().set("Surveys." + survey.toLowerCase() + ".option_" + option, null);
                    plugin.getSurveysManager().saveConfig();
                    orderOptions(survey);
                    sound.setSounds(player, CustomSound.CORRECT);
                    player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getEditSurveyFinished().replace("%s%", survey.toLowerCase())));
                    selectOption(survey, player);
                } else {
                    sound.setSounds(player, CustomSound.ERROR);
                    player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getEditSurveyRemoveMinimum()));
                }
            }else{
                sound.setSounds(player, CustomSound.ERROR);
                player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getSurveyDisabled()));
            }
        }else{
            sound.setSounds(player, CustomSound.ERROR);
            player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getEditSurveyNotFound()));
        }
    }

    public void editSurvey(String survey, Player player) {
        SetSounds sound = new SetSounds(plugin);
        if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase())) {
            if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase() + ".usable") &&
                    plugin.getSurveysManager().getConfig().getBoolean("Surveys." + survey.toLowerCase() + ".usable")) {
                TextComponent message, option, add;
                player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getEditSurveyEnter().replace("%s%", survey.toLowerCase())));
                message = new TextComponent(SetColor.setColor(plugin.getMessagesManager().getEditSurveyEnterMessage()));
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/survey edit " + survey + " message"));
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(SetColor.setColor(plugin.getMessagesManager().getEditSurveyEnterMessage())).create()));
                player.spigot().sendMessage(message);
                option = new TextComponent(SetColor.setColor(plugin.getMessagesManager().getEditSurveyEnterOptions()));
                option.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/survey edit " + survey + " option"));
                option.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(SetColor.setColor(plugin.getMessagesManager().getEditSurveyEnterOptions())).create()));
                player.spigot().sendMessage(option);
                add = new TextComponent(SetColor.setColor(plugin.getMessagesManager().getEditSurveyEnterAdd()));
                add.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/survey edit " + survey + " add"));
                add.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(SetColor.setColor(plugin.getMessagesManager().getEditSurveyEnterAdd())).create()));
                player.spigot().sendMessage(add);
                sound.setSounds(player, CustomSound.COMMAND_SEND);
            }else{
                sound.setSounds(player, CustomSound.ERROR);
                player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getSurveyDisabled()));
            }
        } else {
            sound.setSounds(player, CustomSound.ERROR);
            player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getEditSurveyNotFound()));
        }
    }

    public void replaceMessage(String survey, Player player){
        SetSounds sound = new SetSounds(plugin);
        if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase())) {
            if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase() + ".usable") &&
                    plugin.getSurveysManager().getConfig().getBoolean("Surveys." + survey.toLowerCase() + ".usable")) {
                String currentMessage = plugin.getSurveysManager().getConfig().getString("Surveys." + survey.toLowerCase() + ".message");
                sound.setSounds(player, CustomSound.COMMAND_SEND);
                player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getEditSurveyReplaceMessage().replace("%s%", currentMessage)));
                plugin.setEditing(player);
                plugin.surveyCreator.put(player.getName(), survey.toLowerCase());
                plugin.surveyEditing.put(player.getName(), 1);
            }else{
                sound.setSounds(player, CustomSound.ERROR);
                player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getSurveyDisabled()));
            }
        }else{
            sound.setSounds(player, CustomSound.ERROR);
            player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getEditSurveyNotFound()));
        }
    }
    public void replaceOption(String survey, Player player, Integer option){
        SetSounds sound = new SetSounds(plugin);
        if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase())) {
            if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase() + ".usable") &&
                    plugin.getSurveysManager().getConfig().getBoolean("Surveys." + survey.toLowerCase() + ".usable")) {
                plugin.setEditing(player);
                plugin.surveyCreator.put(player.getName(), survey.toLowerCase());
                plugin.surveyEditing.put(player.getName(), 2);
                plugin.optionEdit.put(player.getName(), option);
                sound.setSounds(player, CustomSound.COMMAND_SEND);
                player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getEditSurveyReplaceOption().replace("%i%", String.valueOf(option))));
            }else{
                sound.setSounds(player, CustomSound.ERROR);
                player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getSurveyDisabled()));
            }
        }else{
            sound.setSounds(player, CustomSound.ERROR);
            player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getEditSurveyNotFound()));
        }
    }
    public void selectOption(String survey, Player player){
        SetSounds sound = new SetSounds(plugin);
        if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase())) {
            if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase() + ".usable") &&
                    plugin.getSurveysManager().getConfig().getBoolean("Surveys." + survey.toLowerCase() + ".usable")) {
                sound.setSounds(player, CustomSound.COMMAND_SEND);
                player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getEditSurveyEnterOption()));
                for (String key : plugin.getSurveysManager().getConfig().getConfigurationSection("Surveys." + survey.toLowerCase()).getKeys(false)) {
                    if (key.startsWith("option_")) {
                        TextComponent ex1, remove, ex2, edit, ex3;
                        String[] split = key.split("_");
                        String option = split[1];
                        String currentOption = plugin.getSurveysManager().getConfig().getString("Surveys." + survey.toLowerCase() + "." + key);

                        ex1 = new TextComponent(SetColor.setColor("&b#" + option + " &r&8["));

                        remove = new TextComponent(SetColor.setColor(plugin.getMessagesManager().getEditSurveyRemoveSymbol()));
                        remove.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/survey edit " + survey + " remove " + option));
                        remove.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(SetColor.setColor(plugin.getMessagesManager().getEditSurveyRemoveHover().replace("%i%", option))).create()));

                        ex2 = new TextComponent(SetColor.setColor("  "));

                        edit = new TextComponent(SetColor.setColor(plugin.getMessagesManager().getEditSurveyEditSymbol()));
                        edit.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/survey edit " + survey + " option " + option));
                        edit.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(SetColor.setColor(plugin.getMessagesManager().getEditSurveyHoverOption().replace("%i%", option))).create()));

                        ex3 = new TextComponent(SetColor.setColor("&8] &7" + currentOption));

                        ex1.addExtra(remove);
                        ex1.addExtra(ex2);
                        ex1.addExtra(edit);
                        ex1.addExtra(ex3);

                        player.spigot().sendMessage(ex1);
                    }
                }
                if (plugin.surveyEditing.get(player.getName()) != null && plugin.surveyEditing.get(player.getName()) == 3) {
                    TextComponent anotherOption = new TextComponent(SetColor.setColor(plugin.getMessagesManager().getEditSurveyAddAnotherOption()));
                    anotherOption.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/survey edit " + survey + " add"));
                    anotherOption.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(SetColor.setColor(plugin.getMessagesManager().getEditSurveyAddAnotherOption())).create()));
                    player.spigot().sendMessage(anotherOption);
                }
            }else{
                sound.setSounds(player, CustomSound.ERROR);
                player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getSurveyDisabled()));
            }
        }else{
            sound.setSounds(player, CustomSound.ERROR);
            player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getEditSurveyNotFound()));
        }
    }
    @EventHandler
    public void editChat(AsyncPlayerChatEvent event){
        SetSounds sound = new SetSounds(plugin);
        Player player = event.getPlayer();
        if(plugin.isInEditing(player)){
            String input = event.getMessage();
            String survey = plugin.surveyCreator.get(player.getName());
            if(plugin.surveyEditing.get(player.getName()) == 1){
                plugin.getSurveysManager().getConfig().set("Surveys."+survey+".message", input);
                plugin.getSurveysManager().saveConfig();
                sound.setSounds(player, CustomSound.CORRECT);
                player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getEditSurveyReplaceMessageSet().replace("%s%", input)));
                player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getEditSurveyFinished().replace("%s%", survey)));
                quitEditing(player);
            }else if(plugin.surveyEditing.get(player.getName()) == 2){
                String option = String.valueOf(plugin.optionEdit.get(player.getName()));
                plugin.getSurveysManager().getConfig().set("Surveys."+survey+".option_"+option, input);
                plugin.getSurveysManager().saveConfig();
                sound.setSounds(player, CustomSound.CORRECT);
                player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getEditSurveyReplaceOptionSet().replace("%i%", option).replace("%s%", input)));
                player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getEditSurveyFinished().replace("%s%", survey)));
                selectOption(survey,player);
                quitEditing(player);
            }else if(plugin.surveyEditing.get(player.getName()) == 3){
                LinkedHashMap<String, Object> temp = new LinkedHashMap<>(plugin.getSurveysManager().getConfig().getConfigurationSection("Surveys."+survey.toLowerCase()).getValues(true));
                temp.entrySet().removeIf(e -> !e.getKey().startsWith("option_"));
                Map.Entry<String, String> last = (Map.Entry<String, String>) temp.entrySet().toArray()[temp.size() - 1];
                String[] splitNum = last.getKey().split("_");
                int num = Integer.parseInt(splitNum[1]);
                plugin.getSurveysManager().getConfig().set("Surveys."+survey.toLowerCase()+".option_"+(num+1), input);
                plugin.getSurveysManager().saveConfig();
                sound.setSounds(player, CustomSound.CORRECT);
                player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getEditSurveyAddOptionSet().replace("%s%", input)));
                player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getEditSurveyFinished().replace("%s%", survey)));
                selectOption(survey,player);
                quitEditing(player);
            }
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void noCommands(PlayerCommandPreprocessEvent event){
        SetSounds sound = new SetSounds(plugin);
        Player player = event.getPlayer();
        if(plugin.isInEditing(player)){
            event.setCancelled(true);
            sound.setSounds(player, CustomSound.ERROR);
            player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getEditSurveyNoCommands()));
        }
    }
    public void quitEditing(Player player){
        plugin.removeEditing(player);
        plugin.surveyCreator.remove(player.getName());
        plugin.surveyEditing.remove(player.getName());
        plugin.optionEdit.remove(player.getName());
    }

    @EventHandler
    public void quitOnCreate(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(plugin.isInEditing(player)){
            quitEditing(player);
        }
    }
}
