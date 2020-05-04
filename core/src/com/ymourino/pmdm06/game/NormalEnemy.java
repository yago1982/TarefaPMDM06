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

import com.badlogic.gdx.graphics.Texture;
import com.ymourino.pmdm06.helpers.GameConstantData;
import com.ymourino.pmdm06.helpers.GlobalStatus;


/**
 * Se definen las características y comportamiento del enemigo "normal" del juego.
 *
 * @author Yago Mouriño Mendaña
 */
public class NormalEnemy extends Enemy {

    private static Texture sprite;
    private float xCoord;
    private float yCoord;

    private float laserTimer;
    private float oldTimer;

    private GlobalStatus globalStatus;

    public NormalEnemy(float xCoord) {
        this(false, 0, xCoord);
    }

    public NormalEnemy(boolean givesPowerUp, int powerUpType, float xCoord) {
        super(20, 20, givesPowerUp, powerUpType);

        if (sprite == null) {
            sprite = new Texture("ships/normalenemy.png");
        }

        this.xCoord = xCoord;
        yCoord = GameConstantData.GAME_HEIGHT + 100.0f;

        globalStatus = GlobalStatus.getGlobalStatus();

        laserTimer = 0.0f;
        oldTimer = 0.0f;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        yCoord -= 100.0f * delta;

        laserTimer += delta;
        if (laserTimer - oldTimer > 1.5f) {
            oldTimer = laserTimer;

            addLasers(new LaserEnemy(xCoord + 31, yCoord + 26, 30));
        }
    }

    @Override
    public Texture getSprite() {
        return sprite;
    }

    @Override
    public float getX() {
        return xCoord;
    }

    @Override
    public float getY() {
        return yCoord;
    }
}
