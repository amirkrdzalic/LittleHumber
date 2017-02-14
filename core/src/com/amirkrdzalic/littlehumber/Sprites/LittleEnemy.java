package com.amirkrdzalic.littlehumber.Sprites;

import com.amirkrdzalic.littlehumber.LittleHumber;
import com.amirkrdzalic.littlehumber.Scenes.GameHUD;
import com.amirkrdzalic.littlehumber.Screens.GameScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

/**
 * Created by User on 12/24/2016.
 */
public class LittleEnemy extends  Enemy{

    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;

    private boolean setToDie;
    private boolean Dead;

    public LittleEnemy(GameScreen gameScreen, float x, float y) {
        //parent
        super(gameScreen, x, y);
        frames = new Array<TextureRegion>();
        //the enemy has 2 frames of different (pictures) sprites to switch between
        for (int i = 0; i < 2; i++)
        {
            //find the region in the sprite sheet
            frames.add(new TextureRegion(gameScreen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        }
        //animation from the LIBGDX, needs which textures to cycle through and how fast.
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        //default method to set the position and size of sprite when drawn
        setBounds(getX(), getY(), 16 / LittleHumber.pixalPerM, 16 / LittleHumber.pixalPerM);

        //daeth
        setToDie = false;
        Dead = false;
    }

    public void update(float deltaT)
    {
        stateTime += deltaT;

        //dead check
        if (setToDie && !Dead)
        {
            world.destroyBody(body);
            Dead = true;
            //set to dead enemy
            setRegion(new TextureRegion(gameScreen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
            stateTime = 0;
        }
        else if (!Dead)
        {
            //set its Velocity to move
            body.setLinearVelocity(vel);
            //position of where it will be drawn
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 0.07f);
            //get the texture region and make the animation cycle through the time of StateTime and set the loop to TRUE
            setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));
        }
    }

    @Override
    protected void defineEnemy() {//is called for the enemy in parent class constructor

        //create body inside world
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        //set the rigid body to a circle that big...
        shape.setRadius(6 / LittleHumber.pixalPerM);
        //what is it for collision checking
        fixtureDef.filter.categoryBits = LittleHumber.ENEMY_BIT;
        //what can it collide with
        fixtureDef.filter.maskBits = LittleHumber.GROUND_BIT
                | LittleHumber.LITTLEGUY_BIT
                | LittleHumber.BRICK_BIT
                | LittleHumber.MONEY_BIT
                | LittleHumber.OBJECT_BIT
                | LittleHumber.ENEMY_BIT;

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);

        //create head for killing
        //just some verticies that have collision
        //little guy has samething
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 7).scl(1 / LittleHumber.pixalPerM);
        vertice[1] = new Vector2(5, 7).scl(1 / LittleHumber.pixalPerM);
        vertice[2] = new Vector2(-5, 5).scl(1 / LittleHumber.pixalPerM);
        vertice[3] = new Vector2(5, 5).scl(1 / LittleHumber.pixalPerM);
        head.set(vertice);

        fixtureDef.shape = head;
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.categoryBits = LittleHumber.ENEMY_HEADBIT;
        body.createFixture(fixtureDef).setUserData(this);
    }

    public void draw(Batch batch)
    {
        if (!Dead || stateTime < 1)
        {
            super.draw(batch);
        }
    }

    @Override
    public void hitOnHead() {
        setToDie = true;
        GameHUD.addScore(200);
        LittleHumber.manager.get("music/coin.wav", Sound.class).play();
    }
}
