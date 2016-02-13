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
	
	private String dbName;
	
	public Announcer(String dbName){
		this.dbName = dbName;
	}
	
	@Override
	public void run(){
		while(true){
			try {
				ArrayList<String> unannounced = getUnannouncedPlayers();
				for(String uuid : unannounced){
					announce(uuid);
				}
				Thread.sleep(5000);
			} catch (Exception e) { //Nothing stops this train. Nothing.
				e.printStackTrace();
			} 
		}
	}
	
	private ArrayList<String> getUnannouncedPlayers() throws SQLException{
		String query = "SELECT uuid FROM " + dbName + ".applications WHERE announced = 0";
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
		
		String query = "UPDATE " + dbName + ".applications SET announced = 1 WHERE uuid = ?";
		PreparedStatement stmt = Spinalpack.prepareStatement(query);
		stmt.setString(1, player.getUniqueId().toString());
		stmt.execute();
		
		Bukkit.broadcastMessage(ChatColor.AQUA + "Welcome our newest Spinaling, " + ChatColor.BLUE + player.getName() + ChatColor.AQUA + "!");
		player.sendMessage(ChatColor.GOLD + "Welcome to Spinalcraft!");
	}
}
