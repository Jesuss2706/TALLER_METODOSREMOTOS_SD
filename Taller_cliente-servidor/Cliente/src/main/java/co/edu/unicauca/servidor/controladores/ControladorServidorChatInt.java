package co.edu.unicauca.servidor.controladores;

import co.edu.unicauca.cliente.controladores.UsuarioCllbckInt;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface ControladorServidorChatInt extends Remote
{
    public boolean registrarReferenciaUsuario(String nickname, UsuarioCllbckInt  usuario) throws RemoteException;
    public void enviarMensaje(String mensaje)throws RemoteException;
    public LinkedList<String> listaUsuarios()throws RemoteException;
    public void enviarMensajePrivado(String nickname, String mensaje)throws RemoteException;
}


