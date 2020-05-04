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
 * "Clase" con algunas constantes utilizadas a lo largo del juego.
 *
 * @author Yago Mouriño Mendaña
 */
public class GameConstantData {

    public static final int GAME_WIDTH = 600;
    public static final int GAME_HEIGHT = 800;

    public static final String GAME_CONFIG_FILE = "config.json";
    public static final String GAME_RECORDS_FILE = "records.json";

    public static final float GAME_RECORDS_TIMER = 30.0f;

    public static final float PLAYER_BASE_SPEED = 120.0f;
    public static final float LASER_BASE_SPEED = 800.0f;
    public static final float POWERUP_SPEED = 200.0f;

    public static final int BASE_ENERGY = 10;
}
