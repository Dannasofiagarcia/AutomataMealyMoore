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

    public static void main(String[] args) {
        launch();
    }

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
