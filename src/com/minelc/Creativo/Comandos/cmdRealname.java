package com.minelc.Creativo.Comandos;

import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class 	cmdRealname implements CommandExecutor {

	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
		Player p = (Player) s;
		
		if(args.length == 0) {
			s.sendMessage(ChatColor.RED + "Utiliza: /realname (nick)");
		}  else {
			String nickpl = ChatColor.BLACK + "~"+args[0];
			String nickpl2 = ChatColor.stripColor(nickpl);
			String nick = cmdNick.getNick(cmdNick.nicks, nickpl2).toString().replaceAll("\\[", "").replaceAll("\\]", "");
			/*
			if (Bukkit.getPlayer(args[0]) != null) {

				return true;
			} */

			if (cmdNick.nicks.get(nick) == null) {
				p.sendMessage(ChatColor.DARK_RED + "Ese nick no existe.");
			} else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6El nick " + cmdNick.nicks.get(nick) + "&6 lo está usando " + nick));
			}

				/*
				if(cmdNick.getNick(cmdNick.nicks, nickpl2) == null) {
					p.sendMessage(ChatColor.DARK_RED + "Ese nick no existe.");
					
				} else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6El nombre de " + cmdNick.nicks.get(nick) + "&6 es: " + nick));
				} */
			
				
			
		}
		
		return true;
	}
}

