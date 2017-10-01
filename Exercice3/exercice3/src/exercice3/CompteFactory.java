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
public abstract class CompteFactory
{
    public Compte getCompte(int num,Connection con)
    {
        return createCompte(num,con);
    }
    protected abstract Compte createCompte(int num,Connection con);
}
