/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientbded;

import comptefactory.CompteFactory;
import java.rmi.Naming;
import java.util.List;

/**
 *
 * @author Yann
 */
public class Client
{
    public static void main(String arg[]){
	try{
		CompteFactory h=(CompteFactory)Naming.lookup("compteFactory");
		double s=h.getSolde(1);
		System.out.println("Compte recu, solde :"+s);
                h.depot(1,200);
                s=h.getSolde(1);
		System.out.println("Compte recu, depot effectue de 200, solde :"+s);
                h.retrait(1,400);
                s=h.getSolde(1);
		System.out.println("Compte recu, retrait effectue de 400,solde :"+s);
               /* 
                List ope=h.getOperations(1);
                for(int i = 0;i<ope.size();i++){
                    System.out.println("Operation "+i+" : "+ope.get(i)+"\n");
                }
                */
                int num=h.createAccount(2000);
		System.out.println("Compte créé, id  :"+num);
                h.depot(num,200);
                s=h.getSolde(num);
		System.out.println("Compte recu, depot effectue de 200, solde :"+s);
                h.retrait(num,400);
                s=h.getSolde(num);
		System.out.println("Compte recu, retrait effectue de 400,solde :"+s);
	}
	catch (Exception e){System.err.println("Erreur :"+e);}
	}
}
