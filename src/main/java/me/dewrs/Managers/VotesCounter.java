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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toMap;

public class VotesCounter {
    private CustomSurveys plugin;

    public VotesCounter(CustomSurveys plugin) {
        this.plugin = plugin;
    }

    public void iterateOptions(Player player, String survey){
        SetSounds sound = new SetSounds(plugin);
        if(!plugin.getMainConfigManager().isDbEnabled()) {
            if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase())) {
                if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase() + ".usable") &&
                        plugin.getSurveysManager().getConfig().getBoolean("Surveys." + survey.toLowerCase() + ".usable")) {
                    if (plugin.getSurveyProgress().equals(survey.toLowerCase())) {
                        String message = plugin.getSurveysManager().getConfig().getString("Surveys." + survey.toLowerCase() + ".message");
                        sound.setSounds(player, CustomSound.START_SURVEY);
                        player.sendMessage(SetColor.setColor(plugin.getMessagesManager().getStartSurveyStartVote().replace("%s%", message)));
                        for (String key : plugin.getSurveysManager().getConfig().getConfigurationSection("Surveys." + survey.toLowerCase()).getKeys(false)) {
                            if (key.startsWith("option_")) {
                                String[] split = key.split("_");
                                String numberOption = split[1];
                                TextComponent option, vote;
                                String currentOption = plugin.getSurveysManager().getConfig().getString("Surveys." + survey.toLowerCase() + "." + key);
                                option = new TextComponent(SetColor.setColor(plugin.getMessagesManager().getFormatOptionsStart().replace("%s%", currentOption) + " "));

                                vote = new TextComponent(SetColor.setColor(plugin.getMessagesManager().getStartSurveyVote()));
                                vote.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/survey vote " + survey.toLowerCase() + " " + numberOption));
                                vote.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(SetColor.setColor(plugin.getMessagesManager().getStartSurveyVoteHover().replace("%s%", currentOption))).create()));

                                option.addExtra(vote);
                                player.spigot().sendMessage(option);
                            }
                        }
                    } else {
                        sound.setSounds(player, CustomSound.ERROR);
                        player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getVoteSurveyNoRun()));
                    }
                } else {
                    sound.setSounds(player, CustomSound.ERROR);
                    player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getStartSurveyNotFound()));
                }
            } else {
                sound.setSounds(player, CustomSound.ERROR);
                player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getStartSurveyNotFound()));
            }
        }else{
            try {
                if (SQLManager.surveyExist(plugin.getMySQL(), survey.toLowerCase())) {
                    if (plugin.getSurveyProgress().equals(survey.toLowerCase())) {
                        ArrayList<String> columns = SQLManager.getSQLAllColumns(plugin.getMySQL(), survey.toLowerCase());
                        String message = SQLManager.getSQLMessage(plugin.getMySQL(), survey.toLowerCase());
                        sound.setSounds(player, CustomSound.START_SURVEY);
                        player.sendMessage(SetColor.setColor(plugin.getMessagesManager().getStartSurveyStartVote().replace("%s%", message)));
                        int i = 1;
                        for(String op : columns) {
                            if(op.startsWith("option_")) {
                                int numberOption = i+1;
                                String currentOption = SQLManager.getSQLOption(plugin.getMySQL(), survey.toLowerCase(), op);
                                TextComponent option, vote;
                                option = new TextComponent(SetColor.setColor(plugin.getMessagesManager().getFormatOptionsStart().replace("%s%", currentOption) + " "));

                                vote = new TextComponent(SetColor.setColor(plugin.getMessagesManager().getStartSurveyVote()));
                                vote.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/survey vote " + survey.toLowerCase() + " " + numberOption));
                                vote.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(SetColor.setColor(plugin.getMessagesManager().getStartSurveyVoteHover().replace("%s%", currentOption))).create()));

                                option.addExtra(vote);
                                player.spigot().sendMessage(option);
                                i++;
                            }
                        }
                    }else{
                        sound.setSounds(player, CustomSound.ERROR);
                        player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getVoteSurveyNoRun()));
                    }
                } else {
                    sound.setSounds(player, CustomSound.ERROR);
                    player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getStartSurveyNotFound()));
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
    public void detectCommand(Player player, String[] args){
        SetSounds sound = new SetSounds(plugin);
        if(args.length > 1){
            String survey = args[1];
            if(args.length < 3){
                //Method iterate options
                iterateOptions(player,survey);
            }else{
                try {
                    int option = Integer.parseInt(args[2]);
                    countVote(player,survey,option);
                }catch (NumberFormatException ex){
                    sound.setSounds(player, CustomSound.ERROR);
                    player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getStartSurveyNoNumberOption()));
                }
            }
        }else{
            sound.setSounds(player, CustomSound.ERROR);
            player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getVoteSurveyNoName()));
        }
    }

    public void countVote(Player player, String survey, Integer option){
        SetSounds sound = new SetSounds(plugin);
        if(!plugin.getMainConfigManager().isDbEnabled()) {
            if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase())) {
                if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase() + ".usable") &&
                        plugin.getSurveysManager().getConfig().getBoolean("Surveys." + survey.toLowerCase() + ".usable")) {
                    if (plugin.getSurveyProgress().equals(survey.toLowerCase())) {
                        if (!plugin.votes.containsKey(player.getName())) {
                            plugin.votes.put(player.getName(), option);
                            String optionVoted = plugin.getSurveysManager().getConfig().getString("Surveys." + survey.toLowerCase() + ".option_" + option);
                            sound.setSounds(player, CustomSound.CORRECT);
                            player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getStartSurveyCorrectVote().replace("%s%", optionVoted)));
                        } else {
                            sound.setSounds(player, CustomSound.ERROR);
                            player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getStartSurveyNoVoteAgain()));
                        }
                    } else {
                        sound.setSounds(player, CustomSound.ERROR);
                        player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getVoteSurveyNoRun()));
                    }
                } else {
                    sound.setSounds(player, CustomSound.ERROR);
                    player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getVoteSurveyNoRun()));
                }
            } else {
                sound.setSounds(player, CustomSound.ERROR);
                player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getVoteSurveyNotFound()));
            }
        }else{
            try {
                if (SQLManager.surveyExist(plugin.getMySQL(), survey.toLowerCase())) {
                    if (plugin.getSurveyProgress().equals(survey.toLowerCase())) {
                        if (!plugin.votes.containsKey(player.getName())) {
                            plugin.votes.put(player.getName(), option);
                            String optionVoted = SQLManager.getSQLOption(plugin.getMySQL(), survey.toLowerCase(), "option_"+option);
                            sound.setSounds(player, CustomSound.CORRECT);
                            player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getStartSurveyCorrectVote().replace("%s%", optionVoted)));
                        } else {
                            sound.setSounds(player, CustomSound.ERROR);
                            player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getStartSurveyNoVoteAgain()));
                        }
                    } else {
                        sound.setSounds(player, CustomSound.ERROR);
                        player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getVoteSurveyNoRun()));
                    }
                } else {
                    sound.setSounds(player, CustomSound.ERROR);
                    player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getVoteSurveyNotFound()));
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

    public void finalResult(String survey){
        if(!plugin.votes.isEmpty()) {
            TreeMap<Integer, Integer> result = new TreeMap<>();
            for (Map.Entry<String, Integer> entry : plugin.votes.entrySet()) {
                if (!result.containsKey(entry.getValue())) {
                    result.put(entry.getValue(), 1);
                } else {
                    int votes = result.get(entry.getValue());
                    votes++;
                    result.put(entry.getValue(), votes);
                }
            }
            Map<Integer, Integer> sortedMap = result.entrySet().stream()
                    .sorted(comparingInt(e -> -1 * e.getValue()))
                    .collect(toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (a, b) -> {
                                throw new AssertionError();
                            },
                            LinkedHashMap::new
                    ));
            ArrayList<Integer> optionsNoVote = new ArrayList<>();
            if(!plugin.getMainConfigManager().isDbEnabled()) {
                for (String key : plugin.getSurveysManager().getConfig().getConfigurationSection("Surveys." + survey.toLowerCase()).getKeys(false)) {
                    if (key.startsWith("option_")) {
                        String[] split = key.split("_");
                        int option = Integer.parseInt(split[1]);
                        if (!sortedMap.containsKey(option)) {
                            optionsNoVote.add(option);
                        }
                    }
                }
            }else{
                try {
                    if (SQLManager.surveyExist(plugin.getMySQL(), survey.toLowerCase())) {
                        ArrayList<String> columns = SQLManager.getSQLAllColumns(plugin.getMySQL(), survey.toLowerCase());
                        for (String key : columns) {
                            if (key.startsWith("option_")) {
                                String[] split = key.split("_");
                                int option = Integer.parseInt(split[1]);
                                if (!sortedMap.containsKey(option)) {
                                    optionsNoVote.add(option);
                                }
                            }
                        }
                    }
                }catch (NullPointerException ex){
                    if(plugin.getMainConfigManager().isDbEnabled()) {
                        Bukkit.getConsoleSender().sendMessage(SetColor.setColor(plugin.name + "&cError! Could not connect to MySQL"));
                    }else{
                        ex.printStackTrace();
                    }
                }
            }
            List<Integer> listWinners = new ArrayList<>(sortedMap.keySet());
            Collections.sort(listWinners);
            Collections.reverse(listWinners);
            EndSurvey end = new EndSurvey(plugin);
            end.lastMessage(sortedMap,survey,listWinners,optionsNoVote);
        }else{
            EndSurvey end = new EndSurvey(plugin);
            end.lastMessage(survey);
        }
    }
}
