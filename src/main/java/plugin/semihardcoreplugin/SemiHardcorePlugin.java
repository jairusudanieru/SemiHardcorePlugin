package plugin.semihardcoreplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.semihardcoreplugin.Commands.Revive;
import plugin.semihardcoreplugin.Commands.ReviveMe;
import plugin.semihardcoreplugin.Commands.RevivePlayer;
import plugin.semihardcoreplugin.Commands.WithdrawHearts;
import plugin.semihardcoreplugin.Events.PlayerHeartEvents;
import plugin.semihardcoreplugin.Recipes.HeartRecipe;

public final class SemiHardcorePlugin extends JavaPlugin {

    //The events to register
    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerHeartEvents(this), this);
    }

    //The commands to register
    public void registerCommands() {
        getCommand("withdrawheart").setExecutor(new WithdrawHearts());
        getCommand("reviveplayer").setExecutor(new RevivePlayer());
        getCommand("revive").setExecutor(new Revive());
        getCommand("reviveme").setExecutor(new ReviveMe());
    }

    //The recipes to register
    public void registerRecipes() {
        HeartRecipe.registerRecipe(this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        registerEvents();
        registerCommands();
        registerRecipes();
        Bukkit.getLogger().info("SemiHardcore Plugin has successfully enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("SemiHardcore Plugin has successfully disabled!");
    }
}
