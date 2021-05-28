package com.minelc.Creativo.Comandos;

import com.minelc.Creativo.CreativolMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdTell implements CommandExecutor {
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        final Player p = (Player) s;
        if(args.length == 0) {
            p.sendMessage(ChatColor.YELLOW+"Envía un mensaje privado usando: /tell <jugador> <mensaje>");
        } else if(args.length == 1) {
            p.sendMessage(ChatColor.YELLOW+"¡Escribe un mensaje!");
        } else {
            Player r = Bukkit.getPlayer(args[0]);
            String msg = getMsg(args);
            if(r == null || !r.isOnline()) {
                p.sendMessage(ChatColor.RED + "El jugador "+args[0]+" no está conectado.");
            } else if (r == p) {
                p.sendMessage(ChatColor.RED + "No puedes enviarte un mensaje a ti mismo.");
            } else  {
                CreativolMain.get().recentTell.remove(r);
                CreativolMain.get().recentTell.put(r, p);
                // p.sendMessage(ChatColor.GOLD + "[" +ChatColor.RED + "yo" + ChatColor.GOLD + " -> " + ChatColor.RED + r.getName() + ChatColor.GOLD + "] " + ChatColor.WHITE + msg);
                p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "[" +ChatColor.WHITE + "Yo" + ChatColor.DARK_GRAY + " -> " + ChatColor.WHITE + r.getName() + ChatColor.AQUA + "" + ChatColor.BOLD + "] " + ChatColor.WHITE + msg);
                // r.sendMessage(ChatColor.GOLD + "[" +ChatColor.RED + p.getName() + ChatColor.GOLD + ChatColor.GOLD + " -> "+ChatColor.RED + "yo"  + ChatColor.GOLD + "] " + ChatColor.WHITE + msg);
                r.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "[" +ChatColor.WHITE + p.getName() + ChatColor.DARK_GRAY + " -> "+ChatColor.WHITE + "Yo"  + ChatColor.AQUA + "" + ChatColor.BOLD + "] " + ChatColor.WHITE + msg);
            }
        }

        return false;
    }

    private String getMsg(String[] args) {
        StringBuilder ret = new StringBuilder();
        for(int n = 1; n < args.length; n++) {
            ret.append(" ").append(args[n]);
        }
        return ret.toString();
    }
}
