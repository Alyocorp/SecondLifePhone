package fr.artemis.phone.utils;

import java.math.BigDecimal;

public class NumericUtils {

	/**
	 * Methode de transformation d'une chaine de caractère en nombre à virgule
	 *
	 * @param value La valeur à transformer en nombre à virgule
	 * @return Le nombre à virgule correspondant à la chaîne de caractère
	 */
	public static BigDecimal stringToBigDecimal( String value ) {
		try {
			boolean negative = false;
			// 2 lignes identiques mais l'espace de la deuxieme ligne doit probablement etre
			// un caractère ASCII différent étant donné que lors du passage sur la première
			// ligne, l'espace n'est pas supprimé
			value = value.replace( " ", "" );
			value = value.replace( " ", "" );
			if ( value.startsWith( "-" ) ) {
				value = value.replace( "-", "" );
				negative = true;
			}
			if ( null != value && !"".equals( value ) ) {
				String newValue = value.replace( ",", "." );
				BigDecimal bigD = new BigDecimal( newValue );
				if ( negative ) {
					bigD = bigD.negate();
				}
				return bigD;
			} else {
				return BigDecimal.ZERO;
			}
		} catch ( NumberFormatException ex ) {
			throw new IllegalArgumentException( value + " ne peux pas être transformé en BigDecimal." );
		}
	}
}
