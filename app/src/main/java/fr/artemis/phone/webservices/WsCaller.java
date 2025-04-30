package fr.artemis.phone.webservices;

import androidx.fragment.app.Fragment;

import java.util.Map;

/**
 * Interface de callback pour un appel webservice.
 * Une classe souhaitant appeler un webservice et recevoir une réponse lorsque celle-ci est arrivée doit implémenter cette interface.
 * L'appel d'un webservice se fait au travers de la classe WsUtil. Lorsque le stub du webservice recoit la réponse,
 * il appelle la méthode notifyResponse en lui donnant son nom (webservice) ainsi que le résultat
 */
public interface WsCaller {

	/**
	 * Methode appelé lorsque la réponse du webservice est arrivée
	 *
	 * @param wsName            Le nom du webservice indiquant que la réponse est arrivée
	 * @param callbackResources Map contenant des resources envoyées par l'appelant pour traiter les données une fois reçues.
	 *                          Par exemple une IHM contenant une liste de données désordonnées appelle un WS pour supprimer une donnée à partir de son identifiant.
	 *                          La notification renvoyée à l'IHM est simplement un booleén indiquant si la suppression s'est déroulée correctement.
	 *                          Dans ce cas, il est nécessaire de connaître quelle donnée a été supprimé.
	 *                          Pour se faire, on transmet au webservice l'emplacement de la données supprimée puis le webservice renverra cette emplacement avec sa réponse.
	 *                          Le traitement dans l'IHM pourra se faire ou non sur l'emplacement de la données en fonction de la réponse du webservice
	 * @param response          La réponse du webservice
	 */
	void notifyResponse( WsName wsName, Map<String, Object> callbackResources, Object response );

	/**
	 * Retourne le fragment initiateur de l'appel webservice
	 *
	 * @return Instance du fragment
	 */
	Fragment getFragmentSource();
}