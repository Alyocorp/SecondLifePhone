package fr.artemis.phone.webservices.call;

import java.util.Map;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import android.util.Log;

import fr.artemis.phone.activities.main.MainActivity;
import fr.artemis.phone.dto.UtilisateurDTO;
import fr.artemis.phone.lib.ksoap.ksoap2.SoapEnvelope;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapSerializationEnvelope;
import fr.artemis.phone.lib.ksoap.ksoap2.transport.HttpTransportSE;
import fr.artemis.phone.utils.ConstantesWS;
import fr.artemis.phone.utils.SessionPhone;
import fr.artemis.phone.webservices.AbstractCaller;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.mapper.impl.UtilisateurMapper;

/**
 * Classe d'appel au webservice d'authentification
 */
public class AppelWsAuthentication extends AbstractCaller<UtilisateurDTO> {

	// Le nom d'utilisateur
	private String username;

	// Le mot de passe
	private String password;

	/**
	 * Constructeur obligatoire
	 *
	 * @param source
	 *            L'appelant de la tâche asynchrone qui attend le résultat
	 * @param mapResources
	 *            Map contenant des resources nécessaires aux futurs traitements de l'appelant
	 * @param name
	 *            Le nom de la tache asynchrone (permet d'identifier qui répond lorsques plusieurs taches sont lancées en meme temps)
	 */
	public AppelWsAuthentication( WsCaller source, Map<String, Object> mapResources, WsName name ) {
		super( source, mapResources, name );
	}

	/**
	 * Injection du nom d'utilisateur
	 *
	 * @param username
	 *            Le nom d'utilisateur
	 */
	public void setUsername( String username ) {
		this.username = username;
	}

	/**
	 * Injection du mot de passe
	 *
	 * @param password
	 *            Le mot de passe
	 */
	public void setPassword( String password ) {
		this.password = password;
	}

	/**
	 * De base, on traite l'appelant comme un fragment.
	 * Dans ce cas spécifique, c'est l'activité qui appelle cette tache.
	 * On polymorphise cette méthode afin d'eviter un ClassCastException
	 */
	@Override
	protected void onPreExecute() {
		// Affichage d'un ecran de chargement sur l'activité principale
		( (MainActivity) this.source ).showWaitingScreen();
	}

	/**
	 * De base, on traite l'appelant comme un fragment.
	 * Dans ce cas spécifique, c'est l'activité qui appelle cette tache.
	 * On polymorphise cette méthode afin d'eviter un ClassCastException
	 */
	@Override
	protected void onPostExecute( UtilisateurDTO result ) {
		source.notifyResponse( WsName.AUTHENTIFICATION, mapResources, result );
		( (MainActivity) source ).removeWaitingScreen();
	}

	@Override
	protected UtilisateurDTO executionRequete() {

		// Injection des propriétés nécessaires à l'authentification
		SoapObject requete = new SoapObject( ConstantesWS.NAMESPACE, ConstantesWS.METHOD_AUTH );
		requete.addProperty( ConstantesWS.PARAM_WS_USERNAME, username );
		requete.addProperty( ConstantesWS.PARAM_WS_PASSWORD, password );

		//Création de l'enveloppe
		SoapSerializationEnvelope enveloppe = new SoapSerializationEnvelope( SoapEnvelope.VER11 );

		//Ajout de la requête dans l'enveloppe
		enveloppe.setOutputSoapObject( requete );

		//Envoi de la requête et traitement du résultat
		HttpTransportSE http_transport;
		if ( SessionPhone.getInstance().isDevMode() ) {
			http_transport = new HttpTransportSE( ConstantesWS.URL_AUTH_DEV );
		} else {
			http_transport = new HttpTransportSE( ConstantesWS.URL_AUTH );
		}
		try {
			http_transport.call( "", enveloppe );
			SoapObject reponse = (SoapObject) enveloppe.getResponse();

			return new UtilisateurMapper().map( reponse );

		} catch ( Exception ex ) {
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException( ex );
			Log.e( "BUG", "Erreur lors de l'authentification : ", ex );
		}
		return null;
	}
}