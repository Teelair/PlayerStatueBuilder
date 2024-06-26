package com.skytechbytes.playerstatuebuilder.builder;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;

import com.jhlabs.image.ContrastFilter;
import com.jhlabs.image.HSBAdjustFilter;
import com.jhlabs.image.PosterizeFilter;
import com.jhlabs.image.SaturationFilter;

public class ImageUtil {
	public static BufferedImage overlayImage(BufferedImage below, BufferedImage above) {
		BufferedImage newImage = new BufferedImage(below.getWidth(), below.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		g.drawImage(below, 0, 0, null);
	    g.drawImage(above, (below.getWidth() - above.getWidth()) / 2,
	        (below.getHeight() - above.getHeight()) / 2, null);
	    g.dispose();

	    return newImage;
	}

	/**
	 * Thanks Stackoverflow
	 * @param source
	 * @return
	 */
	public static BufferedImage deepCopy(BufferedImage source) {
		BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
	    Graphics2D g = b.createGraphics();
	    g.drawImage(source, 0, 0, null);
	    g.dispose();
	    return b;
	}

	public static BufferedImage applyFilters(BufferedImage bi, LinkedHashMap<String, Float> params) {
		for (String key : params.keySet()) {
			if (key.equalsIgnoreCase("contrast")) {
				checkParams(key, params.get(key));
				ContrastFilter cf = new ContrastFilter();
				cf.setContrast(params.get(key));
				bi = cf.filter(bi, null);
			}

			if (key.equalsIgnoreCase("brightness")) {
				checkParams(key, params.get(key));
				ContrastFilter cf = new ContrastFilter();
				cf.setBrightness(params.get(key));
				bi = cf.filter(bi, null);
			}

			if (key.equalsIgnoreCase("saturation")) {
				checkParams(key, params.get(key));
				SaturationFilter sf = new SaturationFilter();
				sf.setAmount(params.get(key));
				bi = sf.filter(bi,  null);
			}

			if (key.equalsIgnoreCase("hue")) {
				checkParams(key, params.get(key));
				HSBAdjustFilter hsb = new HSBAdjustFilter();
				hsb.setHFactor(params.get(key));
				bi = hsb.filter(bi, null);
			}

			if (key.equalsIgnoreCase("posterize")) {
				PosterizeFilter p = new PosterizeFilter();
				p.setNumLevels(Math.round(params.get(key)));
				bi = p.filter(bi, null);
			}
		}
		return bi;
	}

	private static void checkParams(String name, Float in) throws IllegalArgumentException {
		if (in == null || in < 0 || in > 1) {
			throw new IllegalArgumentException("Parameter " + name + " must be in the range 0 to 1");
		}
	}
}
