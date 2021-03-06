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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.diyartaikenov.game.RunGame;
import com.diyartaikenov.game.actors.Cactus;
import com.diyartaikenov.game.actors.Dino;
import com.diyartaikenov.game.actors.ParallaxBackground;

import java.util.Iterator;

public class GameScreen implements Screen {
    public static int GROUND_HEIGHT = 180;
    public static int HALF_SECOND_IN_NANOS = 500_000_000;
    /**
     * Decrease colliding actors' bounds by this factor so overlapping would happen
     * when the actors are closer to each other.
     */
    public static float FACTOR = 1.5f;

    private final RunGame game;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private Stage stage;
    private Timer timer;
    private int playerScore;
    private boolean isGameRunning;

    private ParallaxBackground background;
    private TextButtonStyle buttonStyle;
    private Label.LabelStyle labelStyle;
    private TextButton exitButton;
    private Label scoreLabel;

    private Dino dino;
    private Array<Texture> cactusTextures = new Array<>();
    private Queue<Cactus> cacti = new Queue<>();

    private int dinoX = 80;
    private long lastCactusSpawnTime;
    private long randomSeconds;

    public GameScreen(RunGame game) {
        this.game = game;
        camera = new OrthographicCamera(WIDTH, HEIGHT);
        camera.position.set(WIDTH / 2f, HEIGHT / 2f, 0);
        viewport = new FitViewport(WIDTH, HEIGHT, camera);
        viewport.apply();
        // It's important to supply viewport and sprite batch to Stage constructor.
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        // Create textures for parallax background.
        Array<Texture> layerTextures = new Array<>();
        int layerAmount = 10;
        for (int i = 1; i <= layerAmount; i++) {
            Texture layer = new Texture(
                    Gdx.files.internal("background/" + i + "_background.png")
            );
            layerTextures.add(layer);
        }
        background = new ParallaxBackground(layerTextures);

        buttonStyle = new TextButtonStyle();
        buttonStyle.font = game.font;
        labelStyle = new Label.LabelStyle();
        labelStyle.font = game.font;

        exitButton = createExitButton("[X]");
        setupScoreLabel(labelStyle);

        dino = new Dino(dinoX, GROUND_HEIGHT);

        // Create cacti textures.
        for (int i = 1; i <= 3; i++) {
            Texture texture = new Texture(Gdx.files.internal("cacti/cactus" + i + ".png"));
            cactusTextures.add(texture);
        }

        stage.addActor(background);
        stage.addActor(scoreLabel);
        stage.addActor(exitButton);
        stage.addActor(dino);
        spawnCactus();

        timer = new Timer();
        Timer.Task task = new Timer.Task() {
            @Override
            public void run() {
                scoreLabel.setText("Score: " + ++playerScore);
            }
        };
        timer.scheduleTask(task, 1, 1);

        isGameRunning = true;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        // Check if we need to spawn a new cactus.
        if (isGameRunning &&
                TimeUtils.nanoTime() - lastCactusSpawnTime > HALF_SECOND_IN_NANOS * randomSeconds) {
            spawnCactus();
        }

        stage.act();
        stage.draw();

        handleInput();

        Iterator<Cactus> iterator = cacti.iterator();
        while (iterator.hasNext()) {
            Cactus next = iterator.next();
            if (!next.isVisible()) {
                next.addAction(Actions.removeActor());
                iterator.remove();
            } else if (dino.getBounds().overlaps(next.getBounds())) {
                finishGame();
                break;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0);
    }

    @Override
    public void dispose() {
        stage.dispose();
        dino.dispose();
        for (Texture t: cactusTextures) {
            t.dispose();
        }
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            dino.jump();
        }
    }

    private TextButton createExitButton(String text) {
        TextButton exitButton = new TextButton(text, buttonStyle);
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

        return exitButton;
    }

    private void setupScoreLabel(Label.LabelStyle labelStyle) {
        scoreLabel = new Label("Score: 0", labelStyle);
        scoreLabel.setPosition(0, HEIGHT - scoreLabel.getHeight());
    }

    private void spawnCactus() {
        int randomIndex = MathUtils.random(0, cactusTextures.size - 1);
        Texture texture = cactusTextures.get(randomIndex);
        int cactusHeight = MathUtils.random(50, 70);
        Cactus cactus = new Cactus(texture, WIDTH, GROUND_HEIGHT, cactusHeight);
        stage.addActor(cactus);
        dino.setZIndex(stage.getActors().size);
        cacti.addLast(cactus);
        randomSeconds = MathUtils.random(2L, 5L);
        lastCactusSpawnTime = TimeUtils.nanoTime();
    }

    private void finishGame() {
        isGameRunning = false;
        background.setSpeed(0);
        dino.setMoving(false);
        for (Cactus c: cacti) {
            c.setMoving(false);
        }
        cacti.clear();
        timer.stop();
        saveProgress();
        displayGameOverMenu();
    }

    private void displayGameOverMenu() {
        Label gameOverLabel = new Label("Game Over", labelStyle);
        gameOverLabel.setPosition(
                WIDTH / 2f - gameOverLabel.getWidth() / 2,
                HEIGHT / 2f - gameOverLabel.getHeight() / 2
        );
        stage.addActor(gameOverLabel);

        scoreLabel.setPosition(
                WIDTH / 2f - scoreLabel.getWidth() / 2,
                gameOverLabel.getY() - scoreLabel.getHeight() * 2
        );

        TextButton restartButton = createRestartButton();
        restartButton.setPosition(
                WIDTH / 2f - restartButton.getWidth() / 2,
                scoreLabel.getY() - restartButton.getHeight() * 2
        );
        stage.addActor(restartButton);

        TextButton newExitButton = createExitButton("Exit");
        newExitButton.setPosition(
                WIDTH / 2f - newExitButton.getWidth() / 2,
                restartButton.getY() - newExitButton.getHeight() * 2
        );
        stage.addActor(newExitButton);
    }

    private void saveProgress() {
        int oldScore = game.preferences.getInteger(PREF_PLAYER_SCORE);
        if (playerScore > oldScore) {
            game.preferences.putInteger(PREF_PLAYER_SCORE, playerScore);
            game.preferences.flush();
        }
    }

    private TextButton createRestartButton() {
        TextButton restartButton = new TextButton("Restart", buttonStyle);
        restartButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
                dispose();
                return true;
            }
        });

        return restartButton;
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
