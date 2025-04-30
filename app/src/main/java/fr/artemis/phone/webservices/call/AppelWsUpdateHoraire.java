package fr.artemis.phone.webservices.call;

import java.util.Calendar;
import java.util.Map;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import android.util.Log;

import fr.artemis.phone.dto.CRMPhoneHorairesDTO;
import fr.artemis.phone.lib.ksoap.ksoap2.SoapEnvelope;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapSerializationEnvelope;
import fr.artemis.phone.lib.ksoap.ksoap2.transport.HttpTransportSE;
import fr.artemis.phone.utils.ConstantesWS;
import fr.artemis.phone.utils.DateUtils;
import fr.artemis.phone.utils.PhoneUtil;
import fr.artemis.phone.utils.SessionPhone;
import fr.artemis.phone.webservices.AbstractCaller;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.mapper.impl.HorairesMapper;

/**
 * Appel du webservice de mise à joru d'horaire de salarié
 */
public class AppelWsUpdateHoraire extends AbstractCaller<CRMPhoneHorairesDTO> {

	// L'horaire
	private CRMPhoneHorairesDTO horaire;

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
	public AppelWsUpdateHoraire( WsCaller source, Map<String, Object> mapResources, WsName name ) {
		super( source, mapResources, name );
	}

	/**
	 * Injection de l'horaire à enregistrer
	 *
	 * @param horaire
	 *            L'horaire à enregistrer
	 */
	public void setHoraire( CRMPhoneHorairesDTO horaire ) {
		this.horaire = horaire;
	}

