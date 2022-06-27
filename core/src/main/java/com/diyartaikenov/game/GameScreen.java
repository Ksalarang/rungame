package com.diyartaikenov.game;

import static com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import static com.diyartaikenov.game.RunGame.HEIGHT;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Iterator;

public class GameScreen implements Screen {
    static int GROUND_HEIGHT = 180;
    static int SECOND_IN_NANOS = 2_000_000_000;

    private final RunGame game;
    private OrthographicCamera camera;
    private FitViewport viewport;
    private Stage stage;

    private ParallaxBackground background;
    private TextButtonStyle buttonStyle;
    private TextButton exitButton;
    private Dino dino;
    private Array<Texture> cactusTextures = new Array<>();
    private Queue<Cactus> cacti = new Queue<>();

    private int dinoX = 80;
    private long lastCactusSpawnTime;

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

        setupExitButton();

        dino = new Dino(dinoX, GROUND_HEIGHT);

        // Create cacti textures.
        for (int i = 1; i <= 3; i++) {
            Texture texture = new Texture(Gdx.files.internal("cacti/cactus" + i + ".png"));
            cactusTextures.add(texture);
        }

        stage.addActor(background);
        stage.addActor(exitButton);
        stage.addActor(dino);
        spawnCactus();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        // Check if we need to spawn a new cactus.
        if (TimeUtils.nanoTime() - lastCactusSpawnTime > SECOND_IN_NANOS) {
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
                System.out.println("overlap");
                // todo: finish game
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
        background.dispose();
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

    private void spawnCactus() {
        int randomIndex = MathUtils.random(0, cactusTextures.size - 1);
        Texture texture = cactusTextures.get(randomIndex);
        int cactusHeight = MathUtils.random(50, 70);
        Cactus cactus = new Cactus(texture, WIDTH, GROUND_HEIGHT, cactusHeight);
        stage.addActor(cactus);
        cacti.addLast(cactus);
        lastCactusSpawnTime = TimeUtils.nanoTime();
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
