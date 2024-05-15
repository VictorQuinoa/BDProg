package comn;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Interfaz extends JFrame {

    private JPanel panelPrincipal;

    public Interfaz() {
        super("Gestor de Biblioteca");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel Principal
        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.X_AXIS));

        // Panel Crear
        JPanel panelCrear = createBorderedPanel("Crear Libro");
        JTextField fldTitulo = new JTextField(10);
        JTextField fldAutor = new JTextField(10);
        JTextField fldPublicacion = new JTextField(10);
        JButton btnCrear = new JButton("Añadir Libro");

        panelCrear.add(new JLabel("Título:"));
        panelCrear.add(fldTitulo);
        panelCrear.add(Box.createVerticalStrut(20));
        panelCrear.add(new JLabel("Autor:"));
        panelCrear.add(fldAutor);
        panelCrear.add(Box.createVerticalStrut(20));
        panelCrear.add(new JLabel("Año Publicación:"));
        panelCrear.add(fldPublicacion);
        panelCrear.add(Box.createVerticalStrut(20));
        panelCrear.add(btnCrear);

        btnCrear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titulo = fldTitulo.getText().trim();
                String autor = fldAutor.getText().trim();
                int anoPublicacion = Integer.parseInt(fldPublicacion.getText().trim());

                if (titulo.isEmpty() || autor.isEmpty() || fldPublicacion.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(panelPrincipal, "Por favor complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Libro libro = Model.crearLibro(1,titulo, autor, anoPublicacion);
                if (libro != null) {
                    JOptionPane.showMessageDialog(panelPrincipal, "Libro añadido a la base de datos con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(panelPrincipal, "No se pudo añadir el libro a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Panel Cambiar
        JPanel panelCambiar = createBorderedPanel("Modificar Libro");
        JComboBox<String> boxLibrosTitulos = new JComboBox<>();
        JTextField fldTitulo2 = new JTextField(10);
        JTextField fldAutor2 = new JTextField(10);
        JTextField fldPublicacion2 = new JTextField(10);
        JSpinner spDisponible = new JSpinner();

        String[] opcionesDisponible = {"si", "no"};
        SpinnerListModel spinnerModel = new SpinnerListModel(opcionesDisponible);
        spDisponible.setModel(spinnerModel);

        JButton btnCambiar = new JButton("Modificar Libro");

        panelCambiar.add(boxLibrosTitulos);
        panelCambiar.add(new JLabel("Título:"));
        panelCambiar.add(fldTitulo2);
        panelCambiar.add(new JLabel("Autor:"));
        panelCambiar.add(fldAutor2);
        panelCambiar.add(new JLabel("Año Publicación:"));
        panelCambiar.add(fldPublicacion2);
        panelCambiar.add(new JLabel("Disponible:"));
        panelCambiar.add(spDisponible);
        panelCambiar.add(btnCambiar);

        btnCambiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String selectedTitulo = (String) boxLibrosTitulos.getSelectedItem();

                Libro libro = Model.buscarLibroPorTitulo(selectedTitulo);

                if (libro != null) {
                    libro.titulo = fldTitulo2.getText().trim();
                    libro.autor = fldAutor2.getText().trim();
                    libro.anoPublicacion = Integer.parseInt(fldPublicacion2.getText().trim());
                    libro.disponible = spDisponible.getValue().equals("si");

                    Model.actualizarLibro(libro);
                } else {
                    JOptionPane.showMessageDialog(panelPrincipal, "No se encontró el libro seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Panel Eliminar
        JPanel panelEliminar = createBorderedPanel("Eliminar Libro");
        JComboBox<String> boxLibrosTitulos2 = new JComboBox<>();
        JButton btnEliminar = new JButton("Eliminar Libro");

        panelEliminar.add(boxLibrosTitulos2);
        panelEliminar.add(Box.createVerticalStrut(160));
        panelEliminar.add(btnEliminar);

        // Panel Buscar
        JPanel panelBuscar = createBorderedPanel("Buscar Libro");
        JTextField fldBusqueda = new JTextField(20);
        JComboBox<String> boxTipoBusqueda = new JComboBox<>(new String[]{"Título", "Autor", "Año Publicación"});
        JTextArea areaLibros = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(areaLibros);

        panelBuscar.add(fldBusqueda);
        panelBuscar.add(boxTipoBusqueda);
        panelBuscar.add(scrollPane);

        panelPrincipal.add(panelCrear);
        panelPrincipal.add(panelCambiar);
        panelPrincipal.add(panelEliminar);
        panelPrincipal.add(panelBuscar);

        setContentPane(panelPrincipal);
        pack();
    }

    private JPanel createBorderedPanel(String title) {
        JPanel panel = new JPanel();
        Border border = BorderFactory.createTitledBorder(title);
        panel.setBorder(border);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Interfaz ventana = new Interfaz();
            ventana.setVisible(true);
        });
    }
}