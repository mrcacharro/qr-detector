package gui;

import database.Database;
import detector.QRDetector;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class VideoPanel extends JPanel {

    private QRDetector qrDetector;
    private VideoCapture camera;
    private BufferedImage currentFrame;

    public VideoPanel(Database db) {
        camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.out.println("No hay cámara disponible");
        }

        qrDetector = new QRDetector(db);

        Thread videoThread = new Thread(this::updateVideo);
        videoThread.start();
    }

    private void updateVideo() {
        Mat matFrame = new Mat();

        while (camera.isOpened()) {
            if (camera.read(matFrame)) {
                qrDetector.detectAndDrawQRCode(matFrame);
                currentFrame = matToBufferedImage(matFrame);
                repaint();
            }

            try {
                Thread.sleep(30);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        camera.release();
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        Mat rgbMat = new Mat();
        Imgproc.cvtColor(mat, rgbMat, Imgproc.COLOR_BGR2RGB);

        int width = rgbMat.width();
        int height = rgbMat.height();
        int channels = rgbMat.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        rgbMat.get(0, 0, sourcePixels);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        image.getRaster().setDataElements(0, 0, width, height, sourcePixels);
        return image;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentFrame != null) {
            g.drawImage(currentFrame, 0, 0, getWidth(), getHeight(), null);
        }
    }
}