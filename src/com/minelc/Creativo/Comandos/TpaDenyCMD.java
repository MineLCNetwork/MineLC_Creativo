package com.minelc.Creativo.Comandos;

import com.minelc.CORE.Controller.Jugador;
import com.minelc.Creativo.CreativolMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaDenyCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("¡Sólo los jugadores pueden usar este commando!");
            return false;
        }

        Player jugador = (Player) commandSender;
        Jugador jug = Jugador.getJugador(jugador);

        if(CreativolMain.tpa.containsKey(jugador)) {
            if(CreativolMain.tpa.get(jugador) != null) jugador.sendMessage(ChatColor.GOLD + "Has denegado la solicitud de " + ChatColor.RED + CreativolMain.tpa.get(jugador).getName()+ChatColor.GOLD+".");
            CreativolMain.tpa.get(jugador).sendMessage(ChatColor.GOLD + "Han denegado tu solicitud.");
            CreativolMain.tpa.remove(jugador);
            return true;
        }
        else {
            jugador.sendMessage(ChatColor.RED+"Error: " + ChatColor.DARK_RED + "No tienes solicitudes pendientes.");
        }
        return true;
    }
}
