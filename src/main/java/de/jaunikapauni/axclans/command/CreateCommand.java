package de.jaunikapauni.axclans.command;

import de.jaunikapauni.axclans.AxClans;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CreateCommand implements CommandExecutor {
    AxClans reference;
    public CreateCommand(AxClans reference){
        this.reference = reference;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Player p = (Player) sender;
        if(!p.hasPermission("axclans.create")){
            p.sendMessage("You don't have the permission! [axclans.create]");
            return true;
        }
        String name = args[0];
        reference.getClanManager().createClan(name, p);
        p.sendMessage("You successfully created the clan " + name);
        return true;
    }
}
