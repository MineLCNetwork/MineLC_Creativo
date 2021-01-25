package com.minelc.Creativo.Comandos;

import com.minelc.Creativo.CreativolMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdSpawn implements CommandExecutor {

	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
		final Player p = (Player) s;

		if (p.getLocation().getWorld().getName().equals("vip")) {
			p.sendMessage(ChatColor.GREEN+"Fuiste enviado al spawn VIP");
			int x = CreativolMain.get().getIntConfig("ubicaciones.spawn.vip.x", 2007);
			int y = CreativolMain.get().getIntConfig("ubicaciones.spawn.vip.y", 162);
			int z = CreativolMain.get().getIntConfig("ubicaciones.spawn.vip.z", 2009);
			Location loc = new Location(p.getWorld(), x, y, z);
			p.teleport(loc);
		} else if (p.getLocation().getWorld().getName().equals("plots")) {
			p.sendMessage(ChatColor.GREEN+"Fuiste enviado al spawn de Parcelas");
			int x = CreativolMain.get().getIntConfig("ubicaciones.spawn.plots.x", 0);
			int y = CreativolMain.get().getIntConfig("ubicaciones.spawn.plots.y", 73);
			int z = CreativolMain.get().getIntConfig("ubicaciones.spawn.plots.z", 0);
			Location loc = new Location(p.getWorld(), x, y, z);
			p.teleport(loc);
		} else {
			p.sendMessage(ChatColor.RED + "¡No pudimos encontrar un spawn para este mundo!");
			p.sendMessage(ChatColor.GREEN+"Fuiste enviado al spawn de Parcelas.");
			int x = CreativolMain.get().getIntConfig("ubicaciones.spawn.plots.x", 0);
			int y = CreativolMain.get().getIntConfig("ubicaciones.spawn.plots.y", 73);
			int z = CreativolMain.get().getIntConfig("ubicaciones.spawn.plots.z", 0);
			Location loc = new Location(Bukkit.getWorld("plots"), x, y, z);
			p.teleport(loc);
		}

		return true;
	}
}