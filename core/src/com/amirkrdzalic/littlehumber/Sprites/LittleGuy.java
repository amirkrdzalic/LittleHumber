package com.amirkrdzalic.littlehumber.Sprites;

import com.amirkrdzalic.littlehumber.LittleHumber;
import com.amirkrdzalic.littlehumber.Screens.GameScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Created by User on 12/21/2016.
 */
public class LittleGuy extends Sprite {
    public World world;
    public Body body;
    private GameScreen gameScreen;

    //character
    private TextureRegion littleGuyStand;
    private TextureRegion littleGuyDead;
    private Animation littleguyRun;
    private Animation littleguyJump;
    //Array<TextureRegion> frames;

    //sprite states
    public enum State
    {
        JUMPING,
        FALLING,
        STANDING,
        RUNNING,
        DEAD,
        WON
    };
    public State currentState;
    public State prevState;

    private boolean runRight;
    private float stateTimer;

    private boolean LisDead;
    private boolean hasWon;

    //constructor
    public LittleGuy(GameScreen gameScreen)
    {
        //super(gameScreen.getAtlas().findRegion("little"));
        this.gameScreen = gameScreen;
        this.world = gameScreen.getWorld();
        currentState = State.STANDING;
        prevState = State.STANDING;
        stateTimer = 0;
        runRight = true;

        //array of textures to pass to the constructor
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i < 4; i++)
        {
            frames.add(new TextureRegion(gameScreen.getAtlas().findRegion("little"), i * 16, 0, 16, 16));
        }
        littleguyRun = new Animation(0.1f, frames);
        littleguyJump = new Animation(0.1f, frames);
        //littleGuyStand = new TextureRegion(getTexture(), 115, 35, 16, 16);
        littleGuyStand = new TextureRegion(gameScreen.getAtlas().findRegion("little"), 0, 0, 16, 16);
        littleGuyDead = new TextureRegion(gameScreen.getAtlas().findRegion("little"), 96, 0, 16, 16);

        defineLittleGuy();
        setBounds(0, 0, 16 / LittleHumber.pixalPerM, 17 / LittleHumber.pixalPerM);
        //texture and coordinates to specified region
        setRegion(littleGuyStand);
    }

    public void defineLittleGuy()
    {
        //create body inside world
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / LittleHumber.pixalPerM, 32 / LittleHumber.pixalPerM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / LittleHumber.pixalPerM);
        fixtureDef.filter.categoryBits = LittleHumber.LITTLEGUY_BIT;
        //what he can collide with
        fixtureDef.filter.maskBits = LittleHumber.GROUND_BIT |
            LittleHumber.BRICK_BIT |
            LittleHumber.MONEY_BIT |
            LittleHumber.OBJECT_BIT |
            LittleHumber.ENEMY_BIT |
            LittleHumber.ENEMY_HEADBIT |
            LittleHumber.ITEM_BIT;
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);

        //collision sensor for top head
        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / LittleHumber.pixalPerM, 6 / LittleHumber.pixalPerM), new Vector2(2 / LittleHumber.pixalPerM, 6 / LittleHumber.pixalPerM));
        fixtureDef.filter.categoryBits = LittleHumber.LITTLEGUY_HEADBIT;
        fixtureDef.shape = head;
        fixtureDef.isSensor = true;

        //create fix and attach to body, then set the custom user data to current object
        body.createFixture(fixtureDef).setUserData(this);

    }

    public void update(float deltaT)
    {
        if (LittleGuyisDead())
        {
            die();
        }
        if (LittleGuyhasWon())
        {
            win();
        }
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 0.07f);//getHeight()); = 0.17f
        setRegion(getFrame(deltaT));

    }

    public TextureRegion getFrame(float deltaT)
    {
        currentState = getState();

        TextureRegion region;

        switch (currentState)
        {
            case WON:
                region = littleGuyDead;
                break;
            case DEAD:
                region = littleGuyDead;
                break;
            case JUMPING:
                region = (TextureRegion) littleguyJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = (TextureRegion) littleguyRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
                default:
                    region = littleGuyStand;
                    break;
        }

        if ((body.getLinearVelocity().x < 0 || !runRight) && !region.isFlipX())
        {
            region.flip(true, false);
            runRight = false;
        }
        else if ((body.getLinearVelocity().x > 0 || runRight) && region.isFlipX())
        {
            region.flip(true, false);
            runRight = true;
        }

        stateTimer = currentState == prevState ? stateTimer + deltaT : 0;
        prevState = currentState;
        return region;
    }

    public State getState()
    {
        if (hasWon)
        {
            return State.WON;
        }
        else if (LisDead)
        {
            return State.DEAD;
        }
        else if (body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 && prevState == State.JUMPING))
        {
            return State.JUMPING;
        }
        else if (body.getLinearVelocity().y < 0)
        {
            return State.FALLING;
        }
        else if (body.getLinearVelocity().x != 0)
        {
            return State.RUNNING;
        }
        else {
            return State.STANDING;
        }
    }

    public void die()
    {
        if (!LittleGuyisDead())
        {
            LisDead = true;
            gameScreen.music.stop();
            LittleHumber.manager.get("music/losesong.mp3", Sound.class).play();
            Filter filter = new Filter();
            filter.maskBits = LittleHumber.DEAD_BIT;

            for (Fixture fixture : body.getFixtureList())
            {
                fixture.setFilterData(filter);
            }
            body.applyLinearImpulse(new Vector2(0f, 4f), body.getWorldCenter(), true);
        }
    }

    public void win()
    {
        if (!LittleGuyhasWon()) {
            //win stuff
            hasWon = true;
            gameScreen.music.stop();
            LittleHumber.manager.get("music/wonsong.mp3", Sound.class).play();
            body.applyLinearImpulse(new Vector2(0f, 3f), body.getWorldCenter(), true);
        }
    }

    public boolean LittleGuyhasWon() { return hasWon; }

    public boolean LittleGuyisDead()
    {
        return LisDead;
    }

    public float getStateTimer()
    {
        return stateTimer;
    }

    public void hit(Enemy enemy)
    {
        die();
        //maybe remove the para.
    }

}
