package com.minelc.Creativo.runnables;

import com.github.intellectualsites.plotsquared.bukkit.util.BukkitUtil;
import com.github.intellectualsites.plotsquared.plot.PlotSquared;
import com.github.intellectualsites.plotsquared.plot.object.Location;
import com.github.intellectualsites.plotsquared.plot.object.Plot;
import com.github.intellectualsites.plotsquared.plot.util.TaskManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.MetadataValue;

import java.util.Iterator;
import java.util.List;

public class DeleteEntitiesInRoad implements Runnable {
    @Override
    public void run() {
        final World world = Bukkit.getWorld("plots");
        try {
            if (world == null) {
                return;
            }
            List<Entity> entities = world.getEntities();
            Iterator<Entity> iterator = entities.iterator();
            while (iterator.hasNext()) {
                Entity entity = iterator.next();
                switch (entity.getType().toString()) {
                    case "EGG":
                    case "FISHING_HOOK":
                    case "ENDER_SIGNAL":
                    case "AREA_EFFECT_CLOUD":
                    case "EXPERIENCE_ORB":
                    case "LEASH_HITCH":
                    case "FIREWORK":
                    case "LIGHTNING":
                    case "WITHER_SKULL":
                    case "UNKNOWN":
                    case "PLAYER":
                        // non moving / unmovable
                        continue;
                    case "THROWN_EXP_BOTTLE":
                    case "SPLASH_POTION":
                    case "SNOWBALL":
                    case "SHULKER_BULLET":
                    case "SPECTRAL_ARROW":
                    case "ENDER_PEARL":
                    case "ARROW":
                    case "LLAMA_SPIT":
                    case "TRIDENT":
                        // managed elsewhere | projectile
                        continue;
                    case "ITEM_FRAME":
                    case "PAINTING":
                        // Not vehicles
                        continue;
                    case "ARMOR_STAND":
                        // Temporarily classify as vehicle
                    case "MINECART":
                    case "MINECART_CHEST":
                    case "MINECART_COMMAND":
                    case "MINECART_FURNACE":
                    case "MINECART_HOPPER":
                    case "MINECART_MOB_SPAWNER":
                    case "ENDER_CRYSTAL":
                    case "MINECART_TNT":
                    case "BOAT":
                        Location location = BukkitUtil.getLocation(entity.getLocation());
                        Plot plot = location.getPlot();
                        if (plot == null) {
                            if (location.isPlotArea()) {
                                if (entity.hasMetadata("ps-tmp-teleport")) {
                                    continue;
                                }
                                iterator.remove();
                                entity.remove();
                            }
                            continue;
                        }
                        List<MetadataValue> meta = entity.getMetadata("plot");
                        if (meta.isEmpty()) {
                            continue;
                        }
                        Plot origin = (Plot) meta.get(0).value();
                        if (!plot.equals(origin.getBasePlot(false))) {
                            if (entity.hasMetadata("ps-tmp-teleport")) {
                                continue;
                            }
                            iterator.remove();
                            entity.remove();
                        }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
