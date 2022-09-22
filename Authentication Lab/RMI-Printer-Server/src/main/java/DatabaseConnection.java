
import java.sql.*;

public class DatabaseConnection {

    //Source : https://docs.microsoft.com/en-us/sql/connect/jdbc/step-3-proof-of-concept-connecting-to-sql-using-java?view=sql-server-ver15

    public Boolean userAuthenticated(String Username, String Password) {

        String connectionUrl =
                "jdbc:sqlserver://dtucourses.database.windows.net:1433;"
                        + "database=AuthLab;"
                        + "user=DataSecurity;"
                        + "password=DS12346!;"
                        + "encrypt=true;"
                        + "trustServerCertificate=false;"
                        + "loginTimeout=30;";
        
        ResultSet resultSet = null;

        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();) {

            String selectSql = ("SELECT COUNT (*) FROM dbo.Users WHERE dbo.Users.Username='"+Username+"' AND dbo.Users.Password='"+Password+"';");


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
}

