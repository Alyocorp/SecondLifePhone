package fr.artemis.phone.utils;

import android.content.Context;
import android.util.Log;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Classe utilitaire de stockage de données
 */
public class StorageUtil {

	/**
	 * Ecriture d'un objet en mémoire
	 *
	 * @param context
	 *            Le contexte applicatif
	 * @param key
	 *            La clé permettant d'identifier l'objet stocké
	 * @param object
	 *            L'objet à stocker
	 * @throws IOException
	 */
	public static void writeObjectInternal( Context context, String key, Object object ) throws IOException {
		FileOutputStream fos = context.openFileOutput( key, Context.MODE_PRIVATE );
		ObjectOutputStream oos = new ObjectOutputStream( fos );
		oos.writeObject( object );
		oos.close();
		fos.close();
	}

	/**
	 * Lecture d'un objet stocké en mémoire
	 *
	 * @param context
	 *            Le contexte applicatif
	 * @param key
	 *            La clé identifiant l'objet à lire
	 * @return L'objet en mémoire
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object readObjectInternal( Context context, String key ) throws IOException, ClassNotFoundException {
		try {
			FileInputStream fis = context.openFileInput( key );
			ObjectInputStream ois = new ObjectInputStream( fis );
			return ois.readObject();
		} catch ( FileNotFoundException ex ) {
			return null;
		}
	}

	/**
	 * Ecriture d'un DTO dans un fichier en format JSON
	 * 
	 * @param dtoToSave
	 *            Le DTO à ecrire dans le fichier
	 * @param absoluteFilePath
	 *            Le fichier dans lequel on écrit le DTO
	 */
	public static void saveDtoIntoFileInJSONFormat( Serializable dtoToSave, String absoluteFilePath ) {

		String dtoJson = new Gson().toJson( dtoToSave );

		FileOutputStream fos = null;
		try {
			// Ecriture du JSON dans le fichier
			File file = new File( absoluteFilePath );
			fos = new FileOutputStream( file );
			Log.d( "SAVE", dtoJson );
			fos.write( dtoJson.getBytes() );
			fos.flush();
			fos.close();
		} catch ( Exception ex ) {
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException( ex );
		} finally {
			if ( null != fos ) {
				try {
					fos.close();
				} catch ( IOException ex ) {
					FirebaseCrashlytics.getInstance().recordException( ex );
					// Impossible de fermer le fichier....
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * Lecture d'un fichier JSON
	 * 
	 * @param fileName
	 *            Le nom du fichier
	 * @param clazz
	 *            La classe attendue en retour
	 * @param <T>
	 *            La classe attendue en retour
	 * @return
	 */
	public static <T> T readJSONFile( String fileName, Class<T> clazz ) {
		try {
			FileReader fileReader = new FileReader( fileName );
			JsonReader jsonReader = new JsonReader( ( fileReader ) );
			T ret = new Gson().fromJson( jsonReader, clazz );
			jsonReader.close();
			fileReader.close();
			return ret;

		} catch ( Exception ex ) {
			throw new IllegalStateException( "Fichier introuvable" );
		}
	}
}