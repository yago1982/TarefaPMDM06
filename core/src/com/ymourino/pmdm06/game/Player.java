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

package com.ymourino.pmdm06.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Align;
import com.ymourino.pmdm06.helpers.GameConstantData;
import com.ymourino.pmdm06.helpers.GlobalStatus;

/**
 * Todas las naves del juegador tienen el mismo comportamiento, que se implementa en esta clase
 * abstracta.
 *
 * @author Yago Mouriño Mendaña
 */
public abstract class Player extends Ship {

    public static final int RED_SHIP = 0;
    public static final int GREEN_SHIP = 1;
    public static final int BLUE_SHIP = 2;

    public static final int LASER_NORMAL = 0;
    public static final int LASER_UPGRADED = 1;
    public static final int LASER_ALL = 2;

    private static Texture sprite;
    private float xCoord;
    private float yCoord;
    private int speedModificator;

    private int shootEnergy;
    private int laserType;
    private int specialAttackCounter;
    private Texture specialAttackIndicator;
    private Texture shieldIndicator;
    private float currentShootTimer;
    private float oldShootTimer;

    private BitmapFont shieldFont;

    private GlobalStatus globalStatus;

    public Player(String spriteRoute, int shootEnergy, int energy, int speedModificator) {
        super(energy);

        sprite = new Texture(spriteRoute);
        xCoord = GameConstantData.GAME_WIDTH / 2.0f - getSprite().getWidth() / 2.0f;
        yCoord = 50;
        this.speedModificator = speedModificator;

        this.shootEnergy = shootEnergy;
        laserType = LASER_NORMAL;
        specialAttackCounter = 3;
        specialAttackIndicator = new Texture("powerups/specialindicator.png");
        shieldIndicator = new Texture("ships/shieldindicator.png");
        currentShootTimer = 0.0f;
        oldShootTimer = 0.0f;

        globalStatus = GlobalStatus.getGlobalStatus();

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/kenvector_future.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 26;

        fontParameter.color = new Color(200.0f / 255.0f, 200.0f / 255.0f, 200.0f / 255.0f, 1);
        shieldFont = fontGenerator.generateFont(fontParameter);

        fontGenerator.dispose();
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        moveX(delta);
        moveY(delta);
        shoot(delta);

        cheats();
    }

    /**
     * Se dibuja la nave en las coordenadas adecuadas.
     *
     * @param spriteBatch
     */
    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(shieldIndicator, 551, 25);
        if (getEnergy() > 0) {
            shieldFont.draw(spriteBatch, String.valueOf(getEnergy()) + " %", 327, 45, 200, Align.right, false);
        } else {
            shieldFont.draw(spriteBatch, "0 %", 327, 45, 200, Align.right, false);
        }

        for (int i = 0; i < specialAttackCounter; i++) {
            spriteBatch.draw(specialAttackIndicator, 25 + 24 * i + 12 * i, 25);
        }

