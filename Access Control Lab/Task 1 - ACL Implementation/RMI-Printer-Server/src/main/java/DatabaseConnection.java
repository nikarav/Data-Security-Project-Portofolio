
import java.sql.*;

public class DatabaseConnection {

    // Source :
    // https://docs.microsoft.com/en-us/sql/connect/jdbc/step-3-proof-of-concept-connecting-to-sql-using-java?view=sql-server-ver15

    private static String connectionUrl = "jdbc:sqlserver:<URL>;"
            + "database=<database>;"
            + "user=<user>;"
            + "password=<password>!;"
            + "encrypt=true;"
            + "trustServerCertificate=false;"
            + "loginTimeout=30;";

    public static Boolean userAuthenticated(String Username, String Password) {

        ResultSet resultSet = null;

        try (Connection connection = DriverManager.getConnection(connectionUrl);
                Statement statement = connection.createStatement();) {

            String selectSql = ("SELECT COUNT (*) FROM dbo.UsersTask WHERE dbo.UsersTask.Username='" + Username
                    + "' AND dbo.UsersTask.Password='" + Password + "';");

            resultSet = statement.executeQuery(selectSql);
            String count = "0";

            while (resultSet.next()) {
                count = resultSet.getString(1);

                System.out.println(count);

            }

            return (Integer.parseInt(count) != 0);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
