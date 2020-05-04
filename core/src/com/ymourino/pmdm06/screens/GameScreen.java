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
import com.ymourino.pmdm06.game.ShooterGame;
import com.ymourino.pmdm06.helpers.GlobalStatus;
import com.ymourino.pmdm06.helpers.Records;
import com.ymourino.pmdm06.helpers.SoundPlayer;

/**
 *
 * @author Yago Mouriño Mendaña
 */
public class GameScreen implements Screen {
    private Game game;
    private SpriteBatch spriteBatch;
    private GlobalStatus globalStatus;

    private Texture background;
    private Texture pauseIndicator;
    private Texture exitConfirmation;
    private Texture gameOver;

    private boolean askingToExit;

    private int shipType;
    private ShooterGame shooterGame;

    public GameScreen(Game game, int shipType) {
        this.game = game;

        globalStatus = GlobalStatus.getGlobalStatus();
        globalStatus.setPointsToZero();
        globalStatus.getSoundPlayer().stopMusic();
        globalStatus.getSoundPlayer().playMusic(SoundPlayer.MUSIC_GAME);

        this.shipType = shipType;
        spriteBatch = new SpriteBatch();

        background = new Texture("backgrounds/background stars 1.png");
        pauseIndicator = new Texture("backgrounds/pausa.png");
        exitConfirmation = new Texture("backgrounds/confexit.png");
        gameOver = new Texture("backgrounds/gameover.png");

        askingToExit = false;

        shooterGame = new ShooterGame(shipType);
    }

    @Override
    public void show() {}

    // LOOP //
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();

        spriteBatch.draw(background, 0.0f, globalStatus.scrollBackgroundTileA(delta));
        spriteBatch.draw(background, 0.0f, globalStatus.scrollBackgroundTileB(delta));

        if (Gdx.input.isKeyJustPressed(Input.Keys.P) && (!askingToExit && !globalStatus.isGameOver())) {
            if (globalStatus.isOnPause()) {
                globalStatus.resumeGame();
            } else {
                globalStatus.pauseGame(false);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !globalStatus.isOnPause()) {
            globalStatus.pauseGame(false);
            askingToExit = true;
        }

        // Se dibujan todos los elementos del juego antes de comprobar si se ha pausado, para que
        // permanezcan en pantalla.
        shooterGame.render(spriteBatch);

        // Se comprueban todas las posibilidades por las que el juego puede estar parado y se
        // responde al teclado dependendiendo de la posibilidad activa.
        if (globalStatus.isOnPause()) {
            if (askingToExit) {
                spriteBatch.draw(exitConfirmation, 0.0f, 0.0f);

                if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
                    globalStatus.resumeGame();
                    globalStatus.getSoundPlayer().stopMusic();
                    this.game.setScreen(new WelcomeScreen(this.game));
                    globalStatus.getSoundPlayer().playExitScreen();
                    this.dispose();
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.N)) {
                    askingToExit = false;
                    globalStatus.resumeGame();
                }
            } else if (globalStatus.isGameOver()) {
                Records records = new Records();
                if (globalStatus.getPoints() > records.getLowerRecord()) {
                    globalStatus.resumeGame();
                    this.game.setScreen(new EnterNameScreen(this.game));
                    this.dispose();
                } else {
                    spriteBatch.draw(gameOver, 0.0f, 0.0f);

                    if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                        globalStatus.resumeGame();
                        globalStatus.getSoundPlayer().stopMusic();
                        this.game.setScreen(new WelcomeScreen(this.game));
                        globalStatus.getSoundPlayer().playExitScreen();
                        this.dispose();
                    }
                }
            } else {
                spriteBatch.draw(pauseIndicator, 0.0f, 0.0f);
            }
        } else {
            // Si el juego no está en pausa, la partida sigue.
            shooterGame.update(delta, spriteBatch);
            shooterGame.checkCollisions();
        }

        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {
        globalStatus.pauseGame(true);
    }

    @Override
    public void resume() {
        globalStatus.getSoundPlayer().playMusic();
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        // TODO: descartar todos los recursos
    }
}
