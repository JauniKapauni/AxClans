package de.jaunikapauni.axclans.command;

import de.jaunikapauni.axclans.AxClans;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CreateCommand implements CommandExecutor {
    AxClans reference;

    public CreateCommand(AxClans reference) {
        this.reference = reference;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can run this command");
            return true;
        }
        Player p = (Player) sender;
        if (args.length != 1) {
            return false;
        }
        if (!p.hasPermission("axclans.create")) {
            p.sendMessage("You don't have the permission! [axclans.create]");
            return true;
        }
        Bukkit.getScheduler().runTaskAsynchronously(reference, () -> {
            if (reference.getClanManager().checkIfPlayerInClan(p.getUniqueId().toString())) {
                Bukkit.getScheduler().runTask(reference, () -> {
                    p.sendMessage("You already have a clan!");
                });
                return;
            }
            String name = args[0];

                if (!reference.getEconomyAPI().has(p.getUniqueId(), 1000)) {
                    Bukkit.getScheduler().runTask(reference, () -> {
                        p.sendMessage("Not enough money");
                    });
                    return;
                }
                if (reference.getClanManager().createClan(name, p)) {
                    reference.getEconomyAPI().withdraw(p.getUniqueId(), 1000);
                    Bukkit.getScheduler().runTask(reference, () -> {
                        p.sendMessage("You successfully created the clan " + name);
                    });
                } else {
                    Bukkit.getScheduler().runTask(reference, () -> {
                        p.sendMessage("Clan already exists");
                    });
                }
            });
        return true;
    }
}
