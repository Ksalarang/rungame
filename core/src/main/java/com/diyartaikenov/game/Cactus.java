package com.diyartaikenov.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

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
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch, parentAlpha);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public boolean isVisible() {
        return sprite.getX() + sprite.getWidth() >= 0;
    }
}
