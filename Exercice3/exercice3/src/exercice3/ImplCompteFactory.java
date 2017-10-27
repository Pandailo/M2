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
    Hashtable<Integer, Pair<ImplCompte,Integer>> comptes    = new Hashtable();
    private List<Pair<Connection,Integer>> distrCon = new ArrayList<>();

    private final int  SEUIL = 5;

    public ImplCompteFactory() throws SQLException, RemoteException, ClassNotFoundException{
        super();
        cp = new ConnectionPool();
        distrCon.add(new Pair(cp.getConnection(),0));
    }
  
    private Compte createCompte(int num,Connection con) throws RemoteException{     
        ImplCompte c = new ImplCompte(num,con);
        comptes.put(num, new Pair(c,1));
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
                    Compte c =createCompte(num,distrCon.get(i).getKey());  
                    System.out.println("");
                    distrCon.set(i,new Pair(distrCon.get(i).getKey(),distrCon.get(i).getValue()+1));
                    return c;
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
       comptes.replace(num, new Pair(comptes.get(num).getKey(),comptes.get(num).getValue()+1));      
       System.out.println("compte : "+comptes.get(num).getKey().getAccountId()+" nb use : "+comptes.get(num).getValue());
       return (Compte)comptes.get(num).getKey();
    }
    
  
    @Override
    public int createAccount(double solde){
        int num = -1;
        Connection con=null;
        try
        {
            boolean nouvCo = true;
            for(int i=0;i<distrCon.size();i++)
            {
                if(distrCon.get(i).getValue()<SEUIL){
                     if(distrCon.get(i).getValue()<SEUIL){
                         con= distrCon.get(i).getKey();
                         distrCon.set(i,new Pair(distrCon.get(i).getKey(),distrCon.get(i).getValue()+1));
                         nouvCo = false;
                     }
                }
            }
            if(nouvCo){
               try {
                   Connection c=cp.getConnection();
                   distrCon.add(new Pair(c,1));
                   con=c;
               }
               catch (SQLException ex) {
                   Logger.getLogger(ImplCompteFactory.class.getName()).log(Level.SEVERE, null, ex);
               }
            }
            
            ImplCompte c=new ImplCompte(con);
            num=c.createAccount(solde);
            c.setAccountId(num);
            comptes.put(num, new Pair(c,1));

        }
        catch (RemoteException ex)
        {
            Logger.getLogger(ImplCompteFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return num;
    }
    
    @Override
    public void freeAccount(int num){
        System.out.println("exercice3.ImplCompteFactory.freeAccount()");
        //Si le compte existe
        if(comptes.containsKey(num)){
            if(comptes.get(num).getValue()>1){
                //s'il est utilisé plus d'une fois on décrémente son nb d'utilisation
                comptes.put(num,new Pair(comptes.get(num).getKey(),comptes.get(num).getValue()-1));           
                System.out.println("il reste "+comptes.get(num).getValue()+"comptes"+num);
            }
            else{
                //si on veut libérer son unique utilisation
                for(int i=0;i<distrCon.size();i++){
                    //on décrémente le nb d'utilisation de la connexion qu'il utilise
                    if(distrCon.get(i).getKey().equals(comptes.get(num).getKey().getCon())){
                        distrCon.set(i,new Pair(distrCon.get(i).getKey(),distrCon.get(i).getValue()-1));
                    }
                }
                
                System.out.println("il reste "+(comptes.get(num).getValue()-1)+"comptes"+num);
                //on l'enlève de la liste 
                comptes.remove(num);
            }
        }
    }
}
