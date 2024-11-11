package gui;

import database.Database;

import javax.swing.*;

public class Camera extends JFrame {
    public Camera(Database db) {
        setBounds(600, 200, 640, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("detecta qrs");

        VideoPanel videoCamera = new VideoPanel(db);
        add(videoCamera);
    }
}