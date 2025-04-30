package fr.artemis.phone.webservices.call;

import java.util.List;
import java.util.Map;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import android.util.Log;

import fr.artemis.phone.dto.CRMPhoneHorairesDTO;
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
import fr.artemis.phone.webservices.mapper.impl.HorairesMapper;

/**
 * Récupération de la liste des horaires d'un salarié sur un mois donné
 */
public class AppelWsGetListeHorairesByIdUser extends AbstractCaller<List<CRMPhoneHorairesDTO>> {

	// L'identifiant de l'utilisateur
	private int idUser;

	// L'année
	private int annee;

	// Le mois
	private int mois;

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
	public AppelWsGetListeHorairesByIdUser( WsCaller source, Map<String, Object> mapResources, WsName name ) {
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
	 * Injection de l'année
	 *
	 * @param annee
	 *            L'annnée
	 */
	public void setAnnee( int annee ) {
		this.annee = annee;
	}

	/**
	 * Injection du mois
	 *
	 * @param mois
	 *            Le mois
	 */
	public void setMois( int mois ) {
		this.mois = mois;
	}

	@Override
	protected List<CRMPhoneHorairesDTO> executionRequete() {

		// Injection des propriétés nécessaires à l'authentification
		SoapObject requete = new SoapObject( ConstantesWS.NAMESPACE, ConstantesWS.METHOD_LISTE_HORAIRES_BY_MONTH );
		requete.addProperty( ConstantesWS.PARAM_WS_ID_USER, idUser );
		requete.addProperty( ConstantesWS.PARAM_WS_ANNEE, annee );
		requete.addProperty( ConstantesWS.PARAM_WS_MOIS, mois );

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
				return new HorairesMapper().mapList( reponse );
			}

		} catch ( Exception ex ) {
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException( ex );
			Log.e( "BUG", "Erreur lors de la recuperation des horaires : ", ex );
		}
		return null;
	}
}