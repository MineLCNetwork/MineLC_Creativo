package com.minelc.Creativo.Comandos;

import com.google.common.collect.Maps;
import com.minelc.CORE.Controller.Jugador;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MemCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if(sender instanceof Player && !Jugador.getJugador((Player) sender).is_MODERADOR()) {
            sender.sendMessage("No permisos!");
            return true;
        }

        sender.sendMessage("Rem libre: "+Runtime.getRuntime().freeMemory()/1000000);
        sender.sendMessage("Rem maxima: "+Runtime.getRuntime().maxMemory()/1000000);
        sender.sendMessage("Rem asignada: "+Runtime.getRuntime().totalMemory()/1000000);
        sender.sendMessage("");
        sender.sendMessage("Mundos cargados:"+ Bukkit.getWorlds().size());
        for(World w : Bukkit.getWorlds()) {
            sender.sendMessage("Mundo: "+w.getName());
            sender.sendMessage("Chunks:"+w.getLoadedChunks().length);
            sender.sendMessage("Entidades:"+w.getEntities().size());
            HashMap<String, Integer> hm = Maps.newHashMap();
            if(w.getEntities().size() > 300) {
                sender.sendMessage("Eliminando entidades...");
                for(Entity ent : w.getEntities()) {
                    if(hm.containsKey(ent.getType().name())) {
                        hm.put(ent.getType().name(), 1);
                    } else {
                        hm.put(ent.getType().name(), hm.get(ent.getType().name()+1));
                    }
                    ent.remove();
                }
                StringBuilder msg = new StringBuilder();
                for(Map.Entry<String, Integer> e : hm.entrySet()) {
                    msg.append(e.getKey()).append(":").append(e.getValue()).append(" | ");
                }
                sender.sendMessage("Entidades:"+msg);
            } else {
                for(Entity ent : w.getEntities()) {
                    if(hm.containsKey(ent.getType().name())) {
                        hm.put(ent.getType().name(), 1);
                    } else {
                        hm.put(ent.getType().name(), hm.get(ent.getType().name()+1));
                    }
                }
                StringBuilder msg = new StringBuilder();
                for(Map.Entry<String, Integer> e : hm.entrySet()) {
                    msg.append(e.getKey()).append(":").append(e.getValue()).append(" | ");
                }
                sender.sendMessage("Entidades:"+msg);
            }
            sender.sendMessage("");
        }
        return false;
    }
}
