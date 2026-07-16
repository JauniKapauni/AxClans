package de.jaunikapauni.axclans.manager;

import de.jaunikapauni.axclans.AxClans;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClanManager {
    AxClans reference;

    public ClanManager(AxClans reference) {
        this.reference = reference;
    }

    public boolean createClan(String name, Player p) {
        try (Connection conn = reference.getDatabaseManager().getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO clans(name, created_at) VALUES (?, NOW())")) {
                ps.setString(1, name);
                ps.executeUpdate();
                try (PreparedStatement ps1 = conn.prepareStatement("SELECT id FROM clans WHERE name = ?")) {
                    ps1.setString(1, name);
                    ResultSet rs = ps1.executeQuery();
                    if (rs.next()) {
                        int clan_id = rs.getInt("id");
                        try (PreparedStatement ps2 = conn.prepareStatement("UPDATE players SET clan_id = ? WHERE uuid = ?")) {
                            ps2.setInt(1, clan_id);
                            ps2.setString(2, p.getUniqueId().toString());
                            ps2.executeUpdate();
                        }
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void deleteClan(String name) {
        try (Connection conn = reference.getDatabaseManager().getConnection()) {
            int clanId = -1;
            try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM clans WHERE name = ?")) {
                ps.setString(1, name);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        clanId = rs.getInt("id");
                    }
                }
            }
            if (clanId != -1) {
                try (PreparedStatement ps1 = conn.prepareStatement("UPDATE players SET clan_id = null WHERE clan_id = ?")) {
                    ps1.setInt(1, clanId);
                    ps1.executeUpdate();
                }
                try (PreparedStatement ps2 = conn.prepareStatement("DELETE FROM clans WHERE id = ?")) {
                    ps2.setInt(1, clanId);
                    ps2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void joinClan(Player p, String name) {
        try (Connection conn = reference.getDatabaseManager().getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM clans WHERE name = ?")) {
                ps.setString(1, name);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int clan_id = rs.getInt("id");
                    try (PreparedStatement ps2 = conn.prepareStatement("UPDATE players SET clan_id = ? WHERE uuid = ?")) {
                        ps2.setInt(1, clan_id);
                        ps2.setString(2, p.getUniqueId().toString());
                        ps2.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void leaveClan(Player p) {
        try (Connection conn = reference.getDatabaseManager().getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE players SET clan_id = null WHERE uuid = ?")) {
                ps.setString(1, p.getUniqueId().toString());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getClanName(Player p) {
        try (Connection conn = reference.getDatabaseManager().getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT clan_id FROM players WHERE uuid = ?")) {
                ps.setString(1, p.getUniqueId().toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int clan_id = rs.getInt("clan_id");
                    try (PreparedStatement ps2 = conn.prepareStatement("SELECT name FROM clans WHERE id = ?")) {
                        ps2.setInt(1, clan_id);
                        ResultSet rs2 = ps2.executeQuery();
                        if (rs2.next()) {
                            return rs2.getString("name");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    public boolean clanExists(String name) {
        try (Connection conn = reference.getDatabaseManager().getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM clans WHERE name = ?")) {
                ps.setString(1, name);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkIfPlayerInClan(String uuid) {
        try (Connection conn = reference.getDatabaseManager().getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT clan_id FROM players WHERE uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String clanId = rs.getString("clan_id");
                    if (clanId != null) {
                        return true;
                    }
                    return false;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getMemberCount(String name){
        try(Connection conn = reference.getDatabaseManager().getConnection()){
            try(PreparedStatement ps = conn.prepareStatement("SELECT id FROM clans WHERE name = ?")){
                ps.setString(1, name);
                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()){
                        int clanid = rs.getInt("id");
                        try(PreparedStatement ps2 = conn.prepareStatement("SELECT COUNT(*) FROM players WHERE clan_id = ?")){
                            ps2.setInt(1, clanid);
                            try(ResultSet rs2 = ps2.executeQuery()){
                                if(rs2.next()){
                                    return rs2.getInt(1);
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public void requestJoin(Player p, String name) {
        try (Connection conn = reference.getDatabaseManager().getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM clans WHERE name = ?")) {
                ps.setString(1, name);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int clanId = rs.getInt("id");
                    try (PreparedStatement ps2 = conn.prepareStatement("INSERT INTO requests(clan_id, player_uuid) VALUES (?, ?)")) {
                        ps2.setInt(1, clanId);
                        ps2.setString(2, p.getUniqueId().toString());
                        ps2.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean acceptRequest(Player owner, String playerName) {
        int clanId = 0;
        OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
        String uuid = target.getUniqueId().toString();
        try (Connection conn = reference.getDatabaseManager().getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT clan_id FROM players WHERE uuid = ?")) {
                ps.setString(1, owner.getUniqueId().toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    if(rs.getObject("clan_id") == null){
                        return false;
                    }
                    clanId = rs.getInt("clan_id");
                    try (PreparedStatement add = conn.prepareStatement("UPDATE players SET clan_id = ? WHERE uuid = ?")) {
                        add.setInt(1, clanId);
                        add.setString(2, uuid);
                        add.executeUpdate();
                    }
                    try (PreparedStatement del = conn.prepareStatement("DELETE FROM requests WHERE player_uuid = ?")) {
                        del.setString(1, uuid);
                        del.executeUpdate();
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void denyRequest(String playerName) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
        try (Connection conn = reference.getDatabaseManager().getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM requests WHERE player_uuid = ?")) {
                ps.setString(1, target.getUniqueId().toString());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
