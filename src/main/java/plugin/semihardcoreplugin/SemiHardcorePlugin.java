package plugin.semihardcoreplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.semihardcoreplugin.Commands.*;
import plugin.semihardcoreplugin.Events.PlayerAuthMeEvent;
import plugin.semihardcoreplugin.Events.PlayerDeathRespawnEvent;
import plugin.semihardcoreplugin.Events.PlayerJoinLeaveEvent;
import plugin.semihardcoreplugin.Events.PlayerUseHeartsEvent;
import plugin.semihardcoreplugin.Recipes.HeartRecipe;

public final class SemiHardcorePlugin extends JavaPlugin {

    //The events to register
    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerDeathRespawnEvent(this), this);
        getServer().getPluginManager().registerEvents(new PlayerUseHeartsEvent(this), this);
    }

    //The commands to register
    public void registerCommands() {
        getCommand("revive").setExecutor(new Revive(this));
        getCommand("withdraw").setExecutor(new Withdraw(this));
    }

    //Checking if the plugin AuthMe is on the server
    public void checkAuthMe() {
        Plugin authMe = Bukkit.getServer().getPluginManager().getPlugin("AuthMe");
        if (authMe != null) {
            Bukkit.getLogger().info("[Semi-Hardcore] AuthMe plugin found!");
            getServer().getPluginManager().registerEvents(new PlayerAuthMeEvent(this), this);
        } else {
            getServer().getPluginManager().registerEvents(new PlayerJoinLeaveEvent(this), this);
        }
    }

    //The recipes to register
    public void registerRecipes() {
        HeartRecipe.registerRecipe(this);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        registerEvents();
        registerCommands();
        registerRecipes();
        checkAuthMe();
        Bukkit.getLogger().info("[Semi-Hardcore] Plugin has successfully enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("[Semi-Hardcore] Plugin has successfully disabled!");
    }
}
