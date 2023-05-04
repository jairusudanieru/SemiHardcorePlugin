package plugin.semihardcoreplugin.Events;

import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.LogoutEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PlayerAuthMeEvent implements Listener {

    private final JavaPlugin plugin;
    public PlayerAuthMeEvent(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        //Setting the join message
        event.setJoinMessage(null);
    }

    @EventHandler
    public void onPlayerLeave(@NotNull PlayerQuitEvent event) {
        //Event variables
        Player player = event.getPlayer();
        String playerName = player.getName();
        String leaveMessage = plugin.getConfig().getString("leaveMessage");
        boolean isAuthed = AuthMeApi.getInstance().isAuthenticated(player);

        //Setting the quit message
        if (leaveMessage != null) leaveMessage = leaveMessage.replace("%player%",playerName);
        if (leaveMessage != null && isAuthed) event.setQuitMessage(ChatColor.YELLOW + leaveMessage);
    }

    @EventHandler
    public void onPlayerLogin(@NotNull LoginEvent event) {
        //Event variables
        Player player = event.getPlayer();
        String playerName = player.getName();
        String newJoinMessage = plugin.getConfig().getString("welcomeMessage");
        String joinMessage = plugin.getConfig().getString("joinMessage");

        //Setting the logged in message
        if (newJoinMessage != null) newJoinMessage = newJoinMessage.replace("%player%",playerName);
        if (joinMessage != null) joinMessage = joinMessage.replace("%player%",playerName);
        if (!player.hasPlayedBefore() && newJoinMessage != null) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + newJoinMessage);
        } else if (joinMessage != null) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + joinMessage);
        }
    }

    @EventHandler
    public void onPlayerLogout(@NotNull LogoutEvent event) {
        //Event variables
        Player player = event.getPlayer();
        String playerName = player.getName();
        String leaveMessage = plugin.getConfig().getString("joinMessage");

        //Setting the logged out message
        if (leaveMessage != null) leaveMessage = leaveMessage.replace("%player%",playerName);
        if (leaveMessage != null) Bukkit.broadcastMessage(ChatColor.YELLOW + leaveMessage);
    }
}
