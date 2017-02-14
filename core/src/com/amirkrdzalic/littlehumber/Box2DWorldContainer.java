package com.amirkrdzalic.littlehumber;

import com.amirkrdzalic.littlehumber.Screens.GameScreen;
import com.amirkrdzalic.littlehumber.Sprites.Brick;
import com.amirkrdzalic.littlehumber.Sprites.LittleEnemy;
import com.amirkrdzalic.littlehumber.Sprites.Money;
import com.amirkrdzalic.littlehumber.Sprites.WinBox;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Created by User on 12/21/2016.
 */
public class Box2DWorldContainer {

    private Array<LittleEnemy> littleEnemies;

    public Array<LittleEnemy> getLittleEnemies() {
        return littleEnemies;
    }

    public Box2DWorldContainer(GameScreen screen)
    {
        //world
        World world = screen.getWorld();
        //need the Tiled Map we got
        TiledMap map = screen.getMap();
        //body definition
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        //body is going to be something else anyways
        Body body;



        //ground
        for(MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / LittleHumber.pixalPerM, (rect.getY() + rect.getHeight() / 2) / LittleHumber.pixalPerM);

            body = world.createBody(bodyDef);
            shape.setAsBox(rect.getWidth() / 2 / LittleHumber.pixalPerM, rect.getHeight() / 2 / LittleHumber.pixalPerM);
            fixtureDef.shape = shape;
            body.createFixture(fixtureDef);
        }
        //pipes
        //get layer and all objects in it
        for(MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class))
        {
            //rect object
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            //define the body def to a static body, not dynamic, never moves
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rect.getX() + rect.getWidth() / 2) / LittleHumber.pixalPerM, (rect.getY() + rect.getHeight() / 2) / LittleHumber.pixalPerM);
            //create the body into the world
            body = world.createBody(bodyDef);
            //make the shape
            shape.setAsBox(rect.getWidth() / 2 / LittleHumber.pixalPerM, rect.getHeight() / 2 / LittleHumber.pixalPerM);
            //make the fixture def for collision to the shape
            fixtureDef.shape = shape;
            //what is it equal to?
            fixtureDef.filter.categoryBits = LittleHumber.OBJECT_BIT;
            //make the body create a fixture and set it to the fixDef
            body.createFixture(fixtureDef);
        }

        //bricks
        for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Brick(screen, rect);
        }

        //money
        for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Money(screen, rect);
        }

        //creating enemies according to the .TILE FILE
        littleEnemies = new Array<LittleEnemy>();
        for(MapObject object: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            littleEnemies.add(new LittleEnemy(screen, rect.getX() / LittleHumber.pixalPerM, rect.getY() / LittleHumber.pixalPerM));
        }
        //for win box
        for(MapObject object: map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new WinBox(screen, rect);
        }
    }
}
