/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  com.badlogic.gdx.ApplicationAdapter
 *  com.badlogic.gdx.Gdx
 *  com.badlogic.gdx.Input
 *  com.badlogic.gdx.InputProcessor
 *  com.badlogic.gdx.graphics.Color
 *  com.badlogic.gdx.graphics.GL20
 *  com.badlogic.gdx.graphics.Pixmap
 *  com.badlogic.gdx.graphics.Pixmap$Format
 *  com.badlogic.gdx.graphics.Texture
 *  com.badlogic.gdx.graphics.g2d.Batch
 *  com.badlogic.gdx.graphics.g2d.Sprite
 *  com.badlogic.gdx.graphics.g2d.SpriteBatch
 */
package com.emeraldSword.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.emeraldSword.game.Grid;
import com.emeraldSword.game.GridManager;
import com.emeraldSword.game.PossibleSwap;
import com.emeraldSword.game.VillageResources;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmeraldSword
extends ApplicationAdapter
implements InputProcessor {
    SpriteBatch batch;
    Texture img;
    Grid grid;
    VillageResources first;
    VillageResources second;
    float xPosFirst;
    float yPosFirst;
    float xPosSecond;
    float yPosSecond;
    boolean chainSearch = false;
    List<PossibleSwap> possibleSwaps = new ArrayList<PossibleSwap>();
    boolean inverse = false;
    boolean straight = true;
    boolean falling = false;
    boolean count = false;
    List<VillageResources> chains = new ArrayList<VillageResources>();

    public void create() {
        this.batch = new SpriteBatch();
        this.img = new Texture("stock-photo-33531251.jpg");
        this.grid = new Grid(9, 9);
        GridManager.populateGridRandomly(this.grid);
        Gdx.input.setInputProcessor((InputProcessor)this);
    }

    public void render() {
        Gdx.gl.glClearColor(255.0f, 255.0f, 255.0f, 1.0f);
        Gdx.gl.glClear(16384);
        this.batch.begin();
        this.batch.draw(this.img, 0.0f, 0.0f);
        for (VillageResources res : this.grid.getAllRes()) {
            res.draw(this.batch);
        }
        if (this.first != null && this.second == null) {
            this.highlightSelection();
        }
        if (this.first != null && this.second != null) {
            this.SwapSelection();
            this.count = true;
        }
        if (!this.chainSearch) {
            this.searchSwap();
        }
        if (!this.falling && !this.chains.isEmpty() || this.count) {
            this.count = false;
            this.chains = this.countMatch();
        }
        this.fillEmpty();
        this.batch.end();
    }

    private void fillEmpty() {
        int j = 0;
        while (j < this.grid.getNbLine()) {
            int i = 0;
            while (i < this.grid.getNbCol()) {
                VillageResources resToTest = this.grid.getResourcesAt(i, j);
                if (resToTest.getSprite() == null) {
                    int y = resToTest.getyPosition() * 40 + 60;
                    VillageResources resOnTop = this.grid.getResourcesAt(i, j + 1);
                    if (resOnTop != null) {
                        resOnTop.setyFallPos(y);
                    } else if (!this.falling) {
                        int xPosition = resToTest.getxPosition();
                        int yPosition = resToTest.getyPosition();
                        this.grid.getAllRes().remove(resToTest);
                        int type = (int)(Math.random() * 10.0 + 1.0);
                        VillageResources vr = new VillageResources(xPosition, yPosition, type);
                        vr.setyFallPos(y);
                        this.grid.getAllRes().add(vr);
                    }
                }
                if (resToTest.getSprite() != null && resToTest.getyFallPos() != 0 && resToTest.getSprite().getY() > (float)resToTest.getyFallPos()) {
                    resToTest.setFalling(true);
                    resToTest.getSprite().translateY(-2.0f);
                    this.falling = true;
                } else if (resToTest.isFalling()) {
                    resToTest.setyFallPos(0);
                    resToTest.setFalling(false);
                    VillageResources resOnBottom = this.grid.getResourcesAt(i, j - 1);
                    this.grid.switchResources(resToTest, resOnBottom);
                    this.falling = false;
                    this.chainSearch = false;
                }
                ++i;
            }
            ++j;
        }
    }

    private List<VillageResources> countMatch() {
        ArrayList<VillageResources> chains = new ArrayList<VillageResources>();
        int j = 0;
        while (j < this.grid.getNbLine() - 1) {
            int i = 0;
            while (i < this.grid.getNbCol() - 1) {
                VillageResources resToSwitch = this.grid.getResourcesAt(i, j);
                Map<String, List<VillageResources>> detectedChain = this.grid.detectChain(resToSwitch);
                if (detectedChain.get("vertical") != null) {
                    for (VillageResources chainPart : detectedChain.get("vertical")) {
                        if (chains.contains(chainPart)) continue;
                        chains.add(chainPart);
                        chainPart.setSprite(null);
                    }
                }
                if (detectedChain.get("horizontal") != null) {
                    for (VillageResources chainPart : detectedChain.get("horizontal")) {
                        if (chains.contains(chainPart)) continue;
                        chains.add(chainPart);
                        chainPart.setSprite(null);
                    }
                }
                ++i;
            }
            ++j;
        }
        return chains;
    }

    private void highlightSelection() {
        Pixmap mask = new Pixmap(39, 39, Pixmap.Format.RGBA8888);
        mask.setColor(new Color(255.0f, 0.0f, 0.0f, 0.6f));
        mask.drawCircle(20, 18, 16);
        mask.drawCircle(20, 18, 17);
        mask.drawCircle(20, 18, 18);
        Texture texture2 = new Texture(mask);
        Sprite highlight = new Sprite(texture2);
        highlight.setPosition((float)(this.first.getxPosition() * 40 + 55), (float)(this.first.getyPosition() * 40 + 55));
        highlight.draw((Batch)this.batch);
    }

    private void SwapSelection() {
        PossibleSwap ps = new PossibleSwap(this.first, this.second);
        if (this.possibleSwaps.contains(ps)) {
            if (!(this.second.getxPosition() != this.first.getxPosition() + 1 && this.second.getxPosition() != this.first.getxPosition() - 1 && this.second.getxPosition() != this.first.getxPosition() || this.second.getyPosition() != this.first.getyPosition() + 1 && this.second.getyPosition() != this.first.getyPosition() - 1 && this.second.getyPosition() != this.first.getyPosition())) {
                if (this.first.getSprite().getX() == this.xPosSecond && this.first.getSprite().getY() == this.yPosSecond && this.second.getSprite().getX() == this.xPosFirst && this.second.getSprite().getY() == this.yPosFirst) {
                    this.grid.switchResources(this.first, this.second);
                    this.first = null;
                    this.second = null;
                } else {
                    if (this.first.getxPosition() > this.second.getxPosition()) {
                        this.first.getSprite().translateX(-2.0f);
                        this.second.getSprite().translateX(2.0f);
                    }
                    if (this.first.getxPosition() < this.second.getxPosition()) {
                        this.first.getSprite().translateX(2.0f);
                        this.second.getSprite().translateX(-2.0f);
                    }
                    if (this.first.getyPosition() > this.second.getyPosition()) {
                        this.first.getSprite().translateY(-2.0f);
                        this.second.getSprite().translateY(2.0f);
                    }
                    if (this.first.getyPosition() < this.second.getyPosition()) {
                        this.first.getSprite().translateY(2.0f);
                        this.second.getSprite().translateY(-2.0f);
                    }
                }
            }
        } else if (!(this.second.getxPosition() != this.first.getxPosition() + 1 && this.second.getxPosition() != this.first.getxPosition() - 1 && this.second.getxPosition() != this.first.getxPosition() || this.second.getyPosition() != this.first.getyPosition() + 1 && this.second.getyPosition() != this.first.getyPosition() - 1 && this.second.getyPosition() != this.first.getyPosition())) {
            if (this.straight && this.first.getSprite().getX() == this.xPosSecond && this.first.getSprite().getY() == this.yPosSecond && this.second.getSprite().getX() == this.xPosFirst && this.second.getSprite().getY() == this.yPosFirst) {
                this.inverse = true;
                this.straight = false;
            } else if (this.inverse && this.first.getSprite().getX() == this.xPosFirst && this.first.getSprite().getY() == this.yPosFirst && this.second.getSprite().getX() == this.xPosSecond && this.second.getSprite().getY() == this.yPosSecond) {
                this.first = null;
                this.second = null;
                this.inverse = false;
                this.straight = true;
            } else {
                if (!this.inverse && this.first.getxPosition() > this.second.getxPosition()) {
                    this.first.getSprite().translateX(-2.0f);
                    this.second.getSprite().translateX(2.0f);
                }
                if (!this.inverse && this.first.getxPosition() < this.second.getxPosition()) {
                    this.first.getSprite().translateX(2.0f);
                    this.second.getSprite().translateX(-2.0f);
                }
                if (!this.inverse && this.first.getyPosition() > this.second.getyPosition()) {
                    this.first.getSprite().translateY(-2.0f);
                    this.second.getSprite().translateY(2.0f);
                }
                if (!this.inverse && this.first.getyPosition() < this.second.getyPosition()) {
                    this.first.getSprite().translateY(2.0f);
                    this.second.getSprite().translateY(-2.0f);
                }
                if (this.inverse && this.first.getxPosition() < this.second.getxPosition()) {
                    this.first.getSprite().translateX(-2.0f);
                    this.second.getSprite().translateX(2.0f);
                }
                if (this.inverse && this.first.getxPosition() > this.second.getxPosition()) {
                    this.first.getSprite().translateX(2.0f);
                    this.second.getSprite().translateX(-2.0f);
                }
                if (this.inverse && this.first.getyPosition() < this.second.getyPosition()) {
                    this.first.getSprite().translateY(-2.0f);
                    this.second.getSprite().translateY(2.0f);
                }
                if (this.inverse && this.first.getyPosition() > this.second.getyPosition()) {
                    this.first.getSprite().translateY(2.0f);
                    this.second.getSprite().translateY(-2.0f);
                }
            }
        }
    }

    private void searchSwap() {
        this.possibleSwaps = new ArrayList<PossibleSwap>();
        int j = 0;
        while (j < this.grid.getNbLine()) {
            int i = 0;
            while (i < this.grid.getNbCol()) {
                PossibleSwap ps;
                VillageResources resToSwitch = this.grid.getResourcesAt(i, j);
                VillageResources resOnTop = this.grid.getResourcesAt(i, j + 1);
                VillageResources resOnRight = this.grid.getResourcesAt(i + 1, j);
                int typeToSwitch = resToSwitch.getType();
                int typeOnRight = (resOnRight!=null?resOnRight.getType():0);
                int typeOnTop = (resOnTop!=null?resOnTop.getType():0);
                if(resOnRight !=null) {
	                resToSwitch.setType(typeOnRight);
	                resOnRight.setType(typeToSwitch);
	                if (this.grid.hasChain(resToSwitch) || this.grid.hasChain(resOnRight)) {
	                    ps = new PossibleSwap(resToSwitch, resOnRight);
	                    this.possibleSwaps.add(ps);
	                    System.out.println("Can switch [" + resToSwitch.getxPosition() + " : " + resToSwitch.getyPosition() + "] of type " + typeToSwitch + " with [" + resOnRight.getxPosition() + " : " + resOnRight.getyPosition() + "] of type " + typeOnRight);
	                }
	                resToSwitch.setType(typeToSwitch);
	                resOnRight.setType(typeOnRight);
                }
                if(resOnTop != null) {
	                resToSwitch.setType(typeOnTop);
	                resOnTop.setType(typeToSwitch);
	                if (this.grid.hasChain(resToSwitch) || this.grid.hasChain(resOnTop)) {
	                    ps = new PossibleSwap(resToSwitch, resOnTop);
	                    this.possibleSwaps.add(ps);
	                    System.out.println("Can switch [" + resToSwitch.getxPosition() + " : " + resToSwitch.getyPosition() + "] of type " + typeToSwitch + " with [" + resOnTop.getxPosition() + " : " + resOnTop.getyPosition() + "] of type " + typeOnTop);
	                }
	                resToSwitch.setType(typeToSwitch);
	                resOnTop.setType(typeOnTop);
                }
                ++i;
            }
            ++j;
        }
        this.chainSearch = true;
    }

    public boolean keyDown(int keycode) {
        return false;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == 0) {
            VillageResources res = this.grid.getSpriteClickOn(screenX, screenY);
            if (res != null) {
                if (this.first == null) {
                    this.first = res;
                    this.xPosFirst = this.first.getSprite().getX();
                    this.yPosFirst = this.first.getSprite().getY();
                } else {
                    this.second = res;
                    this.xPosSecond = this.second.getSprite().getX();
                    this.yPosSecond = this.second.getSprite().getY();
                }
            } else {
                this.first = null;
                this.second = null;
            }
        }
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    public boolean scrolled(int amount) {
        return false;
    }
}
