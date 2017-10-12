/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author yv965015
 */
public interface Compte extends Remote
{
    public int getAccountId()throws RemoteException;
    public double getSolde()throws RemoteException;
    public void setSolde(double solde)throws RemoteException;
    public Connection getCon()throws RemoteException;
    public void setAccountId(int accountId) throws RemoteException;
    public List getOperations() throws SQLException, RemoteException;
    public int createAccount(double solde)throws RemoteException;
    public void depot(double somme)throws RemoteException;
    public void retrait(double somme)throws RemoteException;
}

    

