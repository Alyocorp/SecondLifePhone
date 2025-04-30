package fr.artemis.phone.fragments.utilisateur.planning.synchronise;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.artemis.phone.BuildConfig;
import fr.artemis.phone.R;
import fr.artemis.phone.activities.main.MainActivity;
import fr.artemis.phone.dto.CRMPhoneDevisCaracteristiqueDTO;
import fr.artemis.phone.dto.CRMPhonePlanningDTO;
import fr.artemis.phone.dto.CRMPhonePlanningDevisDTO;
import fr.artemis.phone.dto.CRMPhonePlanningInterventionDTO;
import fr.artemis.phone.utils.DateUtils;
import fr.artemis.phone.utils.SessionPhone;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.WsUtil;
import fr.artemis.phone.weekview.DateTimeInterpreter;
import fr.artemis.phone.weekview.MonthLoader;
import fr.artemis.phone.weekview.WeekView;
import fr.artemis.phone.weekview.WeekViewEvent;

/**
 * Fragment du planning synchronisé avec le CRM
 */
public class FragmentPlanningSynchronise extends Fragment implements WsCaller, WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {

	public static final String DEVIS = "DEVIS";
	public static final String INTERVENTION = "INTERVENTION";

	@BindView( R.id.layoutFiltres )
	ScrollView layoutFiltres;

	@BindView( R.id.layoutFiltresSecteurs )
	LinearLayout layoutFiltresSecteurs;

	@BindView( R.id.layoutFiltresTypes )
	LinearLayout layoutFiltresTypes;

	@BindView( R.id.btPrevDate )
	ImageButton btPrevDate;

	@BindView( R.id.tvDateDuJour )
	TextView tvDateDuJour;

	@BindView( R.id.btNextDate )
	ImageButton btNextDate;

	@BindView( R.id.btShowCalendar )
	ImageButton btShowCalendar;

	@BindView( R.id.weekView )
	WeekView weekView;

	@BindView( R.id.rgAffichage )
	RadioGroup rgAffichage;

	@BindView( R.id.rb1Jour )
	RadioButton rb1Jour;

	@BindView( R.id.rb3Jours )
	RadioButton rb3Jours;

	@BindView( R.id.layoutDetailsBottom )
	LinearLayout layoutDetailsBottom;

	@BindView( R.id.layoutPlanningDevis )
	LinearLayout layoutPlanningDevis;

	@BindView( R.id.tvDevisClient )
	TextView tvDevisClient;

	@BindView( R.id.tvDevisAdresse )
	TextView tvDevisAdresse;

	@BindView( R.id.tvDevisTelephone )
	TextView tvDevisTelephone;

	@BindView( R.id.tvDevisInfos )
	TextView tvDevisInfo;

	@BindView( R.id.layoutPlanningIntervention )
	LinearLayout layoutPlanningIntervention;

	@BindView( R.id.tvInterClient )
	TextView tvInterClient;

	@BindView( R.id.tvInterAdresse )
	TextView tvInterAdresse;

	@BindView( R.id.tvInterTelephone )
	TextView tvInterTelephone;

	@BindView( R.id.btCloseDevis )
	ImageButton btCloseDevis;

	@BindView( R.id.btCloseInter )
	ImageButton btCloseInter;

	@BindView( R.id.tvAcces1 )
	TextView tvAcces1;
	@BindView( R.id.tvAcces2 )
	TextView tvAcces2;
	@BindView( R.id.tvAcces3 )
	TextView tvAcces3;
	@BindView( R.id.tvAcces4 )
	TextView tvAcces4;
	@BindView( R.id.tvAcces5 )
	TextView tvAcces5;

	@BindView( R.id.tvTri1 )
	TextView tvTri1;
	@BindView( R.id.tvTri2 )
	TextView tvTri2;
	@BindView( R.id.tvTri3 )
	TextView tvTri3;
	@BindView( R.id.tvTri4 )
	TextView tvTri4;
	@BindView( R.id.tvTri5 )
	TextView tvTri5;

	@BindView( R.id.tvSalubrite1 )
	TextView tvSalubrite1;
	@BindView( R.id.tvSalubrite2 )
	TextView tvSalubrite2;
	@BindView( R.id.tvSalubrite3 )
	TextView tvSalubrite3;
	@BindView( R.id.tvSalubrite4 )
	TextView tvSalubrite4;
	@BindView( R.id.tvSalubrite5 )
	TextView tvSalubrite5;

