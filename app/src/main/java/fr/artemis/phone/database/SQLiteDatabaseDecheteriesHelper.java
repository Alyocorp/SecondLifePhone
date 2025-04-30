package fr.artemis.phone.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import fr.artemis.phone.dto.CRMPhoneDecheterieDTO;
import fr.artemis.phone.dto.CRMPhoneDecheteriesTarifsDTO;
import fr.artemis.phone.dto.CRMPhoneDecheteriesUpdatesDTO;
import fr.artemis.phone.utils.Constantes;
import fr.artemis.phone.utils.ConstantesMaps;
import fr.artemis.phone.utils.StorageUtil;

/**
 * Classe d'accès à la base de données
 */
public class SQLiteDatabaseDecheteriesHelper extends SQLiteOpenHelper {

	// Nom de la base de données
	public static final String DATABASE_NAME = "decheteries.db";

	// Tables
	public static final String TABLE_DECHETERIES = "t_decheteries";
	public static final String TABLE_DECHETERIES_TARIFS = "t_decheteries_tarifs";

	// Champs
	public static final String FIELD_D_ID = "id";
	public static final String FIELD_D_LATITUDE = "latitude";
	public static final String FIELD_D_LONGITUDE = "longitude";
	public static final String FIELD_D_NOM = "nom";
	public static final String FIELD_D_DESCRIPTION = "description";
	public static final String FIELD_D_HORAIRES = "horaires";
	public static final String FIELD_D_BLACKLIST = "blacklist";
	public static final String FIELD_D_PRO_OU_PARTIC = "pro_ou_partic";
	public static final String FIELD_D_BARRIERE = "barriere";
	public static final String FIELD_D_CARTE = "carte";
	public static final String FIELD_D_COMPTE_OUVERT = "compte_ouvert";
	public static final String FIELD_DT_ID = "id";
	public static final String FIELD_DT_FK_DECHETERIE = "fk_decheterie";
	public static final String FIELD_DT_MATIERE = "matiere";
	public static final String FIELD_DT_PRIX_TONNE = "prix_tonne";

	public static final String[] ALL_FIELDS_DECHETERIES = { FIELD_D_ID, FIELD_D_NOM, FIELD_D_DESCRIPTION, FIELD_D_HORAIRES, FIELD_D_PRO_OU_PARTIC, FIELD_D_BARRIERE, FIELD_D_COMPTE_OUVERT,
			FIELD_D_CARTE, FIELD_D_BLACKLIST, FIELD_D_LATITUDE, FIELD_D_LONGITUDE };
	public static final String[] ALL_FIELDS_DECHETERIES_TARIFS = { FIELD_DT_ID, FIELD_DT_FK_DECHETERIE, FIELD_DT_MATIERE, FIELD_DT_PRIX_TONNE };

	private static boolean firstInsertionNeeded = false;

	// Requête de création de la table des decheteries
	public static final String SQL_CREATE_DECHETERIES = "CREATE TABLE " + TABLE_DECHETERIES + "(" + FIELD_D_ID + " INTEGER PRIMARY KEY," + FIELD_D_LATITUDE + " TEXT," + FIELD_D_LONGITUDE + " TEXT,"
			+ FIELD_D_NOM + " TEXT," + FIELD_D_DESCRIPTION + " TEXT," + FIELD_D_HORAIRES + " TEXT," + FIELD_D_BLACKLIST + " INTEGER DEFAULT 0," + FIELD_D_PRO_OU_PARTIC + " TEXT," + FIELD_D_BARRIERE
			+ " TEXT," + FIELD_D_CARTE + " TEXT," + FIELD_D_COMPTE_OUVERT + " INTEGER DEFAULT 0" + ");";
	public static final String SQL_CREATE_DECHETERIES_TARIFS = "CREATE TABLE " + TABLE_DECHETERIES_TARIFS + "(" + FIELD_DT_ID + " INTEGER PRIMARY KEY," + FIELD_DT_FK_DECHETERIE
			+ " INTEGER REFERENCES " + TABLE_DECHETERIES + "," + FIELD_DT_MATIERE + " TEXT," + FIELD_DT_PRIX_TONNE + " REAL" + ");";

	// Requête de vérification si une table existe déjà
	public static final String SQL_TABLE_EXISTS = "SELECT name FROM sqlite_master WHERE type='table' AND name=' " + TABLE_DECHETERIES + "';";

	// Instance singleton
	private static SQLiteDatabaseDecheteriesHelper instance;

	private static Context context;

	/**
	 * Récupération de l'instance
	 * 
	 * @param context
	 *            Le contexte Android
	 * @return Instance singleton
	 */
	public static synchronized SQLiteDatabaseDecheteriesHelper getIntance( Context context ) {
		if ( null == instance ) {
			SQLiteDatabaseDecheteriesHelper.context = context;
			instance = new SQLiteDatabaseDecheteriesHelper( context );
		}
		return instance;
	}

