package com.amirkrdzalic.littlehumber.Sprites;

import com.amirkrdzalic.littlehumber.LittleHumber;
import com.amirkrdzalic.littlehumber.Screens.GameScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by User on 12/21/2016.
 */
public class Brick extends TileObject{

    public Brick(GameScreen gameScreen, Rectangle rectBounds)
    {
        super(gameScreen, rectBounds);
        fixture.setUserData(this);
        setCategoryFilter(LittleHumber.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {

        if (getCell() != null)
        {
            LittleHumber.manager.get("music/bump.wav", Sound.class).play();
        }
        setCategoryFilter(LittleHumber.DESTROYED_BIT);
        getCell().setTile(null);

    }

}