	@BindView( R.id.tvQuantite1 )
	TextView tvQuantite1;
	@BindView( R.id.tvQuantite2 )
	TextView tvQuantite2;
	@BindView( R.id.tvQuantite3 )
	TextView tvQuantite3;
	@BindView( R.id.tvQuantite4 )
	TextView tvQuantite4;
	@BindView( R.id.tvQuantite5 )
	TextView tvQuantite5;


	@BindView( R.id.tvInterVolume )
	TextView tvInterVolume;

	@BindView( R.id.tvInterValeur )
	TextView tvInterValeur;

	@BindView( R.id.tvInterTotal )
	TextView tvInterTotal;

	@BindView( R.id.tvInterRemarques )
	TextView tvInterRemarques;

	@BindView( R.id.tvInterMateriel )
	TextView tvInterMateriel;

	@BindView( R.id.btOuvrirDevis )
	Button btOuvrirDevis;

	// Filtre secteur
	private RadioGroup rgSecteur;

	// Filtre type
	private RadioGroup rgType;

	// Le secteur préselectionné ou selectionné par l'utilisateur
	private String secteurSelected = null;

	// Le type de planning préselectionné ou selectionné par l'utilisateur
	private String typeSelected = null;

	// La date du jour
	private LocalDate dateEnCours = LocalDate.now();

	// La date de début des evenements du planning chargés en mémoire
	private LocalDate startDate = LocalDate.now().minus( 1, ChronoUnit.WEEKS );

	// La date de fin des evenements du planning chargés en mémoire
	private LocalDate endDate = LocalDate.now().plus( 1, ChronoUnit.MONTHS );

	// La liste des plannings
	private List<CRMPhonePlanningDTO> listePlannings = null;

	// La liste des plannigns par secteur
	private Map<String, List<CRMPhonePlanningDTO>> listePlanningsParSecteur = new HashMap<>();

	// La liste des plannings par type
	private Map<String, List<CRMPhonePlanningDTO>> listePlanningsParType = new HashMap<>();

	// La liste des plannings affichés
	private List<CRMPhonePlanningDTO> listePlanningsAffiches = new ArrayList<>();

	// La liste des evenements du planning
	List<WeekViewEvent> events;

	@Nullable
	@Override
	public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
		View view = inflater.inflate( R.layout.fragment_planning_synchronise, container, false );

		ButterKnife.bind( this, view );

		assert null != getActivity();

		initDatas();

		initView();

