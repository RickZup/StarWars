import connection.Conexao;
import view.Menu;

import java.sql.Connection;
import java.sql.SQLException;

public class main {
    public static void main(String[] args) throws SQLException {

        Conexao conexao = new Conexao();
        Connection minhaConexao = conexao.getConnection();
        Menu menu = new Menu(minhaConexao);

        menu.exibirMenu();
    }
}
