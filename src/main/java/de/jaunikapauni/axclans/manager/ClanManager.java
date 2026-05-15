package de.jaunikapauni.axclans.manager;

import de.jaunikapauni.axclans.AxClans;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClanManager {
    AxClans reference;
    public ClanManager(AxClans reference){
        this.reference = reference;
    }

    public void createClan(String name, Player p){
        try(Connection conn = reference.getDatabaseManager().getConnection()){
            try(PreparedStatement ps = conn.prepareStatement("INSERT INTO clans(name, created_at) VALUES (?, NOW())")){
                ps.setString(1, name);
                ps.executeUpdate();
                try(PreparedStatement ps1 = conn.prepareStatement("SELECT id FROM clans WHERE name = ?")){
                    ps1.setString(1, name);
                    ResultSet rs = ps1.executeQuery();
                    if(rs.next()){
                        String clan_id = rs.getString("id");
                        try(PreparedStatement ps2 = conn.prepareStatement("UPDATE players SET clan_id WHERE uuid = ?")){
                            ps2.setString(1, clan_id);
                            ps2.setString(2, p.getUniqueId().toString());
                            ps2.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
