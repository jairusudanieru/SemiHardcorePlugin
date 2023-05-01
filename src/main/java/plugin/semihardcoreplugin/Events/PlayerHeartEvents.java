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
import org.jetbrains.annotations.NotNull;
import plugin.semihardcoreplugin.SemiHardcorePlugin;

public class PlayerHeartEvents implements Listener {

    //Getting the plugin instance
    SemiHardcorePlugin plugin;
    public PlayerHeartEvents(SemiHardcorePlugin plugin){
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
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 1f, 1f);

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
        String playerName = event.getPlayer().getName();
        AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        //Checking if the player have more than 1 heart, if not the player gets eliminated
        if (maxHealth != null && maxHealth.getBaseValue() > 2) {
            double newValue = maxHealth.getBaseValue() - 2;
            maxHealth.setBaseValue(newValue);
            player.setHealth(newValue);
            player.sendTitle(ChatColor.RED + "-1 Heart", playerName, 10, 60, 10);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.setGameMode(GameMode.SURVIVAL);
                player.spawnParticle(Particle.TOTEM, player.getLocation(), 30);
                player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 1f, 1f);
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
        Material itemType = player.getInventory().getItemInMainHand().getType();
        String playerName = event.getPlayer().getName();

        //Checking if the item is not null and if the item is the correct item
        if (item != null) {
            meta = item.getItemMeta();
            if (action.isRightClick() && itemType.equals(Material.NETHER_STAR) && meta.hasEnchant(Enchantment.DURABILITY)) {
                AttributeInstance maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
                //Checking if the player have less than 20 hearts, if the player does, it won't allow to add more
                if (maxHealth != null && maxHealth.getBaseValue() != 40) {
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

}
