package com.amirkrdzalic.littlehumber.Sprites;

import com.amirkrdzalic.littlehumber.LittleHumber;
import com.amirkrdzalic.littlehumber.Scenes.GameHUD;
import com.amirkrdzalic.littlehumber.Screens.GameScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by User on 12/21/2016.
 */
public class Money extends TileObject{

    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;

    public Money(GameScreen gameScreen, Rectangle rectBounds)
    {
        super(gameScreen, rectBounds);
        fixture.setUserData(this);
        setCategoryFilter(LittleHumber.MONEY_BIT);
        tileSet = map.getTileSets().getTileSet("MarioTileSet");
    }

    @Override
    public void onHeadHit() {

        if (getCell().getTile().getId() == BLANK_COIN)
        {
            LittleHumber.manager.get("music/bump.wav", Sound.class).play();
        }
        else
        {
            GameHUD.addScore(200);
            LittleHumber.manager.get("music/coin.wav", Sound.class).play();
            getCell().setTile(tileSet.getTile(BLANK_COIN));
        }
    }


}