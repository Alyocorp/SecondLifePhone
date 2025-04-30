package fr.artemis.phone.activities.main;

import java.time.LocalDate;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.artemis.phone.R;
import fr.artemis.phone.dto.CRMPhonePlanningInterventionDTO;
import fr.artemis.phone.dto.RoleUser;
import fr.artemis.phone.dto.SalarieLightDTO;
import fr.artemis.phone.dto.UtilisateurDTO;
import fr.artemis.phone.fragments.FragmentProvider;
import fr.artemis.phone.fragments.FragmentTags;
import fr.artemis.phone.fragments.login.FragmentLogin;
import fr.artemis.phone.fragments.responsable.accueil.FragmentRespAccueil;
import fr.artemis.phone.fragments.responsable.equipements.accueil.FragmentRespEquipementAccueil;
import fr.artemis.phone.fragments.responsable.equipements.demandes.FragmentRespEquipementDemande;
import fr.artemis.phone.fragments.responsable.equipements.liste.FragmentRespEquipementListe;
import fr.artemis.phone.fragments.responsable.equipements.suivi.FragmentRespEquipementSuivi;
import fr.artemis.phone.fragments.responsable.rapports.FragmentRespRapportChantier;
import fr.artemis.phone.fragments.responsable.rapports.subfragments.global.FragmentRespRapportChantierGlobal;
import fr.artemis.phone.fragments.responsable.rapports.subfragments.incoherence.FragmentRespRapportChantierIncoherence;
import fr.artemis.phone.fragments.responsable.rapports.subfragments.intervention.FragmentRespRapportChantierIntervention;
import fr.artemis.phone.fragments.responsable.rapports.subfragments.salarie.FragmentRespRapportChantierSalarie;
import fr.artemis.phone.fragments.utilisateur.accueil.FragmentUserAccueil;
import fr.artemis.phone.fragments.utilisateur.decheterie.FragmentDecheterie;
import fr.artemis.phone.fragments.utilisateur.depotsvente.FragmentDepotsVente;
import fr.artemis.phone.fragments.utilisateur.documents.FragmentUserDocuments;
import fr.artemis.phone.fragments.utilisateur.equipements.FragmentUserEquipement;
import fr.artemis.phone.fragments.utilisateur.horaires.FragmentUserHoraires;
import fr.artemis.phone.fragments.utilisateur.planning.accueil.FragmentPlanningAccueil;
import fr.artemis.phone.fragments.utilisateur.planning.semaine.FragmentPlanningSemaine;
import fr.artemis.phone.fragments.utilisateur.planning.synchronise.FragmentPlanningSynchronise;
import fr.artemis.phone.fragments.utilisateur.rapportchantier.rapport.FragmentRapportChantier;
import fr.artemis.phone.fragments.utilisateur.rapportchantier.selection.FragmentSelectionRapportChantier;
import fr.artemis.phone.utils.ConstantesKeys;
import fr.artemis.phone.utils.ConstantesMenu;
import fr.artemis.phone.utils.SessionPhone;
import fr.artemis.phone.utils.StorageUtil;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.WsUtil;

