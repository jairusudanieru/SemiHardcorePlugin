package plugin.semihardcoreplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.semihardcoreplugin.Commands.Revive;
import plugin.semihardcoreplugin.Commands.ReviveMe;
import plugin.semihardcoreplugin.Commands.RevivePlayer;
import plugin.semihardcoreplugin.Commands.Withdraw;
import plugin.semihardcoreplugin.Events.PlayerAuthMeEvents;
import plugin.semihardcoreplugin.Events.PlayerHeartEvents;
import plugin.semihardcoreplugin.Events.PlayerJoinLeaveEvents;
import plugin.semihardcoreplugin.Recipes.HeartRecipe;

public final class SemiHardcorePlugin extends JavaPlugin {

    //The events to register
    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new PlayerHeartEvents(this), this);
    }

    //The commands to register
    public void registerCommands() {
        getCommand("revive").setExecutor(new Revive(this));
        getCommand("reviveme").setExecutor(new ReviveMe(this));
        getCommand("reviveplayer").setExecutor(new RevivePlayer(this));
        getCommand("withdraw").setExecutor(new Withdraw(this));
    }

    public void checkAuthMe() {
        Plugin authMe = Bukkit.getServer().getPluginManager().getPlugin("AuthMe");
        if (authMe != null) {
            Bukkit.getLogger().info("[Semi-Hardcore] AuthMe plugin found!");
            getServer().getPluginManager().registerEvents(new PlayerAuthMeEvents(this), this);
        } else {
            getServer().getPluginManager().registerEvents(new PlayerJoinLeaveEvents(this), this);
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
