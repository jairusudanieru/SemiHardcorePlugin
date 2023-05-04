package plugin.semihardcoreplugin.Events;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class PlayerHeartEvents implements Listener {

    //Getting the plugin instance
    private final JavaPlugin plugin;
    public PlayerHeartEvents(JavaPlugin plugin) {
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
            player.sendTitle(ChatColor.RED + "ELIMINATED!", playerName, 10, 60, 10);
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
            player.sendTitle(ChatColor.RED + "-1 Heart", playerName, 10, 60, 10);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.setGameMode(GameMode.SURVIVAL);
                player.spawnParticle(Particle.TOTEM, player.getLocation(), 30);
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1f, 1.5f);
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

    @EventHandler
    public void onPlayerRegenerate(@NotNull PlayerInteractEvent event) {
        //Event variables
        ItemMeta meta;
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();
        if (item == null) return;
        Material itemType = player.getInventory().getItemInMainHand().getType();
        String playerName = event.getPlayer().getName();
        int maxHeart = plugin.getConfig().getInt("maxHearts");
        boolean actionType = action.isRightClick();
        boolean itemRight = itemType.equals(Material.NETHER_STAR);
        meta = item.getItemMeta();

        //Checking if the item is the correct item
        if (actionType && itemRight && meta.hasEnchant(Enchantment.DURABILITY)) {
            AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            //Checking if the player have less than 20 hearts, if the player does, it won't allow to add more
            if (maxHealth != null && maxHealth.getBaseValue() < (maxHeart*2)) {
                double newValue = maxHealth.getBaseValue() + 2;
                maxHealth.setBaseValue(newValue);
                player.setHealth(newValue);
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                player.spawnParticle(Particle.HEART, player.getLocation(), 30);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                player.sendTitle(ChatColor.GREEN + "+1 Heart", playerName, 10, 60, 10);
            } else {
                player.sendMessage(ChatColor.RED + "You have reached the maximum heart allowed!");
            }
        }
    }

}
