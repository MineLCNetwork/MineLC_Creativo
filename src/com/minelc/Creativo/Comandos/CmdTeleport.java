package com.minelc.Creativo.Comandos;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.minelc.CORE.Controller.Jugador;


public class CmdTeleport implements CommandExecutor {

	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
		Player p = (Player) s;
		Jugador jug = Jugador.getJugador(p);
		
		if(jug.is_MODERADOR()) {
			if(args.length > 0) {
				Player ptp = null;
				for(Player Online : Bukkit.getOnlinePlayers()) {
					if(Online.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
						ptp = Online;
						if(Online.getName().equalsIgnoreCase(args[0])) {
							break;
						}
					}
				}
				if(ptp != null) {
					p.sendMessage(ChatColor.DARK_PURPLE+""+ChatColor.BOLD+"MOD > "+ChatColor.YELLOW+"Teleportado a "+ChatColor.GREEN+ptp.getName()+ChatColor.YELLOW+"!");
					p.teleport(ptp);
					
					for(Player Online : Bukkit.getOnlinePlayers())
						p.showPlayer(Online);
				} else {
					p.sendMessage(ChatColor.DARK_PURPLE+""+ChatColor.BOLD+"MOD > "+ChatColor.YELLOW+"No se encontro al jugador "+ChatColor.RED+args[0]+ChatColor.YELLOW+"!");
				}
			} else {
				p.sendMessage(ChatColor.DARK_PURPLE+""+ChatColor.BOLD+"MOD > "+ChatColor.YELLOW+"Usa /teleport <jugador>!");
			}
		} else {
			p.sendMessage(ChatColor.RED+"Solo los moderadores pueden usar este comando!");
		}
		return true;
	}
}

