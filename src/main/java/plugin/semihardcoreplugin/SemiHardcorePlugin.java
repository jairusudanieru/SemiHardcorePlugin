package plugin.semihardcoreplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.semihardcoreplugin.Commands.WithdrawHearts;
import plugin.semihardcoreplugin.Events.PlayerHeartEvents;
import plugin.semihardcoreplugin.Recipes.HeartRecipe;

public final class SemiHardcorePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new PlayerHeartEvents(this), this);
        getCommand("withdrawheart").setExecutor(new WithdrawHearts());
        HeartRecipe.registerRecipe(this);
        Bukkit.getLogger().info("SemiHardcore Plugin has successfully enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("SemiHardcore Plugin has successfully disabled!");
    }
}
