package plugin.semihardcoreplugin.Events;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class PlayerDeathRespawnEvent implements Listener {

    //Getting the plugin instance
    private final JavaPlugin plugin;
    public PlayerDeathRespawnEvent(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(@NotNull PlayerDeathEvent event) {
        //Event variables
        Player player = event.getPlayer();
        String playerName = event.getPlayer().getName();
        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        //Checking if the player only have 1 heart
        if (maxHealth == null || maxHealth.getBaseValue() <= 2) {
            event.setCancelled(true);
            player.setGameMode(GameMode.SPECTATOR);
            player.getWorld().strikeLightningEffect(player.getLocation());
            player.sendTitle("§cELIMINATED!", playerName, 10, 60, 10);
            player.spawnParticle(Particle.WHITE_ASH, player.getLocation(), 30);

            //Check if player has items in inventory and drop them
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && !item.getType().equals(Material.AIR)) {
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                }
            }
            player.getInventory().clear();
        }
    }

    @EventHandler
    public void onPlayerRespawn(@NotNull PlayerRespawnEvent event) {
        //Event variables
        Player player = event.getPlayer();
        String playerName = player.getName();
        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        boolean safeRespawn = plugin.getConfig().getBoolean("safeRespawn");

        //Checking if the player have more than 1 heart, if not the player gets eliminated
        if (maxHealth != null && maxHealth.getBaseValue() > 2) {
            double newValue = maxHealth.getBaseValue() - 2;
            maxHealth.setBaseValue(newValue);
            player.setHealth(newValue);
            player.sendTitle("§c-1 Heart", playerName, 10, 60, 10);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.setGameMode(GameMode.SURVIVAL);
                player.spawnParticle(Particle.TOTEM, player.getLocation(), 30);
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1f, 1.5f);
                //Checking the config if the safeRespawn is enabled
                if (safeRespawn) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 3));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 0));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 6000, 0));
                }
                }, 1L);
        } else {
            maxHealth.setBaseValue(20);
            player.setHealth(20);
            player.setSaturation(20);
        }
    }

}
