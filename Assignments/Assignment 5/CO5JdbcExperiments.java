import java.sql.*;
import java.util.*;

public class CO5JdbcExperiments {
    // Configure with -Ddb.url, -Ddb.user and -Ddb.password; never hard-code credentials.
    static Connection connect() throws SQLException {
        String url=System.getProperty("db.url","jdbc:mysql://localhost:3306/java_assignment");
        String user=System.getProperty("db.user","root");
        String password=System.getProperty("db.password","");
        return DriverManager.getConnection(url,user,password);
    }
    static boolean unsafeLogin(Connection c,String id,String password)throws SQLException {
        String sql="SELECT COUNT(*) FROM app_user WHERE user_id='"+id+"' AND password_text='"+password+"'";
        try(Statement s=c.createStatement();ResultSet r=s.executeQuery(sql)){r.next();return r.getInt(1)>0;}
    }
    static boolean safeLogin(Connection c,String id,String password)throws SQLException {
        String sql="SELECT COUNT(*) FROM app_user WHERE user_id=? AND password_text=?";
        try(PreparedStatement p=c.prepareStatement(sql)){p.setString(1,id);p.setString(2,password);try(ResultSet r=p.executeQuery()){r.next();return r.getInt(1)>0;}}
    }
    static void createTables(Connection c)throws SQLException {
        try(Statement s=c.createStatement()){
            s.executeUpdate("CREATE TABLE IF NOT EXISTS app_user(user_id VARCHAR(60) PRIMARY KEY,password_text VARCHAR(120) NOT NULL)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS bus_pass(pass_id VARCHAR(30) PRIMARY KEY,reg_no VARCHAR(30) NOT NULL,name VARCHAR(120) NOT NULL,route_no INT NOT NULL,amount DECIMAL(10,2) NOT NULL)");
        }
    }
    static long insertStatement(Connection c,List<String[]> rows)throws SQLException {
        long start=System.nanoTime();
        try(Statement s=c.createStatement()){for(String[]r:rows)s.executeUpdate("INSERT INTO bus_pass VALUES('"+r[0]+"','"+r[1]+"','"+r[2]+"',"+r[3]+","+r[4]+")");}
        return System.nanoTime()-start;
    }
    static long insertPrepared(Connection c,List<String[]> rows)throws SQLException {
        long start=System.nanoTime();
        try(PreparedStatement p=c.prepareStatement("INSERT INTO bus_pass VALUES(?,?,?,?,?)")){
            for(String[]r:rows){p.setString(1,r[0]);p.setString(2,r[1]);p.setString(3,r[2]);p.setInt(4,Integer.parseInt(r[3]));p.setBigDecimal(5,new java.math.BigDecimal(r[4]));p.executeUpdate();}
        }return System.nanoTime()-start;
    }
    static long insertBatch(Connection c,List<String[]> rows)throws SQLException {
        boolean old=c.getAutoCommit();c.setAutoCommit(false);long start=System.nanoTime();
        try(PreparedStatement p=c.prepareStatement("INSERT INTO bus_pass VALUES(?,?,?,?,?)")){
            int count=0;for(String[]r:rows){p.setString(1,r[0]);p.setString(2,r[1]);p.setString(3,r[2]);p.setInt(4,Integer.parseInt(r[3]));p.setBigDecimal(5,new java.math.BigDecimal(r[4]));p.addBatch();if(++count%1000==0)p.executeBatch();}
            p.executeBatch();c.commit();return System.nanoTime()-start;
        }catch(Exception e){c.rollback();if(e instanceof SQLException se)throw se;throw new SQLException(e);}
        finally{c.setAutoCommit(old);}
    }
    static void metadata(Connection c)throws SQLException {
        DatabaseMetaData m=c.getMetaData();
        System.out.println("Database: "+m.getDatabaseProductName()+" "+m.getDatabaseProductVersion());
        System.out.println("Driver: "+m.getDriverName()+" "+m.getDriverVersion());
        try(ResultSet r=m.getTables(c.getCatalog(),null,"%",new String[]{"TABLE"})){while(r.next())System.out.println("Table: "+r.getString("TABLE_NAME"));}
    }
    public static void main(String[] args)throws Exception {
        try(Connection c=connect()){createTables(c);metadata(c);
            System.out.println("Unsafe injection accepted: "+unsafeLogin(c,"' OR '1'='1' -- ","x"));
            System.out.println("Prepared injection accepted: "+safeLogin(c,"' OR '1'='1' -- ","x"));
        }
    }
}