	@Override
	protected CRMPhoneHorairesDTO executionRequete() {

		SoapObject requete = new SoapObject( ConstantesWS.NAMESPACE, ConstantesWS.METHOD_UPDATE_HORAIRES );
		requete.addProperty( ConstantesWS.PARAM_WS_HORAIRE, horaire );

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
			String content = parseContent( horaire );
			http_transport.callPropre( "", enveloppe, content );

			SoapObject reponse = (SoapObject) enveloppe.getResponse();

			if ( null != reponse ) {
				return new HorairesMapper().map( reponse );
			}

		} catch ( Exception ex ) {
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException( ex );
			Log.e( "BUG", "Erreur lors de l'enregistrement d'une horaire : ", ex );
		}
		return null;
	}

	/**
	 * Ecriture de l'enveloppe SOAP de l'horaire
	 *
	 * @param horaire
	 *            Le DTO
	 * @return L'enveloppe SOAP
	 */
	private String parseContent( CRMPhoneHorairesDTO horaire ) {

		StringBuilder sbContent = new StringBuilder();

		if ( null != horaire ) {
			String arret;
			String conges;
			String recup;
			if ( horaire.isArret() ) {
				arret = "true";
			} else {
				arret = "false";
			}
			if ( horaire.isConges() ) {
				conges = "true";
			} else {
				conges = "false";
			}
			if ( horaire.isRecup() ) {
				recup = "true";
			} else {
				recup = "false";
			}

			String heureDebutMatin = null;
			String heureFinMatin = null;
			String heureDebutAprem = null;
			String heureFinAprem = null;

			if ( null != horaire.getHeureDebutMatin() ) {
				String heure = String.valueOf( DateUtils.dateToCalendar( horaire.getHeureDebutMatin() ).get( Calendar.HOUR_OF_DAY ) );
				if ( heure.length() == 1 ) {
					heure = "0" + heure;
				}
				String minute = String.valueOf( DateUtils.dateToCalendar( horaire.getHeureDebutMatin() ).get( Calendar.MINUTE ) );
				if ( minute.length() == 1 ) {
					minute = "0" + minute;
				}
				heureDebutMatin = "1970-01-01T" + heure + ":" + minute + ":00";
			}

			if ( null != horaire.getHeureFinMatin() ) {
				String heure = String.valueOf( DateUtils.dateToCalendar( horaire.getHeureFinMatin() ).get( Calendar.HOUR_OF_DAY ) );
				if ( heure.length() == 1 ) {
					heure = "0" + heure;
				}
				String minute = String.valueOf( DateUtils.dateToCalendar( horaire.getHeureFinMatin() ).get( Calendar.MINUTE ) );
				if ( minute.length() == 1 ) {
					minute = "0" + minute;
				}
				heureFinMatin = "1970-01-01T" + heure + ":" + minute + ":00";
			}

			if ( null != horaire.getHeureDebutAprem() ) {
				String heure = String.valueOf( DateUtils.dateToCalendar( horaire.getHeureDebutAprem() ).get( Calendar.HOUR_OF_DAY ) );
				if ( heure.length() == 1 ) {
					heure = "0" + heure;
				}
				String minute = String.valueOf( DateUtils.dateToCalendar( horaire.getHeureDebutAprem() ).get( Calendar.MINUTE ) );
				if ( minute.length() == 1 ) {
					minute = "0" + minute;
				}
				heureDebutAprem = "1970-01-01T" + heure + ":" + minute + ":00";
			}

			if ( null != horaire.getHeureFinAprem() ) {
				String heure = String.valueOf( DateUtils.dateToCalendar( horaire.getHeureFinAprem() ).get( Calendar.HOUR_OF_DAY ) );
				if ( heure.length() == 1 ) {
					heure = "0" + heure;
				}
				String minute = String.valueOf( DateUtils.dateToCalendar( horaire.getHeureFinAprem() ).get( Calendar.MINUTE ) );
				if ( minute.length() == 1 ) {
					minute = "0" + minute;
				}
				heureFinAprem = "1970-01-01T" + heure + ":" + minute + ":00";
			}

			String annee = String.valueOf( horaire.getHoraireDate().get( Calendar.YEAR ) );
			String mois = String.valueOf( horaire.getHoraireDate().get( Calendar.MONTH ) + 1 );
			String jour = String.valueOf( horaire.getHoraireDate().get( Calendar.DAY_OF_MONTH ) );

			if ( mois.length() == 1 ) {
				mois = "0" + mois;
			}

			if ( jour.length() == 1 ) {
				jour = "0" + jour;
			}

			String dateHoraire = annee + "-" + mois + "-" + jour + "T00:00:00";

			sbContent.append( "<?xml version=\"1.0\" encoding=\"utf-8\"?>" );
			sbContent.append( "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservices.portail.artemis.fr/\">" );
			sbContent.append( "<soapenv:Header/>" );
			sbContent.append( "<soapenv:Body>" );
			sbContent.append( "<web:updateHoraire>" );
			sbContent.append( "<horaire>" );
			sbContent.append( "<arret>" ).append( arret ).append( "</arret>" );
			sbContent.append( "<recup>" ).append( recup ).append( "</recup>" );
			sbContent.append( "<conges>" ).append( conges ).append( "</conges>" );
			if ( null != heureDebutMatin ) {
				sbContent.append( "<heureDebutMatin>" ).append( heureDebutMatin ).append( "</heureDebutMatin>" );
			}
			if ( null != heureFinMatin ) {
				sbContent.append( "<heureFinMatin>" ).append( heureFinMatin ).append( "</heureFinMatin>" );
			}
			if ( null != heureDebutAprem ) {
				sbContent.append( "<heureDebutAprem>" ).append( heureDebutAprem ).append( "</heureDebutAprem>" );
			}
			if ( null != heureFinAprem ) {
				sbContent.append( "<heureFinAprem>" ).append( heureFinAprem ).append( "</heureFinAprem>" );
			}
			sbContent.append( "<horairesDate>" ).append( dateHoraire ).append( "</horairesDate>" );
			sbContent.append( "<fkSalarie>" ).append( horaire.getFkSalarie() ).append( "</fkSalarie>" );
			sbContent.append( "<id>" ).append( horaire.getId() ).append( "</id>" );
			sbContent.append( "</horaire>" );
			sbContent.append( "</web:updateHoraire>" );
			sbContent.append( "</soapenv:Body>" );
			sbContent.append( "</soapenv:Envelope>" );
		}

		return sbContent.toString();
	}
}