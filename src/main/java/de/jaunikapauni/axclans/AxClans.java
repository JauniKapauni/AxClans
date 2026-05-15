package de.jaunikapauni.axclans;

import de.jaunikapauni.axclans.command.CreateCommand;
import de.jaunikapauni.axclans.command.DeleteCommand;
import de.jaunikapauni.axclans.listener.PlayerJoinListener;
import de.jaunikapauni.axclans.manager.ClanManager;
import de.jaunikapauni.axclans.manager.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class AxClans extends JavaPlugin {
    DatabaseManager databaseManager;
    public DatabaseManager getDatabaseManager(){
        return databaseManager;
    }
    ClanManager clanManager;
    public ClanManager getClanManager(){
        return clanManager;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        try{
            databaseManager = new DatabaseManager(this);
            clanManager = new ClanManager(this);
            if(databaseManager.initDatabaseTable1() || databaseManager.initDatabaseTable2() == false){
                getLogger().severe("Error creating table!");
                Bukkit.getServer().shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getCommand("create").setExecutor(new CreateCommand(this));
        getCommand("create").setExecutor(new DeleteCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
