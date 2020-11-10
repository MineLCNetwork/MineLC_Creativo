package com.minelc.Creativo.Comandos;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.minelc.CORE.Controller.Jugador;


public class CmdGameMode implements CommandExecutor {

	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
		Player p = (Player) s;
		Jugador jug = Jugador.getJugador(p);
		
		if(jug.is_VIP()) {
			if(args.length > 0) {
				GameMode newgm = GameMode.CREATIVE;
				try {
					for(GameMode gm : GameMode.values()) {
						try {
						if(gm.name().equalsIgnoreCase(args[0])) {
							newgm = gm;
						} else if(gm.getValue() == Integer.parseInt(args[0])) {
							newgm = gm;
						}
						} catch(Exception ex) {
							ex.printStackTrace();
						}
					}
				} catch(Exception ex) {
					ex.printStackTrace();
				} finally {
					p.setGameMode(newgm);
				}
			} else {
				p.sendMessage(ChatColor.RED+"Especifica el modo de juego [0 - Survival, 1 - Creative, 2 - Adventure, 3 - Spectator]!");				
			}
		} else {
			p.sendMessage(ChatColor.RED+"No tienes permisos para usar este comando!");
		}
		
		return true;
	}
}

