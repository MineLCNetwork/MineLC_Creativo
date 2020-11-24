package com.minelc.Creativo.listeners;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import com.github.intellectualsites.plotsquared.bukkit.events.PlayerEnterPlotEvent;
import com.github.intellectualsites.plotsquared.bukkit.events.PlayerLeavePlotEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.map.MapRenderer;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.scoreboard.*;

import com.google.common.collect.Maps;
import com.minelc.CORE.CoreMain;
import com.minelc.CORE.Controller.Database;
import com.minelc.CORE.Controller.Jugador;
import com.minelc.CORE.Controller.Ranks;
import com.minelc.CORE.Utils.Util;
import com.minelc.Creativo.CreativolMain;
import com.minelc.Creativo.Comandos.cmdNick;


public class PlayerListener implements Listener {
	public static Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
	public static Map<Block, Long> redstonecd = Maps.newHashMap(); //clear
	public static Map<Block, Integer> redstonemaxvl = Maps.newHashMap(); //clear
	public static Map<Player, Long> interact_cd = Maps.newHashMap();
	public static int Tiempo = 60;
	public static int taskID;

	public static void resetRedstone() {
		redstonecd = Maps.newHashMap();
		redstonemaxvl = Maps.newHashMap();
	}
    @EventHandler
    public void shiftEvent(PlayerToggleSneakEvent e) {
    	for(Entity ent : e.getPlayer().getNearbyEntities(0.5, 0.5, 0.5)) {
    		if(ent.getCustomName() == "sit") {
    			ent.remove();
    		}
    	}
    }
    
	@EventHandler
	  public void onPreCommand(PlayerCommandPreprocessEvent event)
	  {
		String msg = event.getMessage().toLowerCase();
			String[] args = msg.split("\\s+");
	    if ((msg.startsWith("/worldedit:/calc")) || 
	      (msg.startsWith("/worldedit:/eval")) || 
	      (msg.startsWith("/worldedit:/solve")) || 
	      (msg.startsWith("//calc")) || 
	      (msg.startsWith("//eval")) || 
	      // (msg.startsWith("/me")) ||
	      (msg.startsWith("/minecraft:me")) || 
	      (msg.startsWith("//solve")))
	    {
	      event.setCancelled(true);
	      Player player = event.getPlayer();
	      player.sendMessage(ChatColor.WHITE+"El comando no existe.");
	      Bukkit.getLogger().log(Level.INFO, "AntiCrash: " +player.getName()+" intentó usar "+ msg);
	    }

	    if((msg.startsWith("/2")) || (msg.startsWith("/plots")) || (msg.startsWith("/ps")) || (msg.startsWith("/plotme")) || (msg.startsWith("/p2"))) {
	    	event.setCancelled(true);
	    	Player player = event.getPlayer();
	    	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6P2&8] &cPor favor inicie sus comandos con \"/p\" en vez de " + args[0]));
			Bukkit.getLogger().log(Level.INFO, "AntiGrief: " +player.getName()+" intentó usar el prefijo de P2 <<" + args[0] + ">> con el comando " + msg);
		}

	    if (msg.startsWith("/p set border") || (msg.startsWith("/plot set border"))) {
	    	if(event.getPlayer().hasPermission("svip.general")) {
				if (!args[3].startsWith("44:")) {
					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6P2&8] &cEl bloque que estás introduciendo no está permitido."));
					Bukkit.getLogger().info("AntiGrief: " + event.getPlayer() + " intento colocar un bloque no permitido (" + args[3] + ") ");
				} else {
					event.setCancelled(false);
					Bukkit.getLogger().info("AntiGrief: " + event.getPlayer() + " colocó exitosamente su borde de parcela con " + args[3]);
				}
			} else {
	    		event.setCancelled(true);
	    		event.getPlayer().sendMessage(ChatColor.RED+"Este comando solo pueden usarlo SVIP en adelante");
			}
		}
	  }

	@EventHandler
	public void onSignPlace(SignChangeEvent e) {
		int line = 0;
		for(String s : e.getLines()) {
			e.setLine(line, ChatColor.translateAlternateColorCodes('&', s));
			line++;
		}
	}
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		if(e.getResult() == Result.KICK_FULL) {
			if(Jugador.getJugador(e.getPlayer().getName()).is_VIP()) {
				e.allow();
			}
		}
	}
	
