package org.ncc.maintenance_mode;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String s, String[] strings) {
        if (sender.hasPermission("smode.command")) {
            if (Main.getValue()) {
                Main.setValue(false);
                sender.sendMessage("Maintenance Mode now off");
            } else {
                Main.setValue(true);
                sender.sendMessage("Maintenance Mode now on");
                Bukkit.getOnlinePlayers().forEach((Consumer<Player>) player -> player.kick(Component.text(Main.kickMessage)));
            }
        }else{
            sender.sendMessage("You have no permission to use this command");
        }

        return true;
    }
}
