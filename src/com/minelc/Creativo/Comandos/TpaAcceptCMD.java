package com.minelc.Creativo.Comandos;

import com.minelc.CORE.Controller.Jugador;
import com.minelc.Creativo.CreativolMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaAcceptCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("¡Sólo los jugadores pueden usar este commando!");
            return false;
        }

        Player jugador = (Player) commandSender;
        Jugador jug = Jugador.getJugador(jugador);

        if(CreativolMain.tpa.containsKey(jugador)) {
            if(CreativolMain.tpa.get(jugador) != null) {
                CreativolMain.tpa.get(jugador).teleport(jugador);
                CreativolMain.tpa.get(jugador).sendMessage(ChatColor.GOLD + "Petición de teletransporte aceptada.");
                jugador.sendMessage(ChatColor.GOLD + "Has aceptado la solicitud de "+ ChatColor.RED + CreativolMain.tpa.get(jugador).getName() + ChatColor.GOLD + ".");
                CreativolMain.tpa.remove(jugador);
            } else {
                jugador.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "Jugador no encontrado.");
            }
            return true;
        } else {
            jugador.sendMessage(ChatColor.RED+"Error: " + ChatColor.DARK_RED + "No tienes solicitudes pendientes.");
        }
        return false;
    }
}
