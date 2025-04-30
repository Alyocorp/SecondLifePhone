package fr.artemis.phone.fragments.utilisateur.rapportchantier.selection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.artemis.phone.R;
import fr.artemis.phone.activities.main.MainActivity;
import fr.artemis.phone.dto.CRMPhonePlanningDTO;
import fr.artemis.phone.dto.CRMPhonePlanningInterventionDTO;
import fr.artemis.phone.utils.DateUtils;
import fr.artemis.phone.utils.SessionPhone;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.WsUtil;

/**
 * Fragment de selection de rapport de chantier
 */
public class FragmentSelectionRapportChantier extends Fragment implements WsCaller {

	@BindView( R.id.layoutCalendarViewUserRapportChantier )
	FrameLayout layoutCalendarViewUserRapportChantier;

	@BindView( R.id.calendarViewUserRapportChantier )
	CalendarView calendarViewUserRapportChantier;

	@BindView( R.id.btPrevDate )
	ImageButton btPrevDate;

	@BindView( R.id.tvDateDuJour )
	TextView tvDateDuJour;

	@BindView( R.id.btNextDate )
	ImageButton btNextDate;

	@BindView( R.id.btShowCalendar )
	ImageButton btShowCalendar;

	@BindView( R.id.layoutSelectionRapportChantier )
	LinearLayout layoutSelectionRapportChantier;

	// La date du jour
	private LocalDate dateEnCours = LocalDate.now();

	// La date de début des interventions du planning chargés en mémoire
	private LocalDate startDate = DateUtils.getFirstDayOfWeek( LocalDate.now() );

	// La date de fin des interventions du planning chargés en mémoire
	private LocalDate endDate = DateUtils.getLastDayOfWeek( LocalDate.now() );

	private List<CRMPhonePlanningDTO> listeInterDeLaSemaine = null;

	@Nullable
	@Override
	public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
		View view = inflater.inflate( R.layout.fragment_user_selection_rapport_chantier, container, false );

		ButterKnife.bind( this, view );

		assert null != getActivity();

		initDatas();

		initView();

		initEvents();

		return view;
	}

	/**
	 * Récupération des données
	 */
	private void initDatas() {
		WsUtil.getListePlanningInterventionBetween( this, null, startDate, endDate );
	}

	/**
	 * Initilisation de la vue
	 */
	private void initView() {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "dd/MM/yyyy" );
		String dateFormattee = dateEnCours.format( formatter );
		tvDateDuJour.setText( dateFormattee );
	}

	/**
	 * Initialisation des evenements
	 */
	private void initEvents() {
		btShowCalendar.setOnClickListener( view -> switchCalendarVisibility() );
		btPrevDate.setOnClickListener( view -> decreaseCurrentDate() );
		btNextDate.setOnClickListener( view -> increaseCurrentDate() );

		// month + 1 vu que Calendar.MONTH commence à 0 (pour janvier)
		calendarViewUserRapportChantier.setOnDateChangeListener( ( view, year, month, dayOfMonth ) -> modifyDateSelected( year, month + 1, dayOfMonth ) );
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
		calendar.set( Calendar.MONTH, dateEnCours.getMonthValue() - 1);
		calendar.set( Calendar.DAY_OF_MONTH, dateEnCours.getDayOfMonth() );

		calendarViewUserRapportChantier.setDate( calendar.getTimeInMillis() );

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
		calendar.set( Calendar.MONTH, dateEnCours.getMonthValue() - 1);
		calendar.set( Calendar.DAY_OF_MONTH, dateEnCours.getDayOfMonth() );

		calendarViewUserRapportChantier.setDate( calendar.getTimeInMillis() );

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
		layoutCalendarViewUserRapportChantier.setVisibility( View.GONE );

		checkDateUpdated();
	}

	/**
	 * Si la date change, il peut être nécessaire de recharger la liste des interventions
	 */
	private void checkDateUpdated() {
		// Mise à jour de l'intervalle de dates et des interventions si nécéssaire
		if ( !startDate.equals( DateUtils.getFirstDayOfWeek( dateEnCours ) ) && !endDate.equals( DateUtils.getLastDayOfWeek( dateEnCours ) ) ) {
			startDate = DateUtils.getFirstDayOfWeek( dateEnCours );
			endDate = DateUtils.getLastDayOfWeek( dateEnCours );

			initDatas();
		}
	}

	/**
	 * Affichage / Masquage du calendrier
	 */
	private void switchCalendarVisibility() {
		if ( layoutCalendarViewUserRapportChantier.getVisibility() == View.VISIBLE ) {
			btShowCalendar.setImageResource( R.drawable.ic_bt_drop_down );
			layoutCalendarViewUserRapportChantier.setVisibility( View.GONE );
		} else {
			btShowCalendar.setImageResource( R.drawable.ic_bt_drop_up );
			layoutCalendarViewUserRapportChantier.setVisibility( View.VISIBLE );
		}
	}

	/**
	 * Affichage de la liste des interventions de la semaine
	 */
	private void setViewWithDatas() {

		layoutSelectionRapportChantier.removeAllViews();

		if ( null != listeInterDeLaSemaine && !listeInterDeLaSemaine.isEmpty() ) {

			List<CRMPhonePlanningInterventionDTO> listeIntersDuSecteur = new ArrayList<>();

			boolean toAdd;

			for ( CRMPhonePlanningDTO planning : listeInterDeLaSemaine ) {
				if ( planning.getSecteur().getSecteur().equals( SessionPhone.getInstance().getSalarie().getSecteur().getSecteur() ) ) {

					for ( CRMPhonePlanningInterventionDTO interInPlanning : planning.getListeInterventions() ) {
						toAdd = true;

						for ( CRMPhonePlanningInterventionDTO interInList : listeIntersDuSecteur ) {
							if ( interInList.getFkDevis().equals( interInPlanning.getFkDevis() ) ) {
								toAdd = false;
							}
						}

						if ( toAdd ) {
							listeIntersDuSecteur.add( interInPlanning );
						}
					}
				}
			}

			Collections.sort( listeIntersDuSecteur, ( i1, i2 ) -> {
				if ( null != i1.getCodePostal() && null != i2.getCodePostal() ) {
					return i1.getCodePostal().compareTo( i2.getCodePostal() );
				} else {
					return -1;
				}
			} );

			for ( CRMPhonePlanningInterventionDTO inter : listeIntersDuSecteur ) {
				Button btInter = new Button( getContext() );
				String str = inter.getCodePostal() + " - " + inter.getClient().getCivilite() + " " + inter.getClient().getNom() + " - " + inter.getVolume() + " m³";
				btInter.setText( str );
				layoutSelectionRapportChantier.addView( btInter );
				btInter.setOnClickListener( v -> ( (MainActivity) getActivity() ).showRapportChantier( dateEnCours, inter ) );
			}
		}
	}

	@Override
	public void notifyResponse( WsName wsName, Map<String, Object> callbackResources, Object response ) {
		switch ( wsName ) {

			case GET_LISTE_PLANNINGS_INTERVENTION_BETWEEN :
				if ( null != response ) {
					listeInterDeLaSemaine = (List<CRMPhonePlanningDTO>) response;

					setViewWithDatas();
				}
				break;
			default :
				throw new IllegalStateException( "Reception d'une reponse de webservice provenant de : " + wsName + ". Les données associées sont : " + response );
		}
	}

	@Override
	public Fragment getFragmentSource() {
		return this;
	}
}