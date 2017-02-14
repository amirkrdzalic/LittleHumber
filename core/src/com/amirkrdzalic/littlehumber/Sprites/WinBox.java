package com.amirkrdzalic.littlehumber.Sprites;

import com.amirkrdzalic.littlehumber.LittleHumber;
import com.amirkrdzalic.littlehumber.Screens.GameScreen;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by User on 12/29/2016.
 */
public class WinBox extends TileObject {

    public WinBox(GameScreen gameScreen, Rectangle rectBounds)
    {
        super(gameScreen, rectBounds);
        fixture.setUserData(this);
        setCategoryFilter(LittleHumber.ITEM_BIT);
    }

    @Override
    public void onHeadHit() {

    }
}
