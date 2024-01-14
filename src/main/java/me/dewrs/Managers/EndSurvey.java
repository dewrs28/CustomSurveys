package me.dewrs.Managers;

import me.dewrs.CustomSurveys;
import me.dewrs.Utils.CustomSound;
import me.dewrs.Utils.SetColor;
import me.dewrs.Utils.SetSounds;
import me.dewrs.Utils.SetTitles;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EndSurvey {
    private CustomSurveys plugin;

    public EndSurvey(CustomSurveys plugin) {
        this.plugin = plugin;
    }

    public void detectCommand(Player player, String[] args){
        SetSounds sound = new SetSounds(plugin);
        if(args.length > 1){
            String survey = args[1];
            endSurvey(player, survey);
        }else{
            sound.setSounds(player, CustomSound.ERROR);
            player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getEndSurveyNoName()));
        }
    }

    public void endSurvey(Player player, String survey) {
        SetSounds sound = new SetSounds(plugin);
        if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase())) {
            if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase() + ".usable") &&
                    plugin.getSurveysManager().getConfig().getBoolean("Surveys." + survey.toLowerCase() + ".usable")) {
                if (plugin.getSurveyProgress().equals(survey.toLowerCase())) {
                    Bukkit.getServer().getScheduler().cancelTask(plugin.getTaskID());
                    plugin.setSurveyProgress("");
                    player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getEndSurveyCorrectEnd()));
                    VotesCounter result = new VotesCounter(plugin);
                    result.finalResult(survey);
                } else {
                    sound.setSounds(player, CustomSound.ERROR);
                    player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getEndSurveyNoRun()));
                }
            }else{
                sound.setSounds(player, CustomSound.ERROR);
                player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getSurveyDisabled()));
            }
        }else{
            sound.setSounds(player, CustomSound.ERROR);
            player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getEndSurveyNotFound()));
        }
    }
    public void endSurvey(String survey){
        if(plugin.getSurveyProgress().equals(survey.toLowerCase())){
            Bukkit.getServer().getScheduler().cancelTask(plugin.getTaskID());
            plugin.setSurveyProgress("");
            VotesCounter result = new VotesCounter(plugin);
            result.finalResult(survey);
        }
    }

    public void lastMessage(Map<Integer, Integer> sortedMap, String survey, List<Integer> listWinners, ArrayList<Integer> optionsNoVote){
        SetSounds sound = new SetSounds(plugin);
        String message = plugin.getSurveysManager().getConfig().getString("Surveys."+survey.toLowerCase()+".message");
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage("");
            sound.setSounds(p, CustomSound.END_SURVEY);
            p.sendMessage(SetColor.setColor(plugin.getMessagesManager().getStartSurveyEndMessage().replace("%s%", message)));
            for (Map.Entry<Integer, Integer> entry : sortedMap.entrySet()) {
                String currentOption = plugin.getSurveysManager().getConfig().getString("Surveys."+survey.toLowerCase()+".option_"+entry.getKey());
                int votes = entry.getValue();
                p.sendMessage(SetColor.setColor(plugin.getMessagesManager().getFormatOptionsEnd().replace("%s%", currentOption).replace("%i%", String.valueOf(votes))));
                if(!optionsNoVote.isEmpty()){
                    for(Integer i : optionsNoVote) {
                        String noVoteMsg = plugin.getSurveysManager().getConfig().getString("Surveys." + survey.toLowerCase() + ".option_"+i);
                        p.sendMessage(SetColor.setColor(plugin.getMessagesManager().getFormatOptionsEnd().replace("%s%", noVoteMsg).replace("%i%", String.valueOf(0))));
                    }
                }
            }
            String winner = plugin.getSurveysManager().getConfig().getString("Surveys."+survey.toLowerCase()+".option_"+listWinners.get(0));
            p.sendMessage("");
            p.sendMessage(SetColor.setColor(plugin.getMessagesManager().getEndSurveyWinner().replace("%s%", winner)));
            p.sendMessage("");
        }
        if(plugin.getMainConfigManager().isTitlesEndEnabled()) {
            String title = plugin.getMainConfigManager().getTitlesEndUp().replace("%s%", message);
            String winner = plugin.getSurveysManager().getConfig().getString("Surveys."+survey.toLowerCase()+".option_"+listWinners.get(0));
            String subtitle = plugin.getMainConfigManager().getTitlesEndDown().replace("%w%", winner);
            int fadeIn = plugin.getMainConfigManager().getTitlesEndFadeIn();
            int stay = plugin.getMainConfigManager().getTitlesEndStay();
            int fadeOut = plugin.getMainConfigManager().getTitlesEndFadeOut();
            for(Player p : Bukkit.getOnlinePlayers()){
                SetTitles.setTitle(p,title,subtitle,fadeIn,stay,fadeOut);
            }
        }
        plugin.votes.clear();
    }

    public void lastMessage(String survey){
        SetSounds sound = new SetSounds(plugin);
        String message = plugin.getSurveysManager().getConfig().getString("Surveys."+survey.toLowerCase()+".message");
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage("");
            sound.setSounds(p, CustomSound.END_SURVEY);
            p.sendMessage(SetColor.setColor(plugin.getMessagesManager().getStartSurveyEndMessage().replace("%s%", message)));
            for (String key : plugin.getSurveysManager().getConfig().getConfigurationSection("Surveys." + survey.toLowerCase()).getKeys(false)) {
                if (key.startsWith("option_")) {
                    String[] split = key.split("_");
                    int option = Integer.parseInt(split[1]);
                    String currentOption = plugin.getSurveysManager().getConfig().getString("Surveys." + survey.toLowerCase() + ".option_"+option);
                    p.sendMessage(SetColor.setColor(plugin.getMessagesManager().getFormatOptionsEnd().replace("%s%", currentOption).replace("%i%", String.valueOf(0))));
                }
            }
            p.sendMessage("");
        }
        if(plugin.getMainConfigManager().isTitlesEndEnabled()) {
            String title = plugin.getMainConfigManager().getTitlesEndUp().replace("%s%", message);
            String subtitle = plugin.getMainConfigManager().getTitlesEndDown().replace("%w%", plugin.getMessagesManager().getEndSurveyNoVotes());
            int fadeIn = plugin.getMainConfigManager().getTitlesEndFadeIn();
            int stay = plugin.getMainConfigManager().getTitlesEndStay();
            int fadeOut = plugin.getMainConfigManager().getTitlesEndFadeOut();
            for(Player p : Bukkit.getOnlinePlayers()){
                SetTitles.setTitle(p,title,subtitle,fadeIn,stay,fadeOut);
            }
        }
        plugin.votes.clear();
    }
}
