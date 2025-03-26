package pluginForWork;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class TipCommand implements CommandExecutor {
    private final PluginForWork plugin;
    private final Random random = new Random();

    public TipCommand(PluginForWork plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if (!(sender instanceof Player)) {
            sender.sendMessage("只有玩家可以使用这个命令！");
            return true;
        }

        Player player = (Player) sender;
        Location pos = player.getLocation();

        List<?> regions = plugin.getConfig().getList("region");

        boolean isInRegion = regions.stream().anyMatch(region -> {

            if (region instanceof Map) {
                Map<String, Object> regoinInfos = (Map<String, Object>) region;


                if (!String.valueOf(pos.getWorld()).equals(regoinInfos.get("world"))) {
                    return false;
                }

                Map<String, Integer> min = (Map<String, Integer>) regoinInfos.get("min");
                Map<String, Integer> max = (Map<String, Integer>) regoinInfos.get("max");

                int minX = min.get("x");
                int minZ = min.get("z");
                int maxX = max.get("x");
                int maxZ = max.get("z");


                return pos.getX() >= minX && pos.getX() <= maxX &&
                        pos.getZ() >= minZ && pos.getZ() <= maxZ;

            }
            return false;
        });

        if (!isInRegion) {
            player.sendMessage("§c你必须进入特定区域才能使用这个命令！");
            return true;
        }

        List<?> rewards = plugin.getConfig().getList("rewards");
        Object randomReward = rewards.get(random.nextInt(rewards.size()));


        if (randomReward instanceof Map) {
            Map<String, Object> section = (Map<String, Object>) randomReward;

            Material material = Material.valueOf((String) section.get("material"));
            int min = (int) section.get("min");
            int max = (int) section.get("max");
            int amount = random.nextInt(max - min + 1) + min;

            ItemStack item = new ItemStack(material, amount);
            player.getInventory().addItem(item);
            player.sendMessage("§a你获得了 " + amount + " 个 " + material.name());
        }

        return true;
    }
}
