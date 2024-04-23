package com.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class Item {
    String jogos;
    String categoria;
    double avaliacao;

    public Item(String jogos, String categoria, double avaliacao) {
        this.jogos = jogos;
        this.categoria = categoria;
        this.avaliacao = avaliacao;
    }

    @Override
    public String toString() {
        return "Jogo: " + jogos + ", Categoria: " + categoria + ", Avaliação: " + avaliacao;
    }
}

public class OrdenacaoJogos {

    private static ArrayList<Item> itens = new ArrayList<>();

    public static void main(String[] args) {
        exibirMenu();
    }

    private static void exibirMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcao;
        do {
            System.out.println("Menu:");
            System.out.println("1. Ler o arquivo 'JogosDesordenados.csv'");
            System.out.println("2. Ordenar jogos pela categoria (Insertion Sort)");
            System.out.println("3. Ordenar jogos por avaliação em cada categoria (Selection Sort)");
            System.out.println("4. Fechar o programa");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            switch (opcao) {
                case 1:
                    lerArquivoCSV();
                    break;
                case 2:
                    ordenarPorCategoriaInsertionSort();
                    break;
                case 3:
                    ordenarPorAvaliacaoSelectionSort();
                    break;
                case 4:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 4);
        scanner.close();
    }

    private static void lerArquivoCSV() {
        try (CSVReader reader = new CSVReader(new FileReader("JogosDesordenados.csv"))) {
            String[] linha;
            while ((linha = reader.readNext()) != null) {
                if (linha.length >= 3) {
                    String jogos = linha[0];
                    String categoria = linha[1];
                    double avaliacao = Double.parseDouble(linha[2]);
                    itens.add(new Item(jogos, categoria, avaliacao));
                }
            }
            System.out.println("Arquivo lido com sucesso!");
        } catch (IOException | CsvException | NumberFormatException e) {
            System.err.println("Erro ao ler o arquivo CSV: " + e.getMessage());
        }
    }

    private static void ordenarPorCategoriaInsertionSort() {
        int n = itens.size();
        for (int i = 1; i < n; ++i) {
            Item chave = itens.get(i);
            int j = i - 1;
            while (j >= 0 && itens.get(j).categoria.compareTo(chave.categoria) > 0) {
                itens.set(j + 1, itens.get(j));
                j = j - 1;
            }
            itens.set(j + 1, chave);
        }
        salvarCSV("JogosOrdenadosporCategoria.csv");
    }

    private static void ordenarPorAvaliacaoSelectionSort() {
        int n = itens.size();
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (itens.get(j).avaliacao < itens.get(minIndex).avaliacao) {
                    minIndex = j;
                }
            }
            Item temp = itens.get(minIndex);
            itens.set(minIndex, itens.get(i));
            itens.set(i, temp);
        }
        salvarCSV("JogosOrdenadosporAvaliacao.csv");
    }

    private static void salvarCSV(String nomeArquivo) {
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            for (Item item : itens) {
                writer.append(item.jogos).append(",").append(item.categoria).append(",").append(String.valueOf(item.avaliacao)).append("\n");
            }
            writer.flush();
            System.out.println("Dados salvos em '" + nomeArquivo + "' com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo CSV: " + e.getMessage());
        }
    }
}
