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

package com.ymourino.pmdm06.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.ymourino.pmdm06.helpers.GameConstantData;

import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase utilizada para cargar y guardar la configuración del juego, lo que actualmente solo
 * afecta a los volúmenes.
 *
 * @author Yago Mouriño Mendaña
 */
public class GameConfig {
    private static GameConfig gameConfig;
    private AudioConfig audioConfig;

    public static GameConfig getGameConfig() {
        if (gameConfig == null) {
            if (Files.exists(Paths.get(GameConstantData.GAME_CONFIG_FILE))) {
                try {
                    Gson gson = new Gson();
                    JsonReader jr = new JsonReader(new FileReader(GameConstantData.GAME_CONFIG_FILE));
                    gameConfig = gson.fromJson(jr, GameConfig.class);
                } catch (Exception e) {
                    Logger.getLogger(GameConfig.class.getName()).log(Level.SEVERE, null, e);
                    gameConfig = new GameConfig();
                }
            } else {
                gameConfig = new GameConfig();
                saveGameConfig();
            }
        }

        return gameConfig;
    }

    public static void saveGameConfig() {
        if (gameConfig != null) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            try {
                PrintWriter pw = new PrintWriter(GameConstantData.GAME_CONFIG_FILE);
                pw.println(gson.toJson(gameConfig));
                pw.close();
            } catch (Exception e) {
                Logger.getLogger(GameConfig.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    private GameConfig() {
        audioConfig = new AudioConfig();
    }

    public AudioConfig getAudioConfig() {
        return audioConfig;
    }
}
