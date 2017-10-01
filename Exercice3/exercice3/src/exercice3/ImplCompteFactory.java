/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exercice3;

import java.sql.Connection;

/**
 *
 * @author Yann
 */
public class ImplCompteFactory implements CompteFactory
{
    public Compte createCompte(int num,Connection con){
        return new Compte(num,con);
    }

    @Override
    public Compte getCompte(int num, Connection con)
    {
        /* des trucs
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        */return new Compte();
    }
}
