package baleroni.saveit;

import java.io.Serializable;

// number = Math.ceil(number * 100) / 100;

public class Projeto implements Serializable {
    long id;
    private String nome;
    private float objetivo = 0;
    private float corrente;

    public Projeto(long id, String nome) {
        this.id = id;
        this.nome = nome;
        this.corrente = 0;
    }

    public Projeto(String nome) {
        this(0, nome);
    }

    public String get_nome() {
        return this.nome;
    }

    public Projeto mudar_nome(String novo_nome) {
        this.nome = novo_nome;
        return this;
    }

    public float get_objetivo() {
        return this.objetivo;
    }

    public float get_corrente() {
        return this.corrente;
    }

    public int add_corrente(float valor) {
        if (valor < 0) {
            return -1;
        }
        corrente += valor;
        return 0;
    }

    public int sub_corrente(float valor) {
        if (valor < 0) {
            return -1;
        }
        corrente -= valor;
        return 0;
    }

    @Override
    public String toString() {
        return nome;
    }
}
