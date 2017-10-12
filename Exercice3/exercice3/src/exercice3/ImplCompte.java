/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exercice3;

import Interfaces.Compte;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

/**
 *
 * @author yv965015
 */
public class ImplCompte extends UnicastRemoteObject implements Compte
{
    private int accountId;
    private double solde;
    private Connection con;

    public ImplCompte(int accountId,Connection con) throws RemoteException
    {
        this.accountId = accountId;
        this.con = con;
        OraclePreparedStatement st;
        try
        {
            st = (OraclePreparedStatement)con.prepareStatement("select solde from accounts where id = ?"); 
            st.setInt(1, accountId);
            OracleResultSet rs = (OracleResultSet) st.executeQuery();
            if(rs.next())
            {
                this.solde = rs.getDouble(1);
            }
            rs.close();
            st.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ImplCompte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ImplCompte() throws RemoteException
    {
    }

    public ImplCompte(int accountId) throws RemoteException
    {
        this.accountId = accountId;
    }

    public ImplCompte(Connection con) throws RemoteException
    {
        this.con = con;
    }

    public ImplCompte(double solde) throws RemoteException
    {
        this.solde = solde;
    }

    @Override
    public int getAccountId() 
    {
        return accountId;
    }

    @Override
    public double getSolde()
    {
        return solde;
    }

    @Override
    public void setSolde(double solde)
    {
        this.solde = solde;
    }

    @Override
    public void setAccountId(int accountId)
    {
        this.accountId = accountId;
    }

    public void operation(double montant,String type)
    {
        int accountId=this.getAccountId();
        if("depot"==type){
            try {
                OraclePreparedStatement st=(OraclePreparedStatement)this.con.prepareStatement("call operation(?,?,?)");
                st.setInt(1,accountId);
                st.setDouble(2,montant);
                st.setString(3,type);
                st.execute();
                this.setSolde(this.getSolde()+montant);
            }
            catch (SQLException ex) {
                Logger.getLogger(ImplCompte.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        else if("retrait"==type){
                try {
                OraclePreparedStatement st=(OraclePreparedStatement)this.con.prepareStatement("call operation(?,?,?)");
                st.setInt(1,accountId);
                st.setDouble(2,montant);
                st.setString(3,type);
                st.execute();
                this.setSolde(this.getSolde()-montant);
            }
            catch (SQLException ex) {
                Logger.getLogger(ImplCompte.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            System.out.println("Operation doit être de type retrait ou depot. Aucun autre type ne saurait être accepté, vous avez entré :"+type);
        }
    }
    @Override
    public List getOperations() {
        List<String> operations = new ArrayList();
        try
        {
            
            PreparedStatement st = (PreparedStatement)con.prepareStatement("select type,montant,createdDate from operations where accountId = ?");
            st.setInt(1, this.accountId);
            ResultSet rs = (ResultSet) st.executeQuery();
            String s="";
            while(rs.next())
            {
                s="";
                s+=rs.getString(1)+" ";
                s+= rs.getDouble(2)+" " ;
                s+= rs.getDate(3)+" ";
                operations.add(s+"\n");
            }
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ImplCompte.class.getName()).log(Level.SEVERE, null, ex);
        }
        return operations;
    }
    @Override
    public int createAccount(double solde){
        int accountId = -1;
        CallableStatement st;
        Connection con=this.con;
        try
        {
            st = (CallableStatement)con.prepareCall("{?=call createAccount(?)}"); 
            st.registerOutParameter(1, Types.INTEGER);
            st.setDouble(2, solde);
            st.execute();
            accountId=st.getInt(1);
            st.close();
            this.solde=solde;
        }
        catch (SQLException ex)
        {
            Logger.getLogger(ImplCompte.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return accountId;
    }
    @Override
    public void depot(double somme)
    {
        this.operation(somme, "depot");
       
    }
    @Override
    public void retrait(double somme){
        this.operation(somme, "retrait");
    }

}
