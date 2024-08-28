package com.megatrex4;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.util.Identifier;

public class PathSpeed implements ModInitializer {
	public static final String MOD_ID = "pathspeed";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static Map<Identifier, Double> blockSpeedMap = new HashMap<>();

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing PathSpeed Mod");

		loadOrCreateConfig();

		MovementSpeedHandler.register();

		ServerLifecycleEvents.SERVER_STARTED.register(server -> loadOrCreateConfig());

		MovementSpeedCommands.register();
	}

	public void loadOrCreateConfig() {
		File configFile = new File("config/SpeedPath.json");

		if (!configFile.exists()) {
			LOGGER.info("Config file not found, creating with default values...");
			createDefaultConfig(configFile);
		}

		loadConfig(configFile);
	}

	private void createDefaultConfig(File configFile) {
		blockSpeedMap.put(Identifier.of("minecraft", "dirt_path"), 1.5);
		blockSpeedMap.put(Identifier.of("namespace", "block_name"), 1.0);

		try (FileWriter writer = new FileWriter(configFile)) {
			GSON.toJson(blockSpeedMap, writer);
			LOGGER.info("Default config created");
		} catch (Exception e) {
			LOGGER.error("Failed to create default config", e);
		}
	}

	private void loadConfig(File configFile) {
		try (FileReader reader = new FileReader(configFile)) {
			Type mapType = new TypeToken<Map<String, Double>>(){}.getType();
			Map<String, Double> configData = GSON.fromJson(reader, mapType);

			blockSpeedMap.clear();
			configData.forEach((key, value) -> blockSpeedMap.put(Identifier.of(key), value));

			LOGGER.info("Loaded block speed configurations");
		} catch (Exception e) {
			LOGGER.error("Failed to load block speed configurations", e);
		}
	}
	public static double getSpeedMultiplier(Identifier blockId) {
		return blockSpeedMap.getOrDefault(blockId, 1.0);
	}
}