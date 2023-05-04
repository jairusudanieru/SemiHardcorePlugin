package plugin.semihardcoreplugin.Commands;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Revive implements CommandExecutor, TabCompleter {

    //Getting the plugin instance
    private final JavaPlugin plugin;
    public Revive(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        //Checking if the args is 1, then show the array for tab completion
        boolean isConsole = sender instanceof ConsoleCommandSender;
        boolean isOperator = sender.isOp();
        if (isConsole) return Collections.emptyList();
        if (args.length == 1 && isOperator) {
            List<String> players = new ArrayList<>();
            for (Player player : Bukkit.getServer().getOnlinePlayers()) players.add(player.getName());
            return players;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        //Messages variables
        String specify = "Please specify a player you want to revive!";
        String usage = "Usage: /revive <playername>";
        String usagePl = "Usage: /revive";
        String cantFind = "§cCan't find that player!";
        String revSuccess = "§aYou have been successfully revived!";
        String cantRev = "§cYou can't revive this player because they're not eliminated!";
        String cantRevPl = "§cYou can't revive because you're not eliminated!";

        //Checking if it's possible to use the command
        if (!command.getName().equalsIgnoreCase("revive")) return false;
        boolean isPlayer = sender instanceof Player;
        boolean isConsole = sender instanceof ConsoleCommandSender;
        boolean isOperator = sender.isOp();
        if (isConsole && args.length == 0) { sender.sendMessage(specify); return true; }
        if (isPlayer && !isOperator && args.length > 0) { sender.sendMessage(usagePl); return true; }
        if (args.length > 1) { sender.sendMessage(usage); return true; }

        //Getting the player info
        Player target = null;
        if (isPlayer) target = (Player) sender;
        World world = target != null ? target.getWorld() : null;
        UUID uuid = target != null ? target.getUniqueId() : null;

        //Checking if the args is 1
        if (args.length == 1) {
            target = Bukkit.getPlayer(args[0]);
            if (target == null || !target.isOnline()) { sender.sendMessage(cantFind); return true; }
            world = target.getWorld();
            uuid = target.getUniqueId();
        }

        //Getting the file locations
        File advancementFile = null;
        File playerDataFile = null;
        File statsFile = null;
        if (world != null) {
            advancementFile = new File(world.getWorldFolder(), "advancements/" + uuid + ".json");
            playerDataFile = new File(world.getWorldFolder(), "playerdata/" + uuid + ".dat");
            statsFile = new File(world.getWorldFolder(), "stats/" + uuid + ".json");
        }

        //Checking if it's possible to revive the player!
        AttributeInstance maxHealth = target != null ? target.getAttribute(Attribute.GENERIC_MAX_HEALTH) : null;
        if (maxHealth != null && target.getGameMode() == GameMode.SPECTATOR && maxHealth.getBaseValue() <= 2) {
            // Delete player data files in world folder
            target.kickPlayer(revSuccess);
            if (advancementFile.exists()) advancementFile.delete();
            if (playerDataFile.exists()) playerDataFile.delete();
            if (statsFile.exists()) statsFile.delete();
            Bukkit.getLogger().info("Successfully revived " + target.getName());
        } else {
            if (target == sender) sender.sendMessage(cantRevPl);
            else sender.sendMessage(cantRev);
            return true;
        }

        return true;
    }
}
