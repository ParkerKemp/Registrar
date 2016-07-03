package com.spinalcraft.registrar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commandler {
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
}