		initEvents();

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		( (MainActivity) getActivity() ).removeWaitingScreen();
	}

	/**
	 * Initialisation de la vue
	 */
	private void initView() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "dd/MM/yyyy" );
		String dateFormattee = dateEnCours.format( formatter );
		tvDateDuJour.setText( dateFormattee );

		// Show a toast message about the touched event.
		weekView.setOnEventClickListener( this );

		// The week view has infinite scrolling horizontally. We have to provide the events of a
		// month every time the month changes on the week view.
		weekView.setMonthChangeListener( this );

		// Set long press listener for events.
		weekView.setEventLongPressListener( this );

		// Set long press listener for empty view
		weekView.setEmptyViewLongPressListener( this );

		// Set up a date time interpreter to interpret how the date and time will be formatted in
		// the week view. This is optional.
		setupDateTimeInterpreter( false );

		rb3Jours.setChecked( true );

		weekView.goToHour( 8 );
	}

	/**
	 * Set up a date time interpreter which will show short date values when in week view and long
	 * date values otherwise.
	 *
	 * @param shortDate True if the date values should be short.
	 */
	private void setupDateTimeInterpreter( final boolean shortDate ) {
		weekView.setDateTimeInterpreter( new DateTimeInterpreter() {
			@Override
			public String interpretDate( Calendar date ) {
				SimpleDateFormat weekdayNameFormat = new SimpleDateFormat( "EEE", Locale.getDefault() );
				String weekday = weekdayNameFormat.format( date.getTime() );
				SimpleDateFormat format = new SimpleDateFormat( " d", Locale.getDefault() );

				if ( shortDate ) {
					weekday = String.valueOf( weekday.charAt( 0 ) );
				}
				return weekday.toUpperCase() + format.format( date.getTime() );
			}

			@Override
			public String interpretTime( int hour ) {
				return hour + " : 00";
			}
		} );
	}

	@Override
	public void onEventClick( WeekViewEvent event, RectF eventRect ) {
		if ( null != event.getUserObject() ) {
			switch ( event.getUserObjectType() ) {
				case DEVIS: {
					CRMPhonePlanningDevisDTO devis = (CRMPhonePlanningDevisDTO) event.getUserObject();
					showInfosDevis( devis );

					break;
				}
				case INTERVENTION: {
					CRMPhonePlanningInterventionDTO intervention = (CRMPhonePlanningInterventionDTO) event.getUserObject();
					showInfosInter( intervention );

					break;
				}
			}
		} else {
			layoutDetailsBottom.setVisibility( View.GONE );
		}
	}

	/**
	 * Affichage des informations sur un devis
	 *
	 * @param devis Le devis
	 */
	private void showInfosDevis( CRMPhonePlanningDevisDTO devis ) {
		layoutDetailsBottom.setVisibility( View.VISIBLE );
		layoutPlanningDevis.setVisibility( View.VISIBLE );
		layoutPlanningIntervention.setVisibility( View.GONE );
		tvDevisClient.setText( getClient( devis ) );
		tvDevisAdresse.setText( getAdresse( devis ) );
		tvDevisAdresse.setPaintFlags( tvDevisAdresse.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG );
		tvDevisAdresse.setTextColor( Color.BLUE );
		tvDevisAdresse.setOnClickListener( v -> {
			Intent geoIntent = new Intent( Intent.ACTION_VIEW );
			geoIntent.setData( Uri.parse( "https://www.google.fr/maps/place/" + tvDevisAdresse.getText().toString().replace( " ", "+" ) ) );
			startActivity( geoIntent );
		} );

		// Telephone
		if ( null != devis.getClient().getTelephone() && !"".equals( devis.getClient().getTelephone() ) ) {
			tvDevisTelephone.setText( devis.getClient().getTelephone().replace( " ", "" ).replace( ".", "" ).replace( "-", "" ) );
			tvDevisTelephone.setPaintFlags( tvDevisTelephone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG );
			tvDevisTelephone.setTextColor( Color.BLUE );
			tvDevisTelephone.setOnClickListener( v -> {
				AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
				builder.setTitle("Appel");
				builder.setMessage("Appeler le client au " + tvDevisTelephone.getText() + " ?");
				builder.setCancelable(false);
				builder.setPositiveButton("Oui", (dialog, which) -> {
					Intent callIntent = new Intent( Intent.ACTION_CALL );
					callIntent.setData( Uri.parse( "tel:" + tvDevisTelephone.getText().toString() ) );
					startActivity( callIntent );
				});

				builder.setNegativeButton("Non", (dialog, which) -> Toast.makeText(getContext(), "Appel annulé", Toast.LENGTH_SHORT).show());
				builder.show();
			} );
		}

		tvDevisInfo.setText( devis.getInformationsComplementaires() );
	}

	/**
	 * Affichage des informations sur une intervention
	 *
	 * @param inter L'intervention
	 */
	private void showInfosInter( CRMPhonePlanningInterventionDTO inter ) {
		// En premier lieu, on masque les carastéristiques afin d'afficher uniquement les valeurs contenues dans l'intervention

		layoutDetailsBottom.setVisibility( View.VISIBLE );
		layoutPlanningDevis.setVisibility( View.GONE );
		layoutPlanningIntervention.setVisibility( View.VISIBLE );
		tvInterClient.setText( getClient( inter ) );
		tvInterAdresse.setText( getAdresse( inter ) );
		tvInterAdresse.setPaintFlags( tvInterAdresse.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG );
		tvInterAdresse.setTextColor( Color.BLUE );
		tvInterAdresse.setOnClickListener( v -> {
			Intent geoIntent = new Intent( Intent.ACTION_VIEW );
			geoIntent.setData( Uri.parse( "https://www.google.fr/maps/place/" + tvInterAdresse.getText().toString().replace( " ", "+" ) ) );
			startActivity( geoIntent );
		} );

		// Telephone
		if ( null != inter.getClient().getTelephone() && !"".equals( inter.getClient().getTelephone() ) ) {
			tvInterTelephone.setText( inter.getClient().getTelephone().replace( " ", "" ).replace( ".", "" ).replace( "-", "" ) );
			tvInterTelephone.setPaintFlags( tvInterTelephone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG );
			tvInterTelephone.setTextColor( Color.BLUE );
			tvInterTelephone.setOnClickListener( v -> {
				AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
				builder.setTitle("Appel");
				builder.setMessage("Appeler le client au " + tvInterTelephone.getText() + " ?");
				builder.setCancelable(false);
				builder.setPositiveButton("Oui", (dialog, which) -> {
					Intent callIntent = new Intent( Intent.ACTION_CALL );
					callIntent.setData( Uri.parse( "tel:" + tvInterTelephone.getText().toString() ) );
					startActivity( callIntent );
				});

				builder.setNegativeButton("Non", (dialog, which) -> Toast.makeText(getContext(), "Appel annulé", Toast.LENGTH_SHORT).show());
				builder.show();
			} );
		}

		tvInterVolume.setText( inter.getVolume() + " m³" );
		tvInterValeur.setText( inter.getValeur() + " €" );
		tvInterTotal.setText( inter.getMontantTotal() + " €" );
		tvInterRemarques.setText( inter.getRemarques() );
		tvInterMateriel.setText( inter.getMateriel() );

		btOuvrirDevis.setOnClickListener( v -> WsUtil.getFileDevis( FragmentPlanningSynchronise.this, null, inter.getFichierDevis() ) );

		for ( CRMPhoneDevisCaracteristiqueDTO carac : inter.getListeCaracteristiques() ) {
			if ( null != carac.getNote() && null != carac.getNote().getNote() ) {
				switch (carac.getCaracteristique().getCaracteristique()) {
					case "Accès":
						switch (carac.getNote().getNote()) {
							case "1":
								tvAcces1.setText("X");
								tvAcces2.setText(" ");
								tvAcces3.setText(" ");
								tvAcces4.setText(" ");
								tvAcces5.setText(" ");
								break;
							case "2":
								tvAcces1.setText(" ");
								tvAcces2.setText("X");
								tvAcces3.setText(" ");
								tvAcces4.setText(" ");
								tvAcces5.setText(" ");
								break;
							case "3":
								tvAcces1.setText(" ");
								tvAcces2.setText(" ");
								tvAcces3.setText("X");
								tvAcces4.setText(" ");
								tvAcces5.setText(" ");
								break;
							case "4":
								tvAcces1.setText(" ");
								tvAcces2.setText(" ");
								tvAcces3.setText(" ");
								tvAcces4.setText("X");
								tvAcces5.setText(" ");
								break;
							case "5":
								tvAcces1.setText(" ");
								tvAcces2.setText(" ");
								tvAcces3.setText(" ");
								tvAcces4.setText(" ");
								tvAcces5.setText("X");
								break;
						}
						break;
					case "Tri":
						switch (carac.getNote().getNote()) {
							case "1":
								tvTri1.setText("X");
								tvTri2.setText(" ");
								tvTri3.setText(" ");
								tvTri4.setText(" ");
								tvTri5.setText(" ");
								break;
							case "2":
								tvTri1.setText(" ");
								tvTri2.setText("X");
								tvTri3.setText(" ");
								tvTri4.setText(" ");
								tvTri5.setText(" ");
								break;
							case "3":
								tvTri1.setText(" ");
								tvTri2.setText(" ");
								tvTri3.setText("X");
								tvTri4.setText(" ");
								tvTri5.setText(" ");
								break;
							case "4":
								tvTri1.setText(" ");
								tvTri2.setText(" ");
								tvTri3.setText(" ");
								tvTri4.setText("X");
								tvTri5.setText(" ");
								break;
							case "5":
								tvTri1.setText(" ");
								tvTri2.setText(" ");
								tvTri3.setText(" ");
								tvTri4.setText(" ");
								tvTri5.setText("X");
								break;
						}
						break;
					case "Salubrité":
						switch (carac.getNote().getNote()) {
							case "1":
								tvSalubrite1.setText("X");
								tvSalubrite2.setText(" ");
								tvSalubrite3.setText(" ");
								tvSalubrite4.setText(" ");
								tvSalubrite5.setText(" ");
								break;
							case "2":
								tvSalubrite1.setText(" ");
								tvSalubrite2.setText("X");
								tvSalubrite3.setText(" ");
								tvSalubrite4.setText(" ");
								tvSalubrite5.setText(" ");
								break;
							case "3":
								tvSalubrite1.setText(" ");
								tvSalubrite2.setText(" ");
								tvSalubrite3.setText("X");
								tvSalubrite4.setText(" ");
								tvSalubrite5.setText(" ");
								break;
							case "4":
								tvSalubrite1.setText(" ");
								tvSalubrite2.setText(" ");
								tvSalubrite3.setText(" ");
								tvSalubrite4.setText("X");
								tvSalubrite5.setText(" ");
								break;
							case "5":
								tvSalubrite1.setText(" ");
								tvSalubrite2.setText(" ");
								tvSalubrite3.setText(" ");
								tvSalubrite4.setText(" ");
								tvSalubrite5.setText("X");
								break;
						}
						break;
					case "Coeff. revente":
						switch (carac.getNote().getNote()) {
							case "0":
								tvQuantite1.setText("X");
								tvQuantite2.setText(" ");
								tvQuantite3.setText(" ");
								tvQuantite4.setText(" ");
								tvQuantite5.setText(" ");
								break;
							case "1":
								tvQuantite1.setText(" ");
								tvQuantite2.setText("X");
								tvQuantite3.setText(" ");
								tvQuantite4.setText(" ");
								tvQuantite5.setText(" ");
								break;
							case "2":
								tvQuantite1.setText(" ");
								tvQuantite2.setText(" ");
								tvQuantite3.setText("X");
								tvQuantite4.setText(" ");
								tvQuantite5.setText(" ");
								break;
							case "3":
								tvQuantite1.setText(" ");
								tvQuantite2.setText(" ");
								tvQuantite3.setText(" ");
								tvQuantite4.setText("X");
								tvQuantite5.setText(" ");
								break;
							case "++":
								tvQuantite1.setText(" ");
								tvQuantite2.setText(" ");
								tvQuantite3.setText(" ");
								tvQuantite4.setText(" ");
								tvQuantite5.setText("X");
								break;
						}
						break;
				}
			}
		}
	}

	@Override
	public void onEventLongPress( WeekViewEvent event, RectF eventRect ) {
		Toast.makeText( getActivity(), "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT ).show();
	}

	@Override
	public void onEmptyViewLongPress( Calendar time ) {
//		Toast.makeText( getActivity(), "Empty view long pressed: " + getEventTitle( time ), Toast.LENGTH_SHORT ).show();
	}

	@Override
	public List<? extends WeekViewEvent> onMonthChange( int newYear, int newMonth ) {
		// Populate the week view with some events.
		events = new ArrayList<>();

		for ( CRMPhonePlanningDTO planning : listePlanningsAffiches ) {
			if ( null != planning.getListeDevis() && !planning.getListeDevis().isEmpty() ) {
				for ( CRMPhonePlanningDevisDTO devis : planning.getListeDevis() ) {
					if ( ( devis.getDateDebut().get( Calendar.MONTH ) == ( newMonth - 1 ) ) && devis.getDateDebut().get( Calendar.YEAR ) == newYear ) {
						WeekViewEvent event = new WeekViewEvent( planning.getId() + "_" + devis.getId(), devis.getClient().getNom() + " - " + devis.getCodePostal(), devis.getDateDebut(), devis.getDateFin() );
						event.setColor( ContextCompat.getColor( getContext(), R.color.green ) );
						event.setUserData( DEVIS, devis );
						events.add( event );
					}
				}
			}

			if ( null != planning.getListeInterventions() && !planning.getListeInterventions().isEmpty() ) {
				for ( CRMPhonePlanningInterventionDTO intervention : planning.getListeInterventions() ) {
					if ( ( intervention.getDateDebut().get( Calendar.MONTH ) == ( newMonth - 1 ) ) && intervention.getDateDebut().get( Calendar.YEAR ) == newYear ) {
						WeekViewEvent event = new WeekViewEvent( planning.getId() + "_" + intervention.getId(), intervention.getClient().getNom() + " - " + intervention.getCodePostal(), intervention.getDateDebut(), intervention.getDateFin() );
						event.setColor( ContextCompat.getColor( getContext(), R.color.red ) );
						event.setUserData( INTERVENTION, intervention );
						events.add( event );
					}
				}
			}
		}

		return events;
	}

	public WeekView getWeekView() {
		return weekView;
	}

	/**
	 * Initialisation des evenements
	 */
	private void initEvents() {
		btShowCalendar.setOnClickListener( view -> switchCalendarVisibility() );
		btPrevDate.setOnClickListener( view -> decreaseCurrentDate() );
		btNextDate.setOnClickListener( view -> increaseCurrentDate() );
		rgAffichage.setOnCheckedChangeListener( ( group, idChild ) -> modifyView( group, idChild ) );
		btCloseDevis.setOnClickListener( v -> closeBottomLayout() );
		btCloseInter.setOnClickListener( v -> closeBottomLayout() );
	}

	/**
	 * Fermeture du layout de détails de devis ou d'intervention
	 */
	private void closeBottomLayout() {
		layoutDetailsBottom.setVisibility( View.GONE );
	}

	/**
	 * Modification de l'affichage en fonction de la selection utilisateur
	 *
	 * @param group   Le group de bouton concernant l'affichage
	 * @param idChild L'identifiant du bouton selectionné
	 */
	private void modifyView( RadioGroup group, int idChild ) {
		if ( idChild == rb1Jour.getId() ) {
			weekView.setNumberOfVisibleDays( 1 );
			weekView.goToHour( 8 );
		} else if ( idChild == rb3Jours.getId() ) {
			weekView.setNumberOfVisibleDays( 3 );
			weekView.goToHour( 8 );
		}
	}

	/**
	 * Incrementation de la date en cours
	 */
	private void increaseCurrentDate() {
		dateEnCours = dateEnCours.plusDays( 1 );
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "dd/MM/yyyy" );
		String dateFormattee = dateEnCours.format( formatter );
		tvDateDuJour.setText( dateFormattee );

		Calendar calendar = Calendar.getInstance();
		calendar.set( Calendar.YEAR, dateEnCours.getYear() );
		calendar.set( Calendar.MONTH, dateEnCours.getMonthValue() - 1 );
		calendar.set( Calendar.DAY_OF_MONTH, dateEnCours.getDayOfMonth() );

		checkDateUpdated();
	}

	/**
	 * Decrementation de la date en cours
	 */
	private void decreaseCurrentDate() {
		dateEnCours = dateEnCours.minusDays( 1 );
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "dd/MM/yyyy" );
		String dateFormattee = dateEnCours.format( formatter );
		tvDateDuJour.setText( dateFormattee );

		Calendar calendar = Calendar.getInstance();
		calendar.set( Calendar.YEAR, dateEnCours.getYear() );
		calendar.set( Calendar.MONTH, dateEnCours.getMonthValue() - 1 );
		calendar.set( Calendar.DAY_OF_MONTH, dateEnCours.getDayOfMonth() );

		checkDateUpdated();
	}

	/**
	 * Affichage / Masquage du calendrier
	 */
	private void switchCalendarVisibility() {
		if ( layoutFiltres.getVisibility() == View.VISIBLE ) {
			btShowCalendar.setImageResource( R.drawable.ic_bt_drop_down );
			layoutFiltres.setVisibility( View.GONE );
		} else {
			btShowCalendar.setImageResource( R.drawable.ic_bt_drop_up );
			layoutFiltres.setVisibility( View.VISIBLE );
		}
	}

	/**
	 * Verification si la semaine a changé
	 */
	private void checkDateUpdated() {
		weekView.goToDate( DateUtils.localDateToCalendar( dateEnCours ) );
		// TODO hors bornes -> appel WS
		// CALL WS
//		int newNumWeek = DateUtils.getWeekNumber( dateEnCours );
//		if ( newNumWeek != numSemaine ) {
//			initDatas();
//		}
	}

	/**
	 * Initialisation des données
	 */
	private void initDatas() {
		WsUtil.getListePlanningsBetween( this, null, startDate, endDate );
	}

	/**
	 * Mise à jour des plannings
	 */
	private void updatePlannings() {
		reloadListesAndFiltres();
		weekView.goToDate( DateUtils.localDateToCalendar( dateEnCours ) );
	}

	/**
	 * Rafraichissement de la liste des plannings correspondants aux filtres
	 *
	 * @param rgSource Le groupe de boutons source
	 * @param rbId     L'identifiant du bouton selectionné
	 */
	private void planningFilter( RadioGroup rgSource, int rbId ) {
		RadioButton rbSelected = rgSource.findViewById( rbId );
		if ( rgSource.equals( rgSecteur ) ) {
			secteurSelected = rbSelected.getText().toString();
		} else if ( rgSource.equals( rgType ) ) {
			typeSelected = rbSelected.getText().toString();
		}

		if ( null != secteurSelected && null != typeSelected ) {

			List<CRMPhonePlanningDTO> listePlanningDuMemeType = listePlanningsParType.get( typeSelected );

			listePlanningsAffiches = listePlanningDuMemeType.stream().distinct().filter( listePlanningsParSecteur.get( secteurSelected )::contains ).collect( Collectors.toList() );
			weekView.goToDate( DateUtils.localDateToCalendar( dateEnCours ) );
		}
	}

	/**
	 * Rechargement de la liste des filtres ( secteur et type )
	 */
	private void reloadListesAndFiltres() {

		List<String> listeSecteurs = new ArrayList<>();
		List<String> listeTypes = new ArrayList<>();

		if ( null != listePlannings && !listePlannings.isEmpty() ) {
			// Creation des secteurs et types possibles
			for ( CRMPhonePlanningDTO planningDto : listePlannings ) {
				if ( !listeSecteurs.contains( planningDto.getSecteur().getSecteur() ) ) {
					listeSecteurs.add( planningDto.getSecteur().getSecteur() );
				}
				if ( !listeTypes.contains( planningDto.getTypeEvenement().getTypeEvenement() ) ) {
					listeTypes.add( planningDto.getTypeEvenement().getTypeEvenement() );
				}
			}

			// Creation de la map par secteur
			for ( String secteur : listeSecteurs ) {
				for ( CRMPhonePlanningDTO planningDto : listePlannings ) {
					if ( planningDto.getSecteur().getSecteur().equals( secteur ) ) {
						List<CRMPhonePlanningDTO> listeParSecteur = listePlanningsParSecteur.get( secteur );
						if ( null == listeParSecteur ) {
							listeParSecteur = new ArrayList<>();
						}
						listeParSecteur.add( planningDto );
						listePlanningsParSecteur.put( secteur, listeParSecteur );
					}
				}
			}

			// Creation de la map par type
			for ( String type : listeTypes ) {
				for ( CRMPhonePlanningDTO planningDto : listePlannings ) {
					if ( planningDto.getTypeEvenement().getTypeEvenement().equals( type ) ) {
						List<CRMPhonePlanningDTO> listeParType = listePlanningsParType.get( type );
						if ( null == listeParType ) {
							listeParType = new ArrayList<>();
						}
						listeParType.add( planningDto );
						listePlanningsParType.put( type, listeParType );
					}
				}
			}
		}

		if ( !listeSecteurs.isEmpty() ) {
			layoutFiltresSecteurs.removeAllViews();
			rgSecteur = new RadioGroup( getContext() );
			rgSecteur.setOnCheckedChangeListener( ( group, checkedId ) -> planningFilter( group, checkedId ) );
			for ( String secteur : listeSecteurs ) {
				RadioButton rbSecteur = new RadioButton( getContext() );
				rbSecteur.setId( View.generateViewId() );
				rbSecteur.setText( secteur );
				rgSecteur.addView( rbSecteur );
				rbSecteur.setChecked( secteur.equals( SessionPhone.getInstance().getSalarie().getSecteur().getSecteur() ) );
			}
			layoutFiltresSecteurs.addView( rgSecteur );
		}
		if ( !listeTypes.isEmpty() ) {
			layoutFiltresTypes.removeAllViews();
			rgType = new RadioGroup( getContext() );
			rgType.setOnCheckedChangeListener( ( group, checkedId ) -> planningFilter( group, checkedId ) );
			for ( String type : listeTypes ) {
				RadioButton rbType = new RadioButton( getContext() );
				rbType.setId( View.generateViewId() );
				rbType.setText( type );
				rgType.addView( rbType );
				rbType.setChecked( type.equals( "Intervention" ) );
			}
			layoutFiltresTypes.addView( rgType );
		}
	}

	/**
	 * Ouverture d'un document dans une application tierce à partir du nom de fichier.
	 * Attention: une autorisation est necessaire dans le manifest afin de permettre l'accès du cache de l'application en cours aux applications tierces
	 *
	 * @param fileName Le nom du fichier à ouvrir
	 */
	public void openDocument( String fileName ) {
		Intent intent = new Intent( android.content.Intent.ACTION_VIEW );
		File file = new File( SessionPhone.getInstance().getFilePath() + fileName );
		Uri uri = FileProvider.getUriForFile( getContext(), BuildConfig.APPLICATION_ID + ".provider", file );

		// Ouverture d'un fichier dans la bonne application à patir de son type MIME
		String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl( Uri.fromFile( file ).toString() );
		String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension( extension );
		if ( extension.equalsIgnoreCase( "" ) || mimetype == null ) {
			intent.setDataAndType( uri, "text/*" );
		} else {
			intent.setDataAndType( uri, mimetype );
		}
		intent.setFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION );
		startActivity( Intent.createChooser( intent, "Choose an Application:" ) );
	}

	@Override
	public void notifyResponse( WsName wsName, Map<String, Object> mapResources, Object response ) {
		switch ( wsName ) {
			case GET_LISTE_PLANNINGS_BETWEEN:
				if ( null != response ) {
					this.listePlannings = (List<CRMPhonePlanningDTO>) response;
					updatePlannings();
				}
				break;
			case GET_DEVIS_FILE:
				if ( null != response ) {
					openDocument( String.valueOf( response ) );
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

	/**
	 * Retourne l'identité d'un client d'un devis du planning
	 *
	 * @param devis Le devis du planning
	 * @return L'identité du client
	 */
	private String getClient( CRMPhonePlanningDevisDTO devis ) {
		StringBuilder sbClient = new StringBuilder();

		if ( null != devis && null != devis.getClient() ) {
			if ( null != devis.getClient().getCivilite() ) {
				sbClient.append( devis.getClient().getCivilite() ).append( " " );
			}
			if ( null != devis.getClient().getNom() ) {
				sbClient.append( devis.getClient().getNom() ).append( " " );
			}
			if ( null != devis.getClient().getPrenom() ) {
				sbClient.append( devis.getClient().getPrenom() );
			}
		}

		return sbClient.toString();
	}

	/**
	 * Retourne l'adresse d'un devis à partir du planning
	 *
	 * @param devis Le devis du planning
	 * @return L'adresse du devis
	 */
	private String getAdresse( CRMPhonePlanningDevisDTO devis ) {
		StringBuilder sbAdresse = new StringBuilder();

		if ( null != devis ) {
			if ( null != devis.getAdresse() ) {
				sbAdresse.append( devis.getAdresse() ).append( " " );
			}
			if ( null != devis.getAdresseComplement() ) {
				sbAdresse.append( devis.getAdresseComplement() ).append( " " );
			}
			if ( null != devis.getCodePostal() ) {
				sbAdresse.append( devis.getCodePostal() ).append( " " );
			}
			if ( null != devis.getVille() ) {
				sbAdresse.append( devis.getVille() );
			}
		}

		return sbAdresse.toString();
	}

	/**
	 * Retourne l'identité du client à partir d'une intervention
	 *
	 * @param intervention L'intervention
	 * @return L'identité du client
	 */
	private String getClient( CRMPhonePlanningInterventionDTO intervention ) {
		StringBuilder sbClient = new StringBuilder();

		if ( null != intervention && null != intervention.getClient() ) {
			if ( null != intervention.getClient().getCivilite() ) {
				sbClient.append( intervention.getClient().getCivilite() ).append( " " );
			}
			if ( null != intervention.getClient().getNom() ) {
				sbClient.append( intervention.getClient().getNom() ).append( " " );
			}
			if ( null != intervention.getClient().getPrenom() ) {
				sbClient.append( intervention.getClient().getPrenom() );
			}
		}

		return sbClient.toString();
	}

	/**
	 * Retourne l'adresse d'une intervention en chaine de caractères
	 *
	 * @param intervention L'intervention
	 * @return L'adresse
	 */
	private String getAdresse( CRMPhonePlanningInterventionDTO intervention ) {
		StringBuilder sbAdresse = new StringBuilder();

		if ( null != intervention ) {
			if ( null != intervention.getAdresse() ) {
				sbAdresse.append( intervention.getAdresse() ).append( " " );
			}
			if ( null != intervention.getAdresseComplement() ) {
				sbAdresse.append( intervention.getAdresseComplement() ).append( " " );
			}
			if ( null != intervention.getCodePostal() ) {
				sbAdresse.append( intervention.getCodePostal() ).append( " " );
			}
			if ( null != intervention.getVille() ) {
				sbAdresse.append( intervention.getVille() ).append( " " );
			}
		}
		return sbAdresse.toString();
	}
}
