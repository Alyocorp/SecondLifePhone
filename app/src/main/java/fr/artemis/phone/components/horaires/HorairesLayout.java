package fr.artemis.phone.components.horaires;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import fr.artemis.phone.R;
import fr.artemis.phone.fragments.utilisateur.horaires.FragmentUserHoraires;
import fr.artemis.phone.utils.DateUtils;

/**
 * Ligne de saisie d'horaire de travail pour un salarié.
 * Colonnes :
 * DATE | Début matin | Fin matin | Début après midi | Fin après midi | Congés | Récup | Arret
 */
public class HorairesLayout extends LinearLayout {

	private TextView tvJour;

	private EditText etDebutAm;

	private EditText etFinAm;

	private EditText etDebutPm;

	private EditText etFinPm;

	private CheckBox cbConges;

	private CheckBox cbRecuperation;

	private CheckBox cbArret;

	private LocalDate date;

	private Map<String, FragmentUserHoraires.TextWatcherHoraires> mapWatchers;

	private Map<String, FragmentUserHoraires.CheckedListener> mapListeners;

	public HorairesLayout( Context context, LocalDate date, Map<String, FragmentUserHoraires.TextWatcherHoraires> mapWatchers, Map<String, FragmentUserHoraires.CheckedListener> mapListeners ) {
		super( context );
		this.date = date;
		this.mapWatchers = mapWatchers;
		this.mapListeners = mapListeners;
		for ( Map.Entry<String, FragmentUserHoraires.TextWatcherHoraires> entry : mapWatchers.entrySet() ) {
			entry.getValue().setLayout( this );
		}
		for ( Map.Entry<String, FragmentUserHoraires.CheckedListener> entry : mapListeners.entrySet() ) {
			entry.getValue().setLayout( this );
		}

		init();
	}

