package com.diyartaikenov.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Dino extends Actor implements Disposable {
    private final int GRAVITY = -10;
    private final int RUNNING_FRAMES_AMOUNT = 8;
    private final int HEIGHT = 50;

    private Vector2 position;
    private Vector2 velocity;
    private int groundHeight;

    private Animation<Texture> runAnimation;
    private Texture currentFrame;
    private Texture jumpFrame;
    private Texture landFrame;
    private float frameAspectRatio;
    private float stateTime;
    private State state = State.RUNNING;

    public Dino(int x, int y) {
        position = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        groundHeight = y;

        Array<Texture> frames = new Array<>();
        for (int i = 1; i <= RUNNING_FRAMES_AMOUNT; i++) {
            frames.add(new Texture(Gdx.files.internal("run/run" + i + ".png")));
        }
        runAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
        currentFrame = runAnimation.getKeyFrame(stateTime);
        frameAspectRatio = (float) currentFrame.getWidth() / (float) currentFrame.getHeight();
        jumpFrame = new Texture(Gdx.files.internal("jump.png"));
        landFrame = new Texture(Gdx.files.internal("land.png"));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        calculatePosition(delta);
        updateState();
        updateCurrentFrame(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currentFrame,
                position.x, position.y,
                HEIGHT * frameAspectRatio,
                HEIGHT
        );
    }

    public void dispose() {
        // TODO: 20.06.2022 dispose run animation frames
        jumpFrame.dispose();
        landFrame.dispose();
    }

    public void jump() {
        if (state != State.JUMPING && state != State.LANDING) {
            velocity.y = 300;
        }
    }

    private void calculatePosition(float deltaTime) {
        if (position.y > groundHeight) {
            velocity.add(0, GRAVITY);
        }
        velocity.scl(deltaTime);
        position.add(0, velocity.y);
        if (position.y < groundHeight) {
            position.y = groundHeight;
        }
        velocity.scl(1 / deltaTime);
    }

    private void updateState() {
        if (position.y == groundHeight) {
            state = State.RUNNING;
        } else if (velocity.y > 0) {
            state = State.JUMPING;
        } else if (velocity.y < 0) {
            state = State.LANDING;
        }
    }

    private void updateCurrentFrame(float deltaTime) {
        if (state == State.RUNNING) {
            stateTime += deltaTime;
            currentFrame = runAnimation.getKeyFrame(stateTime);
        } else if (state == State.JUMPING) {
            currentFrame = jumpFrame;
        } else if (state == State.LANDING) {
            currentFrame = landFrame;
        }
    }

    private enum State { RUNNING, JUMPING, LANDING, }
}