/**
 * Activité principale
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, WsCaller {

	private String[] permissions = { Manifest.permission.CALL_PHONE, Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.READ_EXTERNAL_STORAGE };

	@BindView( R.id.toolbar )
	Toolbar toolbar;

	@BindView( R.id.dynamic_fragment_view )
	FrameLayout mainFrame;

	@BindView( R.id.drawer_layout )
	DrawerLayout drawerLayout;

	@BindView( R.id.navigationView )
	NavigationView navigationView;

	@BindView( R.id.layoutWaiting )
	RelativeLayout layoutWaiting;

	// Le fragment affiché
	private Fragment currentFragment;

	// Flag permettant de casser la mauvaise gestion des menus d'Android
	private boolean estCeQuAndroidAFiniDeFaireSonBordel = false;

	// Item de l'actionbar (bouton) permettant d'authentifier ou de déconnecter un
	// utilisateur suivant le statut d'authentification
	private MenuItem itemLog = null;

	// Item du nom d'utilisateur affiché dans la toolbar
	private MenuItem itemUser = null;

	// Item de fermeture de l'application
	private MenuItem itemClose = null;

	// Verrouillage de l'application tant que la connectivité Internet n'a pas été
	// testé
	// volatile puisque le verrou est mis à jour par le thread de verification de la
	// connectivité et que son statut est lu par le thread de l'app Android
	private volatile boolean verrou = false;

	private boolean firstLoad = true;

	/**
	 * Initialisation de l'IHM
	 *
	 * @param savedInstanceState
	 *            Etat de l'instance en cas de restauration
	 *            (reouverture l'IHM)
	 */
	@Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );

		// Injection de l'activité dans la vue
		setContentView( R.layout.activity_main );

		initFragments();

		// Bindings
		ButterKnife.bind( this );

		// Injection de la toolbar dans l'activité
		setSupportActionBar( toolbar );

		// Injection du menu de navigation
		NavigationView navigationView = findViewById( R.id.navigationView );
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
		drawerLayout.addDrawerListener( toggle );
		toggle.syncState();
		navigationView.setNavigationItemSelectedListener( this );

		showLoginView();

		for ( String permission : permissions ) {
			if ( ContextCompat.checkSelfPermission( getApplicationContext(), permission ) != PackageManager.PERMISSION_GRANTED ) {
				ActivityCompat.requestPermissions( this, permissions, 1 );
			}
		}

		// Create thumbnails and dump it to local storage
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize( size );
		int screenWidth = size.x;
		int screenHeight = size.y;

		SessionPhone.getInstance().setFilePath( getCacheDir().getPath() + "/" );
		SessionPhone.getInstance().setScreenWidth( screenWidth );
		SessionPhone.getInstance().setScreenHeight( screenHeight );
	}

	/**
	 * Injection de la barre de menu à la place de la barre de menu par défaut
	 * d'Android
	 *
	 * @param menu
	 *            La barre de menu Android par défaut
	 * @return Peu importe, super.onCreateOptionsMenu renvoi une exception...
	 */
	@Override
	public boolean onCreateOptionsMenu( Menu menu ) {

		// Initialisation du menu (ou réinitialisation inutile de la part d'Android)
		getMenuInflater().inflate( R.menu.app_state, menu );

		for ( int indexMenu = 0 ; indexMenu < menu.size() ; indexMenu++ ) {
			if ( menu.getItem( indexMenu ).getTitle().equals( ConstantesMenu.MENU_LOG ) ) {
				itemLog = menu.getItem( indexMenu );
			} else if ( menu.getItem( indexMenu ).getTitle().equals( ConstantesMenu.MENU_USER_LOGGED ) ) {
				itemUser = menu.getItem( indexMenu );
			} else if ( menu.getItem( indexMenu ).getTitle().equals( ConstantesMenu.MENU_CLOSE ) ) {
				itemClose = menu.getItem( indexMenu );
			}
		}
		if ( null != itemLog && null != itemUser && null != itemClose ) {
			initEvents();
		}

		if ( estCeQuAndroidAFiniDeFaireSonBordel ) {
			if ( SessionPhone.getInstance().isAuthenticated() ) {
				updateLoginToLogged();
			} else {
				updateLoggedToLogin();
			}
		}

		estCeQuAndroidAFiniDeFaireSonBordel = true;

		toolbar.setTitle( R.string.ariane_accueil );

		return true;
	}

	/**
	 * Listeners dans la barre de menu
	 *
	 * @param item
	 *            L'item selectionné par l'utilisateur
	 * @return Vrai pour afficher la selection utlisateur
	 */
	@Override
	public boolean onNavigationItemSelected( @NonNull MenuItem item ) {
		int id = item.getItemId();

		// Si l'utilisateur n'est pas authentifié, on le redirige vers le fragment
		// d'authentification
		// Sinon on le laisse naviguer
		if ( !SessionPhone.getInstance().isAuthenticated() ) {
			showLoginView();
		} else {
			switch ( id ) {
				case R.id.nav_home_resp :
				case R.id.nav_home_user :

					drawerLayout.closeDrawer( GravityCompat.START, true );
					toolbar.setTitle( R.string.ariane_accueil );

					showAccueil();

					return true;

				case R.id.nav_decheteries :
				case R.id.nav_decheteries_resp_user :

					showWaitingScreen();

					drawerLayout.closeDrawer( GravityCompat.START, true );
					toolbar.setTitle( R.string.ariane_dechetteries );

					showDecheteries();

					return true;

				case R.id.nav_depots_vente :
				case R.id.nav_depots_vente_resp_user :

					showWaitingScreen();

					drawerLayout.closeDrawer( GravityCompat.START, true );
					toolbar.setTitle( R.string.ariane_depots_vente );

					showDepotsVente();

					return true;

				case R.id.nav_equipements_resp :

					showWaitingScreen();

					drawerLayout.closeDrawer( GravityCompat.START, true );
					toolbar.setTitle( R.string.ariane_equipements );

					showEquipementsRespAccueil();

					removeWaitingScreen();

					return true;

				case R.id.nav_equipements :
				case R.id.nav_equipements_resp_user :

					showWaitingScreen();

					drawerLayout.closeDrawer( GravityCompat.START, true );
					toolbar.setTitle( R.string.ariane_equipements );

					showEquipementsUserAccueil();

					return true;

				case R.id.nav_horaires :
				case R.id.nav_horaires_resp_user :

					showWaitingScreen();

					drawerLayout.closeDrawer( GravityCompat.START, false );
					toolbar.setTitle( R.string.ariane_horaires );

					showUserHoraires();

					return true;

				case R.id.nav_planning :
				case R.id.nav_planning_resp_user :

					drawerLayout.closeDrawer( GravityCompat.START, false );
					toolbar.setTitle( R.string.ariane_planning );

					showPlanningAccueil();

					return true;

				case R.id.nav_rapport_chantier :
				case R.id.nav_rapport_chantier_resp_user :

					drawerLayout.closeDrawer( GravityCompat.START, false );
					toolbar.setTitle( R.string.ariane_rapport );

					showRapportChantier();

					return true;

				case R.id.nav_rapports_resp :

					drawerLayout.closeDrawer( GravityCompat.START, false );
					toolbar.setTitle( R.string.ariane_rapport );

					showRapportChantierResp();

					return true;

				case R.id.nav_documents :
				case R.id.nav_documents_resp_user :

					drawerLayout.closeDrawer( GravityCompat.START, false );
					toolbar.setTitle( R.string.ariane_documents );

					showDocuments();

					return true;
			}
		}

		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();

		LocalDate dateSession = SessionPhone.getInstance().getDateOuvertureSession();
		if ( null != dateSession && !dateSession.isEqual( LocalDate.now() ) ) {
			new AlertDialog.Builder( this ).setIcon( android.R.drawable.ic_dialog_alert ).setTitle( "ERREUR" ).setMessage( "Session terminée, veuillez vous reconnecter" )
					.setPositiveButton( "OK", null ).show();
			showLoginView();
		}

		// A la fin de l'affichage, on lance un thread qui vérifie l'état de la
		// connexion Internet
		checkInternetConnection();

		// On lance un autre thread pour savoir si la connexion passe par le réseau
		// local Art-Emis
		checkLocalConnection();

		while( !verrou ) {}

		if ( SessionPhone.getInstance().isConnected() && firstLoad ) {
			// On vérifie si on doit mettre l'application à jour
			try {
				new AppUpdater( this ).setUpdateFrom( UpdateFrom.JSON ).setUpdateJSON( "https://www.debarras-de-maison.com/crmPhone2/update.json" ).start();
			} catch ( Exception ex ) {
				ex.printStackTrace();
				FirebaseCrashlytics.getInstance().recordException( ex );
			}
			firstLoad = false;
		}

		Log.i( "CONNEXION", "Connexion testée : " + SessionPhone.getInstance().isConnected() );
	}

	/**
	 * Vérification si on est sur le réseau local Art-Emis
	 */
	private void checkLocalConnection() {
		Timer timer = new Timer();
		timer.schedule( new TimerTask() {

			@Override
			public void run() {
				try {
					WifiManager wifiManager = (WifiManager) MainActivity.this.getSystemService( Context.WIFI_SERVICE );
					WifiInfo info = wifiManager.getConnectionInfo();
					String ssid = info.getSSID();

					SessionPhone.getInstance().setArtemisConnection( ssid.equals( "\"Livebox-eb30\"" ) );
				} catch ( Exception ex ) {
					FirebaseCrashlytics.getInstance().recordException( ex );
					ex.printStackTrace();
					throw new IllegalStateException( "Erreur lors de la vérification de la connectivité." );
				}
			}
		}, 0, 1000 );
	}

	/**
	 * Vérification de la connection Internet
	 */
	private void checkInternetConnection() {

		// Lancement de la vérification de la connection Internet toutes les 3 secondes
		Timer timer = new Timer();
		timer.schedule( new TimerTask() {

			@Override
			public void run() {

				try {
					// Récupération du module réseau d'Android
					ConnectivityManager manager = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );

					// Récupération des différents réseaux
					Network[] networks = manager.getAllNetworks();

					boolean connected = false;

					// Parcours des réseaux afin de trouver une connection Internet
					for ( Network network : networks ) {
						NetworkInfo networkInfo = manager.getNetworkInfo( network );
						if ( networkInfo.isConnected() ) {
							connected = true;
						}
					}

					// Mise en session de l'état de la connexion Internet
					SessionPhone.getInstance().setConnected( connected );

					// Modification de l'état du verrou uniquement pour le premier test
					if ( !verrou ) {
						verrou = true;
					}
				} catch ( Exception ex ) {
					FirebaseCrashlytics.getInstance().recordException( ex );
					ex.printStackTrace();
					throw new IllegalStateException( "Erreur lors de la vérification de la connectivité." );
				}
			}
		}, 0, 3000 );
	}

	/**
	 * Affichage des rapports de chantier d'un responsable
	 */
	private void showRapportChantierResp() {
		if ( null != currentFragment && !currentFragment.equals( FragmentProvider.getFragment( FragmentTags.RAPPORT_RESP.getTagName() ) ) ) {

			// Nettoyage des fragments bousillés par Android
			handleFragmentLifecycleInAProperWay();

			FragmentRespRapportChantier fragment = new FragmentRespRapportChantier();
			FragmentProvider.addFragment( FragmentTags.RAPPORT_RESP.getTagName(), fragment );
			FragmentProvider.addFragment( FragmentTags.RAPPORT_RESP_GLOBAL.getTagName(), new FragmentRespRapportChantierGlobal() );
			FragmentProvider.addFragment( FragmentTags.RAPPORT_RESP_INTERVENTION.getTagName(), new FragmentRespRapportChantierIntervention() );
			FragmentProvider.addFragment( FragmentTags.RAPPORT_RESP_SALARIE.getTagName(), new FragmentRespRapportChantierSalarie() );
			FragmentProvider.addFragment( FragmentTags.RAPPORT_RESP_INCOHERENCE.getTagName(), new FragmentRespRapportChantierIncoherence() );

			// Insertion du fragment dans Android
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add( mainFrame.getId(), fragment ).addToBackStack( FragmentTags.RAPPORT_RESP.getTagName() );
			ft.commit();

			currentFragment = fragment;
		}
	}

	/**
	 * Affichage des rapports de chantier
	 */
	private void showRapportChantier() {
		if ( null != currentFragment && !currentFragment.equals( FragmentProvider.getFragment( FragmentTags.SELECTION_RAPPORT_CHANTIER.getTagName() ) ) ) {

			// Nettoyage des fragments bousillés par Android
			handleFragmentLifecycleInAProperWay();

			FragmentSelectionRapportChantier fragment = new FragmentSelectionRapportChantier();
			FragmentProvider.addFragment( FragmentTags.SELECTION_RAPPORT_CHANTIER.getTagName(), fragment );

			// Insertion du fragment dans Android
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add( mainFrame.getId(), fragment ).addToBackStack( FragmentTags.SELECTION_RAPPORT_CHANTIER.getTagName() );
			ft.commit();

			currentFragment = fragment;
		}
	}

	/**
	 * Affichage de la gestion des equipements pour un utilisateur
	 */
	private void showEquipementsUserAccueil() {
		if ( null != currentFragment && !currentFragment.equals( FragmentProvider.getFragment( FragmentTags.EQUIPEMENTS_USER.getTagName() ) ) ) {

			// Nettoyage des fragments bousillés par Android
			handleFragmentLifecycleInAProperWay();

			FragmentUserEquipement fragment = new FragmentUserEquipement();
			FragmentProvider.addFragment( FragmentTags.EQUIPEMENTS_USER.getTagName(), fragment );

			// Insertion du fragment dans Android
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add( mainFrame.getId(), fragment ).addToBackStack( FragmentTags.EQUIPEMENTS_USER.getTagName() );
			ft.commit();

			currentFragment = fragment;
		}
	}

	/**
	 * Affichage du module d'horaires de travail pour un utilisateur
	 */
	private void showUserHoraires() {
		if ( null != currentFragment && !currentFragment.equals( FragmentProvider.getFragment( FragmentTags.HORAIRES_USER.getTagName() ) ) ) {

			// Nettoyage des fragments bousillés par Android
			handleFragmentLifecycleInAProperWay();

			FragmentUserHoraires fragment = new FragmentUserHoraires();
			FragmentProvider.addFragment( FragmentTags.HORAIRES_USER.getTagName(), fragment );

			// Insertion du fragment dans Android
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add( mainFrame.getId(), fragment ).addToBackStack( FragmentTags.HORAIRES_USER.getTagName() );
			ft.commit();

			currentFragment = fragment;
		}
	}

	/**
	 * Affichage d'un rapport de chantier
	 *
	 * @param dateDuRapport
	 *            La date concernée par le rapport de chantier
	 * @param inter
	 *            L'intervention associée au rapport de chantier
	 */
	public void showRapportChantier( LocalDate dateDuRapport, CRMPhonePlanningInterventionDTO inter ) {

		if ( null != currentFragment && !currentFragment.equals( FragmentProvider.getFragment( FragmentTags.RAPPORT_CHANTIER.getTagName() ) ) ) {
			// Nettoyage des fragments bousillés par Android
			handleFragmentLifecycleInAProperWay();

			FragmentRapportChantier fragment = new FragmentRapportChantier();
			fragment.setIntervention( inter );
			fragment.setDateRapport( dateDuRapport );

			FragmentProvider.addFragment( FragmentTags.RAPPORT_CHANTIER.getTagName(), fragment );

			// Insertion du fragment dans Android
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add( mainFrame.getId(), fragment ).addToBackStack( FragmentTags.RAPPORT_CHANTIER.getTagName() );
			ft.commit();

			currentFragment = fragment;
		}
	}

	/**
	 * Affichage de la gestion des equipements pour le responsable
	 */
	private void showEquipementsRespAccueil() {
		if ( null != currentFragment && !currentFragment.equals( FragmentProvider.getFragment( FragmentTags.EQUIPEMENTS_RESP_ACCUEIL.getTagName() ) ) ) {

			// Nettoyage des fragments bousillés par Android
			handleFragmentLifecycleInAProperWay();

			FragmentRespEquipementAccueil fragment = new FragmentRespEquipementAccueil();
			FragmentProvider.addFragment( FragmentTags.EQUIPEMENTS_RESP_ACCUEIL.getTagName(), fragment );

			// Insertion du fragment dans Android
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add( mainFrame.getId(), fragment ).addToBackStack( FragmentTags.EQUIPEMENTS_RESP_ACCUEIL.getTagName() );
			ft.commit();

			currentFragment = fragment;
		}
	}

	/**
	 * Affichage de la gestion des documents
	 */
	public void showDocuments() {
		if ( null != currentFragment && !currentFragment.equals( FragmentProvider.getFragment( FragmentTags.DOCUMENTS.getTagName() ) ) ) {

			// Nettoyage des fragments bousillés par Android
			handleFragmentLifecycleInAProperWay();

			FragmentUserDocuments fragment = new FragmentUserDocuments();
			FragmentProvider.addFragment( FragmentTags.DOCUMENTS.getTagName(), fragment );

			// Insertion du fragment dans Android
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add( mainFrame.getId(), fragment ).addToBackStack( FragmentTags.DOCUMENTS.getTagName() );
			ft.commit();

			currentFragment = fragment;
		}
	}

	/**
	 * Affichage de la gestion des equipements
	 */
	public void showEquipementsRespListe() {
		if ( null != currentFragment && !currentFragment.equals( FragmentProvider.getFragment( FragmentTags.EQUIPEMENTS_RESP_LISTE.getTagName() ) ) ) {

			// Nettoyage des fragments bousillés par Android
			handleFragmentLifecycleInAProperWay();

			FragmentRespEquipementListe fragment = new FragmentRespEquipementListe();
			FragmentProvider.addFragment( FragmentTags.EQUIPEMENTS_RESP_LISTE.getTagName(), fragment );

			// Insertion du fragment dans Android
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add( mainFrame.getId(), fragment ).addToBackStack( FragmentTags.EQUIPEMENTS_RESP_LISTE.getTagName() );
			ft.commit();

			currentFragment = fragment;
		}
	}

	/**
	 * Affichage de la gestion des demandes d'equipements
	 */
	public void showEquipementsRespDemandes() {
		if ( null != currentFragment && !currentFragment.equals( FragmentProvider.getFragment( FragmentTags.EQUIPEMENTS_RESP_DEMANDES.getTagName() ) ) ) {

			// Nettoyage des fragments bousillés par Android
			handleFragmentLifecycleInAProperWay();

			FragmentRespEquipementDemande fragment = new FragmentRespEquipementDemande();
			FragmentProvider.addFragment( FragmentTags.EQUIPEMENTS_RESP_DEMANDES.getTagName(), fragment );

			// Insertion du fragment dans Android
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add( mainFrame.getId(), fragment ).addToBackStack( FragmentTags.EQUIPEMENTS_RESP_DEMANDES.getTagName() );
			ft.commit();

			currentFragment = fragment;
		}
	}

	/**
	 * Affichage du suivi des equipements
	 */
	public void showEquipementsRespSuivi() {
		if ( null != currentFragment && !currentFragment.equals( FragmentProvider.getFragment( FragmentTags.EQUIPEMENTS_RESP_SUIVI.getTagName() ) ) ) {

			// Nettoyage des fragments bousillés par Android
			handleFragmentLifecycleInAProperWay();

			FragmentRespEquipementSuivi fragment = new FragmentRespEquipementSuivi();
			FragmentProvider.addFragment( FragmentTags.EQUIPEMENTS_RESP_SUIVI.getTagName(), fragment );

			// Insertion du fragment dans Android
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add( mainFrame.getId(), fragment ).addToBackStack( FragmentTags.EQUIPEMENTS_RESP_SUIVI.getTagName() );
			ft.commit();

			currentFragment = fragment;
		}
	}

	/**
	 * Affichage de la carte des decheteries
	 */
	private void showDecheteries() {
		if ( null != currentFragment && !currentFragment.equals( FragmentProvider.getFragment( FragmentTags.DECHETERIES.getTagName() ) ) ) {

			// Nettoyage des fragments bousillés par Android
			handleFragmentLifecycleInAProperWay();

			FragmentDecheterie fragment = new FragmentDecheterie();
			FragmentProvider.addFragment( FragmentTags.DECHETERIES.getTagName(), fragment );

			// Insertion du fragment dans Android
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add( mainFrame.getId(), fragment ).addToBackStack( FragmentTags.DECHETERIES.getTagName() );
			ft.commit();

			currentFragment = fragment;
		}
	}

	/**
	 * Affichage de la carte des dépots vente
	 */
	private void showDepotsVente() {
		if ( null != currentFragment && !currentFragment.equals( FragmentProvider.getFragment( FragmentTags.DEPOTS_VENTE.getTagName() ) ) ) {

			// Nettoyage des fragments bousillés par Android
			handleFragmentLifecycleInAProperWay();

			FragmentDepotsVente fragment = new FragmentDepotsVente();
			FragmentProvider.addFragment( FragmentTags.DEPOTS_VENTE.getTagName(), fragment );

			// Insertion du fragment dans Android
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add( mainFrame.getId(), fragment ).addToBackStack( FragmentTags.DEPOTS_VENTE.getTagName() );
			ft.commit();

			currentFragment = fragment;
		}
	}

	/**
	 * Affichage du fragment de l'accueil des plannings
	 */
	public void showPlanningAccueil() {
		if ( null != currentFragment && !currentFragment.equals( FragmentProvider.getFragment( FragmentTags.PLANNING_ACCUEIL.getTagName() ) ) ) {

			// Nettoyage des fragments bousillés par Android
			handleFragmentLifecycleInAProperWay();

			FragmentPlanningAccueil fragment = new FragmentPlanningAccueil();
			FragmentProvider.addFragment( FragmentTags.PLANNING_ACCUEIL.getTagName(), fragment );

			// Insertion du fragment dans Android
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add( mainFrame.getId(), fragment ).addToBackStack( FragmentTags.PLANNING_ACCUEIL.getTagName() );
			ft.commit();

			currentFragment = fragment;
		}
	}

	/**
	 * Affichage du fragment des plannings par semaine
	 */
	public void showPlanningSemaine() {
		if ( null != currentFragment && !currentFragment.equals( FragmentProvider.getFragment( FragmentTags.PLANNING_SEMAINE.getTagName() ) ) ) {

			showWaitingScreen();

			// Nettoyage des fragments bousillés par Android
			handleFragmentLifecycleInAProperWay();

			FragmentPlanningSemaine fragment = new FragmentPlanningSemaine();
			FragmentProvider.addFragment( FragmentTags.PLANNING_SEMAINE.getTagName(), fragment );

			// Insertion du fragment dans Android
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add( mainFrame.getId(), fragment ).addToBackStack( FragmentTags.PLANNING_SEMAINE.getTagName() );
			ft.commit();

			currentFragment = fragment;
		}
	}

	/**
	 * Affichage du fragment du planning synchronisé avec le CRM
	 */
	public void showPlanningSynchronise() {
		if ( null != currentFragment && !currentFragment.equals( FragmentProvider.getFragment( FragmentTags.PLANNING_SYNCHRONISE.getTagName() ) ) ) {

			showWaitingScreen();

			// Nettoyage des fragments bousillés par Android
			handleFragmentLifecycleInAProperWay();

			FragmentPlanningSynchronise fragment = new FragmentPlanningSynchronise();
			FragmentProvider.addFragment( FragmentTags.PLANNING_SYNCHRONISE.getTagName(), fragment );

			// Insertion du fragment dans Android
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add( mainFrame.getId(), fragment ).addToBackStack( FragmentTags.PLANNING_SYNCHRONISE.getTagName() );
			ft.commit();

			currentFragment = fragment;
		}
	}

	/**
	 * Initialisation des evenements
	 */
	private void initEvents() {
		itemLog.setOnMenuItemClickListener( menuItem -> {
			if ( SessionPhone.getInstance().isAuthenticated() ) {
				logout();
			} else {
				showLoginView();
			}
			return true;
		} );
		itemClose.setOnMenuItemClickListener( menuItem -> close() );
	}

	/**
	 * Initialisation des fragments
	 */
	private void initFragments() {
		FragmentProvider.addFragment( FragmentTags.LOGIN.getTagName(), new FragmentLogin() );
	}

	/**
	 * Affichage de la vue d'authentification utilisateur
	 */
	private void showLoginView() {
		if ( !FragmentProvider.getFragment( FragmentTags.LOGIN.getTagName() ).equals( currentFragment ) ) {

			( (TextView) navigationView.getHeaderView( 0 ).findViewById( R.id.tv_menu_header_user ) ).setText( R.string.non_connecte );

			navigationView.getMenu().setGroupVisible( R.id.menuGroupResponsable, false );
			navigationView.getMenu().setGroupVisible( R.id.menuGroupUtilisateur, false );

			// Nettoyage des fragments bousillés par Android
			handleFragmentLifecycleInAProperWay();

			// Instanciation du fragment de login
			FragmentProvider.addFragment( FragmentTags.LOGIN.getTagName(), new FragmentLogin() );

			// Ajout du fragment dans Android
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add( mainFrame.getId(), FragmentProvider.getFragment( FragmentTags.LOGIN.getTagName() ) ).addToBackStack( FragmentTags.LOGIN.getTagName() );
			ft.commit();

			currentFragment = FragmentProvider.getFragment( FragmentTags.LOGIN.getTagName() );
		}
	}

	/**
	 * Affichage du fragment d'accueil
	 */
	public void showAccueil() {
		if ( SessionPhone.getInstance().getUserDto().getRole().equals( RoleUser.ROLE_ADMIN ) ) {
			showAccueilResponsable();
		} else {
			showAccueilUtilisateur();
		}
		( (TextView) navigationView.getHeaderView( 0 ).findViewById( R.id.tv_menu_header_user ) ).setText( SessionPhone.getInstance().getUserDto().getNomUtilisateur() );
	}

	/**
	 * Affichage de l'application pour un responsable
	 */
	private void showAccueilResponsable() {

		if ( null != currentFragment && !currentFragment.equals( FragmentProvider.getFragment( FragmentTags.ACCUEIL_RESPONSABLE.getTagName() ) ) ) {

			navigationView.getMenu().setGroupVisible( R.id.menuGroupResponsable, true );
			navigationView.getMenu().setGroupVisible( R.id.menuGroupUtilisateur, false );

			// Nettoyage des fragments bousillés par Android
			handleFragmentLifecycleInAProperWay();

			FragmentRespAccueil fragment = new FragmentRespAccueil();
			FragmentProvider.addFragment( FragmentTags.ACCUEIL_RESPONSABLE.getTagName(), fragment );

			// Insertion du fragment dans Android
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add( mainFrame.getId(), fragment ).addToBackStack( FragmentTags.ACCUEIL_RESPONSABLE.getTagName() );
			ft.commit();

			currentFragment = fragment;
		}
	}

	/**
	 * Affichage de l'application pour un utilisateur
	 */
	private void showAccueilUtilisateur() {
		if ( null != currentFragment && !currentFragment.equals( FragmentProvider.getFragment( FragmentTags.ACCUEIL_UTILISATEUR.getTagName() ) ) ) {

			navigationView.getMenu().setGroupVisible( R.id.menuGroupResponsable, false );
			navigationView.getMenu().setGroupVisible( R.id.menuGroupUtilisateur, true );

			// Nettoyage des fragments bousillés par Android
			handleFragmentLifecycleInAProperWay();

			FragmentUserAccueil fragment = new FragmentUserAccueil();
			FragmentProvider.addFragment( FragmentTags.ACCUEIL_UTILISATEUR.getTagName(), fragment );

			// Insertion du fragment dans Android
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.add( mainFrame.getId(), fragment ).addToBackStack( FragmentTags.ACCUEIL_UTILISATEUR.getTagName() );
			ft.commit();

			currentFragment = fragment;
		}
	}

	/**
	 * A chaque demande d'affichage d'un fragment, on efface les anciens et on en
	 * instancie un nouveau.
	 */
	private void handleFragmentLifecycleInAProperWay() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		for ( Fragment fragmentInProvider : FragmentProvider.getFragments() ) {
			ft.remove( fragmentInProvider );
		}
		ft.commit();
	}

	/**
	 * Initialisation des évènements injectés dans le fragment Login
	 */
	public void initEventsInFragAuth() {
		( (FragmentLogin) FragmentProvider.getFragment( FragmentTags.LOGIN.getTagName() ) ).getBtAuthenticate().setOnClickListener( source -> authenticate() );
	}

	/**
	 * Authentification de l'utilisateur suite au clic sur le bouton de demande
	 * d'authentification
	 */
	private void authenticate() {
		try {
			String username = ( (FragmentLogin) FragmentProvider.getFragment( FragmentTags.LOGIN.getTagName() ) ).getUsername();
			String password = ( (FragmentLogin) FragmentProvider.getFragment( FragmentTags.LOGIN.getTagName() ) ).getPassword();

			StorageUtil.writeObjectInternal( this, ConstantesKeys.KEY_USERNAME.getKeyName(), username );
			StorageUtil.writeObjectInternal( this, ConstantesKeys.KEY_PASSWORD.getKeyName(), password );

			showWaitingScreen();

			// Récupération de l'utilisateur authentifié
			WsUtil.authenticate( this, null, username, password );

		} catch ( Exception ex ) {
			Log.e( "ERROR", "Erreur lors de la tentative d'authentification.", ex );
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException( ex );
		}
	}

	/**
	 * Affichage d'un ecran d'attente pour l'utilisateur
	 */
	public void showWaitingScreen() {
		layoutWaiting.requestFocus();
		layoutWaiting.setVisibility( View.VISIBLE );
	}

	/**
	 * Suppression de l'ecran d'attente
	 */
	public void removeWaitingScreen() {
		layoutWaiting.setVisibility( View.GONE );
	}

	/**
	 * Modification de la toolbar pour afficher un utilisateur authentifié
	 */
	private void updateLoginToLogged() {
		itemLog.setIcon( ContextCompat.getDrawable( this, R.drawable.ic_logout ) );
		( (TextView) ( (FrameLayout) itemUser.getActionView() ).getChildAt( 0 ) ).setText( SessionPhone.getInstance().getUserDto().getNomUtilisateur() );
	}

	/**
	 * Modification de la toolbar pour afficher un utilisateur non authentifié
	 */
	private void updateLoggedToLogin() {
		itemLog.setIcon( ContextCompat.getDrawable( this, R.drawable.ic_login ) );
		( (TextView) ( (FrameLayout) itemUser.getActionView() ).getChildAt( 0 ) ).setText( "" );
	}

	/**
	 * Deconnexion de l'utilisateur
	 */
	private void logout() {
		SessionPhone.getInstance().setUserDto( null );
		SessionPhone.getInstance().setAuthenticated( false );

		updateLoggedToLogin();

		showLoginView();

	}

	/**
	 * Fermeture de l'application
	 *
	 * @return Inutile...
	 */
	private boolean close() {
		new AlertDialog.Builder( this ).setIcon( android.R.drawable.ic_dialog_alert ).setTitle( "Fermeture" ).setMessage( "Voulez-vous fermer l'application ?" )
				.setPositiveButton( "Oui", ( dialog, which ) -> finish() ).setNegativeButton( "Non", null ).show();
		return true;
	}

	@Override
	public void notifyResponse( WsName wsName, Map<String, Object> mapResources, Object response ) {
		switch ( wsName ) {
			case AUTHENTIFICATION :
				if ( null != response ) {
					// Si l'utilisateur est authentifié, on l'ajoute dans la session
					UtilisateurDTO userDto = (UtilisateurDTO) response;
					SessionPhone.getInstance().setUserDto( userDto );
					SessionPhone.getInstance().setAuthenticated( true );
					SessionPhone.getInstance().setDateOuvertureSession( LocalDate.now() );
					( (FragmentLogin) FragmentProvider.getFragment( FragmentTags.LOGIN.getTagName() ) ).resetView();

					showAccueil();

					updateLoginToLogged();

					WsUtil.getSalarieByIdUser( this, null, userDto.getId() );
				} else {
					( (FragmentLogin) FragmentProvider.getFragment( FragmentTags.LOGIN.getTagName() ) ).showAuthError();
				}
				break;
			case GET_SALARIE_BY_ID_USER :
				if ( null != response ) {
					// Mise en session du salarié authentifié
					SalarieLightDTO salarie = (SalarieLightDTO) response;
					SessionPhone.getInstance().setSalarie( salarie );
				}
				break;
			default :
				throw new IllegalStateException( "Reception d'une reponse de webservice provenant de : " + wsName + ". Les données associées sont : " + response );
		}
	}

	@Override
	public void onBackPressed() {
		// do nothing
	}

	@Override
	public Fragment getFragmentSource() {
		return null;
	}
}