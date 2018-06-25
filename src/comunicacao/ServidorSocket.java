package comunicacao;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import mainS.MainS;
import model.ListaUsuario;
import model.Usuario;

/**
 *
 * @author Carlos Eduardo Henkes
 */
public class ServidorSocket implements Runnable {
    
    Stage p;
    
    public boolean fimCrono = false;
    private boolean cronoPausado = false;

    public int pontosD = 0;
    public int pontosE = 0;
    private int faltasD = 0;
    private int faltasE = 0;
    private int vermelhoD = 0;
    private int vermelhoE = 0;
    private int amareloD = 0;
    private int amareloE = 0;
    
    public ServidorSocket(Stage p) {
        this.p = p;
    }
    
    public ServidorSocket() {
        
    }
    
    @Override
    public void run() {
        try {
            
            ServerSocket servidor = new ServerSocket(9696);
            System.out.println("Servidor ouvindo a porta 9696");
            Socket cliente = servidor.accept();
            System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
            
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            
            while (true) {
                
                String msg = entrada.readUTF();
                
                String[] escolha = msg.split("\\@");
                if (escolha[0].equals("!LOGIN")) {
                    saida.writeUTF(login(escolha));
                    saida.flush();
                } else if (escolha[0].equals("!ABRE_FUTEBOL")) {
                    MainS.loadScene("/tela/FXMLFutebol.fxml");
                    saida.writeUTF("FutebolCarregado");
                    saida.flush();
                } else if (escolha[0].equals("!ABRE_BASQUETE")) {
                    MainS.loadScene("/tela/FXMLBasquetebol.fxml");
                    saida.writeUTF("BasqueteCarregado");
                    saida.flush();
                } else if (escolha[0].equals("!ABRE_VOLEIBOL")) {
                    MainS.loadScene("/tela/FXMLVoleibol.fxml");
                    saida.writeUTF("VoleibolCarregado");
                    saida.flush();
                } else if (escolha[0].equals("!NOME_TIMES")) {
                    Platform.runLater(() -> {
                        Label l = (Label) p.getScene().getRoot().lookup("#lTimeEsquerda");
                        Label c = (Label) p.getScene().getRoot().lookup("#lTimeDireita");
                        l.setText(escolha[1]);
                        c.setText(escolha[2]);
                    });
                    saida.writeUTF("!TIME_SET");
                    saida.flush();
                } else if (escolha[0].equals("!TIME")) {
                    aTime(escolha);
                    saida.writeUTF("!BLZ");
                    saida.flush();
                }else if (escolha[0].equals("!CRONO")) {
                    cronometro(escolha);
                    saida.writeUTF("!BLZ");
                    saida.flush();
                }
                else if (escolha[0].equals("!TESTE")) {
                    System.out.println("CONECTED");
                    saida.writeUTF("!BLZ");
                    saida.flush();
                }
                
            }
            
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    
    public String login(String[] msg) throws IOException {
        String user = msg[1];
        String password = msg[2];
        Usuario usuario = new Usuario();
        if (validaLogin(user, password, usuario)) {
            System.out.println("user verificado");
            if (usuario.isUserAdm()) {
                return ("!LOGADO@ADM");
            } else if (usuario.isUserPlacar()) {
                return ("!LOGADO@PLACAR");
            } else if (usuario.isUserPropaganda()) {
                return ("!LOGADO@PROP");
            }
            
        }
        
        return ("!NOT_LOGIN");
    }
    
    boolean validaLogin(String user, String password, Usuario usuario) {
        if (user.equals("") || password.equals("")) {
            return false;
        } else {
            ListaUsuario usuarios = lerXML();
            for (Usuario u : usuarios.getUsuarios()) {
                if (user.equals(u.getUsuario()) && password.equals(u.getSenha())) {
                    usuario.setUsuario(u.getUsuario());
                    usuario.setSenha(u.getSenha());
                    usuario.setUserAdm(u.isUserAdm());
                    usuario.setUserPlacar(u.isUserPlacar());
                    usuario.setUserPropaganda(u.isUserPropaganda());
                    
                    return true;
                }
            }
        }
        return false;
    }
    
    public String aTime(String[] msg) {
        
        String opc = msg[1];
        
        if (opc.equals("DIREITA")) {
            mudaPontoD((Label) p.getScene().getRoot().lookup("#lPontosD"), msg[2], msg[3]);
            return "!BLZ";
        } else if (opc.equals("ESQUERDA")) {
            mudaPontoE((Label) p.getScene().getRoot().lookup("#lPontosE"), msg[2], msg[3]);
            return "!BLZ";
        } else if(opc.equals("FALTA")){
            if(msg[2].equals("DIREITA")){
                mudaFaltaD((Label) p.getScene().getRoot().lookup("#lFaltasD"), msg[3]);
            }else if(msg[2].equals("ESQUERDA")){
                mudaFaltaE((Label) p.getScene().getRoot().lookup("#lFaltasE"), msg[3]);
            }
        } else if(opc.equals("AMARELO")){
            if(msg[2].equals("DIREITA")){
                mudaAmareloD((Label) p.getScene().getRoot().lookup("#lAmareloD"), msg[3]);
            }else if(msg[2].equals("ESQUERDA")){
                mudaAmareloE((Label) p.getScene().getRoot().lookup("#lAmareloE"), msg[3]);
            }
        } else if (opc.equals("VERMELHO")){
            if(msg[2].equals("DIREITA")){
                mudaVermelhoD((Label) p.getScene().getRoot().lookup("#lVermelhoD"), msg[3]);
            }else if(msg[2].equals("ESQUERDA")){
                mudaVermelhoE((Label) p.getScene().getRoot().lookup("#lVermelhoE"), msg[3]);
            }
        }
        
        return "!ERROR";
    }
    
    private void mudaPontoD(Label l, String op, String val) {
        if (op.equals("SOMA")) {
            if (val.equals("UM")) {
                pontosD++;                
            } else if (val.equals("DOIS")) {
                pontosD = pontosD + 2;
            } else {
                pontosD = pontosD + 3;
            }
        } else if(val.equals("MENOS")) {
            if (val.equals("UM")) {
                pontosD--;
            } else if (val.equals("DOIS")) {
                pontosD = pontosD - 2;
            } else {
                pontosD = pontosD - 3;
            }
        }
        
        if (pontosD > 9) {
            Platform.runLater(() -> {
                l.setText("" + pontosD);
            });
        } else {
            Platform.runLater(() -> {
                l.setText("0" + pontosD);
            });
        }
        
    }
    
    private void mudaPontoE(Label l, String op, String val) {
        if (op.equals("SOMA")) {
            if (val.equals("UM")) {
                pontosE++;
            } else if (val.equals("DOIS")) {
                pontosE = pontosE + 2;
            } else {
                pontosE = pontosE + 3;
            }
        } else {
            if (val.equals("UM")) {
                pontosE--;
            } else if (val.equals("DOIS")) {
                pontosE = pontosE - 2;
            } else {
                pontosE = pontosE - 3;
            }
        }
        if (pontosE > 9) {
            Platform.runLater(() -> {
                l.setText("" + pontosE);
            });
        } else {
            Platform.runLater(() -> {
                l.setText("0" + pontosE);
            });
        }
        
    }
    
    private void mudaFaltaD(Label l, String op) {
            if (op.equals("SOMA")) {
                faltasD++;                
            } else {
                faltasD--;
            }
            
            if (faltasD > 9) {
                Platform.runLater(() -> {
                    l.setText("" + faltasD);
                });
            } else {
                Platform.runLater(() -> {
                    l.setText("0" + faltasD);
                });
            }
            
    }
    
    private void mudaFaltaE(Label l, String op) {
        if (op.equals("SOMA")) {
            faltasE++;            
        } else {
            faltasE--;
        }
        
        if (faltasE > 9) {
            Platform.runLater(() -> {
                l.setText("" + faltasE);
            });
        } else {
            Platform.runLater(() -> {
                l.setText("0" + faltasE);
            });
        }
        
    }
    
    private void mudaAmareloD(Label l, String op){
        if (op.equals("SOMA")) {
            amareloD++;            
        } else {
            amareloD--;
        }
        
        if (amareloD > 9) {
            Platform.runLater(() -> {
                l.setText("" + amareloD);
            });
        } else {
            Platform.runLater(() -> {
                l.setText("0" + amareloD);
            });
        }
    }
    
    private void mudaAmareloE(Label l, String op){
        if (op.equals("SOMA")) {
            amareloE++;            
        } else {
            amareloE--;
        }
        
        if (amareloE > 9) {
            Platform.runLater(() -> {
                l.setText("" + amareloE);
            });
        } else {
            Platform.runLater(() -> {
                l.setText("0" + amareloE);
            });
        }
    }
    
    private void mudaVermelhoD(Label l, String op){
        if (op.equals("SOMA")) {
            vermelhoD++;            
        } else {
            vermelhoD--;
        }
        
        if (vermelhoD > 9) {
            Platform.runLater(() -> {
                l.setText("" + vermelhoD);
            });
        } else {
            Platform.runLater(() -> {
                l.setText("0" + vermelhoD);
            });
        }
    }
    
    private void mudaVermelhoE(Label l, String op){
        if (op.equals("SOMA")) {
            vermelhoE++;            
        } else {
            vermelhoE--;
        }
        
        if (vermelhoE > 9) {
            Platform.runLater(() -> {
                l.setText("" + vermelhoE);
            });
        } else {
            Platform.runLater(() -> {
                l.setText("0" + vermelhoE);
            });
        }
    }
    
    public void cronometro(String[] msg){
        if(msg[2].equals("PAUSA")){
            cronoPausado = false;
        }else{
            cronoPausado = true;
        }
    }
    
    public ListaUsuario lerXML() {
        ListaUsuario lista = null;
        try {
            File file = new File("src/XML/users.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(ListaUsuario.class);
            
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            lista = (ListaUsuario) jaxbUnmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public void gravarXML(ListaUsuario l) {
        try {
            
            File file = new File("src/xml/usuarios.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(ListaUsuario.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            
            jaxbMarshaller.marshal(l, file);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        
    }
    
}
