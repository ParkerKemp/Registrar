package com.spinalcraft.registrar;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.spinalcraft.spinalpack.Spinalpack;

public class Commandler {
	@SuppressWarnings("deprecation")
	public static boolean handle(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("data")){
	    	if(args.length == 0)
	    		return false;
	    	AppInfo info = AppInfo.fromUsername(args[0]);
	    	if(info == null){
	    		sender.sendMessage(ChatColor.RED + "No data found for " + args[0] + "!");
	    		return true;
	    	}
	    	
	    	info.reportTo(sender);
	    	return true;
	    }
		//used for testing. Resets the announced bit
		if(cmd.getName().equalsIgnoreCase("announceresetbit")){
			try{
				if (args.length == 0){
					if (sender instanceof Player){
						setAnnouncementBit(((Player) sender).getUniqueId(), false);
						return true;
					} else {
						return false;
					}
				} else {
					String playerName = args[0];
					Player player = Bukkit.getPlayer(playerName);
					if (player == null){
						sender.sendMessage("Player " + playerName + " not found!");
					} else {
						setAnnouncementBit(player.getUniqueId(), false);
						return true;
					}
				}
			} catch (SQLException e){
				sender.sendMessage("Database error");
				e.printStackTrace();
			}
		}
		//This command is called by the site using a socket.
		if (cmd.getName().equalsIgnoreCase("__announce__")){
			if (sender instanceof Player){
				sender.sendMessage("You shouldn't use this command!");
				return true;
			}
			if (args.length == 1){
				Bukkit.getPluginManager().callEvent(new PlayerSiteRegisterEvent(args[0]));
				return true;
			}
			return true; //At this point its only used by the site, no need to post errors
		}
		return false;
	}
	
	private static void setAnnouncementBit(UUID uuid, boolean bit) throws SQLException{
		String query = "UPDATE " + RegistrarPlugin.dbName + ".applications SET announced = ? WHERE uuid = ?";
		PreparedStatement stmt = Spinalpack.prepareStatement(query);
		stmt.setString(1, bit ? "1" : "0");
		stmt.setString(1, uuid.toString());
		stmt.execute();
	}
}