	/**
	 * Constructeur
	 * 
	 * @param context
	 *            Contexte Android
	 */
	private SQLiteDatabaseDecheteriesHelper( Context context ) {
		super( context, DATABASE_NAME, null, Constantes.NUMERO_VERSION_DECHETERIES_IN_APK );
	}

	@Override
	public void onConfigure( SQLiteDatabase db ) {
		super.onConfigure( db );
		db.setForeignKeyConstraintsEnabled( true );
	}

	@Override
	public void onCreate( SQLiteDatabase db ) {
		try {
			StorageUtil.writeObjectInternal( context, ConstantesMaps.KEY_VERSION_MAP_DECHET.getValue(), Constantes.NUMERO_VERSION_DECHETERIES_IN_APK );
			firstInsertionNeeded = true;
			db.execSQL( SQL_CREATE_DECHETERIES );
			db.execSQL( SQL_CREATE_DECHETERIES_TARIFS );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {

	}

	/**
	 * Insertion des décheteries en base de données si elle n'existe pas
	 * 
	 * @param liste
	 *            La liste des decheteries à inserer en base de données
	 */
	public void insertBaseDecheteries( List<CRMPhoneDecheterieDTO> liste ) {
		getWritableDatabase();
		if ( firstInsertionNeeded && null != liste && !liste.isEmpty() ) {
			for ( CRMPhoneDecheterieDTO decheterie : liste ) {
				insertDecheterie( decheterie );
				firstInsertionNeeded = false;
			}
		}
	}

	/**
	 * Insertion d'une decheterie dans la base de données locale
	 * 
	 * @param decheterie
	 *            La decheterie à ajouter dans la base de données locale
	 */
	public void insertDecheterie( CRMPhoneDecheterieDTO decheterie ) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put( FIELD_D_ID, decheterie.getId() );
		values.put( FIELD_D_NOM, decheterie.getNom() );
		values.put( FIELD_D_DESCRIPTION, decheterie.getDescription() );
		values.put( FIELD_D_HORAIRES, decheterie.getHoraires() );
		values.put( FIELD_D_PRO_OU_PARTIC, decheterie.getProOuPartic() );
		values.put( FIELD_D_BARRIERE, decheterie.getBarriere() );
		values.put( FIELD_D_COMPTE_OUVERT, decheterie.getCompteOuvert() );
		values.put( FIELD_D_CARTE, decheterie.getCarte() );
		values.put( FIELD_D_BLACKLIST, decheterie.isBlacklist() );
		values.put( FIELD_D_LATITUDE, decheterie.getLatitude() );
		values.put( FIELD_D_LONGITUDE, decheterie.getLongitude() );

		db.insert( TABLE_DECHETERIES, null, values );

		if ( null != decheterie.getTarifs() && !decheterie.getTarifs().isEmpty() ) {
			insertTarifs( decheterie.getTarifs() );
		}
	}

	/**
	 * Insertion des tarifs en base de données locale
	 * 
	 * @param tarifs
	 *            Les tarifs à insérer
	 */
	public void insertTarifs( List<CRMPhoneDecheteriesTarifsDTO> tarifs ) {
		if ( null != tarifs && !tarifs.isEmpty() ) {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues values;
			for ( CRMPhoneDecheteriesTarifsDTO tarif : tarifs ) {
				values = new ContentValues();
				values.put( FIELD_DT_ID, tarif.getId() );
				values.put( FIELD_DT_FK_DECHETERIE, tarif.getFkDecheterie() );
				values.put( FIELD_DT_MATIERE, tarif.getMatiere() );
				values.put( FIELD_DT_PRIX_TONNE, String.valueOf( tarif.getPrixTonne() ) );
				db.insert( TABLE_DECHETERIES_TARIFS, null, values );
			}
		}
	}

	/**
	 * Mise à jour en provenance du serveur
	 * 
	 * @param listeUpdates
	 *            La liste des mises à jour
	 */
	public void update( List<CRMPhoneDecheteriesUpdatesDTO> listeUpdates ) {
		if ( null != listeUpdates && !listeUpdates.isEmpty() ) {
			for ( CRMPhoneDecheteriesUpdatesDTO update : listeUpdates ) {
				getWritableDatabase().execSQL( update.getSqlUpdates() );
			}
		}
	}

	/**
	 * Lecture des décheteries à partir de la base de données
	 * 
	 * @return Flux des décheteries
	 */
	public InputStream readDecheteriesFromDatabase() {
		List<CRMPhoneDecheterieDTO> decheteries = new ArrayList<>();

		Cursor cursor = getReadableDatabase().query( TABLE_DECHETERIES, ALL_FIELDS_DECHETERIES, null, null, null, null, null );

		cursor.moveToFirst();
		while( !cursor.isAfterLast() ) {
			CRMPhoneDecheterieDTO decheterie = cursorToDecheterie( cursor );
			decheteries.add( decheterie );

			Cursor cursor2 = getReadableDatabase().query( TABLE_DECHETERIES_TARIFS, ALL_FIELDS_DECHETERIES_TARIFS, "fk_decheterie = ?", new String[] { String.valueOf( decheterie.getId() ) }, null,
					null, null );
			cursor2.moveToFirst();
			while( !cursor2.isAfterLast() ) {
				CRMPhoneDecheteriesTarifsDTO tarif = cursorToTarif( cursor2 );
				decheterie.getTarifs().add( tarif );
				cursor2.moveToNext();
			}
			cursor2.close();

			cursor.moveToNext();
		}
		// assurez-vous de la fermeture du curseur
		cursor.close();

		StringBuilder sbKml = new StringBuilder();

		sbKml.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<kml xmlns=\"http://www.opengis.net/kml/2.2\">"
				+ "<Document><name>Decheteries</name><description/><Folder><name>decheterie.kml</name>" );

		for ( CRMPhoneDecheterieDTO decheterie : decheteries ) {

			sbKml.append( "<Placemark>\n" + "<id>" + decheterie.getId() + "</id>\n" + "<name>" + decheterie.getNom() + "</name>\n" + "<description>" + decheterie.getDescription() + "</description>\n"
					+ "<horaires>" + decheterie.getHoraires() + "</horaires>\n" + "<type>" + decheterie.getProOuPartic() + "</type>\n" + "<barriere>" + decheterie.getBarriere() + "</barriere>\n"
					+ "<compteOuvert>" + decheterie.getCompteOuvert() + "</compteOuvert>\n" + "<carte>" + decheterie.getCarte() + "</carte>\n" + "<blacklist>" + decheterie.isBlacklist()
					+ "</blacklist>\n" );

			if ( null != decheterie.getTarifs() && !decheterie.getTarifs().isEmpty() ) {
				sbKml.append( "<tarifs>" );
				for ( CRMPhoneDecheteriesTarifsDTO tarif : decheterie.getTarifs() ) {
					sbKml.append( "<tarif>" );
					sbKml.append( "<id>" ).append( tarif.getId() ).append( "</id>" );
					sbKml.append( "<fkDecheterie>" ).append( tarif.getFkDecheterie() ).append( "</fkDecheterie>" );
					sbKml.append( "<matiere>" ).append( tarif.getMatiere() ).append( "</matiere>" );
					sbKml.append( "<prix>" ).append( tarif.getPrixTonne() ).append( "</prix>" );
					sbKml.append( "</tarif>" );
				}
				sbKml.append( "</tarifs>" );
			}
			sbKml.append( "<Point>\n" + "<coordinates>" + decheterie.getLongitude() + "," + decheterie.getLatitude() + ",0</coordinates>\n" + "</Point>\n" + "</Placemark>\n" );

		}
		sbKml.append( "</Folder>\n" + "  </Document>\n" + "</kml>" );

		return new ByteArrayInputStream( sbKml.toString().getBytes() );

	}

