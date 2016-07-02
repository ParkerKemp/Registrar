package com.spinalcraft.registrar;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerSiteRegisterEvent extends Event implements Cancellable{
	private boolean cancelled = false;
	private String uuid;
	private static final HandlerList handlers = new HandlerList();
	
	public PlayerSiteRegisterEvent(String uuid) {
		this.uuid = uuid;
	}
	
	public String getUUID(){
		return uuid;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}

}
