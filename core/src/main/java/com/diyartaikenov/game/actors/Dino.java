package com.diyartaikenov.game.actors;

import static com.diyartaikenov.game.screens.GameScreen.GROUND_HEIGHT;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Dino extends Actor implements Disposable {
    private final int GRAVITY = -10;
    private final int RUNNING_FRAMES_AMOUNT = 8;
    private final int HEIGHT = 50;

    private Vector2 velocity;
    /**
     * Used to check for overlapping with another {@link Rectangle}.
     */
    private Rectangle bounds;
    private float boundsOffsetX;
    private float boundsOffsetY;

    private Animation<Texture> runAnimation;
    private Texture currentFrame;
    private Texture jumpFrame;
    private float frameAspectRatio;
    private float stateTime;
    private State state = State.RUNNING;
    private boolean isMoving = true;

    public Dino(int x, int y) {
        setPosition(x, y);
        velocity = new Vector2(0, 0);

        Array<Texture> frames = new Array<>();
        for (int i = 1; i <= RUNNING_FRAMES_AMOUNT; i++) {
            frames.add(new Texture(Gdx.files.internal("run/run" + i + ".png")));
        }
        runAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
        currentFrame = runAnimation.getKeyFrame(stateTime);
        jumpFrame = new Texture(Gdx.files.internal("jump.png"));

        frameAspectRatio = (float) currentFrame.getWidth() / (float) currentFrame.getHeight();
        setWidth(HEIGHT * frameAspectRatio);
        setHeight(HEIGHT);

        // Decrease the rectangle by factor f so overlapping would happen
        // when Dino and the cactus are closer to each other.
        float f = 2f;
        bounds = new Rectangle(x, y, getWidth() / f, getHeight() / f);
        boundsOffsetX = (getWidth() - bounds.getWidth()) / 2;
        boundsOffsetY = (getHeight() - bounds.getHeight()) / 2;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isMoving) {
            calculatePosition(delta);
            updateState();
            updateCurrentFrame(delta);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currentFrame,
                getX(), getY(),
                getWidth(),
                getHeight()
        );
    }

    public void dispose() {
        // TODO: 20.06.2022 dispose run animation frames
        jumpFrame.dispose();
    }

    public void jump() {
        if (state != State.JUMPING) {
            velocity.y = 300;
        }
    }

    public void setMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public Rectangle getBounds() {
        float x = getX() + boundsOffsetX;
        float y = getY() + boundsOffsetX;
        bounds.setPosition(x, y);
        return bounds;
    }

    private void calculatePosition(float deltaTime) {
        if (getY() > GROUND_HEIGHT) {
            velocity.add(0, GRAVITY);
        }
        velocity.scl(deltaTime);
        moveBy(0, velocity.y);
        velocity.scl(1 / deltaTime);

        if (getY() < GROUND_HEIGHT) {
            setY(GROUND_HEIGHT);
        }
    }

    private void updateState() {
        if (getY() == GROUND_HEIGHT) {
            state = State.RUNNING;
        } else if (getY() > GROUND_HEIGHT) {
            state = State.JUMPING;
        }
    }

    private void updateCurrentFrame(float deltaTime) {
        if (state == State.RUNNING) {
            stateTime += deltaTime;
            currentFrame = runAnimation.getKeyFrame(stateTime);
        } else if (state == State.JUMPING) {
            currentFrame = jumpFrame;
        }
    }

    private enum State { RUNNING, JUMPING, }
}
