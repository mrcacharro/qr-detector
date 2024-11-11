import database.Database;
import gui.Camera;
import org.opencv.core.Core;


public class Main {
    public static void main(String[] args) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("Iniciando programa...");

        Database sqliteDatabase = new Database();
        sqliteDatabase.createTable();

        Camera camera = new Camera(sqliteDatabase);
        camera.setVisible(true);

    }
}
