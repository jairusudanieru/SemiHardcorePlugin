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

    //The commands to register
    public void registerCommands() {
        getCommand("semihardcore").setExecutor(new ReloadCommand(this));
        getCommand("revive").setExecutor(new ReviveCommand(this));
        getCommand("withdraw").setExecutor(new WithdrawCommand(this));
    }

    //The events to register
    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerDeathRespawnEvent(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerUseHeartsEvent(this), this);
    }

    //Checking if the plugin AuthMe is on the server
    public void checkAuthMe() {
        Plugin authMe = Bukkit.getServer().getPluginManager().getPlugin("AuthMe");
        if (authMe != null) {
            Bukkit.getLogger().info("[Semi-Hardcore] AuthMe plugin found!");
            Bukkit.getPluginManager().registerEvents(new PlayerAuthMeEvent(this), this);
        } else {
            Bukkit.getPluginManager().registerEvents(new PlayerJoinLeaveEvent(this), this);
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
        registerCommands();
        registerEvents();
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
