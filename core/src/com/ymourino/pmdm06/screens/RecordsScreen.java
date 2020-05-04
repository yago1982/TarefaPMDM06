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
import com.ymourino.pmdm06.helpers.GlobalStatus;
import com.ymourino.pmdm06.helpers.Records;

import java.util.Map;

/**
 *
 * @author Yago Mouriño Mendaña
 */
public class RecordsScreen implements Screen {

    private Game game;
    private SpriteBatch spriteBatch;
    private GlobalStatus globalStatus;

    private Texture background;

    private int savedMenuOption;
    private float yRecords;
    private float backTimer;
    private float timerExit;
    private int newRecord;
    private float flashCounter;

    private BitmapFont fontTitles;
    private BitmapFont fontData;

    private Records records;

    public RecordsScreen(Game game, int savedMenuOption) {
        this(game, savedMenuOption, 0, 30.0f);
    }

    public RecordsScreen(Game game, int savedMenuOption, float timerExit) {
        this(game, savedMenuOption, 0, timerExit);
    }

    public RecordsScreen(Game game, int savedMenuOption, int newRecord, float timerExit) {
        this.game = game;

        globalStatus = GlobalStatus.getGlobalStatus();

        // Guardamos la opción del menú principal que estaba seleccionada para volver a activarla
        // cuando regresemos a la pantalla de título.
        this.savedMenuOption = savedMenuOption;
        yRecords = -100.0f;
        backTimer = 0.0f;
        this.timerExit = timerExit;
        this.newRecord = newRecord;

        flashCounter = 0;

        spriteBatch = new SpriteBatch();

        background = new Texture("backgrounds/background stars 1.png");

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/kenvector_future.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 42;

        fontParameter.color = new Color(200.0f / 255.0f, 200.0f / 255.0f, 200.0f / 255.0f, 1);
        fontTitles = fontGenerator.generateFont(fontParameter);

        fontParameter.color = new Color(112.0f / 255.0f, 112.0f / 255.0f, 112.0f / 255.0f, 1);
        fontData = fontGenerator.generateFont(fontParameter);

        fontGenerator.dispose();

        records = new Records();
    }

    @Override
    public void show() {}

    // LOOP //
    @Override
    public void render(float delta) {
        if (!globalStatus.isOnPause()) {
            int flasher = 1;
            Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            spriteBatch.begin();

            spriteBatch.draw(background, 0.0f, globalStatus.scrollBackgroundTileA(delta));
            spriteBatch.draw(background, 0.0f, globalStatus.scrollBackgroundTileB(delta));

            // Se va actualizando el contador para salir de la pantalla si se alcanza el límite
            // establecido.
            if (timerExit > 0.0f) {
                backTimer += delta;

                if (backTimer > timerExit) {
                    this.game.setScreen(new WelcomeScreen(this.game, savedMenuOption));
                    this.dispose();
                }
            }

            // Se puede salir de la pantalla de records si se pulsa la tecla escape, siempre y
            // cuándo la tabla no se esté mostrando por haber terminado una partida entre los diez
            // mejores.
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && newRecord == 0) {
                this.game.setScreen(new WelcomeScreen(this.game, savedMenuOption));
                globalStatus.getSoundPlayer().playExitScreen();
                this.dispose();
            }

            if (yRecords < 725.0f) {
                yRecords += 240.0f * delta;
            }

            // Usamos el contador flashCounter para hacer parpadear el récord recién conseguido.
            flashCounter += 120.0f * delta;

            if (flashCounter < 60.0f)
                printRecords(yRecords, false);
            else
                printRecords(yRecords, true);

            if (flashCounter > 120.0f)
                flashCounter = 0.0f;

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

    private void printRecords(float y, boolean showNewRecord) {
        fontTitles.draw(spriteBatch, "pos.", 47, y);
        fontTitles.draw(spriteBatch, "pts.", 247, y);
        fontTitles.draw(spriteBatch, "jug.", 447, y);

        int i = 1;

        for (Map.Entry<Integer, String> record : records.entrySet()) {
            if (record.getKey() == newRecord && newRecord > 0) {
                if (showNewRecord) {
                    fontData.draw(spriteBatch, String.valueOf(i) + ".", 47, y - 30 - (60 * i), 111, Align.right, false);
                    fontData.draw(spriteBatch, String.valueOf(record.getKey()), 173, y - 30 - (60 * i), 248, Align.center, false);
                    fontData.draw(spriteBatch, record.getValue(), 426, y - 30 - (60 * i), 127, Align.right, false);
                }
            } else {
                fontData.draw(spriteBatch, String.valueOf(i) + ".", 47, y - 30 - (60 * i), 111, Align.right, false);
                fontData.draw(spriteBatch, String.valueOf(record.getKey()), 173, y - 30 - (60 * i), 248, Align.center, false);
                fontData.draw(spriteBatch, record.getValue(), 426, y - 30 - (60 * i), 127, Align.right, false);
            }

            i++;
        }
    }
}
