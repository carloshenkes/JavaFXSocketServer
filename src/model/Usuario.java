
package model;

/**
 *
 * @author Carlos Eduardo Henkes
 */
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class Usuario {

    private String usuario;
    private String senha;
    private boolean userADM;
    private boolean userPlacar;
    private boolean userPropaganda;

    public Usuario() {
        this.userADM = false;
        this.userPlacar = false;
        this.userPropaganda = false;
    }
    
    

    public boolean isUserAdm() {
        return userADM;
    }

    public void setUserAdm(boolean userADM) {
        this.userADM = userADM;
    }

    public boolean isUserPlacar() {
        return userPlacar;
    }

    public void setUserPlacar(boolean userPlacar) {
        this.userPlacar = userPlacar;
    }

    public boolean isUserPropaganda() {
        return userPropaganda;
    }

    public void setUserPropaganda(boolean userPropaganda) {
        this.userPropaganda = userPropaganda;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }

}
