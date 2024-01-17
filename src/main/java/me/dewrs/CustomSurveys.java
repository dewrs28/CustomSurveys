package me.dewrs;

import me.dewrs.Config.MainConfigManager;
import me.dewrs.Config.MessagesManager;
import me.dewrs.Config.SurveysManager;
import me.dewrs.Managers.CreateSurvey;
import me.dewrs.Managers.EditSurvey;
import me.dewrs.SQL.SQLConnection;
import me.dewrs.Utils.SetColor;
import me.dewrs.Utils.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomSurveys extends JavaPlugin {
    private SQLConnection connection;
    private int taskID;
    private String surveyProgress = "";
    public HashMap<String,Integer> votes;
    public HashMap<String,Integer> optionEdit;
    public HashMap<String,Integer> surveyEditing;
    public ArrayList<String> isEditing;
    public void setEditing(Player player){
        if(!isEditing.contains(player.getName())){
            isEditing.add(player.getName());
        }
    }
    public void removeEditing(Player player){
        if(isEditing.contains(player.getName())){
            isEditing.remove(player.getName());
        }
    }
    public boolean isInEditing(Player player){
        if(isEditing.contains(player.getName())){
            return true;
        }else{
            return false;
        }
    }
    public ArrayList<String> preventQuit;
    public void setPreventQuit(Player player){
        if(!preventQuit.contains(player.getName())){
            preventQuit.add(player.getName());
        }
    }
    public void removePreventQuit(Player player){
        if(preventQuit.contains(player.getName())){
            preventQuit.remove(player.getName());
        }
    }
    public boolean isInPreventQuit(Player player){
        if(preventQuit.contains(player.getName())){
            return true;
        }else{
            return false;
        }
    }
    public HashMap<String,String> surveyCreator;
    public HashMap<String,Integer> iterator;
    public HashMap<String,Integer> selectOptions;
    public HashMap<String,Integer> stage;
    private ArrayList<String> inAdminSurvey;
    public void setAdminSurvey(Player player){
        if(!inAdminSurvey.contains(player.getName())){
            inAdminSurvey.add(player.getName());
        }
    }
    public void removeAdminSurvey(Player player){
        if(inAdminSurvey.contains(player.getName())){
            inAdminSurvey.remove(player.getName());
        }
    }
    public boolean isInAdminSurvey(Player player){
        if(inAdminSurvey.contains(player.getName())){
            return true;
        }else{
            return false;
        }
    }
    public String name = SetColor.setColor("&8[&eCustomSurveys&8] ");
    PluginDescriptionFile pdf = this.getDescription();
    public String version = SetColor.setColor("&c"+pdf.getVersion());
    private MainConfigManager mainConfigManager;
    private MessagesManager messagesManager;
    private SurveysManager surveysManager;
    private String host;
    private int port;
    private String database;
    private String user;
    private String password;
    private static final int SPIGOT_RESOURCE_ID = 114527;
    public void onEnable(){
        regCommands();
        regEvents();
        inAdminSurvey = new ArrayList<>();
        stage = new HashMap<>();
        selectOptions = new HashMap<>();
        iterator = new HashMap<>();
        surveyCreator = new HashMap<>();
        surveyEditing = new HashMap<>();
        votes = new HashMap<>();
        optionEdit = new HashMap<>();
        preventQuit = new ArrayList<>();
        isEditing = new ArrayList<>();
        mainConfigManager = new MainConfigManager(this);
        messagesManager = new MessagesManager(this);
        surveysManager = new SurveysManager(this);
        host = getMainConfigManager().getDbHost();
        port = getMainConfigManager().getDbPort();
        database = getMainConfigManager().getDbDatabase();
        user = getMainConfigManager().getDbUser();
        password = getMainConfigManager().getDbPassword();
        getMainConfigManager().setVersionPaths();
        getMessagesManager().setVersionPaths();
        if(getMainConfigManager().isDbEnabled()) {
            this.connection = new SQLConnection(host, port, database, user, password);
        }
        if(getSurveysManager().getConfig().getConfigurationSection("Surveys") != null) {
            verifySurveySyntax();
        }else if(getSurveysManager().getConfig().getKeys(true).size() != 0){
            Bukkit.getConsoleSender().sendMessage(SetColor.setColor(name + "&c&lError! Check the file 'surveys.yml'"));
        }
        Bukkit.getConsoleSender().sendMessage(SetColor.setColor(name+"&ahas been enabled, version: "+version));
        new UpdateChecker(this, SPIGOT_RESOURCE_ID).getLatestVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                Bukkit.getConsoleSender().sendMessage(SetColor.setColor(name+"Is up to date!"));
            } else {
                Bukkit.getConsoleSender().sendMessage(SetColor.setColor(name+"*********************************************************************"));
                Bukkit.getConsoleSender().sendMessage(SetColor.setColor(name+"&c CustomSurveys is outdated!"));
                Bukkit.getConsoleSender().sendMessage(SetColor.setColor(name+"&cNewest version: &e1.1"));
                Bukkit.getConsoleSender().sendMessage(SetColor.setColor(name+"&cYour version: &e" + version));
                Bukkit.getConsoleSender().sendMessage(SetColor.setColor(name+"&cPlease Update Here: &ehttps://spigotmc.org/114527"));
                Bukkit.getConsoleSender().sendMessage(SetColor.setColor(name+"*********************************************************************"));
            }
        });
    }
    public void onDisable(){
        Bukkit.getConsoleSender().sendMessage(SetColor.setColor(name+"&chas been disabled"));
    }
    public void regCommands(){
        this.getCommand("survey").setExecutor(new CommandMain(this));
    }
    public void regEvents(){
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new CreateSurvey(this), this);
        pm.registerEvents(new EditSurvey(this), this);
    }
    public MainConfigManager getMainConfigManager() {
        return mainConfigManager;
    }

    public MessagesManager getMessagesManager() {
        return messagesManager;
    }

    public SurveysManager getSurveysManager() {
        return surveysManager;
    }
    public void verifySurveySyntax() {
        if (getSurveysManager().getConfig().getConfigurationSection("Surveys") != null) {
            for (String path : getSurveysManager().getConfig().getConfigurationSection("Surveys").getKeys(false)) {
                char ch;
                for (int i = 0; i < path.length(); i++) {
                    ch = path.charAt(i);
                    if (Character.isUpperCase(ch)) {
                        LinkedHashMap<String, Object> temp = new LinkedHashMap<>(getSurveysManager().getConfig().getConfigurationSection("Surveys." + path).getValues(true));
                        getSurveysManager().getConfig().set("Surveys." + path, null);
                        getSurveysManager().saveConfig();
                        path = path.toLowerCase();
                        for (Map.Entry<String, Object> entry : temp.entrySet()) {
                            getSurveysManager().getConfig().set("Surveys." + path + "." + entry.getKey(), entry.getValue());
                        }
                        getSurveysManager().saveConfig();
                        break;
                    }
                }
                if (getSurveysManager().getConfig().getString("Surveys." + path + ".message") == null) {
                    if (getSurveysManager().getConfig().contains("Surveys." + path + ".usable")) {
                        getSurveysManager().getConfig().set("Surveys." + path + ".usable", false);
                        getSurveysManager().saveConfig();
                    }
                    Bukkit.getConsoleSender().sendMessage(SetColor.setColor(name + "&c&lError! Check the file 'surveys.yml'"));
                }
                LinkedHashMap<String, Object> temp = new LinkedHashMap<>(getSurveysManager().getConfig().getConfigurationSection("Surveys." + path).getValues(true));
                temp.entrySet().removeIf(e -> !e.getKey().startsWith("option_"));
                if (temp.size() < 2) {
                    if (getSurveysManager().getConfig().contains("Surveys." + path + ".usable")) {
                        getSurveysManager().getConfig().set("Surveys." + path + ".usable", false);
                        getSurveysManager().saveConfig();
                    }
                    Bukkit.getConsoleSender().sendMessage(SetColor.setColor(name + "&c&lError! Check the file 'surveys.yml'"));
                } else {
                    orderOptions(path);
                }
            }
        }
    }

    public void orderOptions(String path){
        String tempMessage = null;
        boolean tempBoolean=false,aux=false;
        if(getSurveysManager().getConfig().getString("Surveys." + path + ".message") != null) {
            tempMessage = getSurveysManager().getConfig().getString("Surveys." + path + ".message");
        }
        if(getSurveysManager().getConfig().contains("Surveys." + path + ".usable")) {
           tempBoolean = getSurveysManager().getConfig().getBoolean("Surveys." + path + ".usable");
           aux = true;
        }
        LinkedHashMap<String, Object> temp = new LinkedHashMap<>(getSurveysManager().getConfig().getConfigurationSection("Surveys."+path).getValues(true));
        getSurveysManager().getConfig().set("Surveys."+path, null);
        temp.entrySet().removeIf(e -> !e.getKey().startsWith("option_"));
        if(aux) {
            getSurveysManager().getConfig().set("Surveys." + path + ".usable", tempBoolean);
        }
        if(tempMessage != null) {
            getSurveysManager().getConfig().set("Surveys." + path + ".message", tempMessage);
        }
        int i = 1;
        for (Map.Entry<String, Object> entry : temp.entrySet()) {
            getSurveysManager().getConfig().set("Surveys." + path + ".option_"+i, entry.getValue());
            i++;
        }
        getSurveysManager().saveConfig();
    }
    public String getSurveyProgress() {
        return surveyProgress;
    }

    public void setSurveyProgress(String surveyProgress) {
        this.surveyProgress = surveyProgress;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public Connection getMySQL(){
        return this.connection.getConnection();
    }

    public String getDatabase() {
        return database;
    }

    public ArrayList<String> getStringOptions(String survey){
        ArrayList<String> options = new ArrayList<>();
        for(String key : getSurveysManager().getConfig().getConfigurationSection("Surveys."+survey.toLowerCase()).getKeys(false)){
            if(key.startsWith("option_")) {
                String option = getSurveysManager().getConfig().getString("Surveys."+survey.toLowerCase()+"."+key);
                options.add(option);
            }
        }
        return options;
    }
}