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
        if(client.getLocalPlayer() == null || client.getLocalPlayer().getWorldLocation() == null)
            return null;

        drawTile(graphics, client.getLocalPlayer().getWorldLocation(), config.getTileColor(), 2, config.getOutlineOpacity(), config.getFillOpacity());
        return null;
    }

    private void drawTile(Graphics2D graphics, WorldPoint point, Color color, int strokeWidth, int outlineAlpha, int fillAlpha) {

        if(outlineAlpha == 0 && fillAlpha == 0)
            return;

        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
        if (point.distanceTo(playerLocation) >= 32)
            return;

        LocalPoint lp = LocalPoint.fromWorld(client, point);
        if (lp == null)
            return;

        Polygon poly = Perspective.getCanvasTilePoly(client, lp);
        if (poly == null)
            return;

        graphics.setStroke(new BasicStroke(strokeWidth));

        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), outlineAlpha));
        graphics.draw(poly);

        graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), fillAlpha));
        graphics.fill(poly);

    }

}
