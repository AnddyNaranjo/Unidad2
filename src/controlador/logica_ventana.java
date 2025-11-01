package controlador;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import vista.ventana;
import modelo.*;

//Definición de la clase logica_ventana que implementa tres interfaces para manejar eventos.
public class logica_ventana implements ActionListener, ListSelectionListener, ItemListener {
	private ventana delegado; // Referencia a la ventana principal que contiene la GUI.
	private String nombres, email, telefono, categoria=""; // Variables para almacenar datos del contacto.
	private persona persona; // Objeto de tipo persona, que representa un contacto.
	private List<persona> contactos; // Lista de objetos persona que representa todos los contactos.
	private boolean favorito = false || true; // Booleano que indica si un contacto es favorito.

	// Constructor que inicializa la clase y configura los escuchadores de eventos para los componentes de la GUI.
	public logica_ventana(ventana delegado) {
		  // Asigna la ventana recibida como parámetro a la variable de instancia delegado.
	    this.delegado = delegado;
	    // Carga los contactos almacenados al inicializar.
	    cargarContactosRegistrados(); 
	    // Registra los ActionListener para los botones de la GUI.
	    this.delegado.btn_add.addActionListener(this);
	    this.delegado.btn_eliminar.addActionListener(this);
	    this.delegado.btn_modificar.addActionListener(this);
	    // Registra los ListSelectionListener para la lista de contactos.

	    // Registra los ItemListener para el JComboBox de categoría y el JCheckBox de favoritos.
	    this.delegado.cmb_categoria.addItemListener(this);
	    this.delegado.chb_favorito.addItemListener(this);
	    
	    this.delegado.tablaContactos.getSelectionModel().addListSelectionListener(this);
	    
	    this.delegado.btn_exportar.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            exportarCSV();
	        }
	    });



	    
	    //Metodo de busqueda
	    this.delegado.txt_buscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
	        public void insertUpdate(javax.swing.event.DocumentEvent e) {
	            filtrarTabla();
	        }
	        public void removeUpdate(javax.swing.event.DocumentEvent e) {
	            filtrarTabla();
	        }
	        public void changedUpdate(javax.swing.event.DocumentEvent e) {
	            filtrarTabla();
	        }
	    });
	    
	    
	    //metodo exportar
	    this.delegado.btn_exportar.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            exportarCSV();
	        }
	    });

	    
	    //metodo acortado para exportar CSV
	    javax.swing.KeyStroke exportKey = javax.swing.KeyStroke.getKeyStroke("control E");
	    javax.swing.InputMap inputMap = delegado.contentPane.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
	    javax.swing.ActionMap actionMap = delegado.contentPane.getActionMap();

	    inputMap.put(exportKey, "exportarCSV");
	    actionMap.put("exportarCSV", new javax.swing.AbstractAction() {
	        public void actionPerformed(ActionEvent e) {
	            delegado.btn_exportar.doClick(); // Simula clic en el botón
	        }
	    });



	    //metodo para menu con click derecho
	    javax.swing.JPopupMenu menuContextual = new javax.swing.JPopupMenu();

	    javax.swing.JMenuItem modificarItem = new javax.swing.JMenuItem("Modificar");
	    modificarItem.addActionListener(e -> delegado.btn_modificar.doClick());
	    menuContextual.add(modificarItem);

	    javax.swing.JMenuItem eliminarItem = new javax.swing.JMenuItem("Eliminar");
	    eliminarItem.addActionListener(e -> delegado.btn_eliminar.doClick());
	    menuContextual.add(eliminarItem);

	    delegado.tablaContactos.addMouseListener(new java.awt.event.MouseAdapter() {
	        public void mousePressed(java.awt.event.MouseEvent e) {
	            if (e.isPopupTrigger()) {
	                int fila = delegado.tablaContactos.rowAtPoint(e.getPoint());
	                delegado.tablaContactos.setRowSelectionInterval(fila, fila);
	                menuContextual.show(delegado.tablaContactos, e.getX(), e.getY());
	            }
	        }

	        public void mouseReleased(java.awt.event.MouseEvent e) {
	            if (e.isPopupTrigger()) {
	                int fila = delegado.tablaContactos.rowAtPoint(e.getPoint());
	                delegado.tablaContactos.setRowSelectionInterval(fila, fila);
	                menuContextual.show(delegado.tablaContactos, e.getX(), e.getY());
	            }
	        }
	    });

	}

	// Método privado para inicializar las variables con los valores ingresados en la GUI.
	private void incializacionCampos() {
		// Obtiene el texto ingresado en los campos de nombres, email y teléfono de la GUI.
		nombres = delegado.txt_nombres.getText();
		email = delegado.txt_email.getText();
		telefono = delegado.txt_telefono.getText();
	    favorito = delegado.chb_favorito.isSelected(); // Asegura que se lea el estado actual
	}

	// Método privado para cargar los contactos almacenados desde un archivo.
	private void cargarContactosRegistrados() {
	    SwingWorker<Void, Void> worker = new SwingWorker<>() {
	        @Override
	        protected Void doInBackground() {
	            delegado.barraProgreso.setIndeterminate(true); // Comienza la animación
	            try {
	                contactos = new personaDAO(new persona()).leerArchivo();
	                delegado.modeloTabla.setRowCount(0);
	                for (persona contacto : contactos) {
	                    delegado.modeloTabla.addRow(new Object[]{
	                        contacto.getNombre(),
	                        contacto.getTelefono(),
	                        contacto.getEmail(),
	                        contacto.getCategoria(),
	                        contacto.isFavorito()
	                    });
	            	    actualizarEstadisticas();

	                }
	            } catch (IOException e) {
	                JOptionPane.showMessageDialog(delegado, "Existen problemas al cargar todos los contactos");
	            }
	            return null;
	        }

	        @Override
	        protected void done() {
	            delegado.barraProgreso.setIndeterminate(false); // Detiene la animación
	            delegado.barraProgreso.setValue(100); // Marca como completado
	        }
	    };
	    worker.execute();

	}


	// Método privado para limpiar los campos de entrada en la GUI y reiniciar variables.
	private void limpiarCampos() {
		delegado.txt_nombres.setText("eliminado");
	    delegado.txt_telefono.setText("S/N");
	    delegado.txt_email.setText("S/N");
	    categoria = "";
	    favorito = false;
	    delegado.chb_favorito.setSelected(favorito);
	    delegado.cmb_categoria.setSelectedIndex(0);
	    incializacionCampos();
	    cargarContactosRegistrados();
	}

	// Método que maneja los eventos de acción (clic) en los botones.
	@Override
	public void actionPerformed(ActionEvent e) {
		incializacionCampos();
	    // Verifica si el evento proviene del botón "Agregar".
	    if (e.getSource() == delegado.btn_add) {
			 // Inicializa las variables con los valores actuales de la GUI.
	        // Verifica si los campos de nombres, teléfono y email no están vacíos.
	        if ((!nombres.equals("")) && (!telefono.equals("")) && (!email.equals(""))) {
	            // Verifica si se ha seleccionado una categoría válida.
	            if ((!categoria.equals("Elija una Categoria")) && (!categoria.equals(""))) {
	                // Crea un nuevo objeto persona con los datos ingresados y lo guarda.
	                persona = new persona(nombres, telefono, email, categoria, favorito);
	                try {
	                    new personaDAO(persona).escribirArchivo();
	                } catch (IOException ex) {
	                    ex.printStackTrace(); // O puedes mostrar un mensaje al usuario
	                    JOptionPane.showMessageDialog(null, "Error al guardar el contacto: " + ex.getMessage());
	                }
	                // Limpia los campos después de agregar el contacto.
	                limpiarCampos();
	                // Muestra un mensaje de éxito.
	                JOptionPane.showMessageDialog(delegado, "Contacto Registrado!!!");
	            } else {
	                // Muestra un mensaje de advertencia si no se ha seleccionado una categoría válida.
	                JOptionPane.showMessageDialog(delegado, "Elija una Categoria!!!");
	            }
	        } else {
	            // Muestra un mensaje de advertencia si algún campo está vacío.
	            JOptionPane.showMessageDialog(delegado, "Todos los campos deben ser llenados!!!");
	        }
	    
	    } else if (e.getSource() == delegado.btn_eliminar) {
	    	int index = delegado.tablaContactos.convertRowIndexToModel(delegado.tablaContactos.getSelectedRow());
	        if (index != -1) {
	            index = delegado.tablaContactos.convertRowIndexToModel(index); // Corrige el índice si hay filtro

	            int confirm = JOptionPane.showConfirmDialog(delegado, "¿Estás seguro de eliminar este contacto?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
	            if (confirm == JOptionPane.YES_OPTION) {
	                try {
	                    contactos = new personaDAO(new persona()).leerArchivo(); // Recarga la lista completa
	                    contactos.remove(index); // Elimina el contacto seleccionado
	                    new personaDAO(new persona()).actualizarContactos(contactos); // Guarda los cambios
	                    limpiarCampos(); // Refresca la tabla y limpia los campos
	                    JOptionPane.showMessageDialog(delegado, "Contacto eliminado correctamente.");
	                } catch (IOException ex) {
	                    JOptionPane.showMessageDialog(delegado, "Error al eliminar el contacto.");
	                }
	            }
	        } else {
	            JOptionPane.showMessageDialog(delegado, "Seleccione un contacto para eliminar.");
	        }
	    	
	    } else if (e.getSource() == delegado.btn_modificar) {
	        int index = delegado.tablaContactos.getSelectedRow();
	        if (index != -1) {
	            index = delegado.tablaContactos.convertRowIndexToModel(index); // Corrige el índice si hay filtro
	            incializacionCampos();

	            if ((!nombres.equals("")) && (!telefono.equals("")) && (!email.equals(""))) {
	                if ((!categoria.equals("Elija una Categoria")) && (!categoria.equals(""))) {
	                    try {
	                        contactos = new personaDAO(new persona()).leerArchivo(); // Recarga la lista completa
	                        contactos.get(index).setNombre(nombres);
	                        contactos.get(index).setTelefono(telefono);
	                        contactos.get(index).setEmail(email);
	                        contactos.get(index).setCategoria(categoria);
	                        contactos.get(index).setFavorito(favorito);

	                        new personaDAO(new persona()).actualizarContactos(contactos); // Guarda todos los contactos
	                        System.out.println(contactos.get(index).isFavorito());
	                        limpiarCampos(); // Refresca la tabla y limpia los campos
	                        JOptionPane.showMessageDialog(delegado, "Contacto modificado correctamente.");
	                    } catch (IOException ex) {
	                        JOptionPane.showMessageDialog(delegado, "Error al modificar el contacto.");
	                    }
	                } else {
	                    JOptionPane.showMessageDialog(delegado, "Elija una categoría válida.");
	                }
	            } else {
	                JOptionPane.showMessageDialog(delegado, "Todos los campos deben estar llenos.");
	            }
	        } else {
	            JOptionPane.showMessageDialog(delegado, "Seleccione un contacto para modificar.");
	        }
	    }

	}

	// Método que maneja los eventos de selección en la lista de contactos.
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// Obtiene el índice del elemento seleccionado en la lista de contactos.
		int index = delegado.tablaContactos.getSelectedRow();

		if (index != -1) {
		    index = delegado.tablaContactos.convertRowIndexToModel(index);
		    delegado.txt_nombres.setText(delegado.modeloTabla.getValueAt(index, 0).toString());
		    delegado.txt_telefono.setText(delegado.modeloTabla.getValueAt(index, 1).toString());
		    delegado.txt_email.setText(delegado.modeloTabla.getValueAt(index, 2).toString());
		    delegado.cmb_categoria.setSelectedItem(delegado.modeloTabla.getValueAt(index, 3).toString());
		    delegado.chb_favorito.setSelected((boolean) delegado.modeloTabla.getValueAt(index, 4));
		}
	    
	}

	// Método privado para cargar los datos del contacto seleccionado en los campos de la GUI.
	private void cargarContacto(int index) {
		// Establece el nombre del contacto en el campo de texto de nombres.
	    delegado.txt_nombres.setText(contactos.get(index).getNombre());
	    // Establece el teléfono del contacto en el campo de texto de teléfono.
	    delegado.txt_telefono.setText(contactos.get(index).getTelefono());
	    // Establece el correo electrónico del contacto en el campo de texto de correo electrónico.
	    delegado.txt_email.setText(contactos.get(index).getEmail());
	    // Establece el estado de favorito del contacto en el JCheckBox de favorito.
	    delegado.chb_favorito.setSelected(contactos.get(index).isFavorito());
	    // Establece la categoría del contacto en el JComboBox de categoría.
	    delegado.cmb_categoria.setSelectedItem(contactos.get(index).getCategoria());
	}

	// Método que maneja los eventos de cambio de estado en los componentes cmb_categoria y chb_favorito.
	@Override
	public void itemStateChanged(ItemEvent e) {
		// Verifica si el evento proviene del JComboBox de categoría.
	    if (e.getSource() == delegado.cmb_categoria) {
	        // Obtiene el elemento seleccionado en el JComboBox y lo convierte en una cadena.
	        categoria = delegado.cmb_categoria.getSelectedItem().toString();
	        // Actualiza la categoría seleccionada en la variable "categoria".
	    } else if (e.getSource() == delegado.chb_favorito) {
	        // Verifica si el evento proviene del JCheckBox de favorito.
	        favorito = delegado.chb_favorito.isSelected();
	        // Obtiene el estado seleccionado del JCheckBox y actualiza el estado de favorito en la variable "favorito".
	    }
	}
	
	//metodo para filtrar la tabla
	private void filtrarTabla() {
	    String texto = delegado.txt_buscar.getText();
	    TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) delegado.tablaContactos.getRowSorter();

	    if (texto.trim().length() == 0) {
	        sorter.setRowFilter(null);
	    } else {
	        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto, 0)); // Filtra por columna 0 (Nombre)
	    }
	}
	
	
	//creamos el metodo csv
	private void exportarCSV() {
	    try {
	        FileWriter writer = new FileWriter("c:/gestionContactos/exportados.csv");

	        // Escribir encabezado
	        writer.write("Nombre;Telefono;Email;Categoria;Favorito\n");

	        // Recorrer la tabla
	        for (int i = 0; i < delegado.modeloTabla.getRowCount(); i++) {
	            for (int j = 0; j < delegado.modeloTabla.getColumnCount(); j++) {
	                writer.write(delegado.modeloTabla.getValueAt(i, j).toString() + ";");
	            }
	            writer.write("\n");
	        }

	        writer.close();
	        JOptionPane.showMessageDialog(delegado, "Contactos exportados correctamente.");
	    } catch (IOException ex) {
	        JOptionPane.showMessageDialog(delegado, "Error al exportar contactos.");
	    }
	}
	
	//metodos de la ventana de estadistica
	private void actualizarEstadisticas() {
	    int total = contactos.size();
	    int favoritos = 0;
	    int familia = 0;
	    int amigos = 0;
	    int trabajo = 0;

	    for (persona p : contactos) {
	        if (p.isFavorito()) favoritos++;
	        switch (p.getCategoria().toLowerCase()) {
	            case "familia": familia++; break;
	            case "amigos": amigos++; break;
	            case "trabajo": trabajo++; break;
	        }
	    }

	    delegado.lbl_totalContactos.setText("Total de contactos: " + total);
	    delegado.lbl_favoritos.setText("Contactos favoritos: " + favoritos);
	    delegado.lbl_familia.setText("Familia: " + familia);
	    delegado.lbl_amigos.setText("Amigos: " + amigos);
	    delegado.lbl_trabajo.setText("Trabajo: " + trabajo);
	}


}