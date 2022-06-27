package com.diyartaikenov.game.screens;

import static com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import static com.diyartaikenov.game.RunGame.HEIGHT;
import static com.diyartaikenov.game.RunGame.PREF_PLAYER_SCORE;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.diyartaikenov.game.RunGame;

public class MainMenuScreen implements Screen {
    private final RunGame game;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private Stage stage;

    private Sprite background;
    private TextButtonStyle buttonStyle;
    private TextButton playButton;
    private TextButton exitButton;

    public MainMenuScreen(RunGame game) {
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

        setupPlayButton();
        setupExitButton();

        // Show player score if it was persisted in preferences before.
        int playerScore = game.preferences.getInteger(PREF_PLAYER_SCORE);
        if (playerScore > 0) {
            Label.LabelStyle labelStyle = new Label.LabelStyle();
            labelStyle.font = game.font;
            Label scoreLabel = new Label("Points " + playerScore, labelStyle);
            scoreLabel.setPosition(
                    WIDTH / 2f - scoreLabel.getWidth() / 2,
                    playButton.getY() + scoreLabel.getHeight() * 2
                    );
            stage.addActor(scoreLabel);
        }

        stage.addActor(playButton);
        stage.addActor(exitButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

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

    private void setupPlayButton() {
        playButton = new TextButton("Play", buttonStyle);
        playButton.setPosition(
                WIDTH / 2f - playButton.getWidth() / 2,
                HEIGHT / 2f - playButton.getHeight() / 2
        );
        playButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
                dispose();
                return true;
            }
        });
    }

    private void setupExitButton() {
        exitButton = new TextButton("Exit", buttonStyle);
        exitButton.setPosition(
                WIDTH / 2f - exitButton.getWidth() / 2,
                playButton.getY() - exitButton.getHeight() * 2
        );

        exitButton.addListener(new InputListener(){
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
