import  java.rmi.*;
public class ClientHello{
	public static void main(String arg[]){
	try{
		Hello h=(Hello)Naming.lookup("nomdelobjet");
		String messageRecu=h.ditBonjour();
		System.out.println(messageRecu);
	}///home1/yv965015/VERON/M2/S1/BDED/TP/Exercice1/question3/Serveur/ImpServeurHello_Stub.class
	catch (Exception e){System.err.println("Erreur :"+e);}
	}
}
