package com.skytechbytes.playerstatuebuilder.builder;

import java.util.HashMap;
import java.util.Stack;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.skytechbytes.playerstatuebuilder.Log;
/**
 * 
 * @author SkyTechBytes
 * "This is by far the worst code I've ever seen"
 * "But it works"
 *
 */
public class Schematic {
	public static final Stack<Schematic> history = new Stack<>();

	/*
	 * Hashception
	 */
	private final HashMap<Integer, HashMap<Integer, HashMap<Integer, MaterialHolder>>> matrix = new HashMap<>();
	private final World w;
	private int count = 0; //Count of non-air blocks
	public Schematic (World w) {
		this.w = w;
	}

	int maxX = Integer.MIN_VALUE;
	int maxY = Integer.MIN_VALUE;
	int maxZ = Integer.MIN_VALUE;
	int minX = Integer.MAX_VALUE;
	int minY = Integer.MAX_VALUE;
	int minZ = Integer.MAX_VALUE;

	/**
	 * You can put in world coordinates or small relative coordinates- it really shouldn't matter
	 * @param x
	 * @param y
	 * @param z
	 * @param m
	 */
	public void setBlockAt(int x, int y, int z, Material m) {
		maxX = Math.max(maxX, x);
		maxY = Math.max(maxY, y);
		maxZ = Math.max(maxZ, z);
		minX = Math.min(minX, x);
		minY = Math.min(minY, y);
		minZ = Math.min(minZ, z);

		matrix.computeIfAbsent(z, unused -> new HashMap<>())
				.computeIfAbsent(y, unused -> new HashMap<>())
				.put(x, new MaterialHolder(Material.AIR));

		Material previous = matrix.get(z).get(y).get(x).getMaterial();
		MaterialHolder replacement = new MaterialHolder(m);
		matrix.get(z).get(y).put(x, replacement);
		if (previous == Material.AIR && replacement.getMaterial() != Material.AIR) {
			count++;
		}
	}

	public void createSchematic(boolean eraseMode, boolean replaceAirOnly) {
		for (int keyZ : matrix.keySet()) {
			for (int keyY : matrix.get(keyZ).keySet()) {
				for (int keyX : matrix.get(keyZ).get(keyY).keySet()) {
					MaterialHolder mat = matrix.get(keyZ).get(keyY).get(keyX);
					//Make sure we aren't doing anything we shouldn't

					if (mat.getMaterial().equals(Material.AIR)) {
						continue;
					}

					//WARNING: DO NOT CHANGE
					Block b = w.getBlockAt(new Location(w, keyX, keyY, keyZ));
					if (replaceAirOnly) {
						//We only replace AIR blocks in the world!
						if (!b.getType().equals(Material.AIR)) {
							continue;
						}
					}

					/*
					 * So if the material previously successfully changed and you want to erase and the material is the same
					 * material it was changed to previously, then we will revert it back to an AIR block, which it must
					 * have been (when building the statue, the plugin does not modify non-air blocks)
					 * If this were somehow triggered too early, nothing would happen as all mat.isSuccess() is false by default
					 * Please note that you'll also need the "override" permission to overwrite any non-air blocks
					 */
					if (eraseMode &&
							mat.isSuccess() &&
							b.getBlockData().getMaterial().equals(matrix.get(keyZ).get(keyY).get(keyX).getMaterial())) {
						b.setType(Material.AIR);
						continue;
					} else if (eraseMode) {
						continue;
					}

					b.setType(matrix.get(keyZ).get(keyY).get(keyX).getMaterial());
					mat.setSuccess(true);
				}
			}
		}

		if (!eraseMode) {
			Log.log("Statue Created");

			Schematic.history.add(this);
		}
	}

	public HashMap<Integer, HashMap<Integer, HashMap<Integer, MaterialHolder>>> getMatrix() {
		return matrix;
	}

	public int getCount() {
		return count;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getMaxZ() {
		return maxZ;
	}

	public int getMinX() {
		return minX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMinZ() {
		return minZ;
	}

	public World getWorld() {
		return this.w;
	}
}
