package com.playertile;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.WorldType;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
		name = "Player Tile",
		description = "Display Server tile of the players exact location"
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

	public boolean inPVPWorld = false;
	public boolean inBlockedRegion = false;

	private final int[] BLOCKED_REGIONS = new int[] {-1};//add to list if specific regions need to be blocked.

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(tileOverlay);
		CheckForIllegalAreas();
		log.info("Player Tile started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(tileOverlay);
		log.info("Player Tile stopped!");
	}

	void CheckForIllegalAreas(){
		if(client.getGameState() == GameState.LOGGED_IN || client.getGameState() == GameState.LOADING)
		{
			inPVPWorld = client.getWorldType().contains(WorldType.PVP);

			inBlockedRegion = false;
			Player localPlayer = client.getLocalPlayer();
			if(localPlayer != null)
			{
				int currentRegion = localPlayer.getWorldLocation().getRegionID();
				for (int region : BLOCKED_REGIONS)
				{
					if(region == currentRegion)
					{
						inBlockedRegion = true;
						break;
					}
				}
			}
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		CheckForIllegalAreas();
	}

	@Provides
	PlayerTileConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PlayerTileConfig.class);
	}
}
