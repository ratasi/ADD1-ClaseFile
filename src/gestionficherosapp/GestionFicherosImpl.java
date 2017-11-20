package gestionficherosapp;
import java.io.*;
import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;

import gestionficheros.FormatoVistas;
import gestionficheros.GestionFicheros;
import gestionficheros.GestionFicherosException;
import gestionficheros.TipoOrden;

public class GestionFicherosImpl implements GestionFicheros {
	private File carpetaDeTrabajo = null;
	private Object[][] contenido;
	private int filas = 0;
	private int columnas = 3;
	private FormatoVistas formatoVistas = FormatoVistas.NOMBRES;
	private TipoOrden ordenado = TipoOrden.DESORDENADO;

	public GestionFicherosImpl() {
		carpetaDeTrabajo = File.listRoots()[0];
		actualiza();
	}

	private void actualiza() {

		String[] ficheros = carpetaDeTrabajo.list(); // obtener los nombres
		// calcular el número de filas necesario
		filas = ficheros.length / columnas;
		if (filas * columnas < ficheros.length) {
			filas++; // si hay resto necesitamos una fila más
		}

		// dimensionar la matriz contenido según los resultados

		contenido = new String[filas][columnas];
		// Rellenar contenido con los nombres obtenidos
		for (int i = 0; i < columnas; i++) {
			for (int j = 0; j < filas; j++) {
				int ind = j * columnas + i;
				if (ind < ficheros.length) {
					contenido[j][i] = ficheros[ind];
				} else {
					contenido[j][i] = "";
				}
			}
		}
	}

	@Override
	//Esta función hace que podamos subir a la carpeta padre 
	public void arriba() {
		
		//Si nos devuelve null es que no tiene carpeta padre, por lo tanto no se puede actualizar y "volver"
		if (carpetaDeTrabajo.getParentFile() != null) {
			carpetaDeTrabajo = carpetaDeTrabajo.getParentFile();
			actualiza();
		}

	}

