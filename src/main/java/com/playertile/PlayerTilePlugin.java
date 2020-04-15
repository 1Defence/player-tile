package com.playertile;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Player Tile"
)
public class PlayerTilePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private PlayerTileOverlay tileOverlay;

	@Inject
	private PlayerTileConfig config;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(tileOverlay);
		log.info("Player Tile started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(tileOverlay);
		log.info("Player Tile stopped!");
	}


	@Provides
	PlayerTileConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PlayerTileConfig.class);
	}
}
