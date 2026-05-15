package de.jaunikapauni.axclans.command;

import de.jaunikapauni.axclans.AxClans;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DeleteCommand implements CommandExecutor {
    AxClans reference;
    public DeleteCommand(AxClans reference){
        this.reference = reference;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        Player p = (Player) sender;
        String name = args[0];
        reference.getClanManager().deleteClan(name);
        p.sendMessage("You successfully deleted the clan " + name);
        return true;
    }
}
