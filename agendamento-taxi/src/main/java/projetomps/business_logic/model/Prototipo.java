package projetomps.business_logic.model;

/** Interface do padrão Prototype conforme o diagrama (apenas clonar). */
public interface Prototipo<T> {
    T clonar();   // retorna cópia de si próprio (deep copy)
}
