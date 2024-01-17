package me.dewrs.Managers;

import me.dewrs.CustomSurveys;
import me.dewrs.SQL.SQLManager;
import me.dewrs.Utils.CustomSound;
import me.dewrs.Utils.SetColor;
import me.dewrs.Utils.SetSounds;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CreateSurvey implements Listener {

    private CustomSurveys plugin;

    public CreateSurvey(CustomSurveys plugin) {
        this.plugin = plugin;
    }

    public boolean surveyExist(String survey){
        if(!plugin.getMainConfigManager().isDbEnabled()) {
            if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase())) {
                return true;
            } else {
                return false;
            }
        }else{
            return SQLManager.surveyExist(plugin.getMySQL(), survey.toLowerCase());
        }
    }

    public void createSurvey(Player player, String survey){
        plugin.surveyCreator.put(player.getName(), survey.toLowerCase());
        player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getCreateSurveyNameSet().replace("%s%", survey.toLowerCase())));
        player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getCreateSurveyMessage()));
        plugin.stage.put(player.getName(), 1);
    }

    @EventHandler
    public void settingSurvey(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        SetSounds sound = new SetSounds(plugin);
        if (plugin.isInAdminSurvey(player)) {
            String survey = plugin.surveyCreator.get(player.getName());
            String input = event.getMessage();
            if (!input.startsWith("/")) {
                if (plugin.stage.get(player.getName()) == 1) {
                    sound.setSounds(player, CustomSound.COMMAND_SEND);
                    plugin.getSurveysManager().getConfig().set("Surveys."+survey+".usable", false);
                    plugin.getSurveysManager().getConfig().set("Surveys."+survey + ".message", input);
                    plugin.getSurveysManager().saveConfig();
                    player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getCreateSurveyMessageSet().replace("%s%", input)));
                    plugin.stage.replace(player.getName(), 2);
                    player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getCreateSurveyOptionsNumber()));
                } else if (plugin.stage.get(player.getName()) == 2) {
                    try {
                        int optionsNumber = Integer.parseInt(input);
                        if (optionsNumber > 1) {
                            sound.setSounds(player, CustomSound.COMMAND_SEND);
                            plugin.selectOptions.put(player.getName(), optionsNumber);
                            player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getCreateSurveyOptionsNumberSet().replace("%i%", input)));
                            plugin.stage.replace(player.getName(), 3);
                            plugin.iterator.put(player.getName(), 1);
                            player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getCreateSurveyOptions().replace("%s%", "1")));
                        } else {
                            sound.setSounds(player, CustomSound.ERROR);
                            player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getCreateSurveyMinimumOption()));
                        }
                    } catch (NumberFormatException e) {
                        sound.setSounds(player, CustomSound.ERROR);
                        player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getCreateSurveyNoNumber()));
                    }
                } else if (plugin.stage.get(player.getName()) == 3) {
                    if (!plugin.iterator.get(player.getName()).equals(plugin.selectOptions.get(player.getName()))) {
                        sound.setSounds(player, CustomSound.COMMAND_SEND);
                        int i = plugin.iterator.get(player.getName());
                        plugin.getSurveysManager().getConfig().set("Surveys."+survey + ".option_" + i, input);
                        plugin.getSurveysManager().saveConfig();
                        player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getCreateSurveyOptionsSet().replace("%s%", input).replace("%i%", String.valueOf(i))));
                        player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getCreateSurveyOptions().replace("%s%", String.valueOf(i + 1))));
                        plugin.iterator.replace(player.getName(), i + 1);
                    } else {
                        int i = plugin.selectOptions.get(player.getName());
                        sound.setSounds(player, CustomSound.CORRECT);
                        plugin.getSurveysManager().getConfig().set("Surveys."+survey + ".option_" + i, input);
                        plugin.getSurveysManager().getConfig().set("Surveys."+survey+".usable", true);
                        plugin.getSurveysManager().saveConfig();

                        String message = plugin.getSurveysManager().getConfig().getString("Surveys."+survey.toLowerCase()+".message");
                        ArrayList<String> options = plugin.getStringOptions(survey);
                        if(plugin.getMainConfigManager().isDbEnabled()){
                            SQLManager.createSQLSurvey(plugin.getMySQL(), survey.toLowerCase(), getNumberOptions(survey));
                            SQLManager.setSQLSurvey(plugin.getMySQL(), survey.toLowerCase(), message, options);
                            plugin.getSurveysManager().getConfig().set("Surveys."+survey.toLowerCase(), null);
                            plugin.getSurveysManager().saveConfig();
                        }

                        player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getCreateSurveyOptionsSet().replace("%s%", input).replace("%i%", String.valueOf(i))));
                        player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getCreateSurveyFinished().replace("%s%", plugin.surveyCreator.get(player.getName()))));
                        plugin.removeAdminSurvey(player);
                        plugin.stage.remove(player.getName());
                        plugin.selectOptions.remove(player.getName());
                        plugin.iterator.remove(player.getName());
                        plugin.surveyCreator.remove(player.getName());
                        if(plugin.isInPreventQuit(player)){
                            plugin.removePreventQuit(player);
                        }
                        //adminSurvey
                        //stage
                        //selectOptions
                        //iterator
                        //surveyCreator
                    }
                }
            }
            event.setCancelled(true);
        }else{
            if(plugin.isInPreventQuit(player)){
                String input = event.getMessage();
                if(input.equalsIgnoreCase("Y")){
                    player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getCreateSurveyRejoinConfirm().replace("%s%", plugin.surveyCreator.get(player.getName()))));
                    plugin.setAdminSurvey(player);
                    if(plugin.stage.get(player.getName()) == 1){
                        sound.setSounds(player, CustomSound.COMMAND_SEND);
                        player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getCreateSurveyMessage()));
                    }else if(plugin.stage.get(player.getName()) == 2){
                        sound.setSounds(player, CustomSound.COMMAND_SEND);
                        player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getCreateSurveyOptionsNumber()));
                    }else if(plugin.stage.get(player.getName()) == 3){
                        sound.setSounds(player, CustomSound.COMMAND_SEND);
                        player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getCreateSurveyOptions().replace("%s%", String.valueOf(plugin.iterator.get(player.getName())))));
                    }
                }else if(input.equalsIgnoreCase("N")){
                    sound.setSounds(player, CustomSound.CORRECT);
                    String survey = plugin.surveyCreator.get(player.getName());
                    plugin.getSurveysManager().getConfig().set("Surveys."+survey, null);
                    plugin.getSurveysManager().saveConfig();
                    plugin.removeAdminSurvey(player);
                    plugin.stage.remove(player.getName());
                    plugin.selectOptions.remove(player.getName());
                    plugin.iterator.remove(player.getName());
                    player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getCreateSurveyRejoinCancel().replace("%s%", plugin.surveyCreator.get(player.getName()))));
                    plugin.surveyCreator.remove(player.getName());
                    plugin.removePreventQuit(player);
                }else{
                    sound.setSounds(player, CustomSound.ERROR);
                    player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getCreateSurveyRejoinMessage()));
                }
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void noCommands(PlayerCommandPreprocessEvent event){
        Player player = event.getPlayer();
        SetSounds sound = new SetSounds(plugin);
        if(plugin.isInAdminSurvey(player) || plugin.isInPreventQuit(player)){
            event.setCancelled(true);
            sound.setSounds(player, CustomSound.ERROR);
            player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getCreateSurveyNoCommands()));
        }
    }

    @EventHandler
    public void joinInAdminSurvey(PlayerJoinEvent event){
        Player player = event.getPlayer();
        SetSounds sound = new SetSounds(plugin);
        if(plugin.isInPreventQuit(player)){
            sound.setSounds(player, CustomSound.ERROR);
            player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getCreateSurveyRejoinMessage()));
        }
    }

    @EventHandler
    public void quitOnCreate(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(plugin.isInAdminSurvey(player)){
            plugin.setPreventQuit(player);
            plugin.removeAdminSurvey(player);
        }
    }
    public int getNumberOptions(String survey) {
        LinkedHashMap<String, Object> temp = new LinkedHashMap<>(plugin.getSurveysManager().getConfig().getConfigurationSection("Surveys." + survey.toLowerCase()).getValues(true));
        temp.entrySet().removeIf(e -> !e.getKey().startsWith("option_"));
        return temp.size();
    }
}
