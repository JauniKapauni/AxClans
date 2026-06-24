package de.jaunikapauni.axclans.command;

import de.jaunikapauni.axclans.AxClans;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JoinCommand implements CommandExecutor {
    AxClans reference;
    public JoinCommand(AxClans reference){
        this.reference = reference;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Player p = (Player) sender;
        if(!p.hasPermission("axclans.join")){
            p.sendMessage("You don't have the permission! [axclans.join]");
            return true;
        }
        String name = args[0];
        if(reference.getClanManager().clanExists(name)){
            reference.getClanManager().requestJoin(p, name);
            p.sendMessage("You requested to join " + name);
        } else {
            p.sendMessage(ChatColor.RED + "Clan " + name + " does not exist!");
        }
        return true;
    }
}
