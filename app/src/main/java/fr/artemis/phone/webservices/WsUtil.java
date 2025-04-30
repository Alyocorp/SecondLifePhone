package fr.artemis.phone.webservices;

import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.util.Map;

import fr.artemis.phone.activities.main.MainActivity;
import fr.artemis.phone.dto.CRMPhoneDecheterieDTO;
import fr.artemis.phone.dto.CRMPhoneDepotsVenteDTO;
import fr.artemis.phone.dto.CRMPhoneEquipementDTO;
import fr.artemis.phone.dto.CRMPhoneEquipementSalarieDTO;
import fr.artemis.phone.dto.CRMPhoneHorairesDTO;
import fr.artemis.phone.dto.CRMPhoneRapportChantierDTO;
import fr.artemis.phone.dto.CRMPhoneRapportChantierPhotoDTO;
import fr.artemis.phone.fragments.utilisateur.documents.FragmentUserDocuments;
import fr.artemis.phone.webservices.call.AppelServletUploadPhoto;
import fr.artemis.phone.webservices.call.AppelWsAuthentication;
import fr.artemis.phone.webservices.call.AppelWsDeleteEquipement;
import fr.artemis.phone.webservices.call.AppelWsDeleteEquipementSalarie;
import fr.artemis.phone.webservices.call.AppelWsGenerateEmptyEquipement;
import fr.artemis.phone.webservices.call.AppelWsGenerateEmptyEquipementSalarie;
import fr.artemis.phone.webservices.call.AppelWsGetBalanceTotaleHoraire;
import fr.artemis.phone.webservices.call.AppelWsGetDecheteriesUpdates;
import fr.artemis.phone.webservices.call.AppelWsGetListeDepotsVente;
import fr.artemis.phone.webservices.call.AppelWsGetDevisFile;
import fr.artemis.phone.webservices.call.AppelWsGetInterventionDetails;
import fr.artemis.phone.webservices.call.AppelWsGetKmlDecheteries;
import fr.artemis.phone.webservices.call.AppelWsGetKmlDepotsVente;
import fr.artemis.phone.webservices.call.AppelWsGetListeDocuments;
import fr.artemis.phone.webservices.call.AppelWsGetListeEquipements;
import fr.artemis.phone.webservices.call.AppelWsGetListeEquipementsSalarie;
import fr.artemis.phone.webservices.call.AppelWsGetListeEquipementsSalarieActives;
import fr.artemis.phone.webservices.call.AppelWsGetListeEquipementsSalariePossedes;
import fr.artemis.phone.webservices.call.AppelWsGetListeHorairesByIdUser;
import fr.artemis.phone.webservices.call.AppelWsGetListePlanningsBetween;
import fr.artemis.phone.webservices.call.AppelWsGetListePlanningsInterventionBetween;
import fr.artemis.phone.webservices.call.AppelWsGetListeSalariesActifs;
import fr.artemis.phone.webservices.call.AppelWsGetListeTypesTailles;
import fr.artemis.phone.webservices.call.AppelWsGetPlanningFile;
import fr.artemis.phone.webservices.call.AppelWsGetPlanningRapport;
import fr.artemis.phone.webservices.call.AppelWsGetRapportDeChantier;
import fr.artemis.phone.webservices.call.AppelWsGetSalarieByIdUser;
import fr.artemis.phone.webservices.call.AppelWsGetVersionMapDecheterie;
import fr.artemis.phone.webservices.call.AppelWsGetVersionMapDepotsVente;
import fr.artemis.phone.webservices.call.AppelWsIsEquipementDeletable;
import fr.artemis.phone.webservices.call.AppelWsIsEquipementSalarieDeletable;
import fr.artemis.phone.webservices.call.AppelWsSaveEquipement;
import fr.artemis.phone.webservices.call.AppelWsSaveEquipementSalarie;
import fr.artemis.phone.webservices.call.AppelWsSaveRapportChantier;
import fr.artemis.phone.webservices.call.AppelWsSendDocument;
import fr.artemis.phone.webservices.call.AppelWsUpdateDecheterie;
import fr.artemis.phone.webservices.call.AppelWsUpdateDepotVente;
import fr.artemis.phone.webservices.call.AppelWsUpdateHoraire;

public class WsUtil {

