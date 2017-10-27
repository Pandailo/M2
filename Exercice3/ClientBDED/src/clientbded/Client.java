/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientbded;

import Interfaces.CompteFactory;
import Interfaces.Compte;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Yann
 */
public class Client
{
    public static void main(String arg[]){
	try{
                Registry registry = LocateRegistry.getRegistry("localhost",1099);
		CompteFactory cf=(CompteFactory)registry.lookup("compteFactory");
		Compte c=cf.getCompte(1);
		System.out.println("Compte recu, solde :"+c.getSolde());
                c.depot(200);
		System.out.println("Compte recu, depot effectue de 200, solde :"+c.getSolde());
                c.retrait(400);
		System.out.println("Compte recu, retrait effectue de 400,solde :"+c.getSolde());
               /* 
                List ope=cf.getOperations(1);
                for(int i = 0;i<ope.size();i++){
                    System.out.println("Operation "+i+" : "+ope.get(i)+"\n");
                }
                */
                int num=cf.createAccount(2000);
                Compte c2=cf.getCompte(1);
		System.out.println("Compte créé, id  :"+num);
                c2.depot(200);
		System.out.println("Compte 2 recu, depot effectue de 200, solde :"+c2.getSolde());
                c2.retrait(400);
		System.out.println("Compte  400 recu, retrait effectue de 400,solde :"+c2.getSolde());
                Compte c3=cf.getCompte(250);
                System.out.println("Compte 250 recu, solde initial de :"+c3.getSolde());   
                c3.retrait(400);
		System.out.println("Compte recu 250, retrait effectue de 400,solde :"+c3.getSolde());            
                cf.freeAccount(250);
                cf.freeAccount(250);
	}
	catch (NotBoundException | RemoteException e){
            System.err.println("Erreur :"+e);
        }
    }
}
