/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadnorthfutbol;

import java.util.ArrayList;
import pojosnorthfutbol.Comentario;
import pojosnorthfutbol.Equipo;
import pojosnorthfutbol.ExcepcionNF;

/**
 *
 * @author adamj
 */
public class main {
    public static void main(String[] args) {
        
        try {
            CADNorthFutbol cad = new CADNorthFutbol();
            ArrayList<Equipo> equipos = cad.leerEquipos();
            System.out.println(equipos);
            
        } catch (ExcepcionNF e) {
            System.out.println(e);
        }
    }
}
