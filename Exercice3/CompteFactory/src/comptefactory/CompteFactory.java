/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comptefactory;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.util.List;

/**
 *
 * @author Yann
 */
public interface CompteFactory extends Remote
{
    public void getCompte(int num) throws RemoteException;
    public void depot(int idc,double somme) throws RemoteException;
    public void retrait(int idc,double somme) throws RemoteException;
    public double getSolde(int idc) throws RemoteException;
    public List getOperations(int idc) throws RemoteException;
    public int createAccount(double solde)throws RemoteException;
}

