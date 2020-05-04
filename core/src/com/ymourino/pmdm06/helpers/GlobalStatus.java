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

package com.ymourino.pmdm06.helpers;

/**
 * La clase GlobalStatus implementa el patrón singleton y se usa para dar acceso a diferentes partes
 * comunes del juego, como el sonido o la puntuación.
 *
 * @author Yago Mouriño Mendaña
 */
public class GlobalStatus {
    private static GlobalStatus globalStatus;
    private SoundPlayer soundPlayer;

    private boolean onPause;

    private float yTileA;
    private float yTileB;

    private int points;
    private boolean gameOver;

    public static GlobalStatus getGlobalStatus() {
        if (globalStatus == null) {
            globalStatus = new GlobalStatus();
        }

        return globalStatus;
    }

    private GlobalStatus() {
        soundPlayer = SoundPlayer.getSoundPlayer();
        yTileA = 0.0f;
        yTileB = 990.0f;
        gameOver = false;
        onPause = false;
    }

    public SoundPlayer getSoundPlayer() {
        return soundPlayer;
    }

    public boolean isOnPause() {
        return onPause;
    }

    public void pauseGame(boolean shouldPauseMusic) {
        if (shouldPauseMusic) {
            soundPlayer.pauseMusic();
        }

        onPause = true;
    }

    public void resumeGame() {
        soundPlayer.playMusic();
        onPause = false;
    }

    public float scrollBackgroundTileA(float delta) {
        if (!onPause) {
            return yTileA < -990.0f
                    ? (yTileA = 990.0f)
                    : (yTileA -= 30.0f * delta);
        } else {
            return yTileA;
        }
    }

    public float scrollBackgroundTileB(float delta) {
        if (!onPause) {
            return yTileB < -990.0f
                    ? (yTileB = 990.0f)
                    : (yTileB -= 30.0f * delta);
        } else {
            return yTileB;
        }
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setPointsToZero() {
        points = 0;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getPoints() {
        return points;
    }
}
