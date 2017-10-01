import java.rmi.server.*;
import java.net.*;
import java.rmi.*;
import java.lang.*;

public class ImpServeurHello extends UnicastRemoteObject implements Hello{
	public ImpServeurHello() throws RemoteException{super(); }
	public String ditBonjour() throws RemoteException{return "bonjour a tous";}
	public static void main(String arg[]){
	try{
		ImpServeurHello s=new ImpServeurHello();
		String nom="nomdelobjet";
		Naming.rebind(nom,s); // enregistrement
		System.out.println("Serveur enregistre");
	}
	catch (Exception e){System.err.println("Erreur :"+e);}
	}
}
