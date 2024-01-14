package me.dewrs.Config;

import me.dewrs.CustomSurveys;
import org.bukkit.configuration.file.FileConfiguration;

public class SurveysManager {
    private CustomConfig configFile;
    private CustomSurveys plugin;
    public SurveysManager(CustomSurveys plugin){
        this.plugin = plugin;
        configFile = new CustomConfig("surveys.yml",null,plugin);
        configFile.registerConfig();
        loadConfig();
    }
    public void loadConfig(){

    }
    public void reloadConfig(){
        configFile.reloadConfig();
        loadConfig();
    }
    public void saveConfig(){
        configFile.saveConfig();
        loadConfig();
    }

    public FileConfiguration getConfig(){
        return configFile.getConfig();
    }
}
