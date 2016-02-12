package com.spinalcraft.registrar;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EventListener implements Listener{
	private JavaPlugin plugin;
	
	public EventListener(JavaPlugin plugin){
		this.plugin = plugin;
	}
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void onPlayerLoginEvent(final PlayerLoginEvent event){
		
	}
}
