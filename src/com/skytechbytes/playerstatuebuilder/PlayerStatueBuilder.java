package com.skytechbytes.playerstatuebuilder;
import com.skytechbytes.playerstatuebuilder.support.PlotSquaredWrapper;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import com.skytechbytes.playerstatuebuilder.builder.AssetManager;
import com.skytechbytes.playerstatuebuilder.support.VaultWrapper;
import com.skytechbytes.playerstatuebuilder.support.WorldGuardWrapper;
/**
 * 
 * @author SkyTechBytes
 *
 */
public class PlayerStatueBuilder extends JavaPlugin {
	public static PlayerStatueBuilder instance;
	public static WorldGuardWrapper wgw = null;
	public static VaultWrapper vw = null;
	public static PlotSquaredWrapper plotw = null;
	public static StatueConfiguration sc;

	 // Fired when plugin is first enabled
    @Override
    public void onEnable() {
    	instance = this;
    	
    	sc = new StatueConfiguration();
    	sc.createConfig();
    	
    	try {
    		wgw = new WorldGuardWrapper();
    	} catch (Throwable fit) {
    		Log.log("Worldguard not detected. You MUST have WorldGuard if you want PlayerStatueBuilderX to respect claim/region protections.");
    		wgw = null;
    	}
    	
    	try {
    		vw = new VaultWrapper();
    	} catch (Throwable rock) {
    		vw = null;
    	}

		try {
			plotw = new PlotSquaredWrapper();
		} catch (Throwable stick) {
			Log.log("PlotSquared not detected. The plugin MUST detect PlotSquared if you want the plugin to respect plot squared protections.");
			plotw = null;
		}
    	
    	AssetManager.initialize();
    	
    	this.getCommand("statue").setExecutor(new CommandStatue());
    	this.getCommand("statue").setTabCompleter(new StatueTabComplete());
    	
    	this.getCommand("undostatue").setExecutor(new CommandUndostatue());
    	
    	// Bstats begins here
    	int pluginId = 7093;
    	new Metrics(this, pluginId);
    }
}
