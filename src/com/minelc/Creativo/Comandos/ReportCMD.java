package com.minelc.Creativo.Comandos;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.minelc.CORE.Controller.Jugador;

 
public class ReportCMD implements CommandExecutor {
	
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    	Player p = (Player) sender;
    	if(args.length == 0) {
    		p.sendMessage(ChatColor.YELLOW+"¡Escribe un mensaje a los miembros del staff conectados!");
    	} else if(args.length == 1) {
    		p.sendMessage(ChatColor.YELLOW+"Por favor escribe un mensaje más detallado.");
    	} else {
    		String msg = getMsg(args);
    		for(Player Online : Bukkit.getOnlinePlayers()) {
    			if(Jugador.getJugador(Online).is_AYUDANTE()) {
    				Online.sendMessage(ChatColor.DARK_RED+"[Reporte] "+ChatColor.WHITE+p.getName()+": "+msg);
    			}
    		}
    		if(!Jugador.getJugador(p).is_AYUDANTE()) {
				p.sendMessage(ChatColor.GREEN + "Tu mensaje fue enviado a los miembros del staff conectados");
			}
    	}
        return true;
    }
    
    private String getMsg(String[] args) {
    	String ret = "";
    	for(int n = 0; n < args.length; n++) {
    		ret += " "+args[n];
    	}
    	return ret;
    }
}
