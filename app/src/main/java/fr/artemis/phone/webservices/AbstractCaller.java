package fr.artemis.phone.webservices;

import android.os.AsyncTask;

import androidx.fragment.app.Fragment;

import java.util.Map;

import fr.artemis.phone.activities.main.MainActivity;

/**
 * Classe abstraite d'appel asynchrone.
 * Cette classe permet d'effectuer des taches asynchrones et de les renvoyer aux instances ayant appelé cette tache
 *
 * @param <T> Le type de résultat attendu
 */
public abstract class AbstractCaller<T> extends AsyncTask<String, String, T> {

	// La source de l'appel
	protected WsCaller source;

	// Map de resources fournit pas l'appelant qui sera retournée accompagnée du résultat
	protected Map<String, Object> mapResources;

	// Le nom du webservice appelé
	protected WsName name;

	/**
	 * Constructeur obligatoire
	 *
	 * @param source       L'appelant de la tâche asynchrone qui attend le résultat
	 * @param mapResources Map contenant des resources nécessaires aux futurs traitements de l'appelant
	 * @param name         Le nom de la tache asynchrone (permet d'identifier qui répond lorsques plusieurs taches sont lancées en meme temps)
	 */
	public AbstractCaller( WsCaller source, Map<String, Object> mapResources, WsName name ) {
		this.source = source;
		this.mapResources = mapResources;
		this.name = name;
	}

	@Override
	protected void onPreExecute() {
		try {
			if ( null != source ) {
				// Affichage d'un ecran de chargement sur l'activité principale
				( (MainActivity) this.source.getFragmentSource().getActivity() ).showWaitingScreen();
			}
		} catch ( ClassCastException ex ) {
			ex.printStackTrace();
			throw new IllegalStateException( "Obligation d'hériter de la methode onPreExecute étant donnée que la source n'est pas un fragment." );
		}
	}

	@Override
	protected void onProgressUpdate( String... values ) {
	}

	@Override
	protected T doInBackground( String... params ) {
		return executionRequete();
	}

	@Override
	protected void onPostExecute( T result ) {
		try {
			if ( null != source ) {
				source.notifyResponse( name, mapResources, result );
				// Suppression de l'ecran de chargement
				( (MainActivity) this.source.getFragmentSource().getActivity() ).removeWaitingScreen();
			}
		} catch ( ClassCastException ex ) {
			ex.printStackTrace();
			throw new IllegalStateException( "Obligation d'hériter de la methode onPreExecute étant donnée que la source n'est pas un fragment." );
		}
	}

	/**
	 * Methode comportant la tache a effectuer en asynchrone
	 *
	 * @return Le resultat de la tache qui sera transmis ensuite à l'appelant
	 */
	protected abstract T executionRequete();
}