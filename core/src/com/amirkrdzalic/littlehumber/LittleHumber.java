package com.amirkrdzalic.littlehumber;

import com.amirkrdzalic.littlehumber.Screens.StartScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LittleHumber extends Game {
	//container for all sprites only need one, to be accessed by everyone
	public SpriteBatch batch;
	//Virtual width and height
	public static final int VW = 400;
	public static final int VH = 200;
	//scale value belongs to instance of class and cannot be cahnged for efficiencey and security
	public static final float pixalPerM = 100;

	//collision bits
	//shorts are easier to OR when even and a multiplier of 2
	public static final short DEAD_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short LITTLEGUY_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short MONEY_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEADBIT = 128;
	public static final short ITEM_BIT = 256;
	public static final short LITTLEGUY_HEADBIT = 512;

	//score is set to the score on gamescreen and displayed to the end game when won
	public static Integer bestscore;
	//sounds
	public static AssetManager manager;
	
	@Override
	public void create () {
		manager = new AssetManager();
		manager.load("music/8-punk-8-bit-music.mp3", Music.class);
		manager.load("music/backgroundmusic.wav", Music.class);
		manager.load("music/coin.wav", Sound.class);
		manager.load("music/bump.wav", Sound.class);
		manager.load("music/wonsong.mp3", Sound.class);
		manager.load("music/losesong.mp3", Sound.class);
		manager.finishLoading();

		batch = new SpriteBatch();
		//which screen goes first on first runtime
		setScreen(new StartScreen(this));
	}

	@Override
	public void render ()
	{
		super.render();
	}

	@Override
	public void dispose()
	{
		//free it dont need it anymore
		super.dispose();
		manager.dispose();
		batch.dispose();
	}
}
