package projetomps.business_logic.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import projetomps.business_logic.model.Rotation;
import projetomps.business_logic.model.RotationPrototype;
import projetomps.business_logic.model.Taxist;

/**
 * Classe cliente que demonstra o uso do padrão Prototype.
 */
public class ClientePrototype {

    public void operacao() {
        // 1) Cria a rotação base (protótipo original)
        Rotation original = new Rotation();
        original.setIdRotation(1);
        original.setDate(LocalDate.of(2025, 10, 25));
        original.setStartTime(LocalTime.of(7, 0));
        original.setEndTime(LocalTime.of(15, 0));
        original.setStatus("AGENDADA");
        original.setTaxist(new Taxist("joao", "senha123"));
        original.getTaxist().setName("João das Neves");
        original.getTaxist().setEmail("joao@example.com");

        // 2) Cria o protótipo a partir da rotação original
        RotationPrototype prototipo = new RotationPrototype(original);

        // 3) Clona a rotação base com algumas alterações
        Rotation clone = prototipo.clonarComAlteracoes(
                LocalDate.of(2025, 10, 26),
                LocalTime.of(15, 0),
                LocalTime.of(23, 0),
                new Taxist("maria", "senha456")
        );
        clone.getTaxist().setName("Maria Souza");
        clone.getTaxist().setEmail("maria@example.com");

        // 4) Exibe os resultados
        System.out.println("=== Protótipo Original ===");
        System.out.println("Data: " + original.getDate());
        System.out.println("Início: " + original.getStartTime());
        System.out.println("Fim: " + original.getEndTime());
        System.out.println("Motorista: " + original.getTaxist().getName());

        System.out.println("\n=== Clone Gerado ===");
        System.out.println("Data: " + clone.getDate());
        System.out.println("Início: " + clone.getStartTime());
        System.out.println("Fim: " + clone.getEndTime());
        System.out.println("Motorista: " + clone.getTaxist().getName());
    }

    public static void main(String[] args) {
        new ClientePrototype().operacao();
    }
}
