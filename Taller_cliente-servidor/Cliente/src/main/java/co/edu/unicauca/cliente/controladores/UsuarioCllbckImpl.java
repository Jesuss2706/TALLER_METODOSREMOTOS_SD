package co.edu.unicauca.cliente.controladores;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class UsuarioCllbckImpl extends UnicastRemoteObject implements UsuarioCllbckInt
{
    
    public UsuarioCllbckImpl() throws RemoteException
    {
        super();		
    }

    @Override
    public void notificar(String mensaje, int cantidadUsuarios) throws RemoteException
    {
        if ("PING".equals(mensaje)) {
            return;
        }
        System.out.println("\n----------------------------------------");
        System.out.println("Mensaje enviado del servidor: " + mensaje);       
        System.out.println("Cantidad de usuarios conectados: " + cantidadUsuarios );
        System.out.println("----------------------------------------");
        System.out.println("Elija una opción del menú (1. General, 2. Privado, 3. Salir): ");
    }

}
