package com.spinalcraft.registrar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.spinalcraft.spinalpack.Spinalpack;

public class Announcer implements Runnable{
	
	@Override
	public void run(){
		while(true){
			try {
				ArrayList<String> unannounced = getUnannouncedPlayers();
				for(String uuid : unannounced){
					announce(uuid);
				}
			} catch (Exception e) { //Nothing stops this train. Nothing.
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private ArrayList<String> getUnannouncedPlayers() throws SQLException{
		String query = "SELECT uuid FROM " + RegistrarPlugin.dbName + ".applications WHERE announced = 0";
		PreparedStatement stmt = Spinalpack.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();
		ArrayList<String> players = new ArrayList<String>();
		while(rs.next())
			players.add(rs.getString("uuid"));
		return players;
	}
	
	private void announce(String uuid) throws SQLException{
		Player player = Bukkit.getPlayer(UUID.fromString(uuid));
		
		if(player == null)
			return;
		
		String query = "UPDATE " + RegistrarPlugin.dbName + ".applications SET announced = 1 WHERE uuid = ?";
		PreparedStatement stmt = Spinalpack.prepareStatement(query);
		stmt.setString(1, player.getUniqueId().toString());
		stmt.execute();
		
		Bukkit.broadcastMessage(ChatColor.AQUA + "Welcome our newest Spinaling, " + ChatColor.BLUE + player.getName() + ChatColor.AQUA + "!");
		player.sendMessage(ChatColor.GOLD + "Welcome to Spinalcraft!");
	}
}
