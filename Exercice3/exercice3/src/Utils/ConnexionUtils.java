package Utils;
import java.util.*;
import com.sun.rowset.*;


import java.sql.*;
import java.util.logging.*;
import javax.sql.rowset.*;

public class ConnexionUtils {
	protected static final String LOGIN ="yv965015";
	protected static final String PASS ="yv965015";
	protected static final String URLFAC ="jdbc:oracle:thin:@eluard:1521:ense2017";
	protected static final String URLDIST="jdbc:oracle:thin:@ufrsciencestech.u-bourgogne.fr:25559:ense2017";
	private static Logger log = Logger.getLogger(ConnexionUtils.class.getSimpleName());

	
	private ConnexionUtils()
	{
		
	}
	private static class ConnexionUtilsHolder
	{
		private final static ConnexionUtils instance=new ConnexionUtils();
	}
	public static ConnexionUtils getInstance()
	{
		return ConnexionUtilsHolder.instance;
	}
	public static Connection getConnexion() throws SQLException
	{
		Connection con=null;
		try 
        {
			con=getConnexion(URLFAC);
        } 
        catch (SQLException ex) 
        {
        	con=getConnexion(URLDIST);
        } 
		
		return con;
	}
	private static Connection getConnexion(String url) throws SQLException
	{
		Connection con=null;
		try 
        {
			Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(url, LOGIN, PASS);
        } 
        catch (SQLException ex) 
        {
            try 
            {
                con = DriverManager.getConnection(url, LOGIN, PASS);
            } 
            catch (SQLException ex1) 
            {
                Logger.getLogger(ConnexionUtils.class.getSimpleName()).log(Level.SEVERE, null, ex1);
            }
        } 
		catch (ClassNotFoundException ex) 
        {
            Logger.getLogger(ConnexionUtils.class.getSimpleName()).log(Level.SEVERE, null, ex);
        }
		return con;
	}
}