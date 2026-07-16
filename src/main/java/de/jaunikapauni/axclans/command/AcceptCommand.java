package de.jaunikapauni.axclans.command;

import de.jaunikapauni.axclans.AxClans;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AcceptCommand implements CommandExecutor {
    AxClans reference;
    public AcceptCommand(AxClans reference){
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
        if(!reference.getClanManager().isOwner(p)){
            p.sendMessage("You are not the owner of the clan!");
            return true;
        }
        if(reference.getClanManager().acceptRequest(p, args[0])){
            p.sendMessage("Accepted request");
        } else {
            p.sendMessage("Could not accept request");
        }
        return true;
    }
}
