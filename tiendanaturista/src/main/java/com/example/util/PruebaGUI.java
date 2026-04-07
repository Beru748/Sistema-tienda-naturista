package com.example.util;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class PruebaGUI {

    public static void main(String[] args) {
        // 1. Configurar la ventana principal
        JFrame ventana = new JFrame("Vista de Datos de Oracle");
        ventana.setSize(600, 400); // Ventana más grande para que quepa la tabla
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Usamos BorderLayout para organizar fácilmente el botón arriba y la tabla en el centro
        ventana.setLayout(new BorderLayout()); 
        ventana.setLocationRelativeTo(null);

        // 2. Crear el botón y agregarlo en la parte superior (Norte)
        JButton btnCargar = new JButton("Cargar Inventario desde Oracle");
        ventana.add(btnCargar, BorderLayout.NORTH);

        // 3. Crear la tabla y agregarla dentro de un panel con scroll (por si hay muchos datos)
        JTable tablaDatos = new JTable();
        JScrollPane panelScroll = new JScrollPane(tablaDatos);
        ventana.add(panelScroll, BorderLayout.CENTER);

        // 4. Acción del botón
        btnCargar.addActionListener(e -> {
            // Obtenemos la conexión
            Connection con = ConexionDB.obtenerConexion();
            
            if (con != null) {
                // Preparamos el modelo que controlará los datos de la tabla
                DefaultTableModel modelo = new DefaultTableModel();
                tablaDatos.setModel(modelo);

                // --- AQUÍ PONES TU CONSULTA SQL ---
                // Cambia "productos" por el nombre real de tu tabla en Oracle
                String sql = "SELECT * FROM hr.employees"; 
                try (Statement st = con.createStatement();
                     ResultSet rs = st.executeQuery(sql)) {

                    // 4.1 Obtener los nombres de las columnas desde la Base de Datos
                    ResultSetMetaData metaDatos = rs.getMetaData();
                    int cantidadColumnas = metaDatos.getColumnCount();

                    for (int i = 1; i <= cantidadColumnas; i++) {
                        // Agregamos las columnas al modelo de la tabla
                        modelo.addColumn(metaDatos.getColumnLabel(i));
                    }

                    // 4.2 Leer los registros (filas) y agregarlos a la tabla
                    while (rs.next()) {
                        Object[] fila = new Object[cantidadColumnas];
                        for (int i = 0; i < cantidadColumnas; i++) {
                            // rs.getObject lee cualquier tipo de dato (String, Number, Date, etc.)
                            fila[i] = rs.getObject(i + 1); 
                        }
                        modelo.addRow(fila); // Agregamos la fila terminada al modelo
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(ventana, 
                        "Error al ejecutar la consulta SQL: " + ex.getMessage(), 
                        "Error SQL", 
                        JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(ventana, 
                    "No hay conexión a la base de datos.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        // 5. Mostrar la ventana
        ventana.setVisible(true);
    }
}