package fr.artemis.phone.webservices.call;

import android.util.Log;

import java.util.Map;

import fr.artemis.phone.dto.CRMPhoneInterventionDetailsDTO;
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
import fr.artemis.phone.webservices.mapper.impl.InterventionDetailsMapper;

/**
 * Tache de recuperation du détail d'une intervention
 */
public class AppelWsGetInterventionDetails extends AbstractCaller<CRMPhoneInterventionDetailsDTO> {

	// L'identifiant du devis
	private Integer idDevis;

	/**
	 * Constructeur obligatoire
	 *
	 * @param source       L'appelant de la tâche asynchrone qui attend le résultat
	 * @param mapResources Map contenant des resources nécessaires aux futurs traitements de l'appelant
	 * @param name         Le nom de la tache asynchrone (permet d'identifier qui répond lorsques plusieurs taches sont lancées en meme temps)
	 */
	public AppelWsGetInterventionDetails( WsCaller source, Map<String, Object> mapResources, WsName name ) {
		super( source, mapResources, name );
	}

	/**
	 * Injection de l'identifiant du devis
	 *
	 * @param idDevis L'identifiant du devis
	 */
	public void setIdDevis( Integer idDevis ) {
		this.idDevis = idDevis;
	}

	@Override
	protected CRMPhoneInterventionDetailsDTO executionRequete() {

		// Injection des propriétés nécessaires à l'authentification
		SoapObject requete = new SoapObject( ConstantesWS.NAMESPACE, ConstantesWS.METHOD_INTER_DETAILS );
		requete.addProperty( ConstantesWS.PARAM_WS_ID_DEVIS, idDevis );

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


			if ( null != reponse ) {

				SoapObject obj = (SoapObject) reponse.getProperty( 0 );

				return new InterventionDetailsMapper().map( obj );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
			Log.e( "Erreur lors de l'envoi de la requête : ", e.getMessage() );
		}
		return null;
	}
}