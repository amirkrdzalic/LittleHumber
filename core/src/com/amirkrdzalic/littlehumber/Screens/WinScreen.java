package com.amirkrdzalic.littlehumber.Screens;

import com.amirkrdzalic.littlehumber.LittleHumber;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by User on 12/30/2016.
 */
public class WinScreen implements Screen {

    private Game game;
    private Stage stage;
    private Viewport viewport;
    private Music music;

    public WinScreen(Game game)
    {
        this.game = game;
        viewport = new FitViewport(LittleHumber.VW, LittleHumber.VH, new OrthographicCamera());
        stage = new Stage(viewport, ((LittleHumber) game).batch);

        Label.LabelStyle font2 = new Label.LabelStyle(new BitmapFont(), Color.BLUE);
        Label.LabelStyle font1 = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        //things that go inside table/screen
        Texture texture = new Texture("logo2.png");
        Label startGameLabel = new Label("Press to Play Again", font2);
        Label bestLabel = new Label("Best score is...", font1);
        Label bestscoreLabel = new Label(String.format("%06d", LittleHumber.bestscore), font1);

        table.add(new Image(texture)).expandX();
        table.row();
        table.add(startGameLabel).expandX().padTop(10.0f);
        table.row();
        table.add(bestLabel).expandX().padTop(10.0f);
        table.row();
        table.add(bestscoreLabel).expandX().padTop(10.0f);

        music = LittleHumber.manager.get("music/8-punk-8-bit-music.mp3", Music.class);
        music.setLooping(true);
        music.play();

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (Gdx.input.justTouched())
        {
            game.setScreen(new GameScreen((LittleHumber) game));
            music.stop();
            dispose();
        }
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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

    @Override
    public void dispose() {
        stage.dispose();
    }
}