        super.render(spriteBatch);
    }

    @Override
    public Texture getSprite() {
        return sprite;
    }

    @Override
    public final float getX() {
        return xCoord;
    }

    @Override
    public final float getY() {
        return yCoord;
    }

    /**
     * Mueve la nave en el eje X al mismo tiempo que evita comportamientos extraños si se pulsan
     * las teclas de "izquierda" y "derecha" a la vez. También se hacen comprobaciones para evitar
     * que la nave salga fuera de los límites de la pantalla de juego.
     *
     * TODO: añadir inercia al movimiento.
     *
     * @param delta
     */
    private void moveX(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.LEFT))
            if (xCoord + getSprite().getWidth() < GameConstantData.GAME_WIDTH) {
                xCoord += (GameConstantData.PLAYER_BASE_SPEED * speedModificator) * delta;

                if (xCoord > GameConstantData.GAME_WIDTH - getSprite().getWidth())
                    xCoord = GameConstantData.GAME_WIDTH - getSprite().getWidth();
            }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            if (xCoord > 0) {
                xCoord -= (GameConstantData.PLAYER_BASE_SPEED * speedModificator) * delta;

                if (xCoord < 0)
                    xCoord = 0;
            }
    }

    /**
     * Mueve la nave en el eje Y al mismo tiempo que evita comportamientos extraños si se pulsan
     * las teclas de "arriba" y "abajo" a la vez. También se hacen comprobaciones para evitar
     * que la nave salga fuera de los límites de la pantalla de juego.
     *
     * TODO: añadir inercia al movimiento.
     *
     * @param delta
     */
    private void moveY(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN))
            if (yCoord + getSprite().getHeight() < GameConstantData.GAME_HEIGHT) {
                yCoord += (GameConstantData.PLAYER_BASE_SPEED * speedModificator) * delta;

                if (yCoord > GameConstantData.GAME_HEIGHT - getSprite().getHeight())
                    yCoord = GameConstantData.GAME_HEIGHT - getSprite().getHeight();
            }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && !Gdx.input.isKeyPressed(Input.Keys.UP))
            if (yCoord > 0) {
                yCoord -= (GameConstantData.PLAYER_BASE_SPEED * speedModificator) * delta;

                if (yCoord < 0)
                    yCoord = 0;
            }
    }

    /**
     * Se realizan los disparos si el jugador ha pulsado las teclas correspondientes. Se permite al
     * jugador pulsar repetidamente la tecla de disparo de modo que la cadencia de tiro sea la que
     * consiga con sus pulsaciones. Si mantiene la tecla pulsada se dispararán aproximadamente
     * cinco tiros por segundo. Para el disparo especial habrá que pulsar la tecla "ALT" una vez
     * por cada disparo que se quiera realizar.
     *
     * @param delta
     */
    private void shoot(float delta) {
        currentShootTimer += delta;

        if ((Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && currentShootTimer - oldShootTimer > 0.20f) ||
                Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
            oldShootTimer = currentShootTimer;
            globalStatus.getSoundPlayer().playLaserSound();

            switch (laserType) {
                case LASER_NORMAL:
                    shootLaserNormal();
                    break;
                case LASER_UPGRADED:
                    shootLaserUpgraded();
                    break;
                case LASER_ALL:
                    shootLaserUpgraded();
                    shootLaserBack();
                    break;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT) && specialAttackCounter > 0) {
            specialAttackCounter--;
            globalStatus.getSoundPlayer().playLaserSpecialSound();
            shootSpecial();
        }
    }

    /**
     * Disparo normal.
     */
    private void shootLaserNormal() {
        float preCalcX = getX() + getSprite().getWidth() / 2.0f;
        float preCalcY = getY() + 14.0f;

        addLasers(
                new LaserNormal(preCalcX - 32.0f, preCalcY, shootEnergy),
                new LaserNormal(preCalcX + 28.0f, preCalcY, shootEnergy)
        );
    }

    /**
     * Disparo potenciado.
     */
    private void shootLaserUpgraded() {
        float preCalcX = getX() + getSprite().getWidth() / 2.0f;
        float preCalcY = getY();

        addLasers(
                new LaserUpgraded(preCalcX - 32.0f, preCalcY + 16.0f, shootEnergy),
                new LaserUpgraded(preCalcX - 18.0f, preCalcY + 24.0f, shootEnergy),
                new LaserUpgraded(preCalcX -  3.0f, preCalcY + 32.0f, shootEnergy),
                new LaserUpgraded(preCalcX + 12.0f, preCalcY + 24.0f, shootEnergy),
                new LaserUpgraded(preCalcX + 26.0f, preCalcY + 16.0f, shootEnergy)
        );
    }

    /**
     * Disparo hacia abajo.
     */
    private void shootLaserBack() {
        float preCalcX = getX() + getSprite().getWidth()  / 2.0f - 5.0f;
        float preCalcY = getY() + getSprite().getHeight() / 2.0f - 5.0f;

        addLasers(
                new LaserBack(preCalcX, preCalcY, 225.0f, shootEnergy),
                new LaserBack(preCalcX, preCalcY, 247.5f, shootEnergy),
                new LaserBack(preCalcX, preCalcY, 270.0f, shootEnergy),
                new LaserBack(preCalcX, preCalcY, 292.5f, shootEnergy),
                new LaserBack(preCalcX, preCalcY, 315.0f, shootEnergy)
        );
    }

    /**
     * Disparo especial.
     */
    private void shootSpecial() {
        float preCalcX = getX() + getSprite().getWidth()  / 2.0f - 5.0f;
        float preCalcY = getY() + getSprite().getHeight() / 2.0f - 5.0f;

        for (int i = 0; i < 360; i += 5)
            addLasers(new LaserSpecial(preCalcX, preCalcY, i, 1000));
    }

    /**
     * Se definen algunos trucos en el juego:
     *  - K + O: suma 3 ataques especiales hasta un máximo total de 9.
     *  - K + T: sube al máximo los ataques.
     *  - K + U: sube al máximo la velocidad de la nave.
     *  - O + K: ...
     *  - O + T: ...
     *  - O + U: ...
     *  - T + K: ...
     *  - T + O: ...
     *  - T + U: ...
     *  - U + K: ...
     *  - U + O: ...
     *  - U + T: ...
     *
     *  Cabe destacar que el orden en que se pulsen las teclas será importante.
     */
    private void cheats() {
        if (Gdx.input.isKeyPressed(Input.Keys.K) && Gdx.input.isKeyJustPressed(Input.Keys.O)) {
            specialAttackCounter += 3;

            if (specialAttackCounter > 9)
                specialAttackCounter = 9;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.K) && Gdx.input.isKeyJustPressed(Input.Keys.T))
                laserType = LASER_ALL;

        if (Gdx.input.isKeyPressed(Input.Keys.K) && Gdx.input.isKeyJustPressed(Input.Keys.U))
                speedModificator = 5;
    }

    /**
     * Se restaura la energía de la nave. Se llama este método desde la gestión de colisiones
     * cuando cogemos el potenciador adecuado.
     */
    public void restoreShield() {
        setEnergy(100);
    }

    /**
     * Se incrementa en un punto la velocidad de la nave hasta el máximo de cinco puntos.
     */
    public void incrementSpeed() {
        if (speedModificator < 5) {
            speedModificator++;
        } else {
            globalStatus.addPoints(100);
        }
    }

    /**
     * Se incrementa el tipo de disparo hasta el máximo definido.
     */
    public void incrementShoot() {
        if (laserType < LASER_ALL) {
            laserType++;
        } else {
            globalStatus.addPoints(100);
        }
    }

    /**
     * Se incrementa el disparo especial hasta el máximo de nueve disparos.
     */
    public void incrementSpecialShoot() {
        if (specialAttackCounter < 9) {
            specialAttackCounter++;
        } else {
            globalStatus.addPoints(100);
        }
    }
}
