package fr.artemis.phone.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static fr.artemis.phone.lib.ksoap.kobjects.isodate.IsoDate.DATE;
import static fr.artemis.phone.lib.ksoap.kobjects.isodate.IsoDate.DATE_TIME;
import static fr.artemis.phone.lib.ksoap.kobjects.isodate.IsoDate.TIME;

public class DateUtils {

	/**
	 * Retourne la liste des jours fériés d'une années
	 *
	 * @param annee L'année
	 * @return La liste des jours fériés de l'année
	 */
	public static List<Calendar> getJourFeries( int annee ) {
		List<Calendar> datesFeries = new ArrayList<>();

		// Jour de l'an
		Calendar jourAn = new GregorianCalendar( annee, Calendar.JANUARY, 1 );
		datesFeries.add( jourAn );

		// Lundi de pacques
		Calendar pacques = calculLundiPacques( annee );
		datesFeries.add( pacques );

		// Fete du travail
		Calendar premierMai = new GregorianCalendar( annee, Calendar.MAY, 1 );
		datesFeries.add( premierMai );

		// 8 mai
		Calendar huitMai = new GregorianCalendar( annee, Calendar.MAY, 8 );
		datesFeries.add( huitMai );

		// Ascension (= pâques + 38 jours)
		Calendar ascension = new GregorianCalendar( annee, pacques.get( Calendar.MONTH ), pacques.get( Calendar.DAY_OF_MONTH ) );
		ascension.add( Calendar.DAY_OF_MONTH, 38 );
		datesFeries.add( ascension );

		// Pentecôte (= pâques + 49 jours)
		Calendar pentecote = new GregorianCalendar( annee, pacques.get( Calendar.MONTH ), pacques.get( Calendar.DAY_OF_MONTH ) );
		pentecote.add( Calendar.DAY_OF_MONTH, 49 );
		datesFeries.add( pentecote );

		// Fête Nationale
		GregorianCalendar quatorzeJuillet = new GregorianCalendar( annee, Calendar.JULY, 14 );
		datesFeries.add( quatorzeJuillet );

		// Assomption
		GregorianCalendar assomption = new GregorianCalendar( annee, Calendar.AUGUST, 15 );
		datesFeries.add( assomption );

		// La Toussaint
		GregorianCalendar toussaint = new GregorianCalendar( annee, Calendar.NOVEMBER, 1 );
		datesFeries.add( toussaint );

		// L'Armistice
		GregorianCalendar armistice = new GregorianCalendar( annee, Calendar.NOVEMBER, 11 );
		datesFeries.add( armistice );

		// Noël
		GregorianCalendar noel = new GregorianCalendar( annee, Calendar.DECEMBER, 25 );
		datesFeries.add( noel );

		return datesFeries;
	}

	/**
	 * Methode de calcul du lundi de paques
	 *
	 * @param annee L'année dont on recherche le lundi de paques
	 * @return La date du lundi de paques
	 */
	private static Calendar calculLundiPacques( int annee ) {
		int a = annee / 100;
		int b = annee % 100;
		int c = ( 3 * ( a + 25 ) ) / 4;
		int d = ( 3 * ( a + 25 ) ) % 4;
		int e = ( 8 * ( a + 11 ) ) / 25;
		int f = ( 5 * a + b ) % 19;
		int g = ( 19 * f + c - e ) % 30;
		int h = ( f + 11 * g ) / 319;
		int j = ( 60 * ( 5 - d ) + b ) / 4;
		int k = ( 60 * ( 5 - d ) + b ) % 4;
		int m = ( 2 * j - k - g + h ) % 7;
		int n = ( g - h + m + 114 ) / 31;
		int p = ( g - h + m + 114 ) % 31;
		int jour = p + 1;
		int mois = n;

		Calendar date = new GregorianCalendar( annee, mois - 1, jour );
		date.add( Calendar.DAY_OF_MONTH, 1 );
		return date;
	}

	/**
	 * Convertit un Calendar en LocalDate
	 *
	 * @param calendar Le calendar a convertir
	 * @return La localdate correspondant au calendar
	 */
	public static LocalDate calendarToLocalDate( Calendar calendar ) {
		return LocalDateTime.ofInstant( calendar.toInstant(), calendar.getTimeZone().toZoneId() ).toLocalDate();
	}

	/**
	 * Convertit une liste de calendar un liste de localdate
	 *
	 * @param listeCalendar La liste de calendar à convertir
	 * @return La liste de date convertie
	 */
	public static List<LocalDate> listeCalendarToListeLocalDate( List<Calendar> listeCalendar ) {

		List<LocalDate> listeDates = null;

		if ( null != listeCalendar && !listeCalendar.isEmpty() ) {
			listeDates = new ArrayList<>();
			for ( Calendar calendar : listeCalendar ) {
				listeDates.add( calendarToLocalDate( calendar ) );
			}
		}

		return listeDates;
	}

