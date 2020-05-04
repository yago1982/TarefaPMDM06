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

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ymourino.pmdm06.helpers.GameConstantData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Se definen comportamientos comunes a todas las naves, tanto las del jugador como los enemigos.
 *
 * @author Yago Mouriño Mendaña
 */
abstract class Ship extends ScreenItem {

    private List<Laser> lasers;

    public Ship(int energy) {
        super(energy);

        this.lasers = new ArrayList<>();
    }

    @Override
    public void update(float delta) {
        Iterator<Laser> laserIterator = lasers.iterator();
        while (laserIterator.hasNext()) {
            Laser laser = laserIterator.next();

            // Nos aseguramos de que los disparos desaparezcan si ya no tienen energía o si se han
            // salido de la pantalla.
            if (laser.getY() > GameConstantData.GAME_HEIGHT - laser.getSprite().getHeight() || laser.getY() < 0 ||
                    laser.getX() > GameConstantData.GAME_WIDTH - laser.getSprite().getWidth() || laser.getX() < 0 ||
                    laser.isDead()) {
                laserIterator.remove();
            } else {
                // Si el disparo sigue "vivo" y en pantalla, se actualiza.
                laser.update(delta);
            }
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        for (Laser laser : lasers) {
            laser.render(spriteBatch);
        }

        super.render(spriteBatch);
    }

    /**
     * Cuando una nave dispara, los disparos los almacena y gestiona ella misma. Esto provoca un
     * comportamiento extraño, y es que si la nave es destruida estando alguno de sus disparos
     * "en el aire", estos también desaparecen. Habría que hacer que sean independientes de la nave
     * que los ha disparado.
     *
     * @param lasers
     */
    public final void addLasers(Laser... lasers) {
        Collections.addAll(this.lasers, lasers);
    }

    public final List<Laser> getLasers() {
        return lasers;
    }
}
