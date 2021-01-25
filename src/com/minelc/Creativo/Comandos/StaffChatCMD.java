package com.minelc.Creativo.Comandos;

import com.minelc.CORE.Controller.Jugador;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class StaffChatCMD implements CommandExecutor {

	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
		Player p = (Player) s;
		Jugador jug = Jugador.getJugador(p);

		if (!jug.is_AYUDANTE()) {
			p.sendMessage(ChatColor.RED+"Solo los moderadores pueden usar este comando!");
			return false;
		}

		if(args.length == 0) {
			p.sendMessage(ChatColor.YELLOW + "Debes escribir un mensaje.");
			return false;
		}

		String msg = getMsg(args);
		String rango = PlaceholderAPI.setPlaceholders(p, "%lc_rankcolors%");

		if (rango.equals("%lc_rankcolors%")) rango = "";

		for (Player pl : Bukkit.getOnlinePlayers()) {
			if(Jugador.getJugador(pl).is_AYUDANTE()) {
				pl.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "STAFF " + rango + ChatColor.WHITE + p.getName() + ChatColor.DARK_GRAY + " » " + ChatColor.YELLOW + msg);
			}
		}
		Bukkit.getLogger().info("[SC] " + p.getName() + ": " + msg);
		return true;
	}

	private String getMsg(String[] args) {
		StringBuilder ret = new StringBuilder();
		for (String arg : args) {
			ret.append(" ").append(arg);
		}
		return ret.toString();
	}
}

