/*
 * The MIT License
 *
 * Copyright 2020 Yago Mouriño Mendaña
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.ymourino.pmdm06.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.ymourino.pmdm06.helpers.GameConstantData;

/**
 * Aquí se definen las características y comportamientos comunes a los elementos del juego que
 * están en pantalla.
 *
 * @author Yago Mouriño Mendaña
 */
public abstract class ScreenItem {

    private float energy;
    private float shield;

    public ScreenItem() {}

    public ScreenItem(float energy) {
        this.shield = energy;
        //this.energy = energy * GameConstantData.BASE_ENERGY;
        this.energy = 100;
    }

    abstract void update(float delta);

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(getSprite(), getX(), getY());
    }

    abstract Texture getSprite();

    abstract float getX();
    abstract float getY();

    public final Rectangle getCollisionRectangle() {
        return new Rectangle(getX(), getY(), getSprite().getWidth(), getSprite().getHeight());
    }

    public final float getEnergy() {
        return energy;
    }

    public final void setEnergy(float energy) {
        this.energy = energy;
    }

    public void hit(float impactEnergy) {
        energy -= (impactEnergy * (1 - (shield / 100)));
    }

    public final boolean isDead() {
        return energy <= 0;
    }
}