	/**
	 * Authentification d'un utilisateur
	 *
	 * @param source
	 *            L'appelant
	 * @param mapRessources
	 *            Map de ressources
	 * @param username
	 *            Le nom d'utilisateur
	 * @param password
	 *            Le mot de pass de l'utilisateur
	 */
	public static void authenticate( WsCaller source, Map<String, Object> mapRessources, String username, String password ) {
		AppelWsAuthentication ws = new AppelWsAuthentication( source, mapRessources, WsName.AUTHENTIFICATION );

		ws.setPassword( password );
		ws.setUsername( username );
		ws.execute();
	}

	/**
	 * Récupération du numero de version de la carte des decheteries
	 *
	 * @param source
	 *            L'appelant
	 * @param mapRessources
	 *            Map de ressources
	 */
	public static void getVersionMapDecheterie( WsCaller source, Map<String, Object> mapRessources ) {
		AppelWsGetVersionMapDecheterie ws = new AppelWsGetVersionMapDecheterie( source, mapRessources, WsName.GET_VERSION_MAP_DECHETERIE );
		ws.execute();
	}

	/**
	 * Récupération de la liste des décheteries
	 *
	 * @param source
	 *            L'appelant
	 * @param mapRessources
	 *            Map de ressources
	 */
	public static void getDecheteries( WsCaller source, Map<String, Object> mapRessources ) {
		AppelWsGetKmlDecheteries ws = new AppelWsGetKmlDecheteries( source, mapRessources, WsName.GET_KML_DECHETERIE );
		ws.execute();
	}

	/**
	 * Enregistrement d'une decheterie
	 *
	 * @param source
	 *            L'appelant
	 * @param mapRessources
	 *            Map de ressources
	 * @param decheterie
	 *            La decheterie à enregistrer
	 */
	public static void updateDecheterie( WsCaller source, Map<String, Object> mapRessources, CRMPhoneDecheterieDTO decheterie ) {
		AppelWsUpdateDecheterie ws = new AppelWsUpdateDecheterie( source, mapRessources, WsName.UPDATE_DECHETERIE );
		ws.setDecheterie( decheterie );
		ws.execute();
	}

	/**
	 * Recuperation du numero de version de la carte des depots vente
	 *
	 * @param source
	 *            L'appelant
	 * @param mapRessources
	 *            Map de ressources
	 */
	public static void getVersionMapDepotsVente( WsCaller source, Map<String, Object> mapRessources ) {
		AppelWsGetVersionMapDepotsVente ws = new AppelWsGetVersionMapDepotsVente( source, mapRessources, WsName.GET_VERSION_MAP_DEPOTS_VENTE );
		ws.execute();
	}

	/**
	 * Récuperation de la liste des depots vente
	 *
	 * @param source
	 *            L'appelant
	 * @param mapRessources
	 *            Map de ressources
	 */
	public static void getDepotsVente( WsCaller source, Map<String, Object> mapRessources ) {
		AppelWsGetKmlDepotsVente ws = new AppelWsGetKmlDepotsVente( source, mapRessources, WsName.GET_KML_DEPOTS_VENTE );
		ws.execute();
	}

	/**
	 * Enregistrement d'un depot vente
	 *
	 * @param source
	 *            L'appelant
	 * @param mapRessources
	 *            Map de ressources
	 * @param depotVente
	 *            Le depot vente à enregistrer
	 */
	public static void updateDepotVente( WsCaller source, Map<String, Object> mapRessources, CRMPhoneDepotsVenteDTO depotVente ) {
		AppelWsUpdateDepotVente ws = new AppelWsUpdateDepotVente( source, mapRessources, WsName.UPDATE_DEPOT_VENTE );
		ws.setDepotVente( depotVente );
		ws.execute();
	}

	/**
	 * Récupération de la liste des equipements
	 *
	 * @param source
	 *            L'appelant
	 * @param mapRessources
	 *            Map de ressources
	 */
	public static void getListeEquipements( WsCaller source, Map<String, Object> mapRessources ) {
		AppelWsGetListeEquipements ws = new AppelWsGetListeEquipements( source, mapRessources, WsName.GET_LISTE_EQUIPEMENTS );
		ws.execute();
	}

	/**
	 * Récupération de la liste des types de tailles
	 *
	 * @param source
	 *            L'appelant
	 * @param mapRessources
	 *            Map de ressources
	 */
	public static void getListeTypesTailles( WsCaller source, Map<String, Object> mapRessources ) {
		AppelWsGetListeTypesTailles ws = new AppelWsGetListeTypesTailles( source, mapRessources, WsName.GET_LISTE_TYPE_TAILLES );
		ws.execute();
	}

