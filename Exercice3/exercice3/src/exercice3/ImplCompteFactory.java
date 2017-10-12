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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Yann
 */
public class ImplCompteFactory extends UnicastRemoteObject implements CompteFactory 
{
    private Connection con;
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
    private void initCo() throws  ClassNotFoundException, RemoteException
    {
        Connection con=null;
        try 
        {
                con=getConnexion(URLFAC);
        } 
         catch (ClassNotFoundException ex)
        {
            Logger.getLogger(ImplCompteFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.con= con;
    }
     private static Connection getConnexion(String url) throws  ClassNotFoundException, RemoteException
    {
        Connection con=null;
        Class.forName("oracle.jdbc.driver.OracleDriver");
        try
        {
            con = DriverManager.getConnection(url, LOGIN, PASS);
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ImplCompteFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }

    private Compte createCompte(int num,Connection con) throws RemoteException{
        try
        {
            this.initCo();
            
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(ImplCompteFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        Compte c = new ImplCompte(num,con);
        comptes.put(num, c);
        return c;
    }

    @Override
    public Compte getCompte(int num) throws RemoteException
    {
        
       if(!comptes.containsKey(num)){
           createCompte(num,con);
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
