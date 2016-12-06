/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import avisows.Aviso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.ws.WebServiceRef;
import operacionws.Operacion;
import operacionws.OperacionWS_Service;

/**
 *
 * @author Serg
 */
@ManagedBean
@SessionScoped
public class OperacionesBean {

    @WebServiceRef(wsdlLocation = "http://localhost:8080/Emasa-Soap-war/OperacionWS?WSDL")
    private OperacionWS_Service service;
    
    @ManagedProperty(value="#{usuarioBean}")
    private UsuarioBean usuarioBean;
    @ManagedProperty(value="#{avisosBean}")
    private AvisosBean avisosBean;
    
    private String error;
    private Operacion operacionSeleccionada;
    private String fecha;

    /**
     * Creates a new instance of listaOperacionesBean
     */
    public OperacionesBean() {
    }
    
    @PostConstruct
    public void init() {
        error = "";
    }

    public String getError() {
        return error;
    }

    public Operacion getOperacionSeleccionada() {
        return operacionSeleccionada;
    }

    public void setOperacionSeleccionada(Operacion operacionSeleccionada) {
        this.operacionSeleccionada = operacionSeleccionada;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
    public List<Operacion> getListaOperaciones(Aviso aviso) {
        return findListaOperaciones(aviso.getId());
    }

    public void setUsuarioBean(UsuarioBean usuarioBean) {
        this.usuarioBean = usuarioBean;
    }

    public void setAvisosBean(AvisosBean avisosBean) {
        this.avisosBean = avisosBean;
    }
    
    public String doCrear() {
        error = "";
        operacionSeleccionada = new Operacion();
        fecha = null;
        return "editarOperacion";
    }
    
    public String doEditar(Operacion operacion) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        formatter.applyPattern("dd-MM-yyyy");
        
        error = "";
        operacionSeleccionada = operacion;
        
        if(operacionSeleccionada.getFecha() != null) {
            fecha = formatter.format(operacionSeleccionada.getFecha().toGregorianCalendar().getTime());
        } else {
            fecha = null;
        }
        return "editarOperacion";
    }
    
    public String doGuardar() {
        if(operacionSeleccionada.getDescripcion() == null || operacionSeleccionada.getDescripcion().trim().isEmpty()) {
            error = "La descripción no puede estar vacía";
            return "editarOperacion";
        }
        
        if(fecha != null && !fecha.trim().isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            formatter.applyPattern("dd-MM-yyyy");
            try {
                Date date = formatter.parse(fecha);
                GregorianCalendar c = new GregorianCalendar();
                c.setTime(date);
                operacionSeleccionada.setFecha(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
            } catch (DatatypeConfigurationException | ParseException e) {
                error = "El formato de fecha debe ser dd-MM-yyyy";
                return "editarOperacion";
            }
        } else {
            error = "La fecha no puede estar vacía";
            return "editarOperacion";
        }
        
        if(operacionSeleccionada.getId() == null) {
            operacionws.Usuario _usuario = new operacionws.Usuario();
            _usuario.setEmail(usuarioBean.getUsuario().getEmail());
            _usuario.setOperador(usuarioBean.getUsuario().isOperador());
            operacionSeleccionada.setUsuarioemail(_usuario);
            
            Aviso aviso = avisosBean.getAvisoSeleccionado();
            operacionws.Aviso _aviso = new operacionws.Aviso();
            _aviso.setId(aviso.getId());
            _aviso.setFechacreacion(aviso.getFechacreacion());
            _aviso.setUbicacion(aviso.getUbicacion());
            _aviso.setEstado(aviso.getEstado());
            _aviso.setObservaciones(aviso.getObservaciones());
            _aviso.setUbicacionTecnica(aviso.getUbicacionTecnica());
            _aviso.setPrioridad(aviso.getPrioridad());
            _aviso.setInicioReparacion(aviso.getInicioReparacion());
            _aviso.setFinReparacion(aviso.getFinReparacion());
            _aviso.setPosGPS(aviso.getPosGPS());
            _aviso.setTipo(aviso.getTipo());
            _aviso.setUsuarioemail(_usuario);
            operacionSeleccionada.setAvisoId(_aviso);
            
            create(operacionSeleccionada);
        } else {
            edit(operacionSeleccionada);
        }
        
        return "listaOperaciones";
    }

    public String doBorrar(Operacion operacion) {
        remove(operacion);
        return "listaOperaciones";
    }
    
    private java.util.List<operacionws.Operacion> findListaOperaciones(int id) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        operacionws.OperacionWS port = service.getOperacionWSPort();
        return port.findListaOperaciones(id);
    }

    private void create(operacionws.Operacion entity) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        operacionws.OperacionWS port = service.getOperacionWSPort();
        port.create(entity);
    }

    private void edit(operacionws.Operacion entity) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        operacionws.OperacionWS port = service.getOperacionWSPort();
        port.edit(entity);
    }

    private void remove(operacionws.Operacion entity) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        operacionws.OperacionWS port = service.getOperacionWSPort();
        port.remove(entity);
    }
    
}
