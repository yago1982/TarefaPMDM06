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
 * Se definen las características y comportamiento del enemigo más débil del juego.
 *
 * @author Yago Mouriño Mendaña
 */
public class WeakEnemy extends Enemy {

    private static Texture sprite;
    private float xCoord;
    private float yCoord;
    private double trajectory;

    private GlobalStatus globalStatus;

    public WeakEnemy() {
        this(false, 0);
    }

    public WeakEnemy(boolean givesPowerUp, int powerUpType) {
        super(0, 10, givesPowerUp, powerUpType);

        if (sprite == null) {
            sprite = new Texture("ships/weakenemy.png");
        }

        xCoord = GameConstantData.GAME_WIDTH / 2.0f - getSprite().getWidth() / 2.0f;
        yCoord = GameConstantData.GAME_HEIGHT + 100.0f;
        //xCoord = GameConstantData.GAME_WIDTH + 50.0f;
        //yCoord = 0.0f;//GameConstantData.GAME_HEIGHT / 2.0f - sprite.getHeight() / 2.0f;
        trajectory = 0;

        globalStatus = GlobalStatus.getGlobalStatus();
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        yCoord -= 150.0f * delta;
        xCoord += 20 * Math.cos(Math.toRadians(trajectory += 4.5f));

        //yCoord += 20 * Math.sin(Math.toRadians(trajectory += 4.5f));
        //xCoord -= 150.0f * delta;
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
