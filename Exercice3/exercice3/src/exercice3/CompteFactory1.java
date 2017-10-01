/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exercice3;

/**
 *
 * @author Yann
 */
public class CompteFactory1 extends CompteFactory
{
    protected Compte createCompte(int num){
        return new Compte(num);
    }
}
