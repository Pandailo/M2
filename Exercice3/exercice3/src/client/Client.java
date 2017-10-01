/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.rmi.Naming;

/**
 *
 * @author Yann
 */
public class Client
{
    public static void main(String arg[]){
	try{
		CompteFactory h=(CompteFactory)Naming.lookup("compteFactory");
		double s=h.getCompte(1).getSolde();
		System.out.println("Compte recu, solde :"+s);
	}
	catch (Exception e){System.err.println("Erreur :"+e);}
	}
}
