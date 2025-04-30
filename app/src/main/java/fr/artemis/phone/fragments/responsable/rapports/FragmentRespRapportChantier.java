package fr.artemis.phone.fragments.responsable.rapports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.artemis.phone.R;
import fr.artemis.phone.dto.CRMPhonePlanningRapportDTO;
import fr.artemis.phone.fragments.FragmentProvider;
import fr.artemis.phone.fragments.FragmentTags;
import fr.artemis.phone.fragments.responsable.rapports.adapter.RapportRespPager;
import fr.artemis.phone.fragments.responsable.rapports.subfragments.global.FragmentRespRapportChantierGlobal;
import fr.artemis.phone.fragments.responsable.rapports.subfragments.incoherence.FragmentRespRapportChantierIncoherence;
import fr.artemis.phone.fragments.responsable.rapports.subfragments.intervention.FragmentRespRapportChantierIntervention;
import fr.artemis.phone.fragments.responsable.rapports.subfragments.salarie.FragmentRespRapportChantierSalarie;
import fr.artemis.phone.utils.DateUtils;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.WsUtil;

/**
 * Fragment des rapports de chantier pour un responsable
 */
public class FragmentRespRapportChantier extends Fragment implements WsCaller {

	private static final String RES_DATE_DEB = "dateDebut";

	@BindView( R.id.btPrevDate )
	ImageButton btPrevDate;

	@BindView( R.id.tvDateDuJour )
	TextView tvDateDuJour;

	@BindView( R.id.btNextDate )
	ImageButton btNextDate;

	@BindView( R.id.btShowCalendar )
	ImageButton btShowCalendar;

	@BindView( R.id.layoutCalendarViewRespRapport )
	FrameLayout layoutCalendarViewRespRapport;

	@BindView( R.id.calendarViewRespRapport )
	CalendarView calendarViewRespRapport;

	// Le composant des onglets
	@BindView( R.id.layoutTabRespRapports )
	TabLayout mTabLayout;

	// Le gestionnaire des onglets
	@BindView( R.id.viewPagerRespRapports )
	ViewPager mViewPager;

