/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author Serg
 */
@Named(value = "listaOperacionesBean")
@Dependent
public class OperacionesBean {

    /**
     * Creates a new instance of listaOperacionesBean
     */
    public OperacionesBean() {
    }
    
}
