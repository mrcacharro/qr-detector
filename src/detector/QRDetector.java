package detector;

import database.Database;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.QRCodeDetector;

import javax.swing.*;

public class QRDetector {
    private QRCodeDetector qrCodeDetector;
    private Database db;

    public QRDetector(Database db) {
        this.db = db;
        qrCodeDetector = new QRCodeDetector();
    }

    public String detectAndDrawQRCode(Mat frame) {
        Mat points = new Mat();
        String decodedText = qrCodeDetector.detectAndDecode(frame, points);

        if (!decodedText.isEmpty()) {
            String status = db.getStatus(decodedText);
            if (status == null) {
                JOptionPane.showMessageDialog(null, "Entrando al metro");
                db.insertOrUpdateTicket(decodedText, "entrando");
            }
            else if (status.equals("entrando")) {
                JOptionPane.showMessageDialog(null, "Saliendo del metro");
                db.insertOrUpdateTicket(decodedText, "saliendo");
            }
            else if (status.equals("saliendo")) {
                JOptionPane.showMessageDialog(null, "Entrando de nuevo al metro");
                db.insertOrUpdateTicket(decodedText, "entrando");
            }

            drawBoundingBox(frame, points);

            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return decodedText;
    }

    private void drawBoundingBox(Mat frame, Mat points) {
        if (points.empty()) {
            return;
        }

        Point[] corners = new Point[4];
        for (int i = 0; i < 4; i++) {
            double[] point = points.get(0, i);
            corners[i] = new Point(point[0], point[1]);
        }

        for (int i = 0; i < 4; i++) {
            Imgproc.line(frame, corners[i], corners[(i + 1) % 4], new Scalar(0, 255, 0), 3);
        }
    }
}