	/**
	 * Demande de création d'un equipement vide (seul l'identifiant doit être
	 * initialisé)
	 *
	 * @param fragmentSource
	 *            L'appelant
	 * @param mapRessources
	 *            Map de ressources
	 */
	public static void generateEmptyEquipement( WsCaller fragmentSource, Map<String, Object> mapRessources ) {
		AppelWsGenerateEmptyEquipement ws = new AppelWsGenerateEmptyEquipement( fragmentSource, mapRessources, WsName.GENERATE_EMPTY_EQUIPEMENT );
		ws.execute();
	}

	/**
	 * Sauvegarde d'un equipement
	 *
	 * @param source
	 *            L'appelant
	 * @param mapRessources
	 *            Map de ressources
	 * @param equipement
	 *            L'equipement à sauvegarde
	 */
	public static void saveEquipement( WsCaller source, Map<String, Object> mapRessources, CRMPhoneEquipementDTO equipement ) {
		AppelWsSaveEquipement ws = new AppelWsSaveEquipement( source, mapRessources, WsName.SAVE_EQUIPEMENT );
		ws.setEquipement( equipement );
		ws.execute();
	}

	/**
	 * Vérification si un equipement peut être supprimer
	 *
	 * @param source
	 *            L'appelant
	 * @param mapResources
	 *            Map de resources
	 * @param id
	 *            L'identifiant de l'equipement à supprimer
	 */
	public static void isEquipementDeletable( WsCaller source, Map<String, Object> mapResources, Integer id ) {
		AppelWsIsEquipementDeletable ws = new AppelWsIsEquipementDeletable( source, mapResources, WsName.IS_EQUIPEMENT_DELETABLE );
		ws.setIdEquipement( id );
		ws.execute();
	}

	/**
	 * Suppression d'un equipement
	 *
	 * @param source
	 *            La source appelant cette tache
	 * @param callbackResources
	 *            Map de resources
	 * @param id
	 *            L'identifiant de l'equipement à supprimer
	 */
	public static void deleteEquipement( WsCaller source, Map<String, Object> callbackResources, Integer id ) {
		AppelWsDeleteEquipement ws = new AppelWsDeleteEquipement( source, callbackResources, WsName.DELETE_EQUIPEMENT );
		ws.setIdEquipement( id );
		ws.execute();
	}

	/**
	 * Recuperation de la liste des equipements d'un salarié
	 *
	 * @param source
	 *            L'appelant
	 * @param mapRessources
	 *            Map de ressources
	 * @param id
	 *            Identifiant de l'utilisateur associé au salarié
	 */
	public static void getListeEquipementsSalarie( WsCaller source, Map<String, Object> mapRessources, Integer id ) {
		AppelWsGetListeEquipementsSalarie ws = new AppelWsGetListeEquipementsSalarie( source, mapRessources, WsName.GET_LISTE_EQUIPEMENTS_SALARIE );
		ws.setIdUser( id );

		ws.execute();
	}

	/**
	 * Sauvegarde d'une demande d'equipement de salarie
	 *
	 * @param source
	 *            L'appelant
	 * @param mapRessources
	 *            Map de ressources
	 * @param equipementSalarie
	 *            La demande d'equipement de salarié
	 */
	public static void saveEquipementSalarie( WsCaller source, Map<String, Object> mapRessources, CRMPhoneEquipementSalarieDTO equipementSalarie ) {
		AppelWsSaveEquipementSalarie ws = new AppelWsSaveEquipementSalarie( source, mapRessources, WsName.SAVE_EQUIPEMENT_SALARIE );
		ws.setEquipementSalarie( equipementSalarie );

		ws.execute();
	}

	/**
	 * Generation d'une demande d'equipement de salarié vide
	 *
	 * @param fragmentSource
	 *            L'appelant
	 * @param callbackResources
	 *            Map de ressources
	 */
	public static void generateEmptyEquipementSalarie( WsCaller fragmentSource, Map<String, Object> callbackResources ) {
		AppelWsGenerateEmptyEquipementSalarie ws = new AppelWsGenerateEmptyEquipementSalarie( fragmentSource, callbackResources, WsName.GENERATE_EMPTY_EQUIPEMENT_SALARIE );
		ws.execute();
	}

