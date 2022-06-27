package com.diyartaikenov.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.diyartaikenov.game.screens.MainMenuScreen;

/**
 * This is my first attempt at creating cross-platform games with libGDX.
 * Please keep it in mind while evaluating my project.
 */
public class RunGame extends Game {
	// The aspect ratio is almost the same as in 750x1334
	public static int WIDTH = 450;
	public static int HEIGHT = 800;
	public static String PREF_PLAYER_SCORE = "playerPoints";

	public SpriteBatch batch;
	public BitmapFont font;
	public Preferences preferences;

	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		preferences = Gdx.app.getPreferences("playerProgress");
		setScreen(new MainMenuScreen(this));
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}
}