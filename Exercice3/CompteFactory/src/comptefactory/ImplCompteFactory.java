/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comptefactory;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Hashtable;
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
    protected static final String URLFAC ="jdbc:oracle:thin:@butor:1521:ense2017";
    protected static final String URLDIST="jdbc:oracle:thin:@ufrsciencestech.u-bourgogne.fr:25559 :ense2017";

    
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
   @Override
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
    public Compte getCompte(int num) throws RemoteException
    {
        
       if(comptes.contains(num)){
           return comptes.get(num);
       }
       else{
           return createCompte(num,con);
       }
    }
    public ImplCompteFactory() throws SQLException, RemoteException, ClassNotFoundException
    {
        initCo();
    }
    public static void main(String arg[]){
	try{
		CompteFactory s=new ImplCompteFactory();
		String nom="CompteFactory";
		Naming.rebind(nom,s); // enregistrement
		System.out.println("Serveur enregistre");
	}
	catch (Exception e){System.err.println("Erreur :"+e);}
	}

}
