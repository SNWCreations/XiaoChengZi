package snw.rfm.xiaochengzi;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public final class XiaoChengZi extends JavaPlugin implements Listener {
    // already updated when constructing
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Set<Predicate<Material>> checks;

    static {
        checks = new HashSet<>() {
            {
                add(m -> m == Material.TALL_GRASS);
                add(Tag.TALL_FLOWERS::isTagged);
                add(m -> Tag.CLIMBABLE.isTagged(m) && m != Material.LADDER);
            }
        };
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getScheduler()
                .runTaskTimer(this, this::check, 5L, 5L);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void check() {
        for (Player player : getServer().getOnlinePlayers()) {
            Block headBlock = player.getEyeLocation().getBlock();
            Material type = headBlock.getType();
            if (checks.stream().anyMatch(i -> i.test(type))) {
                headBlock.setType(Material.AIR, false);
                Location where = headBlock.getLocation();
                World world = headBlock.getWorld();
                world.spawnParticle(Particle.BLOCK_DUST, where, 20, type.createBlockData());
                world.playSound(player, Sound.BLOCK_GRASS_BREAK, SoundCategory.MASTER, 1, 1);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(
                ChatColor.GREEN + "小橙子插件正在运行。作者: ZX夏夜之风",
                ChatColor.GREEN + "我们修复了小橙子可以躲在花/草丛中的漏洞。",
                ChatColor.GREEN + "-- ZX夏夜之风, 2024.6.9 17:26"
        );
    }
}
