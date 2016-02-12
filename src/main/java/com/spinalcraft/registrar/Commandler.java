package com.spinalcraft.registrar;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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
		return false;
	}
}
