package modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Definición de la clase pública "personaDAO"
public class personaDAO {
	
	// Declaración de atributos privados de la clase "personaDAO"
	private File archivo; // Archivo donde se almacenarán los datos de los contactos
	private persona persona; // Objeto "persona" que se gestionará
	
	
	//lock estatico
    private static final Object FILE_LOCK = new Object();
	
	// Constructor público de la clase "personaDAO" que recibe un objeto "persona" como parámetro
	public personaDAO(persona persona) {
		this.persona = persona; // Asigna el objeto "persona" recibido al atributo de la clase
		archivo = new File("c:/gestionContactos"); // Establece la ruta donde se alojará el archivo
		// Llama al método para preparar el archivo
		prepararArchivo();
	}
	
	// Método privado para gestionar el archivo utilizando la clase File
	private void prepararArchivo() {
		// Verifica si el directorio existe
		if (!archivo.exists()) { // Si el directorio no existe, se crea
			archivo.mkdir();
		}
		
		// Accede al archivo "datosContactos.csv" dentro del directorio especificado
		archivo = new File(archivo.getAbsolutePath(), "datosContactos.csv");
		// Verifica si el archivo existe
		if (!archivo.exists()) { // Si el archivo no existe, se crea
			try {
				archivo.createNewFile();
				//Prepara el encabezado para el archivo de csv
			} catch (IOException e) {
				// Maneja la excepción de entrada/salida
				e.printStackTrace();
			}
		}
	}
	private void escribir(String texto){
		// Prepara el archivo para escribir en la última línea
		FileWriter escribir;
		try {
			escribir = new FileWriter(archivo.getAbsolutePath(), true);
			escribir.write(texto + "\n"); // Escribe los datos del contacto en el archivo
			// Cierra el archivo
			escribir.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	// Método público para escribir en el archivo
	 public void escribirArchivo() throws IOException {
	        if (persona != null && persona.getNombre() != null && !persona.getNombre().isEmpty()) {
	            synchronized (FILE_LOCK) {
	                try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, true))) {
	                    String linea = persona.getNombre() + ";" +
	                                   persona.getTelefono() + ";" +
	                                   persona.getEmail() + ";" +
	                                   persona.getCategoria() + ";" +
	                                   persona.isFavorito();
	                    writer.write(linea);
	                    writer.newLine();
	                }
	            }
	        }
	    }

	
	// Método público para leer los datos del archivo
	public List<persona> leerArchivo() throws IOException {
	    List<persona> lista = new ArrayList<>();

	    if (!archivo.exists()) {
	        return lista; // Si el archivo no existe, devuelve lista vacía
	    }

	    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
	        String linea;
	        while ((linea = br.readLine()) != null) {
	            String[] datos = linea.split(";");
	            if (datos.length >= 4) {
	                persona p = new persona();
	                p.setNombre(datos[0]);
	                p.setTelefono(datos[1]);
	                p.setEmail(datos[2]);
	                p.setCategoria(datos[3]);

	                // ✅ Maneja el valor de favorito si existe en la línea
	                if (datos.length > 4) {
	                    p.setFavorito(Boolean.parseBoolean(datos[4]));
	                } else {
	                    p.setFavorito(false); // valor por defecto si el archivo es antiguo
	                }

	                lista.add(p);
	            }
	        }
	    }

	    return lista;
	}

	
	// Método público para guardar los contactos modificados o eliminados
	public void actualizarContactos(List<persona> personas) throws IOException {
        synchronized (FILE_LOCK) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, false))) {
                for (persona p : personas) {
                    String linea = p.getNombre() + ";" +
                                   p.getTelefono() + ";" +
                                   p.getEmail() + ";" +
                                   p.getCategoria() + ";" +
                                   p.isFavorito();
                    writer.write(linea);
                    writer.newLine();
                }
            }
        }
    }
}


