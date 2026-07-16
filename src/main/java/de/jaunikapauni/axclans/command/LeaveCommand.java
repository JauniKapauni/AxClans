package de.jaunikapauni.axclans.command;

import de.jaunikapauni.axclans.AxClans;
import org.bukkit.Bukkit;
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
        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can run this command");
            return true;
        }
        Player p = (Player) sender;
        if(args.length != 1){
            return false;
        }
        if(!p.hasPermission("axclans.leave")){
            p.sendMessage("You don't have the permission! [axclans.leave]");
            return true;
        }
        Bukkit.getScheduler().runTaskAsynchronously(reference, () -> {
            if(reference.getClanManager().checkIfPlayerInClan(p.getUniqueId().toString())){
                String clanName = reference.getClanManager().getClanName(p);
                if(reference.getClanManager().getMemberCount(clanName) == 1){
                    reference.getClanManager().deleteClan(clanName);
                }
                reference.getClanManager().leaveClan(p);
                Bukkit.getScheduler().runTask(reference, () -> {
                    p.sendMessage("You left " + clanName);
                });
            } else {
                Bukkit.getScheduler().runTask(reference, () -> {
                    p.sendMessage("You are not in a clan!");
                });
            }
        });
        return true;
    }
}
