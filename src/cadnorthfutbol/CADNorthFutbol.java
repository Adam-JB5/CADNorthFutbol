/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadnorthfutbol;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

    private String HOST = "jdbc:oracle:thin:@192.168.1.209:1521:test";
    //private String HOST = "jdbc:oracle:thin:@172.16.209.1:1521:test";
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
        String dml = "";
        try {
            conectarBD();
            Statement sentencia = conexion.createStatement();
            dml = "DELETE equipo WHERE id_equipo = " + idEquipo;
            registrosAfectados = sentencia.executeUpdate(dml);
            
            sentencia.close();
            conexion.close();
        } catch (SQLException ex) {
            ExcepcionNF e = new ExcepcionNF();

            switch (ex.getErrorCode()) {
                case 2292:
                    e.setMensajeErrorUsuario("No se puede eliminar ya que tiene asociado un jugador, una noticia, un partido o es seguido por algún usuario");
                    break;
                default:
                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
                    break;
            }

            e.setCodigoErrorBD(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(dml);

            throw e;
        }
        
        return registrosAfectados;
    }
    
    public Integer modificarEquipo(Integer idEquipo, Equipo equipo) throws ExcepcionNF{
        int registrosAfectados = 0;
        String sql = "call modificar_equipo(?, ?, ?, ?, ?)";
        try {
            conectarBD();
            
            CallableStatement sentenciaLlamable = conexion.prepareCall(sql);

            sentenciaLlamable.setString(1, equipo.getNombre());
            sentenciaLlamable.setString(2, equipo.getCiudad());
            sentenciaLlamable.setString(3, equipo.getEntrenador());
            sentenciaLlamable.setString(4, equipo.getGrupo());
            sentenciaLlamable.setObject(5, idEquipo, java.sql.Types.INTEGER);
            
            registrosAfectados = sentenciaLlamable.executeUpdate();

            sentenciaLlamable.close();
            conexion.close();

        } catch (SQLException ex) {
            ExcepcionNF e = new ExcepcionNF();

            switch (ex.getErrorCode()) {
                case 1:
                    e.setMensajeErrorUsuario("Ya existe un equipo con el mismo nombre");
                    break;
                case 1407:
                    e.setMensajeErrorUsuario("Todos los campos son obligatorios");
                    break;
                case 2290:
                    e.setMensajeErrorUsuario("El grupo solamente puede ser: 1, 2, 3, 4 o 5");
                    break;
                default:
                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
                    break;
            }

            e.setCodigoErrorBD(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(sql);

            throw e;
        }
        
        return registrosAfectados;
    }
    
    /**
     * Este metodo inserta un registro en la tabla Equipo de la base de datos
     * @param equipo
     * @return
     * @throws ExcepcionNF 
     */
    public Integer insertarEquipo(Equipo equipo) throws ExcepcionNF{
        int registrosAfectados = 0;
        String dml = "INSERT INTO equipo (id_equipo, nombre, ciudad, entrenador, grupo) VALUES (SEQ_EQUIPO.nextval, ?, ?, ?, ?)";
        
        try {
            conectarBD();
            PreparedStatement sentenciaPreparada = conexion.prepareStatement(dml);
            
            sentenciaPreparada.setString(1, equipo.getNombre());
            sentenciaPreparada.setString(2, equipo.getCiudad());
            sentenciaPreparada.setString(3, equipo.getEntrenador());
            sentenciaPreparada.setString(4, equipo.getGrupo());
            
            registrosAfectados = sentenciaPreparada.executeUpdate();
            
            sentenciaPreparada.close();
            conexion.close();
        } catch (SQLException ex) {
            ExcepcionNF e = new ExcepcionNF();

            switch (ex.getErrorCode()) {
                case 1:
                    e.setMensajeErrorUsuario("Ya existe un equipo con el mismo nombre");
                    break;
                case 1400:
                    e.setMensajeErrorUsuario("Todos los campos son obligatorios");
                    break;
                case 2290:
                    e.setMensajeErrorUsuario("El grupo solamente puede ser: 1, 2, 3, 4 o 5");
                    break;
                default:
                    e.setMensajeErrorUsuario("Error general del sistema. Consulte con el administrador");
                    break;
            }

            e.setCodigoErrorBD(ex.getErrorCode());
            e.setMensajeErrorBD(ex.getMessage());
            e.setSentenciaSQL(dml);

            throw e;
        }
        
        return registrosAfectados;
    }
}
