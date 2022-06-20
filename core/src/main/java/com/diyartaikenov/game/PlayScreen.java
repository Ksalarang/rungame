package com.diyartaikenov.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;

public class PlayScreen implements Screen {
    private final RunGame game;
    private Stage stage;

    public PlayScreen(RunGame game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.DARK_GRAY);

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    //region Empty methods
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
    public void show() {

    }

    @Override
    public void hide() {

    }
    //endregion
}
