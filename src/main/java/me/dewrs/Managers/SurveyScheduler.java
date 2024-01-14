package me.dewrs.Managers;

import me.dewrs.CustomSurveys;
import org.bukkit.scheduler.BukkitRunnable;

public class SurveyScheduler extends BukkitRunnable {
    private CustomSurveys plugin;
    private String survey;

    public SurveyScheduler(CustomSurveys plugin, String survey) {
        this.plugin = plugin;
        this.survey = survey;
    }

    @Override
    public void run() {
        EndSurvey end = new EndSurvey(plugin);
        end.endSurvey(survey);
    }
}
