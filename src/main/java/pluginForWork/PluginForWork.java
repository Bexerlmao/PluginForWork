package pluginForWork;

import org.bukkit.Registry;
import org.bukkit.plugin.java.JavaPlugin;

public final class PluginForWork extends JavaPlugin {

    @Override
    public void onEnable() {

        saveResource("config.yml", false);
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        getCommand("tip").setExecutor(new TipCommand(this));

    }

}
