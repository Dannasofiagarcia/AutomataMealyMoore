package ui;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private ViewController vc;

    public Main(){
        vc = new ViewController();
    }
    /**
     * Metodo principal Main
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Metodo estar que se encarga de ejecutar la interfaz a la hora de ejecutar el codigo principal para que todo funcione al tiempo y sincronizado.
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("MainView.fxml"));
        fxml.setController(vc);
        Parent root = fxml.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Autómatas Moore y Mealy");
        stage.show();
    }
}