/*
	@EventHandler
	public void onLogin(PlayerCommandPreprocessEvent e) {
		if(e.getMessage().startsWith("msg") || e.getMessage().startsWith("tell")) {
			e.setCancelled(true);
			String[] args = convertirArreglo(e.getEventName());
			Player p = e.getPlayer();
			if(args.length == 0) {
	    		p.sendMessage(ChatColor.YELLOW+"Envia un mensaje usando: /tell <jugador> <mensaje>!");
	    	} else if(args.length == 1) {
	    		p.sendMessage(ChatColor.YELLOW+"Escribe un mensaje!");
	    	} else {
	    		Player r = Bukkit.getPlayer(args[0]);
	    		String msg = getMsg(args);
	    		if(r == null || !r.isOnline()) {
	    			p.sendMessage(ChatColor.YELLOW+"El jugador "+args[0]+" no esta conectado!");
	    		} else {
	    			p.sendMessage(ChatColor.GOLD + "[" +ChatColor.RED + "yo" + ChatColor.GOLD + " -> " + ChatColor.RED + r.getName() + ChatColor.GOLD + "] " + ChatColor.WHITE + msg);
	    			r.sendMessage(ChatColor.GOLD + "[" +ChatColor.RED + p.getName() + ChatColor.GOLD + ChatColor.GOLD + " -> "+ChatColor.RED + "yo"  + "] " + ChatColor.WHITE + msg);
	    		}
	    	}
			
		}
	}
	 private String getMsg(String[] args) {
	    	String ret = "";
	    	for(int n = 1; n < args.length; n++) {
	    		ret += " "+args[n];
	    	}
	    	return ret;
	    }
	public String[] convertirArreglo(String msg) {
		String[] nuevo = msg.split(" ");
		return nuevo;
	} */
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		if(e.getPlayer().getGameMode() == GameMode.CREATIVE) {
			if(!e.getPlayer().getAllowFlight()) {
				e.getPlayer().setAllowFlight(true);
			}
		}
	}
	
	@EventHandler
	public void onPiston(BlockPistonExtendEvent e) {
		if(e.isSticky() || e.getDirection() == BlockFace.UP) {
			if(redstonecd.containsKey(e.getBlock()) && redstonecd.get(e.getBlock()) > System.currentTimeMillis()) {
				e.setCancelled(true);
				if(redstonemaxvl.containsKey(e.getBlock())) {
					if(redstonemaxvl.get(e.getBlock()) > 100) {
						redstonemaxvl.remove(e.getBlock());
						redstonecd.remove(e.getBlock());
						e.getBlock().setType(Material.AIR);
						return;
					} else {
						redstonemaxvl.put(e.getBlock(), redstonemaxvl.get(e.getBlock())+1);
					}
				} else {
					redstonemaxvl.put(e.getBlock(), 1);
				}
			} else if(redstonemaxvl.containsKey(e.getBlock())) {
				redstonemaxvl.remove(e.getBlock());
			}
			
			redstonecd.put(e.getBlock(), System.currentTimeMillis()+500);
		}
	}
	
	@EventHandler
	public void onRedStone(BlockRedstoneEvent e) {
		if(redstonecd.containsKey(e.getBlock()) && redstonecd.get(e.getBlock()) > System.currentTimeMillis()) {
			if(redstonemaxvl.containsKey(e.getBlock())) {
				if(redstonemaxvl.get(e.getBlock()) > 100) {
					//redstonemaxvl.remove(e.getBlock());
					//redstonecd.remove(e.getBlock());
					e.setNewCurrent(0);
					//e.getBlock().setType(Material.AIR);
					return;
				} else {
					redstonemaxvl.put(e.getBlock(), redstonemaxvl.get(e.getBlock())+1);
				}
			} else {
				redstonemaxvl.put(e.getBlock(), 1);
			}
		}
		
		redstonecd.put(e.getBlock(), System.currentTimeMillis()+500);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onFiewWork(ProjectileLaunchEvent e) {
		if(e.getEntityType() == EntityType.FIREWORK || e.getEntityType() == EntityType.ENDER_SIGNAL) {
			e.setCancelled(true);
			if(!e.getEntity().isDead()) {
				e.getEntity().remove();
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.PHYSICAL) return;
		 Material type;
		 
		 if(e.getItem() == null) {
			 if(e.getHand() == EquipmentSlot.OFF_HAND) {
				 type = e.getPlayer().getInventory().getItemInOffHand().getType();
			 } else {
				 type = e.getPlayer().getInventory().getItemInMainHand().getType();
			 }
		 } else {
			 type = e.getItem().getType();
		 }
		 
		if(type == Material.ENDER_EYE) {
			e.setCancelled(true);
		} else if(type == Material.LEGACY_EMPTY_MAP) {
			e.setCancelled(true);
			if(e.getHand() == EquipmentSlot.OFF_HAND) {
				e.getPlayer().getInventory().setItemInOffHand(null);
			} else {
				e.getPlayer().getInventory().setItemInMainHand(null);
			}
		} else if(type == Material.MAP) {
			e.setCancelled(true);
			if(e.getHand() == EquipmentSlot.OFF_HAND) {
				e.getPlayer().getInventory().setItemInOffHand(null);
			} else {
				e.getPlayer().getInventory().setItemInMainHand(null);
			}
		} else if(type.name().contains("MINECART")) {
			if(interact_cd.containsKey(e.getPlayer())) {
				long current = System.currentTimeMillis();
				long cd = interact_cd.get(e.getPlayer());
				
				if(current - cd > 60000) {
					interact_cd.put(e.getPlayer(), System.currentTimeMillis());
				} else {
					e.getPlayer().sendMessage("Espera 1 minuto antes de colocar este bloque!");
					e.setCancelled(true);
				}
			} else {
				interact_cd.put(e.getPlayer(), System.currentTimeMillis());
			}
		} else if(type.name().contains("BOAT")) {
			if(interact_cd.containsKey(e.getPlayer())) {
				long current = System.currentTimeMillis();
				long cd = interact_cd.get(e.getPlayer());
				
				if(current - cd > 60000) {
					interact_cd.put(e.getPlayer(), System.currentTimeMillis());
				} else {
					e.getPlayer().sendMessage("Espera 1 minuto antes de colocar este bloque!");
					e.setCancelled(true);
				}
			} else {
				interact_cd.put(e.getPlayer(), System.currentTimeMillis());
			}
		}
	}
	
	@EventHandler
	public void onTNT(BlockPlaceEvent e) {
		if(e.getBlockPlaced().getType() == Material.TNT) {
			e.setCancelled(true);
		}
	}

	@EventHandler
    public void onMap(MapInitializeEvent e) {
		for(MapRenderer r : e.getMap().getRenderers()) {
			e.getMap().removeRenderer(r);
		}
    }
	
	@EventHandler
	public void onLogin(AsyncPlayerPreLoginEvent e) {
		Database.loadPlayerRank_SYNC(Jugador.getJugador(e.getName()));
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent e) {
		e.setJoinMessage(null);
		Jugador jug = Jugador.getJugador(e.getPlayer());
		jug.setBukkitPlayer(e.getPlayer());
		e.getPlayer().setGameMode(GameMode.CREATIVE);
		if(e.getPlayer().getWorld() != Bukkit.getWorld("plots")) { // Ahora le puse de nombre "plots", no "parcelas".
			e.getPlayer().teleport(Bukkit.getWorld("plots").getSpawnLocation());
		}
		addPermissionDefault(e.getPlayer());

		if(e.getPlayer().getLocation().getBlock().getType() == Material.NETHER_PORTAL || e.getPlayer().getLocation().getBlock().getType() == Material.END_PORTAL_FRAME ||
				e.getPlayer().getLocation().clone().add(0,1,0).getBlock().getType() == Material.NETHER_PORTAL || e.getPlayer().getLocation().clone().add(0,1,0).getBlock().getType() == Material.END_PORTAL_FRAME
				|| e.getPlayer().getLocation().clone().add(0,-1,0).getBlock().getType() == Material.NETHER_PORTAL || e.getPlayer().getLocation().clone().add(0,-1,0).getBlock().getType() == Material.END_PORTAL_FRAME) {
			e.getPlayer().teleport(Bukkit.getWorld("plots").getSpawnLocation());
		}
		
		for(Player vanished : CreativolMain.Vanished) {
			e.getPlayer().hidePlayer(vanished);
		}

		for (Player all : Bukkit.getOnlinePlayers()) {
			Jugador jugall = Jugador.getJugador(all);
			setScoreboard(jugall);
		}

		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CreativolMain.get(), new Runnable() {
			@Override
			public void run() {
				onJoin(e.getPlayer());
				Bukkit.getLogger().info("[Debug] Se ha cargado el usuario " + e.getPlayer().getName() + " (onJoin).");
			}
		}, 10L);
	}
	
	private void bucle(Player p4) {
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		taskID = sh.scheduleSyncRepeatingTask(CreativolMain.get(), new Runnable() {
			public void run() {
				if(Tiempo == 0) {
					int a = (int) Math.round((Math.random() * ( 5 - 1 )) + 1);
					if (a == 3) {
						p4.kickPlayer("Disconnected");
					}
				Tiempo = 30;
				} else {
					Tiempo--;
				}
			}
		}, 0L,20);
		
	}

	private String formatoScoreboard(String str, Jugador jug) {
		Player p = jug.getBukkitPlayer();

		if(p.getLocation().getWorld().getName().equals("vip")) {
			str = str.replace("$mundo$", CreativolMain.get().getStringConfig("vip-world", "&b&lVIP"));
		} else if(p.getLocation().getWorld().getName().equals("plots")) {
			str = str.replace("$mundo$", CreativolMain.get().getStringConfig("normal-world", "&cParcelas"));
		} else {
			str = str.replace("$mundo$", CreativolMain.get().getStringConfig("unknown-world", "&9&oNarnia"));
		}

		if(PlaceholderAPI.setPlaceholders(p.getPlayer(), "%plotsquared_currentplot_owner%").isEmpty()) {
			str = str.replace("$current-plot-formatted$", CreativolMain.get().getStringConfig("world", "&6Ninguna"));
		} else {
			str = str.replace("$current-plot-formatted$", "&7(&6%plotsquared_currentplot_x%;%plotsquared_currentplot_y%&7) &6%plotsquared_currentplot_owner%");
		}

		str = str.replace("$online$", Bukkit.getServer().getOnlinePlayers().size() + "");
		str = str.replace("$maxuser$", Bukkit.getServer().getMaxPlayers() + "");

		return PlaceholderAPI.setPlaceholders(p.getPlayer(), ChatColor.translateAlternateColorCodes('&', str));
	}

	public void setScoreboard(Jugador jugOnline) {
		Player Online = jugOnline.getBukkitPlayer();

		Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();
		// jugOnline.getBukkitPlayer().setScoreboard(sb);

		Objective objGame = sb.getObjective("Creativo");

		if(objGame == null) {
			objGame = sb.registerNewObjective("Creativo", "dummy");

			objGame.setDisplaySlot(DisplaySlot.SIDEBAR);

			objGame.setDisplayName(ChatColor.AQUA+""+ChatColor.BOLD+"Creativo");

			List<String> rows = CreativolMain.get().getConfig().getStringList("mensajes.scoreboard");
			int x = rows.size();

			for (String row : rows) {
				objGame.getScore(formatoScoreboard(row, jugOnline)).setScore(x);
				x--;
			}
		}



		for(Player tmOnline : Bukkit.getOnlinePlayers()) {
			Jugador jugTM = Jugador.getJugador(tmOnline);

			try {
				Team tm = sb.getTeam(jugTM.getBukkitPlayer().getName());

				if(tm != null) {
					continue;
				}

				tm = sb.registerNewTeam(jugTM.getBukkitPlayer().getName());

				if (jugTM.isHideRank()) {
					tm.setPrefix(ChatColor.GRAY + "");
					tm.setColor(ChatColor.GRAY);
				} else if (jugTM.is_Owner()) {
					tm.setPrefix(ChatColor.DARK_RED+""+ChatColor.BOLD+Ranks.OWNER.name()+" "+jugTM.getNameTagColor());
					tm.setColor(jugTM.getNameTagColor());
				} else if (isAdmod(tmOnline)) {
					tm.setPrefix(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "AD" + ChatColor.WHITE + "" + ChatColor.BOLD + Ranks.MOD.name() + " " + jugTM.getNameTagColor());
					tm.setColor(jugTM.getNameTagColor());
				} else if (jugTM.is_Admin()) {
					tm.setPrefix(ChatColor.RED + "" + ChatColor.BOLD + Ranks.ADMIN.name() + " " + jugTM.getNameTagColor());
					tm.setColor(jugTM.getNameTagColor());
				} else if (jugTM.is_MODERADOR()) {
					tm.setPrefix(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + Ranks.MOD.name() + " " + jugTM.getNameTagColor());
					tm.setColor(jugTM.getNameTagColor());
				} else if (jugTM.is_AYUDANTE()) {
					tm.setPrefix(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + Ranks.AYUDANTE.name() + " " + jugTM.getNameTagColor());
					tm.setColor(jugTM.getNameTagColor());
				} else if (jugTM.is_YOUTUBER()) {
					tm.setPrefix(ChatColor.RED + "" + ChatColor.BOLD + "YouTuber " + jugTM.getNameTagColor());
					tm.setColor(jugTM.getNameTagColor());
				} else if (jugTM.is_BUILDER()) {
					tm.setPrefix(ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+Ranks.BUILDER.name()+" "+jugTM.getNameTagColor());
					tm.setColor(jugTM.getNameTagColor());
				} else if (jugTM.is_RUBY()) {
					tm.setPrefix(ChatColor.RED + "" + ChatColor.BOLD + Ranks.RUBY.name() + " " + jugTM.getNameTagColor());
					tm.setColor(jugTM.getNameTagColor());
				} else if (jugTM.is_ELITE()) {
					tm.setPrefix(ChatColor.GOLD + "" + ChatColor.BOLD + Ranks.ELITE.name() + " " + jugTM.getNameTagColor());
					tm.setColor(jugTM.getNameTagColor());
				} else if (jugTM.is_SVIP()) {
					tm.setPrefix(ChatColor.GREEN + "" + ChatColor.BOLD + Ranks.SVIP.name() + " " + jugTM.getNameTagColor());
					tm.setColor(jugTM.getNameTagColor());
				} else if (jugTM.is_VIP()) {
					tm.setPrefix(ChatColor.AQUA + "" + ChatColor.BOLD + Ranks.VIP.name() + " " + jugTM.getNameTagColor());
					tm.setColor(jugTM.getNameTagColor());
				} else if (jugTM.is_Premium()) {
					tm.setPrefix(ChatColor.YELLOW + "");
					tm.setColor(ChatColor.YELLOW);
				} else {
					tm.setPrefix(ChatColor.GRAY + "");
					tm.setColor(ChatColor.GRAY);
				}

				tm.addEntry(tmOnline.getName());
				// tmOnline.setScoreboard(scoreboard);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}

		for(Player tmOnline : Bukkit.getOnlinePlayers()) {
			Scoreboard sbTM = tmOnline.getScoreboard();
			try {

				if(sbTM == null) {
					continue;
				}
				Team tm = sbTM.getTeam(jugOnline.getBukkitPlayer().getName());

				if(tm != null) {
					continue;
				}

				tm = sbTM.registerNewTeam(jugOnline.getBukkitPlayer().getName());

				if (jugOnline.isHideRank()) {
					tm.setPrefix(ChatColor.GRAY + "");
					tm.setColor(ChatColor.GRAY);
				} else if (jugOnline.is_Owner()) {
					tm.setPrefix(ChatColor.DARK_RED+""+ChatColor.BOLD+Ranks.OWNER.name()+" "+jugOnline.getNameTagColor());
					tm.setColor(jugOnline.getNameTagColor());
				} else if (isAdmod(jugOnline.getBukkitPlayer())) {
					tm.setPrefix(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "AD" + ChatColor.WHITE + "" + ChatColor.BOLD + Ranks.MOD.name() + " " + jugOnline.getNameTagColor());
					tm.setColor(jugOnline.getNameTagColor());
				} else if (jugOnline.is_Admin()) {
					tm.setPrefix(ChatColor.RED + "" + ChatColor.BOLD + Ranks.ADMIN.name() + " " + jugOnline.getNameTagColor());
					tm.setColor(jugOnline.getNameTagColor());
				} else if (jugOnline.is_MODERADOR()) {
					tm.setPrefix(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + Ranks.MOD.name() + " " + jugOnline.getNameTagColor());
					tm.setColor(jugOnline.getNameTagColor());
				} else if (jugOnline.is_AYUDANTE()) {
					tm.setPrefix(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + Ranks.AYUDANTE.name() + " " + jugOnline.getNameTagColor());
					tm.setColor(jugOnline.getNameTagColor());
				} else if (jugOnline.is_YOUTUBER()) {
					tm.setPrefix(ChatColor.RED + "" + ChatColor.BOLD + "YouTuber " + jugOnline.getNameTagColor());
					tm.setColor(jugOnline.getNameTagColor());
				} else if (jugOnline.is_BUILDER()) {
					tm.setPrefix(ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+Ranks.BUILDER.name()+" "+jugOnline.getNameTagColor());
					tm.setColor(jugOnline.getNameTagColor());
				} else if (jugOnline.is_RUBY()) {
					tm.setPrefix(ChatColor.RED + "" + ChatColor.BOLD + Ranks.RUBY.name() + " " + jugOnline.getNameTagColor());
					tm.setColor(jugOnline.getNameTagColor());
				} else if (jugOnline.is_ELITE()) {
					tm.setPrefix(ChatColor.GOLD + "" + ChatColor.BOLD + Ranks.ELITE.name() + " " + jugOnline.getNameTagColor());
					tm.setColor(jugOnline.getNameTagColor());
				} else if (jugOnline.is_SVIP()) {
					tm.setPrefix(ChatColor.GREEN + "" + ChatColor.BOLD + Ranks.SVIP.name() + " " + jugOnline.getNameTagColor());
					tm.setColor(jugOnline.getNameTagColor());
				} else if (jugOnline.is_VIP()) {
					tm.setPrefix(ChatColor.AQUA + "" + ChatColor.BOLD + Ranks.VIP.name() + " " + jugOnline.getNameTagColor());
					tm.setColor(jugOnline.getNameTagColor());
				} else if (jugOnline.is_Premium()) {
					tm.setPrefix(ChatColor.YELLOW + "");
					tm.setColor(ChatColor.YELLOW);
				} else {
					tm.setPrefix(ChatColor.GRAY + "");
					tm.setColor(ChatColor.GRAY);
				}

				tm.addEntry(jugOnline.getBukkitPlayer().getName());
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}

		jugOnline.getBukkitPlayer().setScoreboard(sb);
	}

	private void onJoin(Player p) {
		Jugador j = Jugador.getJugador(p);
		Player enlineados = j.getBukkitPlayer();
		// Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();

		// j.getBukkitPlayer().setScoreboard(scoreboard);
		// p.setScoreboard(scoreboard);

		// Se dan los permisos

			if(j.is_VIP()) {
				addPermissionVIP(p);
			}
			
			if(j.is_SVIP()) {
				addPermissionSVIP(p);
			}
			
			if(j.is_ELITE()) {
				addPermissionELITE(p);
			}
			if(j.is_RUBY() || j.is_BUILDER()) {
				addPermissionsRuby(p);
			}
			if(j.is_MODERADOR()) {
				addPermissionMOD(p);
			}
			if(j.is_Admin() || isAdmod(p) || j.is_Owner()) {
				addPermissionAdmin(p);
			}
	}
	

	public boolean isAdmod(Player player)
	{
		String name = player.getName();
		if(name.equalsIgnoreCase("ElBuenAnvita"))
		{
			return true;
		}
		return false;
	}
	@EventHandler
	public void onPortal(PlayerPortalEvent e) {
		Player p = e.getPlayer();
		p.teleport(p.getWorld().getSpawnLocation());
	}
	
	@EventHandler
	public void onExplosion(BlockExplodeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onDispense(BlockDispenseEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onFireWork(EntitySpawnEvent e) {
		e.setCancelled(true);
	}
	
	/* @EventHandler
	public void onCreature(CreatureSpawnEvent e) {
		e.setCancelled(true);
	} */

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerTeleportWorld(PlayerChangedWorldEvent e) {
		Location loc = new Location(e.getFrom(), -80.518, 71, -80.615);
		if(e.getPlayer().getWorld().getName().equals("vip")) {
			Bukkit.getLogger().info("El usuario " + e.getPlayer().getName() + " esta en vip ahora");
			if(!e.getPlayer().hasPermission("vip.world")) {
				e.getPlayer().sendMessage(ChatColor.RED+"¡No puedes entrar a este mundo!");
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(CreativolMain.get(), new Runnable() {
					@Override
					public void run() {
						e.getPlayer().sendTitle(ChatColor.DARK_RED + "" + ChatColor.BOLD + "No eres VIP", ChatColor.RED + "No puedes ingresar a este mundo", 10, 100, 20);
					}
				}, 20L);
				e.getPlayer().teleport(loc);
				e.getPlayer().sendMessage(ChatColor.GREEN+"Fuiste enviado al spawn");
				Bukkit.getLogger().info("El usuario " + e.getPlayer().getName() + " intentó ingresar al VIP pero no era VIP o no tenía un tiquete VIP!");
			}
		}

		setScoreboard(Jugador.getJugador(e.getPlayer()));
	}
	
	@EventHandler
	public void onProjectile(ProjectileLaunchEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onPotionSplash(PotionSplashEvent e) {
		e.getAffectedEntities().clear();
		e.setCancelled(true);
	}
	
	@EventHandler
	public void BlockDamageEvent(EntityCombustEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler(ignoreCancelled=true)
	public void onChat(AsyncPlayerChatEvent e) {
		String nickname = cmdNick.nicks.get(e.getPlayer().getName());
		Player p = e.getPlayer();
		Jugador j = Jugador.getJugador(p);
		String PlayerName = p.getName();
		String msg = e.getMessage();
		msg = msg.replaceAll("%", "%%"); // SE ARREGLA EL BUG DE LOS PORCENTAJES.
		if(!msg.startsWith("!")) {
			for(Player rc : Bukkit.getOnlinePlayers()) {
				try {
				if(rc.getWorld() == p.getWorld()) {
					if(rc.getLocation().distance(p.getLocation()) > 500) {
					e.getRecipients().remove(rc);
					}
				} else {
					e.getRecipients().remove(rc);
				}
			} catch(Exception ex) {
			}
			}
			if(nickname==null) {
				if(j.isHideRank())
					e.setFormat(ChatColor.YELLOW + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+msg);
				else if(isAdmod(p))
				{
					e.setFormat(ChatColor.DARK_AQUA+""+ChatColor.BOLD+"AD"+ ChatColor.WHITE+""+ChatColor.BOLD+Ranks.MOD.name()+" "+ j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				}
				else if(j.is_Owner())
					e.setFormat(ChatColor.DARK_RED+""+ChatColor.BOLD+Ranks.OWNER.name()+" "+ChatColor.DARK_GRAY+" "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_Admin())
					e.setFormat(ChatColor.RED+""+ChatColor.BOLD+Ranks.ADMIN.name()+" "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_MODERADOR())
					e.setFormat(ChatColor.DARK_PURPLE+""+ChatColor.BOLD+Ranks.MOD.name()+" "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_AYUDANTE())
					e.setFormat(ChatColor.DARK_PURPLE+""+ChatColor.BOLD+Ranks.AYUDANTE.name()+" "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_YOUTUBER())
					e.setFormat(ChatColor.RED+""+ChatColor.BOLD+"You"+ChatColor.WHITE+""+ChatColor.BOLD+"Tuber "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_BUILDER())
					e.setFormat(ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+Ranks.BUILDER.name()+" "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_RUBY())
					e.setFormat(ChatColor.AQUA + "★ " + ChatColor.RED+""+ChatColor.BOLD+Ranks.RUBY.name()+" "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_ELITE())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+Ranks.ELITE.name()+" "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_SVIP())
					e.setFormat(ChatColor.GREEN+""+ChatColor.BOLD+Ranks.SVIP.name()+" "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_VIP())
					e.setFormat(ChatColor.AQUA+""+ChatColor.BOLD+Ranks.VIP.name()+" "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_Premium())
					e.setFormat(ChatColor.BLUE+""+ChatColor.BOLD+Ranks.PREMIUM.name()+" "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+msg);
				else
					e.setFormat(j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+msg);
			} else {
				if(j.isHideRank())
					e.setFormat(j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+msg);
				else if(isAdmod(p))
				{
					e.setFormat(ChatColor.DARK_AQUA+""+ChatColor.BOLD+"AD"+ ChatColor.WHITE+""+ChatColor.BOLD+Ranks.MOD.name()+" "+j.getNameTagColor()+ nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				}
				else if(j.is_Owner())
					e.setFormat(ChatColor.DARK_RED+""+ChatColor.BOLD+Ranks.OWNER.name()+" "+ChatColor.DARK_GRAY+" "+j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_Admin())
					e.setFormat(ChatColor.RED+""+ChatColor.BOLD+Ranks.ADMIN.name()+" "+j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_MODERADOR())
					e.setFormat(ChatColor.DARK_PURPLE+""+ChatColor.BOLD+Ranks.MOD.name()+" "+j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_AYUDANTE())
					e.setFormat(ChatColor.DARK_PURPLE+""+ChatColor.BOLD+Ranks.AYUDANTE.name()+" "+j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_YOUTUBER())
					e.setFormat(ChatColor.RED+""+ChatColor.BOLD+"You"+ChatColor.WHITE+""+ChatColor.BOLD+"Tuber "+j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_BUILDER())
					e.setFormat(ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+Ranks.BUILDER.name()+" "+j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_RUBY())
					e.setFormat(ChatColor.AQUA + "★ " + ChatColor.RED+""+ChatColor.BOLD+Ranks.RUBY.name()+" "+j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_ELITE())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+Ranks.ELITE.name()+" "+j.getNameTagColor()+ nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_SVIP())
					e.setFormat(ChatColor.GREEN+""+ChatColor.BOLD+Ranks.SVIP.name()+" "+j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_VIP())
					e.setFormat(ChatColor.AQUA+""+ChatColor.BOLD+Ranks.VIP.name()+" "+j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_Premium())
					e.setFormat(ChatColor.BLUE+""+ChatColor.BOLD+Ranks.PREMIUM.name()+" "+j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+msg);
				else
					e.setFormat(j.getNameTagColor()+ nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+msg);
			}
		
		} else {
			if(msg.length() > 1) {
				msg = msg.substring(1);
			} else { 
				e.setCancelled(true);
				return;
			}
			if(nickname==null || nickname == "" || nickname == " ") {
				if(j.isHideRank())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+ "GLOBAL " + ChatColor.YELLOW + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+msg);
				else if(isAdmod(p))
				{
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+  "GLOBAL " + ChatColor.DARK_AQUA+""+ChatColor.BOLD+"AD"+ ChatColor.WHITE+""+ChatColor.BOLD+Ranks.MOD.name()+" "+ j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				}
				else if(j.is_Owner())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+  "GLOBAL " + ChatColor.DARK_RED+""+ChatColor.BOLD+Ranks.OWNER.name()+" "+ChatColor.DARK_GRAY+" "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_Admin())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+ "GLOBAL " + ChatColor.RED+""+ChatColor.BOLD+Ranks.ADMIN.name()+" "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_MODERADOR())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+  "GLOBAL " + ChatColor.DARK_PURPLE+""+ChatColor.BOLD+Ranks.MOD.name()+" "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_AYUDANTE())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+ "GLOBAL " + ChatColor.DARK_PURPLE+""+ChatColor.BOLD+Ranks.AYUDANTE.name()+" "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_YOUTUBER())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+ "GLOBAL " + ChatColor.RED+""+ChatColor.BOLD+"You"+ChatColor.WHITE+""+ChatColor.BOLD+"Tuber "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_BUILDER())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+ "GLOBAL " + ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+Ranks.BUILDER.name()+" "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_RUBY())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+ "GLOBAL " + ChatColor.AQUA + "★ " + ChatColor.RED+""+ChatColor.BOLD+Ranks.RUBY.name()+" "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_ELITE())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+ "GLOBAL " + ChatColor.GOLD+""+ChatColor.BOLD+Ranks.ELITE.name()+" "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_SVIP())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+ "GLOBAL " + ChatColor.GREEN+""+ChatColor.BOLD+Ranks.SVIP.name()+" "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_VIP())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+ "GLOBAL " + ChatColor.AQUA+""+ChatColor.BOLD+Ranks.VIP.name()+" "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_Premium())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+ "GLOBAL " + ChatColor.BLUE+""+ChatColor.BOLD+Ranks.PREMIUM.name()+" "+j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+msg);
				else
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+"GLOBAL " + j.getNameTagColor() + PlayerName + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+msg);
			} else {
				if(j.isHideRank())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+ "GLOBAL " + j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+msg);
				else if(isAdmod(p))
				{
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+ "GLOBAL " + ChatColor.DARK_AQUA+""+ChatColor.BOLD+"AD"+ ChatColor.WHITE+""+ChatColor.BOLD+Ranks.MOD.name()+" "+j.getNameTagColor()+ nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				}
				else if(j.is_Owner())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+ "GLOBAL " + ChatColor.DARK_RED+""+ChatColor.BOLD+Ranks.OWNER.name()+" "+ChatColor.DARK_GRAY+" "+j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_Admin())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+ "GLOBAL " + ChatColor.RED+""+ChatColor.BOLD+Ranks.ADMIN.name()+" "+j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_MODERADOR())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+ "GLOBAL " + ChatColor.DARK_PURPLE+""+ChatColor.BOLD+Ranks.MOD.name()+" "+j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_AYUDANTE())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+ "GLOBAL " + ChatColor.DARK_PURPLE+""+ChatColor.BOLD+Ranks.AYUDANTE.name()+" "+j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_YOUTUBER())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+ "GLOBAL " + ChatColor.RED+""+ChatColor.BOLD+"You"+ChatColor.WHITE+""+ChatColor.BOLD+"Tuber "+j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_BUILDER())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+ "GLOBAL " + ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+Ranks.BUILDER.name()+" "+j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_RUBY())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+  "GLOBAL " + ChatColor.AQUA + "★ " + ChatColor.RED+""+ChatColor.BOLD+Ranks.RUBY.name()+" "+j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_ELITE())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+ "GLOBAL " + ChatColor.GOLD+""+ChatColor.BOLD+Ranks.ELITE.name()+" "+j.getNameTagColor()+ nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_SVIP())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+  "GLOBAL " + ChatColor.GREEN+""+ChatColor.BOLD+Ranks.SVIP.name()+" "+j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_VIP())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+ "GLOBAL " + ChatColor.AQUA+""+ChatColor.BOLD+Ranks.VIP.name()+" "+j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+ChatColor.translateAlternateColorCodes('&', msg));
				else if(j.is_Premium())
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+ "GLOBAL " + ChatColor.BLUE+""+ChatColor.BOLD+Ranks.PREMIUM.name()+" "+j.getNameTagColor() + nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+msg);
				else
					e.setFormat(ChatColor.GOLD+""+ChatColor.BOLD+  "GLOBAL " + j.getNameTagColor()+ nickname + ChatColor.DARK_GRAY+" » " + ChatColor.GRAY+msg);
			}
			
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		e.setQuitMessage(null);
		// Jugador jug = Jugador.getJugador(e.getPlayer());
		Jugador.removeJugador(e.getPlayer().getName());
		// scoreboard.getTeam(e.getPlayer().getName()).unregister();
		if(interact_cd.containsKey(e.getPlayer())) {
			interact_cd.remove(e.getPlayer());
		}
		for (Player all : Bukkit.getOnlinePlayers()) {
			Jugador jugall = Jugador.getJugador(all);
			setScoreboard(jugall);
		}
		CreativolMain.get().removeVanished(e.getPlayer());
	}

	@EventHandler
	public void onKill(PlayerDeathEvent e) {
		e.setDeathMessage(null);
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		e.setLeaveMessage(null);
	}

	public void addPermissionDefault(Player p) {
	    List<String> rows = CreativolMain.get().getConfig().getStringList("permisos.default");
	    int numRows = rows.size();

        if(rows == null || rows.isEmpty()) {
            return;
        }

        for(String row : rows) {
        	if(row.startsWith("-")) {
        		StringBuilder sb = new StringBuilder(row);
        		String rowdos = sb.deleteCharAt(0).toString();
				p.addAttachment(CoreMain.getInstance(), rowdos, false);
        		// Bukkit.getLogger().info("Cargado permiso negativo " + rowdos + " para " + p.getName());
			} else {
            	p.addAttachment(CoreMain.getInstance(), row, true);
        	}
            // Bukkit.getLogger().info("[Debug] Se cargó el permiso <" + row + "> (" + !row.startsWith("-") + ") para el usuario " + p.getName() + ".");
        }
	}
	
	public void addPermissionVIP(Player p) {
		List<String> rows = CreativolMain.get().getConfig().getStringList("permisos.vip");
		int numRows = rows.size();

		if(rows == null || rows.isEmpty()) {
			return;
		}

		for(String row : rows) {
			if(row.startsWith("-")) {
				StringBuilder sb = new StringBuilder(row);
				String rowdos = sb.deleteCharAt(0).toString();
				p.addAttachment(CoreMain.getInstance(), rowdos, false);
				// Bukkit.getLogger().info("Cargado permiso negativo " + rowdos + " para " + p.getName());
			} else {
				p.addAttachment(CoreMain.getInstance(), row, true);
			}
			// Bukkit.getLogger().info("[Debug] Se cargó el permiso <" + row + "> (" + !row.startsWith("-") + ") para el usuario " + p.getName() + ".");
		}
	}
	public void addPermissionMOD(Player p) {
		List<String> rows = CreativolMain.get().getConfig().getStringList("permisos.mod");
		int numRows = rows.size();

		if(rows == null || rows.isEmpty()) {
			return;
		}

		for(String row : rows) {
			if(row.startsWith("-")) {
				StringBuilder sb = new StringBuilder(row);
				String rowdos = sb.deleteCharAt(0).toString();
				p.addAttachment(CoreMain.getInstance(), rowdos, false);
				// Bukkit.getLogger().info("Cargado permiso negativo " + rowdos + " para " + p.getName());
			} else {
				p.addAttachment(CoreMain.getInstance(), row, true);
			}
			// Bukkit.getLogger().info("[Debug] Se cargó el permiso <" + row + "> (" + !row.startsWith("-") + ") para el usuario " + p.getName() + ".");
		}
	}
	public void addPermissionSVIP(Player p) {
		List<String> rows = CreativolMain.get().getConfig().getStringList("permisos.svip");
		int numRows = rows.size();

		if(rows == null || rows.isEmpty()) {
			return;
		}

		for(String row : rows) {
			if(row.startsWith("-")) {
				StringBuilder sb = new StringBuilder(row);
				String rowdos = sb.deleteCharAt(0).toString();
				p.addAttachment(CoreMain.getInstance(), rowdos, false);
				// Bukkit.getLogger().info("Cargado permiso negativo " + rowdos + " para " + p.getName());
			} else {
				p.addAttachment(CoreMain.getInstance(), row, true);
			}
			// Bukkit.getLogger().info("[Debug] Se cargó el permiso <" + row + "> (" + !row.startsWith("-") + ") para el usuario " + p.getName() + ".");
		}
	}
	
	public void addPermissionELITE(Player p) {
		List<String> rows = CreativolMain.get().getConfig().getStringList("permisos.elite");
		int numRows = rows.size();

		if(rows == null || rows.isEmpty()) {
			return;
		}

		for(String row : rows) {
			if(row.startsWith("-")) {
				StringBuilder sb = new StringBuilder(row);
				String rowdos = sb.deleteCharAt(0).toString();
				p.addAttachment(CoreMain.getInstance(), rowdos, false);
				// Bukkit.getLogger().info("Cargado permiso negativo " + rowdos + " para " + p.getName());
			} else {
				p.addAttachment(CoreMain.getInstance(), row, true);
			}
			// Bukkit.getLogger().info("[Debug] Se cargó el permiso <" + row + "> (" + !row.startsWith("-") + ") para el usuario " + p.getName() + ".");
		}
	}
	public void addPermissionsRuby(Player p) {
		List<String> rows = CreativolMain.get().getConfig().getStringList("permisos.ruby");
		int numRows = rows.size();

		if(rows == null || rows.isEmpty()) {
			return;
		}

		for(String row : rows) {
			if(row.startsWith("-")) {
				StringBuilder sb = new StringBuilder(row);
				String rowdos = sb.deleteCharAt(0).toString();
				p.addAttachment(CoreMain.getInstance(), rowdos, false);
				// Bukkit.getLogger().info("Cargado permiso negativo " + rowdos + " para " + p.getName());
			} else {
				p.addAttachment(CoreMain.getInstance(), row, true);
			}
			// Bukkit.getLogger().info("[Debug] Se cargó el permiso <" + row + "> (" + !row.startsWith("-") + ") para el usuario " + p.getName() + ".");
		}
	}
	/*
	public void addPermissionAdmod(Player p) {
	    p.addAttachment(CoreMain.getInstance(), "bukkit.*", true);
	    p.addAttachment(CoreMain.getInstance(), "minecraft.*", true);
	    p.addAttachment(CoreMain.getInstance(), "*", true);
	} */
	public void addPermissionAdmin(Player p) {
		List<String> rows = CreativolMain.get().getConfig().getStringList("permisos.admin");
		int numRows = rows.size();

		if(rows == null || rows.isEmpty()) {
			return;
		}

		for(String row : rows) {
			if(row.startsWith("-")) {
				StringBuilder sb = new StringBuilder(row);
				String rowdos = sb.deleteCharAt(0).toString();
				p.addAttachment(CoreMain.getInstance(), rowdos, false);
				// Bukkit.getLogger().info("Cargado permiso negativo " + rowdos + " para " + p.getName());
			} else {
				p.addAttachment(CoreMain.getInstance(), row, true);
			}
			// Bukkit.getLogger().info("[Debug] Se cargó el permiso <" + row + "> (" + !row.startsWith("-") + ") para el usuario " + p.getName() + ".");
		}
	}

	@EventHandler
	public void onPlayerEnterPlot(PlayerEnterPlotEvent e) {
		Player p = e.getPlayer();

		setScoreboard(Jugador.getJugador(p));
	}

	@EventHandler
	public void onPlayerExitPlot(PlayerLeavePlotEvent e) {
		Player p = e.getPlayer();

		setScoreboard(Jugador.getJugador(p));
	}
}
