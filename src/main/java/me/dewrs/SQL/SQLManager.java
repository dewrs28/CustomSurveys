package me.dewrs.SQL;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SQLManager {
    public static boolean surveyExist(Connection connection, String survey){
        try{
            PreparedStatement statement = connection.prepareStatement("SHOW TABLES LIKE '"+survey+"'");
            ResultSet result = statement.executeQuery();
            if(result.next()){
                return true;
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    public static void createSQLSurvey(Connection connection, String survey, int options){
        try {
            if (!surveyExist(connection, survey)) {
                PreparedStatement statement = connection.prepareStatement("CREATE TABLE "+survey+"(survey VARCHAR(200), message VARCHAR(200))");
                statement.executeUpdate();
                createSQLOptions(connection, survey, options);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    public static void createSQLOptions(Connection connection, String survey, int options){
        try {
            if (surveyExist(connection, survey)) {
                for(int i = 1; i < options+1; i++) {
                    PreparedStatement statement = connection.prepareStatement("ALTER TABLE " + survey + " ADD COLUMN option_"+i+" VARCHAR(200)");
                    statement.executeUpdate();
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public static void setSQLSurvey(Connection connection, String survey, String message, ArrayList<String> options){
        try {
            if (surveyExist(connection, survey)) {
                String command = "INSERT INTO "+survey+" VALUES ('"+survey+"', '"+message+"', ";
                int i = 0;
                for(String op : options){
                    if(i != options.size()-1) {
                        command += "'" + op + "', ";
                    }else{
                        command += "'"+op+"')";
                    }
                    i++;
                }
                PreparedStatement statement = connection.prepareStatement(command);
                statement.executeUpdate();
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public static void removeSQLSurvey(Connection connection, String survey){
        try {
            if (surveyExist(connection, survey)) {
                PreparedStatement statement = connection.prepareStatement("DROP TABLE "+survey);
                statement.executeUpdate();
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public static void editSQLMessage(Connection connection, String survey, String newMessage){
        try {
            if (surveyExist(connection, survey)) {
                PreparedStatement statement = connection.prepareStatement("UPDATE "+survey+" SET message = '"+newMessage+"'");
                statement.executeUpdate();
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public static String getSQLMessage(Connection connection, String survey){
        try {
            if (surveyExist(connection, survey)) {
                PreparedStatement statement = connection.prepareStatement("SELECT message FROM "+survey);
                ResultSet result = statement.executeQuery();
                if(result.next()){
                    return result.getString("message");
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return "";
    }

    public static String getSQLOption(Connection connection, String survey, String option){
        try {
            if (surveyExist(connection, survey)) {
                PreparedStatement statement = connection.prepareStatement("SELECT "+option+" FROM "+survey);
                ResultSet result = statement.executeQuery();
                if(result.next()){
                    return result.getString(option);
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return "";
    }

    public static void addSQLOption(Connection connection, String survey, String option){
        try {
            if (surveyExist(connection, survey)) {
                int newCol = getSQLNumberOptions(connection, survey)+1;
                PreparedStatement statement = connection.prepareStatement("ALTER TABLE "+survey+" ADD COLUMN option_"+newCol+" VARCHAR(200)");
                statement.executeUpdate();
                statement = connection.prepareStatement("UPDATE "+survey+" SET option_"+getSQLNumberOptions(connection,survey)+" = '"+option+"'");
                statement.executeUpdate();
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public static int getSQLNumberOptions(Connection connection, String survey){
        ArrayList<String> numOptions = new ArrayList<>();
        try {
            if (surveyExist(connection, survey)) {
                PreparedStatement statement = connection.prepareStatement("SHOW COLUMNS FROM "+survey);
                ResultSet result = statement.executeQuery();
                while(result.next()){
                    numOptions.add(result.getString("Field"));
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return numOptions.size()-2;
    }
    public static ArrayList<String> getSQLAllColumns(Connection connection, String survey){
        ArrayList<String> columns = new ArrayList<>();
        try {
            if (surveyExist(connection, survey)) {
                PreparedStatement statement = connection.prepareStatement("SHOW COLUMNS FROM "+survey);
                ResultSet result = statement.executeQuery();
                while(result.next()){
                    columns.add(result.getString("Field"));
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return columns;
    }

    public static void editSQLOption(Connection connection, String survey, String num, String newOption){
        try {
            if (surveyExist(connection, survey)) {
                PreparedStatement statement = connection.prepareStatement("UPDATE "+survey+" SET option_"+num+" = '"+newOption+"'");
                statement.executeUpdate();
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public static ArrayList<String> getSQLStringOptions(Connection connection, String survey){
        ArrayList<String> options = new ArrayList<>();
        try {
            if (surveyExist(connection, survey)) {
                for(int i = 1; i < getSQLNumberOptions(connection,survey)+1; i++) {
                    PreparedStatement statement = connection.prepareStatement("SELECT option_"+i+" FROM "+survey);
                    ResultSet result = statement.executeQuery();
                    if(result.next()) {
                        options.add(result.getString("option_"+i));
                    }
                }
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return options;
    }

    public static void removeSQLOption(Connection connection, String survey, int num){
        try {
            if (surveyExist(connection, survey)) {
                PreparedStatement statement = connection.prepareStatement("ALTER TABLE "+survey+" DROP COLUMN option_"+num);
                statement.executeUpdate();
                orderSQLOptions(connection, survey, num);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public static void orderSQLOptions(Connection connection, String survey, int num){
        LinkedHashMap<String, Object> temp = new LinkedHashMap<>();
        ArrayList<String> tempOptions = new ArrayList<>();
        try {
            if (surveyExist(connection, survey)) {
                temp.put("message", getSQLMessage(connection, survey));
                for(int i = 1; i < getSQLNumberOptions(connection,survey)+2; i++) {
                    if(i != num) {
                        PreparedStatement statement = connection.prepareStatement("SELECT option_" + i + " FROM " + survey);
                        ResultSet result = statement.executeQuery();
                        if (result.next()) {
                            tempOptions.add(result.getString("option_" + i));
                        }
                    }
                }
                removeSQLSurvey(connection, survey);
                createSQLSurvey(connection, survey, tempOptions.size());
                setSQLSurvey(connection, survey, (String) temp.get("message"), tempOptions);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public static ArrayList<String> getSQLSurveys(Connection connection, String database) {
        ArrayList<String> numOptions = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT table_name AS name\n" +
                    "FROM information_schema.tables WHERE table_schema = '"+database+"'");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                numOptions.add(result.getString("name"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return numOptions;
    }
}
