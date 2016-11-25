package com.spinalcraft.registrar;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import com.spinalcraft.skull.SpinalcraftPlugin;

public class RegistrarPlugin extends SpinalcraftPlugin {
	
	public static String dbName;
	
	@Override
	public void onEnable(){
		saveDefaultConfig();
		loadConfig();

		//new Thread(new Announcer()).start();
		new Thread(new Reminder()).start();
		
		Bukkit.getPluginManager().registerEvents(new RegistrarEventListener(this), this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		return Commandler.handle(sender, cmd, label, args);
	}

	private void loadConfig(){
		reloadConfig();
		FileConfiguration config = getConfig();
		
		dbName = config.getString("databaseName");
				
		//Copied for reference from Spawnalcraft
//		if(config.isSet("spawn.x") && config.isSet("spawn.y") && config.isSet("spawn.z")){
//			float x = (float) config.getDouble("spawn.x");
//			float y = (float) config.getDouble("spawn.y");
//			float z = (float) config.getDouble("spawn.z");
//			float pitch = (float) config.getDouble("spawn.pitch");
//			float yaw = (float) config.getDouble("spawn.yaw");
//			spawn = new Location(Bukkit.getWorld("world"), x, y, z, yaw, pitch);
//		}
//		forceSpawn = config.getBoolean("force-spawn-on-join");
	}
}
