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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Intersector;
import com.ymourino.pmdm06.helpers.GlobalStatus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Esta clase gestiona realmente el juego y se encarga de la creación de enemigos y gestión de las
 * colisiones. Necesitaría una refactorización completa y posiblemente dividirla en clases más
 * pequeñas y especializadas.
 *
 * @author Yago Mouriño Mendaña
 */
public class ShooterGame {

    private GlobalStatus globalStatus;
    private Ship player;

    private float currentTimer;
    private float oldTimer;
    private float currentDifficultyTimer;
    private float oldDifficultyTimer;
    private int enemyCounter;
    private int totalEnemyCounter;
    private int limitEnemies;

    private BitmapFont fontScore;

    private List<Enemy> enemies;
    private List<PowerUp> powerUps;

    private Random randomGenerator;

    public ShooterGame(int shipType) {
        globalStatus = GlobalStatus.getGlobalStatus();

        currentTimer = 0.0f;
        oldTimer = 0.0f;
        currentDifficultyTimer = 0.0f;
        oldDifficultyTimer = 0.0f;
        enemyCounter = 0;
        totalEnemyCounter = 0;
        limitEnemies = 3;

        enemies = new ArrayList<>();
        powerUps = new ArrayList<>();

        switch (shipType) {
            case Player.RED_SHIP:
                player = new RedShip();
                break;
            case Player.GREEN_SHIP:
                player = new GreenShip();
                break;
            case Player.BLUE_SHIP:
                player = new BlueShip();
                break;
        }

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/kenvector_future.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 32;

        fontParameter.color = new Color(1.0f, 1.0f, 1.0f, 1);
        fontScore = fontGenerator.generateFont(fontParameter);

        fontGenerator.dispose();

        randomGenerator = new Random();
    }

    public void update(float delta, SpriteBatch spriteBatch) {
        player.update(delta);

        // Se actualizan los enemigos todavía activos y se eliminan aquellos destruidos o que han
        // salido de la pantalla.
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();

            if (enemy.getY() + enemy.getSprite().getHeight() < 0 || enemy.isDead()) {
                enemyIterator.remove();
            } else {
                enemy.update(delta);
            }
        }

        // Ídem con los potenciadores.
        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp powerUp = powerUpIterator.next();

