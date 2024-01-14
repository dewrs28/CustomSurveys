package me.dewrs;

import me.dewrs.Managers.*;
import me.dewrs.Utils.CustomSound;
import me.dewrs.Utils.SetColor;
import me.dewrs.Utils.SetSounds;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandMain implements CommandExecutor {
    private CustomSurveys plugin;
    public CommandMain(CustomSurveys plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(SetColor.setColor(plugin.name+"only players"));
            return false;
        }else{
            Player player = (Player) sender;
            SetSounds sound = new SetSounds(plugin);
            if(args.length > 0){
                if(args[0].equalsIgnoreCase("reload")){
                    player.sendMessage("");
                    if(player.hasPermission("survey.admin.reload") || player.isOp()) {
                        sound.setSounds(player, CustomSound.CORRECT);
                        player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getReloadCommand()));
                        plugin.getMainConfigManager().reloadConfig();
                        plugin.getMessagesManager().reloadConfig();
                        plugin.getSurveysManager().reloadConfig();
                        if(!plugin.getSurveyProgress().equals("")) {
                            plugin.verifySurveySyntax();
                            plugin.votes.clear();
                            plugin.setSurveyProgress("");
                        }
                        Bukkit.getServer().getScheduler().cancelTask(plugin.getTaskID());
                    }else{
                        sound.setSounds(player, CustomSound.NO_PERMISSION);
                        player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getNoPermission()));
                    }
                    return true;
                }else if(args[0].equalsIgnoreCase("create")){
                    player.sendMessage("");
                    if(player.hasPermission("survey.admin.create") || player.isOp()) {
                        if (args.length > 1) {
                            String nameSurvey = args[1];
                            CreateSurvey survey = new CreateSurvey(plugin);
                            if (!survey.surveyExist(nameSurvey)) {
                                survey.createSurvey(player, nameSurvey);
                                plugin.setAdminSurvey(player);
                                sound.setSounds(player, CustomSound.COMMAND_SEND);
                            } else {
                                if (!plugin.getSurveysManager().getConfig().contains("Surveys." + nameSurvey.toLowerCase() + ".usable") ||
                                        !plugin.getSurveysManager().getConfig().getBoolean("Surveys." + nameSurvey.toLowerCase() + ".usable")) {
                                    player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getSurveyDisabled()));
                                    sound.setSounds(player, CustomSound.ERROR);
                                } else {
                                    player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getCreateSurveyExist()));
                                    sound.setSounds(player, CustomSound.ERROR);
                                }
                            }
                        } else {
                            sound.setSounds(player, CustomSound.ERROR);
                            player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getCreateSurveyNoName()));
                        }
                    }else{
                        sound.setSounds(player, CustomSound.NO_PERMISSION);
                        player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getNoPermission()));
                    }
                    return true;
                }else if(args[0].equalsIgnoreCase("remove")){
                    player.sendMessage("");
                    if(player.hasPermission("survey.admin.remove") || player.isOp()) {
                        if (args.length > 1) {
                            String nameSurvey = args[1];
                            RemoveSurvey survey = new RemoveSurvey(plugin);
                            survey.removeSurvey(nameSurvey, player);
                        } else {
                            sound.setSounds(player, CustomSound.ERROR);
                            player.sendMessage(SetColor.setColor(plugin.name + plugin.getMessagesManager().getRemoveSurveyNoName()));
                        }
                    }else{
                        sound.setSounds(player, CustomSound.NO_PERMISSION);
                        player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getNoPermission()));
                    }
                    return true;
                }else if(args[0].equalsIgnoreCase("edit")){
                    player.sendMessage("");
                    if(player.hasPermission("survey.admin.edit") || player.isOp()) {
                        EditSurvey survey = new EditSurvey(plugin);
                        survey.detectCommand(player, args);
                    }else{
                        sound.setSounds(player, CustomSound.NO_PERMISSION);
                        player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getNoPermission()));
                    }
                    return true;
                }else if(args[0].equalsIgnoreCase("list")){
                    player.sendMessage("");
                    if(player.hasPermission("survey.admin.list") || player.isOp()) {
                        ListingSurveys survey = new ListingSurveys(plugin);
                        survey.listSurveys(player);
                    }else{
                        sound.setSounds(player, CustomSound.NO_PERMISSION);
                        player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getNoPermission()));
                    }
                    return true;
                }else if(args[0].equalsIgnoreCase("view")){
                    player.sendMessage("");
                    if(player.hasPermission("survey.admin.view") || player.isOp()) {
                        ListingSurveys survey = new ListingSurveys(plugin);
                        survey.viewSurvey(player, args);
                    }else{
                        sound.setSounds(player, CustomSound.NO_PERMISSION);
                        player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getNoPermission()));
                    }
                    return true;
                }else if(args[0].equalsIgnoreCase("start")){
                    player.sendMessage("");
                    if(player.hasPermission("survey.admin.start") || player.isOp()) {
                        StartSurvey survey = new StartSurvey(plugin);
                        survey.detectCommand(player, args);
                    }else{
                        sound.setSounds(player, CustomSound.NO_PERMISSION);
                        player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getNoPermission()));
                    }
                    return true;
                }else if(args[0].equalsIgnoreCase("vote")){
                    player.sendMessage("");
                    VotesCounter survey = new VotesCounter(plugin);
                    survey.detectCommand(player,args);
                    return true;
                }else if(args[0].equalsIgnoreCase("end")){
                    player.sendMessage("");
                    if(player.hasPermission("survey.admin.end") || player.isOp()) {
                        EndSurvey survey = new EndSurvey(plugin);
                        survey.detectCommand(player, args);
                    }else{
                        sound.setSounds(player, CustomSound.NO_PERMISSION);
                        player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getNoPermission()));
                    }
                    return true;
                }else if(args[0].equalsIgnoreCase("help")){
                    if(args.length < 2 || args[1].equalsIgnoreCase("1")) {
                        List<String> helpCommand = plugin.getMessagesManager().getHelpCommand();
                        sound.setSounds(player, CustomSound.COMMAND_SEND);
                        for (String help : helpCommand) {
                            player.sendMessage(SetColor.setColor(help));
                        }
                    }else if(args[1].equalsIgnoreCase("2")){
                        List<String> helpCommand = plugin.getMessagesManager().getHelpJSONMessages();
                        sound.setSounds(player, CustomSound.COMMAND_SEND);
                        for (String help : helpCommand) {
                            player.sendMessage(SetColor.setColor(help));
                        }
                    }else{
                        player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getIncorrectCommand()));
                    }
                    return true;
                }else{
                    player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getIncorrectCommand()));
                    return true;
                }
            }else{
                player.sendMessage(SetColor.setColor(plugin.name+plugin.getMessagesManager().getIncorrectCommand()));
                return true;
            }
        }
    }
}
