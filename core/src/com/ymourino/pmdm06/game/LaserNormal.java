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

/**
 *
 * @author Yago Mouriño Mendaña
 */
public class LaserNormal extends Laser {

    private static Texture sprite;
    private float xCoord;
    private float yCoord;

    public LaserNormal(float xCoord, float yCoord, int energy) {
        super(energy);

        if (sprite == null) {
            sprite = new Texture("lasers/laser1.png");
        }

        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    @Override
    public void update(float delta) {
        yCoord += (GameConstantData.LASER_BASE_SPEED * delta);
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
