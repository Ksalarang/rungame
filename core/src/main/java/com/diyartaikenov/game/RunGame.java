package com.diyartaikenov.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.diyartaikenov.game.screens.MainMenuScreen;

public class RunGame extends Game {
	// The aspect ratio is almost the same as in 750x1334
	public static int WIDTH = 450;
	public static int HEIGHT = 800;

	public SpriteBatch batch;
	public BitmapFont font;

	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
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