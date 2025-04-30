package fr.artemis.phone.webservices.call;

import java.util.Calendar;
import java.util.Map;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import android.util.Log;

import fr.artemis.phone.dto.CRMPhoneRapportChantierDTO;
import fr.artemis.phone.dto.CRMPhoneRapportChantierEquipeDTO;
import fr.artemis.phone.dto.CRMPhoneRapportChantierPhotoDTO;
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

public class AppelWsSaveRapportChantier extends AbstractCaller<CRMPhoneRapportChantierDTO> {

	// L'equipement
	private CRMPhoneRapportChantierDTO rapportChantier;

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
	public AppelWsSaveRapportChantier( WsCaller source, Map<String, Object> mapResources, WsName name ) {
		super( source, mapResources, name );
	}

	/**
	 * Injection du rapport de chantier à sauvegarder
	 *
	 * @param rapportChantier
	 *            Le rapprot de chantier
	 */
	public void setRapportChantier( CRMPhoneRapportChantierDTO rapportChantier ) {
		this.rapportChantier = rapportChantier;
	}

	@Override
	protected CRMPhoneRapportChantierDTO executionRequete() {

		SoapObject requete = new SoapObject( ConstantesWS.NAMESPACE, ConstantesWS.METHOD_SAVE_RAPPORT_CHANTIER );
		// requete.addProperty( ConstantesWS.PARAM_WS_RAPPORT, rapportChantier );

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
			String content = parseContent( rapportChantier );

			http_transport.callPropre( "", enveloppe, content );
			SoapObject reponse = (SoapObject) enveloppe.getResponse();

			return new RapportChantierMapper().map( reponse );

		} catch ( Exception ex ) {
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException( ex );
			Log.e( "BUG", "Erreur lors de l'enregistrement d'un rapport de chantier : ", ex );
		}
		return null;
	}

	/**
	 * Creation de la requete SOAP de sauvegarde
	 *
	 * @param rapport
	 *            Le rapport de chantier a transformer en enveloppe SOAP
	 * @return La chaine de caractere correspondant à l'envelopep SOAP
	 */
	private String parseContent( CRMPhoneRapportChantierDTO rapport ) {

		// Date de saisie
		String anneeDebutSaisie;
		String moisDebutSaisie;
		String jourDebutSaisie;

		String dateSaisie = null;

		if ( null != rapport.getDateSaisie() ) {
			anneeDebutSaisie = String.valueOf( rapport.getDateSaisie().get( Calendar.YEAR ) );
			moisDebutSaisie = String.valueOf( rapport.getDateSaisie().get( Calendar.MONTH ) + 1 );

			if ( moisDebutSaisie.length() == 1 ) {
				moisDebutSaisie = "0" + moisDebutSaisie;
			}
			jourDebutSaisie = String.valueOf( rapport.getDateSaisie().get( Calendar.DAY_OF_MONTH ) );
			if ( jourDebutSaisie.length() == 1 ) {
				jourDebutSaisie = "0" + jourDebutSaisie;
			}
			dateSaisie = anneeDebutSaisie + "-" + moisDebutSaisie + "-" + jourDebutSaisie + "T" + "00:00:00";
		}

		// Date de dapport
		String anneeDebutRapport;
		String moisDebutRapport;
		String jourDebutRapport;

		String dateRapport = null;

		if ( null != rapport.getDateRapport() ) {

			anneeDebutRapport = String.valueOf( rapport.getDateRapport().get( Calendar.YEAR ) );
			moisDebutRapport = String.valueOf( rapport.getDateRapport().get( Calendar.MONTH ) + 1 );

			if ( moisDebutRapport.length() == 1 ) {
				moisDebutRapport = "0" + moisDebutRapport;
			}
			jourDebutRapport = String.valueOf( rapport.getDateRapport().get( Calendar.DAY_OF_MONTH ) );
			if ( jourDebutRapport.length() == 1 ) {
				jourDebutRapport = "0" + jourDebutRapport;
			}
			dateRapport = anneeDebutRapport + "-" + moisDebutRapport + "-" + jourDebutRapport + "T" + "00:00:00";
		}

		StringBuilder sbContent = new StringBuilder();

		sbContent.append(
				"<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservices.portail.artemis.fr/\">\n"
						+ "   <soapenv:Header/>\n" + "   <soapenv:Body>\n" + "      <web:saveRapportDeChantier>" + "           <rapport>" );

		if ( null != rapport.getId() ) {
			sbContent.append( "<id>" ).append( rapport.getId() ).append( "</id>" );
		}
		if ( null != rapport.getFkIntervention() ) {
			sbContent.append( "<fkIntervention>" ).append( rapport.getFkIntervention() ).append( "</fkIntervention>" );
		}
		if ( null != rapport.getFkUser() ) {
			sbContent.append( "<fkUser>" ).append( rapport.getFkUser() ).append( "</fkUser>" );
		}
		if ( null != rapport.getDateSaisie() ) {
			sbContent.append( "<dateSaisie>" ).append( dateSaisie ).append( "</dateSaisie>" );
		}
		if ( null != rapport.getDateRapport() ) {
			sbContent.append( "<dateRapport>" ).append( dateRapport ).append( "</dateRapport>" );
		}

		if ( null != rapport.getListePhotosAvant() && !rapport.getListePhotosAvant().isEmpty() ) {
			for ( CRMPhoneRapportChantierPhotoDTO photo : rapport.getListePhotosAvant() ) {
				sbContent.append( "<listePhotosAvant>" );
				sbContent.append( "<contentThumbnail>" ).append( photo.getContentThumbnail() ).append( "</contentThumbnail>" );
				sbContent.append( "<fileName>" ).append( photo.getFileName() ).append( "</fileName>" );
				if ( null != photo.getFkRapport() ) {
					sbContent.append( "<fkRapport>" ).append( photo.getFkRapport() ).append( "</fkRapport>" );
				}
				if ( null != photo.getId() ) {
					sbContent.append( "<id>" ).append( photo.getId() ).append( "</id>" );
				}
				sbContent.append( "</listePhotosAvant>" );
			}
		}

		if ( null != rapport.getListePhotosRue() && !rapport.getListePhotosRue().isEmpty() ) {
			for ( CRMPhoneRapportChantierPhotoDTO photo : rapport.getListePhotosRue() ) {
				sbContent.append( "<listePhotosRue>" );
				sbContent.append( "<contentThumbnail>" ).append( photo.getContentThumbnail() ).append( "</contentThumbnail>" );
				sbContent.append( "<fileName>" ).append( photo.getFileName() ).append( "</fileName>" );
				if ( null != photo.getFkRapport() ) {
					sbContent.append( "<fkRapport>" ).append( photo.getFkRapport() ).append( "</fkRapport>" );
				}
				if ( null != photo.getId() ) {
					sbContent.append( "<id>" ).append( photo.getId() ).append( "</id>" );
				}
				sbContent.append( "</listePhotosRue>" );
			}
		}

		if ( null != rapport.getListePhotosApres() && !rapport.getListePhotosApres().isEmpty() ) {
			for ( CRMPhoneRapportChantierPhotoDTO photo : rapport.getListePhotosApres() ) {
				sbContent.append( "<listePhotosApres>" );
				sbContent.append( "<contentThumbnail>" ).append( photo.getContentThumbnail() ).append( "</contentThumbnail>" );
				sbContent.append( "<fileName>" ).append( photo.getFileName() ).append( "</fileName>" );
				if ( null != photo.getFkRapport() ) {
					sbContent.append( "<fkRapport>" ).append( photo.getFkRapport() ).append( "</fkRapport>" );
				}
				if ( null != photo.getId() ) {
					sbContent.append( "<id>" ).append( photo.getId() ).append( "</id>" );
				}
				sbContent.append( "</listePhotosApres>" );
			}
		}

		if ( null != rapport.isControleKit() ) {
			if ( rapport.isControleKit() ) {
				sbContent.append( "<controleKit>" ).append( "true" ).append( "</controleKit>" );
			} else {
				sbContent.append( "<controleKit>" ).append( "false" ).append( "</controleKit>" );
			}
		}

		if ( null != rapport.isDevisReajuste() ) {
			if ( rapport.isDevisReajuste() ) {
				sbContent.append( "<devisReajuste>" ).append( "true" ).append( "</devisReajuste>" );
			} else {
				sbContent.append( "<devisReajuste>" ).append( "false" ).append( "</devisReajuste>" );
			}
		}

		if ( null != rapport.getDevisReajusteVolume() ) {
			sbContent.append( "<devisReajusteVolume>" ).append( rapport.getDevisReajusteVolume() ).append( "</devisReajusteVolume>" );
		}
		if ( null != rapport.getDevisReajusteValeur() ) {
			sbContent.append( "<devisReajusteValeur>" ).append( rapport.getDevisReajusteValeur() ).append( "</devisReajusteValeur>" );
		}
		if ( null != rapport.getDevisReajusteTotal() ) {
			sbContent.append( "<devisReajusteTotal>" ).append( rapport.getDevisReajusteTotal() ).append( "</devisReajusteTotal>" );
		}
		if ( null != rapport.getFkChefDeChantier() ) {
			sbContent.append( "<fkChefDeChantier>" ).append( rapport.getFkChefDeChantier() ).append( "</fkChefDeChantier>" );
		}

		if ( null != rapport.getListeEquipe() && !rapport.getListeEquipe().isEmpty() ) {
			for ( CRMPhoneRapportChantierEquipeDTO equipe : rapport.getListeEquipe() ) {
				sbContent.append( "<listeEquipe>" );
				if ( null != equipe.getFkRapport() ) {
					sbContent.append( "<fkRapport>" ).append( equipe.getFkRapport() ).append( "</fkRapport>" );
				}
				sbContent.append( "<fkSalarie>" ).append( equipe.getFkSalarie() ).append( "</fkSalarie>" );
				if ( null != equipe.getId() ) {
					sbContent.append( "<id>" ).append( equipe.getId() ).append( "</id>" );
				}
				sbContent.append( "</listeEquipe>" );
			}
		}

		if ( null != rapport.isRetard() ) {
			if ( rapport.isRetard() ) {
				sbContent.append( "<retard>" ).append( "true" ).append( "</retard>" );
			} else {
				sbContent.append( "<retard>" ).append( "false" ).append( "</retard>" );
			}
		}

		if ( null != rapport.isRetardClientPrevenu() ) {
			if ( rapport.isRetardClientPrevenu() ) {
				sbContent.append( "<retardClientPrevenu>" ).append( "true" ).append( "</retardClientPrevenu>" );
			} else {
				sbContent.append( "<retardClientPrevenu>" ).append( "false" ).append( "</retardClientPrevenu>" );
			}
		}

		if ( null != rapport.getRetardDetails() && !"".equals( rapport.getRetardDetails() ) ) {
			sbContent.append( "<retardDetails>" ).append( rapport.getRetardDetails() ).append( "</retardDetails>" );
		}

		if ( null != rapport.getHeureArriveeChantier() && !"".equals( rapport.getHeureArriveeChantier() ) ) {
			sbContent.append( "<heureArriveeChantier>" ).append( rapport.getHeureArriveeChantier() ).append( "</heureArriveeChantier>" );
		}

		if ( null != rapport.getHeureDepartChantier() && !"".equals( rapport.getHeureDepartChantier() ) ) {
			sbContent.append( "<heureDepartChantier>" ).append( rapport.getHeureDepartChantier() ).append( "</heureDepartChantier>" );
		}

		if ( null != rapport.isDeterioration() ) {
			if ( rapport.isDeterioration() ) {
				sbContent.append( "<deterioration>" ).append( "true" ).append( "</deterioration>" );
			} else {
				sbContent.append( "<deterioration>" ).append( "false" ).append( "</deterioration>" );
			}
		}

		if ( null != rapport.getDeteriorationDetails() && !"".equals( rapport.getDeteriorationDetails() ) ) {
			sbContent.append( "<deteriorationDetails>" ).append( rapport.getDeteriorationDetails() ).append( "</deteriorationDetails>" );
		}

		if ( null != rapport.isDechetPartic() ) {
			if ( rapport.isDechetPartic() ) {
				sbContent.append( "<dechetPartic>" ).append( "true" ).append( "</dechetPartic>" );
			} else {
				sbContent.append( "<dechetPartic>" ).append( "false" ).append( "</dechetPartic>" );
			}
		}

		if ( null != rapport.getVolumeTraiteDechetPartic() ) {
			sbContent.append( "<volumeTraiteDechetPartic>" ).append( rapport.getVolumeTraiteDechetPartic() ).append( "</volumeTraiteDechetPartic>" );
		}

		if ( null != rapport.isDechetPro() ) {
			if ( rapport.isDechetPro() ) {
				sbContent.append( "<dechetPro>" ).append( "true" ).append( "</dechetPro>" );
			} else {
				sbContent.append( "<dechetPro>" ).append( "false" ).append( "</dechetPro>" );
			}
		}

		if ( null != rapport.getVolumeTraiteDechetProDib() ) {
			sbContent.append( "<volumeTraiteDechetProDib>" ).append( rapport.getVolumeTraiteDechetProDib() ).append( "</volumeTraiteDechetProDib>" );
		}

		if ( null != rapport.getVolumeTraiteDechetProBois() ) {
			sbContent.append( "<volumeTraiteDechetProBois>" ).append( rapport.getVolumeTraiteDechetProBois() ).append( "</volumeTraiteDechetProBois>" );
		}

		if ( null != rapport.getVolumeTraiteDechetProFerraille() ) {
			sbContent.append( "<volumeTraiteDechetProFerraille>" ).append( rapport.getVolumeTraiteDechetProFerraille() ).append( "</volumeTraiteDechetProFerraille>" );
		}

		if ( null != rapport.getVolumeTraiteDechetProPapier() ) {
			sbContent.append( "<volumeTraiteDechetProPapier>" ).append( rapport.getVolumeTraiteDechetProPapier() ).append( "</volumeTraiteDechetProPapier>" );
		}

		if ( null != rapport.getVolumeTraiteDechetProAutres() ) {
			sbContent.append( "<volumeTraiteDechetProAutres>" ).append( rapport.getVolumeTraiteDechetProAutres() ).append( "</volumeTraiteDechetProAutres>" );
		}

		if ( null != rapport.isBennes() ) {
			if ( rapport.isBennes() ) {
				sbContent.append( "<bennes>" ).append( "true" ).append( "</bennes>" );
			} else {
				sbContent.append( "<bennes>" ).append( "false" ).append( "</bennes>" );
			}
		}

		if ( null != rapport.getVolumeTraiteBennesDib() ) {
			sbContent.append( "<volumeTraiteBennesDib>" ).append( rapport.getVolumeTraiteBennesDib() ).append( "</volumeTraiteBennesDib>" );
		}

		if ( null != rapport.getVolumeTraiteBennesBois() ) {
			sbContent.append( "<volumeTraiteBennesBois>" ).append( rapport.getVolumeTraiteBennesBois() ).append( "</volumeTraiteBennesBois>" );
		}

		if ( null != rapport.getVolumeTraiteBennesFerraille() ) {
			sbContent.append( "<volumeTraiteBennesFerraille>" ).append( rapport.getVolumeTraiteBennesFerraille() ).append( "</volumeTraiteBennesFerraille>" );
		}

		if ( null != rapport.getVolumeTraiteBennesPapier() ) {
			sbContent.append( "<volumeTraiteBennesPapier>" ).append( rapport.getVolumeTraiteBennesPapier() ).append( "</volumeTraiteBennesPapier>" );
		}

		if ( null != rapport.getVolumeTraiteBennesAutres() ) {
			sbContent.append( "<volumeTraiteBennesAutres>" ).append( rapport.getVolumeTraiteBennesAutres() ).append( "</volumeTraiteBennesAutres>" );
		}

		if ( null != rapport.isDepotVente() ) {
			if ( rapport.isDepotVente() ) {
				sbContent.append( "<depotVente>" ).append( "true" ).append( "</depotVente>" );
			} else {
				sbContent.append( "<depotVente>" ).append( "false" ).append( "</depotVente>" );
			}
		}

		if ( null != rapport.getVolumeTraiteDepotVente() ) {
			sbContent.append( "<volumeTraiteDepotVente>" ).append( rapport.getVolumeTraiteDepotVente() ).append( "</volumeTraiteDepotVente>" );
		}

		if ( null != rapport.isAssociation() ) {
			if ( rapport.isAssociation() ) {
				sbContent.append( "<association>" ).append( "true" ).append( "</association>" );
			} else {
				sbContent.append( "<association>" ).append( "false" ).append( "</association>" );
			}
		}

		if ( null != rapport.getVolumeTraiteAssociation() ) {
			sbContent.append( "<volumeTraiteAssociation>" ).append( rapport.getVolumeTraiteAssociation() ).append( "</volumeTraiteAssociation>" );
		}

		if ( null != rapport.isRamene() ) {
			if ( rapport.isRamene() ) {
				sbContent.append( "<ramene>" ).append( "true" ).append( "</ramene>" );
			} else {
				sbContent.append( "<ramene>" ).append( "false" ).append( "</ramene>" );
			}
		}

		if ( null != rapport.getVolumeTraiteRamene() ) {
			sbContent.append( "<volumeTraiteRamene>" ).append( rapport.getVolumeTraiteRamene() ).append( "</volumeTraiteRamene>" );
		}

		if ( null != rapport.getVolumeCamionRamene() ) {
			sbContent.append( "<volumeCamionRamene>" ).append( rapport.getVolumeCamionRamene() ).append( "</volumeCamionRamene>" );
		}
		if ( null != rapport.getVolumeCamionFerraille() ) {
			sbContent.append( "<volumeCamionFerraille>" ).append( rapport.getVolumeCamionFerraille() ).append( "</volumeCamionFerraille>" );
		}
		if ( null != rapport.getVolumeCamionPapier() ) {
			sbContent.append( "<volumeCamionPapier>" ).append( rapport.getVolumeCamionPapier() ).append( "</volumeCamionPapier>" );
		}
		if ( null != rapport.getVolumeCamionDecheterie() ) {
			sbContent.append( "<volumeCamionDecheterie>" ).append( rapport.getVolumeCamionDecheterie() ).append( "</volumeCamionDecheterie>" );
		}

		if ( null != rapport.isMenage() ) {
			if ( rapport.isMenage() ) {
				sbContent.append( "<menage>" ).append( "true" ).append( "</menage>" );
			} else {
				sbContent.append( "<menage>" ).append( "false" ).append( "</menage>" );
			}
		}

		if ( null != rapport.getMenageTemps() && !"".equals( rapport.getMenageTemps() ) ) {
			sbContent.append( "<menageTemps>" ).append( rapport.getMenageTemps() ).append( "</menageTemps>" );
		}

		if ( null != rapport.isRespectConsignes() ) {
			if ( rapport.isRespectConsignes() ) {
				sbContent.append( "<respectConsignes>" ).append( "true" ).append( "</respectConsignes>" );
			} else {
				sbContent.append( "<respectConsignes>" ).append( "false" ).append( "</respectConsignes>" );
			}
		}
		if ( null != rapport.getRespectConsignesDetails() && !"".equals( rapport.getRespectConsignesDetails() ) ) {
			sbContent.append( "<respectConsignesDetails>" ).append( rapport.getRespectConsignesDetails() ).append( "</respectConsignesDetails>" );
		}

		if ( null != rapport.isReglementRecuOuIndemnisation() ) {
			if ( rapport.isReglementRecuOuIndemnisation() ) {
				sbContent.append( "<reglementRecuOuIndemnisation>" ).append( "true" ).append( "</reglementRecuOuIndemnisation>" );
			} else {
				sbContent.append( "<reglementRecuOuIndemnisation>" ).append( "false" ).append( "</reglementRecuOuIndemnisation>" );
			}
		}

		if ( null != rapport.getMontantRecuOuIndemnise() ) {
			sbContent.append( "<montantRecuOuIndemnise>" ).append( rapport.getMontantRecuOuIndemnise() ).append( "</montantRecuOuIndemnise>" );
		}

		if ( null != rapport.getDetailsReglement() && !"".equals( rapport.getDetailsReglement() ) ) {
			sbContent.append( "<detailsReglement>" ).append( rapport.getDetailsReglement() ).append( "</detailsReglement>" );
		}

		if ( null != rapport.getRemarques() && !"".equals( rapport.getRemarques() ) ) {
			sbContent.append( "<remarques>" ).append( rapport.getRemarques() ).append( "</remarques>" );
		}

		sbContent.append( "         </rapport>\n" + "      </web:saveRapportDeChantier>\n" + "   </soapenv:Body>\n" + "</soapenv:Envelope>" );

		return sbContent.toString();
	}
}
