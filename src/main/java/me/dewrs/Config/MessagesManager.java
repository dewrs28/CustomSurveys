package me.dewrs.Config;

import me.dewrs.CustomSurveys;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class MessagesManager {
    private CustomConfig configFile;
    private CustomSurveys plugin;
    private String noPermission;
    private String surveyDisabled;
    private String incorrectCommand;
    private String reloadCommand;
    private String createSurveyMessage;
    private String createSurveyOptionsNumber;
    private String createSurveyOptions;
    private String createSurveyFinished;
    private String createSurveyNoName;
    private String createSurveyNoNumber;
    private String createSurveyMinimumOption;
    private String createSurveyNoCommands;
    private String createSurveyMessageSet;
    private String createSurveyOptionsNumberSet;
    private String createSurveyOptionsSet;
    private String createSurveyNameSet;
    private String createSurveyRejoinMessage;
    private String createSurveyRejoinConfirm;
    private String createSurveyRejoinCancel;
    private String createSurveyExist;
    private String removeSurveyRemoved;
    private String removeSurveyNotFound;
    private String removeSurveyNoName;
    private String editSurveyNoName;
    private String editSurveyNotFound;
    private String editSurveyEnter;
    private String editSurveyEnterMessage;
    private String editSurveyEnterOptions;
    private String editSurveyReplaceMessage;
    private String editSurveyReplaceOption;
    private String editSurveyFinished;
    private String editSurveyEnterOption;
    private String editSurveyHoverOption;
    private String editSurveyReplaceMessageSet;
    private String editSurveyReplaceOptionSet;
    private String editSurveyNoCommands;
    private String editSurveyNoNumber;
    private String editSurveyEnterAdd;
    private String editSurveyAddOption;
    private String editSurveyAddOptionSet;
    private String editSurveyRemoveOption;
    private String editSurveyRemoveHover;
    private String editSurveyRemoveSymbol;
    private String editSurveyEditSymbol;
    private String editSurveyRemoveMinimum;
    private String editSurveyAddAnotherOption;
    private String listSurveyView;
    private String listSurveyViewMessage;
    private String listSurveyEdit;
    private String listSurveyList;
    private String startSurveyInProgress;
    private String startSurveyNotFound;
    private String startSurveyTimeNumber;
    private String startSurveyStartMessage;
    private String startSurveyEndMessage;
    private String startSurveyCorrectVote;
    private String startSurveyNoVoteAgain;
    private String startSurveyNoName;
    private String startSurveyVote;
    private String startSurveyVoteHover;
    private String startSurveyStartVote;
    private String startSurveyNoNumberOption;
    private String endSurveyCorrectEnd;
    private String endSurveyNoName;
    private String endSurveyNoRun;
    private String endSurveyNotFound;
    private String voteSurveyNotFound;
    private String voteSurveyNoName;
    private String voteSurveyNoRun;
    private String startSurveyPlayerMessage;
    private String endSurveyNoVotes;
    private String listSurveyStart;
    private String endSurveyWinner;
    private String listSurveysRemove;
    private String formatOptionsStart;
    private String formatOptionsEnd;
    private List<String> helpCommand;
    private List<String> helpJSONMessages;
    public MessagesManager(CustomSurveys plugin){
        this.plugin = plugin;
        configFile = new CustomConfig("messages.yml",null,plugin);
        configFile.registerConfig();
        loadConfig();
    }
    public void loadConfig(){
        FileConfiguration config = configFile.getConfig();
        helpCommand = config.getStringList("help_command");
        helpJSONMessages = config.getStringList("help_shortcuts_json");
        noPermission = config.getString("no_permission");
        surveyDisabled = config.getString("survey_no_usable");
        incorrectCommand = config.getString("error.incorrect_command");
        reloadCommand = config.getString("reload");
        createSurveyMessage = config.getString("create_survey.set_message");
        createSurveyOptionsNumber = config.getString("create_survey.set_number_options");
        createSurveyOptions = config.getString("create_survey.set_options");
        createSurveyFinished = config.getString("create_survey.finished");
        createSurveyNoName = config.getString("create_survey.error_no_name_survey");
        createSurveyNoNumber = config.getString("create_survey.error_select_number");
        createSurveyMinimumOption = config.getString("create_survey.error_minimum_one_option");
        createSurveyNoCommands = config.getString("create_survey.error_no_commands");
        createSurveyMessageSet = config.getString("create_survey.correct_set_message");
        createSurveyOptionsNumberSet = config.getString("create_survey.correct_set_number_options");
        createSurveyOptionsSet = config.getString("create_survey.correct_set_options");
        createSurveyNameSet = config.getString("create_survey.correct_name");
        createSurveyRejoinMessage = config.getString("create_survey.rejoin_message");
        createSurveyRejoinConfirm = config.getString("create_survey.rejoin_confirm");
        createSurveyRejoinCancel = config.getString("create_survey.rejoin_cancel");
        createSurveyExist = config.getString("create_survey.error_survey_exist");
        removeSurveyRemoved = config.getString("remove_survey.removed");
        removeSurveyNotFound = config.getString("remove_survey.survey_not_found");
        removeSurveyNoName = config.getString("remove_survey.error_no_name_survey");
        editSurveyNoName = config.getString("edit_survey.error_no_name_survey");
        editSurveyNotFound = config.getString("edit_survey.survey_not_found");
        editSurveyEnter = config.getString("edit_survey.edit_enter");
        editSurveyEnterMessage = config.getString("edit_survey.edit_enter_message");
        editSurveyEnterOption = config.getString("edit_survey.edit_option.edit_enter_option");
        editSurveyEnterOptions = config.getString("edit_survey.edit_enter_options");
        editSurveyReplaceMessage = config.getString("edit_survey.message.edit_replace_message");
        editSurveyReplaceOption = config.getString("edit_survey.edit_option.edit_replace_option");
        editSurveyFinished = config.getString("edit_survey.edit_finished");
        editSurveyHoverOption = config.getString("edit_survey.edit_option.edit_hover_option");
        editSurveyReplaceMessageSet = config.getString("edit_survey.message.correct_set_message");
        editSurveyReplaceOptionSet = config.getString("edit_survey.edit_option.correct_set_option");
        editSurveyNoCommands = config.getString("edit_survey.error_no_commands");
        editSurveyNoNumber = config.getString("edit_survey.error_option_number");
        editSurveyEnterAdd = config.getString("edit_survey.edit_enter_add");
        editSurveyAddOption = config.getString("edit_survey.add_option.set_option");
        editSurveyAddOptionSet = config.getString("edit_survey.add_option.correct_set_option");
        editSurveyRemoveOption = config.getString("edit_survey.remove_option.correct_remove_option");
        editSurveyRemoveHover = config.getString("edit_survey.remove_option.remove_hover_option");
        editSurveyRemoveSymbol = config.getString("edit_survey.remove_option.symbol");
        editSurveyEditSymbol = config.getString("edit_survey.edit_option.symbol");
        editSurveyRemoveMinimum = config.getString("edit_survey.remove_option.error_minimum_options");
        editSurveyAddAnotherOption = config.getString("edit_survey.add_option.add_another_option");
        listSurveyView = config.getString("list_surveys.view");
        listSurveyViewMessage = config.getString("list_surveys.view_message");
        listSurveyEdit = config.getString("list_surveys.edit");
        listSurveyList = config.getString("list_surveys.list_message");
        startSurveyInProgress = config.getString("start_survey.survey_in_progress");
        startSurveyNotFound = config.getString("start_survey.survey_not_found");
        startSurveyTimeNumber = config.getString("start_survey.error_time_number");
        startSurveyStartMessage = config.getString("start_survey.survey_start");
        startSurveyEndMessage = config.getString("end_survey.survey_end");
        startSurveyCorrectVote = config.getString("vote_survey.successful_vote");
        startSurveyNoVoteAgain = config.getString("vote_survey.no_vote_again");
        startSurveyNoName = config.getString("start_survey.error_no_name_survey");
        startSurveyVote = config.getString("start_survey.vote");
        startSurveyVoteHover = config.getString("start_survey.vote_hover");
        startSurveyStartVote = config.getString("start_survey.survey_vote");
        startSurveyNoNumberOption = config.getString("start_survey.error_number_option");
        endSurveyCorrectEnd = config.getString("end_survey.correct_end_survey");
        endSurveyNotFound = config.getString("end_survey.survey_not_found");
        endSurveyNoName = config.getString("end_survey.error_no_name_survey");
        endSurveyNoRun = config.getString("end_survey.survey_no_run");
        voteSurveyNotFound = config.getString("vote_survey.survey_not_found");
        voteSurveyNoName = config.getString("vote_survey.error_no_name_survey");
        voteSurveyNoRun = config.getString("vote_survey.survey_no_run");
        startSurveyPlayerMessage = config.getString("start_survey.survey_start_message");
        endSurveyNoVotes = config.getString("end_survey.no_votes");
        listSurveyStart = config.getString("list_surveys.start");
        endSurveyWinner = config.getString("end_survey.winner_message");
        listSurveysRemove = config.getString("list_surveys.remove");
        formatOptionsStart = config.getString("start_survey.option_listing_format");
        formatOptionsEnd = config.getString("end_survey.option_listing_format");
    }
    public void reloadConfig(){
        configFile.reloadConfig();
        loadConfig();
    }
    public FileConfiguration getConfig(){
        return configFile.getConfig();
    }
    public void saveConfig(){
        configFile.saveConfig();
        loadConfig();
    }
    public String getIncorrectCommand() {
        return incorrectCommand;
    }
    public String getReloadCommand() {
        return reloadCommand;
    }
    public String getCreateSurveyMessage() {
        return createSurveyMessage;
    }
    public String getCreateSurveyOptionsNumber() {
        return createSurveyOptionsNumber;
    }
    public String getCreateSurveyOptions() {
        return createSurveyOptions;
    }
    public String getCreateSurveyFinished() {
        return createSurveyFinished;
    }
    public String getCreateSurveyNoName() {
        return createSurveyNoName;
    }
    public String getCreateSurveyNoNumber() {
        return createSurveyNoNumber;
    }
    public String getCreateSurveyMinimumOption() {
        return createSurveyMinimumOption;
    }
    public String getCreateSurveyNoCommands() {
        return createSurveyNoCommands;
    }
    public String getCreateSurveyMessageSet() {
        return createSurveyMessageSet;
    }
    public String getCreateSurveyOptionsNumberSet() {
        return createSurveyOptionsNumberSet;
    }
    public String getCreateSurveyOptionsSet() {
        return createSurveyOptionsSet;
    }
    public String getCreateSurveyNameSet() {
        return createSurveyNameSet;
    }
    public String getCreateSurveyRejoinMessage() {
        return createSurveyRejoinMessage;
    }
    public String getCreateSurveyRejoinConfirm() {
        return createSurveyRejoinConfirm;
    }
    public String getCreateSurveyRejoinCancel() {
        return createSurveyRejoinCancel;
    }
    public String getCreateSurveyExist() {
        return createSurveyExist;
    }
    public String getRemoveSurveyRemoved() {
        return removeSurveyRemoved;
    }
    public String getRemoveSurveyNotFound() {
        return removeSurveyNotFound;
    }
    public String getRemoveSurveyNoName() {
        return removeSurveyNoName;
    }
    public String getEditSurveyNoName() {
        return editSurveyNoName;
    }
    public String getEditSurveyNotFound() {
        return editSurveyNotFound;
    }
    public String getEditSurveyEnter() {
        return editSurveyEnter;
    }
    public String getEditSurveyEnterMessage() {
        return editSurveyEnterMessage;
    }
    public String getEditSurveyEnterOptions() {
        return editSurveyEnterOptions;
    }
    public String getEditSurveyReplaceMessage() {
        return editSurveyReplaceMessage;
    }
    public String getEditSurveyReplaceOption() {
        return editSurveyReplaceOption;
    }
    public String getEditSurveyFinished() {
        return editSurveyFinished;
    }
    public String getEditSurveyEnterOption() {
        return editSurveyEnterOption;
    }
    public String getEditSurveyHoverOption() {
        return editSurveyHoverOption;
    }
    public String getEditSurveyReplaceMessageSet() {
        return editSurveyReplaceMessageSet;
    }
    public String getEditSurveyReplaceOptionSet() {
        return editSurveyReplaceOptionSet;
    }
    public String getEditSurveyNoCommands() {
        return editSurveyNoCommands;
    }
    public String getEditSurveyNoNumber() {
        return editSurveyNoNumber;
    }
    public String getEditSurveyEnterAdd() {
        return editSurveyEnterAdd;
    }
    public String getEditSurveyAddOption() {
        return editSurveyAddOption;
    }
    public String getEditSurveyAddOptionSet() {
        return editSurveyAddOptionSet;
    }
    public String getEditSurveyRemoveOption() {
        return editSurveyRemoveOption;
    }
    public String getEditSurveyRemoveHover() {
        return editSurveyRemoveHover;
    }
    public String getEditSurveyRemoveSymbol() {
        return editSurveyRemoveSymbol;
    }
    public String getEditSurveyEditSymbol() {
        return editSurveyEditSymbol;
    }
    public String getEditSurveyRemoveMinimum() {
        return editSurveyRemoveMinimum;
    }
    public String getEditSurveyAddAnotherOption() {
        return editSurveyAddAnotherOption;
    }
    public String getListSurveyView() {
        return listSurveyView;
    }
    public String getListSurveyViewMessage() {
        return listSurveyViewMessage;
    }
    public String getListSurveyEdit() {
        return listSurveyEdit;
    }
    public String getListSurveyList() {
        return listSurveyList;
    }
    public String getSurveyDisabled() {
        return surveyDisabled;
    }
    public String getStartSurveyInProgress() {
        return startSurveyInProgress;
    }
    public String getStartSurveyNotFound() {
        return startSurveyNotFound;
    }
    public String getStartSurveyTimeNumber() {
        return startSurveyTimeNumber;
    }
    public String getStartSurveyStartMessage() {
        return startSurveyStartMessage;
    }
    public String getStartSurveyEndMessage() {
        return startSurveyEndMessage;
    }
    public String getStartSurveyCorrectVote() {
        return startSurveyCorrectVote;
    }
    public String getStartSurveyNoVoteAgain() {
        return startSurveyNoVoteAgain;
    }
    public String getStartSurveyNoName() {
        return startSurveyNoName;
    }
    public String getStartSurveyVote() {
        return startSurveyVote;
    }
    public String getStartSurveyVoteHover() {
        return startSurveyVoteHover;
    }
    public String getStartSurveyStartVote() {
        return startSurveyStartVote;
    }
    public String getStartSurveyNoNumberOption() {
        return startSurveyNoNumberOption;
    }
    public String getEndSurveyCorrectEnd() {
        return endSurveyCorrectEnd;
    }
    public String getEndSurveyNoName() {
        return endSurveyNoName;
    }
    public String getEndSurveyNoRun() {
        return endSurveyNoRun;
    }
    public String getEndSurveyNotFound() {
        return endSurveyNotFound;
    }
    public String getVoteSurveyNotFound() {
        return voteSurveyNotFound;
    }
    public String getVoteSurveyNoName() {
        return voteSurveyNoName;
    }
    public String getVoteSurveyNoRun() {
        return voteSurveyNoRun;
    }
    public String getStartSurveyPlayerMessage() {
        return startSurveyPlayerMessage;
    }
    public String getEndSurveyNoVotes() {
        return endSurveyNoVotes;
    }
    public String getListSurveyStart() {
        return listSurveyStart;
    }
    public String getEndSurveyWinner() {
        return endSurveyWinner;
    }
    public String getNoPermission() {
        return noPermission;
    }
    public String getListSurveysRemove() {
        return listSurveysRemove;
    }
    public String getFormatOptionsStart() {
        return formatOptionsStart;
    }
    public String getFormatOptionsEnd() {
        return formatOptionsEnd;
    }
    public List<String> getHelpCommand() {
        return helpCommand;
    }
    public List<String> getHelpJSONMessages() {
        return helpJSONMessages;
    }
}
