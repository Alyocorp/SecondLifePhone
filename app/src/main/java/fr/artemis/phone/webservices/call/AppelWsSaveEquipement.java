package fr.artemis.phone.webservices.call;

import java.util.Map;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import android.util.Log;

import fr.artemis.phone.dto.CRMPhoneEquipementDTO;
import fr.artemis.phone.dto.CRMPhoneEquipementTailleDTO;
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
import fr.artemis.phone.webservices.mapper.impl.EquipementMapper;

/**
 * Tache d'enregistrement d'un equipement
 */
public class AppelWsSaveEquipement extends AbstractCaller<CRMPhoneEquipementDTO> {

	// L'equipement
	private CRMPhoneEquipementDTO equipement;

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
	public AppelWsSaveEquipement( WsCaller source, Map<String, Object> mapResources, WsName name ) {
		super( source, mapResources, name );
	}

	/**
	 * Injection de l'equipement à sauvegarder
	 *
	 * @param equipement
	 *            L'equipement
	 */
	public void setEquipement( CRMPhoneEquipementDTO equipement ) {
		this.equipement = equipement;
	}

	@Override
	protected CRMPhoneEquipementDTO executionRequete() {

		SoapObject requete = new SoapObject( ConstantesWS.NAMESPACE, ConstantesWS.METHOD_SAVE_EQUIPEMENT );
		requete.addProperty( ConstantesWS.PARAM_WS_EQUIPEMENT, equipement );

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
			String content = parseContent( equipement );

			http_transport.callPropre( "", enveloppe, content );
			SoapObject reponse = (SoapObject) enveloppe.getResponse();

			return new EquipementMapper().map( reponse );

		} catch ( Exception ex ) {
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException( ex );
			Log.e( "BUG", "Erreur lors de l'enregistrement d'un equipement : ", ex );
		}
		return null;
	}

	/**
	 * Creation de la requete SOAP de sauvegarde
	 *
	 * @param equipement
	 *            L'equipement a transformer en enveloppe SOAP
	 * @return La chaine de caractere correspondant à l'envelopep SOAP
	 */
	private String parseContent( CRMPhoneEquipementDTO equipement ) {

		StringBuilder sbContent = new StringBuilder();

		sbContent.append(
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservices.portail.artemis.fr/\">\n"
						+ "   <soapenv:Header/>\n" + "   <soapenv:Body>\n" + "      <web:saveEquipement>" + "           <equipement>" );

		if ( null != equipement.getTailleType() ) {
			sbContent.append( "<tailleType>" );
			sbContent.append( "<tailleType>" ).append( equipement.getTailleType().getTailleType() ).append( "</tailleType>" );
			sbContent.append( "<id>" ).append( equipement.getTailleType().getId() ).append( "</id>" );

			if ( null != equipement.getTailleType().getListeTailles() && !equipement.getTailleType().getListeTailles().isEmpty() ) {
				for ( CRMPhoneEquipementTailleDTO taille : equipement.getTailleType().getListeTailles() ) {
					sbContent.append( "<listeTailles>" );
					sbContent.append( "<id>" ).append( taille.getId() ).append( "</id>" );
					sbContent.append( "<fkTypeTaille>" ).append( taille.getFkTypeTaille() ).append( "</fkTypeTaille>" );
					sbContent.append( "<taille>" ).append( taille.getTaille() ).append( "</taille>" );
					sbContent.append( "</listeTailles>" );
				}
			}

			sbContent.append( "</tailleType>" );
		}
		if ( null != equipement.getEquipement() ) {
			sbContent.append( "<equipement>" ).append( equipement.getEquipement() ).append( "</equipement>" );
		}
		if ( null != equipement.getId() ) {
			sbContent.append( "<id>" ).append( equipement.getId() ).append( "</id>" );
		}

		sbContent.append( "         </equipement>\n" + "      </web:saveEquipement>\n" + "   </soapenv:Body>\n" + "</soapenv:Envelope>" );

		return sbContent.toString();
	}
}