package com.spinalcraft.registrar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.spinalcraft.spinalpack.SpinalcraftPlugin;

public class RegistrarEventListener implements Listener{
	
	private Announcer announcer;
	
	public RegistrarEventListener(RegistrarPlugin plugin) {
		this.announcer = new Announcer(plugin);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		try {
			if (shouldAnnouncePlayer(player)){
				announcer.announcePlayer(player);
			}
		} catch (SQLException e) {
			System.err.println("Database error");
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onRegistered(PlayerSiteRegisterEvent event){
		Player player = Bukkit.getPlayer(UUID.fromString(event.getUUID()));
		if (player == null){
			return;
		} else {
			try {
				if (shouldAnnouncePlayer(player)){
					announcer.announcePlayer(player);
				}
			} catch (SQLException e) {
				System.err.println("Database error");
				e.printStackTrace();
			}
		}
	}
	
	private boolean shouldAnnouncePlayer(Player player) throws SQLException{
		String query = "SELECT announced FROM " + RegistrarPlugin.dbName + ".applications WHERE uuid = ?";
		PreparedStatement stmt = SpinalcraftPlugin.prepareStatement(query);
		stmt.setString(1, player.getUniqueId().toString());
		ResultSet rs = stmt.executeQuery();
		if (!rs.next()) return false;
		return !rs.getBoolean("announced");
	}
}
