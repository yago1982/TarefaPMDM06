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

package com.ymourino.pmdm06.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ymourino.pmdm06.SpaceShooter;
import com.ymourino.pmdm06.helpers.GameConstantData;

/**
 *
 * @author Yago Mouriño Mendaña
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		// 60 FPS.
		config.foregroundFPS = 60;

		// Título de la ventana y dimensiones.
		config.title = "Space Shooter v0.1.0";
		config.width = GameConstantData.GAME_WIDTH;
		config.height = GameConstantData.GAME_HEIGHT;

		// Se establecen a true "pauseWhenBackground" y "pauseWhenMinimized" para que se pause
		// el juego si la ventana pierde el foco o si se minimiza.
		config.pauseWhenBackground = true;
		config.pauseWhenMinimized = true;

		// No se podrá redimensionar la ventana.
		config.resizable = false;

		new LwjglApplication(new SpaceShooter(), config);
	}
}
