package comn;

public class Libro {

    public int id;

    public String titulo;

    public String autor;

    public int anoPublicacion;

    public boolean disponible;

    public Libro(int id, String titulo, String autor, int anoPublicacion) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacion = anoPublicacion;
        this.disponible = true;
    }

    public Libro(int id, String titulo, String autor, int anoPublicacion, boolean disponible) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacion = anoPublicacion;
        this.disponible = disponible;
    }

}