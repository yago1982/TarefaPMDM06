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

/**
 * Se definen los elementos comunes a todos los enemigos.
 *
 * @author Yago Mouriño Mendaña
 */
abstract class Enemy extends Ship {

    public static final int WEAK_ENEMY = 0;
    public static final int NORMAL_ENEMY = 1;
    public static final int STRONG_ENEMY = 2;

    private int points;
    private boolean givesPowerUp;
    private int powerUpType;

    public Enemy(int energy, int points) {
        this(energy, points, false, 0);
    }

    public Enemy(int energy, int points, boolean givesPowerUp, int powerUpType) {
        super(energy);

        this.points = points;
        this.givesPowerUp = givesPowerUp;
        this.powerUpType = powerUpType;
    }

    public int getPoints() {
        return points;
    }

    public boolean givesPowerUp() {
        return givesPowerUp;
    }

    public int getPowerUpType() {
        return powerUpType;
    }
}