	/**
	 * Vérification si une demande d'equipement peut être supprimer
	 *
	 * @param fragmentSource
	 *            L'appelant
	 * @param callbackResources
	 *            Map de ressources
	 * @param id
	 *            L'identifiant de la demande d'equipement à supprimer
	 */
	public static void isEquipementSalarieDeletable( WsCaller fragmentSource, Map<String, Object> callbackResources, Integer id ) {
		AppelWsIsEquipementSalarieDeletable ws = new AppelWsIsEquipementSalarieDeletable( fragmentSource, callbackResources, WsName.IS_EQUIPEMENT_SALARIE_DELETABLE );
		ws.setIdEquipementSalarie( id );
		ws.execute();
	}

	/**
	 * Suppression d'une demande d'equipement de salarié
	 *
	 * @param fragmentSource
	 *            L'appelant
	 * @param callbackResources
	 *            Map de resources
	 * @param id
	 *            L'identifiant de la demande d'equipement à supprimer
	 */
	public static void deleteEquipementSalarie( WsCaller fragmentSource, Map<String, Object> callbackResources, Integer id ) {
		AppelWsDeleteEquipementSalarie ws = new AppelWsDeleteEquipementSalarie( fragmentSource, callbackResources, WsName.DELETE_EQUIPEMENT_SALARIE );
		ws.setIdEquipementSalarie( id );
		ws.execute();
	}

	/**
	 * Récupération de la liste des salariés actifs
	 *
	 * @param source
	 *            L'appelant
	 * @param mapRessources
	 *            Map de ressources
	 */
	public static void getListeSalariesActifs( WsCaller source, Map<String, Object> mapRessources ) {
		AppelWsGetListeSalariesActifs ws = new AppelWsGetListeSalariesActifs( source, mapRessources, WsName.GET_LISTE_SALARIES_ACTIFS );
		ws.execute();
	}

	/**
	 * Récupération de la liste des demandes d'equipements de salariés non cloturées
	 *
	 * @param source
	 *            L'appelant
	 * @param mapRessources
	 *            Map de ressources
	 */
	public static void getListeEquipementsSalarieActives( WsCaller source, Map<String, Object> mapRessources ) {
		AppelWsGetListeEquipementsSalarieActives ws = new AppelWsGetListeEquipementsSalarieActives( source, mapRessources, WsName.GET_LISTE_EQUIPEMENTS_SALARIE_ACTIVES );
		ws.execute();
	}

	/**
	 * Récupération de la liste des equipements de salariés
	 *
	 * @param source
	 *            L'appelant
	 * @param mapRessources
	 *            Map de ressources
	 */
	public static void getListeEquipementsSalarieSuivi( WsCaller source, Map<String, Object> mapRessources ) {
		AppelWsGetListeEquipementsSalariePossedes ws = new AppelWsGetListeEquipementsSalariePossedes( source, mapRessources, WsName.GET_LISTE_EQUIPEMENTS_SALARIE_POSSEDES );
		ws.execute();
	}

	/**
	 * Récupération de la liste des horaires d'un salarié
	 *
	 * @param fragmentSource
	 *            L'appelant
	 * @param callbackResources
	 *            Map de resources
	 * @param idUser
	 *            L'identifiant de l'utilisateur
	 * @param annee
	 *            L'année
	 * @param mois
	 *            Le mois
	 */
	public static void getListeHoraires( WsCaller fragmentSource, Map<String, Object> callbackResources, int idUser, int annee, int mois ) {
		Fragment fragment = (Fragment) fragmentSource;
		( (MainActivity) fragment.getActivity() ).showWaitingScreen();

		AppelWsGetListeHorairesByIdUser ws = new AppelWsGetListeHorairesByIdUser( fragmentSource, callbackResources, WsName.GET_LISTE_HORAIRES_BY_ID_USER );
		ws.setIdUser( idUser );
		ws.setAnnee( annee );
		ws.setMois( mois );
		ws.execute();
	}

	/**
	 * Mise à jour d'une horaire salarié
	 *
	 * @param fragmentSource
	 *            L'appelant
	 * @param callbackResources
	 *            Map de ressources
	 * @param horaire
	 *            L'horaire à enregistrer
	 */
	public static void updateHoraire( WsCaller fragmentSource, Map<String, Object> callbackResources, CRMPhoneHorairesDTO horaire ) {
		AppelWsUpdateHoraire ws = new AppelWsUpdateHoraire( fragmentSource, callbackResources, WsName.UPDATE_HORAIRE );
		ws.setHoraire( horaire );
		ws.execute();
	}