	private void init() {

		List<LocalDate> listeJoursFeries = DateUtils.listeCalendarToListeLocalDate( DateUtils.getJourFeries( date.getYear() ) );

		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams( 0, ViewGroup.LayoutParams.MATCH_PARENT, 1 );
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams( 0, ViewGroup.LayoutParams.MATCH_PARENT, 2 );

		tvJour = new TextView( getContext() );
		tvJour.setText( String.valueOf( date.getDayOfMonth() ) );
		tvJour.setLayoutParams( lp1 );
		tvJour.setBackground( ContextCompat.getDrawable( getContext(), R.drawable.bordergrey ) );
		tvJour.setGravity( Gravity.CENTER );

		etDebutAm = new EditText( getContext() );
		etDebutAm.setInputType( InputType.TYPE_CLASS_NUMBER );
		etDebutAm.setLayoutParams( lp2 );
		etDebutAm.setBackground( ContextCompat.getDrawable( getContext(), R.drawable.bordergrey ) );
		etDebutAm.setGravity( Gravity.CENTER );
		etDebutAm.setTextSize( 14 );

		etFinAm = new EditText( getContext() );
		etFinAm.setLayoutParams( lp2 );
		etFinAm.setBackground( ContextCompat.getDrawable( getContext(), R.drawable.bordergrey ) );
		etFinAm.setGravity( Gravity.CENTER );
		etFinAm.setTextSize( 14 );

		etDebutPm = new EditText( getContext() );
		etDebutPm.setLayoutParams( lp2 );
		etDebutPm.setBackground( ContextCompat.getDrawable( getContext(), R.drawable.bordergrey ) );
		etDebutPm.setGravity( Gravity.CENTER );
		etDebutPm.setTextSize( 14 );

		etFinPm = new EditText( getContext() );
		etFinPm.setLayoutParams( lp2 );
		etFinPm.setBackground( ContextCompat.getDrawable( getContext(), R.drawable.bordergrey ) );
		etFinPm.setGravity( Gravity.CENTER );
		etFinPm.setTextSize( 14 );

		cbConges = new CheckBox( getContext() );
		cbConges.setLayoutParams( lp1 );
		cbConges.setBackground( ContextCompat.getDrawable( getContext(), R.drawable.bordergrey ) );

		cbRecuperation = new CheckBox( getContext() );
		cbRecuperation.setLayoutParams( lp1 );
		cbRecuperation.setBackground( ContextCompat.getDrawable( getContext(), R.drawable.bordergrey ) );

		cbArret = new CheckBox( getContext() );
		cbArret.setLayoutParams( lp1 );
		cbArret.setBackground( ContextCompat.getDrawable( getContext(), R.drawable.bordergrey ) );

		// TODO listeners sur les checkboxes

		etDebutAm.setOnClickListener( ( v ) -> {
			InputMethodManager imm = (InputMethodManager) getContext().getSystemService( Context.INPUT_METHOD_SERVICE );
			imm.hideSoftInputFromWindow( v.getWindowToken(), 0 );
			Calendar mcurrentTime = Calendar.getInstance();
			int hour = mcurrentTime.get( Calendar.HOUR_OF_DAY );
			int minute = mcurrentTime.get( Calendar.MINUTE );
			TimePickerDialog.OnTimeSetListener listener = ( timePicker, selectedHour, selectedMinute ) -> {
				String minuteStr = String.valueOf( selectedMinute );
				if ( minuteStr.length() == 1 ) {
					minuteStr = "0" + minuteStr;
				}
				etDebutAm.setText( selectedHour + " : " + minuteStr );
			};
			TimePickerDialog mTimePicker = new TimePickerDialog( getContext(), listener, hour, minute, true );
			mTimePicker.show();
		} );

		etDebutAm.setOnFocusChangeListener( ( v, hasFocus ) -> {
			if ( hasFocus ) {
				InputMethodManager imm = (InputMethodManager) getContext().getSystemService( Context.INPUT_METHOD_SERVICE );
				imm.hideSoftInputFromWindow( v.getWindowToken(), 0 );
				Calendar mcurrentTime = Calendar.getInstance();
				int hour = mcurrentTime.get( Calendar.HOUR_OF_DAY );
				int minute = mcurrentTime.get( Calendar.MINUTE );
				TimePickerDialog.OnTimeSetListener listener = ( timePicker, selectedHour, selectedMinute ) -> {
					String minuteStr = String.valueOf( selectedMinute );
					if ( minuteStr.length() == 1 ) {
						minuteStr = "0" + minuteStr;
					}
					etDebutAm.setText( selectedHour + " : " + minuteStr );
				};
				TimePickerDialog mTimePicker = new TimePickerDialog( getContext(), listener, hour, minute, true );
				mTimePicker.show();
			}
		} );
		etDebutAm.setPadding( 0, 0, 0, 0 );

		etFinAm.setOnClickListener( ( v ) -> {
			InputMethodManager imm = (InputMethodManager) getContext().getSystemService( Context.INPUT_METHOD_SERVICE );
			imm.hideSoftInputFromWindow( v.getWindowToken(), 0 );
			Calendar mcurrentTime = Calendar.getInstance();
			int hour = mcurrentTime.get( Calendar.HOUR_OF_DAY );
			int minute = mcurrentTime.get( Calendar.MINUTE );
			TimePickerDialog.OnTimeSetListener listener = ( timePicker, selectedHour, selectedMinute ) -> {
				String minuteStr = String.valueOf( selectedMinute );
				if ( minuteStr.length() == 1 ) {
					minuteStr = "0" + minuteStr;
				}
				etFinAm.setText( selectedHour + " : " + minuteStr );
			};
			TimePickerDialog mTimePicker = new TimePickerDialog( getContext(), listener, hour, minute, true );
			mTimePicker.show();
		} );
		etFinAm.setOnFocusChangeListener( ( v, hasFocus ) -> {
			if ( hasFocus ) {
				InputMethodManager imm = (InputMethodManager) getContext().getSystemService( Context.INPUT_METHOD_SERVICE );
				imm.hideSoftInputFromWindow( v.getWindowToken(), 0 );
				Calendar mcurrentTime = Calendar.getInstance();
				int hour = mcurrentTime.get( Calendar.HOUR_OF_DAY );
				int minute = mcurrentTime.get( Calendar.MINUTE );
				TimePickerDialog.OnTimeSetListener listener = ( timePicker, selectedHour, selectedMinute ) -> {
					String minuteStr = String.valueOf( selectedMinute );
					if ( minuteStr.length() == 1 ) {
						minuteStr = "0" + minuteStr;
					}
					etFinAm.setText( selectedHour + " : " + minuteStr );
				};
				TimePickerDialog mTimePicker = new TimePickerDialog( getContext(), listener, hour, minute, true );
				mTimePicker.show();
			}
		} );
		etFinAm.setPadding( 0, 0, 0, 0 );

		etDebutPm.setOnClickListener( ( v ) -> {
			InputMethodManager imm = (InputMethodManager) getContext().getSystemService( Context.INPUT_METHOD_SERVICE );
			imm.hideSoftInputFromWindow( v.getWindowToken(), 0 );
			Calendar mcurrentTime = Calendar.getInstance();
			int hour = mcurrentTime.get( Calendar.HOUR_OF_DAY );
			int minute = mcurrentTime.get( Calendar.MINUTE );
			TimePickerDialog.OnTimeSetListener listener = ( timePicker, selectedHour, selectedMinute ) -> {
				String minuteStr = String.valueOf( selectedMinute );
				if ( minuteStr.length() == 1 ) {
					minuteStr = "0" + minuteStr;
				}
				etDebutPm.setText( selectedHour + " : " + minuteStr );
			};
			TimePickerDialog mTimePicker = new TimePickerDialog( getContext(), listener, hour, minute, true );
			mTimePicker.show();
		} );
		etDebutPm.setOnFocusChangeListener( ( v, hasFocus ) -> {
			if ( hasFocus ) {
				InputMethodManager imm = (InputMethodManager) getContext().getSystemService( Context.INPUT_METHOD_SERVICE );
				imm.hideSoftInputFromWindow( v.getWindowToken(), 0 );
				Calendar mcurrentTime = Calendar.getInstance();
				int hour = mcurrentTime.get( Calendar.HOUR_OF_DAY );
				int minute = mcurrentTime.get( Calendar.MINUTE );
				TimePickerDialog.OnTimeSetListener listener = ( timePicker, selectedHour, selectedMinute ) -> {
					String minuteStr = String.valueOf( selectedMinute );
					if ( minuteStr.length() == 1 ) {
						minuteStr = "0" + minuteStr;
					}
					etDebutPm.setText( selectedHour + " : " + minuteStr );
				};
				TimePickerDialog mTimePicker = new TimePickerDialog( getContext(), listener, hour, minute, true );
				mTimePicker.show();
			}
		} );
		etDebutPm.setPadding( 0, 0, 0, 0 );

		etFinPm.setOnClickListener( ( v ) -> {
			InputMethodManager imm = (InputMethodManager) getContext().getSystemService( Context.INPUT_METHOD_SERVICE );
			imm.hideSoftInputFromWindow( v.getWindowToken(), 0 );
			Calendar mcurrentTime = Calendar.getInstance();
			int hour = mcurrentTime.get( Calendar.HOUR_OF_DAY );
			int minute = mcurrentTime.get( Calendar.MINUTE );
			TimePickerDialog.OnTimeSetListener listener = ( timePicker, selectedHour, selectedMinute ) -> {
				String minuteStr = String.valueOf( selectedMinute );
				if ( minuteStr.length() == 1 ) {
					minuteStr = "0" + minuteStr;
				}
				etFinPm.setText( selectedHour + " : " + minuteStr );
			};
			TimePickerDialog mTimePicker = new TimePickerDialog( getContext(), listener, hour, minute, true );
			mTimePicker.show();
		} );
		etFinPm.setOnFocusChangeListener( ( v, hasFocus ) -> {
			if ( hasFocus ) {
				InputMethodManager imm = (InputMethodManager) getContext().getSystemService( Context.INPUT_METHOD_SERVICE );
				imm.hideSoftInputFromWindow( v.getWindowToken(), 0 );
				Calendar mcurrentTime = Calendar.getInstance();
				int hour = mcurrentTime.get( Calendar.HOUR_OF_DAY );
				int minute = mcurrentTime.get( Calendar.MINUTE );
				TimePickerDialog.OnTimeSetListener listener = ( timePicker, selectedHour, selectedMinute ) -> {
					String minuteStr = String.valueOf( selectedMinute );
					if ( minuteStr.length() == 1 ) {
						minuteStr = "0" + minuteStr;
					}
					etFinPm.setText( selectedHour + " : " + minuteStr );
				};
				TimePickerDialog mTimePicker = new TimePickerDialog( getContext(), listener, hour, minute, true );
				mTimePicker.show();
			}
		} );
		etFinPm.setPadding( 0, 0, 0, 0 );

		setWatcherEnabled( false );

		this.addView( tvJour );
		this.addView( etDebutAm );
		this.addView( etFinAm );
		this.addView( etDebutPm );
		this.addView( etFinPm );
		this.addView( cbConges );
		this.addView( cbRecuperation );
		this.addView( cbArret );

		if ( date.getDayOfWeek().equals( DayOfWeek.SATURDAY ) || date.getDayOfWeek().equals( DayOfWeek.SUNDAY ) || listeJoursFeries.contains( date ) ) {
			this.setBackgroundColor( Color.BLACK );
		}

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, 64, 12 );
		this.setLayoutParams( lp );
	}

	public CheckBox getCbArret() {
		return cbArret;
	}

	public CheckBox getCbConges() {
		return cbConges;
	}

	public CheckBox getCbRecuperation() {
		return cbRecuperation;
	}

	public TextView getTvJour() {
		return tvJour;
	}

	public EditText getEtDebutAm() {
		return etDebutAm;
	}

	public EditText getEtDebutPm() {
		return etDebutPm;
	}

	public EditText getEtFinAm() {
		return etFinAm;
	}

	public EditText getEtFinPm() {
		return etFinPm;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setWatcherEnabled( boolean textWatcherEnabled ) {
		if ( !textWatcherEnabled ) {
			etDebutAm.removeTextChangedListener( mapWatchers.get( "DEBUT_MATIN" ) );
			etDebutPm.removeTextChangedListener( mapWatchers.get( "DEBUT_APREM" ) );
			etFinAm.removeTextChangedListener( mapWatchers.get( "FIN_MATIN" ) );
			etFinPm.removeTextChangedListener( mapWatchers.get( "FIN_APREM" ) );
			cbArret.setOnCheckedChangeListener( null );
			cbConges.setOnCheckedChangeListener( null );
			cbRecuperation.setOnCheckedChangeListener( null );
		} else {
			etDebutAm.addTextChangedListener( mapWatchers.get( "DEBUT_MATIN" ) );
			etDebutPm.addTextChangedListener( mapWatchers.get( "DEBUT_APREM" ) );
			etFinAm.addTextChangedListener( mapWatchers.get( "FIN_MATIN" ) );
			etFinPm.addTextChangedListener( mapWatchers.get( "FIN_APREM" ) );
			cbArret.setOnCheckedChangeListener( mapListeners.get( "ARRET" ) );
			cbConges.setOnCheckedChangeListener( mapListeners.get( "CONGES" ) );
			cbRecuperation.setOnCheckedChangeListener( mapListeners.get( "RECUP" ) );
		}
	}

	public Map<String, FragmentUserHoraires.TextWatcherHoraires> getWatchers() {
		return this.mapWatchers;
	}

	public Map<String, FragmentUserHoraires.CheckedListener> getListeners() {
		return mapListeners;
	}
}