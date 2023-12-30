package yv.tils.smp.utils.configs.discord;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import yv.tils.smp.YVtils;

import java.io.File;
import java.io.IOException;

/**
 * @version 4.6.8
 * @since 4.6.8
 */
public class save_yml {
    File file = new File(YVtils.getInstance().getDataFolder() + "/Discord", "save.yml");
    YamlConfiguration ymlfile = YamlConfiguration.loadConfiguration(file);

    public void StringInput() {
        ymlfile.addDefault("DiscordID", "Minecraft Username + UUID");
        ymlfile.options().copyDefaults(true);
        fileSave();
    }

    public void fileSave() {
        try {
            ymlfile.save(file);
        } catch (IOException e) {
            System.out.println("-------");
            Bukkit.getConsoleSender().sendMessage("File creation Error! Please get Support on the YVtils Support Discord");
            System.out.println("-------");
        }
    }
}
