package de.jaunikapauni.axclans.command;

import de.jaunikapauni.axclans.AxClans;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DenyCommand implements CommandExecutor {
    AxClans reference;
    public DenyCommand(AxClans reference){
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
        if (!p.hasPermission("axclans.deny")) {
            p.sendMessage("You don't have the permission! [axclans.deny]");
            return true;
        }
        Bukkit.getScheduler().runTaskAsynchronously(reference, () -> {
            if(!reference.getClanManager().isOwner(p)){
                Bukkit.getScheduler().runTask(reference, () -> {
                    p.sendMessage("You are not the owner of the clan!");
                });
                return;
            }
            if(reference.getClanManager().denyRequest(args[0])){
                Bukkit.getScheduler().runTask(reference, () -> {
                    p.sendMessage("Denied request!");
                });
            } else {
                Bukkit.getScheduler().runTask(reference, () -> {
                    p.sendMessage("No request found!");
                });
            }
        });
        return true;
    }
}
