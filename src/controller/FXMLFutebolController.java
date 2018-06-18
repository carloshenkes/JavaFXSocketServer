/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private Label lAmareloEsquerda;
    @FXML
    private Label lAmareloDireita;
    @FXML
    private Label lVermelhoEsquerda;
    @FXML
    private Label lVermelhoDireita;

    private static File file = new File("src/videos/Propaganda.mp4");
    private static final String mediaurl = file.toURI().toString();
    private MediaPlayer mediaplayer;
    private Media media;
    //private static FXMLSetDadosController c;

    public boolean stopc = true;
    private int segundo = 0;
    private int minuto = 0;
    

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

    public void Placar() {
        Task t2 = new Task() {
            

            @Override
            protected Object call() throws Exception {
                //Soma 1 gol ao placar do time da esquerda
                //ao pressionar <-
                apFutebol.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                    int te = 0;
                     int td = 0;
                    if (event.getCode().equals(KeyCode.LEFT)) {
                        te = Integer.parseInt(lPontosDireita.getText());
                        te = (te + 1);
                        
                        String tes = Integer.toString(te);                        
                        Platform.runLater(() -> {

                            lPontosDireita.setText(tes);
                        });
                        
                    }

                    //Soma 1 gol ao placar do time da direita
                    //ao pressionar ->
                    if (event.getCode().equals(KeyCode.RIGHT)) {
                        td = Integer.parseInt(lPontosDireita.getText());
                        td = (td + 1);
                        
                        String tds = Integer.toString(td);                        
                        Platform.runLater(() -> {

                            lPontosDireita.setText(tds);
                        });
                    }
                });
                //}
                return null;
            }
        };
        new Thread(t2).start();

    }

//    @FXML
//    private void pausaVideo(KeyEvent event) {
//        if (event.getCode() == KeyCode.ESCAPE) {
//            mediaplayer.stop();
//        }
//    }

    public void pegarTime(String nomea, String nomeb) {

        this.lTimeEsquerda.setText(nomea);
        this.lTimeDireita.setText(nomeb);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        media = new Media(mediaurl);
        mediaplayer = new MediaPlayer(media);
        mvFutebol.setMediaPlayer(mediaplayer);
        mediaplayer.play();
        iniciaCronometro();
        Placar();
        
        //c = new FXMLSetDadosController();
        try {
        //    pegarTime(c.retornaTimeA(), c.retornaTimeB());
        } catch (Exception ex) {
            System.out.println("Deu merda" + ex.getMessage());
        }
       
        
        

    }

}
