package controller;

import java.io.File;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 *
 * @author Elison
 */
public class FXMLVoleibolController implements Initializable {
    
    @FXML
    private AnchorPane apVolei;
    @FXML
    public BorderPane bpVolei;
    @FXML
    private MediaView mvDireitaVolei;
    @FXML
    private MediaView mvEsquerdaVolei;
    @FXML
    private Label lTimeEsquerda;
    @FXML
    private Label lTimeDireita;
    @FXML
    private Label lPontosEsquerda;
    @FXML
    private Label lPontosDireita;
    @FXML
    private Label lSetsEsquerda;
    @FXML
    private Label lSetsDireita;
    @FXML
    private Label lSet;
    @FXML
    private Label lPrimeiroSet;
    @FXML
    private Label lSegundoSet;
    @FXML
    private Label lTerceiroSet;
    @FXML
    private Label lQuartoSet;
    @FXML
    private Label lCronometroVolei;
    @FXML
    private ImageView ivTimeEsquerda;
    @FXML
    private ImageView ivTimeDireita;
    
    
    private static File file = new File("src/videos/Propaganda.mp4");
    private static final String mediaurl = file.toURI().toString();
    private MediaPlayer mediaplayer;
    private Media media;
    
    public boolean stopc = true;
    private int segundo = 0;
    private int minuto = 0;
    private int hora = 0;
    
    public void iniciaCronometro() {
        Task t = new Task() {

            @Override
            protected Object call() throws Exception {
                while (stopc == true) {
                    segundo++;

                    if (segundo == 60) {
                        minuto++;
                        segundo = 0;
                    }

                    if (minuto == 60) {
                        hora ++;
                        minuto = 0;
                    }
                    String min = minuto <= 9 ? "0" + minuto : minuto + "";
                    String seg = segundo <= 9 ? "0" + segundo : segundo + "";
                    String hor = hora <= 9 ? "0" + hora : hora + "";

                    Platform.runLater(() -> {

                        lCronometroVolei.setText(hor + ":" + min + ":" + seg);
                    });
                    Thread.sleep(1000);

                }
                return null;
            }
        };
        new Thread(t).start();

    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        media = new Media(mediaurl);
        mediaplayer = new MediaPlayer(media);
        mvDireitaVolei.setMediaPlayer(mediaplayer);
        mvEsquerdaVolei.setMediaPlayer(mediaplayer);
        mediaplayer.play();
        iniciaCronometro();
    }    
    
}
