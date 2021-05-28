package com.minelc.Creativo.Comandos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.minelc.CORE.Controller.Jugador;

public class cmdNick implements CommandExecutor {
	public static Map<String, String> nicks = new HashMap<String, String>();
	
	
	public static <K, V> Set<K> getNick(Map<K, V> map, V value) {
	    Set<K> keys = new HashSet<>();
	    for (Entry<K, V> entry : map.entrySet()) {
	        if (ChatColor.stripColor(entry.getValue().toString()).contains(value.toString())) {
	            keys.add(entry.getKey());
	        }
	    }
	    return keys;
	}
	

	
	
	
	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
		Player p = (Player) s;
		Jugador jug = Jugador.getJugador(p);
		if(args.length == 0) {
			if(p.getName().equals("obed_007") || jug.is_AYUDANTE()|| jug.is_MODERADOR() || jug.is_Admin() || jug.is_Owner()) {
				s.sendMessage(ChatColor.GREEN + "Mod: /nick (nuevo nick)");
				s.sendMessage(ChatColor.GREEN + "Mod: /nick (nuevo nick) (jugador)");
			} else {
				s.sendMessage(ChatColor.GREEN+ "Usa: /nick (Nuevo Nick)");
			}
		} else if(args.length == 1) {
			if (p.hasPermission("minelc.creativo.nick")) {
			// if(p.getName().equals("obed_007") || jug.is_VIP()|| jug.is_BUILDER() || jug.is_ELITE() || jug.is_RUBY() || jug.is_SVIP() || jug.is_AYUDANTE()|| jug.is_MODERADOR() || jug.is_Admin() || jug.is_Owner()) {
				String checkregex = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', args[0]));
				String nok = args[0].replace("&k", "");
				String nick = ChatColor.BLACK+"~" + ChatColor.GRAY + "" +ChatColor.translateAlternateColorCodes('&', nok);
				String nnc = ChatColor.stripColor(nick);

				if (!Pattern.matches("^[0-9a-zA-Z\\u00C0-\\u00FF]*$", checkregex)) {
					p.sendMessage(ChatColor.RED + "Sólo se admiten nicks con carácteres españoles.");
					return true;
				}

				if(nnc.length() > 40 || nnc.length() < 3) {
					p.sendMessage(ChatColor.RED+"Comprueba el tamaño del nick!");
				} else if( args[0].equalsIgnoreCase("puto")||  args[0].equalsIgnoreCase("caca")||  args[0].equalsIgnoreCase("gay")||
						 args[0].equalsIgnoreCase("gey")|| args[0].equalsIgnoreCase("marico")|| args[0].equalsIgnoreCase("perra")||
						 args[0].equalsIgnoreCase("mierda")|| args[0].equalsIgnoreCase("putito")|| args[0].equalsIgnoreCase("zorra")||
						 args[0].equalsIgnoreCase("pendejo")|| args[0].equalsIgnoreCase("culo")|| args[0].equalsIgnoreCase("pene")|| args[0].equalsIgnoreCase("sexo")||
						 args[0].equalsIgnoreCase("vagina")|| args[0].equalsIgnoreCase("concha")|| args[0].equalsIgnoreCase("chucha") || args[0].contains("%")) {
					s.sendMessage(ChatColor.RED + "Nick no permitido.");
				}
				else {
					if(!nicks.containsValue(nick)) {
						if(nicks.containsKey(p.getName())) {
							
							nicks.remove(p.getName(), nicks.get(p.getName()));
						}
						p.setDisplayName(nick);
						p.sendMessage(ChatColor.GREEN+"Tu nick fue cambiado a "+ nick);
						nicks.put(p.getName(), nick);
					} else {
						p.sendMessage(ChatColor.RED+"Nick en uso!");
					}
				}
			} else {
				s.sendMessage(ChatColor.RED + "Para usar este comando necesitas tener rango " +ChatColor.GREEN + "VIP");
				s.sendMessage(ChatColor.GOLD + "Compra rangos en: www.minelc.net/shop");
			}
		
		} else if(args.length == 2) {
			if(jug.is_Admin() || jug.is_MODERADOR()||jug.is_AYUDANTE()|| jug.is_Owner() || s.isOp()) {
				Player pn = Bukkit.getPlayer(args[1]);
				if(pn != null && pn.isOnline()) {
					String nick = ChatColor.BLACK+"~" + ChatColor.GRAY + "" +ChatColor.translateAlternateColorCodes('&', args[0]);
					String nnc = ChatColor.stripColor(nick);
					if(nnc.length() > 40 || nnc.length() < 3) {
						p.sendMessage(ChatColor.RED+"Comprueba el tamaño del nick!");
					} else {
						if(!nicks.containsValue(nick)) {
							if(nicks.containsKey(pn.getName())) {
								nicks.remove(pn.getName(), nicks.get(p.getName()));
							}
							pn.setDisplayName(nick);
							p.sendMessage(ChatColor.GREEN+"El nick de "+ChatColor.YELLOW+args[1]+ChatColor.GREEN+" fue cambiado a: "+nick);
							nicks.put(pn.getName(), nick);
						} else {
							p.sendMessage(ChatColor.RED+"Nick en uso!");
						}
					}
				} else {
					p.sendMessage(ChatColor.RED+"Usuario no conectado");
				}
				
				
			} else {
				s.sendMessage(ChatColor.RED + "No tienes permisos para usar ese comando.");
			}
			
			
			
		}
		
		
		
		return true;
	}
}

