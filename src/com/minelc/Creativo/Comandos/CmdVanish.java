package com.minelc.Creativo.Comandos;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.minelc.CORE.Controller.Jugador;
import com.minelc.Creativo.CreativolMain;


public class CmdVanish implements CommandExecutor {

	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
		Player p = (Player) s;
		Jugador jug = Jugador.getJugador(p);
		
		if(jug.is_MODERADOR()) {
		if(CreativolMain.get().isVanished(p)) {
			for(Player Online : Bukkit.getOnlinePlayers()) {
				Online.showPlayer(p);
			}
			CreativolMain.get().removeVanished(p);
			p.sendMessage("");
			p.sendMessage(ChatColor.DARK_PURPLE+""+ChatColor.BOLD+"MOD > "+ChatColor.YELLOW+"Invisibilidad"+ChatColor.GRAY+":"+ChatColor.RED+" Desactivada");
		} else {
			for(Player Online : Bukkit.getOnlinePlayers()) {
				Online.hidePlayer(p);
			}
			CreativolMain.get().addVanished(p);
			p.sendMessage("");
			p.sendMessage(ChatColor.DARK_PURPLE+""+ChatColor.BOLD+"MOD > "+ChatColor.YELLOW+"Invisibilidad"+ChatColor.GRAY+":"+ChatColor.GREEN+" Activada");
			p.sendMessage("");
		}
		} else {
			p.sendMessage(ChatColor.WHITE+"El comando no existe.");
		}
		
		return true;
	}
}