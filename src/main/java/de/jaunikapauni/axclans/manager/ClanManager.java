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

    public void deleteClan(String name){
        try(Connection conn = reference.getDatabaseManager().getConnection()){
            int clanId = -1;
            try(PreparedStatement ps = conn.prepareStatement("SELECT id FROM clans WHERE name = ?")){
                ps.setString(1, name);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        clanId = rs.getInt("id");
                    }
                }
            }
            if(clanId != -1){
                try(PreparedStatement ps1 = conn.prepareStatement("UPDATE players SET clan_id = null WHERE clan_id = ?")){
                    ps1.setInt(1, clanId);
                    ps1.executeUpdate();
                }
                try(PreparedStatement ps2 = conn.prepareStatement("DELETE FROM clan WHERE id = ?")){
                    ps2.setInt(1, clanId);
                    ps2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
