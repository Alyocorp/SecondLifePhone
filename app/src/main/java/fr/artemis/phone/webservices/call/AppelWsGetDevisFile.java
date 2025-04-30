package fr.artemis.phone.webservices.call;

import android.util.Log;

import com.thegrizzlylabs.sardineandroid.DavResource;
import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import fr.artemis.phone.utils.Constantes;
import fr.artemis.phone.utils.DateUtils;
import fr.artemis.phone.utils.SessionPhone;
import fr.artemis.phone.webservices.AbstractCaller;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;

/**
 * Récupération du fichier du devis
 */
public class AppelWsGetDevisFile extends AbstractCaller<String> {

	// Le nom du fichier
	private String fileName;

	/**
	 * Constructeur obligatoire
	 *
	 * @param source       L'appelant de la tâche asynchrone qui attend le résultat
	 * @param mapResources Map contenant des resources nécessaires aux futurs traitements de l'appelant
	 * @param name         Le nom de la tache asynchrone (permet d'identifier qui répond lorsques plusieurs taches sont lancées en meme temps)
	 */
	public AppelWsGetDevisFile( WsCaller source, Map<String, Object> mapResources, WsName name ) {
		super( source, mapResources, name );
	}

	/**
	 * Injection du nom du fichier
	 *
	 * @param fileName le nom du fichier
	 */
	public void setFileName( String fileName ) {
		this.fileName = fileName;
	}

	@Override
	protected String executionRequete() {
		StringBuilder sbWebDavUrlStart = new StringBuilder();
		StringBuilder sbWebDavUrlDirectory = new StringBuilder();

		if ( SessionPhone.getInstance().isArtemisConnection() ) {
			sbWebDavUrlStart.append( Constantes.NAS_PROTOCOL ).append( Constantes.NAS_IP_LAN ).append( ":" ).append( Constantes.NAS_PORT_WAN );
		} else {
			sbWebDavUrlStart.append( Constantes.NAS_PROTOCOL ).append( Constantes.NAS_IP_WAN ).append( ":" ).append( Constantes.NAS_PORT_WAN );
		}
		sbWebDavUrlDirectory.append( Constantes.NAS_DIRECTORY_DEVIS );

		// Connexion WEBDAV
		Sardine sardine = new OkHttpSardine();
		sardine.setCredentials( Constantes.NAS_USER, Constantes.NAS_PASS, true );
		downloadFile( sardine, sbWebDavUrlStart.toString() + sbWebDavUrlDirectory.toString() + fileName, fileName );

		return fileName;
	}

	/**
	 * Téléchargement du fichier recherché sur le NAS
	 *
	 * @param sardine La connexion WEBDAV
	 * @param path    Le fichier à télécharger
	 */
	private void downloadFile( Sardine sardine, String path, String fileName ) {
		try {
			InputStream in = sardine.get( path );

			File file = new File( SessionPhone.getInstance().getFilePath() + fileName );
			OutputStream out = new FileOutputStream( file );
			byte[] data = new byte[1024];
			int byteContent;
			while ( ( byteContent = in.read( data, 0, 1024 ) ) != -1 ) {
				out.write( data, 0, byteContent );
			}
			in.close();
			out.close();
		} catch ( IOException ex ) {
			ex.printStackTrace();
			Log.d( "ERROR", "Erreur lors de la lecture du fichier" );
		}
	}
}