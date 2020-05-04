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

package com.ymourino.pmdm06.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.ymourino.pmdm06.config.AudioConfig;
import com.ymourino.pmdm06.config.GameConfig;

/**
 * La clase SoundPlayer implementa el patrón singleton y se usa para reproducir los sonidos del
 * juego.
 *
 * @author Yago Mouriño Mendaña
 */
public class SoundPlayer {

    public static final int MUSIC_MENU = 0;
    public static final int MUSIC_GAME = 1;

    private static SoundPlayer soundPlayer;

    private AudioConfig audioConfig;
    private Music menuMusic;
    private Music gameMusic;

    private Sound menuChange;
    private Sound menuEnter;
    private Sound exitScreen;

    private Sound laserSound;
    private Sound laserSpecialSound;
    private Sound powerUpTaken;

    private int playingSong;

    public static SoundPlayer getSoundPlayer() {
        if (soundPlayer == null) {
            soundPlayer = new SoundPlayer();
        }

        return soundPlayer;
    }

    private SoundPlayer() {
        audioConfig = GameConfig.getGameConfig().getAudioConfig();
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/welcome_low.mp3"));
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("music/game_low.mp3"));

        menuMusic.setVolume(audioConfig.getMusicVolume());
        menuMusic.setLooping(true);

        gameMusic.setVolume(audioConfig.getMusicVolume());
        gameMusic.setLooping(true);

        menuChange = Gdx.audio.newSound(Gdx.files.internal("sounds/tick.ogg"));
        menuEnter = Gdx.audio.newSound(Gdx.files.internal("sounds/enter.ogg"));
        exitScreen = Gdx.audio.newSound(Gdx.files.internal("sounds/close.ogg"));

        laserSound = Gdx.audio.newSound(Gdx.files.internal("lasers/laser1.ogg"));
        laserSpecialSound = Gdx.audio.newSound(Gdx.files.internal("lasers/laserSpecial.ogg"));
        powerUpTaken = Gdx.audio.newSound(Gdx.files.internal("sounds/powerup.ogg"));
    }

    public void playMusic() {
        playMusic(playingSong);
    }

    public void playMusic(int music) {
        if (audioConfig.getMusicVolume() > 0.0f && !musicPlaying()) {
            playingSong = music;

            switch (music) {
                case MUSIC_MENU:
                    menuMusic.play();
                    break;
                case MUSIC_GAME:
                    gameMusic.play();
                    break;
            }
        }
    }

    public void pauseMusic() {
        if (musicPlaying()) {
            switch (playingSong) {
                case MUSIC_MENU:
                    menuMusic.pause();
                    break;
                case MUSIC_GAME:
                    gameMusic.pause();
                    break;
            }
        }
    }

    public void stopMusic() {
        if (musicPlaying()) {
            switch (playingSong) {
                case MUSIC_MENU:
                    menuMusic.stop();
                    break;
                case MUSIC_GAME:
                    gameMusic.stop();
                    break;
            }
        }
    }

    public void setVolume(float volume) {
        menuMusic.setVolume(volume);
        gameMusic.setVolume(volume);
    }

    public void playMenuChange() {
        if (audioConfig.getUiVolume() > 0.0f) {
            menuChange.play(audioConfig.getUiVolume());
        }
    }

    public void playMenuEnter() {
        if (audioConfig.getUiVolume() > 0.0f) {
            menuEnter.play(audioConfig.getUiVolume());
        }
    }

    public void playExitScreen() {
        if (audioConfig.getUiVolume() > 0.0f) {
            exitScreen.play(audioConfig.getUiVolume());
        }
    }

    public void playLaserSound() {
        if (audioConfig.getFxVolume() > 0.0f) {
            laserSound.play(audioConfig.getFxVolume());
        }
    }

    public void playLaserSpecialSound() {
        if (audioConfig.getFxVolume() > 0.0f) {
            laserSpecialSound.play(audioConfig.getFxVolume());
        }
    }

    public void playPowerUpTakenSound() {
        if (audioConfig.getFxVolume() > 0.0f) {
            powerUpTaken.play(audioConfig.getFxVolume());
        }
    }

    private boolean musicPlaying() {
        return menuMusic.isPlaying() || gameMusic.isPlaying();
    }
}
