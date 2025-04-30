package fr.artemis.phone.fragments.utilisateur.horaires;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.artemis.phone.R;
import fr.artemis.phone.activities.main.MainActivity;
import fr.artemis.phone.components.horaires.HorairesLayout;
import fr.artemis.phone.dto.CRMPhoneHorairesDTO;
import fr.artemis.phone.utils.DateUtils;
import fr.artemis.phone.utils.SessionPhone;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.WsUtil;

/**
 * Fragment des horaires des utilisateurs
 */
public class FragmentUserHoraires extends Fragment implements WsCaller {

	@BindView( R.id.layoutCalendarViewUserHoraires )
	FrameLayout layoutCalendarViewUserHoraires;

	@BindView( R.id.calendarViewUserHoraires )
	CalendarView calendarViewUserHoraires;

	@BindView( R.id.btPrevDate )
	ImageButton btPrevDate;

	@BindView( R.id.tvDateDuJour )
	TextView tvDateDuJour;

	@BindView( R.id.btNextDate )
	ImageButton btNextDate;

	@BindView( R.id.btShowCalendar )
	ImageButton btShowCalendar;

	@BindView( R.id.layoutHeures )
	LinearLayout layoutHeures;

	@BindView( R.id.tvHorairesTitleJour )
	TextView tvHorairesTitleJour;

	@BindView( R.id.tvBalanceTotale )
	TextView tvBalanceTotale;

	// La liste des horaires
	List<CRMPhoneHorairesDTO> listeHoraires;

	// La date du jour
	private LocalDate dateEnCours = LocalDate.now();

	// Map contenant la date associé à la ligne de saisie utilisateur
	private Map<LocalDate, HorairesLayout> mapLignesHoraires = new HashMap<>();

	@Nullable
	@Override
	public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
		View view = inflater.inflate( R.layout.fragment_user_horaires, container, false );

		ButterKnife.bind( this, view );

		assert null != getActivity();

		initDatas();

		initView();

		initEvents();

