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
import Interfaces.CompteFactory;
import java.rmi.AlreadyBoundException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class  Serveur {
public static void main( String []args) {
        try{
                CompteFactory compteFactory = new ImplCompteFactory();
                 Registry  registry = LocateRegistry.createRegistry (1099);
                //Registry registry = LocateRegistry.getRegistry();
                registry.bind("compteFactory",compteFactory);
                System.out.println("Serveur pret"); 
        }
        catch(ClassNotFoundException | AlreadyBoundException | RemoteException | SQLException e){
            System.out.println(e);
        }
    }
}