	/**
	 * Recuperation de la balance horaire d'un salarié
	 *
	 * @param fragmentSource
	 *            La source
	 * @param callbackResources
	 *            Map de ressources
	 * @param idUser
	 *            L'identifiant de l'utilisateur
	 */
	public static void getBalanceTotaleHoraire( WsCaller fragmentSource, Map<String, Object> callbackResources, Integer idUser ) {
		AppelWsGetBalanceTotaleHoraire ws = new AppelWsGetBalanceTotaleHoraire( fragmentSource, callbackResources, WsName.GET_BALANCE_TOTALE_HORAIRE );
		ws.setIdUser( idUser );
		ws.execute();
	}

	/**
	 * Récupération du fichier des plannings en fonction de la date
	 *
	 * @param source
	 *            La source
	 * @param callbackResources
	 *            Map des ressources
	 * @param date
	 *            La date en cours
	 */
	public static void getPlanningFile( WsCaller source, Map<String, Object> callbackResources, LocalDate date ) {
		AppelWsGetPlanningFile ws = new AppelWsGetPlanningFile( source, callbackResources, WsName.GET_PLANNING_SEMAINE );
		ws.setDate( date );
		ws.execute();
	}

	/**
	 * Récupération de la liste des plannings entre 2 dates
	 *
	 * @param source
	 *            L'appelant
	 * @param callbackResources
	 *            Map des ressources
	 * @param startDate
	 *            la date de début
	 * @param endDate
	 *            La date de fin
	 */
	public static void getListePlanningsBetween( WsCaller source, Map<String, Object> callbackResources, LocalDate startDate, LocalDate endDate ) {
		AppelWsGetListePlanningsBetween ws = new AppelWsGetListePlanningsBetween( source, callbackResources, WsName.GET_LISTE_PLANNINGS_BETWEEN );
		ws.setDateDebut( startDate );
		ws.setDateFin( endDate );
		ws.execute();
	}

	/**
	 * Récupération du salarié à partir de son identifiant utilisateur
	 *
	 * @param source
	 *            L'appelant
	 * @param callBackResources
	 *            Map de ressource
	 * @param idUser
	 *            Identifiant utilisateur
	 */
	public static void getSalarieByIdUser( WsCaller source, Map<String, Object> callBackResources, Integer idUser ) {
		AppelWsGetSalarieByIdUser ws = new AppelWsGetSalarieByIdUser( source, callBackResources, WsName.GET_SALARIE_BY_ID_USER );
		ws.setIdUser( idUser );
		ws.execute();
	}

	/**
	 * Telechargement du fichier du devis
	 *
	 * @param source
	 *            L'appelant
	 * @param mapRessources
	 *            map de ressources
	 * @param fileName
	 *            Le nom du fichier du devis
	 */
	public static void getFileDevis( WsCaller source, Map<String, Object> mapRessources, String fileName ) {
		AppelWsGetDevisFile ws = new AppelWsGetDevisFile( source, mapRessources, WsName.GET_DEVIS_FILE );
		ws.setFileName( fileName );
		ws.execute();
	}

	/**
	 * Récupération de la liste des interventions plannifiées entre 2 dates
	 *
	 * @param source
	 *            L'appelant
	 * @param mapResources
	 *            Map de ressources
	 * @param startDate
	 *            Date de début
	 * @param endDate
	 *            Date de fin
	 */
	public static void getListePlanningInterventionBetween( WsCaller source, Map<String, Object> mapResources, LocalDate startDate, LocalDate endDate ) {
		AppelWsGetListePlanningsInterventionBetween ws = new AppelWsGetListePlanningsInterventionBetween( source, mapResources, WsName.GET_LISTE_PLANNINGS_INTERVENTION_BETWEEN );
		ws.setDateDebut( startDate );
		ws.setDateFin( endDate );
		ws.execute();
	}

	/**
	 * Récupération du détail des informations d'une intervention à partir de
	 * l'identifiant d'un devis
	 *
	 * @param source
	 *            La source
	 * @param mapResources
	 *            Map de ressources
	 * @param idDevis
	 *            L'identifiant du devis
	 */
	public static void getInterventionDetailByIdDevis( WsCaller source, Map<String, Object> mapResources, Integer idDevis ) {
		AppelWsGetInterventionDetails ws = new AppelWsGetInterventionDetails( source, mapResources, WsName.GET_INTERVENTION_DETAIL_BY_ID_DEVIS );
		ws.setIdDevis( idDevis );
		ws.execute();
	}

