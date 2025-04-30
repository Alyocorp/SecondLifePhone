package fr.artemis.phone.components.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

/**
 * Wrapper de TextWatcher.
 * Cette classe est utilisée pour analyser les saisies utilisateur.
 * TextWatcher ne permet pas de retrouver la source des evenements d'ou son ecapsulation en y ajoutant une vue (source)
 * Exemple :
 * L'utilisateur saisi une designation dans la ligne 50 du devis :
 * TextWatcher est incapable de savoir que l'evenement provient de la ligne 50...
 * L'ajout de la source permet de retrouver cette ligne
 */
public class TextWatcherWrapper implements TextWatcher {

	// Permet de faire des expressions lambda par encapsulation des différents évenements lancés par TextWatcher
	private OnTextChanged onTextChanged;
	private AfterTextChange afterTextChange;
	private BeforeTextChanged beforeTextChanged;

	// La source des evenements
	private View view;

	public TextWatcherWrapper( AfterTextChange afterTextChange, View view ) {
		this.afterTextChange = afterTextChange;
		this.view = view;
	}

	@Override
	public void beforeTextChanged( CharSequence s, int start, int count, int after ) {
		if ( beforeTextChanged != null ) {
			beforeTextChanged.beforeTextChanged( s, start, count, after );
		}
	}

	@Override
	public void onTextChanged( CharSequence s, int start, int before, int count ) {
		if ( onTextChanged != null ) {
			onTextChanged.onTextChanged( s, start, before, count );
		}
	}

	@Override
	public void afterTextChanged( Editable s ) {
		if ( afterTextChange != null ) {
			afterTextChange.afterTextChanged( s, view );
		}
	}

	/**
	 * Encapsulation de la methode onTextChanged du TextWatcher (permet de coder une expression lambda)
	 */
	public interface OnTextChanged {
		void onTextChanged( CharSequence s, int start, int before, int count );
	}

	/**
	 * Encapsulation de la methode afterTextChanged du TextWatcher (permet de coder une expression lambda)
	 */
	public interface AfterTextChange {
		void afterTextChanged( Editable s, View v );
	}

	/**
	 * Encapsulation de la methode beforeTextChanged du TextWatcher (permet de coder une expression lambda)
	 */
	public interface BeforeTextChanged {
		void beforeTextChanged( CharSequence s, int start, int count, int after );
	}
}