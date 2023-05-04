package plugin.semihardcoreplugin.Events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinLeaveEvent implements Listener {

    private final JavaPlugin plugin;
    public PlayerJoinLeaveEvent(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        //Event variables
        Player player = event.getPlayer();
        String playerName = player.getName();
        String newJoinMessage = plugin.getConfig().getString("welcomeMessage");
        String joinMessage = plugin.getConfig().getString("joinMessage");

        //Setting the join message
        if (newJoinMessage != null) newJoinMessage = newJoinMessage.replace("%player%",playerName);
        if (joinMessage != null) joinMessage = joinMessage.replace("%player%",playerName);
        if (!player.hasPlayedBefore() && newJoinMessage != null) {
            event.setJoinMessage(ChatColor.YELLOW + newJoinMessage);
        } else if (joinMessage != null) {
            event.setJoinMessage(ChatColor.YELLOW + joinMessage);
        }
    }

    @EventHandler
    public void onPlayerLeave(@NotNull PlayerQuitEvent event) {
        //Event variables
        Player player = event.getPlayer();
        String playerName = player.getName();
        String leaveMessage = plugin.getConfig().getString("leaveMessage");

        //Setting the quit message
        if (leaveMessage != null) leaveMessage = leaveMessage.replace("%player%",playerName);
        if (leaveMessage != null) event.setQuitMessage(ChatColor.YELLOW + leaveMessage);
    }

}
