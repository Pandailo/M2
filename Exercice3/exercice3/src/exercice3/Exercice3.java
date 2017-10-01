/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exercice3;

import Utils.ConnexionUtils;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author yv965015
 */
public class Exercice3
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException
    {
        Connection con=ConnexionUtils.getInstance().getConnexion();
        Compte account=new Compte(1,con);
        System.out.println("Accountid : 1, Solde :"+account.getSolde());
        System.out.println("Retrait de 100€");
        account.operation(100, "retrait");
        System.out.println("Accountid : 1, Solde :"+account.getSolde());
        System.out.println("Depot de 150€");
        account.operation(150, "depot");
    }
    
}
