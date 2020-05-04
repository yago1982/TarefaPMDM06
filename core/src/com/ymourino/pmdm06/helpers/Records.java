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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase utilizada para cargar y salvar los récords de los jugadores.
 *
 * @author Yago Mouriño Mendaña
 */
public class Records {
    private RecordsFile records;

    private class RecordsFile {
        private TreeMap<Integer, String> records = new TreeMap<>();

        public void put(Integer key, String value) {
            if (records.containsKey(key)) {
                // TODO: eliminar la puntuación de un usuario en caso de empate no es correcto. Por
                //  el momento queda así por falta de tiempo.
                records.remove(key);
                records.put(key, value);
            } else {
                if (records.size() == 10) {
                    records.remove(records.higherEntry(0).getKey());
                    records.put(key, value);
                } else {
                    records.put(key, value);
                }
            }
        }

        public Set<Map.Entry<Integer, String>> entrySet() {
            return records.descendingMap().entrySet();
        }

        public int getLowerRecord() {
            return records.higherEntry(0).getKey();
        }
    }

    public Records() {
        if (Files.exists(Paths.get(GameConstantData.GAME_RECORDS_FILE))) {
            try {
                Gson gson = new Gson();
                JsonReader jr = new JsonReader(new FileReader("records.json"));
                records = gson.fromJson(jr, RecordsFile.class);
            } catch (Exception e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
                generateDefaultRecords();
                saveRecords();
            }
        } else {
            records = new RecordsFile();
            generateDefaultRecords();
            saveRecords();
        }
    }

    private void saveRecords() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            PrintWriter pw = new PrintWriter(GameConstantData.GAME_RECORDS_FILE);
            pw.println(gson.toJson(records));
            pw.close();
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void generateDefaultRecords() {
        for (int i = 0; i < 10; i++) {
            records.put(100 * (10 - i), "YMM");
        }
    }

    public Set<Map.Entry<Integer, String>> entrySet() {
        return records.entrySet();
    }

    public void put(Integer score, String player) {
        records.put(score, player);
        saveRecords();
    }

    public int getLowerRecord() {
        return records.getLowerRecord();
    }
}
