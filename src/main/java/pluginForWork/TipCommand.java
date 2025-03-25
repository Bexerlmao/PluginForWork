package pluginForWork;

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


        Byte playerState = player.getPersistentDataContainer().get(
                new NamespacedKey(plugin, "PlayerInRegion"),
                PersistentDataType.BYTE
        );


        if (playerState == null || playerState != 1) {
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
