package com.minelc.Creativo.Comandos;

import com.minelc.CORE.Controller.Jugador;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpHereCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("¡Sólo los jugadores pueden usar este commando!");
            return false;
        }

        Player jugador = (Player) commandSender;
        Jugador jug = Jugador.getJugador(jugador);

        if (!jug.is_MODERADOR()) {
            jugador.sendMessage(ChatColor.RED + "No tienes permisos para ejecutar este comando.");
            return true;
        }

        if (args.length == 0) {
            jugador.sendMessage(ChatColor.RED + "Usa /tphere (jugador) para traer un jugador a tu posición.");
            return true;
        }

        if (Bukkit.getServer().getPlayer(args[0]) != null) {
            Player targetPlayer = jugador.getServer().getPlayer(args[0]);
            if (targetPlayer.getName().equalsIgnoreCase(jugador.getName())) {
                jugador.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "No puedes hacer esto en ti mismo.");
                return true;
            } else {
                jugador.sendMessage(ChatColor.RED + "Has traido al jugador " + targetPlayer.getName() + " a tu posición.");
                targetPlayer.sendMessage(ChatColor.RED + "Has sido transportado hacia " + jugador.getName());
                targetPlayer.teleport(jugador);
            }
        } else {
            jugador.sendMessage(ChatColor.DARK_RED + "Jugador no encontrado.");
        }
        return true;
    }
}
