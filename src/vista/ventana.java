package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import controlador.logica_ventana;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

public class ventana extends JFrame {

	public JPanel contentPane; // Panel principal que contendrá todos los componentes de la interfaz.
	public JTextField txt_nombres; // Campo de texto para ingresar nombres.
	public JTextField txt_telefono; // Campo de texto para ingresar números de teléfono.
	public JTextField txt_email; // Campo de texto para ingresar direcciones de correo electrónico.
	public JTextField txt_buscar; // Campo de texto adicional.
	public JCheckBox chb_favorito; // Casilla de verificación para marcar un contacto como favorito.
	public JComboBox cmb_categoria; // Menú desplegable para seleccionar la categoría de contacto.
	public JButton btn_add; // Botón para agregar un nuevo contacto.
	public JButton btn_modificar; // Botón para modificar un contacto existente.
	public JButton btn_eliminar; // Botón para eliminar un contacto.
	
	//boton para exportar CSV
	public JButton btn_exportar;

    //barra de carga	
	public JProgressBar barraProgreso;

	
	//Cambios en las tablas
	public JTable tablaContactos;
	public DefaultTableModel modeloTabla;
	public JScrollPane scrTabla;
	
	public logica_ventana lv;
    
	
	//Ventana de estadistica 
	
	public JLabel lbl_totalContactos;
	public JLabel lbl_favoritos;
	public JLabel lbl_familia;
	public JLabel lbl_amigos;
	public JLabel lbl_trabajo;

	//etiquetas
	public JLabel lbl_etiqueta1;
	public JLabel lbl_etiqueta2;
	public JLabel lbl_etiqueta3;
	public JLabel lbl_etiqueta4;
	public JLabel lbl_idioma;

	
	//Idiomas
	
	public JComboBox cmb_idioma;
	
	public ResourceBundle getBundle() { return bundle; }

	
	private ResourceBundle bundle;

