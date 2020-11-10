package com.minelc.Creativo.Comandos;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Sit  implements CommandExecutor  {

	@Override
	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		if (s instanceof Player) {
			Player p = ((Player) s).getPlayer();
			Location loc = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY()-1.5 , p.getLocation().getZ());
			ArmorStand stand = loc.getWorld().spawn(loc , ArmorStand.class);
			stand.setVisible(false);
			stand.setArms(false);
			stand.setCanPickupItems(false);
			stand.setGravity(false);
			stand.setPassenger(p);
			stand.setCustomName("sit");
		}

		return false;
	}
	
}












