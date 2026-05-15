package de.jaunikapauni.axclans.placeholder;

import de.jaunikapauni.axclans.AxClans;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClanPlaceholder extends PlaceholderExpansion {
    AxClans reference;
    public ClanPlaceholder(AxClans reference){
        this.reference = reference;
    }

    @Override
    public @NotNull String getIdentifier(){
        return "axclans";
    }

    @Override
    public @NotNull String getAuthor(){
        return "JauniKapauni";
    }

    @Override
    public @NotNull String getVersion(){
        return "0.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer p, @NotNull String params){
        if(params.equalsIgnoreCase("name")){
            return getClanName(p);
        }
        return null;
    }

    public String getClanName(OfflinePlayer p){
        try(Connection conn = reference.getDatabaseManager().getConnection()){
            try(PreparedStatement ps = conn.prepareStatement("SELECT clan_id FROM players WHERE uuid = ?")){
                ps.setString(1, p.getUniqueId().toString());
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    int clan_id = rs.getInt("clan_id");
                    try(PreparedStatement ps2 = conn.prepareStatement("SELECT name FROM clans WHERE id = ?")){
                        ps2.setInt(1, clan_id);
                        ResultSet rs2 = ps2.executeQuery();
                        if(rs2.next()){
                            String clanName = rs2.getString("name");
                            return clanName;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "-";
    }
}
