package com.amirkrdzalic.littlehumber.Sprites;

import com.amirkrdzalic.littlehumber.LittleHumber;
import com.amirkrdzalic.littlehumber.Screens.GameScreen;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;



/**
 * Created by User on 12/21/2016.
 */
public abstract class TileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tiledMapTile;
    protected Rectangle rectBounds;
    protected Body body;
    protected Fixture fixture;
    protected GameScreen screen;

    public TileObject(GameScreen gameScreen, Rectangle rectBounds)
    {
        this.screen = gameScreen;
        this.world = gameScreen.getWorld();
        this.map = gameScreen.getMap();
        this.rectBounds = rectBounds;

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set((rectBounds.getX() + rectBounds.getWidth() / 2) / LittleHumber.pixalPerM, (rectBounds.getY() + rectBounds.getHeight() / 2) / LittleHumber.pixalPerM);

        body = world.createBody(bodyDef);

        shape.setAsBox(rectBounds.getWidth() / 2 / LittleHumber.pixalPerM, rectBounds.getHeight() / 2 / LittleHumber.pixalPerM);
        fixtureDef.shape = shape;
        fixture = body.createFixture(fixtureDef);

    }

    public abstract void onHeadHit();

    public void setCategoryFilter (short filterBIT)
    {
        Filter filter = new Filter();
        filter.categoryBits = filterBIT;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell()
    {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x * LittleHumber.pixalPerM / 16), (int)(body.getPosition().y * LittleHumber.pixalPerM / 16));
    }

}
