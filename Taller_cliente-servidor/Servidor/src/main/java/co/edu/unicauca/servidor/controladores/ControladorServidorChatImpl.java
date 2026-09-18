package co.edu.unicauca.servidor.controladores;


import co.edu.unicauca.cliente.controladores.UsuarioCllbckInt;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ControladorServidorChatImpl extends UnicastRemoteObject implements ControladorServidorChatInt {

    //private final List<UsuarioCllbckInt> usuarios;//lista que almacena la referencia remota de los clientes
    private final HashMap<String, UsuarioCllbckInt> usuarios;


    public ControladorServidorChatImpl() throws RemoteException
    {
        super();//asignamos el puerto 
        //usuarios= new ArrayList();
        usuarios= new HashMap<>();
    }
    
    @Override
    public synchronized boolean  registrarReferenciaUsuario(String nickname, UsuarioCllbckInt usuario) throws RemoteException
    {
       //método que unicamente puede ser accedido por un hilo
	System.out.println("Invocando al método registrar usuario desde el servidor");
        boolean bandera=false;
        /*if (!usuarios.contains(usuario))
        {
            bandera=usuarios.add(usuario);  
        }    */
        if (!usuarios.containsKey(nickname)){
            usuarios.put(nickname, usuario);
            bandera = true;
        }else{
            System.out.println("El nickname ya esta en uso");
            bandera = false;
        }
        return bandera;       
    }
   


    /**
     * Revisa todos los usuarios registrados realizando un callback de prueba.
     * Si un cliente no responde (RemoteException), se elimina del HashMap.
     */
    private synchronized void limpiarUsuariosDesconectados() {
        List<String> usuariosEliminar = new ArrayList<>();

        for (HashMap.Entry<String, UsuarioCllbckInt> entry : usuarios.entrySet()) {
            String nickname = entry.getKey();
            UsuarioCllbckInt objUsuario = entry.getValue();

            try {
                // Callback de prueba (heartbeat/ping)
                // Se puede usar un método dedicado como objUsuario.ping() si tu interfaz lo tiene,
                // o enviar una señal mínima.
                objUsuario.notificar("PING", usuarios.size());
            } catch (RemoteException e) {
                System.out.println("El usuario " + nickname + " está desconectado. Se removerá del servidor.");
                usuariosEliminar.add(nickname);
            }
        }

        // Eliminación segura de usuarios caídos fuera del bucle de iteración
        for (String nick : usuariosEliminar) {
            usuarios.remove(nick);
        }
    }

    /**
     * Valida qué usuarios están activos e invoca a limpiarUsuariosDesconectados
     * antes de construir la lista.
     */
    @Override
    public synchronized LinkedList<String> listaUsuarios() {
        System.out.println("Invocando al método listaUsuarios desde el servidor");

        // 1. Validamos y eliminamos los usuarios no disponibles
        limpiarUsuariosDesconectados();

        // 2. Retornamos solo los nicknames que siguen vivos
        return new LinkedList<>(usuarios.keySet());
    }

    @Override
    public void enviarMensaje(String mensaje)throws RemoteException
    {
        notificarUsuarios("un cliente envio el siguiente mensaje: " + mensaje);
    }
    
    private void notificarUsuarios(String mensaje) throws RemoteException 
    {
        System.out.println("Invocando al método notificar usuarios desde el servidor");
        LinkedList<String> usuariosDisponibles = listaUsuarios();
        for(String nicknames: usuariosDisponibles)
        {
            UsuarioCllbckInt objUsuario = usuarios.get(nicknames);
            objUsuario.notificar(mensaje, usuarios.size());//el servidor hace el callback
            
        }
    }

    @Override
    public void enviarMensajePrivado(String nickname, String mensaje)throws RemoteException{
        notificarUsuarioPrivado(nickname,mensaje);
    }

    private void notificarUsuarioPrivado(String nickname, String mensaje) throws RemoteException
    {
        System.out.println("Invocando al método notificar usuario privado desde el servidor");
        LinkedList<String> usuariosDisponibles = listaUsuarios();
        for(String nicknames: usuariosDisponibles)
        {
           if(nicknames.equals(nickname)){
               UsuarioCllbckInt objUsuario = usuarios.get(nickname);
               objUsuario.notificar(mensaje, usuarios.size());//el servidor hace el callback
           }else{
               System.out.println("Usuario no encontrado");
           }

        }
    }

}
