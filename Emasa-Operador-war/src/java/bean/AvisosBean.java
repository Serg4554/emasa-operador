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
import javax.faces.bean.SessionScoped;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.ws.WebServiceRef;

/**
 *
 * @author Serg
 */
@ManagedBean
@SessionScoped
public class AvisosBean {

    @WebServiceRef(wsdlLocation = "http://localhost:8080/Emasa-Soap-war/AvisoWS?WSDL")
    private AvisoWS_Service service;
    
    @ManagedProperty(value="#{usuarioBean}")
    private UsuarioBean usuarioBean;
    
    private Aviso avisoSeleccionado;
    private String prioridad;
    private String inicioReparacion;
    private String finReparacion;
    private String latitudGPS;
    private String longitudGPS;
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

    public Aviso getAvisoSeleccionado() {
        return avisoSeleccionado;
    }

    public void setAvisoSeleccionado(Aviso avisoSeleccionado) {
        this.avisoSeleccionado = avisoSeleccionado;
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

    public String getError() {
        return error;
    }

    public void setUsuarioBean(UsuarioBean usuarioBean) {
        this.usuarioBean = usuarioBean;
    }
    
    public String doCrear() {
        setAvisoSeleccionado(new Aviso());
        error = "";
        prioridad = null;
        inicioReparacion = null;
        finReparacion = null;
        latitudGPS = null;
        longitudGPS = null;
        return "editarAviso";
    }
    
    public String doEditar(Aviso aviso) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        formatter.applyPattern("dd-MM-yyyy");
        
        setAvisoSeleccionado(aviso);
        error = "";
        
        if(avisoSeleccionado.getPrioridad() != null) {
            prioridad = ""+avisoSeleccionado.getPrioridad();
        } else {
            prioridad = null;
        }
        
        if(avisoSeleccionado.getInicioReparacion() != null) {
            inicioReparacion = formatter.format(avisoSeleccionado.getInicioReparacion().toGregorianCalendar().getTime());
        } else {
            inicioReparacion = null;
        }
        
        if(avisoSeleccionado.getFinReparacion() != null) {
            finReparacion = formatter.format(avisoSeleccionado.getFinReparacion().toGregorianCalendar().getTime());
        } else {
            finReparacion = null;
        }
        
        if(avisoSeleccionado.getPosGPS() != null) {
            String[] posGPS = avisoSeleccionado.getPosGPS().split(";");
            latitudGPS = posGPS[0];
            longitudGPS = posGPS[1];
        } else {
            latitudGPS = null;
            longitudGPS = null;
        }
        
        return "editarAviso";
    }
    
    public String doGuardar() {
        if (avisoSeleccionado.getUbicacion() == null || avisoSeleccionado.getUbicacion().trim().isEmpty()) {
            error = "Ubicación inválida";
            return "editarAviso";
        }
        
        if(avisoSeleccionado.getEstado() == null || avisoSeleccionado.getEstado().trim().isEmpty()) {
            error = "El campo estado no puede estar vacío";
            return "editarAviso";
        }
        
        if (avisoSeleccionado.getObservaciones() == null || avisoSeleccionado.getObservaciones().trim().isEmpty()) {
            error = "El campo observaciones no puede estar vacío";
            return "editarAviso";
        }
        
        if(prioridad != null && !prioridad.trim().isEmpty()) {
            try {
                int num = Integer.parseInt(prioridad);
                if(num < 1 || num > 9) {
                    error = "La prioridad debe estar entre 1 y 9";
                    return "editarAviso";
                }
                avisoSeleccionado.setPrioridad(num);
            } catch(NumberFormatException e) {
                error = "La prioridad debe ser un número";
                return "editarAviso";
            }
        } else {
            avisoSeleccionado.setPrioridad(null);
        }
        
        if(inicioReparacion != null && !inicioReparacion.trim().isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            formatter.applyPattern("dd-MM-yyyy");
            try {
                Date date = formatter.parse(inicioReparacion);
                GregorianCalendar c = new GregorianCalendar();
                c.setTime(date);
                avisoSeleccionado.setInicioReparacion(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
            } catch (DatatypeConfigurationException | ParseException e) {
                error = "El formato de fecha debe ser dd-MM-yyyy";
                return "editarAviso";
            }
        } else {
            avisoSeleccionado.setInicioReparacion(null);
        }
        
        if(finReparacion != null && !finReparacion.trim().isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            formatter.applyPattern("dd-MM-yyyy");
            try {
                Date date = formatter.parse(finReparacion);
                GregorianCalendar c = new GregorianCalendar();
                c.setTime(date);
                avisoSeleccionado.setFinReparacion(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
            } catch (DatatypeConfigurationException | ParseException e) {
                error = "El formato de fecha debe ser dd-MM-yyyy";
                return "editarAviso";
            }
        } else {
            avisoSeleccionado.setFinReparacion(null);
        }
        
        if(latitudGPS != null && !latitudGPS.trim().isEmpty()) {
            if(longitudGPS == null || longitudGPS.trim().isEmpty()) {
                error = "La longitud no puede estar vacía";
                return "editarAviso";
             }
        } else if(longitudGPS != null && !longitudGPS.trim().isEmpty()) {
            error = "La latitud no puede estar vacía";
            return "editarAviso";
        }
        
        if((latitudGPS != null && !latitudGPS.trim().isEmpty()) &&
                longitudGPS != null && !longitudGPS.trim().isEmpty()) {
            try {
                Double.parseDouble(longitudGPS);
                Double.parseDouble(latitudGPS);
                avisoSeleccionado.setPosGPS(latitudGPS+";"+longitudGPS);
            } catch(NumberFormatException e) {
                error = "La latitud y longitud deben ser numéricos";
                return "editarAviso";
            }
        } else {
            avisoSeleccionado.setPosGPS(null);
        }
        
        if(avisoSeleccionado.getId() == null) {
            try {
                GregorianCalendar c = new GregorianCalendar();
                c.setTime(new Date());
                avisoSeleccionado.setFechacreacion(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
            } catch (DatatypeConfigurationException ex) {
                Logger.getLogger(AvisosBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            avisows.Usuario _usuario = new avisows.Usuario();
            _usuario.setEmail(usuarioBean.getUsuario().getEmail());
            _usuario.setOperador(usuarioBean.getUsuario().isOperador());
            avisoSeleccionado.setUsuarioemail(_usuario);
            
            create(avisoSeleccionado);
        } else {
            edit(avisoSeleccionado);
        }
        
        return "listaAvisos?faces-redirect=true";
    }
    
    public String doBorrar(Aviso aviso) {
        remove(aviso);
        return "listaAvisos";
    }
    
    public String verOperaciones(Aviso aviso) {
        avisoSeleccionado = aviso;
        return "listaOperaciones";
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

    private void edit(avisows.Aviso entity) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        avisows.AvisoWS port = service.getAvisoWSPort();
        port.edit(entity);
    }
}
