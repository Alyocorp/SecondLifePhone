package fr.artemis.phone.utils;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Classe utilitaire d'appreils Android
 */
public class AndroidUtils {

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
