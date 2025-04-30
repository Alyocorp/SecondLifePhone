package fr.artemis.phone.utils;

public class ConstantesWS {

	public static final String CHARSET_UTF8 = "UTF-8";

	public static final String NAMESPACE = "http://webservices.portail.artemis.fr/";

	public static final String URL_AUTH = "http://calypso.alyocorp.com:8080/artemis.ws/services/authentification";
	public static final String URL_AUTH_DEV = "http://192.168.1.2:8080/artemis.ws/services/authentification";

	// public static final String DEVIS_URL =
	// "http://calypso.debarras-de-maison.com:8080/artemis.ws/services/crmPhone";
	public static final String URL_DEVIS = "http://calypso.alyocorp.com:8080/artemis.ws/services/crmPhone";

	public static final String URL_CRM_PHONE_DEVMODE = "http://192.168.1.2:8080/artemis.ws/services/crmPhone";

	public static final String URL_CRM_PHONE_EMULATOR = "http://10.0.2.2:8080/artemis.ws/services/crmPhone";

	public static final String URL_CRM_PHONE_SERVER = "http://calypso.alyocorp.com:8080/artemis.ws/services/crmPhone";

	public static final String URL_CRM_UPLOAD_PHOTO_DEVMODE = "http://192.168.1.2:8080/artemis.ws/AndroidPhotoUploader";

	public static final String URL_CRM_UPLOAD_PHOTO_SERVER = "http://calypso.alyocorp.com:8080/artemis.ws/AndroidPhotoUploader";

	public static final String METHOD_AUTH = "authentificationUtilisateur";

	public static final String METHOD_CLIENT_BY_DEVIS = "getClientByIdDevis";

	public static final String METHOD_DELETE_EQUIPEMENT = "deleteEquipement";

	public static final String METHOD_DELETE_EQUIPEMENT_SALARIE = "deleteEquipementSalarie";

	public static final String METHOD_GENERATE_EQUIPEMENT = "generateEmptyEquipement";

	public static final String METHOD_GENERATE_EQUIPEMENT_SALARIE = "generateEmptyEquipementSalarie";

	public static final String METHOD_GET_BALANCE_TOTALE_HORAIRE = "getBalanceTotaleHoraire";

	public static final String METHOD_GET_DEPOTS_VENTE = "getListeDepotsVente";

	public static final String METHOD_GET_KML_DECHETERIES = "downloadKmlFileDecheterie";

	public static final String METHOD_GET_DECHETERIES_UPDATES = "getListeDecheteriesUpdates";

	public static final String METHOD_GET_DOCUMENTS = "getListeDocuments";

	public static final String METHOD_GET_KML_DEPOTS_VENTE = "downloadKmlFileDepotsVente";

	public static final String METHOD_GET_RAPPORT_CHANTIER = "getRapportDeChantier";

	public static final String METHOD_GET_SALARIE_BY_IDUSER = "getSalarieByIdUser";

	public static final String METHOD_GET_VERSION_MAP_DECHETERIE = "getVersionMapDecheterie";

	public static final String METHOD_GET_VERSION_MAP_DEPOTS_VENTE = "getVersionMapDepotsVente";

	public static final String METHOD_INTER_DETAILS = "getInterventionDetailByIdDevis";

	public static final String METHOD_IS_EQUIPEMENT_DELETABLE = "isEquipementDeletable";

	public static final String METHOD_IS_EQUIPEMENT_SALARIE_DELETABLE = "isEquipementSalarieDeletable";

	public static final String METHOD_LISTE_EQUIPEMENTS = "getListeEquipements";

	public static final String METHOD_LISTE_EQUIPEMENTS_SALARIE = "getListeEquipementsSalarieByIdUser";

	public static final String METHOD_LISTE_EQUIPEMENTS_SALARIE_ACTIVES = "getListeEquipementsSalarieActives";

	public static final String METHOD_LISTE_EQUIPEMENTS_SALARIE_POSSEDES = "getListeEquipementsSalariePossedes";

	public static final String METHOD_LISTE_PLANNING_INTER_BETWEEN = "getListePlanningInterventionBetween";

	public static final String METHOD_LISTE_HORAIRES_BY_MONTH = "getListeHorairesByMonth";

	public static final String METHOD_LISTE_PHONE_SALARIES = "getListePhoneSalaries";

	public static final String METHOD_LISTE_PLANNING_BETWEEN = "getListePlanningBetween";

	public static final String METHOD_LISTE_PLANNING_RAPPORTS_BETWEEN = "getListeRapportsChantierPlanningBetween";

	public static final String METHOD_LISTE_TYPES_TAILLES = "getListeTypesTailles";

	public static final String METHOD_SAVE_DECHETERIE = "saveDecheterie";

	public static final String METHOD_SAVE_DEPOT_VENTE = "saveDepotVente";

	public static final String METHOD_SAVE_EQUIPEMENT = "saveEquipement";

	public static final String METHOD_SAVE_RAPPORT_CHANTIER = "saveRapportDeChantier";

	public static final String METHOD_SEND_DOCUMENT = "sendDocument";

	public static final String METHOD_UPDATE_HORAIRES = "updateHoraire";

	public static final String PARAM_WS_ANNEE = "annee";

	public static final String PARAM_WS_DATE_DEBUT = "dateDebut";

	public static final String PARAM_WS_DATE_FIN = "dateFin";

	public static final String PARAM_WS_DECHETERIE = "decheterie";

	public static final String PARAM_WS_DEPOT_VENTE = "depotVente";

	public static final String PARAM_WS_DESTINATAIRE = "destinataire";

	public static final String PARAM_WS_EQUIPEMENT = "equipement";

	public static final String PARAM_WS_EQUIPEMENT_SALARIE = "equipementSalarie";

	public static final String PARAM_WS_HORAIRE = "horaire";

	public static final String PARAM_WS_ID_DEVIS = "idDevis";

	public static final String PARAM_WS_ID_DOCUMENT = "idDocument";

	public static final String PARAM_WS_ID_EQUIPEMENT = "idEquipement";

	public static final String PARAM_WS_ID_EQUIPEMENT_SALARIE = "idEquipementSalarie";

	public static final String PARAM_WS_ID_USER = "idUser";

	public static final String PARAM_WS_MOIS = "mois";

	public static final String PARAM_WS_PASSWORD = "password";

	public static final String PARAM_WS_RAPPORT = "rapport";

	public static final String PARAM_WS_USERNAME = "userName";

	public static final String PARAM_WS_VERSION_DECHETERIES = "version";
}