/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exercice3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

 public class ConnectionPool{

        protected static final String LOGIN ="yv965015";
        protected static final String PASS ="yv965015";
        protected static final String URLFAC ="jdbc:oracle:thin:@eluard:1521:ense2017";
        protected static final String URLDIST="jdbc:oracle:thin:@ufrsciencestech.u-bourgogne.fr:25559:ense2017";
        boolean dist;
        static final int INITIAL_CAPACITY = 10;
        
        LinkedList<Connection> pool = new LinkedList<Connection>();
        public String getConnString() {
            if(dist)
                return URLDIST;
            else 
                return URLFAC;
        }
        public String getPwd() {
            return PASS;
        }

        public String getUser() {
            return LOGIN;
        }

        public ConnectionPool() throws SQLException {

            for (int i = 0; i < INITIAL_CAPACITY; i++) {
                try{
                    pool.add(DriverManager.getConnection(URLFAC, LOGIN, PASS));
                    this.dist=false;
                }
                catch (Exception e){
                    pool.add(DriverManager.getConnection(URLDIST, LOGIN, PASS));
                    this.dist=true;
                }
            }

        }

        public synchronized Connection getConnection() throws SQLException {
            if (pool.isEmpty()) {
                try{
                    pool.add(DriverManager.getConnection(URLFAC, LOGIN, PASS));
                }
                catch (Exception e){
                    pool.add(DriverManager.getConnection(URLDIST, LOGIN, PASS));
                }
                
            }
            return pool.pop();
        }

        public synchronized void returnConnection(Connection connection) {
            pool.push(connection);
        }  
  }