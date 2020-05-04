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
import com.ymourino.pmdm06.helpers.GlobalStatus;

/**
 *
 * @author Yago Mouriño Mendaña
 */
public class CreditsScreen implements Screen {

    private Game game;
    private SpriteBatch spriteBatch;
    private GlobalStatus globalStatus;

    private Texture background;
    private Texture credits;

    private float yCredits;

    public CreditsScreen(Game game) {
        this.game = game;

        globalStatus = GlobalStatus.getGlobalStatus();

        yCredits = -799.0f;

        spriteBatch = new SpriteBatch();

        background = new Texture("backgrounds/background stars 1.png");
        credits = new Texture("backgrounds/credits.png");
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

            spriteBatch.draw(credits, 0.0f, yCredits);

            if (yCredits < 800.0f) {
                yCredits += 60.0f * delta;
            } else {
                this.game.setScreen(new WelcomeScreen(this.game, WelcomeScreen.MENU_OPTION_CREDITS));
                this.dispose();
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                this.game.setScreen(new WelcomeScreen(this.game, WelcomeScreen.MENU_OPTION_CREDITS));
                globalStatus.getSoundPlayer().playExitScreen();
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
