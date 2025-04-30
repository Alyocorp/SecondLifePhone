package fr.artemis.phone.webservices.call;

import java.util.Map;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import android.util.Log;

import fr.artemis.phone.dto.CRMPhoneEquipementSalarieDTO;
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

/**
 * Sauvegarde de la demande d'equipement d'un salarié
 */
public class AppelWsSaveEquipementSalarie extends AbstractCaller<Void> {

	// L'equipement du salarié à sauvegarder
	private CRMPhoneEquipementSalarieDTO equipementSalarie;

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
	public AppelWsSaveEquipementSalarie( WsCaller source, Map<String, Object> mapResources, WsName name ) {
		super( source, mapResources, name );
	}

	/**
	 * Injection de l'equipement de salarié à sauvegarder
	 *
	 * @param equipementSalarie
	 *            L'equipement du salarié
	 */
	public void setEquipementSalarie( CRMPhoneEquipementSalarieDTO equipementSalarie ) {
		this.equipementSalarie = equipementSalarie;
	}

	@Override
	protected Void executionRequete() {

		SoapObject requete = new SoapObject( ConstantesWS.NAMESPACE, ConstantesWS.METHOD_SAVE_EQUIPEMENT );
		requete.addProperty( ConstantesWS.PARAM_WS_EQUIPEMENT, equipementSalarie );

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
			String content = parseContent( equipementSalarie );

			http_transport.callPropre( "", enveloppe, content );

			return null;
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
	 * @param equipementSalarie
	 *            L'equipement a transformer en enveloppe SOAP
	 * @return La chaine de caractere correspondant à l'envelopep SOAP
	 */
	private String parseContent( CRMPhoneEquipementSalarieDTO equipementSalarie ) {

		StringBuilder sbContent = new StringBuilder();

		sbContent.append(
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservices.portail.artemis.fr/\">\n"
						+ "   <soapenv:Header/>\n" + "   <soapenv:Body>\n" + "      <web:saveEquipementSalarie>" + "           <equipementSalarie>" );

		if ( null != equipementSalarie.getQuantiteDemandee() ) {
			sbContent.append( "<quantiteDemandee>" ).append( equipementSalarie.getQuantiteDemandee() ).append( "</quantiteDemandee>" );
		}
		if ( null != equipementSalarie.getQuantitePossedee() ) {
			sbContent.append( "<quantitePossedee>" ).append( equipementSalarie.getQuantitePossedee() ).append( "</quantitePossedee>" );
		}
		if ( null != equipementSalarie.getDemandeCloturee() && equipementSalarie.getDemandeCloturee() ) {
			sbContent.append( "<demandeCloturee>" ).append( "true" ).append( "</demandeCloturee>" );
		} else {
			sbContent.append( "<demandeCloturee>" ).append( "false" ).append( "</demandeCloturee>" );
		}

		if ( null != equipementSalarie.getEquipement() ) {
			sbContent.append( "<equipement>" );

			if ( null != equipementSalarie.getEquipement().getTailleType() ) {
				sbContent.append( "<tailleType>" );
				sbContent.append( "<tailleType>" ).append( equipementSalarie.getEquipement().getTailleType().getTailleType() ).append( "</tailleType>" );
				sbContent.append( "<id>" ).append( equipementSalarie.getEquipement().getTailleType().getId() ).append( "</id>" );

				if ( null != equipementSalarie.getEquipement().getTailleType().getListeTailles() && !equipementSalarie.getEquipement().getTailleType().getListeTailles().isEmpty() ) {
					for ( CRMPhoneEquipementTailleDTO taille : equipementSalarie.getEquipement().getTailleType().getListeTailles() ) {
						sbContent.append( "<listeTailles>" );
						sbContent.append( "<id>" ).append( taille.getId() ).append( "</id>" );
						sbContent.append( "<fkTypeTaille>" ).append( taille.getFkTypeTaille() ).append( "</fkTypeTaille>" );
						sbContent.append( "<taille>" ).append( taille.getTaille() ).append( "</taille>" );
						sbContent.append( "</listeTailles>" );
					}
				}

				sbContent.append( "</tailleType>" );
			}
			if ( null != equipementSalarie.getEquipement().getEquipement() ) {
				sbContent.append( "<equipement>" ).append( equipementSalarie.getEquipement().getEquipement() ).append( "</equipement>" );
			}
			if ( null != equipementSalarie.getEquipement().getId() ) {
				sbContent.append( "<id>" ).append( equipementSalarie.getEquipement().getId() ).append( "</id>" );
			}
			sbContent.append( "</equipement>" );
		}

		if ( null != equipementSalarie.getTaille() ) {
			sbContent.append( "<taille>" );

			sbContent.append( "<id>" ).append( equipementSalarie.getTaille().getId() ).append( "</id>" );
			sbContent.append( "<fkTypeTaille>" ).append( equipementSalarie.getTaille().getFkTypeTaille() ).append( "</fkTypeTaille>" );
			sbContent.append( "<taille>" ).append( equipementSalarie.getTaille().getTaille() ).append( "</taille>" );
			sbContent.append( "</taille>" );
		}

		sbContent.append( "<id>" ).append( equipementSalarie.getId() ).append( "</id>" );

		sbContent.append( " </equipementSalarie>\n" + "      </web:saveEquipementSalarie>\n" + "   </soapenv:Body>\n" + "</soapenv:Envelope>" );

		return sbContent.toString();
	}
}