package com.minelc.Creativo.Comandos;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.minelc.CORE.Controller.Jugador;


public class CmdTeleportWorld implements CommandExecutor {

	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
		Player p = (Player) s;
		Jugador jug = Jugador.getJugador(p);
		
		if(jug.is_MODERADOR()) {
			if(args.length > 0) {
				World w = null;
				for(World wn : Bukkit.getWorlds()) {
					if(wn.getName().equalsIgnoreCase(args[0])) {
						w = wn;
						break;
					}
				}
				if(w != null) {
					p.sendMessage(ChatColor.DARK_PURPLE+""+ChatColor.BOLD+"MOD > "+ChatColor.YELLOW+"Teleportado a "+ChatColor.GREEN+w.getName()+ChatColor.YELLOW+"!");
					p.teleport(w.getSpawnLocation());
					
					for(Player Online : Bukkit.getOnlinePlayers())
						p.showPlayer(Online);
				} else {
					p.sendMessage(ChatColor.DARK_PURPLE+""+ChatColor.BOLD+"MOD > "+ChatColor.YELLOW+"No se encontro el mundo "+ChatColor.RED+args[0]+ChatColor.YELLOW+"!");
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

