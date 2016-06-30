package com.spinalcraft.registrar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.spinalcraft.spinalpack.Spinalpack;

public class RegistrarEventListener implements Listener{
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		try {
			if (shouldAnnouncePlayer(player)){
				announcePlayer(player);
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
					announcePlayer(player);
				}
			} catch (SQLException e) {
				System.err.println("Database error");
				e.printStackTrace();
			}
		}
	}
	
	private boolean shouldAnnouncePlayer(Player player) throws SQLException{
		String query = "SELECT announced FROM " + RegistrarPlugin.dbName + ".applications WHERE uuid = ?";
		PreparedStatement stmt = Spinalpack.prepareStatement(query);
		stmt.setString(1, player.getUniqueId().toString());
		ResultSet rs = stmt.executeQuery();
		if (rs == null){
			return false;
		}
		return rs.getBoolean("announced");
	}
	
	private void announcePlayer(Player player) throws SQLException{
		if(player == null)
			return;
		
		String query = "UPDATE " + RegistrarPlugin.dbName + ".applications SET announced = 1 WHERE uuid = ?";
		PreparedStatement stmt = Spinalpack.prepareStatement(query);
		stmt.setString(1, player.getUniqueId().toString());
		stmt.execute();
		
		Spinalpack spinalPack = (Spinalpack) Bukkit.getPluginManager().getPlugin("Spinalpack");
		if (spinalPack != null){
			spinalPack.broadcastMessage(ChatColor.AQUA + "Welcome our newest Spinaling, " + ChatColor.BLUE + player.getName() + ChatColor.AQUA + "!");
		}
		player.sendMessage(ChatColor.GOLD + "Welcome to Spinalcraft!");
	}
}
