/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import avisows.Aviso;
import avisows.AvisoWS_Service;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Serg
 */
@ManagedBean
@RequestScoped
public class AvisosBean {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/Emasa-Soap-war/AvisoWS.wsdl")
    private AvisoWS_Service service;
    
    /**
     * Creates a new instance of listaAvisosBean
     */
    public AvisosBean() {
    }
    
    @PostConstruct
    public void init() {
    }
    
    public List<Aviso> getListaAvisos() {
        return findAll();
    }
    
    public String doCrear() {
        return "nuevoAviso";
    }

    private java.util.List<avisows.Aviso> findAll() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        avisows.AvisoWS port = service.getAvisoWSPort();
        return port.findAll();
    }
}
