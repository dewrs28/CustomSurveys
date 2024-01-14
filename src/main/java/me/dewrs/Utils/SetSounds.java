package me.dewrs.Utils;

import me.dewrs.CustomSurveys;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SetSounds {
    private CustomSurveys plugin;

    public SetSounds(CustomSurveys plugin) {
        this.plugin = plugin;
    }

    public void setSounds(Player player, CustomSound sound){
        if(plugin.getMainConfigManager().isSoundsEnabled()) {
            Sound newSound = null;
            if (sound == CustomSound.ERROR) {
                newSound = plugin.getMainConfigManager().getSoundError();
            }
            if (sound == CustomSound.COMMAND_SEND) {
                newSound = plugin.getMainConfigManager().getSoundCommand();
            }
            if (sound == CustomSound.NO_PERMISSION) {
                newSound = plugin.getMainConfigManager().getSoundNoPermission();
            }
            if (sound == CustomSound.START_SURVEY) {
                newSound = plugin.getMainConfigManager().getSoundStartSurvey();
            }
            if (sound == CustomSound.END_SURVEY) {
                newSound = plugin.getMainConfigManager().getSoundEndSurvey();
            }
            if (sound == CustomSound.CORRECT) {
                newSound = plugin.getMainConfigManager().getSoundCorrect();
            }
            player.playSound(player.getLocation(), newSound, 10, 2);
        }
    }
}
