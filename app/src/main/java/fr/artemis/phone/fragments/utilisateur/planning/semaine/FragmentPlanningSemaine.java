package fr.artemis.phone.fragments.utilisateur.planning.semaine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.artemis.phone.BuildConfig;
import fr.artemis.phone.R;
import fr.artemis.phone.utils.DateUtils;
import fr.artemis.phone.utils.SessionPhone;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.WsUtil;

/**
 * Fragment des plannings par semaine
 */
public class FragmentPlanningSemaine extends Fragment implements WsCaller {

	@BindView( R.id.layoutCalendarView )
	FrameLayout layoutCalendarView;

	@BindView( R.id.calendarView )
	CalendarView calendarView;

	@BindView( R.id.btPrevDate )
	ImageButton btPrevDate;

	@BindView( R.id.tvDateDuJour )
	TextView tvDateDuJour;

	@BindView( R.id.btNextDate )
	ImageButton btNextDate;

	@BindView( R.id.btShowCalendar )
	ImageButton btShowCalendar;

	@BindView( R.id.layoutPlanning )
	LinearLayout layoutPlanning;

	// La date du jour
	private LocalDate dateEnCours = LocalDate.now();

	// Le numero de semaine en cours
	private int numSemaine;

	@Nullable
	@Override
	public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
		View view = inflater.inflate( R.layout.fragment_planning_semaine, container, false );

		ButterKnife.bind( this, view );

		assert null != getActivity();

		initDatas();

		initView();

		initEvents();

		return view;
	}

	/**
	 * Initialisation de la vue
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
		calendarView.setOnDateChangeListener( ( view, year, month, dayOfMonth ) -> modifyDateSelected( year, month + 1, dayOfMonth ) );
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

		calendarView.setDate( calendar.getTimeInMillis() );

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

		calendarView.setDate( calendar.getTimeInMillis() );

		checkDateUpdated();
	}

	/**
	 * Modification de la date selectionnée à partir du calendrier
	 *
	 * @param year  L'année
	 * @param month Le mois
	 * @param day   Le jour
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
		layoutCalendarView.setVisibility( View.GONE );

		checkDateUpdated();
	}

	/**
	 * Affichage / Masquage du calendrier
	 */
	private void switchCalendarVisibility() {
		if ( layoutCalendarView.getVisibility() == View.VISIBLE ) {
			btShowCalendar.setImageResource( R.drawable.ic_bt_drop_down );
			layoutCalendarView.setVisibility( View.GONE );
		} else {
			btShowCalendar.setImageResource( R.drawable.ic_bt_drop_up );
			layoutCalendarView.setVisibility( View.VISIBLE );
		}
	}

	/**
	 * Verification si la semaine a changé
	 */
	private void checkDateUpdated() {
		int newNumWeek = DateUtils.getWeekNumber( dateEnCours );
		if ( newNumWeek != numSemaine ) {
			initDatas();
		}
	}

	/**
	 * Affichage d'un message indiquant que le planning n'est pas disponible
	 */
	private void showEmptyPlanning() {
		layoutPlanning.removeAllViews();
		TextView tv = new TextView( getContext() );
		tv.setText( R.string.planning_indisponible );
		layoutPlanning.addView( tv );
	}

	/**
	 * Initialisation des données
	 */
	private void initDatas() {
		numSemaine = DateUtils.getWeekNumber( dateEnCours );

		WsUtil.getPlanningFile( this, null, dateEnCours );
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
			case GET_PLANNING_SEMAINE:
				String fichier = (String) response;

				if ( null != fichier ) {
					openDocument( fichier );
				} else {
					showEmptyPlanning();
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