package com.minelc.Creativo.Comandos;

import com.minelc.CORE.Controller.Jugador;
import com.minelc.Creativo.CreativolMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("¡Sólo los jugadores pueden usar este commando!");
            return false;
        }

        Player jugador = (Player) commandSender;
        Jugador jug = Jugador.getJugador(jugador);

        if (args.length == 0) {
            jugador.sendMessage(ChatColor.RED + "Usa /tpa (jugador) para enviar una solicitud de teletransporte.");
            return true;
        }

        if (args.length==1) {
            if(Bukkit.getServer().getPlayer(args[0]) != null) {
                Player targetPlayer = jugador.getServer().getPlayer(args[0]);
                if (targetPlayer.getName().equalsIgnoreCase(jugador.getName())) {
                    jugador.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "Es que ya estás aquí, ¿para qué quieres ir contigo mismo?");
                    return true;
                } else {
                    if(CreativolMain.tpa.get(targetPlayer) !=null) {
                        jugador.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "Ya ha sido enviada una solicitud a este jugador.");
                    } else {
                        CreativolMain.tpa.put(targetPlayer, jugador);
                        jugador.sendMessage(ChatColor.GOLD + "La petición ha sido enviada a " + ChatColor.RED + targetPlayer.getName());
                        targetPlayer.sendMessage(ChatColor.RED + jugador.getName()+ ChatColor.GOLD +" te ha pedido teletransportarse hasta ti."
                                + "\n" +"Escribe"+ChatColor.RED+" /tpaccept" +ChatColor.GOLD + " para aceptar el teletransporte." + "\n" + "Para denegar la teletransportaci�n, escribe " +ChatColor.RED+ "/tpdeny.");
                        return true;
                    }
                }
            } else {
                jugador.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "Jugador no encontrado.");
                return true;
            }
        }
        if(args.length > 1) {
            jugador.sendMessage(ChatColor.RED + "Usa /tpa (jugador) para enviar una solicitud de teletransporte.");
            return true;
        }
        return false;
    }
}
