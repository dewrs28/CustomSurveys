package me.dewrs.Config;

import me.dewrs.CustomSurveys;
import me.dewrs.Utils.SetColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

public class MainConfigManager {
    private CustomConfig configFile;
    private CustomSurveys plugin;
    private boolean titlesStartEnabled;
    private boolean titlesEndEnabled;
    private String titlesStartUp;
    private String titlesStartDown;
    private int titlesStartFadeIn;
    private int titlesStartStay;
    private int titlesStartFadeOut;
    private String titlesEndUp;
    private String titlesEndDown;
    private int titlesEndFadeIn;
    private int titlesEndStay;
    private int titlesEndFadeOut;
    private int defaultTimeSurvey;
    private Sound soundError;
    private Sound soundCommand;
    private Sound soundNoPermission;
    private Sound soundStartSurvey;
    private Sound soundEndSurvey;
    private Sound soundCorrect;
    private boolean soundsEnabled;
    private boolean dbEnabled;
    private String dbHost;
    private int dbPort;
    private String dbDatabase;
    private String dbUser;
    private String dbPassword;

    public MainConfigManager(CustomSurveys plugin){
        this.plugin = plugin;
        configFile = new CustomConfig("config.yml",null,plugin);
        configFile.registerConfig();
        loadConfig();
    }
    public void loadConfig(){
        FileConfiguration config = configFile.getConfig();
        titlesStartEnabled = config.getBoolean("titles.start_survey.show_title");
        titlesEndEnabled = config.getBoolean("titles.end_survey.show_title");
        titlesStartUp = config.getString("titles.start_survey.up");
        titlesStartDown = config.getString("titles.start_survey.down");
        titlesStartFadeIn = config.getInt("titles.start_survey.fadeIn");
        titlesStartStay = config.getInt("titles.start_survey.stay");
        titlesStartFadeOut = config.getInt("titles.start_survey.fadeOut");
        titlesEndUp = config.getString("titles.end_survey.up");
        titlesEndDown = config.getString("titles.end_survey.down");
        titlesEndFadeIn = config.getInt("titles.end_survey.fadeIn");
        titlesEndStay = config.getInt("titles.end_survey.stay");
        titlesEndFadeOut = config.getInt("titles.end_survey.fadeOut");
        defaultTimeSurvey = config.getInt("options.default_time_survey");
        soundsEnabled = config.getBoolean("options.enabled_sounds");

        dbEnabled = config.getBoolean("database.enabled");
        if(isDbEnabled()){
            regDatabase();
        }

        regSounds();
    }

    public void regSounds(){
        FileConfiguration config = configFile.getConfig();
        try {
            soundError = Sound.valueOf(config.getString("sounds.error"));
            soundNoPermission = Sound.valueOf(config.getString("sounds.no_permission"));
            soundCommand = Sound.valueOf(config.getString("sounds.command_send"));
            soundStartSurvey = Sound.valueOf(config.getString("sounds.start_survey"));
            soundEndSurvey = Sound.valueOf(config.getString("sounds.end_survey"));
            soundCorrect = Sound.valueOf(config.getString("sounds.correct"));
        }catch (IllegalArgumentException ex){
            Bukkit.getConsoleSender().sendMessage(SetColor.setColor(plugin.name+"&cError! Invalid sounds in the 'config.yml'"));
        }
    }

    public void regDatabase(){
        FileConfiguration config = configFile.getConfig();
        dbHost = config.getString("database.host");
        dbPort = config.getInt("database.port");
        dbDatabase = config.getString("database.database");
        dbUser = config.getString("database.user");
        dbPassword = config.getString("database.password");
    }
    public void reloadConfig(){
        configFile.reloadConfig();
        loadConfig();
    }
    public void saveConfig(){
        configFile.saveConfig();
        loadConfig();
    }

    public void setVersionPaths(){
        FileConfiguration config = configFile.getConfig();
        if(!config.contains("database.enabled")){
            config.set("database.enabled", false);
        }
        if(!config.contains("database.host")){
            config.set("database.host", "hostname");
        }
        if(!config.contains("database.port")){
            config.set("database.port", "port");
        }
        if(!config.contains("database.database")){
            config.set("database.database", "database");
        }
        if(!config.contains("database.user")){
            config.set("database.user", "user");
        }
        if(!config.contains("database.password")){
            config.set("database.password", "pass");
        }
        saveConfig();
    }

    public boolean isTitlesStartEnabled() {
        return titlesStartEnabled;
    }
    public boolean isTitlesEndEnabled() {
        return titlesEndEnabled;
    }
    public String getTitlesStartUp() {
        return titlesStartUp;
    }
    public String getTitlesStartDown() {
        return titlesStartDown;
    }
    public int getTitlesStartFadeIn() {
        return titlesStartFadeIn;
    }
    public int getTitlesStartStay() {
        return titlesStartStay;
    }
    public int getTitlesStartFadeOut() {
        return titlesStartFadeOut;
    }
    public String getTitlesEndUp() {
        return titlesEndUp;
    }
    public String getTitlesEndDown() {
        return titlesEndDown;
    }
    public int getTitlesEndFadeIn() {
        return titlesEndFadeIn;
    }
    public int getTitlesEndStay() {
        return titlesEndStay;
    }
    public int getTitlesEndFadeOut() {
        return titlesEndFadeOut;
    }
    public int getDefaultTimeSurvey() {
        return defaultTimeSurvey;
    }
    public Sound getSoundError() {
        return soundError;
    }
    public Sound getSoundCommand() {
        return soundCommand;
    }
    public Sound getSoundNoPermission() {
        return soundNoPermission;
    }
    public Sound getSoundStartSurvey() {
        return soundStartSurvey;
    }
    public Sound getSoundEndSurvey() {
        return soundEndSurvey;
    }
    public Sound getSoundCorrect() {
        return soundCorrect;
    }
    public boolean isSoundsEnabled() {
        return soundsEnabled;
    }
    public boolean isDbEnabled() {
        return dbEnabled;
    }
    public String getDbHost() {
        return dbHost;
    }
    public int getDbPort() {
        return dbPort;
    }
    public String getDbDatabase() {
        return dbDatabase;
    }
    public String getDbUser() {
        return dbUser;
    }
    public String getDbPassword() {
        return dbPassword;
    }
}
