package co.edu.unicauca.cliente.servicios;

import co.edu.unicauca.cliente.controladores.UsuarioCllbckImpl;
import co.edu.unicauca.cliente.utilidades.UtilidadesConsola;
import co.edu.unicauca.cliente.utilidades.UtilidadesGenerales;
import co.edu.unicauca.cliente.utilidades.UtilidadesRegistroC;
import co.edu.unicauca.servidor.controladores.ControladorServidorChatInt;

public class ClienteDeObjetos
{
    public static void main(String[] args)
    {

        try
        {
            ControladorServidorChatInt servidor;
            int numPuertoRMIRegistry = 0;
            String direccionIpRMIRegistry = "";
            int opc = 0;

            String nickname = "";
            System.out.println("Cual es el la dirección ip donde se encuentra  el rmiregistry ");
            direccionIpRMIRegistry = UtilidadesConsola.leerCadena();
            System.out.println("Cual es el número de puerto por el cual escucha el rmiregistry ");
            numPuertoRMIRegistry = UtilidadesConsola.leerEntero();



            servidor = (ControladorServidorChatInt) UtilidadesRegistroC.obtenerObjRemoto(numPuertoRMIRegistry,direccionIpRMIRegistry, "ServidorChat");

            UsuarioCllbckImpl objNuevoUsuario= new UsuarioCllbckImpl();

            do{
                if(nickname != "" && (servidor.registrarReferenciaUsuario(nickname, objNuevoUsuario))){
                    System.out.println("Usuario creado con exito");
                }else{
                    //Leer nombre desde consola (nickname user)
                    System.out.println("Digite su nickname para mostrar en el servidor: ");
                    nickname = UtilidadesConsola.leerCadena();
                }
            }while(nickname == "" && (servidor.registrarReferenciaUsuario(nickname, objNuevoUsuario)));

            do{
                System.out.println("Seleccione una opcion del menú");
                System.out.println("1. Enviar mensaje general");
                System.out.println("2. Enviar mensaje privado");
                System.out.println("3. Salir ....");
                opc = UtilidadesConsola.leerEntero();

                switch (opc) {
                    case 1:
                        System.out.println("Digite el mensaje a enviar al servidor: ");
                        String mensajeGeneral = UtilidadesConsola.leerCadena();
                        servidor.enviarMensaje(mensajeGeneral);  
                        break;
                    case 2:
                        System.out.println("Los usuarios disponibles son:");
                        UtilidadesGenerales.mostrarClientes(servidor.listaUsuarios());
                        System.out.println("A quien desea enviarle el mensaje?:");
                        String nicknameEnviar = UtilidadesConsola.leerCadena();
                        System.out.println("Digite el mensaje a enviar: ");
                        String mensajePrivado = UtilidadesConsola.leerCadena();
                        servidor.enviarMensajePrivado(nicknameEnviar, mensajePrivado);
                        break;
                    case 3:
                        System.out.println("Saliendo, que tenga un bendecido dia >:)");
                        break;
                    default:
                        System.out.println("Digite una opcion valida porfavorsito :D");
                        break;
                }

            }while(opc == 3);
        }
        catch(Exception e)
        {
                System.out.println("No se pudo realizar la conexion...");
                System.out.println(e.getMessage());
        }

    }
	
}
