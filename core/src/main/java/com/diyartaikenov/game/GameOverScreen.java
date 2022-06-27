package com.diyartaikenov.game;

import static com.diyartaikenov.game.RunGame.HEIGHT;
import static com.diyartaikenov.game.RunGame.WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameOverScreen implements Screen {
    private final RunGame game;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private Stage stage;

    private Label gameOverLabel;

    public GameOverScreen(RunGame game, ParallaxBackground background) {
        this.game = game;
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        camera.position.set(WIDTH / 2f, HEIGHT / 2f, 0);
        viewport = new FitViewport(WIDTH, HEIGHT, camera);
        viewport.apply();
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = game.font;

        setupLabels(labelStyle);

        background.setSpeed(0);
        stage.addActor(background);
        stage.addActor(gameOverLabel);
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void setupLabels(Label.LabelStyle labelStyle) {
        gameOverLabel = new Label("Game Over", labelStyle);
        gameOverLabel.setColor(Color.BLACK);
        gameOverLabel.setPosition(
                WIDTH / 2f - gameOverLabel.getWidth() / 2,
                HEIGHT / 2f - gameOverLabel.getHeight() / 2
        );
    }

    //region Empty methods
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
