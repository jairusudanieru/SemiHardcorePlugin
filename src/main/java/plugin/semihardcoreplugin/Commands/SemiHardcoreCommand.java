package plugin.semihardcoreplugin.Commands;

import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SemiHardcoreCommand implements CommandExecutor, TabCompleter {

    private final JavaPlugin plugin;
    public SemiHardcoreCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        //Checking if the args is 1, then show the array for tab completion
        boolean isConsole = sender instanceof ConsoleCommandSender;
        boolean isOperator = sender.isOp();
        List<String> choices = new ArrayList<>();
        if (isConsole) return Collections.emptyList();
        if (args.length == 1 && !isOperator) {
            choices.add("info");
            choices.add("creator");
            return choices;
        } else if (args.length == 1) {
            choices.add("reload");
            choices.add("info");
            choices.add("creator");
            return choices;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        //Message variables
        String noPerms = "§cSorry, you don't have permission to use this command!";
        String usage = "Usage: /semihardcore (reload|info|creator)";
        String reloaded = "§e[Semi-Hardcore] Configuration successfully reloaded!";
        String info = "This is a simple plugin that adds a twist to Hardcore mode Minecraft servers.";
        String creator = "§eJairusu#5237 is the creator of this plugin!";

        //Checking if it's possible to use the command
        if (!command.getName().equalsIgnoreCase("semihardcore")) return false;
        boolean isOperator = sender.isOp();
        if (args.length != 1) {
            sender.sendMessage(usage);
            return true;
        }

        //Checking which args the sender used
        String choice = args[0];
        if (choice.equals("reload") && isOperator) {
            plugin.reloadConfig();
            sender.sendMessage(reloaded);
        } else if (choice.equals("reload")) {
            sender.sendMessage(noPerms);
        } else if (choice.equals("info")) {
            sender.sendMessage(info);
        } else if (choice.equals("creator")) {
            sender.sendMessage(creator);
        } else {
            sender.sendMessage(usage);
        }
        return true;
    }
}
