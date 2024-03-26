package com.skytechbytes.playerstatuebuilder;

import com.google.gson.Gson;
import com.skytechbytes.playerstatuebuilder.objects.PlayerInfo;
import com.skytechbytes.playerstatuebuilder.objects.PlayerSkin;
import com.skytechbytes.playerstatuebuilder.objects.SkinDetails;
import com.skytechbytes.playerstatuebuilder.objects.Texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 *
 * @author SkyTechBytes
 * Big thanks to Crafatar and Playerdb.co for an amazing api to use for names and skins
 *
 */
public class Util {
	private static final Map<String, BufferedImage> cache = new ConcurrentHashMap<>();

	public static BufferedImage getSkinImage(String name) throws Exception {
		BufferedImage bi;
		if (!cache.containsKey(name)) {
			if (isDiskSkin(name)) {
				bi = getDiskSkinImage(name);
			} else {
				bi = getCloudSkinImage(name);
			}
		} else {
			bi = cache.get(name);
		}

		return bi;
	}

	public static boolean isDiskSkin(String name) {
		return name.startsWith(".") && name.length() > 1;
	}

	public static BufferedImage getDiskSkinImage(String name) throws Exception {
		BufferedImage bi;
		/*
		 * The skin is on the local disk in the plugin's data folder.
		 */
		// Remove the weirdness at the front
		String fileName = name.substring(1);

		Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
		boolean hasSpecialChar = pattern.matcher(fileName).find();

		if (hasSpecialChar) {
			throw new Exception("Admins: make sure your skin image is alphanumeric only; Your skin name can't have a special character (don't include .png).");
		}

		if (!(fileName.endsWith(".png") || fileName.endsWith(".PNG"))) {
			fileName = fileName + ".png";
		}

		try {
			Path customFile = Paths.get(PlayerStatueBuilder.instance.getDataFolder().toURI().resolve(fileName));

			bi = ImageIO.read(customFile.toFile());

			if (bi == null) {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new Exception("Unable to load file from plugin data folder. Make sure the name is spelled correctly and the skin image file is in the same folder as the config file.");
		}

		return bi;
	}

	private static String getUUID(String name) throws Exception {
		try {
			Gson gson = new Gson();
			PlayerInfo info = gson.fromJson(APIWrapper.readJsonFromUrl("https://api.mojang.com/users/profiles/minecraft/" + name), PlayerInfo.class);
			return info.id();
		} catch (Exception e) {
			throw new Exception("Failed to lookup uuid, likely because player specified does not exist.");
		}
	}

	public static BufferedImage getCloudSkinImage(String name) throws Exception {
		/*
		 * Obtain player UUID to try alternate endpoints
		 */
		String uuid = getUUID(name);
		Gson gson = new Gson();
		PlayerSkin skin = gson.fromJson(APIWrapper.readJsonFromUrl("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid), PlayerSkin.class);
		SkinDetails details = gson.fromJson(new String(Base64.getDecoder().decode(skin.properties()[0].value())), SkinDetails.class);
		Texture texture = details.textures().get("SKIN");
		return ImageIO.read(new URL(texture.url()));
	}
}