	private CRMPhoneDecheterieDTO cursorToDecheterie( Cursor cursor ) {

		int id = cursor.getInt(0);
		String nom = cursor.getString(1);
		String description = cursor.getString(2);
		String horaires = cursor.getString(3);
		String proOuPartic = cursor.getString(4);
		String barriere = cursor.getString(5);
		String compteOuvert = cursor.getString(6);
		String carte = cursor.getString(7 );
		boolean blacklist;
		String latitude = cursor.getString(9);
		String longitude = cursor.getString(10 );

		if (id == 2023 ) {
			System.out.println("longitude = " + longitude);
		}

		if ( cursor.getString( 8 ).equals( "1" ) ) {
			blacklist = true;
		} else {
			blacklist = false;
		}

		CRMPhoneDecheterieDTO decheterie = new CRMPhoneDecheterieDTO();
		decheterie.setId( id );
		decheterie.setNom( nom );
		decheterie.setDescription(description );
		decheterie.setHoraires( horaires );
		decheterie.setProOuPartic( proOuPartic );
		decheterie.setBarriere( barriere );
		decheterie.setCompteOuvert( Boolean.getBoolean( cursor.getString( 6 ) ) );
		decheterie.setCarte( carte );
		decheterie.setBlacklist( blacklist );
		decheterie.setLatitude( latitude );
		decheterie.setLongitude( longitude );

		return decheterie;
	}

	private CRMPhoneDecheteriesTarifsDTO cursorToTarif( Cursor cursor ) {
		CRMPhoneDecheteriesTarifsDTO tarif = new CRMPhoneDecheteriesTarifsDTO();
		tarif.setId( cursor.getInt( 0 ) );
		tarif.setFkDecheterie( cursor.getInt( 1 ) );
		tarif.setMatiere( cursor.getString( 2 ) );
		tarif.setPrixTonne( new BigDecimal( cursor.getDouble( 3 ) ) );

		return tarif;
	}
}