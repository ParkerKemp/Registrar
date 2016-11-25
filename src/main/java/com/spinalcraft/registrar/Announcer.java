package com.spinalcraft.registrar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.spinalcraft.spinalpack.SpinalcraftPlugin;

public class Announcer implements Runnable{
	private final static int ANNOUNCE_INTERVAL = 1000 * 60 * 5; //5 minutes
	
	private RegistrarPlugin plugin;
	
	public Announcer(RegistrarPlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void run(){
		while(true){
			try {
				ArrayList<String> unannounced = getUnannouncedPlayers();
				for(String uuid : unannounced){
					announcePlayer(Bukkit.getPlayer(UUID.fromString(uuid)));
				}
				
				Thread.sleep(ANNOUNCE_INTERVAL);
			} catch (Exception e) { //Nothing stops this train. Nothing.
				e.printStackTrace();
			}
		}
	}
	
	private ArrayList<String> getUnannouncedPlayers() throws SQLException{
		String query = "SELECT uuid FROM " + RegistrarPlugin.dbName + ".applications WHERE announced = 0";
		PreparedStatement stmt = SpinalcraftPlugin.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();
		ArrayList<String> players = new ArrayList<String>();
		while(rs.next())
			players.add(rs.getString("uuid"));
		return players;
	}
	
	public void announcePlayer(Player player) throws SQLException{
		if(player == null)
			return;
		
		String query = "UPDATE " + RegistrarPlugin.dbName + ".applications SET announced = 1 WHERE uuid = ?";
		PreparedStatement stmt = SpinalcraftPlugin.prepareStatement(query);
		stmt.setString(1, player.getUniqueId().toString());
		stmt.execute();
		
		plugin.broadcastMessage(ChatColor.AQUA + "Welcome our newest Spinaling, " + ChatColor.BLUE + player.getName() + ChatColor.AQUA + "!");
		
		player.sendMessage(ChatColor.GOLD + "Welcome to Spinalcraft!");
	}
}
