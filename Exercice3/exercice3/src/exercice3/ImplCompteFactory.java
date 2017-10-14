/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exercice3;

import Interfaces.Compte;
import Interfaces.CompteFactory;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 * 
 * @author Yann
 */
public class ImplCompteFactory extends UnicastRemoteObject implements CompteFactory 
{
    ConnectionPool cp;
    Hashtable<Integer, Compte> comptes    = new Hashtable<Integer, Compte>();
    private List<Pair<Connection,Integer>> distrCon = new ArrayList<>();

    private final int  SEUIL = 5;

    public ImplCompteFactory() throws SQLException, RemoteException, ClassNotFoundException{
        super();
        cp = new ConnectionPool();
        distrCon.add(new Pair(cp.getConnection(),0));
    }
  
    private Compte createCompte(int num,Connection con) throws RemoteException{
        
        Compte c = new ImplCompte(num,con);
        comptes.put(num, c);
        return c;
    }

    @Override
    public Compte getCompte(int num) throws RemoteException
    {
       boolean nouvCo = true;
       if(!comptes.containsKey(num)){
           for(int i=0;i<distrCon.size();i++)
           {
               if(distrCon.get(i).getValue()<SEUIL){
                    createCompte(num,distrCon.get(i).getKey());  
                    distrCon.set(i,new Pair(distrCon.get(i).getKey(),distrCon.get(i).getValue()+1));
                    if(distrCon.get(i).getValue()<SEUIL){
                        nouvCo = false;
                    }
                    return (Compte)comptes.get(num);
               }
           }
            if(nouvCo){
               try {
                   Connection c=cp.getConnection();
                   distrCon.add(new Pair(c,1));
                   createCompte(num,c); 
               }
               catch (SQLException ex) {
                   Logger.getLogger(ImplCompteFactory.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
       }
       return (Compte)comptes.get(num);
    }
    
  
    @Override
    public int createAccount(double solde){
        int num = -1;
        try
        {
            Compte c=new ImplCompte(con);
            num=c.createAccount(solde);
            c.setAccountId(num);
            comptes.put(num, c);

        }
        catch (RemoteException ex)
        {
            Logger.getLogger(ImplCompteFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return num;
    }

}
