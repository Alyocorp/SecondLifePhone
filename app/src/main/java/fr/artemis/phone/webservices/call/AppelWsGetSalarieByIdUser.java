package fr.artemis.phone.webservices.call;

import java.util.Map;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import android.util.Log;

import fr.artemis.phone.activities.main.MainActivity;
import fr.artemis.phone.dto.SalarieLightDTO;
import fr.artemis.phone.lib.ksoap.ksoap2.SoapEnvelope;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapSerializationEnvelope;
import fr.artemis.phone.lib.ksoap.ksoap2.transport.HttpTransportSE;
import fr.artemis.phone.utils.ConstantesWS;
import fr.artemis.phone.utils.PhoneUtil;
import fr.artemis.phone.utils.SessionPhone;
import fr.artemis.phone.webservices.AbstractCaller;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.mapper.impl.SalarieMapper;

public class AppelWsGetSalarieByIdUser extends AbstractCaller<SalarieLightDTO> {

	// L'identifiant de l'utilisateur
	private int idUser;

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
	public AppelWsGetSalarieByIdUser( WsCaller source, Map<String, Object> mapResources, WsName name ) {
		super( source, mapResources, name );
	}

	/**
	 * Injection de l'identifiant de l'utilisateur
	 *
	 * @param idUser
	 *            L'identifiant de l'utilisateur
	 */
	public void setIdUser( int idUser ) {
		this.idUser = idUser;
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
	protected void onPostExecute( SalarieLightDTO result ) {
		source.notifyResponse( WsName.GET_SALARIE_BY_ID_USER, mapResources, result );
		( (MainActivity) source ).removeWaitingScreen();
	}

	@Override
	protected SalarieLightDTO executionRequete() {

		// Injection des propriétés nécessaires à l'authentification
		SoapObject requete = new SoapObject( ConstantesWS.NAMESPACE, ConstantesWS.METHOD_GET_SALARIE_BY_IDUSER );
		requete.addProperty( ConstantesWS.PARAM_WS_ID_USER, idUser );

		//Création de l'enveloppe
		SoapSerializationEnvelope enveloppe = new SoapSerializationEnvelope( SoapEnvelope.VER11 );

		//Ajout de la requête dans l'enveloppe
		enveloppe.setOutputSoapObject( requete );

		//Envoi de la requête et traitement du résultat
		HttpTransportSE http_transport;
		if ( PhoneUtil.isEmulator() ) {
			http_transport = new HttpTransportSE( ConstantesWS.URL_CRM_PHONE_EMULATOR );
		} else {
			if ( SessionPhone.getInstance().isDevMode() ) {
				http_transport = new HttpTransportSE( ConstantesWS.URL_CRM_PHONE_DEVMODE );
			} else {
				http_transport = new HttpTransportSE( ConstantesWS.URL_CRM_PHONE_SERVER );
			}
		}
		try {
			// Appel WS authentifié par l'identifiant de session utilisateur
			http_transport.callAuthenticated( "", enveloppe );
			SoapObject reponse = (SoapObject) enveloppe.bodyIn;

			if ( null != reponse && reponse.getPropertyCount() > 0 ) {
				return new SalarieMapper().map( (SoapObject) reponse.getProperty( 0 ) );
			}

		} catch ( Exception ex ) {
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException( ex );
			Log.e( "BUG", "Erreur lors de la recuperation des horaires : ", ex );
		}
		return null;
	}
}