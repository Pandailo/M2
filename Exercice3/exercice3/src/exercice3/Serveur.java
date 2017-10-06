/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exercice3;

/**
 *
 * @author Yann
 */
import comptefactory.CompteFactory;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class  Serveur {
public static void main( String []args) {
        try{
                CompteFactory compteFactory = new ImplCompteFactory();
                 Registry  registry = LocateRegistry.createRegistry (1099);
                //Registry registry = LocateRegistry.getRegistry();
                registry.bind("compteFactory",compteFactory);
                System.out.println("Serveur pret"); 
        }
        catch(Exception e){System.out.println(e);}
    }
}
