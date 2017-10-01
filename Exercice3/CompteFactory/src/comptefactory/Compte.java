/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comptefactory;


import static com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

/**
 *
 * @author yv965015
 */
public class Compte
{
    private int accountId;
    private double solde;
    private Connection con;

    public Compte(int accountId,Connection con)
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
            
        }
        catch (SQLException ex)
        {
            Logger.getLogger(Compte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Compte()
    {
    }

    public Compte(int accountId)
    {
        this.accountId = accountId;
    }

    public Compte(double solde)
    {
        this.solde = solde;
    }

    public int getAccountId()
    {
        return accountId;
    }

    public double getSolde()
    {
        return solde;
    }

    public void setSolde(double solde)
    {
        this.solde = solde;
    }

    public Connection getCon()
    {
        return con;
    }
    
    public void operation(double montant,String type)
    {
        int accountId=this.getAccountId();
        if("depot"==type){
            try {
                OraclePreparedStatement st=(OraclePreparedStatement)this.getCon().prepareStatement("call operation(?,?,?)");
                st.setInt(1,accountId);
                st.setDouble(2,montant);
                st.setString(3,type);
                st.execute();
            }
            catch (SQLException ex) {
                Logger.getLogger(Compte.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        else if("retrait"==type){
                try {
                OraclePreparedStatement st=(OraclePreparedStatement)this.getCon().prepareStatement("call operation(?,?,?)");
                st.setInt(1,accountId);
                st.setDouble(2,montant);
                st.setString(3,type);
                st.execute();
            }
            catch (SQLException ex) {
                Logger.getLogger(Compte.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            System.out.println("Operation doit être de type retrait ou depot. Aucun autre type ne saurait être accepté, vous avez entré :"+type);
        }
    }
    
    
    
}
