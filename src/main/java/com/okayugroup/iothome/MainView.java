/*
 * This file is part of Iot-Home.
 *
 * Iot-Home is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Iot-Home is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Iot-Home. If not, see <https://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2024 OkayuGroup
 */

package com.okayugroup.iothome;

import com.okayugroup.iothome.event.EventController;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainView {
    private JPanel formPanel;
    private JTextArea logArea;
    private JCheckBox useHtmlSettings;
    public static final Font Font = loadFont();
    private JPanel eventsPane;
    private JButton saveNodesButton;
    private JButton resetNodesButton;
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static void main(String[] args) {

        setUIFont(new FontUIResource(Font.deriveFont(10f)));

        JFrame frame = new JFrame("IoT-Home");
        MainView view = new MainView();
        view.initComponents();
        new LogController(view);
        LogController.LOGGER.log("アプリが起動しました。");
        frame.setContentPane(view.formPanel);
        frame.setSize(1200,800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void setLog(LogController.LogLevel level, String message) {
        String builder = LocalDateTime.now().format(DATE_FORMAT) +
                " [" +
                level.name() +
                "]: " +
                message +
                "\n";
        logArea.append(builder);
    }
    // UIManagerを使用してアプリケーション全体のフォントを設定するメソッド
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put(key, f);
        }
    }
    private static Font loadFont() {
        try {
            // フォントファイルをストリームとして読み込む
            InputStream inputStream = MainView.class.getResourceAsStream("/NotoSansJP-Medium.ttf");
            if (inputStream == null) {
                throw new IOException("Font file not found: " + "/NotoSansJP-Medium.ttf");
            }

            // フォントを作成
            return java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, inputStream);
        } catch (FontFormatException | IOException e) {
            LogController.LOGGER.log("フォントが読み込めませんでした。" + e.getMessage());
            // デフォルトのフォントを返すか、例外を処理する
            return new JLabel().getFont(); // デフォルトのフォントを返す
        }
    }

    private void createUIComponents() {
        eventsPane = new EventsPane();
        ((EventsPane)eventsPane).setUserEventsObject(EventController.getTree());
    }
    private void initComponents() {
        useHtmlSettings.addActionListener(e->LogController.LOGGER.log("この機能はまだ実装されていません"));
        saveNodesButton.addActionListener(e-> {
            try {
                EventController.getTree().saveToFile();
                LogController.LOGGER.log("正常に保存が完了したと思います");
            } catch (IOException ex) {
                LogController.LOGGER.log(LogController.LogLevel.ERROR, "保存できませんでした");
                LogController.LOGGER.log(ex.getMessage());
            }
        });
        resetNodesButton.addActionListener( e -> EventController.resetTree());
    }
}