	// Formatteur de date
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "dd/MM/yyyy" );

	// La date selectionnée par l'utilisateur (par défaut la date du jour)
	private LocalDate dateEnCours = LocalDate.now();

	// Le premier jour de la semaine en fonction de la date selectionnée par
	// l'utilisateur (dateEnCours)
	private LocalDate dateDebut;

	// Le dernier jour de la semaine en fonction de la date selectionnée par
	// l'utilisateur (dateEnCours)
	private LocalDate dateFin;

	// Map contenant les rapports de chantier par date (date de début en clé)
	private Map<LocalDate, List<CRMPhonePlanningRapportDTO>> mapRapports = new HashMap<>();

	@Nullable
	@Override
	public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
		View view = inflater.inflate( R.layout.fragment_resp_rapport_chantier, container, false );

		assert null != getActivity();

		ButterKnife.bind( this, view );

		// Instanciation des fragments contenus dans les onglets
		LinkedHashMap<String, String> mapsOnglets = new LinkedHashMap<>();
		mapsOnglets.put( "Global", FragmentTags.RAPPORT_RESP_GLOBAL.getTagName() );
		mapsOnglets.put( "Chantier", FragmentTags.RAPPORT_RESP_INTERVENTION.getTagName() );
		mapsOnglets.put( "Salarié", FragmentTags.RAPPORT_RESP_SALARIE.getTagName() );
		mapsOnglets.put( "Erreurs", FragmentTags.RAPPORT_RESP_INCOHERENCE.getTagName() );

		// On force l'instanciation de tous les onglets :
		// Android se vautre comme une grosse merde puisqu'il n'instancie pas les
		// onglets qu'il affiche.... logique...
		mViewPager.setOffscreenPageLimit( mapsOnglets.size() - 1 );

		// Mise en forme des onglets
		RapportRespPager adapter = new RapportRespPager( getFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mapsOnglets );
		mViewPager.setAdapter( adapter );
		mTabLayout.setupWithViewPager( mViewPager );

		// Mise en forme de la date du jour par défaut
		String dateFormattee = dateEnCours.format( formatter );
		tvDateDuJour.setText( dateFormattee );
		dateDebut = DateUtils.getFirstDayOfWeek( dateEnCours );
		dateFin = DateUtils.getLastDayOfWeek( dateEnCours );

		initDatas();

		initEvents();

		return view;
	}

	/**
	 * Initialisation des données
	 */
	private void initDatas() {
		Map<String, Object> mapRessources = new HashMap<>();
		mapRessources.put( RES_DATE_DEB, dateDebut );

		WsUtil.getListePlanningRapport( this, mapRessources, dateDebut, dateFin );
	}

	private void initEvents() {
		btShowCalendar.setOnClickListener( view -> switchCalendarVisibility() );
		btPrevDate.setOnClickListener( view -> decreaseCurrentDate() );
		btNextDate.setOnClickListener( view -> increaseCurrentDate() );
	}

	/**
	 * Incrementation de la date en cours
	 */
	private void increaseCurrentDate() {
		dateEnCours = dateEnCours.plusDays( 1 );
		dateDebut = DateUtils.getFirstDayOfWeek( dateEnCours );
		dateFin = DateUtils.getLastDayOfWeek( dateEnCours );
		String dateFormattee = dateEnCours.format( formatter );
		tvDateDuJour.setText( dateFormattee );

		Calendar calendar = Calendar.getInstance();
		calendar.set( Calendar.YEAR, dateEnCours.getYear() );
		calendar.set( Calendar.MONTH, dateEnCours.getMonthValue() - 1 );
		calendar.set( Calendar.DAY_OF_MONTH, dateEnCours.getDayOfMonth() );

		calendarViewRespRapport.setDate( calendar.getTimeInMillis() );

		checkDateUpdated();
	}

	/**
	 * Decrementation de la date en cours
	 */
	private void decreaseCurrentDate() {
		dateEnCours = dateEnCours.minusDays( 1 );
		dateDebut = DateUtils.getFirstDayOfWeek( dateEnCours );
		dateFin = DateUtils.getLastDayOfWeek( dateEnCours );
		String dateFormattee = dateEnCours.format( formatter );
		tvDateDuJour.setText( dateFormattee );

		Calendar calendar = Calendar.getInstance();
		calendar.set( Calendar.YEAR, dateEnCours.getYear() );
		calendar.set( Calendar.MONTH, dateEnCours.getMonthValue() - 1 );
		calendar.set( Calendar.DAY_OF_MONTH, dateEnCours.getDayOfMonth() );

		calendarViewRespRapport.setDate( calendar.getTimeInMillis() );

		checkDateUpdated();
	}

	/**
	 * Affichage / Masquage du calendrier
	 */
	private void switchCalendarVisibility() {
		if ( layoutCalendarViewRespRapport.getVisibility() == View.VISIBLE ) {
			btShowCalendar.setImageResource( R.drawable.ic_bt_drop_down );
			layoutCalendarViewRespRapport.setVisibility( View.GONE );
		} else {
			btShowCalendar.setImageResource( R.drawable.ic_bt_drop_up );
			layoutCalendarViewRespRapport.setVisibility( View.VISIBLE );
		}
	}

	/**
	 * Si la date change, il peut être nécessaire de recharge la liste des dates
	 * pour les horaires, les weekends et jours fériés
	 */
	private void checkDateUpdated() {
		if ( null == mapRapports.get( dateDebut ) ) {
			initDatas();
		}
	}

	/**
	 * Mise en forme de la vue
	 * 
	 * @param dateDesRapports La date des rapports demandés
	 */
	private void setView( LocalDate dateDesRapports ) {
		List<CRMPhonePlanningRapportDTO> listeRapports = mapRapports.get( dateDesRapports );
		if ( null != listeRapports && !listeRapports.isEmpty() ) {
			FragmentRespRapportChantierGlobal fragGlobal = (FragmentRespRapportChantierGlobal) FragmentProvider.getFragment( FragmentTags.RAPPORT_RESP_GLOBAL.getTagName() );
			FragmentRespRapportChantierIntervention fragChantier = (FragmentRespRapportChantierIntervention) FragmentProvider.getFragment( FragmentTags.RAPPORT_RESP_INTERVENTION.getTagName() );
			FragmentRespRapportChantierSalarie fragSalarie = (FragmentRespRapportChantierSalarie) FragmentProvider.getFragment( FragmentTags.RAPPORT_RESP_SALARIE.getTagName() );
			FragmentRespRapportChantierIncoherence fragIncoherence = (FragmentRespRapportChantierIncoherence) FragmentProvider.getFragment( FragmentTags.RAPPORT_RESP_INCOHERENCE.getTagName() );

			// TODO remplir les onglets
		} else {
			// TODO vider les onglets
		}
	}

	@Override
	public void notifyResponse( WsName wsName, Map<String, Object> callbackResources, Object response ) {
		switch ( wsName ) {
			case GET_LISTE_RAPPORTS_CHANTIER_PLANNING:
				if ( null != response && null != callbackResources.get( RES_DATE_DEB ) ) {
					LocalDate dateDemandee = (LocalDate) callbackResources.get( RES_DATE_DEB );
					mapRapports.put( dateDemandee, (List<CRMPhonePlanningRapportDTO>) response );

					setView( dateDemandee );
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