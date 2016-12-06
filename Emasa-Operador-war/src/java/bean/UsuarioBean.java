/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.xml.ws.WebServiceRef;
import usuariows.Usuario;
import usuariows.UsuarioWS_Service;

/**
 *
 * @author Serg
 */
@ManagedBean
@SessionScoped
public class UsuarioBean {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/Emasa-Soap-war/UsuarioWS.wsdl")
    private UsuarioWS_Service service;

    private Usuario usuario;
    /**
     * Creates a new instance of UsuarioBean
     */
    public UsuarioBean() {
    }
    
    @PostConstruct
    public void init() {
        usuario = find(2);
    }

    private Usuario find(java.lang.Object id) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        usuariows.UsuarioWS port = service.getUsuarioWSPort();
        return port.find(id);
    }

    public Usuario getUsuario() {
        return usuario;
    }
    
}
