package fr.artemis.phone.utils;

import java.time.LocalDate;

/**
 * Classe utilitaire pour les rapports de chantier
 */
public class RapportChantierUtil {

	/**
	 * Récupération du nom de fichier d'un rapport de chantier à partir de la date
	 * et de l'identifiant du devis.
	 * Le nom généré est de la forme "rapport-AAAA-MM-JJ-FKDEVIS".
	 * Où : AAAA est l'année ; MM le mois sur 2 caractères ; JJ le mois sur 2
	 * caractères ; FKDEVIS l'identifiant du devis
	 *
	 * @param dateDuRapport
	 *            La date du rapport
	 * @param fkDevis
	 *            L'identifiant du devis
	 * @return Le nom du fichier qui correspond à ce rapport de chantier
	 */
	public static String getRapportFileName( LocalDate dateDuRapport, Integer fkDevis ) {

		if ( null == dateDuRapport || null == fkDevis ) {

			throw new IllegalStateException( "Impossible de générer un nom de fichier de rapport de chantier sans date ni intervention." );

		} else {

			String day = String.valueOf( dateDuRapport.getDayOfMonth() );
			if ( day.length() == 1 ) {
				day = "0" + day;
			}

			String month = String.valueOf( dateDuRapport.getMonthValue() );
			if ( month.length() == 1 ) {
				month = "0" + month;
			}

			String year = String.valueOf( dateDuRapport.getYear() );

			return "rapport-" + year + "-" + month + "-" + day + "-" + fkDevis + ".json";
		}
	}
}