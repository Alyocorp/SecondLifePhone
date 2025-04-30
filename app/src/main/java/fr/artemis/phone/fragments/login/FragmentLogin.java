package fr.artemis.phone.fragments.login;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.artemis.phone.R;
import fr.artemis.phone.activities.main.MainActivity;
import fr.artemis.phone.utils.Constantes;
import fr.artemis.phone.utils.ConstantesKeys;
import fr.artemis.phone.utils.ConstantesMaps;
import fr.artemis.phone.utils.StorageUtil;

/**
 * Fragment de l'authentification utilisateur
 */
public class FragmentLogin extends Fragment {

	@BindView( R.id.etPassword )
	EditText etPassword;

	@BindView( R.id.etUsername )
	EditText etUsername;

	@BindView( R.id.btAuthenticate )
	Button btAuthenticate;

	@Nullable
	@Override
	public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
		View view = inflater.inflate( R.layout.fragment_login, container, false );

		ButterKnife.bind( this, view );

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

		assert null != getActivity();

		try {
			String username = (String) StorageUtil.readObjectInternal( getContext(), ConstantesKeys.KEY_USERNAME.getKeyName() );
			String password = (String) StorageUtil.readObjectInternal( getContext(), ConstantesKeys.KEY_PASSWORD.getKeyName() );

			if ( null != username ) {
				this.etUsername.setText( username );
			}

			if ( null != password ) {
				this.etPassword.setText( password );
			}

			( (MainActivity) getActivity() ).initEventsInFragAuth();
		} catch( Exception ex ) {
			Log.e( "ERROR", "onResume: Erreur lors de la lecture des champs" );
			ex.printStackTrace();
		}
	}

	/**
	 * Retourne le bouton d'authentification.
	 * Le fragment sert d'affichage de formulaire, c'est l'activité principale
	 * (MainActivity)
	 * qui se charge d'effectuer l'authentification et de remplacer le fragment à
	 * afficher.
	 * C'est pourquoi un listener est injecté dans le le bouton à partir de
	 * MainActivity
	 *
	 * @return Le bouton d'authentification
	 */
	public Button getBtAuthenticate() {
		return btAuthenticate;
	}

	/**
	 * Retourne le nom d'utilisateur saisi par l'utilisateur
	 *
	 * @return Le nom d'utilisateur
	 */
	public String getUsername() {
		return etUsername.getText().toString();
	}

	/**
	 * Retourne le mot de passe saisie par l'utilisateur
	 *
	 * @return Le mot de passe saisi par l'utilisateur
	 */
	public String getPassword() {
		return etPassword.getText().toString();
	}

	/**
	 * Réinitialisation de l'IHM
	 */
	public void resetView() {
		this.etUsername.setError( null );
		this.etPassword.setError( null );
		this.etUsername.setText( "" );
		this.etPassword.setText( "" );
	}

	/**
	 * Affichage d'erreur de l'authentification
	 */
	public void showAuthError() {
		this.etUsername.setError( "Erreur" );
		this.etPassword.setError( "Erreur" );
	}
}