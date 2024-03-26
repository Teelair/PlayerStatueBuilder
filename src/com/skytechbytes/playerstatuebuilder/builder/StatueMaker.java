package com.skytechbytes.playerstatuebuilder.builder;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import com.skytechbytes.playerstatuebuilder.LegacyConverter;
import com.skytechbytes.playerstatuebuilder.Log;
/**
 * 
 * @author SkyTechBytes
 * This is where the magic happens- all the mappings!
 *
 */
public class StatueMaker extends BukkitRunnable {
	private final Schematic s;

	private final Location origin;
	private BufferedImage bi;
	private final String mode;
	private final LinkedHashMap<String, Float> params;
	private final String direction;

	public StatueMaker(Location origin, String direction, String mode, BufferedImage bi, LinkedHashMap<String, Float> flags) {
		s = new Schematic(origin.getWorld());
		this.bi = bi;
		this.mode = mode;
		this.direction = direction;
		this.params = flags;
		this.origin = origin;
	}

	private void initialize() {
        switch (direction) {
            case "North" -> FaceBuilder.minor_orientation = 1;
            case "East" -> FaceBuilder.minor_orientation = 2;
            case "West" -> FaceBuilder.minor_orientation = 3;
            case "South" -> FaceBuilder.minor_orientation = 0;
        }
	}

	protected void generateStatueSchematic() throws Exception {
		/*
		 * Types of blocks flags
		 */
		ColorMaps.getActiveColorMaps().clear();
		
		if (params.containsKey("wool")) {
			ColorMaps.getActiveColorMaps().add(C.WOOL);
		}
		
		if (params.containsKey("planks")) {
			ColorMaps.getActiveColorMaps().add(C.PLANKS);
		}
		
		if (params.containsKey("terracotta")) {
			ColorMaps.getActiveColorMaps().add(C.TERRACOTTA);
		}
		
		if (params.containsKey("concrete")) {
			ColorMaps.getActiveColorMaps().add(C.CONCRETE);
		}
		
		if (params.containsKey("glass")) {
			ColorMaps.getActiveColorMaps().add(C.GLASS);
		}
		
		if (params.containsKey("gray")) {
			ColorMaps.getActiveColorMaps().add(C.GRAY);
		}
		
		if (ColorMaps.getActiveColorMaps().isEmpty()) {
			ColorMaps.getActiveColorMaps().add(C.WOOL);
			ColorMaps.getActiveColorMaps().add(C.PLANKS);
			ColorMaps.getActiveColorMaps().add(C.TERRACOTTA);
			ColorMaps.getActiveColorMaps().add(C.CONCRETE);
		}
		//Then populate the schematic based on those block types
		
		if (params.containsKey("xy")) {
			FaceBuilder.master_orientation = 0;
		} else if (params.containsKey("xz")) {
			FaceBuilder.master_orientation = 2;
		} else if (params.containsKey("yz")) {
			FaceBuilder.master_orientation = 1;
		} else {
			FaceBuilder.master_orientation = 0;
		}

        switch (mode) {
            case "slim" -> this.makeSlimStatueSchematic(origin, bi);
            case "legacy" -> this.makeLegacyStatueSchematic(origin, bi);
            default -> this.makeStatueSchematic(origin, bi);
        }
	}

	/**
	 * Actually make the statue from the sketchpad
	 */
	protected void createStatue() {
		Log.log("Creating Structure...");

		s.createSchematic(false, false);
	}

