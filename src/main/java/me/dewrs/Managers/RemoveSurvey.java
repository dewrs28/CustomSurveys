package me.dewrs.Managers;

import me.dewrs.CustomSurveys;
import me.dewrs.SQL.SQLManager;
import me.dewrs.Utils.CustomSound;
import me.dewrs.Utils.SetColor;
import me.dewrs.Utils.SetSounds;
import org.bukkit.entity.Player;

public class RemoveSurvey {
    private CustomSurveys plugin;

    public RemoveSurvey(CustomSurveys plugin) {
        this.plugin = plugin;
    }

    public void removeSurvey(String survey, Player player) {
        SetSounds sound = new SetSounds(plugin);
        if (!plugin.getMainConfigManager().isDbEnabled()) {
            if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase())) {
                if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase() + ".usable") &&
                        plugin.getSurveysManager().getConfig().getBoolean("Surveys." + survey.toLowerCase() + ".usable")) {
                    plugin.getSurveysManager().getConfig().set("Surveys." + survey.toLowerCase(), null);
                    plugin.getSurveysManager().saveConfig();
                    sound.setSounds(player, CustomSound.CORRECT);
                    player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getRemoveSurveyRemoved().replace("%s%", survey.toLowerCase())));
                } else {
                    sound.setSounds(player, CustomSound.ERROR);
                    player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getSurveyDisabled()));
                }
            } else {
                sound.setSounds(player, CustomSound.ERROR);
                player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getRemoveSurveyNotFound()));
            }
        }else{
            try {
                if (SQLManager.surveyExist(plugin.getMySQL(), survey.toLowerCase())) {
                    SQLManager.removeSQLSurvey(plugin.getMySQL(), survey.toLowerCase());
                    sound.setSounds(player, CustomSound.CORRECT);
                    player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getRemoveSurveyRemoved().replace("%s%", survey.toLowerCase())));
                } else {
                    sound.setSounds(player, CustomSound.ERROR);
                    player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getRemoveSurveyNotFound()));
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
