package fr.artemis.phone.webservices.call;

import java.util.Map;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import android.util.Log;

import fr.artemis.phone.dto.CRMPhoneDepotsVenteDTO;
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
import fr.artemis.phone.webservices.mapper.impl.DepotVenteMapper;

/**
 * Tache de mise à jour d'un depot vente
 */
public class AppelWsUpdateDepotVente extends AbstractCaller<CRMPhoneDepotsVenteDTO> {

	// Le dépot vente
	private CRMPhoneDepotsVenteDTO depotVente;

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
	public AppelWsUpdateDepotVente( WsCaller source, Map<String, Object> mapResources, WsName name ) {
		super( source, mapResources, name );
	}

	/**
	 * Injection du depot vente à enregistrer
	 *
	 * @param depotVente
	 *            Le depot vente
	 */
	public void setDepotVente( CRMPhoneDepotsVenteDTO depotVente ) {
		this.depotVente = depotVente;
	}

	@Override
	protected CRMPhoneDepotsVenteDTO executionRequete() {

		SoapObject requete = new SoapObject( ConstantesWS.NAMESPACE, ConstantesWS.METHOD_SAVE_DEPOT_VENTE );
		requete.addProperty( ConstantesWS.PARAM_WS_DEPOT_VENTE, depotVente );

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
			SoapObject reponse = (SoapObject) enveloppe.getResponse();

			if ( null != reponse ) {
				return new DepotVenteMapper().map( reponse );
			}

		} catch ( Exception ex ) {
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException( ex );
			Log.e( "BUG", "Erreur lors de l'enregistrement d'un depot vente : ", ex );
		}
		return null;
	}
}