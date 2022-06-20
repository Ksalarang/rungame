package com.diyartaikenov.game;

import static com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import static com.diyartaikenov.game.RunGame.HEIGHT;
import static com.diyartaikenov.game.RunGame.WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class PlayScreen implements Screen {
    private final RunGame game;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private Stage stage;

    private Sprite background;
    private TextButtonStyle buttonStyle;
    private TextButton exitButton;

    public PlayScreen(RunGame game) {
        this.game = game;
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        camera.position.set(WIDTH / 2f, HEIGHT / 2f, 0);
        viewport = new FitViewport(WIDTH, HEIGHT, camera);
        viewport.apply();
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        background = new Sprite(new Texture(Gdx.files.internal("background.png")));
        background.setBounds(0, 0, WIDTH, HEIGHT);

        buttonStyle = new TextButtonStyle();
        buttonStyle.font = game.font;

        setupExitButton();

        stage.addActor(exitButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.DARK_GRAY);

        game.batch.begin();
        background.draw(game.batch);
        game.batch.end();

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
        background.getTexture().dispose();
    }

    private void setupExitButton() {
        exitButton = new TextButton("[X]", buttonStyle);
        exitButton.setPosition(
                WIDTH - exitButton.getWidth() - 5,
                HEIGHT - exitButton.getHeight() - 5
        );
        exitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                Gdx.app.exit();
                return true;
            }
        });
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