            if (powerUp.getY() + powerUp.getSprite().getHeight() < 0) {
                powerUpIterator.remove();
            } else {
                powerUp.update(delta);
            }
        }

        // TODO: equilibrar la aparición de potenciadores.

        if (Math.random() < 0.0005f) {
            addPowerUp(randomGenerator.nextInt(599), 800);
        }

        if (enemies.size() == 0) {
            enemyCounter = 0;
        }

        currentTimer += delta;
        currentDifficultyTimer += delta;

        // Cada 60 segundos se incrementa el número de enemigos que hace aparecer el juego.
        if (currentDifficultyTimer - oldDifficultyTimer > 60.0f) {
            oldDifficultyTimer = currentDifficultyTimer;
            limitEnemies += 1;
        }

        if (currentTimer - oldTimer > 0.5f && enemyCounter < limitEnemies) {
            oldTimer = currentTimer;
            enemyCounter++;
            totalEnemyCounter++;

            if (totalEnemyCounter % 25 == 0) {
                // Los enemigos más duros siempre aparecen de tres en tres.
                enemies.add(new HardEnemy(18));
                enemies.add(new HardEnemy(268));
                enemies.add(new HardEnemy(518));
            } else {
                if (Math.random() < 0.3) {
                    enemies.add(new WeakEnemy(true, randomGenerator.nextInt(5)));
                    enemies.add(new NormalEnemy(randomGenerator.nextInt(476) + 62));
                } else {
                    enemies.add(new WeakEnemy());
                    enemies.add(new NormalEnemy(randomGenerator.nextInt(476) + 62));
                }
            }
        }
    }

    /**
     * Se dibujan en pantalla todos los elementos del juego activos.
     *
     * @param spriteBatch
     */
    public void render(SpriteBatch spriteBatch) {
        for (PowerUp powerUp : powerUps) {
            powerUp.render(spriteBatch);
        }

        player.render(spriteBatch);

        for (Enemy enemy : enemies) {
            enemy.render(spriteBatch);
        }

        fontScore.draw(spriteBatch, String.valueOf(globalStatus.getPoints()), 25, 775);
    }

    /**
     * Se compruebas colisiones entre todos los elementos susceptibles de interactuar entre sí
     * (enemigos con disparos, enemigos con nuestra nave, etc.).
     *
     * TODO: el sistema de colisiones con rectángulos no queda bien, debido a la forma irregular
     *  de las naves.
     */
    public void checkCollisions() {
        boolean alreadyRemoved;
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            alreadyRemoved = false;

            // Colisiones entre los disparos del jugador y los enemigos.
            Iterator<Laser> laserIterator = player.getLasers().iterator();
            while (laserIterator.hasNext()) {
                Laser laser = laserIterator.next();

                if (Intersector.overlaps(laser.getCollisionRectangle(), enemy.getCollisionRectangle())) {
                    float enemyEnergy = enemy.getEnergy();
                    float laserEnergy = laser.getEnergy();

                    enemy.hit(laserEnergy);
                    laser.hit(enemyEnergy);

                    if (enemy.isDead()) {
                        globalStatus.addPoints(enemy.getPoints());

                        if (enemy.givesPowerUp()) {
                            addPowerUp(enemy.getX(), enemy.getY(), enemy.getPowerUpType());
                        }

                        enemyIterator.remove();
                        alreadyRemoved = true;
                    }

                    laserIterator.remove();
                    break;
                }
            }

            // Colisiones entre los disparos enemigos y el jugador.
            Iterator<Laser> laserEnemyIterator = enemy.getLasers().iterator();
            while (laserEnemyIterator.hasNext()) {
                Laser laser = laserEnemyIterator.next();

                if (Intersector.overlaps(laser.getCollisionRectangle(), player.getCollisionRectangle())) {
                    float laserEnergy = laser.getEnergy();
                    float playerEnergy = player.getEnergy();

                    laser.hit(playerEnergy);
                    player.hit(laserEnergy);

                    if (player.isDead()) {
                        globalStatus.pauseGame(false);
                        globalStatus.setGameOver(true);
                    }

                    laserEnemyIterator.remove();
                }
            }

            // Colisiones entre enemigos y jugador.
            if (Intersector.overlaps(enemy.getCollisionRectangle(), player.getCollisionRectangle())) {
                float enemyEnergy = enemy.getEnergy();
                float playerEnergy = player.getEnergy();

                enemy.hit(playerEnergy);
                player.hit(enemyEnergy);

                if (enemy.isDead()) {
                    globalStatus.addPoints(enemy.getPoints());

                    if (enemy.givesPowerUp()) {
                        addPowerUp(enemy.getX(), enemy.getY(), enemy.getPowerUpType());
                    }

                    // Se comprueba si el enemigo ya ha sido eliminado para evitar una excepción.
                    // Podría dar un error si estamos disparando a un enemigo y lo destruimos pero
                    // chocamos contra él al mismo tiempo (ya que no desaparece de pantalla al
                    // eliminarlo del iterador).
                    if (!alreadyRemoved) {
                        enemyIterator.remove();
                    }
                }

                if (player.isDead()) {
                    globalStatus.pauseGame(false);
                    globalStatus.setGameOver(true);
                }
            }
        }

        Iterator<PowerUp> powerUpIterator = powerUps.iterator();
        while (powerUpIterator.hasNext()) {
            PowerUp powerUp = powerUpIterator.next();

            if (Intersector.overlaps(powerUp.getCollisionRectangle(), player.getCollisionRectangle())) {
                if (powerUp instanceof GodUp) {
                    // TODO
                } else if (powerUp instanceof ShieldUp) {
                    ((Player) player).restoreShield();
                } else if (powerUp instanceof ShootUp) {
                    ((Player) player).incrementShoot();
                } else if (powerUp instanceof SpecialUp) {
                    ((Player) player).incrementSpecialShoot();
                } else if (powerUp instanceof SpeedUp) {
                    ((Player) player).incrementSpeed();
                }

                globalStatus.getSoundPlayer().playPowerUpTakenSound();
                powerUpIterator.remove();
            }
        }
    }

    private void addPowerUp(float xCoord, float yCoord) {

        // TODO: los potenciadores NO deberían tener todos la misma probabilidad de aparición, ya
        //  que no todos ofrecen la misma ventaja.

        addPowerUp(xCoord, yCoord, randomGenerator.nextInt(4 - 0 + 1));
    }

    private void addPowerUp(float xCoord, float yCoord, int type) {
        switch (type) {
            case PowerUp.POWERUP_SHIELD:
                powerUps.add(new ShieldUp(xCoord, yCoord));
                break;
            case PowerUp.POWERUP_SPEED:
                powerUps.add(new SpeedUp(xCoord, yCoord));
                break;
            case PowerUp.POWERUP_SHOOT:
                powerUps.add(new ShootUp(xCoord, yCoord));
                break;
            case PowerUp.POWERUP_GOD:
                break;
            case PowerUp.POWERUP_SPECIAL:
                powerUps.add(new SpecialUp(xCoord, yCoord));
                break;
        }
    }
}
