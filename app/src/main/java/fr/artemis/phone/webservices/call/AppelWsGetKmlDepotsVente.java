package fr.artemis.phone.webservices.call;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import android.util.Base64;
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
 * Tache de recuperation du flux KML des depots vente
 */
public class AppelWsGetKmlDepotsVente extends AbstractCaller<String> {

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
	public AppelWsGetKmlDepotsVente( WsCaller source, Map<String, Object> mapResources, WsName name ) {
		super( source, mapResources, name );
	}

	@Override
	protected String executionRequete() {

		// Injection des propriétés nécessaires à l'authentification
		SoapObject requete = new SoapObject( ConstantesWS.NAMESPACE, ConstantesWS.METHOD_GET_KML_DEPOTS_VENTE );

		// Création de l'enveloppe
		SoapSerializationEnvelope enveloppe = new SoapSerializationEnvelope( SoapEnvelope.VER11 );

		// Ajout de la requête dans l'enveloppe
		enveloppe.setOutputSoapObject( requete );

		// Envoi de la requête et traitement du résultat
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
				byte[] byteValue = Base64.decode( reponse.toString().getBytes( StandardCharsets.UTF_8 ), Base64.DEFAULT );
				return new String( byteValue, StandardCharsets.UTF_8 );
			}
		} catch ( Exception ex ) {
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException( ex );
			Log.e( "BUG", "Erreur lors de la recuperation des depots vente : ", ex );
		}
		return null;
	}
}