/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import avisows.Aviso;
import avisows.AvisoWS_Service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Serg
 */
@ManagedBean
@RequestScoped
public class AvisosBean {

    @WebServiceRef(wsdlLocation = "http://localhost:8080/Emasa-Soap-war/AvisoWS?WSDL")
    private AvisoWS_Service service;
    
    @ManagedProperty(value="#{usuarioBean}")
    private UsuarioBean usuarioBean;
    
    private String ubicacion;
    private String estado;
    private String observaciones;
    private String ubicacionTecnica;
    private String prioridad;
    private String inicioReparacion;
    private String finReparacion;
    private String latitudGPS;
    private String longitudGPS;
    private String tipo;
    private String error;
    
    /**
     * Creates a new instance of listaAvisosBean
     */
    public AvisosBean() {
    }
    
    @PostConstruct
    public void init() {
        error = "";
    }
    
    public List<Aviso> getListaAvisos() {
        return findAll();
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getUbicacionTecnica() {
        return ubicacionTecnica;
    }

    public void setUbicacionTecnica(String ubicaciónTecnica) {
        this.ubicacionTecnica = ubicaciónTecnica;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getInicioReparacion() {
        return inicioReparacion;
    }

    public void setInicioReparacion(String inicioReparacion) {
        this.inicioReparacion = inicioReparacion;
    }

    public String getFinReparacion() {
        return finReparacion;
    }

    public void setFinReparacion(String finReparacion) {
        this.finReparacion = finReparacion;
    }

    public String getLatitudGPS() {
        return latitudGPS;
    }

    public void setLatitudGPS(String latitudGPS) {
        this.latitudGPS = latitudGPS;
    }

    public String getLongitudGPS() {
        return longitudGPS;
    }

    public void setLongitudGPS(String longitudGPS) {
        this.longitudGPS = longitudGPS;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getError() {
        return error;
    }

    public void setUsuarioBean(UsuarioBean usuarioBean) {
        this.usuarioBean = usuarioBean;
    }
    
    public String doCrear() {
        return "nuevoAviso";
    }
    
    public String doGuardar() {
        Aviso aviso = new Aviso();
        
        try {
            GregorianCalendar c = new GregorianCalendar();
            c.setTime(new Date());
            aviso.setFechacreacion(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(AvisosBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (ubicacion == null || ubicacion.trim().isEmpty()) {
            error = "Ubicación inválida";
            return "nuevoAviso";
        } else {
            aviso.setUbicacion(ubicacion);
        }
        
        if(estado == null || estado.trim().isEmpty()) {
            error = "El campo estado no puede estar vacío";
            return "nuevoAviso";
        } else {
            aviso.setEstado(estado);
        }
        
        if (observaciones == null || observaciones.trim().isEmpty()) {
            error = "El campo observaciones no puede estar vacío";
            return "nuevoAviso";
        } else {
            aviso.setObservaciones(observaciones);
        }
        
        if(ubicacionTecnica != null && !ubicacionTecnica.trim().isEmpty()) {
            aviso.setUbicacionTecnica(ubicacionTecnica);
        }
        
        if(prioridad != null && !prioridad.trim().isEmpty()) {
            try {
                int num = Integer.parseInt(prioridad);
                if(num < 1 || num > 9) {
                    error = "La prioridad debe estar entre 1 y 9";
                    return "nuevoAviso";
                }
                aviso.setPrioridad(num);
            } catch(NumberFormatException e) {
                error = "La prioridad debe ser un número";
                return "nuevoAviso";
            }
        }
        
        if(inicioReparacion != null && !inicioReparacion.trim().isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            formatter.applyPattern("dd-MM-yyyy");
            try {
                Date date = formatter.parse(inicioReparacion);
                GregorianCalendar c = new GregorianCalendar();
                c.setTime(date);
                aviso.setInicioReparacion(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
            } catch (DatatypeConfigurationException | ParseException e) {
                error = "El formato de fecha debe ser dd-MM-yyyy";
                return "nuevoAviso";
            }
        }
        
        if(finReparacion != null && !finReparacion.trim().isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            formatter.applyPattern("dd-MM-yyyy");
            try {
                Date date = formatter.parse(finReparacion);
                GregorianCalendar c = new GregorianCalendar();
                c.setTime(date);
                aviso.setFinReparacion(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
            } catch (DatatypeConfigurationException | ParseException e) {
                error = "El formato de fecha debe ser dd-MM-yyyy";
                return "nuevoAviso";
            }
        }
        
        if((latitudGPS != null && !latitudGPS.trim().isEmpty()) &&
                (longitudGPS == null || longitudGPS.trim().isEmpty())) {
            error = "La longitud no puede estar vacía";
            return "nuevoAviso";
        } else if(longitudGPS != null && !longitudGPS.trim().isEmpty()) {
            error = "La latitud no puede estar vacía";
            return "nuevoAviso";
        }
        
        if((latitudGPS != null && !latitudGPS.trim().isEmpty()) &&
                longitudGPS != null && !longitudGPS.trim().isEmpty()) {
            try {
                Double.parseDouble(longitudGPS);
                Double.parseDouble(latitudGPS);
                aviso.setPosGPS(latitudGPS+";"+longitudGPS);
            } catch(NumberFormatException e) {
                error = "La latitud y longitud deben ser numéricos";
                return "nuevoAviso";
            }
        }
        
        if (tipo != null && !tipo.trim().isEmpty()) {
            aviso.setTipo(tipo);
        }
        
        avisows.Usuario _usuario = new avisows.Usuario();
        _usuario.setEmail(usuarioBean.getUsuario().getEmail());
        _usuario.setOperador(usuarioBean.getUsuario().isOperador());
        
        aviso.setUsuarioemail(_usuario);
        
        create(aviso);
        
        return "listaAvisos?faces-redirect=true";
    }
    
    public String doBorrar(Aviso aviso) {
        remove(aviso);
        return "listaAvisos";
    }

    private java.util.List<avisows.Aviso> findAll() {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        avisows.AvisoWS port = service.getAvisoWSPort();
        return port.findAll();
    }

    private void create(avisows.Aviso entity) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        avisows.AvisoWS port = service.getAvisoWSPort();
        port.create(entity);
    }

    private void remove(avisows.Aviso entity) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        avisows.AvisoWS port = service.getAvisoWSPort();
        port.remove(entity);
    }
}
