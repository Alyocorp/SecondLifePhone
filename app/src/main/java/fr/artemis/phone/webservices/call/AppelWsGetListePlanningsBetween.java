package fr.artemis.phone.webservices.call;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import android.util.Log;

import fr.artemis.phone.dto.CRMPhonePlanningDTO;
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
import fr.artemis.phone.webservices.mapper.impl.PlanningMapper;

/**
 * Recuperation de la liste des plannings entre 2 dates
 */
public class AppelWsGetListePlanningsBetween extends AbstractCaller<List<CRMPhonePlanningDTO>> {

	private LocalDate dateDebut;
	private LocalDate dateFin;

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
	public AppelWsGetListePlanningsBetween( WsCaller source, Map<String, Object> mapResources, WsName name ) {
		super( source, mapResources, name );
	}

	/**
	 * Injection de la date de debut
	 *
	 * @param dateDebut
	 *            La date de debut
	 */
	public void setDateDebut( LocalDate dateDebut ) {
		this.dateDebut = dateDebut;
	}

	/**
	 * Injection de la date de fin
	 *
	 * @param dateFin
	 *            La date de fin
	 */
	public void setDateFin( LocalDate dateFin ) {
		this.dateFin = dateFin;
	}

	@Override
	protected List<CRMPhonePlanningDTO> executionRequete() {

		// Injection des propriétés nécessaires à l'authentification
		SoapObject requete = new SoapObject( ConstantesWS.NAMESPACE, ConstantesWS.METHOD_LISTE_PLANNING_BETWEEN );

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
			String content = parseContent();
			http_transport.callPropre( "", enveloppe, content );
			SoapObject reponse = (SoapObject) enveloppe.bodyIn;

			if ( null != reponse && reponse.getPropertyCount() > 0 ) {
				return new PlanningMapper().mapList( reponse );
			}

		} catch ( Exception ex ) {
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException( ex );
			Log.e( "BUG", "Erreur lors de la recuperation des plannings : ", ex );
		}
		return null;
	}

	private String parseContent() {
		StringBuilder sbContent = new StringBuilder();

		String anneeDebut;
		String moisDebut;
		String jourDebut;

		String anneeFin;
		String moisFin;
		String jourFin;

		String debut;
		String fin;

		anneeDebut = String.valueOf( dateDebut.getYear() );
		moisDebut = String.valueOf( dateDebut.getMonthValue() );
		if ( moisDebut.length() == 1 ) {
			moisDebut = "0" + moisDebut;
		}
		jourDebut = String.valueOf( dateDebut.getDayOfMonth() );
		if ( jourDebut.length() == 1 ) {
			jourDebut = "0" + jourDebut;
		}

		anneeFin = String.valueOf( dateFin.getYear() );
		moisFin = String.valueOf( dateFin.getMonthValue() );
		if ( moisFin.length() == 1 ) {
			moisFin = "0" + moisFin;
		}
		jourFin = String.valueOf( dateFin.getDayOfMonth() );
		if ( jourFin.length() == 1 ) {
			jourFin = "0" + jourFin;
		}

		debut = anneeDebut + "-" + moisDebut + "-" + jourDebut + "T" + "00:00:00";
		fin = anneeFin + "-" + moisFin + "-" + jourFin + "T" + "23:59:59";

		sbContent.append( "<?xml version=\"1.0\" encoding=\"utf-8\"?>" );
		sbContent.append( "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservices.portail.artemis.fr/\">" );
		sbContent.append( "<soapenv:Header/>" );
		sbContent.append( "<soapenv:Body>" );
		sbContent.append( "<web:getListePlanningBetween>" );
		sbContent.append( "<startDate>" ).append( debut ).append( "</startDate>" );
		sbContent.append( "<endDate>" ).append( fin ).append( "</endDate>" );
		sbContent.append( "</web:getListePlanningBetween>" );
		sbContent.append( "</soapenv:Body>" );
		sbContent.append( "</soapenv:Envelope>" );

		return sbContent.toString();
	}
}