/*
 * This file is part of BlueMap, licensed under the MIT License (MIT).
 *
 * Copyright (c) Blue (Lukas Rieger) <https://bluecolored.de>
 * Copyright (c) contributors
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
package de.bluecolored.bluemap.core.storage.sql.dialect;

import org.intellij.lang.annotations.Language;

public class MySQLDialect implements Dialect {

    public static final MySQLDialect INSTANCE = new MySQLDialect();

    private MySQLDialect() {};

    @Override
    @Language("MySQL")
    public String writeMapTile() {
        return "REPLACE INTO `bluemap_map_tile` (`map`, `lod`, `x`, `z`, `compression`, `data`) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    @Language("MySQL")
    public String readMapTile() {
        return "SELECT t.`data` " +
                "FROM `bluemap_map_tile` t " +
                " INNER JOIN `bluemap_map` m " +
                "  ON t.`map` = m.`id` " +
                " INNER JOIN `bluemap_map_tile_compression` c " +
                "  ON t.`compression` = c.`id` " +
                "WHERE m.`map_id` = ? " +
                "AND t.`lod` = ? " +
                "AND t.`x` = ? " +
                "AND t.`z` = ? " +
                "AND c.`compression` = ?";
    }

    @Override
    @Language("MySQL")
    public String readMapTileInfo() {
        return "SELECT t.`changed`, LENGTH(t.`data`) as 'size' " +
                "FROM `bluemap_map_tile` t " +
                " INNER JOIN `bluemap_map` m " +
                "  ON t.`map` = m.`id` " +
                " INNER JOIN `bluemap_map_tile_compression` c " +
                "  ON t.`compression` = c.`id` " +
                "WHERE m.`map_id` = ? " +
                "AND t.`lod` = ? " +
                "AND t.`x` = ? " +
                "AND t.`z` = ? " +
                "AND c.`compression` = ?";
    }

    @Override
    @Language("MySQL")
    public String deleteMapTile() {
        return "DELETE t " +
                "FROM `bluemap_map_tile` t " +
                " INNER JOIN `bluemap_map` m " +
                "  ON t.`map` = m.`id` " +
                "WHERE m.`map_id` = ? " +
                "AND t.`lod` = ? " +
                "AND t.`x` = ? " +
                "AND t.`z` = ?";
    }

    @Override
    @Language("MySQL")
    public String writeMeta() {
        return "REPLACE INTO `bluemap_map_meta` (`map`, `key`, `value`) " +
                "VALUES (?, ?, ?)";
    }

    @Override
    @Language("MySQL")
    public String readMeta() {
        return "SELECT t.`value` " +
                "FROM `bluemap_map_meta` t " +
                " INNER JOIN `bluemap_map` m " +
                "  ON t.`map` = m.`id` " +
                "WHERE m.`map_id` = ? " +
                "AND t.`key` = ?";
    }

    @Override
    @Language("MySQL")
    public String readMetaSize() {
        return "SELECT LENGTH(t.`value`) as 'size' " +
                "FROM `bluemap_map_meta` t " +
                " INNER JOIN `bluemap_map` m " +
                "  ON t.`map` = m.`id` " +
                "WHERE m.`map_id` = ? " +
                "AND t.`key` = ?";
    }

    @Override
    @Language("MySQL")
    public String deleteMeta() {
        return "DELETE t " +
                "FROM `bluemap_map_meta` t " +
                " INNER JOIN `bluemap_map` m " +
                "  ON t.`map` = m.`id` " +
                "WHERE m.`map_id` = ? " +
                "AND t.`key` = ?";
    }

    @Override
    @Language("MySQL")
    public String purgeMapTile() {
        return "DELETE t " +
                "FROM `bluemap_map_tile` t " +
                " INNER JOIN `bluemap_map` m " +
                "  ON t.`map` = m.`id` " +
                "WHERE m.`map_id` = ?";
    }

    @Override
    @Language("MySQL")
    public String purgeMapMeta() {
        return "DELETE t " +
                "FROM `bluemap_map_meta` t " +
                " INNER JOIN `bluemap_map` m " +
                "  ON t.`map` = m.`id` " +
                "WHERE m.`map_id` = ?";
    }

    @Override
    @Language("MySQL")
    public String purgeMap() {
        return "DELETE " +
                "FROM `bluemap_map` " +
                "WHERE `map_id` = ?";
    }

    @Override
    @Language("MySQL")
    public String selectMapIds() {
        return "SELECT `map_id` FROM `bluemap_map`";
    }

    @Override
    @Language("MySQL")
    public String initializeStorageMeta() {
        return "CREATE TABLE IF NOT EXISTS `bluemap_storage_meta` (" +
                "`key` varchar(255) NOT NULL, " +
                "`value` varchar(255) DEFAULT NULL, " +
                "PRIMARY KEY (`key`)" +
                ") COLLATE 'utf8mb4_bin'";
    }

    @Override
    @Language("MySQL")
    public String selectStorageMeta() {
        return "SELECT `value` FROM `bluemap_storage_meta` " +
                "WHERE `key` = ?";
    }

    @Override
    @Language("MySQL")
    public String insertStorageMeta() {
        return "INSERT INTO `bluemap_storage_meta` (`key`, `value`) " +
                "VALUES (?, ?)";
    }

    @Override
    @Language("MySQL")
    public String initializeMap() {
        return "CREATE TABLE `bluemap_map` (" +
                "`id` SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT," +
                "`map_id` VARCHAR(255) NOT NULL," +
                "PRIMARY KEY (`id`)," +
                "UNIQUE INDEX `map_id` (`map_id`)" +
                ") COLLATE 'utf8mb4_bin';";
    }

    @Override
    @Language("MySQL")
    public String initializeMapTileCompression() {
        return "CREATE TABLE `bluemap_map_tile_compression` (" +
                "`id` SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT," +
                "`compression` VARCHAR(255) NOT NULL," +
                "PRIMARY KEY (`id`)," +
                "UNIQUE INDEX `compression` (`compression`)" +
                ") COLLATE 'utf8mb4_bin';";
    }

    @Override
    @Language("MySQL")
    public String initializeMapMeta() {
        return "CREATE TABLE `bluemap_map_meta` (" +
                "`map` SMALLINT UNSIGNED NOT NULL," +
                "`key` varchar(255) NOT NULL," +
                "`value` LONGBLOB NOT NULL," +
                "PRIMARY KEY (`map`, `key`)," +
                "CONSTRAINT `fk_bluemap_map_meta_map` FOREIGN KEY (`map`) REFERENCES `bluemap_map` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT" +
                ") COLLATE 'utf8mb4_bin'";
    }

    @Override
    @Language("MySQL")
    public String initializeMapTile() {
        return "CREATE TABLE `bluemap_map_tile` (" +
                "`map` SMALLINT UNSIGNED NOT NULL," +
                "`lod` SMALLINT UNSIGNED NOT NULL," +
                "`x` INT NOT NULL," +
                "`z` INT NOT NULL," +
                "`compression` SMALLINT UNSIGNED NOT NULL," +
                "`changed` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                "`data` LONGBLOB NOT NULL," +
                "PRIMARY KEY (`map`, `lod`, `x`, `z`)," +
                "CONSTRAINT `fk_bluemap_map_tile_map` FOREIGN KEY (`map`) REFERENCES `bluemap_map` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT," +
                "CONSTRAINT `fk_bluemap_map_tile_compression` FOREIGN KEY (`compression`) REFERENCES `bluemap_map_tile_compression` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT" +
                ") COLLATE 'utf8mb4_bin';";
    }

    @Override
    @Language("MySQL")
    public String updateStorageMeta() {
        return "UPDATE `bluemap_storage_meta` " +
                "SET `value` = ? " +
                "WHERE `key` = ?";
    }

    @Override
    @Language("MySQL")
    public String deleteMapMeta() {
        return "DELETE FROM `bluemap_map_meta`" +
                "WHERE `key` IN (?, ?, ?)";
    }

    @Override
    @Language("MySQL")
    public String updateMapMeta() {
        return "UPDATE `bluemap_map_meta` " +
                "SET `key` = ? " +
                "WHERE `key` = ?";
    }

    @Override
    @Language("MySQL")
    public String lookupFK(String table, String idField, String valueField) {
        return "SELECT `" + idField + "` FROM `" + table + "` " +
                "WHERE `" + valueField + "` = ?";
    }

    @Override
    @Language("MySQL")
    public String insertFK(String table, String valueField) {
        return "INSERT INTO `" + table + "` (`" + valueField + "`) " +
                "VALUES (?)";
    }


}
