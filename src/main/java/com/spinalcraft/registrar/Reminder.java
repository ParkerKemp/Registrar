package com.spinalcraft.registrar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Reminder implements Runnable {
	
	@Override
	public void run(){
		while(true){
			try {
				Bukkit.broadcast(ChatColor.GOLD + "Want to play on Spinalcraft? Take a minute and register to join at " + ChatColor.GREEN + "register.spinalcraft.com " + ChatColor.GOLD + "!", "registrar.reminder");
				Thread.sleep(1000 * 10);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}

}
