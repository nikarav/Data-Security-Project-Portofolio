
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {

    //Source : https://docs.microsoft.com/en-us/sql/connect/jdbc/step-3-proof-of-concept-connecting-to-sql-using-java?view=sql-server-ver15

    private static String connectionUrl =
            "jdbc:sqlserver://dtucourses.database.windows.net:1433;"
                    + "database=AuthLab;"
                    + "user=DataSecurity;"
                    + "password=DS12346!;"
                    + "encrypt=true;"
                    + "trustServerCertificate=false;"
                    + "loginTimeout=30;";

    public static Boolean userAuthenticated(String Username, String Password) {
        
        ResultSet resultSet = null;

        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();) {

            String selectSql = ("SELECT COUNT (*) FROM dbo.UsersTask WHERE dbo.UsersTask.Username='"+Username+"' AND dbo.UsersTask.Password='"+Password+"';");


            resultSet = statement.executeQuery(selectSql);
            String count = "0";

            while (resultSet.next()) {
                count = resultSet.getString(1);

                System.out.println(count);

            }


            return (Integer.parseInt(count)!=0);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getAccessControl(String Username) { ResultSet resultSet = null;

        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();) {

            String selectSql = ("SELECT AccessRight FROM dbo.AccessControlTask WHERE dbo.AccessControlTask.Username='"+Username+"';");


            resultSet = statement.executeQuery(selectSql);
            List<String> accessRight = new ArrayList<String>() {};
            while (resultSet.next()) {
                accessRight.add(resultSet.getString(1));
                System.out.println(accessRight);
            }

            return accessRight;
        } catch (SQLException e) {
            System.out.println(e.toString());
            List<String> accessRight = new ArrayList<String>() {};
            accessRight.add("User");
            return accessRight;
        }
    }
}

