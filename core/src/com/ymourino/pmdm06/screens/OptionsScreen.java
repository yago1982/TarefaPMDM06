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
public class OptionsScreen implements Screen {

    public static final int MENU_OPTIONS_SIZE = 4;
    public static final int MENU_OPTION_VOLUME_MUSIC = 0;
    public static final int MENU_OPTION_VOLUME_FX = 1;
    public static final int MENU_OPTION_VOLUME_UI = 2;
    public static final int MENU_OPTION_EXIT = 3;
    public static final int MENU_FIRST_OPTION = 0;
    public static final int MENU_LAST_OPTION = 3;

    private Game game;
    private SpriteBatch spriteBatch;
    private GlobalStatus globalStatus;
    private GameConfig gameConfig;

    private Texture background;
    private Texture optionsTitle;

    private Texture volMusicOption;
    private Texture volMusicOptionSelected;
    private Texture volFxOption;
    private Texture volFxOptionSelected;
    private Texture volUiOption;
    private Texture volUiOptionSelected;
    private Texture backOption;
    private Texture backOptionSelected;

    private Texture scaleItem;
    private Texture scaleItemActive;

    private int optionSelected;

    public OptionsScreen(Game game) {
        this.game = game;

        gameConfig = GameConfig.getGameConfig();
        optionSelected = MENU_FIRST_OPTION;

        globalStatus = GlobalStatus.getGlobalStatus();

        spriteBatch = new SpriteBatch();

        background = new Texture("backgrounds/background stars 1.png");
        optionsTitle = new Texture("backgrounds/options.png");

        volMusicOption = new Texture("buttons/volmusic.png");
        volMusicOptionSelected = new Texture("buttons/volmusic_selected.png");
        volFxOption = new Texture("buttons/volfx.png");
        volFxOptionSelected = new Texture("buttons/volfx_selected.png");
        volUiOption = new Texture("buttons/volui.png");
        volUiOptionSelected = new Texture("buttons/volui_selected.png");
        backOption = new Texture("buttons/volver.png");
        backOptionSelected = new Texture("buttons/volver_selected.png");

        scaleItem = new Texture("indicators/scaleitem_inactive2.png");
        scaleItemActive = new Texture("indicators/scaleitem_active2.png");
    }

    @Override
    public void show() {}

    // LOOP //
    @Override
    public void render(float delta) {
        if (!globalStatus.isOnPause()) {
            int volMusic = (int) (gameConfig.getAudioConfig().getMusicVolume() * 10);
            int volFx = (int) (gameConfig.getAudioConfig().getFxVolume() * 10);
            int volUi = (int) (gameConfig.getAudioConfig().getUiVolume() * 10);

            Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            spriteBatch.begin();

            spriteBatch.draw(background, 0.0f, globalStatus.scrollBackgroundTileA(delta));
            spriteBatch.draw(background, 0.0f, globalStatus.scrollBackgroundTileB(delta));

            spriteBatch.draw(optionsTitle, 0.0f,0.0f);

            // Menú circular con las opciones de configuración del juego.
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

            if (optionSelected == MENU_OPTION_VOLUME_MUSIC) {
                spriteBatch.draw(volMusicOptionSelected, 0.0f, 500.0f);
            } else {
                spriteBatch.draw(volMusicOption, 0.0f, 500.0f);
            }

            for (int i = 0; i < 10; i++) {
                if (volMusic > i) {
                    spriteBatch.draw(scaleItemActive, 25.0f + 25 * i, 450.0f);
                } else {
                    spriteBatch.draw(scaleItem, 25.0f + 25 * i, 450.0f);
                }
            }

            if (optionSelected == MENU_OPTION_VOLUME_FX) {
                spriteBatch.draw(volFxOptionSelected, 0.0f, 350.0f);
            } else {
                spriteBatch.draw(volFxOption, 0.0f, 350.0f);
            }

            for (int i = 0; i < 10; i++) {
                if (volFx > i) {
                    spriteBatch.draw(scaleItemActive, 25.0f + 25 * i, 300.0f);
                } else {
                    spriteBatch.draw(scaleItem, 25.0f + 25 * i, 300.0f);
                }
            }

            if (optionSelected == MENU_OPTION_VOLUME_UI) {
                spriteBatch.draw(volUiOptionSelected, 0.0f, 200.0f);
            } else {
                spriteBatch.draw(volUiOption, 0.0f, 200.0f);
            }

            for (int i = 0; i < 10; i++) {
                if (volUi > i) {
                    spriteBatch.draw(scaleItemActive, 25.0f + 25 * i, 150.0f);
                } else {
                    spriteBatch.draw(scaleItem, 25.0f + 25 * i, 150.0f);
                }
            }

            if (optionSelected == MENU_OPTION_EXIT) {
                spriteBatch.draw(backOptionSelected, 0.0f, 50.0f);
            } else {
                spriteBatch.draw(backOption, 0.0f, 50.0f);
            }

            // Con las teclas de dirección izquierda y derecha se modifican los volúmenes.
            // Además los cambios se pueden escuchar en tiempo real, pues con cada pulsación
            // se reproducen los sonidos correspondientes con el nuevo volumen.
            if (optionSelected == MENU_OPTION_VOLUME_MUSIC) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && volMusic < 10) {
                    volMusic++;
                    gameConfig.getAudioConfig().setMusicVolume(volMusic / 10.0f);

                    if (volMusic == 1) {
                        globalStatus.getSoundPlayer().playMusic(0);
                    }
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && volMusic > 0) {
                    volMusic--;
                    gameConfig.getAudioConfig().setMusicVolume(volMusic / 10.0f);

                    if (volMusic == 0) {
                        globalStatus.getSoundPlayer().stopMusic();
                    }
                }

                globalStatus.getSoundPlayer().setVolume(volMusic / 10.0f);
            } else if (optionSelected == MENU_OPTION_VOLUME_FX) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && volFx < 10) {
                    volFx++;
                    gameConfig.getAudioConfig().setFxVolume(volFx / 10.0f);
                    globalStatus.getSoundPlayer().playLaserSound();
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && volFx > 0) {
                    volFx--;
                    gameConfig.getAudioConfig().setFxVolume(volFx / 10.0f);
                    globalStatus.getSoundPlayer().playLaserSound();
                }
            } else if (optionSelected == MENU_OPTION_VOLUME_UI) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && volUi < 10) {
                    volUi++;
                    gameConfig.getAudioConfig().setUiVolume(volUi / 10.0f);
                    globalStatus.getSoundPlayer().playMenuChange();
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && volUi > 0) {
                    volUi--;
                    gameConfig.getAudioConfig().setUiVolume(volUi / 10.0f);
                    globalStatus.getSoundPlayer().playMenuChange();
                }
            } else if (optionSelected == MENU_OPTION_EXIT) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    GameConfig.saveGameConfig();
                    this.game.setScreen(new WelcomeScreen(this.game,WelcomeScreen.MENU_OPTION_OPTIONS));
                    globalStatus.getSoundPlayer().playExitScreen();
                    this.dispose();
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
