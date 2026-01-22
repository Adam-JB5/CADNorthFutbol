/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadnorthfutbol;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import pojosnorthfutbol.Equipo;
import pojosnorthfutbol.ExcepcionNF;

/**
 *
 * @author DAM209
 */
public class CADNorthFutbol {
    
    private Connection conexion;

    private String HOST = "jdbc:oracle:thin:@172.16.209.1:1521:test";
    private String USERBD = "NF";
    private String PASSWORD = "kk";

    public CADNorthFutbol() throws ExcepcionNF {
        try {

            System.out.println("Conexion");
            Class.forName("oracle.jdbc.driver.OracleDriver");

        } catch (ClassNotFoundException ex) {
            ExcepcionNF e = new ExcepcionNF();
            e.setMensajeErrorBD(ex.getMessage());
            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
            throw e;
        }
    }

    private void conectarBD() throws ExcepcionNF {
        try {

            conexion = DriverManager.getConnection(HOST, USERBD, PASSWORD);

        } catch (SQLException ex) {
            ExcepcionNF e = new ExcepcionNF();
            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
            e.setCodigoErrorBD(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            throw e;
        }
    }
    
    /**
     * Este método hace una consulta recogiendo datos de todos los equipos de
     * la base de datos
     * @return
     * @throws ExcepcionNF
     * @author Adam Janah
     * @version 1.0
     * @since 22/01/2025 DD/MM/AAAA
     */
    public ArrayList<Equipo> leerEquipos() throws ExcepcionNF{
        ArrayList<Equipo> listaEquipos = new ArrayList<>();
        Equipo eq;
        String dql = "SELECT * FROM equipo";
        try {
            conectarBD();
            Statement sentencia = conexion.createStatement();

            ResultSet resultado = sentencia.executeQuery(dql);
            while (resultado.next()) {
                eq = new Equipo();
                eq.setIdEquipo(resultado.getInt("ID_EQUIPO"));
                eq.setNombre(resultado.getString("NOMBRE"));
                eq.setCiudad(resultado.getString("CIUDAD"));
                eq.setEntrenador(resultado.getString("ENTRENADOR"));
                eq.setGrupo(resultado.getString("GRUPO"));
                
                listaEquipos.add(eq);
            }
            resultado.close();

            sentencia.close();
            conexion.close();

        } catch (SQLException ex) {

            ExcepcionNF e = new ExcepcionNF();

            e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
            e.setCodigoErrorBD(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(dql);

            throw e;
        }
        return listaEquipos;
    }
    
    /**
     * 
     * @return
     * @throws ExcepcionNF
     * @author Adam Janah
     * @version 1.0
     * @since 22/01/2025 DD/MM/AAAA
     */
    public Integer eliminarEquipo(Integer idEquipo) throws ExcepcionNF{
        int registrosAfectados = 0;
        String dml = "DELETE equipo WHERE id_equipo = " + idEquipo;
        try {
            conectarBD();
            Statement sentencia = conexion.createStatement();
            registrosAfectados = sentencia.executeUpdate(dml);

            sentencia.close();
            conexion.close();
        } catch (SQLException e) {
            
        }
    }
}
