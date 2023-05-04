package plugin.semihardcoreplugin.Recipes;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class HeartRecipe {

    public static void registerRecipe(JavaPlugin plugin) {
        //Creating the custom item
        ItemStack result = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = result.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 5, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(ChatColor.RESET + "Player Heart");
        result.setItemMeta(meta);

        //Creating the custom recipe for the item
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "player_heart"), result);
        recipe.shape(" N ", "NSN", " N ");
        recipe.setIngredient('N', Material.NETHERITE_INGOT);
        recipe.setIngredient('S', Material.NETHER_STAR);

        //Adding the recipe to the server
        Bukkit.getServer().addRecipe(recipe);
    }
}