	/**
	 * Récupération de la liste des dépots vente
	 *
	 * @param source
	 *            La source
	 * @param mapResources
	 *            Map de ressources
	 */
	public static void getListeDepotsVente( WsCaller source, Map<String, Object> mapResources ) {
		AppelWsGetListeDepotsVente ws = new AppelWsGetListeDepotsVente( source, mapResources, WsName.GET_LISTE_DEPOTS_VENTE );
		ws.execute();
	}

	/**
	 * Récupération d'un rapport de chantier
	 *
	 * @param source
	 *            La source
	 * @param mapResources
	 *            Map de ressources
	 * @param date
	 *            La date du rapport de chantier
	 * @param idDevis
	 *            L'identifiant du devis
	 */
	public static void getRapportDeChantier( WsCaller source, Map<String, Object> mapResources, LocalDate date, Integer idDevis ) {
		AppelWsGetRapportDeChantier ws = new AppelWsGetRapportDeChantier( source, mapResources, WsName.GET_RAPPORT_CHANTIER );
		ws.setIdDevis( idDevis );
		ws.setDateDuRapport( date );
		ws.execute();
	}

	/**
	 * Enregistrement d'un rapport de chantier
	 *
	 * @param mapResources
	 * @param rapportChantier
	 *            Le rapport de chantier à enregister
	 */
	public static void saveRapportChantier( WsCaller source, Map<String, Object> mapResources, Map<String, String> mapPhotos, CRMPhoneRapportChantierDTO rapportChantier ) {
		AppelServletUploadPhoto servletUploader = new AppelServletUploadPhoto( source, mapPhotos, WsName.UPLOAD_PHOTO );
		servletUploader.setRapportChantier( rapportChantier );
		servletUploader.execute();

		AppelWsSaveRapportChantier ws = new AppelWsSaveRapportChantier( source, mapResources, WsName.SAVE_RAPPORT_CHANTIER );
		ws.setRapportChantier( rapportChantier );
		ws.execute();
	}

	/**
	 * Récupération de la liste des mises à jour sur les decheteries
	 * 
	 * @param source
	 *            La source
	 * @param mapResources
	 *            Map de ressources
	 * @param version
	 *            Le numéro de version de la carte en cours
	 */
	public static void getDecheteriesUpdates( WsCaller source, Map<String, Object> mapResources, Integer version ) {
		AppelWsGetDecheteriesUpdates ws = new AppelWsGetDecheteriesUpdates( source, mapResources, WsName.GET_DECHETERIES_UPDATES );
		ws.setVersion( version );
		ws.execute();
	}

	/**
	 * Récupération de la liste des rapports de chantier entre 2 dates
	 * 
	 * @param source
	 *            La source
	 * @param mapResources
	 *            Map de ressources
	 * @param dateDebut
	 *            La date de début
	 * @param dateFin
	 *            La date de fin
	 */
	public static void getListePlanningRapport( WsCaller source, Map<String, Object> mapResources, LocalDate dateDebut, LocalDate dateFin ) {
		AppelWsGetPlanningRapport ws = new AppelWsGetPlanningRapport( source, mapResources, WsName.GET_LISTE_RAPPORTS_CHANTIER_PLANNING );
		ws.setDateDebut( dateDebut );
		ws.setDateFin( dateFin );
		ws.execute();
	}

	/**
	 * Récupération de la liste des documents
	 *
	 * @param source
	 *            La source
	 * @param mapResources
	 *            Map de ressources
	 */
	public static void getListeDocuments( WsCaller source, Map<String, Object> mapResources ) {
		AppelWsGetListeDocuments ws = new AppelWsGetListeDocuments( source, mapResources, WsName.GET_DOCUMENTS );
		ws.execute();
	}

	/**
	 * Envoi d'un document par email
	 * 
	 * @param source La source
	 * @param destinataire Le destinataire
	 * @param id L'identifiant du document
	 */
	public static void sendDocument( WsCaller source, String destinataire, Integer id ) {
		AppelWsSendDocument ws = new AppelWsSendDocument( source, null, WsName.SEND_DOCUMENT );
		ws.setDestinataire( destinataire );
		ws.setIdDocument( id );
		ws.execute();
	}
}