	public void cargarIdioma(String codigoIdioma) {
	    Locale locale;
	    switch (codigoIdioma) {
	        case "en": locale = new Locale("en"); break;
	        case "fr": locale = new Locale("fr"); break;
	        default: locale = new Locale("es"); break;
	    }
	    bundle = ResourceBundle.getBundle("recursos.idiomas.mensajes", locale);
	    
	 // Aplicar traducciones
	    setTitle(bundle.getString("titulo.ventana"));


	    chb_favorito.setText(bundle.getString("etiqueta.favorito"));
	   // chb_estadistica.setText(bundle.getString("etiqueta.estadistica"));

	    btn_add.setText(bundle.getString("boton.agregar"));
	    btn_modificar.setText(bundle.getString("boton.modificar"));
	    btn_eliminar.setText(bundle.getString("boton.eliminar"));
	    btn_exportar.setText(bundle.getString("boton.exportar"));
	   // btn_guardar.setText(bundle.getString("boton.guardar"));
	    
	    lbl_totalContactos.setText(bundle.getString("estadistica.total"));
	    lbl_favoritos.setText(bundle.getString("estadistica.favoritos"));
	    lbl_familia.setText(bundle.getString("estadistica.familia"));
	    lbl_amigos.setText(bundle.getString("estadistica.amigos"));
	    lbl_trabajo.setText(bundle.getString("estadistica.trabajo"));

	   
	    lbl_etiqueta1.setText(bundle.getString("etiqueta.nombres"));
	    lbl_etiqueta2.setText(bundle.getString("etiqueta.telefono"));
	    lbl_etiqueta3.setText(bundle.getString("etiqueta.email"));
	    lbl_etiqueta4.setText(bundle.getString("etiqueta.buscar"));
	    
	    lbl_idioma.setText(bundle.getString("etiqueta.idioma"));
	    
	   
	        lv.actualizarEstadisticas(); // refresca las estadísticas con el idioma actual
	    

	}
	
	

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		 // Invoca el método invokeLater de la clase EventQueue para ejecutar la creación de la interfaz de usuario en un hilo de despacho de eventos (Event Dispatch Thread).
	    EventQueue.invokeLater(new Runnable() {
	        public void run() {
	            try {
	                // Dentro de este método, se crea una instancia de la clase ventana, que es la ventana principal de la aplicación.
	                ventana frame = new ventana();
	                // Establece la visibilidad de la ventana como verdadera, lo que hace que la ventana sea visible para el usuario.
	                frame.setVisible(true);
	            } catch (Exception e) {
	                // En caso de que ocurra una excepción durante la creación o visualización de la ventana, se imprime la traza de la pila de la excepción.
	                e.printStackTrace();
	            }
	        }
	    });
	}

	/**
	 * Create the frame.
	 */
	public ventana() {
		


		setForeground(Color.CYAN);
		setTitle("GESTION DE CONTACTOS"); // Establece el título de la ventana.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Define el comportamiento al cerrar la ventana.
		setResizable(false); // Evita que la ventana sea redimensionable.
		setBounds(100, 100, 1026, 748); // Establece el tamaño y la posición inicial de la ventana.
		contentPane = new JPanel(); // Crea un nuevo panel de contenido.
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)); // Establece un borde vacío alrededor del panel.

		setContentPane(contentPane); // Establece el panel de contenido como el panel principal de la ventana.
		contentPane.setLayout(null); // Configura el diseño del panel como nulo para posicionar manualmente los componentes.
		
		String[] idiomas = {"Español", "Inglés", "Frances"};

		

		
		//Modificacion agregada
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(0, 0, 1020, 720);
		contentPane.add(tabbedPane);

		JPanel panelContactos = new JPanel();
		panelContactos.setBackground(Color.WHITE);
		panelContactos.setLayout(null);
		tabbedPane.addTab("Contactos", panelContactos);

		JPanel panelEstadisticas = new JPanel();
		panelEstadisticas.setBackground(Color.WHITE);
		panelEstadisticas.setLayout(null);
		tabbedPane.addTab("Estadísticas", panelEstadisticas);

		
		// Creación y configuración de etiquetas para los campos de entrada.
		lbl_etiqueta1 = new JLabel("NOMBRES:"); // Etiqueta para nombres.
		lbl_etiqueta1.setBounds(25, 41, 131, 13); // Define la posición y tamaño de la etiqueta.
		lbl_etiqueta1.setFont(new Font("Tahoma", Font.BOLD, 15)); // Configura la fuente de la etiqueta.
		panelContactos.add(lbl_etiqueta1); // Agrega la etiqueta al panel de contenido.
		
		lbl_etiqueta2 = new JLabel("TELEFONO:");
		lbl_etiqueta2.setBounds(25, 80, 131, 13);
		lbl_etiqueta2.setFont(new Font("Tahoma", Font.BOLD, 15));
		panelContactos.add(lbl_etiqueta2);
		
		lbl_etiqueta3 = new JLabel("EMAIL:");
		lbl_etiqueta3.setBounds(25, 122, 131, 13);
		lbl_etiqueta3.setFont(new Font("Tahoma", Font.BOLD, 15));
		panelContactos.add(lbl_etiqueta3);
		
		lbl_etiqueta4 = new JLabel("BUSCAR POR NOMBRE:");
		lbl_etiqueta4.setFont(new Font("Tahoma", Font.BOLD, 15));
		lbl_etiqueta4.setBounds(288, 644, 192, 13);
		panelContactos.add(lbl_etiqueta4);
		
		// Creación y configuración de campos de texto para ingresar nombres, teléfonos y correos electrónicos.
		txt_nombres = new JTextField(); // Campo de texto para nombres.
		txt_nombres.setBounds(166, 28, 384, 31); // Define la posición y tamaño del campo de texto.
		txt_nombres.setFont(new Font("Tahoma", Font.PLAIN, 15)); // Configura la fuente del campo de texto.
		panelContactos.add(txt_nombres); // Agrega el campo de texto al panel de contenido.
		txt_nombres.setColumns(10); // Establece el número de columnas para el campo de texto.
		
		txt_telefono = new JTextField();
		txt_telefono.setBounds(166, 69, 384, 31);
		txt_telefono.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_telefono.setColumns(10);
		panelContactos.add(txt_telefono);
		
		txt_email = new JTextField();
		txt_email.setBounds(166, 110, 384, 31);
		txt_email.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_email.setColumns(10);
		panelContactos.add(txt_email);
		
		txt_buscar = new JTextField();
		txt_buscar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_buscar.setColumns(10);
		txt_buscar.setBounds(490, 635, 500, 31);
		panelContactos.add(txt_buscar);
		
		// Creación y configuración de una casilla de verificación para indicar si un contacto es favorito.
		chb_favorito = new JCheckBox("CONTACTO FAVORITO"); // Casilla de verificación.
		chb_favorito.setBackground(Color.WHITE);
		chb_favorito.setBounds(24, 170, 193, 21); // Define la posición y tamaño de la casilla de verificación.
		chb_favorito.setFont(new Font("Tahoma", Font.PLAIN, 15)); // Configura la fuente de la casilla de verificación.
		panelContactos.add(chb_favorito); // Agrega la casilla de verificación al panel de contenido.

		
		cmb_categoria = new JComboBox(); // Crea un nuevo JComboBox para permitir la selección de categorías.
		cmb_categoria.setBackground(Color.WHITE);
		cmb_categoria.setBounds(300, 167, 251, 31); // Establece la posición y el tamaño del JComboBox en el panel.
		panelContactos.add(cmb_categoria); // Agrega el JComboBox al panel de contenido.

		// Arreglo que contiene las categorías disponibles.
		String[] categorias = {"Elija una Categoria", "Familia", "Amigos", "Trabajo"};
		for (String categoria : categorias) {
		    // Agrega cada categoría al JComboBox.
		    cmb_categoria.addItem(categoria);
		}

		btn_add = new JButton("AGREGAR"); // Crea un nuevo botón con el texto "AGREGAR".
        btn_add.setBackground(new Color(162, 255, 201)); // Verde con transparencia

		btn_add.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseEntered(MouseEvent e) {
		        btn_add.setBackground(new Color(1, 239, 100)); // Verde al pasar el mouse
		        btn_add.setForeground(Color.WHITE); // Texto blanco para contraste
		    }

		    @Override
		    public void mouseExited(MouseEvent e) {
		        btn_add.setBackground(new Color(162, 255, 201)); // Verde con transparencia
		        btn_add.setForeground(Color.BLACK); // Texto negro
		    }
		});

		ImageIcon originalIcon = new ImageIcon("C:\\Users\\Anddy\\Desktop\\Deberes Universidad Salesiana\\Practicas\\EclipseTrabajos\\u1c5_AGC-master\\src\\source\\iconos\\agregar.png");
		Image imagenEscalada = originalIcon.getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH); // Ajusta el tamaño aquí
		btn_add.setIcon(new ImageIcon(imagenEscalada));
		btn_add.setFont(new Font("Tahoma", Font.PLAIN, 15)); // Configura la fuente del botón.
		btn_add.setBounds(683, 54, 181, 65); // Establece la posición y el tamaño del botón en el panel.
		panelContactos.add(btn_add); // Agrega el botón al panel de contenido.
		
		btn_modificar = new JButton("MODIFICAR");
		btn_modificar.setBackground(new Color(213, 255, 253)); // Verde con transparencia

		btn_modificar.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseEntered(MouseEvent e) {
		        btn_modificar.setBackground(new Color(1, 244, 237)); // Verde al pasar el mouse
		        btn_modificar.setForeground(Color.BLACK); // Texto blanco para contraste
		    }

		    @Override
		    public void mouseExited(MouseEvent e) {
				btn_modificar.setBackground(new Color(213, 255, 253)); // Verde con transparencia
		        btn_modificar.setForeground(Color.BLACK); // Texto negro
		    }
		});
		ImageIcon modIcon = new ImageIcon("C:\\Users\\Anddy\\Desktop\\Deberes Universidad Salesiana\\Practicas\\EclipseTrabajos\\u1c5_AGC-master\\src\\source\\iconos\\lapiz.png");
		Image modEscalada = modIcon.getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH); // Ajusta el tamaño aquí
		btn_modificar.setIcon(new ImageIcon(modEscalada));
		btn_modificar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_modificar.setBounds(596, 148, 181, 65);
		panelContactos.add(btn_modificar);
		
		btn_eliminar = new JButton("ELIMINAR");
		btn_eliminar.setBackground(new Color(250, 118, 118)); // Verde con transparencia

		btn_eliminar.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseEntered(MouseEvent e) {
		        btn_eliminar.setBackground(new Color(242, 4, 4)); // Verde al pasar el mouse
		        btn_eliminar.setForeground(Color.WHITE); // Texto blanco para contraste
		    }

		    @Override
		    public void mouseExited(MouseEvent e) {
				btn_eliminar.setBackground(new Color(250, 118, 118)); // Verde con transparencia
		        btn_eliminar.setForeground(Color.BLACK); // Texto negro
		    }
		});
		ImageIcon deletIcon = new ImageIcon("C:\\Users\\Anddy\\Desktop\\Deberes Universidad Salesiana\\Practicas\\EclipseTrabajos\\u1c5_AGC-master\\src\\source\\iconos\\borrar.png");
		Image deletEscalada = deletIcon.getImage().getScaledInstance(36, 36, Image.SCALE_SMOOTH); // Ajusta el tamaño aquí
		btn_eliminar.setIcon(new ImageIcon(deletEscalada));
		btn_eliminar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_eliminar.setBounds(801, 148, 181, 65);
		panelContactos.add(btn_eliminar);
		
		//agregamos el boton exportar
		btn_exportar = new JButton("DESCARGAR CSV");
		btn_exportar.setBackground(new Color(251, 254, 162)); // Verde con transparencia

		btn_exportar.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseEntered(MouseEvent e) {
		        btn_exportar.setBackground(new Color(250, 254, 3)); // Verde al pasar el mouse
		        btn_exportar.setForeground(Color.BLACK); // Texto blanco para contraste
		    }

		    @Override
		    public void mouseExited(MouseEvent e) {
				btn_exportar.setBackground(new Color(251, 254, 162)); // Verde con transparencia
		        btn_exportar.setForeground(Color.BLACK); // Texto negro
		    }
		});
		ImageIcon exIcon = new ImageIcon("C:\\Users\\Anddy\\Desktop\\Deberes Universidad Salesiana\\Practicas\\EclipseTrabajos\\u1c5_AGC-master\\src\\source\\iconos\\descargar.png");
		Image exEscalada = exIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH); // Ajusta el tamaño aquí
		btn_exportar.setIcon(new ImageIcon(exEscalada));
		btn_exportar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_exportar.setBounds(25, 627, 192, 47);
		panelContactos.add(btn_exportar);
		
		//barra de progreso
		barraProgreso = new JProgressBar();
		barraProgreso.setBackground(Color.WHITE);
		barraProgreso.setBounds(25, 225, 400, 25);
		barraProgreso.setStringPainted(true); // Muestra el porcentaje
		panelContactos.add(barraProgreso);


		
		
		//Actualizar codigo de tablas
		String[] columnas = {"Nombre", "Teléfono", "Email", "Categoría", "Favorito"};
		modeloTabla = new DefaultTableModel(columnas, 0);
		tablaContactos = new JTable(modeloTabla);
		tablaContactos.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tablaContactos.setRowSorter(new TableRowSorter<>(modeloTabla));

		scrTabla = new JScrollPane(tablaContactos);
		scrTabla.setViewportBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrTabla.setBounds(25, 271, 971, 346);
		panelContactos.add(scrTabla);
		cmb_idioma = new JComboBox(idiomas);
		cmb_idioma.setBounds(855, 6, 150, 25);
		panelContactos.add(cmb_idioma);
		
		lbl_idioma = new JLabel("Cambiar Idioma:");
		lbl_idioma.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbl_idioma.setBounds(727, 6, 131, 25);
		panelContactos.add(lbl_idioma);
        
		cmb_idioma.addActionListener(e -> {
			String seleccionado = cmb_idioma.getSelectedItem().toString();
		    switch (seleccionado) {
		        case "Inglés": cargarIdioma("en"); break;
		        case "Frances": cargarIdioma("fr"); break;
		        default: cargarIdioma("es"); break;
		    }
		});
		
		
		//Constructos de la parte de estadistica
		
		lbl_totalContactos = new JLabel("Total de contactos: 0");
		lbl_totalContactos.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_totalContactos.setIcon(new ImageIcon("C:\\Users\\Anddy\\Desktop\\Deberes Universidad Salesiana\\Practicas\\EclipseTrabajos\\u1c5_AGC-master\\src\\source\\iconos\\trabajo-en-equipo.png"));
		lbl_totalContactos.setFont(new Font("Tahoma", Font.BOLD, 30));
		lbl_totalContactos.setBounds(238, 77, 524, 46);
		panelEstadisticas.add(lbl_totalContactos);

		lbl_favoritos = new JLabel("Contactos favoritos: 0");
		lbl_favoritos.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_favoritos.setIcon(new ImageIcon("C:\\Users\\Anddy\\Desktop\\Deberes Universidad Salesiana\\Practicas\\EclipseTrabajos\\u1c5_AGC-master\\src\\source\\iconos\\estrella.png"));
		lbl_favoritos.setFont(new Font("Tahoma", Font.BOLD, 30));
		lbl_favoritos.setBounds(297, 221, 411, 46);
		panelEstadisticas.add(lbl_favoritos);

		lbl_familia = new JLabel("Familia: 0");
		lbl_familia.setIcon(new ImageIcon("C:\\Users\\Anddy\\Desktop\\Deberes Universidad Salesiana\\Practicas\\EclipseTrabajos\\u1c5_AGC-master\\src\\source\\iconos\\familia.png"));
		lbl_familia.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lbl_familia.setBounds(110, 443, 186, 30);
		panelEstadisticas.add(lbl_familia);

		lbl_amigos = new JLabel("Amigos: 0");
		lbl_amigos.setIcon(new ImageIcon("C:\\Users\\Anddy\\Desktop\\Deberes Universidad Salesiana\\Practicas\\EclipseTrabajos\\u1c5_AGC-master\\src\\source\\iconos\\hacer-amigos.png"));
		lbl_amigos.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lbl_amigos.setBounds(434, 387, 186, 30);
		panelEstadisticas.add(lbl_amigos);

		lbl_trabajo = new JLabel("Trabajo: 0");
		lbl_trabajo.setIcon(new ImageIcon("C:\\Users\\Anddy\\Desktop\\Deberes Universidad Salesiana\\Practicas\\EclipseTrabajos\\u1c5_AGC-master\\src\\source\\iconos\\empresario.png"));
		lbl_trabajo.setFont(new Font("Tahoma", Font.PLAIN, 25));
		lbl_trabajo.setBounds(754, 443, 179, 30);
		panelEstadisticas.add(lbl_trabajo);

		lv = new logica_ventana(this);
		
		

	}
	
}
