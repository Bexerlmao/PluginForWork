package pluginForWork;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataType;

import java.lang.module.Configuration;
import java.util.List;
import java.util.Map;

public class PlayerPosListener implements Listener {
    private final PluginForWork plugin;

    public PlayerPosListener(PluginForWork plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Location pos = event.getTo();

        List<?> regions = plugin.getConfig().getList("region");

        boolean isInRegion = regions.stream().anyMatch(region -> {

            if (region instanceof Map) {
                Map<String, Object> regoinInfos = (Map<String, Object>) region;

                if(!String.valueOf(pos.getWorld()).equals(regoinInfos.get("world"))){
                    return false;
                }
                Map<String, Integer> min = (Map<String, Integer>) regoinInfos.get("min");
                Map<String, Integer> max = (Map<String, Integer>) regoinInfos.get("max");

                int minX = min.get("x");
                int minZ = min.get("z");
                int maxX = max.get("x");
                int maxZ = max.get("z");

                return pos.getX() >= minX && pos.getX() <= maxX&&
                        pos.getZ() >= minZ && pos.getZ() <= maxZ;

            }
            return false;
        });

        event.getPlayer().sendMessage(String.valueOf(isInRegion ? 1 : 0));

        event.getPlayer().getPersistentDataContainer().set(
          new NamespacedKey(plugin, "PlayerInRegion"),
                PersistentDataType.BYTE,
                (byte) (isInRegion ? 1 : 0)
        );

    }

}
