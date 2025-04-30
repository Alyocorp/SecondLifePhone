package fr.artemis.phone.utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Classe utilisateur concernant le matériel
 */
public class PhoneUtil {

	/**
	 * Détecte si l'appreil en cours d'utilisation est un émulateur ou non
	 *
	 * @return oui si appareil émulé
	 */
	public static boolean isEmulator() {
		return ( Build.BRAND.startsWith( "generic" ) && Build.DEVICE.startsWith( "generic" ) )
				|| Build.FINGERPRINT.startsWith( "generic" )
				|| Build.FINGERPRINT.startsWith( "unknown" )
				|| Build.HARDWARE.contains( "goldfish" )
				|| Build.HARDWARE.contains( "ranchu" )
				|| Build.MODEL.contains( "google_sdk" )
				|| Build.MODEL.contains( "Emulator" )
				|| Build.MODEL.contains( "Android SDK built for x86" )
				|| Build.MANUFACTURER.contains( "Genymotion" )
				|| Build.PRODUCT.contains( "sdk_google" )
				|| Build.PRODUCT.contains( "google_sdk" )
				|| Build.PRODUCT.contains( "sdk" )
				|| Build.PRODUCT.contains( "sdk_x86" )
				|| Build.PRODUCT.contains( "vbox86p" )
				|| Build.PRODUCT.contains( "emulator" )
				|| Build.PRODUCT.contains( "simulator" );
	}

	/**
	 * Permet de cacher le clavier logiciel
	 *
	 * @param activity L'activité de l"application concernée
	 */
	public static void hideKeyboard( Activity activity ) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService( Activity.INPUT_METHOD_SERVICE );
		//Find the currently focused view, so we can grab the correct window token from it.
		View view = activity.getCurrentFocus();
		//If no view currently has focus, create a new one, just so we can grab a window token from it
		if ( view == null ) {
			view = new View( activity );
		}
		imm.hideSoftInputFromWindow( view.getWindowToken(), 0 );
	}
}