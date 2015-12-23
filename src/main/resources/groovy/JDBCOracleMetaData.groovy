                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
import oracle.jdbc.pool.OracleDataSource;
import groovy.sql.*;
import java.sql.*;
import oracle.jdbc.*;
import oracle.jdbc.aq.*;
import java.util.concurrent.*;

String DRIVER = "oracle.jdbc.OracleDriver";
//String URL = "jdbc:oracle:thin:@//leopard:1521/XE";
//String URL = "jdbc:oracle:thin:@//tporacle:1521/ORCL";
String URL = "jdbc:oracle:thin:@//localhost:1521/XE";
//String URL = "jdbc:oracle:thin:@//horacle:1521/cdb1";
String USER = "tqreactor";
String PASS = "tq";

ds = new OracleDataSource();
ds.setDriverType(DRIVER);
ds.setURL(URL);
ds.setUser(USER);
ds.setPassword(PASS);
osql = Sql.newInstance(URL, USER, PASS, DRIVER);
conn = null;
ps = null;
rs = null;
binds = [];
try {
    conn = ds.getConnection();
    println "Connected [${conn.getMetaData().getURL()}]";
    osql.eachRow("select distinct data_type, data_length, data_precision, data_scale from all_tab_columns where data_type_mod is null and owner not in  ('APEX_040000', 'SYS', 'XDB', 'SYSTEM')", { 
        if(!it.DATA_TYPE.contains('$')) { 
            bind = [it.DATA_TYPE, it.DATA_LENGTH, it.DATA_PRECISION, it.DATA_SCALE];
            binds.add(bind);
        }
    });
    println "Distinct Data Types: [${binds.size()}]";

    binds.each() { bind ->
        osql.eachRow("select OWNER, TABLE_NAME, COLUMN_NAME from all_tab_columns where data_type = ? and data_length = ? and (data_precision = ? or data_precision is null)  and (data_scale = ? or data_scale is null) and ROWNUM < 2 and owner not in  ('APEX_040000', 'SYS', 'XDB', 'SYSTEM')", bind, { tabcol ->
            print tabcol;
            print " ---> ";
            println bind;
            ps = conn.prepareStatement("SELECT ${tabcol.COLUMN_NAME} FROM ${tabcol.OWNER}.${tabcol.TABLE_NAME} WHERE ROWNUM < 2");
            rs = ps.executeQuery();
            rsmd = rs.getMetaData();
            colType = rsmd.getColumnType(1);
            colName = rsmd.getColumnTypeName(1);
            println "\t$colType : $colName";
            rs.close();
            ps.close();
        });
    }

} finally {
    if(rs!=null) try { rs.close(); } catch (e) {}
    if(ps!=null) try { ps.close(); } catch (e) {}
    if(conn!=null) try { conn.close(); } catch (e) {}
}

return null;