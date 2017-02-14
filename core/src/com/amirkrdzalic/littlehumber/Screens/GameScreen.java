package com.amirkrdzalic.littlehumber.Screens;

import com.amirkrdzalic.littlehumber.Box2DWorldContainer;
import com.amirkrdzalic.littlehumber.LittleHumber;
import com.amirkrdzalic.littlehumber.Scenes.GameHUD;
import com.amirkrdzalic.littlehumber.Sprites.Enemy;
import com.amirkrdzalic.littlehumber.Sprites.LittleGuy;
import com.amirkrdzalic.littlehumber.WorldCollisionListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


/**
 * Created by User on 12/18/2016.
 */
public class GameScreen extends InputAdapter implements Screen{
    private LittleHumber game;
    //characters
    private LittleGuy littleGuy;
    //textures
    private TextureAtlas atlas;
    //sound
    public Music music;

    //camera
    private OrthographicCamera gameCamera;
    //viewport controlls the size of screen on which ever console...
    private Viewport gamePort;
    private GameHUD hud;

    //physics
    private World world;
    private Box2DDebugRenderer boxDR;
    //box2d world
    private Box2DWorldContainer container;

    //loads Map
    private TmxMapLoader mapLoader;
    //map object
    private TiledMap map;
    //renders map to screen
    private OrthogonalTiledMapRenderer mapRenderer;

    //constrctor
    public GameScreen(LittleHumber game)
    {
        super();
        this.game = game;
        atlas = new TextureAtlas("LittleGuyCharacters1.pack");
        //camera for character
        gameCamera = new OrthographicCamera();

        //virtual ratio
        gamePort = new FitViewport(LittleHumber.VW / LittleHumber.pixalPerM, LittleHumber.VH / LittleHumber.pixalPerM, gameCamera);
        hud = new GameHUD(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("littlehumberLEVEL01.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / LittleHumber.pixalPerM);
        //centre game on viewport to use more then just positive x and y axis
        gameCamera.position.set(gamePort.getWorldWidth() /2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10), true);
        boxDR = new Box2DDebugRenderer();
        container = new Box2DWorldContainer(this);

        //create player
        littleGuy = new LittleGuy(this);

        //collision stuf
        world.setContactListener(new WorldCollisionListener());

        music = LittleHumber.manager.get("music/backgroundmusic.wav", Music.class);
        music.setLooping(true);
        music.play();

        //enemy
        //enemy = new LittleEnemy(this, 10f, 0.16f);
    }

    public TextureAtlas getAtlas()
    {
        return atlas;
    }

    public void update(float deltaT)
    {
        keyInput(deltaT);

        //physics is done 60 times a second
        world.step(1 / 60f, 6, 2);
        //update camera and character
        littleGuy.update(deltaT);

        if (littleGuy.getX() > 29.0f)
        {
            littleGuy.die();
        }
        if (littleGuy.getY() / LittleHumber.pixalPerM < 0f)
        {
            littleGuy.die();
        }
        //enemy.update(deltaT);
        for (Enemy enemy : container.getLittleEnemies())
        {
            enemy.update(deltaT);
            //turning on enemies when close enough
            /*if (enemy.getX() < littleGuy.getX() + 224 / LittleHumber.pixalPerM)
            {
                enemy.body.setActive(true);
            }*/
        }

        //freeze if dead
        if (littleGuy.currentState != LittleGuy.State.DEAD && littleGuy.currentState != LittleGuy.State.WON) {
            gameCamera.position.x = littleGuy.body.getPosition().x;
        }

        gameCamera.update();
        //renders what camera sees
        mapRenderer.setView(gameCamera);

        hud.update(deltaT);
        if (hud.totalTime == 0)
        {
            littleGuy.die();
        }

    }

    //INPUT MOVEMENT
    public void keyInput(float deltaT)
    {
        //if not dead
        if (littleGuy.currentState != LittleGuy.State.DEAD && littleGuy.currentState != LittleGuy.State.WON) {
            //JUMP
            if (littleGuy.currentState != LittleGuy.State.JUMPING && littleGuy.currentState != LittleGuy.State.FALLING) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                    littleGuy.body.applyLinearImpulse(new Vector2(0, 4.0f), littleGuy.body.getWorldCenter(), true);
                }
            }
            //right
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && littleGuy.body.getLinearVelocity().x <= 2) {

                littleGuy.body.applyLinearImpulse(new Vector2(0.07f, 0), littleGuy.body.getWorldCenter(), true);
            }
            //left
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && littleGuy.body.getLinearVelocity().x >= -2) {
                littleGuy.body.applyLinearImpulse(new Vector2(-0.07f, 0), littleGuy.body.getWorldCenter(), true);
            }

            //TOUCH SCREEN CONTROLS
            if (Gdx.input.isTouched())
            {
                //jump
                if (littleGuy.currentState != LittleGuy.State.JUMPING && littleGuy.currentState != LittleGuy.State.FALLING) {
                    if (Gdx.input.getY() < 640) {
                        littleGuy.body.applyLinearImpulse(new Vector2(0, 4.0f), littleGuy.body.getWorldCenter(), true);
                    }
                }
                //right
                if (Gdx.input.getX() >= 876 && littleGuy.body.getLinearVelocity().x <= 2)
                {
                    littleGuy.body.applyLinearImpulse(new Vector2(0.07f, 0), littleGuy.body.getWorldCenter(), true);
                }
                //left
                if (Gdx.input.getX() < 876 && littleGuy.body.getLinearVelocity().x >= -2)
                {
                    littleGuy.body.applyLinearImpulse(new Vector2(-0.07f, 0), littleGuy.body.getWorldCenter(), true);
                }
            }
        }
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        update(delta);

        //Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //render TILE MAP
        mapRenderer.render();
        //renders debug lines
        boxDR.render(world, gameCamera.combined);
        //only render what camera can see
        game.batch.setProjectionMatrix(gameCamera.combined);
        game.batch.begin();
        littleGuy.draw(game.batch);
        //enemy.draw(game.batch);
        for (Enemy enemy : container.getLittleEnemies())
        {
            enemy.draw(game.batch);
        }
        game.batch.end();

        hud.stage.draw();

        if (gameOver())
        {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
        if (gameWon())
        {
            //flip???
            LittleHumber.bestscore = GameHUD.bestscore;
            game.setScreen(new WinScreen(game));
            dispose();
        }
    }

    public boolean gameOver()
    {
        if (littleGuy.currentState == LittleGuy.State.DEAD && littleGuy.getStateTimer() > 3)
        {
            return true;
        }
        return false;
    }

    public boolean gameWon()
    {
        if (littleGuy.currentState == LittleGuy.State.WON && littleGuy.getStateTimer() > 2)
        {
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    public TiledMap getMap()
    {
        return map;
    }
    public World getWorld()
    {
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    //get rid of memory
    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        world.dispose();
        boxDR.dispose();
        hud.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log("yes", "");
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log("yes", "");
        return super.touchUp(screenX, screenY, pointer, button);
    }
}