	public static Calendar stringToCalendar( String text, int type ) {

		Calendar c = Calendar.getInstance();

		if ( type != DATE_TIME ) {
			c.setTime( new Date( 0 ) );
		}

		if ( ( type & DATE ) != 0 ) {
			c.set( Calendar.YEAR, Integer.parseInt( text.substring( 0, 4 ) ) );
			c.set( Calendar.MONTH, Integer.parseInt( text.substring( 5, 7 ) ) - 1 + Calendar.JANUARY );
			c.set( Calendar.DAY_OF_MONTH, Integer.parseInt( text.substring( 8, 10 ) ) );

			if ( type == DATE_TIME ) {
				text = text.substring( 11 );
			}
		}

		if ( ( type & TIME ) == 0 ) {
			return c;
		}
		c.set( Calendar.HOUR_OF_DAY, Integer.parseInt( text.substring( 0, 2 ) ) ); // -11
		c.set( Calendar.MINUTE, Integer.parseInt( text.substring( 3, 5 ) ) );
		c.set( Calendar.SECOND, Integer.parseInt( text.substring( 6, 8 ) ) );

		int pos = 8;
		if ( pos < text.length() && text.charAt( pos ) == '.' ) {
			int ms = 0;
			int f = 100;
			while ( true ) {
				char d = text.charAt( ++pos );
				if ( d < '0' || d > '9' ) {
					break;
				}
				ms += ( d - '0' ) * f;
				f /= 10;
			}
			c.set( Calendar.MILLISECOND, ms );
		} else {
			c.set( Calendar.MILLISECOND, 0 );
		}

		if ( pos < text.length() ) {
			if ( text.charAt( pos ) == '+' || text.charAt( pos ) == '-' ) {
				c.setTimeZone( TimeZone.getTimeZone( "GMT" + text.substring( pos ) ) );
			} else if ( text.charAt( pos ) == 'Z' ) {
				c.setTimeZone( TimeZone.getTimeZone( "GMT" ) );
			} else {
				throw new RuntimeException( "illegal time format!" );
			}
		}

		return c;
	}

	/**
	 * Convertit une chaine de caractère contenant un temps (heure/minute) en Date
	 *
	 * @param time Le temps sous forme de chaine de caractère de type HH:mm
	 * @return La date indiquant l'heure
	 */
	public static Date stringTimeToDate( String time ) throws ParseException {
		if ( null != time && !"".equals( time ) ) {
			SimpleDateFormat sdf = new SimpleDateFormat( "HH:mm", Locale.FRANCE );
			return sdf.parse( time.replace( " ", "" ) );
		} else {
			return null;
		}
	}

	/**
	 * Retourne un calendar à partir d'un Date.
	 *
	 * @param date La date à convertir
	 * @return la date convertie
	 */
	public static Calendar dateToCalendar( Date date ) {
		Calendar cal = null;
		if ( date != null ) {
			cal = Calendar.getInstance();
			cal.setTime( date );
		}
		return cal;
	}

	/**
	 * Retourne le numéro de la semaine à partir d'une date
	 *
	 * @param date La date dont on recherche le numero de semaine
	 * @return Le numéro de semaine
	 */
	public static Integer getWeekNumber( LocalDate date ) {
		WeekFields weekFields = WeekFields.of( Locale.getDefault() );
		return date.get( weekFields.weekOfWeekBasedYear() );
	}

	/**
	 * Convertit un LocalDate vers un Calendar
	 *
	 * @param localDate La date à convertir
	 * @return Calendar la date convertie
	 */
	public static Calendar localDateToCalendar( LocalDate localDate ) {
		Date date = Date.from( localDate.atStartOfDay().atZone( ZoneId.systemDefault() ).toInstant() );
		Calendar cal = Calendar.getInstance();
		cal.setTime( date );
		return cal;
	}

	/**
	 * Retourne le premier jour de la semaine à partir d'une date
	 *
	 * @param date La date dont on recherche le premier jour de la semaine
	 * @return Le premier jour de la semaine
	 */
	public static LocalDate getFirstDayOfWeek( LocalDate date ) {
		LocalDate debutSemaine = date;
		// Calendar commence la semaine le dimanche....
		while ( debutSemaine.get( ChronoField.DAY_OF_WEEK ) != Calendar.MONDAY - 1 ) {
			debutSemaine = debutSemaine.minus( 1, ChronoUnit.DAYS );
		}
		return debutSemaine;
	}

	/**
	 * Retourne le dernier jour de la semaine à partir d'une date
	 *
	 * @param date La date
	 * @return Le dernier jour de la semaine
	 */
	public static LocalDate getLastDayOfWeek( LocalDate date ) {
		// Go forward to get Sunday
		LocalDate sunday = date;
		while ( sunday.getDayOfWeek() != DayOfWeek.SUNDAY ) {
			sunday = sunday.plusDays( 1 );
		}

		return sunday;
	}

	/**
	 * Récupération de la liste des jours de la semaine à partir d'une date
	 * 
	 * @param date La date à laquelle on recherche tous les jours de la semaine
	 * @return La liste des jours de la semaine
	 */
	public static List<LocalDate> getListeJoursDeLaSemaine( LocalDate date ) {
		if ( null != date ) {
			List<LocalDate> listeJoursSemaines = new ArrayList<>();

			LocalDate dateDuPremierJourDeLaSemaine = getFirstDayOfWeek( date );
			LocalDate dateDuDernierJourDeLaSemaine = getLastDayOfWeek( date );

			for ( LocalDate dateDuJourTeste = dateDuPremierJourDeLaSemaine ; !dateDuJourTeste.isEqual( dateDuDernierJourDeLaSemaine.plusDays(1) ) ; dateDuJourTeste = dateDuJourTeste.plusDays( 1 ) ) {
				listeJoursSemaines.add( dateDuJourTeste );
			}

			// Tri de la liste des jours par ordre croissant
			Collections.sort( listeJoursSemaines, ( j1, j2 ) -> j1.compareTo( j2 ) );

			return listeJoursSemaines;

		} else {
			return null;
		}
	}
}