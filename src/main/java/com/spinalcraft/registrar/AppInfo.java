package com.spinalcraft.registrar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.spinalcraft.spinalpack.SpinalcraftPlugin;
import com.spinalcraft.usernamehistory.UHistory;
import com.spinalcraft.usernamehistory.UsernameHistory;

public class AppInfo {
	private UUID uuid;
	private String username;
	private String country;
	private String timestamp;
	private int birthYear;
	private String heard;
	private String comment;
	
	public void reportTo(CommandSender sender){		
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		
		sender.sendMessage(ChatColor.GOLD + "Username: " + ChatColor.GREEN + username);
		sender.sendMessage(ChatColor.GOLD + "Joined " + ChatColor.BLUE + timestamp);
		sender.sendMessage(ChatColor.GOLD + "Hailing from " + ChatColor.GREEN + country);
		sender.sendMessage(ChatColor.GOLD + "Approx. age: " + ChatColor.GREEN + (currentYear - birthYear));
		sender.sendMessage(ChatColor.GOLD + "Heard of Spinalcraft from " + ChatColor.GREEN + heardFrom());
		sender.sendMessage(ChatColor.GOLD + "Additional info: " + ChatColor.WHITE + comment);
	}
	
	private String heardFrom(){
		if(heard.equals("mcsl"))
			return "Minecraft Server List";
		else if(heard.equals("pmc"))
			return "Planet Minecraft";
		else if(heard.equals("reddit"))
			return "Reddit";
		else if(heard.equals("players"))
			return referrerList();
		else
			return "(Unspecified)";
	}
	
	private String referrerList(){
		String query = "SELECT * FROM " + RegistrarPlugin.dbName + ".referredPlayers WHERE player = ?";
		try {
			PreparedStatement stmt = SpinalcraftPlugin.prepareStatement(query);
			stmt.setString(1, uuid.toString());
			
			ResultSet rs = stmt.executeQuery();
			
			ArrayList<String> usernames = new ArrayList<String>();
			while(rs.next()){
				UHistory hist = UsernameHistory.getHistoryFromUuid(UUID.fromString(rs.getString("referrer")));
				if(hist == null)
					continue;
				usernames.add(hist.getOldUsernames()[hist.getOldUsernames().length - 1].getName());
			}
			if(usernames.size() == 0)
				return "(Unspecified players)";
			
			return ChatColor.GREEN + implode(ChatColor.GOLD + ", " + ChatColor.GREEN, usernames.toArray(new String[usernames.size()]));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "(Unspecified players)";
	}
	
	public static AppInfo fromUsername(String username){
		AppInfo info = new AppInfo();
		info.username = username;
		try {
			
			UHistory hist = UsernameHistory.getHistoryFromUsername(username);
			if(hist == null)
				return null;
			UUID uuid = hist.getUuid();
			if(uuid == null)
				return null;
			info.uuid = uuid;
			info.username = hist.getOldUsernames()[hist.getOldUsernames().length - 1].getName();
			
			if(!info.loadByUuid(uuid.toString()))
				return null;
			
			return info;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private boolean loadByUuid(String uuid) throws SQLException{
		String query = "SELECT * FROM " + RegistrarPlugin.dbName + ".applications WHERE uuid = ?";
		PreparedStatement stmt = SpinalcraftPlugin.prepareStatement(query);
		stmt.setString(1, uuid);
		ResultSet rs = stmt.executeQuery();
		if(!rs.first())
			return false;
		
//		username = rs.getString("username"); //Username may be outdated at time of retrieval. Do I even need this field?
		country = rs.getString("country");
		timestamp = rs.getString("timestamp");
		birthYear = rs.getInt("year");
		heard = rs.getString("heard");
		comment = rs.getString("comment") != "" ? rs.getString("comment") : "(none)";
		
		return true;
	}
	
	public static String implode(String separator, String... data) {
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < data.length - 1; i++) {
	        if (!data[i].matches(" *")) {
	            sb.append(data[i]);
	            sb.append(separator);
	        }
	    }
	    sb.append(data[data.length - 1].trim());
	    return sb.toString();
	}
	

	public static String dashedUuid(String uuid) {
		StringBuffer sb = new StringBuffer(uuid);
		sb.insert(8, "-");
		 
		sb = new StringBuffer(sb.toString());
		sb.insert(13, "-");
		 
		sb = new StringBuffer(sb.toString());
		sb.insert(18, "-");
		 
		sb = new StringBuffer(sb.toString());
		sb.insert(23, "-");
		 
		return sb.toString();
	}
}
