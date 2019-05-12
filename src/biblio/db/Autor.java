/*
 * Classe que representa a tabela Autor
 */
package biblio.db;

/**
 *
 * @author leonardosilva
 */
public class Autor {
    private int cod_autor;
    private String nome_autor;

    public Autor(int cod_autor, String nome_autor) {
        this.cod_autor = cod_autor;
        this.nome_autor = nome_autor;
    }
    
    /**
     * @return the cod_autor
     */
    public int getCod_autor() {
        return cod_autor;
    }

    /**
     * @param cod_autor the cod_autor to set
     */
    public void setCod_autor(int cod_autor) {
        this.cod_autor = cod_autor;
    }

    /**
     * @return the nome_autor
     */
    public String getNome_autor() {
        return nome_autor;
    }

    /**
     * @param nome_autor the nome_autor to set
     */
    public void setNome_autor(String nome_autor) {
        this.nome_autor = nome_autor;
    }
    
    @Override
    public String toString() {
        return this.getNome_autor();
    }
}
