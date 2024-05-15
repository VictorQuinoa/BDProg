package comn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Model {

    private static final String URL = "jdbc:mysql://localhost:3306/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public static Libro crearLibro(int id, String titulo, String autor, int anoPublicacion) {
        Libro libro = new Libro(id, titulo, autor, anoPublicacion);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO libros (id, titulo, autor, ano_publicacion) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            statement.setString(2, titulo);
            statement.setString(3, autor);
            statement.setInt(4, anoPublicacion);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Libro añadido a la base de datos con éxito.");
            } else {
                System.out.println("No se pudo añadir el libro a la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("Error al añadir el libro a la base de datos: " + e.getMessage());
        }
        return libro;
    }

    public static void eliminarLibro(int id) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "DELETE FROM libros WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, id);

                int rowsDeleted = statement.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("Libro eliminado de la base de datos con éxito.");
                } else {
                    System.out.println("No se encontró ningún libro con el ID especificado.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar el libro de la base de datos: " + e.getMessage());
        }
    }

    public static Libro obtenerLibroPorId(int id) {
        Libro libro = null;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT id, titulo, autor, ano_publicacion FROM libros WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, id);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int libroId = resultSet.getInt("id");
                        String titulo = resultSet.getString("titulo");
                        String autor = resultSet.getString("autor");
                        int anoPublicacion = resultSet.getInt("ano_publicacion");

                        libro = new Libro(libroId, titulo, autor, anoPublicacion);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el libro por ID: " + e.getMessage());
        }

        return libro;
    }

    public static String getTitulo(Integer id) {
        return obtenerLibroPorId(id).titulo;
    }

    public static String getAutor(Integer id) {
        return obtenerLibroPorId(id).autor;
    }

    public static int getAnoPublicacion(Integer id) {
        return obtenerLibroPorId(id).anoPublicacion;
    }

    public static boolean getDisponible(Integer id) {
        return obtenerLibroPorId(id).disponible;
    }

    public static void actualizarLibro(Libro libro) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "UPDATE libros SET titulo = ?, autor = ?, ano_publicacion = ?, disponible = ? WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, libro.titulo);
                statement.setString(2, libro.autor);
                statement.setInt(3, libro.anoPublicacion);
                statement.setInt(4, libro.id);
                statement.setBoolean(5, libro.disponible);

                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Libro actualizado en la base de datos con éxito.");
                } else {
                    System.out.println("No se encontró ningún libro con el ID especificado para actualizar.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar el libro en la base de datos: " + e.getMessage());
        }
    }

    public static Libro buscarLibroPorTitulo(String titulo) {
        Libro libroEncontrado = null;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT id, titulo, autor, ano_publicacion FROM libros WHERE titulo LIKE ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, "%" + titulo + "%");

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int libroId = resultSet.getInt("id");
                        String tituloEncontrado = resultSet.getString("titulo");
                        String autor = resultSet.getString("autor");
                        int anoPublicacion = resultSet.getInt("ano_publicacion");

                        libroEncontrado = new Libro(libroId, tituloEncontrado, autor, anoPublicacion);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar libro por título: " + e.getMessage());
        }

        return libroEncontrado;
    }

    public static List<Libro> buscarLibros(String titulo, String autor, boolean disponible, Integer anoPublicacion) {
        List<Libro> librosEncontrados = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            StringBuilder queryBuilder = new StringBuilder("SELECT id, titulo, autor, ano_publicacion FROM libros WHERE 1 = 1");

            if (titulo != null && !titulo.isEmpty()) {
                queryBuilder.append(" AND titulo LIKE ?");
            }
            if (autor != null && !autor.isEmpty()) {
                queryBuilder.append(" AND autor LIKE ?");
            }
            if (anoPublicacion > 0) {
                queryBuilder.append(" AND ano_publicacion = ?");
            }
            if (disponible) {
                queryBuilder.append(" AND disponible = true");
            }

            try (PreparedStatement statement = conn.prepareStatement(queryBuilder.toString())) {
                int parameterIndex = 1;

                if (titulo != null && !titulo.isEmpty()) {
                    statement.setString(parameterIndex++, "%" + titulo + "%");
                }
                if (autor != null && !autor.isEmpty()) {
                    statement.setString(parameterIndex++, "%" + autor + "%");
                }
                if (anoPublicacion > 0) {
                    statement.setInt(parameterIndex++, anoPublicacion);
                }

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int libroId = resultSet.getInt("id");
                        String tituloEncontrado = resultSet.getString("titulo");
                        String autorEncontrado = resultSet.getString("autor");
                        int anoPublicacionEncontrado = resultSet.getInt("ano_publicacion");

                        Libro libro = new Libro(libroId, tituloEncontrado, autorEncontrado, anoPublicacionEncontrado);
                        librosEncontrados.add(libro);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar libros: " + e.getMessage());
        }

        return librosEncontrados;
    }
}