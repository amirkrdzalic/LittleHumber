package com.amirkrdzalic.littlehumber.Scenes;

import com.amirkrdzalic.littlehumber.LittleHumber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;



public class GameHUD implements Disposable{
    public Stage stage;
    private Viewport viewport;

    public Integer totalTime;
    private float timeCount;
    private static Integer score;
    public static Integer bestscore;

    private Label countdownLabel;
    private static Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label littleLabel;

    public GameHUD(SpriteBatch spriteBatch)
    {
        totalTime = 300;
        timeCount = 0;
        score = 0;
        bestscore = 0;

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.BLUE);
        viewport = new FitViewport(LittleHumber.VW, LittleHumber.VH, new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);

        Table table = new Table();
        table.top();
        //table is size of stage
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", totalTime), font);
        scoreLabel = new Label(String.format("%06d", score), font);
        timeLabel = new Label("TIME", font);
        levelLabel = new Label("1/1", font);
        worldLabel = new Label("Level", font);
        littleLabel = new Label("Score", font);

        table.add(littleLabel).expandX().top().left();
        table.add(worldLabel).expandX().top().center();
        table.add(timeLabel).expandX().top().right();
        table.row();
        table.add(scoreLabel).expandX().top().left();
        table.add(levelLabel).expandX().top();
        table.add(countdownLabel).expand().top().right();

        stage.addActor(table);
    }

    public void update(float deltaT)
    {
        timeCount += deltaT;
        if (timeCount >= 1)
        {
            totalTime--;
            countdownLabel.setText(String.format("%03d", totalTime));
            timeCount = 0;
        }
    }

    public static void addScore(int value)
    {
        score += value;
        if (score > bestscore)
        {
            bestscore = score;
        }
        scoreLabel.setText(String.format("%03d", score));
    }



    @Override
    public void dispose() {
        stage.dispose();
    }
}
