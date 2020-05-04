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

package com.ymourino.pmdm06.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ymourino.pmdm06.config.GameConfig;
import com.ymourino.pmdm06.helpers.GlobalStatus;

/**
 *
 * @author Yago Mouriño Mendaña
 */
public class ShipSelectionScreen implements Screen {

    public static final int MENU_OPTIONS_SIZE = 3;
    public static final int MENU_OPTION_RED_SHIP = 0; // Ship.RED_SHIP;
    public static final int MENU_OPTION_GREEN_SHIP = 1; // Ship.GREEN_SHIP;
    public static final int MENU_OPTION_BLUE_SHIP = 2; // Ship.BLUE_SHIP;
    public static final int MENU_FIRST_OPTION = 0;
    public static final int MENU_LAST_OPTION = 2;

    private Game game;
    private SpriteBatch spriteBatch;
    private GlobalStatus globalStatus;
    private GameConfig gameConfig;

    private Texture background;
    private Texture backgroundSelection;

    private Texture redShip;
    private Texture greenShip;
    private Texture blueShip;

    private Texture leftArrow;
    private Texture rightArrow;

    private int selectedShip;

    public ShipSelectionScreen(Game game) {
        this.game = game;

        selectedShip = MENU_OPTION_RED_SHIP;

        globalStatus = GlobalStatus.getGlobalStatus();
        gameConfig = GameConfig.getGameConfig();

        spriteBatch = new SpriteBatch();

        background = new Texture("backgrounds/background stars 1.png");
        backgroundSelection = new Texture("backgrounds/selection.png");

        redShip = new Texture("ships/infored.png");
        greenShip = new Texture("ships/infogreen.png");
        blueShip = new Texture("ships/infoblue.png");

        leftArrow = new Texture("ui/leftarrow.png");
        rightArrow = new Texture("ui/rightarrow.png");
    }
    @Override
    public void show() {}

    // LOOP //
    @Override
    public void render(float delta) {
        if (!globalStatus.isOnPause()) {
            Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            spriteBatch.begin();

            spriteBatch.draw(background, 0.0f, globalStatus.scrollBackgroundTileA(delta));
            spriteBatch.draw(background, 0.0f, globalStatus.scrollBackgroundTileB(delta));

            spriteBatch.draw(backgroundSelection, 0.0f, 0.0f);

            spriteBatch.draw(leftArrow, 50.0f, 536.0f);
            spriteBatch.draw(rightArrow, 499.0f, 536.0f);

            // Menú circular para la selección de la nave del jugador.
            if (selectedShip == MENU_OPTION_RED_SHIP) {
                spriteBatch.draw(redShip, 0.0f, 0.0f);
            } else if (selectedShip == MENU_OPTION_GREEN_SHIP) {
                spriteBatch.draw(greenShip, 0.0f, 0.0f);
            } else if (selectedShip == MENU_OPTION_BLUE_SHIP) {
                spriteBatch.draw(blueShip, 0.0f, 0.0f);
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                selectedShip = selectedShip > MENU_FIRST_OPTION
                        ? (selectedShip -= 1)
                        : (selectedShip = MENU_LAST_OPTION);
                globalStatus.getSoundPlayer().playMenuChange();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                selectedShip = selectedShip < MENU_LAST_OPTION
                        ? (selectedShip += 1)
                        : (selectedShip = MENU_FIRST_OPTION);
                globalStatus.getSoundPlayer().playMenuChange();
            }

            // Con la tecla escape volvemos a la pantalla principal.
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                this.game.setScreen(new WelcomeScreen(this.game));
                globalStatus.getSoundPlayer().playExitScreen();
                this.dispose();
            }

            // Con la tecla enter activamos la nave y empieza el juego.
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                globalStatus.setGameOver(false);
                this.game.setScreen(new GameScreen(this.game, selectedShip));
                this.dispose();
            }

            spriteBatch.end();
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {
        globalStatus.pauseGame(true);
    }

    @Override
    public void resume() {
        globalStatus.resumeGame();
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        // TODO: descartar todos los recursos
    }
}
