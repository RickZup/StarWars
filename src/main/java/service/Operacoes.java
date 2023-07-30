package service;

import java.sql.*;
import java.util.Scanner;

public class Operacoes {

    private Statement statement;
    private ResultSet resultSet;

    private Scanner entrada = new Scanner(System.in);

    public Operacoes(Connection connection) {
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void consultaTodosOsDados() {
        String sql = "SELECT * FROM rebeldes";

        System.out.println();
        System.out.println(" ---------- Relação de Rebeldes ----------");

        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                System.out.println("--------------------------------------------------------------------");
                System.out.println("ID: " + resultSet.getInt("id") +
                        " | Nome: " + resultSet.getString("nome") +
                        " | Idade: " + resultSet.getInt("idade") +
                        " | Gênero: " + resultSet.getString("genero") +
                        " | Localização: " + resultSet.getString("localizacao") +
                        " | Status: " + getStatusString(resultSet.getBoolean("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getStatusString(boolean status) {
        return status ? "Ativo" : "Inativo";
    }

    public void adicionarRebelde() {

        System.out.println();
        System.out.println("Qual o nome do rebelde?");
        String nome = entrada.next();

        System.out.println();
        System.out.println("Qual a idade de " + nome + "?");
        int idade = entrada.nextInt();

        System.out.println();
        System.out.println("E o gênero?");
        String genero = entrada.next();

        System.out.println();
        System.out.println("Qual a localização de " + nome + "?");
        String localizacao = entrada.next();

        boolean status = true;

        String sql = "INSERT INTO rebeldes (nome, idade, genero, localizacao, status)" +
                "VALUES ('" + nome + "', " + idade + ", '" + genero + "', '" + localizacao + "', " + status + ")";
        try {
            statement.executeUpdate(sql);
            System.out.println();
            System.out.println("Rebelde adicionado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizarLocalizacao() {

        System.out.println();
        System.out.println("Qual o ID do rebelde?");
        int idRebelde = entrada.nextInt();

        System.out.println();
        System.out.println("E qual a nova localização?");
        String novaLocalizacao = entrada.next();

        String sql = "UPDATE rebeldes SET localizacao = '" + novaLocalizacao + "' WHERE id = " + idRebelde;

        try {
            int linhasAfetadas = statement.executeUpdate(sql);
            if (linhasAfetadas > 0) {
                System.out.println();
                System.out.println("Localização atualizada com sucesso para o rebelde de ID: " + idRebelde);
            } else {
                System.out.println();
                System.out.println("Rebelde não encontrado com o ID: " + idRebelde);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void comprarItem() {

        System.out.println();
        System.out.println("Qual seu ID?");
        int idRebelde = entrada.nextInt();

        System.out.println();
        System.out.println("Qual ID do item que deseja comprar?");
        exibirLoja();
        int idItem = entrada.nextInt();

        System.out.println();
        System.out.println("Quantos itens desse você deseja?");
        int quantidade = entrada.nextInt();


        String compraItem = "INSERT INTO inventario (id_rebelde, id_item, quantidade) " +
                "SELECT " + idRebelde + ", " + idItem + ", " + quantidade + " " +
                "FROM rebeldes " +
                "WHERE id = " + idRebelde + " AND status = true";

        try {

            int linhasAfetadas = statement.executeUpdate(compraItem);

            if (linhasAfetadas > 0) {
                System.out.println();
                System.out.println("Item comprado com sucesso e adicionado ao inventário do rebelde de ID: " + idRebelde);
            } else {
                System.out.println();
                System.out.println("VOCÊ É UM TRAIDOR!");
                System.out.println("Não importa onde esteja, vamos encontrá-lo...");
                System.out.println("Você pagará por isso.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void exibirInventario() {
        try {
            String sql = "SELECT inventario.id, inventario.id_rebelde, rebeldes.nome as rebelde_nome, inventario.id_item, loja.nome as item_nome, inventario.quantidade "
                    + "FROM inventario "
                    + "INNER JOIN rebeldes ON inventario.id_rebelde = rebeldes.id "
                    + "INNER JOIN loja ON inventario.id_item = loja.id";

            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println();
            System.out.println("----- Inventário -----");
            System.out.println("ID | ID Rebelde | Nome Rebelde | ID Item | Nome Item | Quantidade");
            System.out.println();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int idRebelde = resultSet.getInt("id_rebelde");
                String rebeldeNome = resultSet.getString("rebelde_nome");
                int idItem = resultSet.getInt("id_item");
                String itemNome = resultSet.getString("item_nome");
                int quantidade = resultSet.getInt("quantidade");

                System.out.println(id + " | " + idRebelde + " | " + rebeldeNome + " | " + idItem + " | " + itemNome + " | " + quantidade);
            }
            System.out.println("-----------------------------");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void exibirLoja() {
        try {
            String sql = "SELECT * FROM loja";

            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println();
            System.out.println("----- Loja -----");
            System.out.println("ID | Nome | Preço");
            System.out.println();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nome = resultSet.getString("nome");
                double preco = resultSet.getDouble("preco");

                System.out.println(id + " | " + nome + " | " + preco);
            }
            System.out.println("------------------------");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void acusarRebelde() throws SQLException {

        System.out.println();
        System.out.println("Informe o seu ID:");
        int idRebeldeAcusador = entrada.nextInt();

        System.out.println();
        System.out.println("Agora o ID  de quem deseja acusar como traidor:");
        int idRebeldeAcusado = entrada.nextInt();

        String consultaRebeldeAcusado = "SELECT id, status, acusacoes FROM rebeldes WHERE id = "
                + idRebeldeAcusado + " AND status = true";

        try {
            ResultSet resultSet = statement.executeQuery(consultaRebeldeAcusado);

            if (resultSet.next()) {
                int acusacoesAnteriores = resultSet.getInt("acusacoes");

                String atualizaAcusacoes = "UPDATE rebeldes SET acusacoes = "
                        + (acusacoesAnteriores + 1)
                        + " WHERE id = " + idRebeldeAcusado;

                int linhasAfetadas = statement.executeUpdate(atualizaAcusacoes);

                if (linhasAfetadas > 0) {
                    System.out.println();
                    System.out.println("Rebelde de ID " + idRebeldeAcusador + " acusou o rebelde de ID " + idRebeldeAcusado + " como reaidor!");
                    System.out.println("Número de acusações do rebelde de ID " + idRebeldeAcusado + " atualizado para: " + (acusacoesAnteriores + 1));

                    // Verifica se o número de acusações chegou a 3
                    if (acusacoesAnteriores + 1 >= 3) {
                        // Atualiza o status do rebelde acusado para false
                        String atualizaStatus = "UPDATE rebeldes SET status = false WHERE id = " + idRebeldeAcusado;
                        statement.executeUpdate(atualizaStatus);
                        System.out.println();
                        System.out.println("O rebelde de ID " + idRebeldeAcusado + " já recebeu três acusações e agora é um TRAIDOR!");
                    }
                } else {
                    System.out.println();
                    System.out.println("Não foi possível atualizar as acusações do rebelde de ID " + idRebeldeAcusado + ".");
                }
            } else {
                System.out.println();
                System.out.println("Rebelde acusado não encontrado ou já é um traidor!.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void exibirRelatorio() {
        try {
            // Consulta o total de rebeldes
            String sqlTotalRebeldes = "SELECT COUNT(*) as total FROM rebeldes";
            ResultSet resultSetTotalRebeldes = statement.executeQuery(sqlTotalRebeldes);
            resultSetTotalRebeldes.next();
            int totalRebeldes = resultSetTotalRebeldes.getInt("total");

            // Consulta o total de rebeldes traidores
            String sqlTotalTraidores = "SELECT COUNT(*) as totalTraidores FROM rebeldes WHERE status = false";
            ResultSet resultSetTotalTraidores = statement.executeQuery(sqlTotalTraidores);
            resultSetTotalTraidores.next();
            int totalTraidores = resultSetTotalTraidores.getInt("totalTraidores");

            // Calcula a porcentagem de rebeldes traidores
            double porcentagemTraidores = ((double) totalTraidores / totalRebeldes) * 100;
            double porcentagemNaoTraidores = 100 - porcentagemTraidores;

            // Exibe o relatório
            System.out.println("----- Relatório Rebeldes Traidores -----");
            System.out.println("Total de Rebeldes: " + totalRebeldes);
            System.out.println("Total de Rebeldes Traidores: " + totalTraidores + " (" + String.format("%.2f", porcentagemTraidores) + "%)");
            System.out.println("Total de Rebeldes Não Traidores: " + (totalRebeldes - totalTraidores) + " (" + String.format("%.2f", porcentagemNaoTraidores) + "%)");
            System.out.println("----------------------------------------");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
