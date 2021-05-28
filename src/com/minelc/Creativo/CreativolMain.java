package com.minelc.Creativo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import com.minelc.Creativo.Comandos.*;
import com.minelc.Creativo.runnables.DeleteEntitiesInRoad;
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

public class CreativolMain extends JavaPlugin implements CommandExecutor {

	private static CreativolMain instance;
	public static Set<Player> Vanished = Sets.newHashSet();
	public static HashMap<Player, Player> tpa = new HashMap<Player, Player>();
	public HashMap<Player, Player> recentTell = new HashMap<Player, Player>();
    public void onEnable() {
    	try {
        	instance = this;
        	
            Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
            
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Timer(), 20L, 20L);
            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new DeleteEntitiesInRoad(), 0L, 20L);

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
           // getCommand("tell").setExecutor(new TellCmd()); // Tell fue cambiado está más abajo
            getCommand("report").setExecutor(new ReportCMD());
            getCommand("near").setExecutor(new cmdNear());
            // getCommand("sit").setExecutor(new Sit());
            getCommand("tell").setExecutor(new CmdTell());
			getCommand("r").setExecutor(new ReplyCMD());
            getCommand("sc").setExecutor(new StaffChatCMD());
            getCommand("tpa").setExecutor(new TpaCMD());
            getCommand("tpaccept").setExecutor(new TpaAcceptCMD());
            getCommand("tpdeny").setExecutor(new TpaDenyCMD());
            getCommand("mem").setExecutor(new MemCMD());
    	} catch(Exception ex) {
    		Bukkit.shutdown();
    		ex.printStackTrace();
    	}
    }

	public void onDisable() {
		for(Player Online : Bukkit.getOnlinePlayers()) {
			Online.kickPlayer(" ");
		}
    }
    
    public static CreativolMain get() {
        return instance;
    }

    
	public boolean isVanished(Player p) {
		return Vanished.contains(p);
	}
	
	public void removeVanished(Player p) {
		Vanished.remove(p);
	}
	
	public void addVanished(Player p) {
		Vanished.add(p);
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


