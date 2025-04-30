package fr.artemis.phone.webservices.call;

import java.time.LocalDate;
import java.util.Map;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import android.util.Log;

import fr.artemis.phone.dto.CRMPhoneRapportChantierDTO;
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
import fr.artemis.phone.webservices.mapper.impl.RapportChantierMapper;

public class AppelWsGetRapportDeChantier extends AbstractCaller<CRMPhoneRapportChantierDTO> {

	// La date du rapport de chantier
	private LocalDate dateDuRapport;

	// l'identifiant du devis
	private Integer idDevis;

	/**
	 * Constructeur obligatoire
	 *
	 * @param source
	 *            L'appelant de la tâche asynchrone qui attend le résultat
	 * @param mapResources
	 *            Map contenant des resources nécessaires aux futurs
	 *            traitements de l'appelant
	 * @param name
	 *            Le nom de la tache asynchrone (permet d'identifier qui répond
	 *            lorsques plusieurs taches sont lancées en meme temps)
	 */
	public AppelWsGetRapportDeChantier( WsCaller source, Map<String, Object> mapResources, WsName name ) {
		super( source, mapResources, name );
	}

	/**
	 * Injection de la date du rapport de chantier
	 * 
	 * @param dateDuRapport
	 *            La date du rapport de chantier
	 */
	public void setDateDuRapport( LocalDate dateDuRapport ) {
		this.dateDuRapport = dateDuRapport;
	}

	/**
	 * Injection de l'identifiant du devis
	 * 
	 * @param idDevis
	 *            L'identifiant du devis
	 */
	public void setIdDevis( Integer idDevis ) {
		this.idDevis = idDevis;
	}

	@Override
	protected CRMPhoneRapportChantierDTO executionRequete() {

		// Injection des propriétés nécessaires à l'authentification
		SoapObject requete = new SoapObject( ConstantesWS.NAMESPACE, ConstantesWS.METHOD_GET_RAPPORT_CHANTIER );

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
			// Appel WS authentifié par l'identifiant de session utilisateur
			String content = parseContent();
			http_transport.callPropre( "", enveloppe, content );
			SoapObject reponse = (SoapObject) enveloppe.bodyIn;

			if ( null != reponse && reponse.getPropertyCount() > 0 ) {
				SoapObject obj = (SoapObject) reponse.getProperty( 0 );
				return new RapportChantierMapper().map( obj );
			}

		} catch ( Exception ex ) {
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException( ex );
			Log.e( "BUG", "Erreur lors de la recuperation du rapport de chantier : ", ex );
		}
		return null;
	}

	private String parseContent() {
		StringBuilder sbContent = new StringBuilder();

		String anneeDebut;
		String moisDebut;
		String jourDebut;

		String date;

		anneeDebut = String.valueOf( dateDuRapport.getYear() );
		moisDebut = String.valueOf( dateDuRapport.getMonthValue() );
		if ( moisDebut.length() == 1 ) {
			moisDebut = "0" + moisDebut;
		}
		jourDebut = String.valueOf( dateDuRapport.getDayOfMonth() );
		if ( jourDebut.length() == 1 ) {
			jourDebut = "0" + jourDebut;
		}
		date = anneeDebut + "-" + moisDebut + "-" + jourDebut + "T" + "00:00:00";

		sbContent.append( "<?xml version=\"1.0\" encoding=\"utf-8\"?>" );
		sbContent.append( "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservices.portail.artemis.fr/\">" );
		sbContent.append( "<soapenv:Header/>" );
		sbContent.append( "<soapenv:Body>" );
		sbContent.append( "<web:getRapportDeChantier>" );
		sbContent.append( "<dateRapport>" ).append( date ).append( "</dateRapport>" );
		sbContent.append( "<idDevis>" ).append( idDevis ).append( "</idDevis>" );
		sbContent.append( "</web:getRapportDeChantier>" );
		sbContent.append( "</soapenv:Body>" );
		sbContent.append( "</soapenv:Envelope>" );

		return sbContent.toString();
	}
}
