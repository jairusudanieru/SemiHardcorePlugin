package plugin.semihardcoreplugin.Events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinLeaveEvents implements Listener {

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) {
        //Event variables
        Player player = event.getPlayer();
        String playerName = player.getName();
        if (!player.hasPlayedBefore()) {
            player.sendMessage(ChatColor.YELLOW + "Welcome to Semi Hardcore " + playerName);
        }

    }

}
