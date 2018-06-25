package controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
public class FXMLFutebolController implements Initializable {

    @FXML
    private AnchorPane apFutebol;
    @FXML
    public BorderPane bpFutebol;
    @FXML
    private MediaView mvFutebol;
    @FXML
    private Label lTimeEsquerda;
    @FXML
    private Label lTimeDireita;
    @FXML
    private Label lPontosEsquerda;
    @FXML
    private Label lPontosDireita;
    @FXML
    private Label lCronometro;
    @FXML
    public ImageView ivFundo;
    @FXML
    private Label lfaltasD;
    @FXML
    private Label lfaltasE;
    @FXML
    private Label lamareloD;
    @FXML
    private Label lamareloE;
    @FXML
    private Label lvermelhoD;
    @FXML
    private Label lvermelhoE;

    private int faltasDireita = 0;
    private int faltasEsquerda = 0;
    private int amareloDireita = 0;
    private int amareloEsquerda = 0;
    private int vermelhoDireita = 0;
    private int vermelhoEsquerda = 0;

    private int pontosE = 0;
    private int pontosD = 0;

    protected static BooleanProperty v = new SimpleBooleanProperty();

    private static File file = new File("src/videos/Propaganda.mp4");
    private static final String mediaurl = file.toURI().toString();
    private MediaPlayer mediaplayer;
    private Media media;

    //configuração do cronometro
    public boolean stopc = true;
    private int segundo = 0;
    private int minuto = 0;

    public void iniciaCronometro() {
        Task t = new Task() {

            @Override
            protected Object call() throws Exception {
                while (stopc) {
                    segundo++;

                    if (segundo == 60) {
                        minuto++;
                        segundo = 0;
                    }

                    if (minuto == 60) {

                        minuto = 0;
                    }
                    String min = minuto <= 9 ? "0" + minuto : minuto + "";
                    String seg = segundo <= 9 ? "0" + segundo : segundo + "";

                    Platform.runLater(() -> {

                        lCronometro.setText(min + ":" + seg);
                    });
                    Thread.sleep(1000);

                }
                return null;
            }
        };
        new Thread(t).start();

    }
    
    void crono(boolean status){
        stopc = status;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // ****************
        //Chama e inicia a thread do cronometro
        iniciaCronometro();
        media = new Media(mediaurl);
        mediaplayer = new MediaPlayer(media);
        mvFutebol.setMediaPlayer(mediaplayer);
        mediaplayer.play();

    }

}
