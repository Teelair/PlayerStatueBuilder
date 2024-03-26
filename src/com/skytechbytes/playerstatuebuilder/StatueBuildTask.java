package com.skytechbytes.playerstatuebuilder;

import com.skytechbytes.playerstatuebuilder.builder.StatueMaker;
import org.bukkit.scheduler.BukkitRunnable;

public class StatueBuildTask extends BukkitRunnable {
	private final String identifier;
	private final StatueMaker callback;
	public StatueBuildTask(String identifier, StatueMaker callback) {
		this.identifier = identifier;
		this.callback = callback;
	}

	@Override
	public void run() {
		try {
			callback.setImage(Util.getSkinImage(identifier));
		} catch (Exception e) {
			Log.log(e.getMessage());
		}

		callback.runTask(PlayerStatueBuilder.instance);
	}
}
