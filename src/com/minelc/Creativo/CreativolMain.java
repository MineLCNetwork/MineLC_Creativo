package com.minelc.Creativo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import com.minelc.Creativo.Comandos.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.minelc.CORE.Controller.Jugador;
import com.minelc.Creativo.listeners.PlayerListener;
import com.minelc.Creativo.runnables.Timer;

public class CreativolMain extends JavaPlugin implements CommandExecutor{

	private static CreativolMain instance;
	public static Set<Player> Vanished = Sets.newHashSet();
	public HashMap<Player, Player> tpa = new HashMap<Player, Player>();
    public void onEnable() {
    	try {
        	instance = this;
        	
            Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
            
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Timer(), 20L, 20L);

            this.saveDefaultConfig();
            // this.loadConfigInUTF();
            // this.saveConfig();
            this.loadConfigInUTF();

            getCommand("nick").setExecutor(new cmdNick());            
            getCommand("fly").setExecutor(new CmdFly());
            getCommand("gamemode").setExecutor(new CmdGameMode());
            getCommand("teleport").setExecutor(new CmdTeleport());
            getCommand("vanish").setExecutor(new CmdVanish());
            getCommand("teleportworld").setExecutor(new CmdTeleportWorld());
            getCommand("spawn").setExecutor(new CmdSpawn());
            getCommand("realname").setExecutor(new cmdRealname());
           // getCommand("tell").setExecutor(new TellCmd());
            getCommand("report").setExecutor(new ReportCMD());
            getCommand("near").setExecutor(new cmdNear());
            // getCommand("sit").setExecutor(new Sit());
            getCommand("tell").setExecutor(new CmdTell());
            getCommand("sc").setExecutor(new StaffChatCMD());
            getCommand("mem").setExecutor(new CommandExecutor() {
    			
    			@Override
    			public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
    				if(sender instanceof Player && !Jugador.getJugador((Player) sender).is_MODERADOR()) {
    					sender.sendMessage("No permisos!");
    					return true;
    				}
    				sender.sendMessage("Rem libre: "+Runtime.getRuntime().freeMemory()/1000000);
    				sender.sendMessage("Rem maxima: "+Runtime.getRuntime().maxMemory()/1000000);
    				sender.sendMessage("Rem asignada: "+Runtime.getRuntime().totalMemory()/1000000);
    				sender.sendMessage("");
    				sender.sendMessage("Mundos cargados:"+Bukkit.getWorlds().size());
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
    						String msg = "";
    						for(Entry<String, Integer> e : hm.entrySet()) {
    						msg = msg+e.getKey()+":"+e.getValue()+" | ";
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
    						String msg = "";
    						for(Entry<String, Integer> e : hm.entrySet()) {
    						msg = msg+e.getKey()+":"+e.getValue()+" | ";
    						}
    						sender.sendMessage("Entidades:"+msg);
    					}
    					sender.sendMessage("");
    				}
    				return true;
    			}
    		});
        	} catch(Exception ex) {
    		Bukkit.shutdown();
    		ex.printStackTrace();
    	}
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		 Player jugador = (Player) sender;
		 Jugador jug = Jugador.getJugador(jugador);
		 if(cmd.getName().equalsIgnoreCase("tpa"))
		 {
			 if(args.length ==0)
			 {
				 jugador.sendMessage(ChatColor.RED + "Usa /tpa (jugador) para enviar una solicitud de teletransporte.");
				 
				 return true;
				 
			 }if(args.length==1)
			 {
				 if(getServer().getPlayer(args[0]) != null) {
					 Player targetPlayer = jugador.getServer().getPlayer(args[0]);
					 if(targetPlayer.getName().equalsIgnoreCase(jugador.getName()))
					 {
						 jugador.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "Es que ya estás aquí, ¿para qué quieres ir contigo mismo?");
					 }
					 else
					 {
						 if(tpa.get(targetPlayer) !=null)
						 {
							 jugador.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "Ya ha sido enviada una solicitud a este jugador.");
						 }
						 else
						 {						
							 tpa.put(targetPlayer, jugador);
							 if(jugador.getDisplayName() !=null)
							 {
								 jugador.sendMessage(ChatColor.GOLD + "La petición ha sido enviada a " +ChatColor.RED + targetPlayer.getDisplayName());
							 }else {
								 jugador.sendMessage(ChatColor.GOLD + "La petición ha sido enviada a " + ChatColor.RED + targetPlayer.getName());
							 }
							 
							 if(targetPlayer.getDisplayName() !=null)
							 {
								 targetPlayer.sendMessage(ChatColor.RED + jugador.getDisplayName()+ ChatColor.GOLD +" te ha pedido teletransportarse hasta ti."
					                	 + "\n" +"Escribe"+ChatColor.RED+" /tpaccept" +ChatColor.GOLD + " para aceptar el teletransporte." + "\n" + "Para denegar la teletransportaci�n, escribe " +ChatColor.RED+ "/tpdeny.");
										 return true;
			                 }else
							 {
			                	 targetPlayer.sendMessage(ChatColor.RED + jugador.getName()+ ChatColor.GOLD +" te ha pedido teletransportarse hasta ti."
			                	 + "\n" +"Escribe"+ChatColor.RED+" /tpaccept" +ChatColor.GOLD + " para aceptar el teletransporte." + "\n" + "Para denegar la teletransportaci�n, escribe " +ChatColor.RED+ "/tpdeny.");
								 return true;

							 }
					 }
					 
						 
					 }
					 
				 }else {
					 jugador.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "Jugador no encontrado.");
					 return true;
				 }
				 
			 }
			 if(args.length >1)
			 {
               jugador.sendMessage(ChatColor.RED + "Usa /tpa (jugador) para enviar una solicitud de teletransporte.");
				 
				 return true;
			 }
		 }if(cmd.getName().equalsIgnoreCase("tpaccept"))
		 {
			 if(tpa.containsKey(jugador))
			 {
				 if(tpa.get(jugador) != null)
				 {
					tpa.get(jugador).teleport(jugador);
					tpa.get(jugador).sendMessage(ChatColor.GOLD + "Petición de teletransporte aceptada.");
					jugador.sendMessage(ChatColor.GOLD + "Has aceptado la solicitud de "+ ChatColor.RED + tpa.get(jugador).getName() + ChatColor.GOLD + ".");
					tpa.remove(jugador);
					return true;
				 }else
				 {
					 jugador.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "Jugador no encontrado.");
					 return true;
				 }
			 }else
			 {
				 jugador.sendMessage(ChatColor.RED+"Error: " + ChatColor.DARK_RED + "No tienes solicitudes pendientes.");
			 }
			 
		 }if(cmd.getName().equalsIgnoreCase("tpdeny")){
			 if(tpa.containsKey(jugador))
			 {
				 if(tpa.get(jugador) != null)
				
					
					 jugador.sendMessage(ChatColor.GOLD + "Has denegado la solicitud de " + ChatColor.RED + tpa.get(jugador).getName()+ChatColor.GOLD+".");
					 tpa.get(jugador).sendMessage(ChatColor.GOLD + "Han denegado tu solicitud.");
					 tpa.remove(jugador);
					 return true;
				
			 }
			 else {
				 jugador.sendMessage(ChatColor.RED+"Error: " + ChatColor.DARK_RED + "No tienes solicitudes pendientes.");
			 }
			 
			 
		 
		 }if(cmd.getName().equalsIgnoreCase("s") || cmd.getName().equalsIgnoreCase("tphere"))
		 {
			 if(jug.is_MODERADOR())
			 {
				 if(args.length ==0)
				 {
					 jugador.sendMessage(ChatColor.RED + "Usa /tphere (jugador) para traer un jugador a tu posici�n.");
					 
					 return true;
					 
				 }if(args.length==1)
				 {
					 if(getServer().getPlayer(args[0]) != null) {
						 Player targetPlayer = jugador.getServer().getPlayer(args[0]);
						 if(targetPlayer.getName().equalsIgnoreCase(jugador.getName()))
						 {
							 jugador.sendMessage(ChatColor.RED + "Error: " + ChatColor.DARK_RED + "No puedes hacer esto en ti mismo.");
							 return true;
						 }
						 else
						 {
							 jugador.sendMessage(ChatColor.RED + "Has traido al jugador "+ targetPlayer.getName() + " a tu posición.");
							 targetPlayer.sendMessage(ChatColor.RED + "Has sido transportado hacia "+ jugador.getName());
							 targetPlayer.teleport(jugador);
						 }
			      }else {
			    	  jugador.sendMessage(ChatColor.DARK_RED + "Jugador no encontrado.");
			      }
			 }
				 return true;
			 }else {
				 jugador.sendMessage(ChatColor.RED + "No tienes permisos para ejecutar este comando.");
			 }
			 
		
		 }
		return false;
	 }
	public void onDisable() {
		for(Player Online : Bukkit.getOnlinePlayers()) {
			Online.kickPlayer("");
		}
    }
    
    public static CreativolMain get() {
        return instance;
    }

    
	public boolean isVanished(Player p) {
		return Vanished.contains(p);
	}
	
	public void removeVanished(Player p) {
		if(Vanished.contains(p)) {
			Vanished.remove(p);
		}
	}
	
	public void addVanished(Player p) {
		if(!Vanished.contains(p)) {
			Vanished.add(p);
		}
	}

    public void loadConfigInUTF() {
        File configFile = new File(this.getDataFolder(), "config.yml");
        if(!configFile.exists()) {
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile), "UTF-8"));
            this.getConfig().load(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getIntConfig(String key, int defaultInt) {
        FileConfiguration config = this.getConfig();
        if(config.contains(key)) {
            if(config.isInt(key)) {
                return config.getInt(key);
            }
        }

        return defaultInt;
    }

    public String getStringConfig(String key, String defaultString) {
        FileConfiguration config = this.getConfig();
        if(config.contains(key)) {
            if(config.isString(key)) {
                return config.getString(key);
            }
        }

        return defaultString;
    }

    public boolean getBooleanConfig(String key, boolean defaultBool) {
        FileConfiguration config = this.getConfig();
        if(config.contains(key)) {
            if(config.isBoolean(key)) {
                return config.getBoolean(key);
            }
        }

        return defaultBool;
    }
}


