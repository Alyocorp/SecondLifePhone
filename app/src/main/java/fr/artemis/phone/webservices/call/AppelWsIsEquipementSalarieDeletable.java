package fr.artemis.phone.webservices.call;

import java.util.Map;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import android.util.Log;

import fr.artemis.phone.lib.ksoap.ksoap2.SoapEnvelope;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapPrimitive;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapSerializationEnvelope;
import fr.artemis.phone.lib.ksoap.ksoap2.transport.HttpTransportSE;
import fr.artemis.phone.utils.ConstantesWS;
import fr.artemis.phone.utils.PhoneUtil;
import fr.artemis.phone.utils.SessionPhone;
import fr.artemis.phone.webservices.AbstractCaller;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;

/**
 * Tache de verification si une demande d'equipement de salarié est supprimable
 */
public class AppelWsIsEquipementSalarieDeletable extends AbstractCaller<Boolean> {

	// L'identifiant de la demande d'equipement
	private int idEquipementSalarie;

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
	public AppelWsIsEquipementSalarieDeletable( WsCaller source, Map<String, Object> mapResources, WsName name ) {
		super( source, mapResources, name );
	}

	/**
	 * Injection de l'identifiant de la demande d'equipement
	 *
	 * @param idEquipementSalarie
	 *            L'identifiant de la demande d'equipement de salarié
	 */
	public void setIdEquipementSalarie( int idEquipementSalarie ) {
		this.idEquipementSalarie = idEquipementSalarie;
	}

	@Override
	protected Boolean executionRequete() {

		// Injection des propriétés nécessaires à l'authentification
		SoapObject requete = new SoapObject( ConstantesWS.NAMESPACE, ConstantesWS.METHOD_IS_EQUIPEMENT_SALARIE_DELETABLE );
		requete.addProperty( ConstantesWS.PARAM_WS_ID_EQUIPEMENT_SALARIE, idEquipementSalarie );

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
			http_transport.callAuthenticated( "", enveloppe );
			SoapPrimitive reponse = (SoapPrimitive) enveloppe.getResponse();

			if ( null != reponse ) {
				return Boolean.valueOf( reponse.toString() );
			}

		} catch ( Exception ex ) {
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException( ex );
			Log.e( "BUG", "Erreur lors du test si un equipement est supprimable : ", ex );
		}
		return null;
	}
}