		return view;
	}

	/**
	 * Initialisation des données pour le fragment des horaires utilisateur
	 */
	private void initDatas() {
		WsUtil.getListeHoraires( this, null, SessionPhone.getInstance().getUserDto().getId(), dateEnCours.getYear(), dateEnCours.getMonthValue() );
	}

	/**
	 * Initialisation des evenements
	 */
	private void initEvents() {
		btShowCalendar.setOnClickListener( view -> switchCalendarVisibility() );
		btPrevDate.setOnClickListener( view -> decreaseCurrentDate() );
		btNextDate.setOnClickListener( view -> increaseCurrentDate() );

		// month + 1 vu que Calendar.MONTH commence à 0 (pour janvier)
		calendarViewUserHoraires.setOnDateChangeListener( ( view, year, month, dayOfMonth ) -> modifyDateSelected( year, month + 1, dayOfMonth ) );
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

		calendarViewUserHoraires.setDate( calendar.getTimeInMillis() );

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

		calendarViewUserHoraires.setDate( calendar.getTimeInMillis() );

		checkDateUpdated();
	}

	/**
	 * Modification de la date selectionnée à partir du calendrier
	 *
	 * @param year
	 *            L'année
	 * @param month
	 *            Le mois
	 * @param day
	 *            Le jour
	 */
	private void modifyDateSelected( int year, int month, int day ) {
		dateEnCours = dateEnCours.withYear( year );
		dateEnCours = dateEnCours.withMonth( month );
		dateEnCours = dateEnCours.withDayOfMonth( day );

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "dd/MM/yyyy" );
		String dateFormattee = dateEnCours.format( formatter );
		tvDateDuJour.setText( dateFormattee );

		// On cache le calendrier après une selection utilisateur
		btShowCalendar.setImageResource( R.drawable.ic_bt_drop_down );
		layoutCalendarViewUserHoraires.setVisibility( View.GONE );

		checkDateUpdated();
	}

	/**
	 * Si la date change, il peut être nécessaire de recharge la liste des dates pour les horaires, les weekends et jours fériés
	 */
	private void checkDateUpdated() {
		// Réinitialisation de l'IHM si le mois a changé
		if ( null == mapLignesHoraires.get( dateEnCours ) ) {
			initDatas();

			initView();
		}
	}

	/**
	 * Intialisation de la vue
	 */
	@SuppressLint( "SetTextI18n" )
	private void initView() {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "dd/MM/yyyy" );
		String dateFormattee = dateEnCours.format( formatter );
		tvDateDuJour.setText( dateFormattee );

		layoutHeures.removeAllViews();
		mapLignesHoraires.clear();

		Calendar cal = Calendar.getInstance();
		cal.set( Calendar.YEAR, dateEnCours.getYear() );
		cal.set( Calendar.MONTH, dateEnCours.getMonthValue() - 1 );
		cal.set( Calendar.DAY_OF_MONTH, dateEnCours.getDayOfMonth() );
		int nbJoursDuMois = cal.getActualMaximum( Calendar.DAY_OF_MONTH );

		HorairesLayout layoutHoraire;

		LocalDate dateDeLaLigne = dateEnCours;

		for ( int line = 1 ; line <= nbJoursDuMois ; line++ ) {

			dateDeLaLigne = dateDeLaLigne.withDayOfMonth( line );

			Map<String, TextWatcherHoraires> mapWatchers = new HashMap<>();
			mapWatchers.put( "DEBUT_MATIN", new TextWatcherHoraires() );
			mapWatchers.put( "FIN_MATIN", new TextWatcherHoraires() );
			mapWatchers.put( "DEBUT_APREM", new TextWatcherHoraires() );
			mapWatchers.put( "FIN_APREM", new TextWatcherHoraires() );

			Map<String, CheckedListener> mapListeners = new HashMap<>();
			mapListeners.put( "ARRET", new CheckedListener() );
			mapListeners.put( "CONGES", new CheckedListener() );
			mapListeners.put( "RECUP", new CheckedListener() );

			layoutHoraire = new HorairesLayout( getContext(), dateDeLaLigne, mapWatchers, mapListeners );
			mapLignesHoraires.put( dateDeLaLigne, layoutHoraire );
			layoutHeures.addView( layoutHoraire );
		}
	}

	/**
	 * Injection des données dans la vue
	 */
	private void setViewWithDatas() {
		if ( null != listeHoraires ) {
			for ( CRMPhoneHorairesDTO horaireOnServer : listeHoraires ) {
				HorairesLayout layout = mapLignesHoraires.get( DateUtils.calendarToLocalDate( horaireOnServer.getHoraireDate() ) );

				layout.setWatcherEnabled( false );

				layout.getCbArret().setChecked( horaireOnServer.isArret() );
				layout.getCbConges().setChecked( horaireOnServer.isConges() );
				layout.getCbRecuperation().setChecked( horaireOnServer.isRecup() );

				if ( horaireOnServer.isRecup() ) {
					layout.getEtDebutAm().setText( R.string.heure_recup_0 );
					layout.getEtFinAm().setText( R.string.heure_recup_0 );
					layout.getEtDebutPm().setText( R.string.heure_recup_0 );
					layout.getEtFinPm().setText( R.string.heure_recup_0 );
				}

				if ( null != horaireOnServer.getHeureDebutMatin() ) {
					String heure = String.valueOf( DateUtils.dateToCalendar( horaireOnServer.getHeureDebutMatin() ).get( Calendar.HOUR_OF_DAY ) );
					String minute = String.valueOf( DateUtils.dateToCalendar( horaireOnServer.getHeureDebutMatin() ).get( Calendar.MINUTE ) );
					if ( minute.length() == 1 ) {
						minute = "0" + minute;
					}
					String heureFinale = heure + " : " + minute;
					layout.getEtDebutAm().setText( heureFinale );
				}
				if ( null != horaireOnServer.getHeureFinMatin() ) {
					String heure = String.valueOf( DateUtils.dateToCalendar( horaireOnServer.getHeureFinMatin() ).get( Calendar.HOUR_OF_DAY ) );
					String minute = String.valueOf( DateUtils.dateToCalendar( horaireOnServer.getHeureFinMatin() ).get( Calendar.MINUTE ) );
					if ( minute.length() == 1 ) {
						minute = "0" + minute;
					}
					String heureFinale = heure + " : " + minute;
					layout.getEtFinAm().setText( heureFinale );
				}
				if ( null != horaireOnServer.getHeureDebutAprem() ) {
					String heure = String.valueOf( DateUtils.dateToCalendar( horaireOnServer.getHeureDebutAprem() ).get( Calendar.HOUR_OF_DAY ) );
					String minute = String.valueOf( DateUtils.dateToCalendar( horaireOnServer.getHeureDebutAprem() ).get( Calendar.MINUTE ) );
					if ( minute.length() == 1 ) {
						minute = "0" + minute;
					}
					String heureFinale = heure + " : " + minute;
					layout.getEtDebutPm().setText( heureFinale );
				}
				if ( null != horaireOnServer.getHeureFinAprem() ) {
					String heure = String.valueOf( DateUtils.dateToCalendar( horaireOnServer.getHeureFinAprem() ).get( Calendar.HOUR_OF_DAY ) );
					String minute = String.valueOf( DateUtils.dateToCalendar( horaireOnServer.getHeureFinAprem() ).get( Calendar.MINUTE ) );
					if ( minute.length() == 1 ) {
						minute = "0" + minute;
					}
					String heureFinale = heure + " : " + minute;
					layout.getEtFinPm().setText( heureFinale );
				}

				layout.setWatcherEnabled( true );
			}
		}
	}

	public class CheckedListener implements CompoundButton.OnCheckedChangeListener {

		private HorairesLayout layout;

		public HorairesLayout getLayout() {
			return layout;
		}

		public void setLayout( HorairesLayout layout ) {
			this.layout = layout;
		}

		@Override
		public void onCheckedChanged( CompoundButton buttonView, boolean isChecked ) {

			String typeJourChecked = null;
			for ( Map.Entry<String, CheckedListener> listener : this.layout.getListeners().entrySet() ) {
				if ( this.equals( listener.getValue() ) ) {
					typeJourChecked = listener.getKey();
				}
			}

			CRMPhoneHorairesDTO horaireToUpdate = null;
			for ( CRMPhoneHorairesDTO horaireInList : listeHoraires ) {
				if ( DateUtils.calendarToLocalDate( horaireInList.getHoraireDate() ).equals( this.layout.getDate() ) ) {
					horaireToUpdate = horaireInList;
				}
			}

			switch ( typeJourChecked ) {
				case "CONGES" : {
					this.layout.setWatcherEnabled( false );
					this.layout.getEtDebutAm().setText( "" );
					this.layout.getEtFinAm().setText( "" );
					this.layout.getEtDebutPm().setText( "" );
					this.layout.getEtFinPm().setText( "" );
					this.layout.getCbArret().setChecked( false );
					this.layout.getCbRecuperation().setChecked( false );
					horaireToUpdate.setHeureDebutMatin( null );
					horaireToUpdate.setHeureFinMatin( null );
					horaireToUpdate.setHeureDebutAprem( null );
					horaireToUpdate.setHeureFinAprem( null );
					horaireToUpdate.setConges( isChecked );
					horaireToUpdate.setArret( false );
					horaireToUpdate.setRecup( false );
					this.layout.setWatcherEnabled( true );
					break;
				}
				case "RECUP" : {
					this.layout.setWatcherEnabled( false );
					if ( isChecked ) {
						this.layout.getEtDebutAm().setText( R.string.heure_recup_0 );
						this.layout.getEtFinAm().setText( R.string.heure_recup_0 );
						this.layout.getEtDebutPm().setText( R.string.heure_recup_0 );
						this.layout.getEtFinPm().setText( R.string.heure_recup_0 );
					} else {
						this.layout.getEtDebutAm().setText( "" );
						this.layout.getEtFinAm().setText( "" );
						this.layout.getEtDebutPm().setText( "" );
						this.layout.getEtFinPm().setText( "" );
					}
					this.layout.getCbConges().setChecked( false );
					this.layout.getCbArret().setChecked( false );
					horaireToUpdate.setHeureDebutMatin( null );
					horaireToUpdate.setHeureFinMatin( null );
					horaireToUpdate.setHeureDebutAprem( null );
					horaireToUpdate.setHeureFinAprem( null );
					horaireToUpdate.setConges( false );
					horaireToUpdate.setArret( false );
					horaireToUpdate.setRecup( isChecked );
					this.layout.setWatcherEnabled( true );
					break;
				}
				case "ARRET" : {
					this.layout.setWatcherEnabled( false );
					this.layout.getEtDebutAm().setText( "" );
					this.layout.getEtFinAm().setText( "" );
					this.layout.getEtDebutPm().setText( "" );
					this.layout.getEtFinPm().setText( "" );
					this.layout.getCbConges().setChecked( false );
					this.layout.getCbRecuperation().setChecked( false );
					horaireToUpdate.setHeureDebutMatin( null );
					horaireToUpdate.setHeureFinMatin( null );
					horaireToUpdate.setHeureDebutAprem( null );
					horaireToUpdate.setHeureFinAprem( null );
					horaireToUpdate.setConges( false );
					horaireToUpdate.setArret( isChecked );
					horaireToUpdate.setRecup( false );
					this.layout.setWatcherEnabled( true );
					break;
				}
				default :
					throw new IllegalStateException( "Champ de saisie inconnu." );

			}

			try {
				( (MainActivity) getActivity() ).removeWaitingScreen();
				WsUtil.updateHoraire( FragmentUserHoraires.this, null, horaireToUpdate );
			} catch ( Exception ex ) {
				ex.printStackTrace();
				FirebaseCrashlytics.getInstance().recordException( ex );
				Log.d( "TAG", "Erreur lors de la recuperation de la liste des horaires." );
			}
		}
	}

	public class TextWatcherHoraires implements TextWatcher {

		private HorairesLayout layout;

		public HorairesLayout getLayout() {
			return layout;
		}

		public void setLayout( HorairesLayout layout ) {
			this.layout = layout;
		}

		@Override
		public void beforeTextChanged( CharSequence charSequence, int i, int i1, int i2 ) {}

		@Override
		public void onTextChanged( CharSequence charSequence, int i, int i1, int i2 ) {}

		@Override
		public void afterTextChanged( Editable editable ) {
			String typeHeureWatched = null;
			for ( Map.Entry<String, TextWatcherHoraires> watchers : this.layout.getWatchers().entrySet() ) {
				if ( this.equals( watchers.getValue() ) ) {
					typeHeureWatched = watchers.getKey();
				}
			}

			CRMPhoneHorairesDTO horaireToUpdate = null;
			for ( CRMPhoneHorairesDTO horaireInList : listeHoraires ) {
				if ( DateUtils.calendarToLocalDate( horaireInList.getHoraireDate() ).equals( this.layout.getDate() ) ) {
					horaireToUpdate = horaireInList;
				}
			}

			try {
				switch ( typeHeureWatched ) {
					case "DEBUT_MATIN" : {
						horaireToUpdate.setHeureDebutMatin( DateUtils.stringTimeToDate( editable.toString() ) );
						break;
					}
					case "FIN_MATIN" : {
						horaireToUpdate.setHeureFinMatin( DateUtils.stringTimeToDate( editable.toString() ) );
						break;
					}
					case "DEBUT_APREM" : {
						horaireToUpdate.setHeureDebutAprem( DateUtils.stringTimeToDate( editable.toString() ) );
						break;
					}
					case "FIN_APREM" : {
						horaireToUpdate.setHeureFinAprem( DateUtils.stringTimeToDate( editable.toString() ) );
						break;
					}
					default :
						throw new IllegalStateException( "Champ de saisie inconnu." );

				}
			} catch ( ParseException e ) {
				e.printStackTrace();
			}

			try {
				( (MainActivity) getActivity() ).showWaitingScreen();
				WsUtil.updateHoraire( FragmentUserHoraires.this, null, horaireToUpdate );
			} catch ( Exception ex ) {
				ex.printStackTrace();
				FirebaseCrashlytics.getInstance().recordException( ex );
				Log.d( "TAG", "Erreur lors de la recuperation de la liste des horaires." );
			}
		}
	}

	/**
	 * Affichage / Masquage du calendrier
	 */
	private void switchCalendarVisibility() {
		if ( layoutCalendarViewUserHoraires.getVisibility() == View.VISIBLE ) {
			btShowCalendar.setImageResource( R.drawable.ic_bt_drop_down );
			layoutCalendarViewUserHoraires.setVisibility( View.GONE );
		} else {
			btShowCalendar.setImageResource( R.drawable.ic_bt_drop_up );
			layoutCalendarViewUserHoraires.setVisibility( View.VISIBLE );
		}
	}

	@Override
	public Fragment getFragmentSource() {
		return this;
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public void notifyResponse( WsName wsSource, Map<String, Object> mapResources, Object result ) {
		switch ( wsSource ) {
			case GET_LISTE_HORAIRES_BY_ID_USER :
				this.listeHoraires = (List<CRMPhoneHorairesDTO>) result;
				setViewWithDatas();
				WsUtil.getBalanceTotaleHoraire( this, null, SessionPhone.getInstance().getUserDto().getId() );
				break;
			case UPDATE_HORAIRE :
				WsUtil.getBalanceTotaleHoraire( this, null, SessionPhone.getInstance().getUserDto().getId() );
				break;

			case GET_BALANCE_TOTALE_HORAIRE :
				this.tvBalanceTotale.setText( String.valueOf( result ) );
				break;

			default :
				throw new IllegalStateException( "Reception d'une reponse de webservice provenant de : " + wsSource + ". Les données associées sont : " + result );
		}
	}
}