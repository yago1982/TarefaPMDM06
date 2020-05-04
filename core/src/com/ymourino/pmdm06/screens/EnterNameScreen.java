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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Align;
import com.ymourino.pmdm06.config.GameConfig;
import com.ymourino.pmdm06.helpers.GameConstantData;
import com.ymourino.pmdm06.helpers.GlobalStatus;
import com.ymourino.pmdm06.helpers.Records;

/**
 *
 * @author Yago Mouriño Mendaña
 */
public class EnterNameScreen implements Screen {

    public static final int MENU_LETTERS_SIZE = 27;
    public static final int MENU_FIRST_LETTER = 0;
    public static final int MENU_LAST_LETTER = MENU_LETTERS_SIZE - 1;

    private Game game;
    private SpriteBatch spriteBatch;
    private GlobalStatus globalStatus;
    private GameConfig gameConfig;

    private Texture background;
    private Texture backgroundEnterName;

    private String[] letterStrings = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
            "m", "n", "ñ", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private Texture[] letterTextures;
    private String iniciales;

    private Texture leftArrow;
    private Texture rightArrow;

    private BitmapFont fontData;

    private int selectedLetter;

    public EnterNameScreen(Game game) {
        this.game = game;

        selectedLetter = MENU_FIRST_LETTER;

        globalStatus = GlobalStatus.getGlobalStatus();
        gameConfig = GameConfig.getGameConfig();

        spriteBatch = new SpriteBatch();

        background = new Texture("backgrounds/background stars 1.png");
        backgroundEnterName = new Texture("backgrounds/iniciales.png");

        // Se cargan las imágenes para cada letra.
        letterTextures = new Texture[MENU_LETTERS_SIZE];
        for (int i = 0; i < letterStrings.length; i++) {
            letterTextures[i] = new Texture("letters/" + letterStrings[i] + ".png");
        }
        iniciales = "";

        leftArrow = new Texture("ui/leftarrow.png");
        rightArrow = new Texture("ui/rightarrow.png");

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/kenvector_future.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 96;

        fontParameter.color = new Color(1.0f, 1.0f, 1.0f, 1);
        fontData = fontGenerator.generateFont(fontParameter);

        fontGenerator.dispose();

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

            spriteBatch.draw(backgroundEnterName, 0.0f, 0.0f);

            spriteBatch.draw(leftArrow, 50.0f, 536.0f);
            spriteBatch.draw(rightArrow, 499.0f, 536.0f);

            spriteBatch.draw(letterTextures[selectedLetter], 250.0f, 536.0f);

            // Menú circular con las letras para introducir las iniciales.
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                selectedLetter = selectedLetter > MENU_FIRST_LETTER
                        ? (selectedLetter -= 1)
                        : (selectedLetter = MENU_LAST_LETTER);
                globalStatus.getSoundPlayer().playMenuChange();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                selectedLetter = selectedLetter < MENU_LAST_LETTER
                        ? (selectedLetter += 1)
                        : (selectedLetter = MENU_FIRST_LETTER);
                globalStatus.getSoundPlayer().playMenuChange();
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                iniciales += letterStrings[selectedLetter];

                if (iniciales.length() == 3) {
                    Records records = new Records();

                    records.put(globalStatus.getPoints(), iniciales);

                    this.dispose();
                    this.game.setScreen(new RecordsScreen(this.game, 0, globalStatus.getPoints(), GameConstantData.GAME_RECORDS_TIMER));
                }
            }

            // TODO: posibilidad de corregir iniciales incorrectas y confirmación antes de guardar
            //  el récord.

            fontData.draw(spriteBatch, iniciales, 100, 450, 400, Align.center, false);
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
