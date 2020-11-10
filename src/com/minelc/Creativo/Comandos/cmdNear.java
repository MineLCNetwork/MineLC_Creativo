package com.minelc.Creativo.Comandos;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.minelc.CORE.Controller.Jugador;

public class cmdNear implements CommandExecutor {
	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
		Player p = (Player) s;
		Jugador jug = Jugador.getJugador(p);
		if(jug.is_MODERADOR()) {
			s.sendMessage(ChatColor.GOLD + "======== Jugadores Cercanos ========");
			for(Player rc : Bukkit.getOnlinePlayers()) {
				try {
				if(rc.getWorld() == p.getWorld()) {
					if(!(rc.getLocation().distance(p.getLocation()) > 500)) {

						s.sendMessage(ChatColor.GOLD  + rc.getName() + " - " + ChatColor.YELLOW + Math.round(rc.getLocation().distance(p.getLocation()))+ "m");
					} 
				}
			
			} catch(Exception ex) {
			}
		
			}
			s.sendMessage(ChatColor.GOLD + "===================================");
		}
		
		return true;
	}
}
