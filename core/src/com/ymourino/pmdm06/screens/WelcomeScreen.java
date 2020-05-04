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
import com.ymourino.pmdm06.config.AudioConfig;
import com.ymourino.pmdm06.config.GameConfig;
import com.ymourino.pmdm06.helpers.GameConstantData;
import com.ymourino.pmdm06.helpers.GlobalStatus;
import com.ymourino.pmdm06.helpers.SoundPlayer;

/**
 *
 * @author Yago Mouriño Mendaña
 */
public class WelcomeScreen implements Screen {

    public static final int MENU_OPTIONS_SIZE = 4;
    public static final int MENU_OPTION_PLAY = 0;
    public static final int MENU_OPTION_OPTIONS = 1;
    public static final int MENU_OPTION_CREDITS = 2;
    public static final int MENU_OPTION_EXIT = 3;
    public static final int MENU_FIRST_OPTION = 0;
    public static final int MENU_LAST_OPTION = 3;

    private Game game;
    private SpriteBatch spriteBatch;
    private GlobalStatus globalStatus;

    private Texture logo;
    private Texture background;

    private Texture playButton;
    private Texture playButtonSelected;
    private Texture optionsButton;
    private Texture optionsButtonSelected;
    private Texture creditsButton;
    private Texture creditsButtonSelected;
    private Texture exitButton;
    private Texture exitButtonSelected;
    private Texture pause;

    private int optionSelected;
    private float recordsTimer;

    GameConfig gameConfig;
    AudioConfig audioConfig;

    public WelcomeScreen(Game game) {
        this(game, 0);
    }

    public WelcomeScreen(Game game, int initialMenuOption) {
        this.game = game;

        globalStatus = GlobalStatus.getGlobalStatus();
        globalStatus.getSoundPlayer().playMusic(SoundPlayer.MUSIC_MENU);

        optionSelected = initialMenuOption;
        recordsTimer = 0.0f;

        spriteBatch = new SpriteBatch();

        logo = new Texture("titles/logo.png");
        background = new Texture("backgrounds/background stars 1.png");

        playButton = new Texture("buttons/jugar_normal.png");
        playButtonSelected = new Texture("buttons/jugar_selected.png");
        optionsButton = new Texture("buttons/opciones_normal.png");
        optionsButtonSelected = new Texture("buttons/opciones_selected.png");
        creditsButton = new Texture("buttons/creditos_normal.png");
        creditsButtonSelected = new Texture("buttons/creditos_selected.png");
        exitButton = new Texture("buttons/salir_normal.png");
        exitButtonSelected = new Texture("buttons/salir_selected.png");

        pause = new Texture("backgrounds/pausa.png");

        gameConfig = GameConfig.getGameConfig();
        audioConfig = gameConfig.getAudioConfig();
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

            // Si se pulsa cualquier tecla se restablece el contador de tiempo de la pantalla de
            // récords.
            if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                recordsTimer = 0.0f;
            } else {
                recordsTimer += delta;
            }

            // Si el contador de la pantalla de récords llega al límite, los mostramos.
            if (recordsTimer > GameConstantData.GAME_RECORDS_TIMER) {
                this.dispose();
                this.game.setScreen(new RecordsScreen(this.game, optionSelected, GameConstantData.GAME_RECORDS_TIMER));
            }

            spriteBatch.draw(logo, 0.0f, 475.f);

            // Menú circular con las opciones de la pantalla principal.
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                optionSelected = optionSelected > MENU_FIRST_OPTION
                        ? (optionSelected -= 1)
                        : (optionSelected = MENU_LAST_OPTION);
                globalStatus.getSoundPlayer().playMenuChange();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                optionSelected = optionSelected < MENU_LAST_OPTION
                        ? (optionSelected += 1)
                        : (optionSelected = MENU_FIRST_OPTION);
                globalStatus.getSoundPlayer().playMenuChange();
            }

            if (optionSelected == MENU_OPTION_PLAY) {
                spriteBatch.draw(playButtonSelected, 75.0f, 325.0f);
            } else {
                spriteBatch.draw(playButton, 75.0f, 325.0f);
            }

            if (optionSelected == MENU_OPTION_OPTIONS) {
                spriteBatch.draw(optionsButtonSelected, 75.0f, 225.0f);
            } else {
                spriteBatch.draw(optionsButton, 75.0f, 225.0f);
            }

            if (optionSelected == MENU_OPTION_CREDITS) {
                spriteBatch.draw(creditsButtonSelected, 75.0f, 125.0f);
            } else {
                spriteBatch.draw(creditsButton, 75.0f, 125.0f);
            }

            if (optionSelected == MENU_OPTION_EXIT) {
                spriteBatch.draw(exitButtonSelected, 75.0f, 25.0f);
            } else {
                spriteBatch.draw(exitButton, 75.0f, 25.0f);
            }

            // Las opciones las seleccionamos con la tecla enter.
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                if (optionSelected == MENU_OPTION_PLAY) {
                    this.game.setScreen(new ShipSelectionScreen(this.game));
                    globalStatus.getSoundPlayer().playMenuEnter();
                    this.dispose();
                } else if (optionSelected == MENU_OPTION_OPTIONS) {
                    this.game.setScreen(new OptionsScreen(this.game));
                    globalStatus.getSoundPlayer().playMenuEnter();
                    this.dispose();
                } else if (optionSelected == MENU_OPTION_CREDITS) {
                    this.game.setScreen(new CreditsScreen(this.game));
                    globalStatus.getSoundPlayer().playMenuEnter();
                    this.dispose();
                } else if (optionSelected == MENU_OPTION_EXIT) {
                    this.dispose();
                    Gdx.app.exit();
                }
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
