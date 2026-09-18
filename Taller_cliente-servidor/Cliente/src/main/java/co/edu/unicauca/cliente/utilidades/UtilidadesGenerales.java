package co.edu.unicauca.cliente.utilidades;
import java.util.LinkedList;

public class UtilidadesGenerales {

    public static void mostrarClientes(LinkedList<String> listaUsuarios){

        for(String user : listaUsuarios){
            System.out.println(user);
        }
    }
}