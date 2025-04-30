package fr.artemis.phone.fragments.utilisateur.documents;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.artemis.phone.R;
import fr.artemis.phone.dto.CRMPhoneGEDDocumentDTO;
import fr.artemis.phone.fragments.utilisateur.documents.adapter.DocumentAdapter;
import fr.artemis.phone.utils.Constantes;
import fr.artemis.phone.utils.SessionPhone;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.WsUtil;

/**
 * Fragment de la gestion des documents éléctroniques partagés
 */
public class FragmentUserDocuments extends Fragment implements WsCaller, DocumentAdapter.ItemClickListener {

	// La liste des documents
	private List<CRMPhoneGEDDocumentDTO> listeDocuments;

	// L'adapter
	private DocumentAdapter documentAdapter;

	@BindView( R.id.rv_documents )
	RecyclerView rvDocuments;

	@BindView( R.id.layoutDocumentPopup )
	LinearLayout layoutDocumentPopup;

	@BindView( R.id.et_email )
	EditText etEmail;

	@BindView( R.id.btOpen )
	Button btOpen;

	@BindView( R.id.btSend )
	Button btSend;

	// Le document selectionné par l'utilisateur
	private CRMPhoneGEDDocumentDTO documentSelected;

	@Nullable
	@Override
	public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
		View view = inflater.inflate( R.layout.fragment_documents, container, false );

		ButterKnife.bind( this, view );

		assert null != getActivity();

		initDatas();

		initEvents();

		return view;
	}

	/**
	 * Initialisation des données
	 */
	private void initDatas() {
		WsUtil.getListeDocuments( this, null );
	}

	/**
	 * Initialisation de la vue
	 */
	private void initView() {
		rvDocuments.setLayoutManager( new LinearLayoutManager( getActivity() ) );
		documentAdapter = new DocumentAdapter( getActivity(), listeDocuments );
		documentAdapter.setClickListener( this );
		rvDocuments.setAdapter( documentAdapter );

		layoutDocumentPopup.setVisibility( View.GONE );
	}

	/**
	 * Initialisation des evenements
	 */
	private void initEvents() {
		this.btOpen.setOnClickListener( listener -> openDocument( documentSelected ) );
		this.btSend.setOnClickListener( listener -> sendDocument( etEmail.getText().toString(), documentSelected ) );
	}

	/**
	 * Ouverture d'un document
	 * 
	 * @param document Le document à ouvrir
	 */
	private void openDocument( CRMPhoneGEDDocumentDTO document ) {
		Intent browserIntent = new Intent( Intent.ACTION_VIEW, Uri.parse( document.getUrl() ) );
		startActivity( browserIntent );
	}

	/**
	 * Envoi d'un document
	 * 
	 * @param destinataire Le destinataire
	 * @param document Le document à envoyer
	 */
	private void sendDocument( String destinataire, CRMPhoneGEDDocumentDTO document ) {
		WsUtil.sendDocument( this, destinataire, document.getId() );
	}

	@Override
	public void onItemClick( View view, int position ) {
		documentSelected = documentAdapter.getItem( position );

		layoutDocumentPopup.setVisibility( View.VISIBLE );
		System.out.println( "documentAdapter = " + documentAdapter.getItem( position ) );
	}

	@Override
	public void notifyResponse( WsName wsName, Map<String, Object> callbackResources, Object response ) {
		switch ( wsName ) {
			case GET_DOCUMENTS:
				this.listeDocuments = (List<CRMPhoneGEDDocumentDTO>) response;
				initView();
				break;

			case SEND_DOCUMENT:
				if ( (Boolean) response ) {
					Toast.makeText( getActivity(), "Le message a été envoyé", Toast.LENGTH_LONG ).show();
				} else {
					Toast.makeText( getActivity(), "Erreur lors de l'envoi du message", Toast.LENGTH_LONG ).show();
				}
				break;

			default:
				throw new IllegalStateException( "Reception d'une reponse de webservice provenant de : " + wsName + ". Les données associées sont : " + response );
		}
	}

	@Override
	public Fragment getFragmentSource() {
		return this;
	}
}