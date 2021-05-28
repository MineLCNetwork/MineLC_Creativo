package com.minelc.Creativo.Comandos;

import com.minelc.Creativo.CreativolMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Comando sólo para jugadores.");
            return false;
        }
        Player p = (Player) commandSender;
        if (args.length < 1) {
            p.sendMessage(ChatColor.YELLOW + "Escribe un mensaje!");
            return true;
        }
        if (CreativolMain.get().recentTell.containsKey(p)) {
            Player destinatario = CreativolMain.get().recentTell.get(p);
            if (destinatario == null || !destinatario.isOnline()) {
                p.sendMessage(ChatColor.RED + "No se pudo enviar el mensaje. El usuario se desconectó.");
                return true;
            } else {
                if (CreativolMain.get().recentTell.containsKey(destinatario)) {
                    CreativolMain.get().recentTell.remove(destinatario);
                }
                CreativolMain.get().recentTell.put(destinatario, p);
                String msg = getMsg(args);
                // p.sendMessage(ChatColor.GOLD + "[" +ChatColor.RED + "yo" + ChatColor.GOLD + " -> " + ChatColor.RED + destinatario.getName() + ChatColor.GOLD + "] " + ChatColor.WHITE + msg);
                // destinatario.sendMessage(ChatColor.GOLD + "[" +ChatColor.RED + p.getName() + ChatColor.GOLD + ChatColor.GOLD + " -> "+ChatColor.RED + "yo"  + ChatColor.GOLD + "] " + ChatColor.WHITE + msg);
                p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "[" +ChatColor.WHITE + "Yo" + ChatColor.DARK_GRAY + " -> " + ChatColor.WHITE + destinatario.getName() + ChatColor.AQUA + "" + ChatColor.BOLD + "] " + ChatColor.WHITE + msg);
                // r.sendMessage(ChatColor.GOLD + "[" +ChatColor.RED + p.getName() + ChatColor.GOLD + ChatColor.GOLD + " -> "+ChatColor.RED + "yo"  + ChatColor.GOLD + "] " + ChatColor.WHITE + msg);
                destinatario.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "[" +ChatColor.WHITE + p.getName() + ChatColor.DARK_GRAY + " -> "+ChatColor.WHITE + "Yo"  + ChatColor.AQUA + "" + ChatColor.BOLD + "] " + ChatColor.WHITE + msg);
            }
        } else {
            p.sendMessage(ChatColor.RED + "No se pudo enviar el mensaje. Nadie te ha escrito :(");
        }
        return false;
    }

    private String getMsg(String[] args) {
        StringBuilder ret = new StringBuilder();
        for (String arg : args) {
            ret.append(" ").append(arg);
        }
        return ret.toString();
    }
}
