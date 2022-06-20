package com.diyartaikenov.game;

import static com.diyartaikenov.game.RunGame.HEIGHT;
import static com.diyartaikenov.game.RunGame.WIDTH;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class ParallaxLayer extends Actor implements Disposable {
    private Texture texture;
    private ParallaxBackground parallaxParent;

    public ParallaxLayer(Texture texture, ParallaxBackground parent) {
        this.texture = texture;
        this.parallaxParent = parent;
        texture.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(),
                getOriginX(), getOriginY(),
                WIDTH, HEIGHT,
                getScaleX(), getScaleY(),
                getRotation(), parallaxParent.getSrcX(), parallaxParent.getSrcY(),
                texture.getWidth()/2,
                texture.getHeight(),
                false, false
        );
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
