/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import avisows.Aviso;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.xml.ws.WebServiceRef;
import operacionws.Operacion;
import operacionws.OperacionWS_Service;

/**
 *
 * @author Serg
 */
@ManagedBean
@RequestScoped
public class OperacionesBean {

    @WebServiceRef(wsdlLocation = "http://localhost:8080/Emasa-Soap-war/OperacionWS?WSDL")
    private OperacionWS_Service service;

    /**
     * Creates a new instance of listaOperacionesBean
     */
    public OperacionesBean() {
    }
    
    public List<Operacion> getListaOperaciones(Aviso aviso) {
        return findListaOperaciones(aviso.getId());
    }
    
    public String doCrear() {
        return "listaOperaciones";
    }

    private java.util.List<operacionws.Operacion> findListaOperaciones(int id) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        operacionws.OperacionWS port = service.getOperacionWSPort();
        return port.findListaOperaciones(id);
    }
    
}
