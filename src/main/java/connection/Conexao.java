package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {

    public Connection getConnection(){
        try{
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/starwars",
                    "postgres", "6126");
            Statement statement = connection.createStatement();
            return connection;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
