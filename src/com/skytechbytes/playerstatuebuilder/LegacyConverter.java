package com.skytechbytes.playerstatuebuilder;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
/**
 * Super useful converter by DarkVamprism!
 * https://www.reddit.com/r/Minecraft/comments/1vl7p7/releaseskin_converter/
 * Added the conversion for slim skin conversions (by SkyTechBytes)
 */
public class LegacyConverter {
	/**
	 * DOES NOT modify the image parameter put inside.
	 * @param legacy the image to convert
	 * @param slim type to convert to
	 * @return converted skin
	 * @throws Exception during reading the image
	 */
	public static BufferedImage convertLegacy(BufferedImage legacy, boolean slim) throws Exception {
        BufferedImage outImg = new BufferedImage(64,64,BufferedImage.TYPE_INT_ARGB);
		Graphics outGfx = outImg.getGraphics();

		// Copy old layout(for the body, head and right limbs)
		outGfx.drawImage(legacy, 0, 0, 64, 32, 0, 0, 64, 32, null);

		// Right leg -> left leg
		outGfx.drawImage(legacy, 24, 48, 20, 52, 4, 16, 8, 20, null); // Top
		outGfx.drawImage(legacy, 28, 48, 24, 52, 8, 16, 12, 20, null); // Bottom
		outGfx.drawImage(legacy, 20, 52, 16, 64, 8, 20, 12, 32, null); // Outside -> inside
		outGfx.drawImage(legacy, 24, 52, 20, 64, 4, 20, 8, 32, null); // Front
		outGfx.drawImage(legacy, 28, 52, 24, 64, 0, 20, 4, 32, null); // Inside -> outside
		outGfx.drawImage(legacy, 32, 52, 28, 64, 12, 20, 16, 32, null); // Back

		// Right arm -> left arm
		
		if (!slim) {
			outGfx.drawImage(legacy, 40, 48, 36, 52, 44, 16, 48, 20, null); // Top
			outGfx.drawImage(legacy, 44, 48, 40, 52, 48, 16, 52, 20, null); // Bottom
			outGfx.drawImage(legacy, 48, 52, 44, 64, 52, 20, 56, 32, null); // Back
			outGfx.drawImage(legacy, 40, 52, 36, 64, 44, 20, 48, 32, null); // Front
			outGfx.drawImage(legacy, 44, 52, 40, 64, 40, 20, 44, 32, null); // Inside -> outside
			outGfx.drawImage(legacy, 36, 52, 32, 64, 48, 20, 52, 32, null); // Outside -> inside
		} else {
			/*
			 * A bit more difficult than it seems (slim converter made by SkyTechBytes)
			 */
			BufferedImage front = legacy.getSubimage(44, 20, 3, 12);//front
			BufferedImage inside = legacy.getSubimage(47, 20, 4, 12);//inside
			BufferedImage back = legacy.getSubimage(51, 20, 3, 12);//back
			BufferedImage outside = legacy.getSubimage(40, 20, 4, 12);//outside
			BufferedImage bottom = legacy.getSubimage(47, 16, 3, 4);//bottom
			BufferedImage top = legacy.getSubimage(44, 16, 3, 4);//top
			outGfx.drawImage(front, 36+3, 52, -3, 12, null);
			outGfx.drawImage(inside, 32+4, 52, -4, 12, null);
			outGfx.drawImage(back,43+3, 52, -3, 12, null);
			outGfx.drawImage(outside, 39+4, 52, -4, 12, null);
			outGfx.drawImage(bottom, 39+3, 48, -3, 4, null);
			outGfx.drawImage(top,36+3, 48, -3, 4, null);
		}

		return outImg;
	}
}
