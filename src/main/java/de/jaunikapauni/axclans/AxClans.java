package de.jaunikapauni.axclans;

import de.jaunikapauni.axclans.command.*;
import de.jaunikapauni.axclans.listener.PlayerJoinListener;
import de.jaunikapauni.axclans.manager.ClanManager;
import de.jaunikapauni.axclans.manager.DatabaseManager;
import de.jaunikapauni.axclans.placeholder.ClanPlaceholder;
import de.jaunikapauni.axeconomy.AxEconomy;
import de.jaunikapauni.axeconomy.api.EconomyAPI;
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
    EconomyAPI economyAPI;
    public EconomyAPI getEconomyAPI(){
        return economyAPI;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        try{
            databaseManager = new DatabaseManager(this);
            clanManager = new ClanManager(this);
            if(databaseManager.initDatabaseTable1() && databaseManager.initDatabaseTable2() && databaseManager.initDatabaseTable3() == false){
                getLogger().severe("Error creating table!");
                Bukkit.getServer().shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getCommand("create").setExecutor(new CreateCommand(this));
        getCommand("delete").setExecutor(new DeleteCommand(this));
        getCommand("join").setExecutor(new JoinCommand(this));
        getCommand("leave").setExecutor(new LeaveCommand(this));
        getCommand("accept").setExecutor(new AcceptCommand(this));
        getCommand("deny").setExecutor(new DenyCommand(this));
        if(Bukkit.getPluginManager().getPlugin("PlaceHolderAPI") != null){
            new ClanPlaceholder(this).register();
            getLogger().info("Successfully registered AxClans placeholders!");
        }
        getLogger().info("");
        getLogger().info("----------------------------------------");
        getLogger().info("Name: " + getName());
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info(String.join("Authors: " + ", ", getDescription().getAuthors()));
        getLogger().info("----------------------------------------");
        getLogger().info("");
        if(Bukkit.getPluginManager().getPlugin("AxEconomy") != null){
            AxEconomy axEconomy = (AxEconomy) Bukkit.getPluginManager().getPlugin("AxEconomy");
            if(axEconomy == null){
                throw new IllegalStateException("AxEconomy is missing!");
            }
            economyAPI = axEconomy.getEconomyAPI();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        databaseManager.close();
    }
}
