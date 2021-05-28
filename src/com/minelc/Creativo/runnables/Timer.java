package com.minelc.Creativo.runnables;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.minelc.Creativo.listeners.PlayerListener;

public class Timer implements Runnable {
	int restartCount = 86400; // 24 hrs
	int clearcount = 43200;
	int autoclearentities = 60;
	
	@Override
	public void run() {
		//autoRestart
		if (restartCount == 600) {
			Bukkit.broadcastMessage(ChatColor.RED+"¡El servidor sera reiniciado en 10 minutos!");
		}
		if (restartCount == 30 || restartCount == 60) {
			Bukkit.broadcastMessage(ChatColor.RED+"¡El servidor sera reiniciado en "+restartCount+" segundos!");
		}
		if(restartCount > 1 && restartCount <= 10) {
			Bukkit.broadcastMessage(ChatColor.RED+"¡El servidor sera reiniciado en "+restartCount+" segundos!");
		} else if(restartCount == 1) {
			Bukkit.broadcastMessage(ChatColor.RED+"¡El servidor sera reiniciado en "+restartCount+" segundo!");
			
			//clear entities
			for(World w : Bukkit.getWorlds()) {
				List<Entity> ents = w.getEntities();
				if(ents.size() > 250) {
					for(Entity ent : w.getEntities()) {
						if(ent.getType() != EntityType.PLAYER) {
							ent.remove();
						}
					}
				}
			}
			
		} else if(restartCount == 0) {
			Bukkit.shutdown();
		}
		restartCount--;
		
		if(restartCount == clearcount) {
			PlayerListener.resetRedstone();
		}
		
		if(autoclearentities-- == 0) {
			for(World w : Bukkit.getWorlds()) {
				List<Entity> ents = w.getEntities();
				if(ents.size() > 500) {
					for(Entity ent : w.getEntities()) {
						if(ent.getType() != EntityType.PLAYER) {
							ent.remove();
						}
					}
				}
			}
			autoclearentities = 60;
		}
		
	}

}