	@Override
	public void run() {
		try {
			generateStatueSchematic();
			createStatue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Warning: the original image is not changed in this process. A new image is returned.
	 * @param ss
	 * @return
     */
	private BufferedImage preProcessing(BufferedImage ss) {
		/*
		 * Add armor to the player if they request it (or just add parts)
		 */
		BufferedImage customizedImage = ImageUtil.deepCopy(ss);
		customizedImage = ImageUtil.applyFilters(customizedImage, params);
		for (String key : AssetManager.armor.keySet()) {
			if (params.containsKey(key)) {
				// Since BufferedImage is mutable, I should be able to modify the bufferedImage inside the method and it should reflect outside.
				
				customizedImage = ImageUtil.overlayImage(customizedImage, AssetManager.armor.get(key));
			}
		}

		return customizedImage;
	}

	private void right_leg(FaceBuilder fb, Reader r, Location l, BufferedImage ss) {
		// Right Leg
				fb.buildFace(l, r.part(ss, 8, 16, 4, 4), 1, 0, 1,-3, false, true);
				fb.buildFace(l, r.part(ss, 0, 20, 4, 12), 2, 0, 0,-3);
				//fb.buildFace(l, r.part(ss, 8, 16, 4, 4), 1, 0, 12,-3);
				fb.buildFace(l, r.part(ss, 12, 20, 4, 12), 0, 0, 0,-3, true, false);
				fb.buildFace(l, r.part(ss, 4, 20, 4, 12), 0, 0, 0, 0);
				//Right Pantleg
				fb.buildFace(l, r.part(ss, 8, 16 + 16, 4, 4), 1, 0, 0,-3, false, true);
				fb.buildFace(l, r.part(ss, 0, 20 + 16, 4, 12), 2,-1, 0,-3);
				//fb.buildFace(l, r.part(ss, 8, 16, 4, 4), 1, 0, 12,-3);
				fb.buildFace(l, r.part(ss, 12, 20 + 16, 4, 12), 0, 0, 0,-4, true, false);
				fb.buildFace(l, r.part(ss, 4, 20 + 16, 4, 12), 0, 0, 0, 1);
	}

	private void left_leg(FaceBuilder fb, Reader r, Location l, BufferedImage ss) {
		// Left Leg
				fb.buildFace(l, r.part(ss, 8 + 16, 16 + 32, 4, 4), 1, 4, 1,-3, false, true);
				fb.buildFace(l, r.part(ss, 24, 52, 4, 12), 2, 7, 0,-3, true, false);
				//fb.buildFace(l, r.part(ss, 8 + 16, 16 + 32, 4, 4), 1, 0 + 4, 12,-3);
				fb.buildFace(l, r.part(ss, 12 + 16, 20 + 32, 4, 12), 0, 4, 0,-3, true, false);
				fb.buildFace(l, r.part(ss, 4 + 16, 20 + 32, 4, 12), 0, 4, 0, 0);
				//Left Pantleg
				fb.buildFace(l, r.part(ss, 8 + 16-16, 16 + 32, 4, 4), 1, 4, 0,-3, false, true);
				fb.buildFace(l, r.part(ss, 24-16, 52, 4, 12), 2, 8, 0,-3, true, false);
				//fb.buildFace(l, r.part(ss, 8 + 16, 16 + 32, 4, 4), 1, 0 + 4, 12,-3);
				fb.buildFace(l, r.part(ss, 12 + 16-16, 20 + 32, 4, 12), 0, 4, 0,-4, true, false);
				fb.buildFace(l, r.part(ss, 4 + 16-16, 20 + 32, 4, 12), 0, 4, 0, 1);
	}

	private void body(FaceBuilder fb, Reader r, Location l, BufferedImage ss) {
		// Body
				fb.buildFace(l, r.part(ss, 20, 20, 8, 12), 0, 0, 12, 0);
				fb.buildFace(l, r.part(ss, 32, 20, 8, 12), 0, 0, 12,-3, true, false);
				//Jacket
				fb.buildFace(l, r.part(ss, 20, 20 + 16, 8, 12), 0, 0, 12, 1);
				fb.buildFace(l, r.part(ss, 32, 20 + 16, 8, 12), 0, 0, 12,-4, true, false);
	}

	private void right_arm(FaceBuilder fb, Reader r, Location l, BufferedImage ss) {
		// Right Arm
				fb.buildFace(l, r.part(ss, 8 + 40, 16, 4, 4), 1, -4, 1 + 12,-3, false, true);
				fb.buildFace(l, r.part(ss, 40, 20, 4, 12), 2, -4, 12,-3);
				fb.buildFace(l, r.part(ss, 8 + 40-4, 16, 4, 4), 1, -4, 12 + 12,-3);
				fb.buildFace(l, r.part(ss, 12 + 40, 20, 4, 12), 0, -4, 12,-3, true, false);
				fb.buildFace(l, r.part(ss, 4 + 40, 20, 4, 12), 0, -4, 12, 0);
				//Right Sleeve
				fb.buildFace(l, r.part(ss, 8 + 40, 16 + 16, 4, 4), 1, -4, 1 + 12-1,-3, false, true);
				fb.buildFace(l, r.part(ss, 40, 20 + 16, 4, 12), 2, -4 - 1, 12,-3);
				fb.buildFace(l, r.part(ss, 44, 16 + 16, 4, 4), 1, -4, 12 + 12 + 1,-3);
				fb.buildFace(l, r.part(ss, 12 + 40, 20 + 16, 4, 12), 0, -4, 12,-3-1, true, false);
				fb.buildFace(l, r.part(ss, 4 + 40, 20 + 16, 4, 12), 0, -4, 12, 1);
	}

	private void left_arm(FaceBuilder fb, Reader r, Location l, BufferedImage ss) {
		// Left Arm
				fb.buildFace(l, r.part(ss, 8 + 16 + 16, 16 + 32, 4, 4), 1, 4 + 4, 1 + 12,-3, false, true);
				fb.buildFace(l, r.part(ss, 24 + 16, 52, 4, 12), 2, 7 + 4, 12,-3, true, false);
				fb.buildFace(l, r.part(ss, 8 + 16 + 16-4, 16 + 32, 4, 4), 1, 4 + 4, 12 + 12,-3);
				fb.buildFace(l, r.part(ss, 12 + 16 + 16, 20 + 32, 4, 12), 0, 4 + 4, 12,-3, true, false);
				fb.buildFace(l, r.part(ss, 4 + 16 + 16, 20 + 32, 4, 12), 0, 4 + 4, 12, 0);
				//Left Sleeve
				fb.buildFace(l, r.part(ss, 8 + 16 + 16 + 16, 16 + 32, 4, 4), 1, 4 + 4, 1 + 12-1,-3, false, true);
				fb.buildFace(l, r.part(ss, 24 + 16 + 16, 52, 4, 12), 2, 7 + 4 + 1, 12,-3, true, false);
				fb.buildFace(l, r.part(ss, 8 + 16 + 16-4 + 16, 16 + 32, 4, 4), 1, 4 + 4, 12 + 12 + 1,-3);
				fb.buildFace(l, r.part(ss, 12 + 16 + 16 + 16, 20 + 32, 4, 12), 0, 4 + 4, 12,-3-1, true, false);
				fb.buildFace(l, r.part(ss, 4 + 16 + 16 + 16, 20 + 32, 4, 12), 0, 4 + 4, 12, 1);
	}

	private void head(FaceBuilder fb, Reader r, Location l, BufferedImage ss) {
		// Head
				fb.buildFace(l, r.part(ss, 16, 0, 8, 8), 1, 0, 1 + 24,-5, false, true);
				fb.buildFace(l, r.part(ss, 0, 8, 8, 8), 2, 0, 24,-5);
				fb.buildFace(l, r.part(ss, 16, 8, 8, 8), 2, 7, 24,-5, true, false);
				fb.buildFace(l, r.part(ss, 8, 0, 8, 8), 1, 0, 1 + 24 + 7,-5);
				fb.buildFace(l, r.part(ss, 24, 8, 8, 8), 0, 0, 24,-5, true, false);
				fb.buildFace(l, r.part(ss, 8, 8, 8, 8), 0, 0, 24, 2);
				//Hat
				fb.buildFace(l, r.part(ss, 32, 8, 8, 8), 2, -1, 24,-5);
				fb.buildFace(l, r.part(ss, 16 + 32, 8, 8, 8), 2, 7 + 1, 24,-5, true, false);
				fb.buildFace(l, r.part(ss, 8 + 32, 0, 8, 8), 1, 0, 1 + 24 + 7 + 1,-5);
				fb.buildFace(l, r.part(ss, 24 + 32, 8, 8, 8), 0, 0, 24,-5-1, true, false);
				fb.buildFace(l, r.part(ss, 8 + 32, 8, 8, 8), 0, 0, 24, 2 + 1);
	}
	
	private void right_arm_slim(FaceBuilder fb, Reader r, Location l, BufferedImage ss) {
		// Right Arm
				fb.buildFace(l, r.part(ss, 8 + 40-1, 16, 3, 4), 1, -4 + 1, 1 + 12,-3, false, true);
				fb.buildFace(l, r.part(ss, 40, 20, 4, 12), 2, -4 + 1, 12,-3);//Done
				fb.buildFace(l, r.part(ss, 8 + 40-4, 16, 3, 4), 1, -4 + 1, 12 + 12,-3);
				fb.buildFace(l, r.part(ss, 12 + 40-1, 20, 3, 12), 0, -4 + 1, 12,-3, true, false);
				fb.buildFace(l, r.part(ss, 4 + 40, 20, 3, 12), 0, -4 + 1, 12, 0);
				//Right Sleeve
				fb.buildFace(l, r.part(ss, 8 + 40-1, 16 + 16, 3, 4), 1, -4 + 1, 1 + 12-1,-3, false, true);
				fb.buildFace(l, r.part(ss, 40, 20 + 16, 4, 12), 2, -4 + 1-1, 12,-3);//Done
				fb.buildFace(l, r.part(ss, 44, 16 + 16, 3, 4), 1, -4 + 1, 12 + 12 + 1,-3);//Unsure
				fb.buildFace(l, r.part(ss, 12 + 40-1, 20 + 16, 3, 12), 0, -4 + 1, 12,-3-1, true, false);
				fb.buildFace(l, r.part(ss, 4 + 40, 20 + 16, 3, 12), 0, -4 + 1, 12, 1);
	}

	private void left_arm_slim(FaceBuilder fb, Reader r, Location l, BufferedImage ss) {
		// Left Arm
				fb.buildFace(l, r.part(ss, 8 + 16 + 16-1, 16 + 32, 3, 4), 1, 4 + 4, 1 + 12,-3, false, true);
				fb.buildFace(l, r.part(ss, 24 + 16-1, 52, 4, 12), 2, 7 + 4 -1, 12,-3, true, false);//Done
				fb.buildFace(l, r.part(ss, 8 + 16 + 16-4, 16 + 32, 3, 4), 1, 4 + 4, 12 + 12,-3);
				fb.buildFace(l, r.part(ss, 12 + 16 + 16-1, 20 + 32, 3, 12), 0, 4 + 4, 12,-3, true, false);
				fb.buildFace(l, r.part(ss, 4 + 16 + 16, 20 + 32, 3, 12), 0, 4 + 4, 12, 0);
				//Left Sleeve
				fb.buildFace(l, r.part(ss, 8 + 16 + 16 + 16-1, 16 + 32, 3, 4), 1, 4 + 4, 1 + 12-1,-3, false, true);
				fb.buildFace(l, r.part(ss, 24 + 16 + 16-1, 52, 4, 12), 2, 7 + 4 + 1 -1, 12,-3, true, false);//Done
				fb.buildFace(l, r.part(ss, 8 + 16 + 16-4 + 16, 16 + 32, 3, 4), 1, 4 + 4, 12 + 12 + 1,-3);
				fb.buildFace(l, r.part(ss, 12 + 16 + 16 + 16-1, 20 + 32, 3, 12), 0, 4 + 4, 12,-3-1, true, false);
				fb.buildFace(l, r.part(ss, 4 + 16 + 16 + 16, 20 + 32, 3, 12), 0, 4 + 4, 12, 1);
	}

	private boolean isSlimSkin(BufferedImage xx) {
		if (xx.getWidth() < 64 || xx.getHeight() < 64) {
			return false;
		}
		// Checks the top left hand side of the right and left arms to see if they are transparent (if they are it's probably slim)
		if (new Color(xx.getRGB(54, 20), true).getAlpha() < 255) {
			return true;
		}

		if (new Color(xx.getRGB(46, 52), true).getAlpha() < 255) {
			return true;
		}

		return false;
	}

	/**
	 * Warning: the original "xx" image should not be changed (other than to convert to the new skin format). ONLY modify the ss image.
	 * @param l
	 * @param xx
     */
	private void makeStatueSchematic(Location l, BufferedImage xx) {
		initialize();

		if (xx.getHeight() < 64) {
			makeStatueSchematic(l, LegacyConverter.convertLegacy(xx, false));
			return;
		}
		if (isSlimSkin(xx)) {
			makeSlimStatueSchematic(l, xx);
			return;
		}
		
		BufferedImage ss = preProcessing(xx);

		//0 is vertical |_
		//1 is flat |-
		//2 is sideways |_
		//Bottom, Right, Left, Top, Back, Front
		//Right Leg
		FaceBuilder fb = new FaceBuilder(s);
		Reader r = new Reader();
		
		//see if the player wants us to just build one part
		boolean builtSomething = false;
		Set<String> flags = params.keySet();
		if (flags.contains("right_leg")) {
			right_leg(fb, r, l, ss);
			builtSomething = true;
		}

		if (flags.contains("left_leg")) {
			left_leg(fb, r, l, ss);
			builtSomething = true;
		}

		if (flags.contains("body")) {
			body(fb, r, l, ss);
			builtSomething = true;
		}

		if (flags.contains("right_arm")) {
			right_arm(fb, r, l, ss);
			builtSomething = true;
		}

		if (flags.contains("left_arm")) {
			left_arm(fb, r, l, ss);
			builtSomething = true;
		}

		if (flags.contains("head")) {
			head(fb, r, l, ss);
			builtSomething = true;
		}
		
		//if the player does not specify what part to build, just build the whole thing
		if (!builtSomething) {
			right_leg(fb, r, l, ss);
			left_leg(fb, r, l, ss);
			body(fb, r, l, ss);
			right_arm(fb, r, l, ss);
			left_arm(fb, r, l, ss);
			head(fb, r, l, ss);
		}
	}

	private void makeSlimStatueSchematic(Location l, BufferedImage xx) {
		initialize();
		
		if (xx.getHeight() < 64) {
			makeSlimStatueSchematic(l, LegacyConverter.convertLegacy(xx, true));
			return;
		}
		
		BufferedImage ss = preProcessing(xx);
		
		//0 is vertical |_
		//1 is flat |-
		//2 is sideways |_
		//Bottom, Right, Left, Top, Back, Front
		//Right Leg
		FaceBuilder fb = new FaceBuilder(s);
		Reader r = new Reader();
		
		//see if the player wants us to just build one part
		boolean builtSomething = false;
		Set<String> flags = params.keySet();
		if (flags.contains("right_leg")) {
			right_leg(fb, r, l, ss);
			builtSomething = true;
		}

		if (flags.contains("left_leg")) {
			left_leg(fb, r, l, ss);
			builtSomething = true;
		}

		if (flags.contains("body")) {
			body(fb, r, l, ss);
			builtSomething = true;
		}

		if (flags.contains("right_arm")) {
			right_arm_slim(fb, r, l, ss);
			builtSomething = true;
		}

		if (flags.contains("left_arm")) {
			left_arm_slim(fb, r, l, ss);
			builtSomething = true;
		}

		if (flags.contains("head")) {
			head(fb, r, l, ss);
			builtSomething = true;
		}
		
		//if the player does not specify what part to build, just build the whole thing
		if (!builtSomething) {
			right_leg(fb, r, l, ss);
			left_leg(fb, r, l, ss);
			body(fb, r, l, ss);
			right_arm_slim(fb, r, l, ss);
			left_arm_slim(fb, r, l, ss);
			head(fb, r, l, ss);
		}
	}
	
	private void makeLegacyStatueSchematic(Location l, BufferedImage ss) {
		makeStatueSchematic(l, LegacyConverter.convertLegacy(ss, false));
	}

	protected LinkedHashMap<String, Float> getParams() {
		return params;
	}
	
	protected Schematic getSchematic() {
		return s;
	}

	public void setImage(BufferedImage bi) {
		this.bi = bi;
	}

	public BufferedImage getImage() {
		return bi;
	}
}