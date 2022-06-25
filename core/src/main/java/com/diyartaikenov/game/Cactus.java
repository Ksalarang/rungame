package com.diyartaikenov.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.awt.Rectangle;

public class Cactus extends Actor {
    private Sprite sprite;
    private final float aspectRatio;
    private Rectangle bounds;

    public Cactus(Texture texture, int x, int y, int height) {
        sprite = new Sprite(texture);
        aspectRatio = sprite.getWidth() / sprite.getHeight();
        sprite.setBounds(x, y, height * aspectRatio, height);
        bounds = new Rectangle(x, y, (int) sprite.getWidth(), (int) sprite.getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        sprite.translateX(-170 * delta);
        bounds.x = (int) sprite.getX();

        // Remove from stage if moved off screen.
        if (sprite.getX() + sprite.getWidth() < 0) {
            remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch, parentAlpha);
    }

    @Override
    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
        bounds.x = (int) x;
        bounds.y = (int) y;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
