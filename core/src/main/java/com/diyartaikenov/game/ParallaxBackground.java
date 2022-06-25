package com.diyartaikenov.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class ParallaxBackground extends Group implements Disposable {
    private final float LAYER_SPEED_DIFFERENCE = 1.5f;

    private float scroll;
    private float speed = 0.5f;

    private int srcX, srcY;

    public ParallaxBackground(Array<Texture> layerTextures) {
        for (Texture texture: layerTextures) {
            ParallaxLayer layer = new ParallaxLayer(texture, this);
            addActor(layer);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        scroll += speed;
    }

    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {
        for (int i = 0; i < getChildren().size; i++) {
            srcX = (int) (scroll + i * LAYER_SPEED_DIFFERENCE * scroll);
            getChildren().get(i).draw(batch, parentAlpha);
        }
    }

    @Override
    public void dispose() {
        for (Actor child: getChildren()) {
            ((ParallaxLayer) child).dispose();
        }
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getSrcX() {
        return srcX;
    }

    public int getSrcY() {
        return srcY;
    }
}
