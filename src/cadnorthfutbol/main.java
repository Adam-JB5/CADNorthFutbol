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

//        try {
//            CADNorthFutbol cad = new CADNorthFutbol();
//            ArrayList<Equipo> equipos = cad.leerEquipos();
//            System.out.println(equipos);
//            
//        } catch (ExcepcionNF e) {
//            System.out.println(e);
//        }

        try {
            CADNorthFutbol cad = new CADNorthFutbol();
            Equipo equipo = new Equipo();
            equipo.setNombre("kk");
            equipo.setCiudad("kk");
            equipo.setEntrenador("kk");
            equipo.setGrupo("2");
            Integer registros = cad.modificarEquipo(1, equipo);
            System.out.println(registros);
        } catch (ExcepcionNF e) {
            System.out.println(e);
        }
        
//        try {
//            CADNorthFutbol cad = new CADNorthFutbol();
//            Equipo equipo = new Equipo();
//            
//            equipo.setNombre("kkIns");
//            equipo.setCiudad("kk");
//            equipo.setEntrenador("kk");
//            equipo.setGrupo("2");
//            Integer registros = cad.insertarEquipo(equipo);
//            System.out.println(registros);
//        } catch (ExcepcionNF e) {
//            System.out.println(e);
//        }
    }
}
