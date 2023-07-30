package view;
import service.Operacoes;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Menu {

    private Operacoes operacoes;
    private Scanner entrada;

    public Menu(Connection connection) {
        operacoes = new Operacoes(connection);
        entrada = new Scanner(System.in);
    }

    public void exibirMenu() throws SQLException {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n--------- Menu ---------");
            System.out.println();
            System.out.println("1. Consultar todos os rebeldes");
            System.out.println("2. Adicionar rebelde");
            System.out.println("3. Atualizar localização do rebelde");
            System.out.println("4. Comprar item na loja");
            System.out.println("5. Exibir inventário");
            System.out.println("6. Exibir loja");
            System.out.println("7. Acusar rebelde como traidor");
            System.out.println("8. Exibir relatório");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = entrada.nextInt();

            switch (opcao) {
                case 1:
                    operacoes.consultaTodosOsDados();
                    break;
                case 2:
                    operacoes.adicionarRebelde();
                    break;
                case 3:
                    operacoes.atualizarLocalizacao();
                    break;
                case 4:
                    operacoes.comprarItem();
                    break;
                case 5:
                    operacoes.exibirInventario();
                    break;
                case 6:
                    operacoes.exibirLoja();
                    break;
                case 7:
                    operacoes.acusarRebelde();
                    break;
                case 8:
                    operacoes.exibirRelatorio();
                    break;
                case 0:
                    System.out.println();
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }
}
