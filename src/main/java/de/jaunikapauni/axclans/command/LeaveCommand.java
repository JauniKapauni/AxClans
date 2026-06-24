package de.jaunikapauni.axclans.command;

import de.jaunikapauni.axclans.AxClans;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LeaveCommand implements CommandExecutor {
    AxClans reference;
    public LeaveCommand(AxClans reference){
        this.reference = reference;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Player p = (Player) sender;
        if(!p.hasPermission("axclans.leave")){
            p.sendMessage("You don't have the permission! [axclans.leave]");
            return true;
        }
        if(reference.getClanManager().checkIfPlayerInClan(p.getUniqueId().toString())){
            String clanName = reference.getClanManager().getClanName(p);
            reference.getClanManager().leaveClan(p);
            p.sendMessage("You left " + clanName);
        } else {
            p.sendMessage("You are not in a clan!");
        }
        return true;
    }
}
