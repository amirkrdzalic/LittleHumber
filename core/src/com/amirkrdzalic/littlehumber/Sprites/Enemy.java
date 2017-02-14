package com.amirkrdzalic.littlehumber.Sprites;

import com.amirkrdzalic.littlehumber.Screens.GameScreen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by User on 12/24/2016.
 */
public abstract class Enemy extends Sprite {

    protected World world;
    protected GameScreen gameScreen;
    public Body body;
    public Vector2 vel;

    public Enemy(GameScreen gameScreen, float x, float y)
    {
        //set it to the world of current object
        this.world = gameScreen.getWorld();
        this.gameScreen = gameScreen;
        //rest of variables and methods are made inside the class that EXTENDS ENEMY....
        setPosition(x, y);
        defineEnemy();
        vel = new Vector2(1, 0);
    }

    protected abstract void defineEnemy();
    public abstract void update(float deltaT);
    public abstract void hitOnHead();

    public void reverseVel(boolean x, boolean y)
    {
        if (x)
        {
            vel.x = -vel.x;
        }
        if (y)
        {
            vel.y = -vel.y;
        }
    }

}
