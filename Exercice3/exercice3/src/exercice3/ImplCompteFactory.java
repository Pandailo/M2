/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exercice3;

import comptefactory.CompteFactory;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Yann
 */
public class ImplCompteFactory extends UnicastRemoteObject implements CompteFactory 
{
    Connection con;
    Hashtable<Integer, Compte> comptes    = new Hashtable<Integer, Compte>();
    protected static final String LOGIN ="yv965015";
    protected static final String PASS ="yv965015";
    protected static final String URLFAC ="jdbc:oracle:thin:@eluard:1521:ense2017";
    protected static final String URLDIST="jdbc:oracle:thin:@ufrsciencestech.u-bourgogne.fr:25559:ense2017";

    public ImplCompteFactory() throws SQLException, RemoteException, ClassNotFoundException
    {
        super();
        initCo();
    }
    private void initCo() throws SQLException, ClassNotFoundException, RemoteException
    {
        Connection con=null;
        try 
        {
                con=getConnexion(URLFAC);
        } 
        catch (SQLException ex) 
        {
                con=getConnexion(URLDIST);
        } 
         catch (ClassNotFoundException ex)
        {
            Logger.getLogger(ImplCompteFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.con= con;
    }
     private static Connection getConnexion(String url) throws SQLException, ClassNotFoundException, RemoteException
    {
        Connection con=null;
        Class.forName("oracle.jdbc.driver.OracleDriver");
        con = DriverManager.getConnection(url, LOGIN, PASS);
        return con;
    }

    public Compte createCompte(int num,Connection con) throws RemoteException{
        try
        {
            this.initCo();
            
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ImplCompteFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(ImplCompteFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        Compte c = new Compte(num,con);
        comptes.put(num, c);
        return c;
    }

    @Override
    public void getCompte(int num) throws RemoteException
    {
        
       if(!comptes.containsKey(num)){
           createCompte(num,con);
       }
    }
    @Override
    public void depot(int idc, double somme)
    {
         
       if(comptes.containsKey(idc)){
            Compte c=this.comptes.get(idc);
            c.operation(somme, "depot");
       }
       else{
           try
           {
               createCompte(idc,con);
               Compte c=this.comptes.get(idc);
               c.operation(somme, "depot");
           }
           catch (RemoteException ex)
           {
               Logger.getLogger(ImplCompteFactory.class.getName()).log(Level.SEVERE, null, ex);
           }
           
       }
    }

    @Override
    public void retrait(int idc, double somme)
    {
        if(comptes.containsKey(idc)){
            Compte c=this.comptes.get(idc);
            c.operation(somme, "retrait");
       }
       else{
           try
           {
               createCompte(idc,con);
               Compte c=this.comptes.get(idc);
               c.operation(somme, "retrait");
           }
           catch (RemoteException ex)
           {
               Logger.getLogger(ImplCompteFactory.class.getName()).log(Level.SEVERE, null, ex);
           }
           
       }
    }

    @Override
    public double getSolde(int idc)
    {
        if(comptes.containsKey(idc)){
            Compte c=this.comptes.get(idc);
            return c.getSolde();
       }
       else{
           try
           {
               createCompte(idc,con);
                Compte c=this.comptes.get(idc);
                return c.getSolde();
           }
           catch (RemoteException ex)
           {
               Logger.getLogger(ImplCompteFactory.class.getName()).log(Level.SEVERE, null, ex);
           }
           
       }
        return 0.0;
    }

    @Override
    public List getOperations(int idc)
    {
        List ope=new ArrayList();
         if(comptes.containsKey(idc)){
            try {
                Compte c=this.comptes.get(idc);
                return c.getOperations(idc);
            }
            catch (SQLException ex) {
                Logger.getLogger(ImplCompteFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
       else{
           try
           {
                createCompte(idc,con);
                Compte c=this.comptes.get(idc);
                return c.getOperations(idc);
           }
           catch (RemoteException ex)
           {
               Logger.getLogger(ImplCompteFactory.class.getName()).log(Level.SEVERE, null, ex);
           }
            catch (SQLException ex)
            {
                Logger.getLogger(ImplCompteFactory.class.getName()).log(Level.SEVERE, null, ex);
            }
           
       }
        return ope;
    }
    @Override
    public int createAccount(double solde){
        Compte c=new Compte(con);
        int num=c.createAccount(solde);
        c.setAccountId(num);
        comptes.put(num, c);
        return num;
    }

}
