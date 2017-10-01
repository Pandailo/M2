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
		Compte c=h.getCompte(1);
		System.out.println("Compte recu, solde :"c.getSolde);
	}
	catch (Exception e){System.err.println("Erreur :"+e);}
	}
}
