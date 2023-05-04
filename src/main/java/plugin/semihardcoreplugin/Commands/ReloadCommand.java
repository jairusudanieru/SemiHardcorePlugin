package plugin.semihardcoreplugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    public ReloadCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        //Message variables
        String noPerms = "§cSorry, you don't have permission to use this command!";
        String usage = "Usage: /reload";
        String reloaded = "§e[Semi-Hardcore] Configuration successfully reloaded!";

        //Checking if it's possible to use the command
        if (!command.getName().equalsIgnoreCase("reload")) return false;
        boolean isPlayer = sender instanceof Player;
        boolean isOperator = sender.isOp();
        if (isPlayer && !isOperator) { sender.sendMessage(noPerms); return true; }
        if (args.length > 0) { sender.sendMessage(usage); return true; }

        //Reload the config
        plugin.reloadConfig();
        sender.sendMessage(reloaded);
        return true;
    }
}
