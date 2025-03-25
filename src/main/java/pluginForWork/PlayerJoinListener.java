package pluginForWork;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;


public class PlayerJoinListener implements Listener {

    private final PluginForWork plugin;

    public PlayerJoinListener(PluginForWork plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        FileConfiguration config = plugin.getConfig();

        String title = config.getString("join.title.message");
        int fadeIn = config.getInt("join.title.fadeIn");
        int stay = config.getInt("join.title.stay");
        int fadeOut = config.getInt("join.title.fadeOut");

        event.getPlayer().sendTitle(title, "", fadeIn, stay, fadeOut);


        String message = config.getString("join.message");
        if (message != null) {
            event.getPlayer().sendMessage(message);
        }


        float volume = (float) config.getDouble("join.sound.volume");
        float pitch = (float) config.getDouble("join.sound.pitch");
        String soundKey = config.getString("join.sound.name");
        NamespacedKey key = NamespacedKey.fromString(soundKey);

        event.getPlayer().playSound(event.getPlayer().getLocation(), Registry.SOUNDS.get(key), SoundCategory.PLAYERS, volume, pitch );
    }
}
