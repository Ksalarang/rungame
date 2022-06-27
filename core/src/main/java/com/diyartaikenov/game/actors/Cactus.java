package com.diyartaikenov.game.actors;

import static com.diyartaikenov.game.screens.GameScreen.FACTOR;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Cactus extends Actor {
    private Sprite sprite;
    private final float aspectRatio;
    private Rectangle bounds;
    private boolean isMoving = true;

    public Cactus(Texture texture, int x, int y, int height) {
        sprite = new Sprite(texture);
        aspectRatio = sprite.getWidth() / sprite.getHeight();
        sprite.setBounds(x, y, height * aspectRatio, height);
        bounds = new Rectangle(x, y,
                sprite.getWidth() / FACTOR,
                sprite.getHeight() / FACTOR
        );
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (isMoving) {
            sprite.translateX(-170 * delta);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch, parentAlpha);
    }

    @Override
    public boolean isVisible() {
        return sprite.getX() + sprite.getWidth() >= 0;
    }

    public Rectangle getBounds() {
        float x = sprite.getX() + (sprite.getWidth() - bounds.getWidth()) / 2;
        float y = sprite.getY() + (sprite.getHeight() - bounds.getHeight()) / 2;
        bounds.setPosition(x, y);
        return bounds;
    }

    public void setMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }
}
