/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.badlogic.gdx.graphics.Pixmap
 *  com.badlogic.gdx.graphics.Texture
 *  com.badlogic.gdx.graphics.g2d.Batch
 *  com.badlogic.gdx.graphics.g2d.Sprite
 *  com.badlogic.gdx.graphics.g2d.SpriteBatch
 */
package com.emeraldSword.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.HashMap;
import java.util.Map;

public class VillageResources {
    public static final Map<Integer, String> spriteLibrary = new HashMap<Integer, String>();
    private int type;
    private int xPosition;
    private int yPosition;
    private int yFallPos = 0;
    private Sprite sprite;
    boolean falling = false;
    boolean noSwitch = false;
    Pixmap pixmap;

    static {
        spriteLibrary.put(1, "one.png");
        spriteLibrary.put(2, "two.png");
        spriteLibrary.put(3, "three.png");
        spriteLibrary.put(4, "four.png");
        spriteLibrary.put(5, "five.png");
        spriteLibrary.put(6, "six.png");
        spriteLibrary.put(7, "seven.png");
        spriteLibrary.put(8, "eight.png");
        spriteLibrary.put(9, "nine.png");
        spriteLibrary.put(10, "ten.png");
    }

    public boolean isNoSwitch() {
        return this.noSwitch;
    }

    public void setNoSwitch(boolean noSwitch) {
        this.noSwitch = noSwitch;
    }

    public boolean isFalling() {
        return this.falling;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    public VillageResources(int xPosition, int yPosition, int type) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.type = type;
        Texture texture = new Texture(spriteLibrary.get(type));
        this.sprite = new Sprite(texture);
        this.sprite.setPosition((float)(xPosition * 40 + 60), (float)(yPosition * 40 + 60));
    }

    public void draw(SpriteBatch batch) {
        if (this.sprite != null) {
            this.sprite.draw((Batch)batch);
        }
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getxPosition() {
        return this.xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return this.yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public Sprite getSprite() {
        return this.sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (((VillageResources)obj).getxPosition() == this.getxPosition() && ((VillageResources)obj).getyPosition() == this.getyPosition() && ((VillageResources)obj).getType() == this.getType()) {
            return true;
        }
        return false;
    }

    public int getyFallPos() {
        return this.yFallPos;
    }

    public void setyFallPos(int yFallPos) {
        this.yFallPos = yFallPos;
    }
}
