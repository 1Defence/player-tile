package com.playertile;

import java.awt.*;
import javax.inject.Inject;
import net.runelite.api.Client;

import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class PlayerTileOverlay extends Overlay
{

    private final Client client;
    private final PlayerTilePlugin plugin;
    private final PlayerTileConfig config;

    private static final int STROKE_WIDTH = 2;

    @Inject
    PlayerTileOverlay(Client client, PlayerTilePlugin plugin, PlayerTileConfig config)
    {
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        this.client = client;
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {

        drawTile(graphics, config.getTileColor(), config.getOutlineOpacity(), config.getFillOpacity());
        return null;

    }

    private void drawTile(Graphics2D graphics, Color color, int outlineAlpha, int fillAlpha)
    {

        if(plugin.inBlockedRegion || plugin.inPVPWorld)
            return;
        //prevent abuse of the plugin.

        if(outlineAlpha == 0 && fillAlpha == 0)
            return;
        //neither an outline or fill specified

        if(client.getLocalPlayer() == null || client.getLocalPlayer().getWorldLocation() == null)
            return;
        //player or players world location aren't set

        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
        LocalPoint lp = LocalPoint.fromWorld(client, playerLocation);
        if (lp == null)
            return;
        //local area to draw the tile

        Polygon poly = Perspective.getCanvasTilePoly(client, lp);
        if (poly == null)
            return;
        //tile area

        graphics.setStroke(new BasicStroke(STROKE_WIDTH));

        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), outlineAlpha));
        graphics.draw(poly);
        //drawing the tile outline

        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), fillAlpha));
        graphics.fill(poly);
        //filling the tile outline

    }

}