	@Override
	public void creaCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo,arg0);
		//que se pueda escribir -> lanzará una excepción
		//que no exista -> lanzará una excepción
		//crear la carpeta -> lanzará una excepción
		
		//Creamos una excepción para que si existe nos 
		//	diga que ya está creada, y si no, crearla
		try {
			if (file.exists()) {
				System.err.println("La carpeta " + file.getName() + " ya existe.");
			}else {
				file.mkdir();
				System.out.println("Carpeta " + file.getName() + " ha sido creada con éxito.");
			}
		}catch(Exception e) {
			//Se utiliza para imprimir el registro del stack donde se ha iniciado la excepción. 
			e.printStackTrace();
		}
				
		actualiza();
	}

	@Override
	public void creaFichero(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		File file = new File(carpetaDeTrabajo,arg0);
		
		try {  
			   if (file.createNewFile()) {  
			    System.out.println("El fichero :  " + file.getName()  
			      + " se ha creado con éxito");  
			   } else {  
			    System.err.println("El fichero :  " + file.getName()
			      + " ya está creado.");  
			   }  
			  } catch (Exception e) {
				  //Se utiliza para imprimir el registro del stack donde se ha iniciado la excepción. 
			   e.printStackTrace();  
			  }  
	}
	
	

	@Override
	public void elimina(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		File file = new File(carpetaDeTrabajo,arg0);
		
		try {
			if (file.delete()) {
			   System.out.println("El fichero ha sido borrado");
			}else {
			   System.out.println("El fichero no puede ser borrado");
			}
		} catch (Exception e) {
			  //Se utiliza para imprimir el registro del stack donde se ha iniciado la excepción. 
			   e.printStackTrace(); 
		}
	}

	@Override
	public void entraA(String arg0) throws GestionFicherosException {
		//Arg0 -> string
		//Le pasamos la carpetadeTrabajo actual y se crea un nuevo path 
			//a partir de la carpetaDeTrabajo mas el nuevo string que es arg0
		File file = new File(carpetaDeTrabajo, arg0);
		// se controla que el nombre corresponda a una carpeta existente
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se ha encontrado "
					+ file.getAbsolutePath()
					+ " pero se esperaba un directorio");
		}
		// se controla que se tengan permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException("Alerta. No se puede acceder a "
					+ file.getAbsolutePath() + ". No hay permiso");
		}
		// nueva asignación de la carpeta de trabajo
			//Actualizamos la carpeta de trabajo
		carpetaDeTrabajo = file;
		// se requiere actualizar contenido
		actualiza();

	}

	@Override
	public int getColumnas() {
		return columnas;
	}

	@Override
	public Object[][] getContenido() {
		return contenido;
	}

	@Override
	public String getDireccionCarpeta() {
		return carpetaDeTrabajo.getAbsolutePath();
	}

	@Override
	public String getEspacioDisponibleCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEspacioTotalCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFilas() {
		return filas;
	}

	@Override
	public FormatoVistas getFormatoContenido() {
		return formatoVistas;
	}

	@Override
	public String getInformacion(String arg0) throws GestionFicherosException {
		
		//La Clase StringBuilder nos ordena el string de forma más eficiente
		StringBuilder strBuilder=new StringBuilder();
		File file = new File(carpetaDeTrabajo,arg0);
		
		//Controlar que existe. Si no, se lanzará una excepción
			//Mediante la llamada al método isDirectory de la clase file controlamos que la ruta exista y sea un directorio
			//en caso contrario lanza una excepción
		if(!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se esperaba un directorio,"
					+ " pero " + file.getAbsolutePath() + "no es un directorio.");
			
		}
		//Controlar que haya permisos de lectura. Si no, se lanzará una excepción
			//La llamada al método canRead de la clase file nos controla que haya acceso de lectura en el fichero
				//sino nos dara una excepción de que no puede entrar
		if(!file.canRead()) {
			throw new GestionFicherosException("Alerta. No se puede acceder "
					+ "a " + file.getAbsolutePath() + ". Porque no tienes permisos");
		}
		
		
		//Título
		strBuilder.append("INFORMACIÓN DEL SISTEMA");
		//\n\n dos saltos de línea
		strBuilder.append("\n\n");
		
		
		//Nombre
		strBuilder.append("Nombre : ");
		strBuilder.append(arg0);
		strBuilder.append("\n");
		
		//Tipo (fichero o directorio)

		strBuilder.append("Tipo : ");
		//Boolean nos devuelve si es true o es false
		//Si isFile es true significa que es un fichero, de lo contrario es directorio
		  boolean isFile = file.isFile();    
			if(isFile) {
		       strBuilder.append(file.getPath() + " es a fichero.");
		     } else {
		    	 strBuilder.append(file.getPath() + " es un directorio.");
		     }
		
		strBuilder.append("\n\n");
				
		//Ubicación
		//getAbsolutePath nos da la ruta absoluta del archivo file que estamos seleccionando
		strBuilder.append("Ubicación : ");
		strBuilder.append(file.getAbsolutePath());
		strBuilder.append("\n\n");
		
		//Fecha de última modificación
		//Gracias a java.util.Date nos ordena la clase lastModified
		strBuilder.append("Última fecha de modificación : ");		
		Date myDate = new java.util.Date(file.lastModified());
		strBuilder.append(myDate);
		strBuilder.append("\n\n");
		
		//Si es un fichero oculto o no
		//Si el fichero es oculto es hidden
		strBuilder.append("Es fichero oculto");
		if(file.isHidden()) {
			strBuilder.append(" El fichero es oculto");
		}else {
				strBuilder.append(" El fichero no es oculto");
			}
		
		strBuilder.append("\n\n");
		
		//Si es directorio: Espacio libre, espacio disponible, espacio total
		//bytes
		strBuilder.append("Espacio libre : ");
		//getFreeSpace-> Espacio libre
		strBuilder.append(file.getFreeSpace() + " bytes");
		strBuilder.append("\n");
		
		strBuilder.append("Espacio disponible : ");
		//getUsableSpace-> Espacio disponible
		strBuilder.append(file.getUsableSpace() + " bytes");
		strBuilder.append("\n");
		
		strBuilder.append("Espacio total : ");
		//getTotalSpace -> Espacio total
		strBuilder.append(file.getTotalSpace() + " bytes");
		strBuilder.append("\n");
		
		return strBuilder.toString();
		
	}

	@Override
	public boolean getMostrarOcultos() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNombreCarpeta() {
		return carpetaDeTrabajo.getName();
	}

	@Override
	public TipoOrden getOrdenado() {
		return ordenado;
	}

	@Override
	public String[] getTituloColumnas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getUltimaModificacion(String arg0)
			throws GestionFicherosException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String nomRaiz(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numRaices() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void renombra(String arg0, String arg1) throws GestionFicherosException {	
			File file = new File(carpetaDeTrabajo,arg0);
			File file2=new File(carpetaDeTrabajo,arg1);
			
			try {  
				   if (file.renameTo(file2)) {
					   			    System.out.println("El fichero :  " + file.getName()  
				      + " se ha modificado el nombre con éxito y ahora se llama : " + file2.getName());  
				   } else {  
				    System.err.println("El fichero :  " + file.getName()
				      + " no se ha modificado.");  
				   }  
				  } catch (Exception e) {
					  //Se utiliza para imprimir el registro del stack donde se ha iniciado la excepción. 
				   e.printStackTrace();  
				  }  
	}


	@Override
	public boolean sePuedeEjecutar(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeEscribir(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeLeer(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setColumnas(int arg0) {
		columnas = arg0;

	}

	@Override
	public void setDirCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(arg0);

		// se controla que la dirección exista y sea directorio
		if (!file.isDirectory()) {
			throw new GestionFicherosException("Error. Se esperaba "
					+ "un directorio, pero " + file.getAbsolutePath()
					+ " no es un directorio.");
		}

		// se controla que haya permisos para leer carpeta
		if (!file.canRead()) {
			throw new GestionFicherosException(
					"Alerta. No se puede acceder a  " + file.getAbsolutePath()
							+ ". No hay permisos");
		}

		// actualizar la carpeta de trabajo
		carpetaDeTrabajo = file;

		// actualizar el contenido
		actualiza();

	}

	@Override
	public void setFormatoContenido(FormatoVistas arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMostrarOcultos(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOrdenado(TipoOrden arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEjecutar(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeEscribir(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSePuedeLeer(String arg0, boolean arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUltimaModificacion(String arg0, long arg1)
			throws GestionFicherosException {
		// TODO Auto-generated method stub

	}

}
