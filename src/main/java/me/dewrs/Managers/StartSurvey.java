package me.dewrs.Managers;

import me.dewrs.CustomSurveys;
import me.dewrs.Utils.CustomSound;
import me.dewrs.Utils.SetColor;
import me.dewrs.Utils.SetSounds;
import me.dewrs.Utils.SetTitles;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StartSurvey {
    private CustomSurveys plugin;

    public StartSurvey(CustomSurveys plugin) {
        this.plugin = plugin;
    }

    public void detectCommand(Player player, String[] args) {
        SetSounds sound = new SetSounds(plugin);
        if (args.length > 1) {
            String survey = args[1];
            if (args.length < 3) {
                //Default time
                int time = plugin.getMainConfigManager().getDefaultTimeSurvey();
                startSurvey(player,survey,time);
            } else {
                //Custom time
                try {
                    int time = Integer.parseInt(args[2]);
                    startSurvey(player,survey,time);
                } catch (NumberFormatException ex) {
                    sound.setSounds(player, CustomSound.ERROR);
                    player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getStartSurveyTimeNumber()));
                }
            }
        } else {
            sound.setSounds(player, CustomSound.ERROR);
            player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getStartSurveyNoName()));
        }
    }

    public void startSurvey(Player player, String survey, Integer time) {
        SetSounds sound = new SetSounds(plugin);
        if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase())) {
            if (plugin.getSurveysManager().getConfig().contains("Surveys." + survey.toLowerCase() + ".usable") &&
                    plugin.getSurveysManager().getConfig().getBoolean("Surveys." + survey.toLowerCase() + ".usable")) {
                if(plugin.getSurveyProgress().equals("")) {
                    String message = plugin.getSurveysManager().getConfig().getString("Surveys." + survey.toLowerCase() + ".message");
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.sendMessage("");
                        sound.setSounds(p, CustomSound.START_SURVEY);
                        p.sendMessage(SetColor.setColor(plugin.getMessagesManager().getStartSurveyStartMessage()));
                        p.sendMessage(SetColor.setColor(plugin.getMessagesManager().getStartSurveyStartVote().replace("%s%", message)));
                        for (String key : plugin.getSurveysManager().getConfig().getConfigurationSection("Surveys." + survey.toLowerCase()).getKeys(false)) {
                            if (key.startsWith("option_")) {
                                String[] split = key.split("_");
                                String numberOption = split[1];
                                TextComponent option, vote;
                                String currentOption = plugin.getSurveysManager().getConfig().getString("Surveys." + survey.toLowerCase() + "." + key);
                                option = new TextComponent(SetColor.setColor(plugin.getMessagesManager().getFormatOptionsStart().replace("%s%", currentOption)+" "));

                                vote = new TextComponent(SetColor.setColor(plugin.getMessagesManager().getStartSurveyVote()));
                                vote.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/survey vote " + survey.toLowerCase() + " " + numberOption));
                                vote.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(SetColor.setColor(plugin.getMessagesManager().getStartSurveyVoteHover().replace("%s%", currentOption))).create()));

                                option.addExtra(vote);
                                p.spigot().sendMessage(option);
                            }
                        }
                        p.sendMessage("");
                    }
                    if(plugin.getMainConfigManager().isTitlesStartEnabled()) {
                        String title = plugin.getMainConfigManager().getTitlesStartUp().replace("%s%", message);
                        String subtitle = plugin.getMainConfigManager().getTitlesStartDown();
                        int fadeIn = plugin.getMainConfigManager().getTitlesStartFadeIn();
                        int stay = plugin.getMainConfigManager().getTitlesStartStay();
                        int fadeOut = plugin.getMainConfigManager().getTitlesStartFadeOut();
                        for(Player p : Bukkit.getOnlinePlayers()) {
                            SetTitles.setTitle(p, title, subtitle, fadeIn, stay, fadeOut);
                        }
                    }
                    plugin.setSurveyProgress(survey.toLowerCase());
                    SurveyScheduler sh = new SurveyScheduler(plugin,survey);
                    sh.runTaskLater(plugin, time * 20);
                    plugin.setTaskID(sh.getTaskId());
                    player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getStartSurveyPlayerMessage()));
                }else{
                    sound.setSounds(player, CustomSound.ERROR);
                    player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getStartSurveyInProgress()));
                }
            }else{
                sound.setSounds(player, CustomSound.ERROR);
                player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getSurveyDisabled()));
            }
        }else{
            sound.setSounds(player, CustomSound.ERROR);
            player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getStartSurveyNotFound()));
        }
    }
}

