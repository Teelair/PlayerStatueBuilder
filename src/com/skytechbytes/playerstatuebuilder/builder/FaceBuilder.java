package com.skytechbytes.playerstatuebuilder.builder;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

/**
 * 
 * @author SkyTechBytes This is where the faces of the statue are built (the
 *         walls)
 *
 */
public class FaceBuilder {
	public static int master_orientation = 2;
	public static int minor_orientation = 3;
	public final Schematic statue;

	public FaceBuilder(Schematic s) {
		this.statue = s;
	}

	public void buildFace(Location location, Material[][] matrix, int orientation, int xOffset, int yOffset, int zOffset) {
		buildFace(location, matrix, orientation, xOffset, yOffset, zOffset, false, false);
	}

	public void buildFace(Location location, Material[][] matrix, int orientation, int xOffset, int yOffset, int zOffset, boolean flipH, boolean flipV) {
		if (matrix == null) {
			return;
		}

		if (flipH) {
			BuildUtils.flipHorizontal(matrix);
		}

		if (flipV) {
			BuildUtils.flipVertical(matrix);
		}

		World w = location.getWorld();
		for (int y = 0; y < matrix.length; y++) {
			for (int x = 0; x < matrix[y].length; x++) {
				if (master_orientation == 0) {
					switch (orientation) {
						case 0 -> gba(location, x + xOffset, -y + matrix.length + yOffset, zOffset, matrix[y][x]);
						case 1 -> gba(location, x + xOffset, yOffset, y + zOffset, matrix[y][x]);
						case 2 -> gba(location, xOffset, -y + matrix.length + yOffset, x + zOffset, matrix[y][x]);
					}
				} else if (master_orientation == 1) {
					switch (orientation) {
						case 0 -> gba(location, zOffset, xOffset + x, -y + matrix.length + yOffset, matrix[y][x]);
						case 1 -> gba(location, y + zOffset, xOffset + x, yOffset, matrix[y][x]);
						case 2 -> gba(location, x + zOffset, xOffset, -y + matrix.length + yOffset, matrix[y][x]);
					}
				} else if (master_orientation == 2) {
					switch (orientation) {
						case 0 -> gba(location, -y + matrix.length + yOffset, zOffset, xOffset + x, matrix[y][x]);
						case 1 -> gba(location, yOffset, y + zOffset, xOffset + x, matrix[y][x]);
						case 2 -> gba(location, -y + matrix.length + yOffset, x + zOffset, xOffset, matrix[y][x]);
					}
				}
			}
		}
	}

	private void gba(Location location, int xOffset, int yOffset, int zOffset, Material material) {
		switch (minor_orientation) {
			case 0 -> ll(location.getBlockX() + xOffset, location.getBlockY() + yOffset, location.getBlockZ() + zOffset, material);
			case 1 -> ll(location.getBlockX() - xOffset, location.getBlockY() + yOffset, location.getBlockZ() - zOffset, material);
			case 2 -> ll(location.getBlockX() + zOffset, location.getBlockY() + yOffset, location.getBlockZ() - xOffset, material); // fixed by swapping +xOffset to -xOffset
			case 3 -> ll(location.getBlockX() - zOffset, location.getBlockY() + yOffset, location.getBlockZ() + xOffset, material); // fixed by swapping -xOffset to +xOffset
		}
	}

	private void ll(int x, int y, int z, Material material) {
		statue.setBlockAt(x, y, z, material);
	}
}