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
 * Récupération du fichier du planning
 */
public class AppelWsGetPlanningFile extends AbstractCaller<String> {

	// La date
	private LocalDate date;

	/**
	 * Constructeur obligatoire
	 *
	 * @param source       L'appelant de la tâche asynchrone qui attend le résultat
	 * @param mapResources Map contenant des resources nécessaires aux futurs traitements de l'appelant
	 * @param name         Le nom de la tache asynchrone (permet d'identifier qui répond lorsques plusieurs taches sont lancées en meme temps)
	 */
	public AppelWsGetPlanningFile( WsCaller source, Map<String, Object> mapResources, WsName name ) {
		super( source, mapResources, name );
	}

	/**
	 * Injection de la date
	 *
	 * @param date La date
	 */
	public void setDate( LocalDate date ) {
		this.date = date;
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
		sbWebDavUrlDirectory.append( Constantes.NAS_DIRECTORY_PLANNING );

		// Connexion WEBDAV
		Sardine sardine = new OkHttpSardine();
		sardine.setCredentials( Constantes.NAS_USER, Constantes.NAS_PASS, true );

		try {
			String strToFind = "Semaine-" + date.getYear() + "-" + DateUtils.getWeekNumber( date ) + "-";

			List<DavResource> resources = sardine.list( sbWebDavUrlStart.toString() + sbWebDavUrlDirectory.toString() );

			List<DavResource> listePlanningsDeLaSemaine = new ArrayList<>();
			for ( DavResource resource : resources ) {
				if ( resource.getName().startsWith( strToFind ) ) {
					listePlanningsDeLaSemaine.add( resource );
				}
			}

			Collections.sort( listePlanningsDeLaSemaine, ( p1, p2 ) -> p2.getModified().compareTo( p1.getModified() ) );

			// Téléchargement du fichier dans le cache Android
			if ( !listePlanningsDeLaSemaine.isEmpty() ) {
				downloadFile( sardine, sbWebDavUrlStart.append( listePlanningsDeLaSemaine.get( 0 ).getPath() ).toString(), listePlanningsDeLaSemaine.get( 0 ).getName() );
				return listePlanningsDeLaSemaine.get( 0 ).getName();
			}
		} catch ( IOException ex ) {
			ex.printStackTrace();
			Log.d( "ERROR", "Erreur lors de la lecture du fichier" );
		}
		return null;
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