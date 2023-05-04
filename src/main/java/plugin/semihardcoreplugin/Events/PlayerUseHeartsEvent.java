package plugin.semihardcoreplugin.Events;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PlayerUseHeartsEvent implements Listener {

    private final JavaPlugin plugin;
    public PlayerUseHeartsEvent(JavaPlugin plugin) { this.plugin = plugin; }

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
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                player.spawnParticle(Particle.HEART, player.getLocation(), 30);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                player.sendTitle("§a+1 Heart", playerName, 10, 60, 10);
            } else {
                player.sendMessage("§cYou have reached the maximum heart allowed!");
            }
        }
    }

}
