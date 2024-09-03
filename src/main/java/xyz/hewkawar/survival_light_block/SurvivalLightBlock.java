package xyz.hewkawar.survival_light_block;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SurvivalLightBlock implements ModInitializer {
	public static final String MOD_ID = "survival-light-block";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		SurvivalLightBlockClient.ModConfig.loadConfig();

		LOGGER.info("Hello Fabric world!");
	}
}