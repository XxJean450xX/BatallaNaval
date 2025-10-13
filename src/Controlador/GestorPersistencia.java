package Controlador;

import Modelo.Partida;
import Modelo.Usuario;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona la persistencia de datos (usuarios y partidas)
 * mediante serialización en archivos locales.
 */
public class GestorPersistencia {
    private static final String ARCHIVO_USUARIOS = "usuarios.dat";
    private static final String ARCHIVO_PARTIDAS = "partidas.dat";

    /**
     * Carga la lista de usuarios desde el archivo de persistencia.
     * @return Lista de {@code Usuario}.
     */
    public static List<Usuario> cargarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_USUARIOS))) {
            usuarios = (List<Usuario>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No se encontró el archivo de usuarios. Se creará uno nuevo.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    /**
     * Guarda la lista completa de usuarios en el archivo de persistencia.
     * @param usuarios La lista de usuarios a guardar.
     */
    public static void guardarUsuarios(List<Usuario> usuarios) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_USUARIOS))) {
            oos.writeObject(usuarios);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga la lista de partidas desde el archivo de persistencia.
     * @return Lista de objetos {@code Partida}.
     */
    public static List<Partida> cargarPartidas() {
        List<Partida> partidas = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_PARTIDAS))) {
            partidas = (List<Partida>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No se encontró el archivo de partidas. Se creará uno nuevo.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return partidas;
    }

    /**
     * Añade una nueva partida al archivo de persistencia, cargando primero las existentes.
     * @param partidas2 La nueva partida a guardar.
     */
    public static void guardarPartida(Partida partidas2) {
        List<Partida> partidas = cargarPartidas();
        partidas.add(partidas2);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_PARTIDAS))) {
            oos.writeObject(partidas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina el contenido del archivo de partidas, reiniciando el historial.
     */
    public static void limpiarPartidas() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_PARTIDAS))) {
            oos.writeObject(new ArrayList<Partida>());
            System.out.println("Archivo de partidas reiniciado.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}