package mainS;
// servidor
import java.io.IOException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import comunicacao.ServidorSocket;

public class Main extends Application {

    public static Stage primaryStage;
    public static Scene sceneBasquete, scenePrincipal;
    public static Class thisClass;


    /**
     * A classe principal da aplicação em JavaFX
     */
    public Main() {
        thisClass = getClass();
    }

    /**
     * Inicia o layout da aplicação
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        setStage(primaryStage);
        
        new Thread(new ServidorSocket(primaryStage)).start();

        // loadScene("/view/FXMLPrincipal.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setStage(Stage s) {
        this.primaryStage = s;
    }

    public static void loadScene(String local) {

        try {
            Parent root = FXMLLoader.load(thisClass.getClass().getResource(local));
            scenePrincipal = new Scene(root);
            Platform.runLater(() -> {
                primaryStage.setScene(scenePrincipal);
                primaryStage.setTitle("Servidor");
                primaryStage.show();
                primaryStage.setFullScreen(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
