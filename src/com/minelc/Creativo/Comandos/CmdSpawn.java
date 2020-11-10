package com.minelc.Creativo.Comandos;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdSpawn implements CommandExecutor {

	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
		final Player p = (Player) s;
			p.sendMessage(ChatColor.GREEN+"Fuiste enviado al spawn");
			Location loc = new Location(p.getWorld(), -80.518, 71, -80.615);
			p.teleport(loc);
		
		return true;
	}
}