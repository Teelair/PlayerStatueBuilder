package com.skytechbytes.testplugin;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.shanerx.mojang.Mojang;
import org.shanerx.mojang.PlayerProfile;

public class Util {
	private static Map<String,BufferedImage> cache = new HashMap<>();
	
	
	public static BufferedImage getSkinImage(Player p, String[] arg3) throws Exception {
		
		String name = p.getName();
		
		if (arg3.length >= 1) {
			name = arg3[0];
			
		}
		
		BufferedImage bi = null;
		if (!cache.containsKey(name)) {
			//don't want those creepy little errors.
			if (name.startsWith(".") && name.length() > 1) {
				
				if (!p.hasPermission("playerstatuebuilderx.custom")) {
					throw new Exception("Insufficient permissions to create custom statues!");
				}
				
				
				/*
				 * The skin is on the local disk in the plugin's data folder.
				 */
				//Remove the weirdness at the front
				String fileName = name.substring(1);
				
				Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
				boolean hasSpecialChar = pattern.matcher(fileName).find();
				
				if (hasSpecialChar) {
					if (p.isOp()) p.sendMessage(ChatColor.RED + "Admins: make sure your skin image is alphanumeric only");
					throw new Exception("Your skin name can't have a special character (don't include .png).");
				}
				
				if (!(fileName.endsWith(".png") || fileName.endsWith(".PNG"))) {
					fileName = fileName + ".png";
				}
				
				
				try {
				
					Path customFile = Paths.get(PlayerStatuePlugin.instance.getDataFolder().toURI().resolve(fileName));
				
				
					bi = ImageIO.read(customFile.toFile());
					
					if (bi == null) {
						throw new Exception();
					}
				} catch (Exception e) {
					throw new Exception("Unable to load file from plugin data folder. Make sure the name is spelled correctly and the skin"
							+ " image file is in the same folder as the config file.");
				}
				
			
			} else {
			
				/*
				 * If the skin has no prefix, it is a normal skin that must be retrieved from the cloud.
				 */
				try {
					Mojang api = new Mojang().connect();
					api.connect();
					
					PlayerProfile pp = api.getPlayerProfile(api.getUUIDOfUsername(name));
		
					Optional<URL> URL  = pp.getTextures().get().getSkin();
		
					bi = ImageIO.read(URL.get());
					
					if (bi == null) {
						throw new Exception();
					}
				} catch (Exception e) {
					throw new Exception("Something is wrong with getting the skin from the API. You may be offline, sending too many requests "
							+ " (i.e. wait 2 minutes before "
							+ "trying again), or the skin just doesn't exist.");
				}
			
			}
			
			cache.put(name, bi);
		} else {
			bi = cache.get(name);
		}
		
		return (bi);
	}
	
//	public static final List<Material> UNBREAKABLE = new ArrayList<Material>();
//	static {
//		UNBREAKABLE.add(Material.BARRIER);
//		UNBREAKABLE.add(Material.BEDROCK);
//		UNBREAKABLE.add(Material.COMMAND_BLOCK);
//		UNBREAKABLE.add(Material.END_GATEWAY);
//		UNBREAKABLE.add(Material.END_PORTAL);
//		UNBREAKABLE.add(Material.END_PORTAL_FRAME);
//		UNBREAKABLE.add(Material.JIGSAW);
//		UNBREAKABLE.add(Material.STRUCTURE_BLOCK);
//		UNBREAKABLE.add(Material.ANVIL);
//		UNBREAKABLE.add(Material.ENCHANTING_TABLE);
//		UNBREAKABLE.add(Material.OBSIDIAN);
//		UNBREAKABLE.add(Material.ENDER_CHEST);
//		UNBREAKABLE.add(Material.NETHER_PORTAL);
//		UNBREAKABLE.add(Material.CHEST);
//		UNBREAKABLE.add(Material.SHULKER_BOX);
//		UNBREAKABLE.add(Material.BARREL);
//	}
//	public static boolean isSensitive(Material m) {
//		return UNBREAKABLE.contains(m);
//	}
}
