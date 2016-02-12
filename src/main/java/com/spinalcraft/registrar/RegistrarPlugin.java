package com.spinalcraft.registrar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class RegistrarPlugin extends JavaPlugin {
	
	static ConsoleCommandSender console;
	
	@Override
	public void onEnable(){
		console = Bukkit.getConsoleSender();
		
		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		
		console.sendMessage(ChatColor.BLUE + "Registrar online!");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		return Commandler.handle(sender, cmd, label, args);
	}
}
