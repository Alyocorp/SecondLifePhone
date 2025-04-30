package fr.artemis.phone.fragments.utilisateur.rapportchantier.rapport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.artemis.phone.R;
import fr.artemis.phone.activities.main.MainActivity;
import fr.artemis.phone.components.layouts.ExpandableLayout;
import fr.artemis.phone.dto.CRMPhoneClientDTO;
import fr.artemis.phone.dto.CRMPhoneInterventionDetailsDTO;
import fr.artemis.phone.dto.CRMPhonePlanningInterventionDTO;
import fr.artemis.phone.dto.CRMPhoneRapportChantierDTO;
import fr.artemis.phone.dto.CRMPhoneRapportChantierEquipeDTO;
import fr.artemis.phone.dto.CRMPhoneRapportChantierPhotoDTO;
import fr.artemis.phone.dto.SalarieLightDTO;
import fr.artemis.phone.utils.DateUtils;
import fr.artemis.phone.utils.NumericUtils;
import fr.artemis.phone.utils.RapportChantierUtil;
import fr.artemis.phone.utils.SessionPhone;
import fr.artemis.phone.utils.StorageUtil;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;
import fr.artemis.phone.webservices.WsUtil;

/**
 * Fragment des rapports de chantier
 */
public class FragmentRapportChantier extends Fragment implements WsCaller {

	// Code d'attente des retours de selection de photos
	private final static int REQUEST_CODE_PHOTOS_AVANT = 100;
	private final static int REQUEST_CODE_PHOTOS_RUE = 200;
	private final static int REQUEST_CODE_PHOTOS_APRES = 300;

	// region BLOCS DEPLIABLES (Listeners à injecter)

	// region TITRES DEPLIABLES
	@BindView( R.id.layoutTitleIntervention )
	RelativeLayout layoutTitleIntervention;

	@BindView( R.id.expandable_layout_detail_intervention )
	ExpandableLayout expandableLayoutDetailIntervention;

	@BindView( R.id.btShowDetailIntervention )
	ImageView btShowDetailIntervention;

	@BindView( R.id.layoutTitlePhotos )
	RelativeLayout layoutTitlePhotos;

	@BindView( R.id.expandable_layout_detail_photos )
	ExpandableLayout expandableLayoutDetailPhotos;

	@BindView( R.id.btShowDetailPhotos )
	ImageView btShowDetailPhotos;

	@BindView( R.id.layoutTitleAvant )
	RelativeLayout layoutTitleAvant;

	@BindView( R.id.expandable_layout_detail_avant )
	ExpandableLayout expandableLayoutDetailAvant;

	@BindView( R.id.btShowDetailAvant )
	ImageView btShowDetailAvant;

	@BindView( R.id.layoutTitleJournee )
	RelativeLayout layoutTitleJournee;

	@BindView( R.id.expandable_layout_detail_journee )
	ExpandableLayout expandableLayoutDetailJournee;

	@BindView( R.id.btShowDetailJournee )
	ImageView btShowDetailJournee;

	@BindView( R.id.layoutTitleFinChantier )
	RelativeLayout layoutTitleFinChantier;

	@BindView( R.id.expandable_layout_detail_fin_chantier )
	ExpandableLayout expandableLayoutDetailFinChantier;

	@BindView( R.id.btShowDetailFinChantier )
	ImageView btShowDetailFinChantier;
	// endregion

	// region Bloc date
	@BindView( R.id.btPrevDate )
	ImageButton btPrevDate;

	@BindView( R.id.tvDateDuJour )
	TextView tvDateDuJour;

	@BindView( R.id.btNextDate )
	ImageButton btNextDate;
	// endregion

	// region Bloc devis réajusté
	@BindView( R.id.layoutDevisReajuste )
	LinearLayout layoutDevisReajuste;

	@BindView( R.id.rg_devis_reajuste )
	RadioGroup rgDevisReajuste;
	// endregion

	// region Bloc retard
	@BindView( R.id.layoutRetard )
	LinearLayout layoutRetard;

	@BindView( R.id.rg_retard )
	RadioGroup rgRetard;

	@BindView( R.id.rb_retard_oui )
	RadioButton rbRetardOui;

	@BindView( R.id.rb_retard_non )
	RadioButton rbRetardNon;
	// endregion

	// region Bloc déterioration
	@BindView( R.id.layoutDeterioration )
	LinearLayout layoutDeterioration;

	@BindView( R.id.rg_deterioration )
	RadioGroup rgDeterioration;

	@BindView( R.id.rb_deterioration_oui )
	RadioButton rbDeteriorationOui;

	@BindView( R.id.rb_deterioration_non )
	RadioButton rbDeteriorationNon;
	// endregion

	// region Bloc décheteries partic
	@BindView( R.id.layoutDechetPartic )
	LinearLayout layoutDechetPartic;

	@BindView( R.id.rg_dechet_partic )
	RadioGroup rgDechetPartic;

	@BindView( R.id.rb_dechet_partic_oui )
	RadioButton rbDechetParticOui;

	@BindView( R.id.rb_dechet_partic_non )
	RadioButton rbDechetParticNon;
	// endregion

	// region Bloc décheteries pro
	@BindView( R.id.layoutDechetPro )
	RelativeLayout layoutDechetPro;

	@BindView( R.id.rg_dechet_pro )
	RadioGroup rgDechetPro;

	@BindView( R.id.rb_dechet_pro_oui )
	RadioButton rbDechetProOui;

	@BindView( R.id.rb_dechet_pro_non )
	RadioButton rbDechetProNon;
	// endregion

	// region Bloc bennes
	@BindView( R.id.layoutBennes )
	RelativeLayout layoutBennes;

	@BindView( R.id.rg_bennes )
	RadioGroup rgBennes;

	@BindView( R.id.rb_bennes_oui )
	RadioButton rbBennesOui;

	@BindView( R.id.rb_bennes_non )
	RadioButton rbBennesNon;
	// endregion

	// region Bloc dépôts vente
	@BindView( R.id.layoutDepotVente )
	LinearLayout layoutDepotVente;

	@BindView( R.id.rg_depot_vente )
	RadioGroup rgDepotVente;

	@BindView( R.id.rb_depot_vente_oui )
	RadioButton rbDepotVenteOui;

	@BindView( R.id.rb_depot_vente_non )
	RadioButton rbDepotVenteNon;
	// endregion

	// region Bloc Association
	@BindView( R.id.layoutAssociation )
	LinearLayout layoutAssociation;

	@BindView( R.id.rg_association )
	RadioGroup rgAssociation;

	@BindView( R.id.rb_association_oui )
	RadioButton rbAssociationOui;

	@BindView( R.id.rb_association_non )
	RadioButton rbAssociationNon;
	// endregion

	// region Bloc Ramène
	@BindView( R.id.layoutRamene )
	LinearLayout layoutRamene;

	@BindView( R.id.rg_ramene )
	RadioGroup rgRamene;

	@BindView( R.id.rb_ramene_oui )
	RadioButton rbRameneOui;

	@BindView( R.id.rb_ramene_non )
	RadioButton rbRameneNon;
	// endregion

	// region Bloc Ménage
	@BindView( R.id.layoutMenage )
	LinearLayout layoutMenage;

	@BindView( R.id.rg_menage )
	RadioGroup rgMenage;

	@BindView( R.id.rb_menage_oui )
	RadioButton rbMenageOui;

	@BindView( R.id.rb_menage_non )
	RadioButton rbMenageNon;
	// endregion

	// region Bloc Respect des consignes
	@BindView( R.id.layoutRespectConsignes )
	LinearLayout layoutRespectConsignes;

	@BindView( R.id.rg_respect_consignes )
	RadioGroup rgRespectConsignes;

	@BindView( R.id.rb_respect_consignes_oui )
	RadioButton rbRespectConsignesOui;

	@BindView( R.id.rb_respect_consignes_non )
	RadioButton rbRespectConsignesNon;
	// endregion

	// region Bloc Règlement
	@BindView( R.id.layoutReglements )
	LinearLayout layoutReglements;

	@BindView( R.id.layoutReglementsInfos )
	LinearLayout layoutReglementsInfos;

	@BindView( R.id.rg_reglement_recu )
	RadioGroup rgReglement;

	@BindView( R.id.rb_reglement_oui )
	RadioButton rbReglementOui;

	@BindView( R.id.rb_reglement_non )
	RadioButton rbReglementNon;
	// endregion

	// region Bloc Remarques
	@BindView( R.id.layoutRemarques )
	LinearLayout layoutRemarques;

	@BindView( R.id.rg_remarques )
	RadioGroup rgRemarques;

	@BindView( R.id.rb_remarques_oui )
	RadioButton rbRemarquesOui;

	@BindView( R.id.rb_remarques_non )
	RadioButton rbRemarquesNon;
	// endregion

	// endregion

	// region BLOC INTERVENTION (A remplir automatiquement avec les valeurs
	// initiales)
	@BindView( R.id.lb_rapport_client_det )
	TextView lbClient;

	@BindView( R.id.lb_rapport_adresse_det )
	TextView lbAdresse;

	@BindView( R.id.lb_rapport_volume_det )
	TextView lbVolume;

	@BindView( R.id.lb_rapport_valeur_det )
	TextView lbValeur;

	@BindView( R.id.lb_rapport_montant_det )
	TextView lbMontant;

	@BindView( R.id.lb_rapport_accompte_det )
	TextView lbAccompte;

	@BindView( R.id.lb_rapport_devis_signe_det )
	TextView lbDevisSigne;
	// endregion)

	// region BLOC PHOTOS
	@BindView( R.id.bt_add_photos_avant )
	Button btPhotoAvant;

	@BindView( R.id.bt_add_photos_rue )
	Button btPhotoRue;

	@BindView( R.id.bt_add_photos_apres )
	Button btPhotoApres;

	@BindView( R.id.layoutPhotosAvant )
	LinearLayout layoutPhotosAvant;

	@BindView( R.id.layoutPhotosRue )
	LinearLayout layoutPhotosRue;

	@BindView( R.id.layoutPhotosApres )
	LinearLayout layoutPhotosApres;
	// endregion

	// region BLOC AVANT (remplit par l'utilisateur à l'ouverture du chantier)
	@BindView( R.id.rg_controle_kit )
	RadioGroup rgControleKit;

	@BindView( R.id.rb_controle_kit_oui )
	RadioButton rbControleKitOui;

	@BindView( R.id.rb_controle_kit_non )
	RadioButton rbControleKitNon;

	@BindView( R.id.rb_devis_reajuste_oui )
	RadioButton rbDevisReajusteOui;

	@BindView( R.id.rb_devis_reajuste_non )
	RadioButton rbDevisReajusteNon;

	@BindView( R.id.et_reajustement_volume )
	EditText etDevisReajusteVolume;

	@BindView( R.id.et_reajustement_valeur )
	EditText etDevisReajusteValeur;

	@BindView( R.id.et_reajustement_total )
	EditText etDevisReajusteTotal;
	// endregion

	// region BLOC JOURNEE (remplit quotidiennement par l'utilisateur)
	@BindView( R.id.sp_chef_chantier )
	Spinner spChefDeChantier;

	@BindView( R.id.layoutEquipeContent )
	LinearLayout layoutEquipeContent;

	@BindView( R.id.rg_retard_client_prevenu )
	RadioGroup rgRetardClientPrevenu;

	@BindView( R.id.rb_retard_client_prevenu_oui )
	RadioButton rbRetardClientPrevenuOui;

	@BindView( R.id.rb_retard_client_prevenu_non )
	RadioButton rbRetardClientPrevenuNon;

	@BindView( R.id.et_retard_pourquoi_pas_prevenu )
	EditText etRetardRaison;

	@BindView( R.id.et_heure_arrivee )
	EditText etHeureArrivee;

	@BindView( R.id.et_heure_depart )
	EditText etHeureDepart;

	@BindView( R.id.et_deterioration_details )
	EditText etDeteriorationDetails;

	@BindView( R.id.et_dechet_partic_volume )
	EditText etDechetParticVolume;

	@BindView( R.id.et_dechet_pro_dib_volume )
	EditText etDechetProVolumeDib;

	@BindView( R.id.et_dechet_pro_bois_volume )
	EditText etDechetProVolumeBois;

	@BindView( R.id.et_dechet_pro_ferraille_volume )
	EditText etDechetProVolumeFerraille;

	@BindView( R.id.et_dechet_pro_papier_volume )
	EditText etDechetProVolumePapier;

	@BindView( R.id.et_dechet_pro_autre_volume )
	EditText etDechetProVolumeAutres;

	@BindView( R.id.et_bennes_dib_volume )
	EditText etBennesVolumeDib;

	@BindView( R.id.et_bennes_bois_volume )
	EditText etBennesVolumeBois;

	@BindView( R.id.et_bennes_ferraille_volume )
	EditText etBennesVolumeFerraille;

	@BindView( R.id.et_bennes_papier_volume )
	EditText etBennesVolumePapier;

	@BindView( R.id.et_bennes_autre_volume )
	EditText etBennesVolumeAutres;

	@BindView( R.id.et_depot_vente_volume )
	EditText etDepotVenteVolume;

	@BindView( R.id.et_association_volume )
	EditText etAssociationVolume;

	@BindView( R.id.et_ramene_volume )
	EditText etRameneVolume;

	@BindView( R.id.et_volume_camion_ramene )
	EditText etVolumeCamionRamene;

	@BindView( R.id.et_volume_camion_ferraille )
	EditText etVolumeCamionFerraille;

	@BindView( R.id.et_volume_camion_papier )
	EditText etVolumeCamionPapier;

	@BindView( R.id.et_volume_camion_decheterie )
	EditText etVolumeCamionDecheterie;

	@BindView( R.id.et_meanage_duree )
	EditText etMenageTemps;

	@BindView( R.id.et_respect_consignes_details )
	EditText etRespectConsignesDetails;

	@BindView( R.id.et_reglement_montant )
	EditText etReglementMontant;

	@BindView( R.id.lb_reglement_montant_restant )
	TextView lbMontantRestant;

	@BindView( R.id.et_reglement_infos )
	EditText etReglementInfos;

	@BindView( R.id.et_remarques )
	EditText etRemarques;

	// endregion

	@BindView( R.id.btFinaliserRapportChantier )
	Button btFinaliser;

	// Le contenu du rapport de chantier
	private CRMPhoneRapportChantierDTO rapportChantier;

	// L'intervention en cours de traitement
	private CRMPhonePlanningInterventionDTO intervention = null;

	// Le client lié à l'intervention
	private CRMPhoneClientDTO client = null;

	// Les détails de l'intervention
	private CRMPhoneInterventionDetailsDTO detailsInter = null;

	// La liste des salariés
	private List<SalarieLightDTO> listeSalaries = null;

	// Le nom du fichier interne du rapport de chantier dans le téléphone
	private String fileNameReportAbsolutePath;

	// La date du jour
	private LocalDate dateEnCours = LocalDate.now();

	// La liste des photos
	private Map<String, String> mapPhotosThumbOrigin = new HashMap<>();

	private List<String> listePhotosAvant = new ArrayList<>();
	private List<String> listePhotosRue = new ArrayList<>();
	private List<String> listePhotosApres = new ArrayList<>();

	// L'equipe
	private Map<Integer, CRMPhoneRapportChantierEquipeDTO> mapEquipe = new HashMap<>();

	@Nullable
	@Override
	public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
		View view = inflater.inflate( R.layout.fragment_user_rapport_chantier, container, false );

		ButterKnife.bind( this, view );

		assert null != getActivity();

		initDatas();

		initView();

		initEvents();

		return view;
	}

	/**
	 * Initialisation des données
	 */
	private void initDatas() {
		this.client = intervention.getClient();
		WsUtil.getInterventionDetailByIdDevis( this, null, intervention.getFkDevis() );
		WsUtil.getListeSalariesActifs( this, null );

		initRapport();
	}

	/**
	 * Initialisation du rapport de chantier à partir du fichier enregistré sur le
	 * téléphone ou de la base de données si il existe
	 */
	private void initRapport() {
		// Recherche du rapport de chantier dans le telephone sinon création d'un
		// nouveau
		String rapportFilePath = getActivity().getFilesDir().getPath() + "/rapports/";
		File rapportDirectory = new File( rapportFilePath );

		if ( !rapportDirectory.exists() ) {
			rapportDirectory.mkdir();
		}

		fileNameReportAbsolutePath = rapportFilePath + RapportChantierUtil.getRapportFileName( dateEnCours, intervention.getFkDevis() );

		File fileRapport = new File( fileNameReportAbsolutePath );
		if ( fileRapport.exists() ) {

			// Lecture du fichier JSON
			rapportChantier = StorageUtil.readJSONFile( fileNameReportAbsolutePath, CRMPhoneRapportChantierDTO.class );

			if ( null != rapportChantier.getMapPhotosThumbnailsSource() ) {
				this.mapPhotosThumbOrigin = rapportChantier.getMapPhotosThumbnailsSource();
			}

			setFormPhotos();

			setFormAvant();

			setFormFinDeChantier();
		} else {
			// Recherche d'un fichier antérieur stocké dans le téléphone
			List<LocalDate> listeDatesDeLaSemaine = DateUtils.getListeJoursDeLaSemaine( dateEnCours );

			String fileNameToTest;
			String fileFound = null;
			for ( LocalDate dateTestee : listeDatesDeLaSemaine ) {
				// Tant qu'aucun fichier n'a été trouvé, on poursuit la recherche
				if ( null == fileFound ) {
					fileNameToTest = rapportFilePath + RapportChantierUtil.getRapportFileName( dateTestee, intervention.getFkDevis() );
					File fileToTest = new File( fileNameToTest );
					if ( fileToTest.exists() ) {
						fileFound = fileNameToTest;
					}
				}
			}

			// Si un fichier d'un rapport antérieur existe pour le meme chantier, on le
			// charge pour les données d'ouverture de chantier
			if ( null != fileFound ) {
				rapportChantier = StorageUtil.readJSONFile( fileFound, CRMPhoneRapportChantierDTO.class );

				if ( !DateUtils.calendarToLocalDate( rapportChantier.getDateRapport() ).equals( dateEnCours ) ) {
					// On conserve les données d'ouverture de chantier et les photos
					// Par contre on efface les données du rapport de la journée dans l'instance en
					// mémoire afin d'initialiser
					// un rapport de chantier prérempli pour la journée
					copyOldReport();
				}
			} else {

				// Recherche en base de données si quelqu'un a déjà entamé le rapport de
				// chantier.
				// Si c'est le cas, on rappatrie le rapport et on le charge
				WsUtil.getRapportDeChantier( this, null, dateEnCours, intervention.getFkDevis() );
			}
		}
	}

	/**
	 * Copie du rapport d'ouverture de chantier dans le rapport actuel
	 */
	private void copyOldReport() {
		// Le rapport en mémoire est un ancien rapport qui contient les informations
		// d'ouverture de chantier ainsi que les photos si elles ont été enregistré
		rapportChantier.setId( null );
		rapportChantier.setAssociation( null );
		rapportChantier.setBennes( null );
		rapportChantier.setDateRapport( DateUtils.localDateToCalendar( dateEnCours ) );
		rapportChantier.setDateSaisie( DateUtils.localDateToCalendar( LocalDate.now() ) );
		rapportChantier.setDechetPartic( null );
		rapportChantier.setDechetPro( null );
		rapportChantier.setDepotVente( null );
		rapportChantier.setDetailsReglement( null );
		rapportChantier.setDeterioration( null );
		rapportChantier.setDeteriorationDetails( null );
		rapportChantier.setFkChefDeChantier( null );
		rapportChantier.setFkUser( SessionPhone.getInstance().getUserDto().getId() );
		rapportChantier.setHeureArriveeChantier( null );
		rapportChantier.setHeureDepartChantier( null );
		rapportChantier.getListeEquipe().clear();
		rapportChantier.setMenage( null );
		rapportChantier.setMenageTemps( null );
		rapportChantier.setMontantRecuOuIndemnise( null );
		rapportChantier.setRamene( null );
		rapportChantier.setReglementRecuOuIndemnisation( null );
		rapportChantier.setRemarques( null );
		rapportChantier.setRespectConsignes( null );
		rapportChantier.setRespectConsignesDetails( null );
		rapportChantier.setRetard( null );
		rapportChantier.setRetardClientPrevenu( null );
		rapportChantier.setRetardDetails( null );
		rapportChantier.setVolumeCamionDecheterie( null );
		rapportChantier.setVolumeCamionFerraille( null );
		rapportChantier.setVolumeCamionPapier( null );
		rapportChantier.setVolumeCamionRamene( null );
		rapportChantier.setVolumeTraiteAssociation( null );
		rapportChantier.setVolumeTraiteBennesAutres( null );
		rapportChantier.setVolumeTraiteBennesBois( null );
		rapportChantier.setVolumeTraiteBennesDib( null );
		rapportChantier.setVolumeTraiteBennesFerraille( null );
		rapportChantier.setVolumeTraiteBennesPapier( null );
		rapportChantier.setVolumeTraiteDechetPartic( null );
		rapportChantier.setVolumeTraiteDechetProAutres( null );
		rapportChantier.setVolumeTraiteDechetProBois( null );
		rapportChantier.setVolumeTraiteDechetProDib( null );
		rapportChantier.setVolumeTraiteDechetProFerraille( null );
		rapportChantier.setVolumeTraiteDechetProPapier( null );
		rapportChantier.setVolumeTraiteDepotVente( null );
		rapportChantier.setVolumeTraiteRamene( null );

		// Enregistrement du fichier JSON
		StorageUtil.saveDtoIntoFileInJSONFormat( rapportChantier, fileNameReportAbsolutePath );

		setFormPhotos();

		setFormAvant();

		setFormFinDeChantier();
	}

	/**
	 * Initialisation de la vue
	 */
	private void initView() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "dd/MM/yyyy" );
		String dateFormattee = dateEnCours.format( formatter );
		tvDateDuJour.setText( dateFormattee );

		etHeureDepart.setOnClickListener( ( v ) -> {
			InputMethodManager imm = (InputMethodManager) getContext().getSystemService( Context.INPUT_METHOD_SERVICE );
			imm.hideSoftInputFromWindow( v.getWindowToken(), 0 );
			Calendar mcurrentTime = Calendar.getInstance();
			int hour = mcurrentTime.get( Calendar.HOUR_OF_DAY );
			int minute = mcurrentTime.get( Calendar.MINUTE );
			TimePickerDialog.OnTimeSetListener listener = (timePicker, selectedHour, selectedMinute ) -> {
				String minuteStr = String.valueOf( selectedMinute );
				if ( minuteStr.length() == 1 ) {
					minuteStr = "0" + minuteStr;
				}
				etHeureDepart.setText( selectedHour + " : " + minuteStr );
			};
			TimePickerDialog mTimePicker = new TimePickerDialog( getContext(), listener, hour, minute, true );
			mTimePicker.show();
		} );
		etHeureDepart.setOnFocusChangeListener( ( v, hasFocus ) -> {
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
					etHeureDepart.setText( selectedHour + " : " + minuteStr );
				};
				TimePickerDialog mTimePicker = new TimePickerDialog( getContext(), listener, hour, minute, true );
				mTimePicker.show();
			}
		} );

		etHeureArrivee.setOnClickListener( ( v ) -> {
			InputMethodManager imm = (InputMethodManager) getContext().getSystemService( Context.INPUT_METHOD_SERVICE );
			imm.hideSoftInputFromWindow( v.getWindowToken(), 0 );
			Calendar mcurrentTime = Calendar.getInstance();
			int hour = mcurrentTime.get( Calendar.HOUR_OF_DAY );
			int minute = mcurrentTime.get( Calendar.MINUTE );
			TimePickerDialog.OnTimeSetListener listener = (timePicker, selectedHour, selectedMinute ) -> {
				String minuteStr = String.valueOf( selectedMinute );
				if ( minuteStr.length() == 1 ) {
					minuteStr = "0" + minuteStr;
				}
				etHeureArrivee.setText( selectedHour + " : " + minuteStr );
			};
			TimePickerDialog mTimePicker = new TimePickerDialog( getContext(), listener, hour, minute, true );
			mTimePicker.show();
		} );
		etHeureArrivee.setOnFocusChangeListener( ( v, hasFocus ) -> {
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
					etHeureArrivee.setText( selectedHour + " : " + minuteStr );
				};
				TimePickerDialog mTimePicker = new TimePickerDialog( getContext(), listener, hour, minute, true );
				mTimePicker.show();
			}
		} );
	}

	/**
	 * Initialisation des evenements
	 */
	private void initEvents() {

		initEventsDepliables();

		initEventsPhotos();

		initEventsUser();

		btFinaliser.setOnClickListener( ( evt ) -> tryToSendRapport() );
	}

	// region BLOC GESTION DES PHOTOS

	/**
	 * Initialisation des evenements liées aux photos de chantier
	 */
	private void initEventsPhotos() {
		btPhotoAvant.setOnClickListener( v -> selectPhotos( REQUEST_CODE_PHOTOS_AVANT ) );
		btPhotoRue.setOnClickListener( v -> selectPhotos( REQUEST_CODE_PHOTOS_RUE ) );
		btPhotoApres.setOnClickListener( v -> selectPhotos( REQUEST_CODE_PHOTOS_APRES ) );
	}

	/**
	 * Selection des photos de chantier
	 */
	private void selectPhotos( int requestCode ) {
		ImagePicker.create( this ).folderMode( true ) // folder mode (false by default)
				.toolbarFolderTitle( "Dossier" ) // folder selection title
				.toolbarImageTitle( "Selection" ) // image selection title
				.toolbarArrowColor( Color.BLACK ) // Toolbar 'up' arrow color
				.includeVideo( true ) // Show video on image picker
				.onlyVideo( false ) // include video (false by default)
				.multi() // multi mode (default mode)
				.limit( 10 ) // max images can be selected (99 by default)
				.showCamera( true ) // show camera or not (true by default)
				.imageDirectory( "Camera" ) // directory name for captured image ("Camera" folder by default)
				.enableLog( false ) // disabling log
				.start( requestCode );
	}

	@Override
	public void onActivityResult( int requestCode, final int resultCode, Intent data ) {
		switch ( requestCode ) {
			case REQUEST_CODE_PHOTOS_APRES :
				dumpThumbnailsBefore( ImagePicker.getImages( data ), "APRES" );
//				handlePhotosApres( ImagePicker.getImages( data ) );
				setFormPhotosApres();
				saveReportInternal();
				break;
			case REQUEST_CODE_PHOTOS_AVANT :
				dumpThumbnailsBefore( ImagePicker.getImages( data ), "AVANT" );
				// handlePhotosAvant( ImagePicker.getImages( data ) );
				// setFormPhotos();
				setFormPhotosAvant();
				saveReportInternal();
				break;
			case REQUEST_CODE_PHOTOS_RUE :
				dumpThumbnailsBefore( ImagePicker.getImages( data ), "RUE" );
				// handlePhotosRue( ImagePicker.getImages( data ) );
				setFormPhotosRue();
				saveReportInternal();
				break;
		}
	}

	/**
	 * Création et enregistrement de photos miniatures par rapport aux photos
	 * selectionnées par l'utilisateur
	 *
	 * @param listeImagesBefore
	 *            La liste des photos selectionnées par l'utilisateur
	 */
	private void dumpThumbnailsBefore( List<Image> listeImagesBefore, String type ) {
		if ( null != listeImagesBefore && !listeImagesBefore.isEmpty() ) {

			int numPhoto = 0;

			for ( Image image : listeImagesBefore ) {
				try {
					numPhoto++;
					// Bloc de rotation en fonction de l'image d'origine
					ExifInterface exif = new ExifInterface( image.getPath() );
					int orientation = exif.getAttributeInt( ExifInterface.TAG_ORIENTATION, 1 );
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inPreferredConfig = Bitmap.Config.RGB_565;
					Bitmap bitmap = BitmapFactory.decodeFile( image.getPath(), options );
					Matrix matrix = new Matrix();
					if ( orientation == 6 ) {
						matrix.postRotate( 90 );
					} else if ( orientation == 3 ) {
						matrix.postRotate( 180 );
					} else if ( orientation == 8 ) {
						matrix.postRotate( 270 );
					}
					bitmap = Bitmap.createBitmap( bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true );
					// Fin du bloc de rotation

					// Dimension de la minitaure en fonction de la taille de l'ecran :
					int size = SessionPhone.getInstance().getScreenWidth() / 4;

					// Création et enregistrement de la miniature
					Bitmap imageBitmap = Bitmap.createScaledBitmap( bitmap, size, size, false );

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					imageBitmap.compress( Bitmap.CompressFormat.JPEG, 100, baos );
					byte[] imageData = baos.toByteArray();

					String rapportPhotoFilePath = getActivity().getFilesDir().getPath() + "/rapports/photos/" + intervention.getFkDevis() + "/";
					File rapportDirectory = new File( rapportPhotoFilePath );

					if ( !rapportDirectory.exists() ) {
						rapportDirectory.mkdirs();
					}

					String subPath;
					if ( type.equals( "AVANT" ) ) {
						subPath = "avant/";
					} else if ( type.equals( "RUE" ) ) {
						subPath = "rue/";
					} else if ( type.equals( "APRES" ) ) {
						subPath = "apres/";
					} else {
						throw new IllegalStateException( "Impossible d'enregistrer une image de type inconnu" );
					}

					String rapportPhotoFilePathDest = rapportPhotoFilePath + subPath;
					File rapportDirectoryPhoto = new File( rapportPhotoFilePathDest );

					if ( !rapportDirectoryPhoto.exists() ) {
						rapportDirectoryPhoto.mkdirs();
					}

					String thumbFileName = rapportPhotoFilePathDest + numPhoto + ".jpg";
					// Association de la miniature à la photo d'origine afin de l'enregistrer sur le
					// serveur lors de la finalisation
					mapPhotosThumbOrigin.put( thumbFileName, image.getPath() );
					this.rapportChantier.setMapPhotosThumbnailsSource( mapPhotosThumbOrigin );
					try ( FileOutputStream fos = new FileOutputStream( thumbFileName ) ) {
						fos.write( imageData );
						if ( type.equals( "AVANT" ) ) {
							this.listePhotosAvant.add( thumbFileName );
						} else if ( type.equals( "RUE" ) ) {
							this.listePhotosRue.add( thumbFileName );
						} else if ( type.equals( "APRES" ) ) {
							this.listePhotosApres.add( thumbFileName );
						}
					} catch ( IOException ex ) {
						ex.printStackTrace();
					}
				} catch ( Exception ex ) {
					Log.e( "DEBUG", "Erreur lors de la creation de la photo miniature" );
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * Affichage des photos selectionnées par l'utilisateur
	 */
	private void setFormPhotosApres() {
		// Suppression de toutes les photos précédentes
		layoutPhotosApres.removeAllViews();
		if ( null != listePhotosApres && !listePhotosApres.isEmpty() ) {

			int nbPhotosPerLine = 4;

			int nbPhotos = listePhotosApres.size();

			if ( nbPhotos > 0 ) {

				// Création des lignes dans le layout des photos
				int nbLines = (int) Math.ceil( (double) listePhotosApres.size() / nbPhotosPerLine );

				// Nombre de cellules vides
				int nbCellulesVide = ( nbLines * nbPhotosPerLine ) - listePhotosApres.size();

				LinearLayout layoutLine;
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
				Map<Integer, LinearLayout> mapLines = new HashMap<>();
				for ( int numLine = 0 ; numLine < nbLines ; numLine++ ) {
					layoutLine = new LinearLayout( this.getContext() );
					layoutLine.setLayoutParams( layoutParams );

					mapLines.put( numLine + 1, layoutLine );
				}

				// Positionnement des photos
				Map<Integer, String> mapPositionPhotos = new HashMap<>();
				for ( int numPhoto = 0 ; numPhoto < listePhotosApres.size() ; numPhoto++ ) {
					mapPositionPhotos.put( numPhoto, listePhotosApres.get( numPhoto ) );
				}

				// Creation de positions vides ( pas de photos à insérer dans ces cases mais
				// cases à instancier pour equilibrer les largeurs)
				for ( int numCelluleVide = 0 ; numCelluleVide < nbCellulesVide ; numCelluleVide++ ) {
					mapPositionPhotos.put( numCelluleVide + listePhotosApres.size(), null );
				}

				for ( Map.Entry<Integer, String> entryImage : mapPositionPhotos.entrySet() ) {
					int numLineDeLaPhoto = (int) Math.ceil( ( (double) ( entryImage.getKey() + 1 ) ) / nbPhotosPerLine );

					LinearLayout ligneContenantLaPhoto = mapLines.get( numLineDeLaPhoto );
					ligneContenantLaPhoto.setOrientation( LinearLayout.HORIZONTAL );

					// CREATION DE L'IHM CONTENANT LA PHOTO
					LinearLayout.LayoutParams layoutParamsDeLaPhoto = new LinearLayout.LayoutParams( 0, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 1 / nbPhotosPerLine );
					RelativeLayout layoutPhoto = new RelativeLayout( getContext() );

					if ( null != entryImage.getValue() ) {
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inPreferredConfig = Bitmap.Config.RGB_565;
						Bitmap bitmap = BitmapFactory.decodeFile( entryImage.getValue(), options );
						ImageView img = new ImageView( getContext() );
						img.setImageBitmap( bitmap );
						layoutPhoto.addView( img );
						layoutParamsDeLaPhoto.height = ( ( layoutPhotosApres.getWidth() / nbPhotosPerLine ) * bitmap.getHeight() ) / bitmap.getWidth();

						Button btDeletePhoto = new Button( getContext() );
						btDeletePhoto.setText( R.string.delete );
						RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams( 100, 100 );
						btParams.addRule( RelativeLayout.ALIGN_PARENT_TOP );
						btParams.addRule( RelativeLayout.ALIGN_PARENT_END );
						btDeletePhoto.setLayoutParams( btParams );
						layoutPhoto.addView( btDeletePhoto );

						btDeletePhoto.setOnClickListener( v -> deletePhotoAvant( entryImage.getValue() ) );

					} else {

						Random rnd = new Random();
						int color = Color.argb( 255, rnd.nextInt( 256 ), rnd.nextInt( 256 ), rnd.nextInt( 256 ) );
						layoutPhoto.setBackgroundColor( color );
					}

					layoutPhoto.setLayoutParams( layoutParamsDeLaPhoto );

					layoutPhoto.requestLayout();

					ligneContenantLaPhoto.addView( layoutPhoto );

					ligneContenantLaPhoto.requestLayout();
				}

				// Ajout des lignes dans le conteneur de photos
				for ( Map.Entry<Integer, LinearLayout> entryLines : mapLines.entrySet() ) {
					layoutPhotosApres.addView( entryLines.getValue() );
				}

				layoutPhotosApres.requestLayout();
			}
		}
	}

	/**
	 * Affichage des photos selectionnées par l'utilisateur
	 */
	private void setFormPhotosRue() {
		// Suppression de toutes les photos précédentes
		layoutPhotosRue.removeAllViews();
		if ( null != listePhotosRue && !listePhotosRue.isEmpty() ) {

			int nbPhotosPerLine = 4;

			int nbPhotos = listePhotosRue.size();

			if ( nbPhotos > 0 ) {

				// Création des lignes dans le layout des photos
				int nbLines = (int) Math.ceil( (double) listePhotosRue.size() / nbPhotosPerLine );

				// Nombre de cellules vides
				int nbCellulesVide = ( nbLines * nbPhotosPerLine ) - listePhotosRue.size();

				LinearLayout layoutLine;
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
				Map<Integer, LinearLayout> mapLines = new HashMap<>();
				for ( int numLine = 0 ; numLine < nbLines ; numLine++ ) {
					layoutLine = new LinearLayout( this.getContext() );
					layoutLine.setLayoutParams( layoutParams );

					mapLines.put( numLine + 1, layoutLine );
				}

				// Positionnement des photos
				Map<Integer, String> mapPositionPhotos = new HashMap<>();
				for ( int numPhoto = 0 ; numPhoto < listePhotosRue.size() ; numPhoto++ ) {
					mapPositionPhotos.put( numPhoto, listePhotosRue.get( numPhoto ) );
				}

				// Creation de positions vides ( pas de photos à insérer dans ces cases mais
				// cases à instancier pour equilibrer les largeurs)
				for ( int numCelluleVide = 0 ; numCelluleVide < nbCellulesVide ; numCelluleVide++ ) {
					mapPositionPhotos.put( numCelluleVide + listePhotosRue.size(), null );
				}

				for ( Map.Entry<Integer, String> entryImage : mapPositionPhotos.entrySet() ) {
					int numLineDeLaPhoto = (int) Math.ceil( ( (double) ( entryImage.getKey() + 1 ) ) / nbPhotosPerLine );

					LinearLayout ligneContenantLaPhoto = mapLines.get( numLineDeLaPhoto );
					ligneContenantLaPhoto.setOrientation( LinearLayout.HORIZONTAL );

					// CREATION DE L'IHM CONTENANT LA PHOTO
					LinearLayout.LayoutParams layoutParamsDeLaPhoto = new LinearLayout.LayoutParams( 0, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 1 / nbPhotosPerLine );
					RelativeLayout layoutPhoto = new RelativeLayout( getContext() );

					if ( null != entryImage.getValue() ) {
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inPreferredConfig = Bitmap.Config.RGB_565;
						Bitmap bitmap = BitmapFactory.decodeFile( entryImage.getValue(), options );
						ImageView img = new ImageView( getContext() );
						img.setImageBitmap( bitmap );
						layoutPhoto.addView( img );
						layoutParamsDeLaPhoto.height = ( ( layoutPhotosRue.getWidth() / nbPhotosPerLine ) * bitmap.getHeight() ) / bitmap.getWidth();

						Button btDeletePhoto = new Button( getContext() );
						btDeletePhoto.setText( R.string.delete );
						RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams( 100, 100 );
						btParams.addRule( RelativeLayout.ALIGN_PARENT_TOP );
						btParams.addRule( RelativeLayout.ALIGN_PARENT_END );
						btDeletePhoto.setLayoutParams( btParams );
						layoutPhoto.addView( btDeletePhoto );

						btDeletePhoto.setOnClickListener( v -> deletePhotoAvant( entryImage.getValue() ) );

					} else {

						Random rnd = new Random();
						int color = Color.argb( 255, rnd.nextInt( 256 ), rnd.nextInt( 256 ), rnd.nextInt( 256 ) );
						layoutPhoto.setBackgroundColor( color );
					}

					layoutPhoto.setLayoutParams( layoutParamsDeLaPhoto );

					layoutPhoto.requestLayout();

					ligneContenantLaPhoto.addView( layoutPhoto );

					ligneContenantLaPhoto.requestLayout();
				}

				// Ajout des lignes dans le conteneur de photos
				for ( Map.Entry<Integer, LinearLayout> entryLines : mapLines.entrySet() ) {
					layoutPhotosRue.addView( entryLines.getValue() );
				}

				layoutPhotosRue.requestLayout();
			}
		}

	}

	/**
	 * Affichage des photos selectionnées par l'utilisateur
	 */
	private void setFormPhotosAvant() {
		// Suppression de toutes les photos précédentes
		layoutPhotosAvant.removeAllViews();
		if ( null != listePhotosAvant && !listePhotosAvant.isEmpty() ) {

			int nbPhotosPerLine = 4;

			int nbPhotos = listePhotosAvant.size();

			if ( nbPhotos > 0 ) {

				// Création des lignes dans le layout des photos
				int nbLines = (int) Math.ceil( (double) listePhotosAvant.size() / nbPhotosPerLine );

				// Nombre de cellules vides
				int nbCellulesVide = ( nbLines * nbPhotosPerLine ) - listePhotosAvant.size();

				LinearLayout layoutLine;
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
				Map<Integer, LinearLayout> mapLines = new HashMap<>();
				for ( int numLine = 0 ; numLine < nbLines ; numLine++ ) {
					layoutLine = new LinearLayout( this.getContext() );
					layoutLine.setLayoutParams( layoutParams );

					mapLines.put( numLine + 1, layoutLine );
				}

				// Positionnement des photos
				Map<Integer, String> mapPositionPhotos = new HashMap<>();
				for ( int numPhoto = 0 ; numPhoto < listePhotosAvant.size() ; numPhoto++ ) {
					mapPositionPhotos.put( numPhoto, listePhotosAvant.get( numPhoto ) );
				}

				// Creation de positions vides ( pas de photos à insérer dans ces cases mais
				// cases à instancier pour equilibrer les largeurs)
				for ( int numCelluleVide = 0 ; numCelluleVide < nbCellulesVide ; numCelluleVide++ ) {
					mapPositionPhotos.put( numCelluleVide + listePhotosAvant.size(), null );
				}

				for ( Map.Entry<Integer, String> entryImage : mapPositionPhotos.entrySet() ) {
					int numLineDeLaPhoto = (int) Math.ceil( ( (double) ( entryImage.getKey() + 1 ) ) / nbPhotosPerLine );

					LinearLayout ligneContenantLaPhoto = mapLines.get( numLineDeLaPhoto );
					ligneContenantLaPhoto.setOrientation( LinearLayout.HORIZONTAL );

					// CREATION DE L'IHM CONTENANT LA PHOTO
					LinearLayout.LayoutParams layoutParamsDeLaPhoto = new LinearLayout.LayoutParams( 0, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 1 / nbPhotosPerLine );
					RelativeLayout layoutPhoto = new RelativeLayout( getContext() );

					if ( null != entryImage.getValue() ) {
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inPreferredConfig = Bitmap.Config.RGB_565;
						Bitmap bitmap = BitmapFactory.decodeFile( entryImage.getValue(), options );
						ImageView img = new ImageView( getContext() );
						img.setImageBitmap( bitmap );
						layoutPhoto.addView( img );
						layoutParamsDeLaPhoto.height = ( ( layoutPhotosAvant.getWidth() / nbPhotosPerLine ) * bitmap.getHeight() ) / bitmap.getWidth();

						Button btDeletePhoto = new Button( getContext() );
						btDeletePhoto.setText( R.string.delete );
						RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams( 100, 100 );
						btParams.addRule( RelativeLayout.ALIGN_PARENT_TOP );
						btParams.addRule( RelativeLayout.ALIGN_PARENT_END );
						btDeletePhoto.setLayoutParams( btParams );
						layoutPhoto.addView( btDeletePhoto );

						btDeletePhoto.setOnClickListener( v -> deletePhotoAvant( entryImage.getValue() ) );

					} else {

						Random rnd = new Random();
						int color = Color.argb( 255, rnd.nextInt( 256 ), rnd.nextInt( 256 ), rnd.nextInt( 256 ) );
						layoutPhoto.setBackgroundColor( color );
					}

					layoutPhoto.setLayoutParams( layoutParamsDeLaPhoto );

					layoutPhoto.requestLayout();

					ligneContenantLaPhoto.addView( layoutPhoto );

					ligneContenantLaPhoto.requestLayout();
				}

				// Ajout des lignes dans le conteneur de photos
				for ( Map.Entry<Integer, LinearLayout> entryLines : mapLines.entrySet() ) {
					layoutPhotosAvant.addView( entryLines.getValue() );
				}

				layoutPhotosAvant.requestLayout();
			}
		}

	}

	// /**
	// * Reception de la liste des photos selectionnées avant le chantier
	// *
	// * @param listeImagesObj
	// * La liste des images
	// */
	// private void handlePhotosAvant( List<Image> listeImagesObj ) {
	// if ( null != listeImagesObj && !listeImagesObj.isEmpty() ) {
	// for ( Image image : listeImagesObj ) {
	// listePhotosAvant.add( image.getPath() );
	// }
	// handlePhotosAvantString();
	//
	// saveReportInternal();
	// }
	// }

	/**
	 * Reception de la liste des photos selectionnées avant le chantier
	 */
	private void handlePhotosAvantString() {

		try {
			// Suppression de toutes les photos précédentes
			layoutPhotosAvant.removeAllViews();
			if ( null != listePhotosAvant && !listePhotosAvant.isEmpty() ) {

				int nbPhotosPerLine = 4;

				int nbPhotos = listePhotosAvant.size();

				if ( nbPhotos > 0 ) {

					// Création des lignes dans le layout des photos
					int nbLines = (int) Math.ceil( (double) listePhotosAvant.size() / nbPhotosPerLine );

					// Nombre de cellules vides
					int nbCellulesVide = ( nbLines * nbPhotosPerLine ) - listePhotosAvant.size();

					LinearLayout layoutLine;
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
					Map<Integer, LinearLayout> mapLines = new HashMap<>();
					for ( int numLine = 0 ; numLine < nbLines ; numLine++ ) {
						layoutLine = new LinearLayout( this.getContext() );
						layoutLine.setLayoutParams( layoutParams );

						mapLines.put( numLine + 1, layoutLine );
					}

					// Positionnement des photos
					Map<Integer, String> mapPositionPhotos = new HashMap<>();
					for ( int numPhoto = 0 ; numPhoto < listePhotosAvant.size() ; numPhoto++ ) {
						mapPositionPhotos.put( numPhoto, listePhotosAvant.get( numPhoto ) );
					}

					// Creation de positions vides ( pas de photos à insérer dans ces cases mais
					// cases à instancier pour equilibrer les largeurs)
					for ( int numCelluleVide = 0 ; numCelluleVide < nbCellulesVide ; numCelluleVide++ ) {
						mapPositionPhotos.put( numCelluleVide + listePhotosAvant.size(), null );
					}

					for ( Map.Entry<Integer, String> entryImage : mapPositionPhotos.entrySet() ) {
						int numLineDeLaPhoto = (int) Math.ceil( ( (double) ( entryImage.getKey() + 1 ) ) / nbPhotosPerLine );

						LinearLayout ligneContenantLaPhoto = mapLines.get( numLineDeLaPhoto );
						ligneContenantLaPhoto.setOrientation( LinearLayout.HORIZONTAL );

						// CREATION DE L'IHM CONTENANT LA PHOTO
						LinearLayout.LayoutParams layoutParamsDeLaPhoto = new LinearLayout.LayoutParams( 0, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 1 / nbPhotosPerLine );
						RelativeLayout layoutPhoto = new RelativeLayout( getContext() );

						if ( null != entryImage.getValue() ) {
							ExifInterface exif = new ExifInterface( entryImage.getValue() );
							int orientation = exif.getAttributeInt( ExifInterface.TAG_ORIENTATION, 1 );
							BitmapFactory.Options options = new BitmapFactory.Options();
							options.inPreferredConfig = Bitmap.Config.RGB_565;
							if ( entryImage.getValue().toLowerCase().contains( "dcim" ) ) {
								options.inSampleSize = 4;
							}
							Bitmap bitmap = BitmapFactory.decodeFile( entryImage.getValue(), options );
							Matrix matrix = new Matrix();
							if ( orientation == 6 ) {
								matrix.postRotate( 90 );
							} else if ( orientation == 3 ) {
								matrix.postRotate( 180 );
							} else if ( orientation == 8 ) {
								matrix.postRotate( 270 );
							}
							bitmap = Bitmap.createBitmap( bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true );
							ImageView img = new ImageView( getContext() );
							img.setImageBitmap( bitmap );
							// img.setLayoutParams(layoutParamsDeLImage);
							layoutPhoto.addView( img );
							layoutParamsDeLaPhoto.height = ( ( layoutPhotosAvant.getWidth() / nbPhotosPerLine ) * bitmap.getHeight() ) / bitmap.getWidth();

							Button btDeletePhoto = new Button( getContext() );
							btDeletePhoto.setText( R.string.delete );
							RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams( 100, 100 );
							btParams.addRule( RelativeLayout.ALIGN_PARENT_TOP );
							btParams.addRule( RelativeLayout.ALIGN_PARENT_END );
							btDeletePhoto.setLayoutParams( btParams );
							layoutPhoto.addView( btDeletePhoto );

							btDeletePhoto.setOnClickListener( v -> deletePhotoAvant( entryImage.getValue() ) );

						} else {

							Random rnd = new Random();
							int color = Color.argb( 255, rnd.nextInt( 256 ), rnd.nextInt( 256 ), rnd.nextInt( 256 ) );
							layoutPhoto.setBackgroundColor( color );
						}

						layoutPhoto.setLayoutParams( layoutParamsDeLaPhoto );

						layoutPhoto.requestLayout();

						ligneContenantLaPhoto.addView( layoutPhoto );

						ligneContenantLaPhoto.requestLayout();
					}

					// Ajout des lignes dans le conteneur de photos
					for ( Map.Entry<Integer, LinearLayout> entryLines : mapLines.entrySet() ) {
						layoutPhotosAvant.addView( entryLines.getValue() );
					}

					layoutPhotosAvant.requestLayout();
				}
			}
		} catch ( IOException ex ) {
			ex.printStackTrace();
		}
	}

	/**
	 * Suppression d'une photo dans la liste des photos avant le chantier
	 *
	 * @param photoPathToDelete
	 *            La photo à supprimer
	 */
	private void deletePhotoAvant( String photoPathToDelete ) {
		listePhotosAvant.remove( photoPathToDelete );
		handlePhotosAvantString();
		saveReportInternal();
	}

	/**
	 * Reception de la liste des photos de la rue du chantier selectionnées
	 *
	 * @param listeImages
	 *            La liste des images
	 */
	private void handlePhotosRue( List<Image> listeImages ) {
		if ( null != listeImages && !listeImages.isEmpty() ) {
			for ( Image image : listeImages ) {
				listePhotosRue.add( image.getPath() );
			}
			handlePhotosRueString();

			saveReportInternal();
		}
	}

	/**
	 * Reception de la liste des photos selectionnées de la rue du chantier
	 */
	private void handlePhotosRueString() {

		try {
			// Suppression de toutes les photos précédentes
			layoutPhotosRue.removeAllViews();

			if ( null != listePhotosRue && !listePhotosRue.isEmpty() ) {

				int nbPhotosPerLine = 4;

				int nbPhotos = listePhotosRue.size();

				if ( nbPhotos > 0 ) {

					// Création des lignes dans le layout des photos
					int nbLines = (int) Math.ceil( (double) listePhotosRue.size() / nbPhotosPerLine );

					// Nombre de cellules vides
					int nbCellulesVide = ( nbLines * nbPhotosPerLine ) - listePhotosRue.size();

					LinearLayout layoutLine;
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
					Map<Integer, LinearLayout> mapLines = new HashMap<>();
					for ( int numLine = 0 ; numLine < nbLines ; numLine++ ) {
						layoutLine = new LinearLayout( this.getContext() );
						layoutLine.setLayoutParams( layoutParams );

						mapLines.put( numLine + 1, layoutLine );
					}

					// Positionnement des photos
					Map<Integer, String> mapPositionPhotos = new HashMap<>();
					for ( int numPhoto = 0 ; numPhoto < listePhotosRue.size() ; numPhoto++ ) {
						mapPositionPhotos.put( numPhoto, listePhotosRue.get( numPhoto ) );
					}

					// Creation de positions vides ( pas de photos à insérer dans ces cases mais
					// cases à instancier pour equilibrer les largeurs)
					for ( int numCelluleVide = 0 ; numCelluleVide < nbCellulesVide ; numCelluleVide++ ) {
						mapPositionPhotos.put( numCelluleVide + listePhotosRue.size(), null );
					}

					for ( Map.Entry<Integer, String> entryImage : mapPositionPhotos.entrySet() ) {
						int numLineDeLaPhoto = (int) Math.ceil( ( (double) ( entryImage.getKey() + 1 ) ) / nbPhotosPerLine );

						LinearLayout ligneContenantLaPhoto = mapLines.get( numLineDeLaPhoto );
						ligneContenantLaPhoto.setOrientation( LinearLayout.HORIZONTAL );

						// CREATION DE L'IHM CONTENANT LA PHOTO
						LinearLayout.LayoutParams layoutParamsDeLaPhoto = new LinearLayout.LayoutParams( 0, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 1 / nbPhotosPerLine );
						RelativeLayout layoutPhoto = new RelativeLayout( getContext() );

						if ( null != entryImage.getValue() ) {
							ExifInterface exif = new ExifInterface( entryImage.getValue() );
							int orientation = exif.getAttributeInt( ExifInterface.TAG_ORIENTATION, 1 );
							BitmapFactory.Options options = new BitmapFactory.Options();
							options.inPreferredConfig = Bitmap.Config.RGB_565;
							if ( entryImage.getValue().toLowerCase().contains( "dcim" ) ) {
								options.inSampleSize = 4;
							}
							Bitmap bitmap = BitmapFactory.decodeFile( entryImage.getValue(), options );
							Matrix matrix = new Matrix();
							if ( orientation == 6 ) {
								matrix.postRotate( 90 );
							} else if ( orientation == 3 ) {
								matrix.postRotate( 180 );
							} else if ( orientation == 8 ) {
								matrix.postRotate( 270 );
							}
							bitmap = Bitmap.createBitmap( bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true );
							ImageView img = new ImageView( getContext() );
							img.setImageBitmap( bitmap );
							// img.setLayoutParams(layoutParamsDeLImage);
							layoutPhoto.addView( img );
							layoutParamsDeLaPhoto.height = ( ( layoutPhotosRue.getWidth() / nbPhotosPerLine ) * bitmap.getHeight() ) / bitmap.getWidth();

							Button btDeletePhoto = new Button( getContext() );
							btDeletePhoto.setText( R.string.delete );
							RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams( 100, 100 );
							btParams.addRule( RelativeLayout.ALIGN_PARENT_TOP );
							btParams.addRule( RelativeLayout.ALIGN_PARENT_END );
							btDeletePhoto.setLayoutParams( btParams );
							layoutPhoto.addView( btDeletePhoto );

							btDeletePhoto.setOnClickListener( v -> deletePhotoRue( entryImage.getValue() ) );

						} else {

							Random rnd = new Random();
							int color = Color.argb( 255, rnd.nextInt( 256 ), rnd.nextInt( 256 ), rnd.nextInt( 256 ) );
							layoutPhoto.setBackgroundColor( color );
						}

						layoutPhoto.setLayoutParams( layoutParamsDeLaPhoto );

						layoutPhoto.requestLayout();

						ligneContenantLaPhoto.addView( layoutPhoto );

						ligneContenantLaPhoto.requestLayout();
					}

					// Ajout des lignes dans le conteneur de photos
					for ( Map.Entry<Integer, LinearLayout> entryLines : mapLines.entrySet() ) {
						layoutPhotosRue.addView( entryLines.getValue() );
					}

					layoutPhotosRue.requestLayout();
				}
			}
		} catch ( IOException ex ) {
			ex.printStackTrace();
		}
	}

	/**
	 * Suppression d'une photo dans la liste des photos de la rue du chantier
	 *
	 * @param photoPathToDelete
	 *            La photo à supprimer
	 */
	private void deletePhotoRue( String photoPathToDelete ) {
		listePhotosRue.remove( photoPathToDelete );
		handlePhotosRueString();
		saveReportInternal();
	}

	/**
	 * Reception de la liste des photos selectionnées après le chantier
	 *
	 * @param listeImages
	 *            La liste des images
	 */
	private void handlePhotosApres( List<Image> listeImages ) {
		if ( null != listeImages && !listeImages.isEmpty() ) {
			for ( Image image : listeImages ) {
				listePhotosApres.add( image.getPath() );
			}
			handlePhotosApresString();

			saveReportInternal();
		}
	}

	/**
	 * Reception de la liste des photos selectionnées apres le chantier
	 */
	private void handlePhotosApresString() {

		try {
			// Suppression de toutes les photos précédentes
			layoutPhotosApres.removeAllViews();
			if ( null != listePhotosApres && !listePhotosApres.isEmpty() ) {
				int nbPhotosPerLine = 4;

				int nbPhotos = listePhotosApres.size();

				if ( nbPhotos > 0 ) {

					// Création des lignes dans le layout des photos
					int nbLines = (int) Math.ceil( (double) listePhotosApres.size() / nbPhotosPerLine );

					// Nombre de cellules vides
					int nbCellulesVide = ( nbLines * nbPhotosPerLine ) - listePhotosApres.size();

					LinearLayout layoutLine;
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );
					Map<Integer, LinearLayout> mapLines = new HashMap<>();
					for ( int numLine = 0 ; numLine < nbLines ; numLine++ ) {
						layoutLine = new LinearLayout( this.getContext() );
						layoutLine.setLayoutParams( layoutParams );

						mapLines.put( numLine + 1, layoutLine );
					}

					// Positionnement des photos
					Map<Integer, String> mapPositionPhotos = new HashMap<>();
					for ( int numPhoto = 0 ; numPhoto < listePhotosApres.size() ; numPhoto++ ) {
						mapPositionPhotos.put( numPhoto, listePhotosApres.get( numPhoto ) );
					}

					// Creation de positions vides ( pas de photos à insérer dans ces cases mais
					// cases à instancier pour equilibrer les largeurs)
					for ( int numCelluleVide = 0 ; numCelluleVide < nbCellulesVide ; numCelluleVide++ ) {
						mapPositionPhotos.put( numCelluleVide + listePhotosApres.size(), null );
					}

					for ( Map.Entry<Integer, String> entryImage : mapPositionPhotos.entrySet() ) {
						int numLineDeLaPhoto = (int) Math.ceil( ( (double) ( entryImage.getKey() + 1 ) ) / nbPhotosPerLine );

						LinearLayout ligneContenantLaPhoto = mapLines.get( numLineDeLaPhoto );
						ligneContenantLaPhoto.setOrientation( LinearLayout.HORIZONTAL );

						// CREATION DE L'IHM CONTENANT LA PHOTO
						LinearLayout.LayoutParams layoutParamsDeLaPhoto = new LinearLayout.LayoutParams( 0, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 1 / nbPhotosPerLine );
						RelativeLayout layoutPhoto = new RelativeLayout( getContext() );

						if ( null != entryImage.getValue() ) {
							ExifInterface exif = new ExifInterface( entryImage.getValue() );
							int orientation = exif.getAttributeInt( ExifInterface.TAG_ORIENTATION, 1 );
							BitmapFactory.Options options = new BitmapFactory.Options();
							options.inPreferredConfig = Bitmap.Config.RGB_565;
							if ( entryImage.getValue().toLowerCase().contains( "dcim" ) ) {
								options.inSampleSize = 4;
							}
							Bitmap bitmap = BitmapFactory.decodeFile( entryImage.getValue(), options );
							Matrix matrix = new Matrix();
							if ( orientation == 6 ) {
								matrix.postRotate( 90 );
							} else if ( orientation == 3 ) {
								matrix.postRotate( 180 );
							} else if ( orientation == 8 ) {
								matrix.postRotate( 270 );
							}
							bitmap = Bitmap.createBitmap( bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true );
							ImageView img = new ImageView( getContext() );
							img.setImageBitmap( bitmap );
							// img.setLayoutParams(layoutParamsDeLImage);
							layoutPhoto.addView( img );
							layoutParamsDeLaPhoto.height = ( ( layoutPhotosApres.getWidth() / nbPhotosPerLine ) * bitmap.getHeight() ) / bitmap.getWidth();

							Button btDeletePhoto = new Button( getContext() );
							btDeletePhoto.setText( R.string.delete );
							RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams( 100, 100 );
							btParams.addRule( RelativeLayout.ALIGN_PARENT_TOP );
							btParams.addRule( RelativeLayout.ALIGN_PARENT_END );
							btDeletePhoto.setLayoutParams( btParams );
							layoutPhoto.addView( btDeletePhoto );

							btDeletePhoto.setOnClickListener( v -> deletePhotoApres( entryImage.getValue() ) );

						} else {

							Random rnd = new Random();
							int color = Color.argb( 255, rnd.nextInt( 256 ), rnd.nextInt( 256 ), rnd.nextInt( 256 ) );
							layoutPhoto.setBackgroundColor( color );
						}

						layoutPhoto.setLayoutParams( layoutParamsDeLaPhoto );

						layoutPhoto.requestLayout();

						ligneContenantLaPhoto.addView( layoutPhoto );

						ligneContenantLaPhoto.requestLayout();
					}

					// Ajout des lignes dans le conteneur de photos
					for ( Map.Entry<Integer, LinearLayout> entryLines : mapLines.entrySet() ) {
						layoutPhotosApres.addView( entryLines.getValue() );
					}

					layoutPhotosApres.requestLayout();
				}
			}
		} catch ( IOException ex ) {
			ex.printStackTrace();
		}
	}

	/**
	 * Suppression d'une photo dans la liste des photos apres lechantier
	 *
	 * @param photoPathToDelete
	 *            La photo à supprimer
	 */
	private void deletePhotoApres( String photoPathToDelete ) {
		listePhotosApres.remove( photoPathToDelete );
		handlePhotosApresString();
		saveReportInternal();
	}
	// endregion

	/**
	 * Initialisation des evenements d'agrandissement des détails (panneaux
	 * dépliables )
	 */
	private void initEventsDepliables() {

		View.OnClickListener layoutInterListener = ( v -> {
			if ( expandableLayoutDetailIntervention.isExpanded() ) {
				expandableLayoutDetailIntervention.collapse();
				btShowDetailIntervention.setImageResource( R.drawable.ic_bt_drop_down );
			} else {
				expandableLayoutDetailIntervention.expand( true );
				btShowDetailIntervention.setImageResource( R.drawable.ic_bt_drop_up );
			}
		} );

		View.OnClickListener layoutPhotosListener = ( v -> {
			if ( expandableLayoutDetailPhotos.isExpanded() ) {
				expandableLayoutDetailPhotos.collapse();
				btShowDetailPhotos.setImageResource( R.drawable.ic_bt_drop_down );
			} else {
				expandableLayoutDetailPhotos.expand( true );
				btShowDetailPhotos.setImageResource( R.drawable.ic_bt_drop_up );
			}
		} );

		View.OnClickListener layoutAvantListener = ( v -> {
			if ( expandableLayoutDetailAvant.isExpanded() ) {
				expandableLayoutDetailAvant.collapse();
				btShowDetailAvant.setImageResource( R.drawable.ic_bt_drop_down );
			} else {
				expandableLayoutDetailAvant.expand( true );
				btShowDetailAvant.setImageResource( R.drawable.ic_bt_drop_up );
			}
		} );

		View.OnClickListener layoutJourneeListener = ( v -> {
			if ( expandableLayoutDetailJournee.isExpanded() ) {
				expandableLayoutDetailJournee.collapse();
				btShowDetailJournee.setImageResource( R.drawable.ic_bt_drop_down );
			} else {
				expandableLayoutDetailJournee.expand( true );
				btShowDetailJournee.setImageResource( R.drawable.ic_bt_drop_up );
			}
		} );

		View.OnClickListener layoutFinChantierListener = ( v -> {
			if ( expandableLayoutDetailFinChantier.isExpanded() ) {
				expandableLayoutDetailFinChantier.collapse();
				btShowDetailFinChantier.setImageResource( R.drawable.ic_bt_drop_down );
			} else {
				expandableLayoutDetailFinChantier.expand( true );
				btShowDetailFinChantier.setImageResource( R.drawable.ic_bt_drop_up );
			}
		} );

		layoutTitleIntervention.setOnClickListener( layoutInterListener );
		btShowDetailIntervention.setOnClickListener( layoutInterListener );

		layoutTitlePhotos.setOnClickListener( layoutPhotosListener );
		btShowDetailPhotos.setOnClickListener( layoutPhotosListener );

		layoutTitleAvant.setOnClickListener( layoutAvantListener );
		btShowDetailAvant.setOnClickListener( layoutAvantListener );

		layoutTitleJournee.setOnClickListener( layoutJourneeListener );
		btShowDetailJournee.setOnClickListener( layoutJourneeListener );

		layoutTitleFinChantier.setOnClickListener( layoutFinChantierListener );
		btShowDetailFinChantier.setOnClickListener( layoutFinChantierListener );

		// Modification de la date en cours
		btPrevDate.setOnClickListener( view -> decreaseCurrentDate() );
		btNextDate.setOnClickListener( view -> increaseCurrentDate() );
	}

	/**
	 * Injection de l'intervention
	 *
	 * @param inter
	 *            L'intervention
	 */
	public void setIntervention( CRMPhonePlanningInterventionDTO inter ) {
		this.intervention = inter;
	}

	/**
	 * Injection de la date du rapport de chantier selectionné par l'utilisteur
	 *
	 * @param dateDuRapport
	 *            La date du rapport de chantier
	 */
	public void setDateRapport( LocalDate dateDuRapport ) {
		this.dateEnCours = dateDuRapport;
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

		// Création ou récupération du rapport de la date selectionnée
		initRapport();
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

		// Création ou récupération du rapport de la date selectionnée
		initRapport();
	}

	/**
	 * Initialisation des evenements liés à l'utilisateur (selection/saisie)
	 */
	private void initEventsUser() {
		rgControleKit.setOnCheckedChangeListener( ( group, checkedId ) -> updateRapport( group, checkedId ) );
		rgDevisReajuste.setOnCheckedChangeListener( ( group, checkedId ) -> updateRapport( group, checkedId ) );
		etDevisReajusteVolume.addTextChangedListener( userTextWatcher() );
		etDevisReajusteValeur.addTextChangedListener( userTextWatcher() );
		etDevisReajusteTotal.addTextChangedListener( userTextWatcher() );
		spChefDeChantier.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected( AdapterView<?> parent, View view, int position, long id ) {
				updateRapportJournee();
			}

			@Override
			public void onNothingSelected( AdapterView<?> parent ) {
				updateRapportJournee();
			}
		} );
		rgRetard.setOnCheckedChangeListener( ( group, checkedId ) -> updateRapport( group, checkedId ) );
		rgRetardClientPrevenu.setOnCheckedChangeListener( ( group, checkedId ) -> updateRapport( group, checkedId ) );
		rgDeterioration.setOnCheckedChangeListener( ( group, checkedId ) -> updateRapport( group, checkedId ) );
		rgDechetPartic.setOnCheckedChangeListener( ( group, checkedId ) -> updateRapport( group, checkedId ) );
		rgDechetPro.setOnCheckedChangeListener( ( group, checkedId ) -> updateRapport( group, checkedId ) );
		rgBennes.setOnCheckedChangeListener( ( group, checkedId ) -> updateRapport( group, checkedId ) );
		rgDepotVente.setOnCheckedChangeListener( ( group, checkedId ) -> updateRapport( group, checkedId ) );
		rgAssociation.setOnCheckedChangeListener( ( group, checkedId ) -> updateRapport( group, checkedId ) );
		rgRamene.setOnCheckedChangeListener( ( group, checkedId ) -> updateRapport( group, checkedId ) );
		rgMenage.setOnCheckedChangeListener( ( group, checkedId ) -> updateRapport( group, checkedId ) );
		rgRespectConsignes.setOnCheckedChangeListener( ( group, checkedId ) -> updateRapport( group, checkedId ) );
		rgReglement.setOnCheckedChangeListener( ( group, checkedId ) -> updateRapport( group, checkedId ) );
		rgRemarques.setOnCheckedChangeListener( ( group, checkedId ) -> updateRapport( group, checkedId ) );

		etDevisReajusteVolume.addTextChangedListener( userTextWatcher() );
		etDevisReajusteValeur.addTextChangedListener( userTextWatcher() );
		etDevisReajusteTotal.addTextChangedListener( userTextWatcher() );
		etRetardRaison.addTextChangedListener( userTextWatcher() );
		etHeureArrivee.addTextChangedListener( userTextWatcher() );
		etHeureDepart.addTextChangedListener( userTextWatcher() );
		etDeteriorationDetails.addTextChangedListener( userTextWatcher() );
		etDechetParticVolume.addTextChangedListener( userTextWatcher() );
		etDechetProVolumeDib.addTextChangedListener( userTextWatcher() );
		etDechetProVolumeBois.addTextChangedListener( userTextWatcher() );
		etDechetProVolumeFerraille.addTextChangedListener( userTextWatcher() );
		etDechetProVolumePapier.addTextChangedListener( userTextWatcher() );
		etDechetProVolumeAutres.addTextChangedListener( userTextWatcher() );
		etBennesVolumeDib.addTextChangedListener( userTextWatcher() );
		etBennesVolumeBois.addTextChangedListener( userTextWatcher() );
		etBennesVolumeFerraille.addTextChangedListener( userTextWatcher() );
		etBennesVolumePapier.addTextChangedListener( userTextWatcher() );
		etBennesVolumeAutres.addTextChangedListener( userTextWatcher() );
		etDepotVenteVolume.addTextChangedListener( userTextWatcher() );
		etAssociationVolume.addTextChangedListener( userTextWatcher() );
		etRameneVolume.addTextChangedListener( userTextWatcher() );
		etVolumeCamionRamene.addTextChangedListener( userTextWatcher() );
		etVolumeCamionFerraille.addTextChangedListener( userTextWatcher() );
		etVolumeCamionPapier.addTextChangedListener( userTextWatcher() );
		etVolumeCamionDecheterie.addTextChangedListener( userTextWatcher() );
		etMenageTemps.addTextChangedListener( userTextWatcher() );
		etRespectConsignesDetails.addTextChangedListener( userTextWatcher() );
		etReglementMontant.addTextChangedListener( userTextWatcher() );
		etReglementInfos.addTextChangedListener( userTextWatcher() );
		etRemarques.addTextChangedListener( userTextWatcher() );
	}

	// Instance du textwatcher sur les champs de saisie utilisateur
	private TextWatcher userTextWatcher = null;

	/**
	 * Instanciation d'un textwatcher unique(singleton) pour chaque champ de saisie
	 * utilisateur
	 *
	 * @return Instance d'un textwatcher
	 */
	private TextWatcher userTextWatcher() {
		if ( null == userTextWatcher ) {
			userTextWatcher = new TextWatcher() {

				@Override
				public void beforeTextChanged( CharSequence s, int start, int count, int after ) {}

				@Override
				public void onTextChanged( CharSequence s, int start, int before, int count ) {}

				@Override
				public void afterTextChanged( Editable s ) {
					updateRapport();
				}
			};
		}
		return userTextWatcher;
	}

	/**
	 * Insertion des données dans la vue
	 */
	private void setForm() {
		setFormPhotos();

		setFormAvant();

		setFormEquipe();

		setFormJournee();

		setFormFinDeChantier();

		updateMontantRestant();
	}

	private void setInputs() {
		// Liste déroulante du chef d'equipe
		Collections.sort( listeSalaries, ( s1, s2 ) -> s1.getNom().compareTo( s2.getNom() ) );
		ArrayAdapter<SalarieLightDTO> adapter = new ArrayAdapter<>( this.getContext(), android.R.layout.simple_spinner_item, listeSalaries );
		adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		spChefDeChantier.setAdapter( adapter );
	}

	/**
	 * Mise en forme des données dans le panneau d'intervention
	 */
	private void setFormIntervention() {
		lbClient.setText( extractIdentiteClient() );
		lbAdresse.setText( extractAdresseChantier() );
		if ( null != detailsInter.getVolume() ) {
			lbVolume.setText( detailsInter.getVolume().toString() + " m³" );
		}
		if ( null != detailsInter.getValeur() ) {
			lbValeur.setText( detailsInter.getValeur().toString() + " €" );
		}
		if ( null != detailsInter.getTotal() ) {
			lbMontant.setText( detailsInter.getTotal().toString() + " €" );
		}
		if ( null != detailsInter.getAccompte() ) {
			lbAccompte.setText( detailsInter.getAccompte().toString() + " €" );
		}
		if ( null != detailsInter.getDevisSigne() ) {
			lbDevisSigne.setText( null != detailsInter.getDevisSigne() || !detailsInter.getDevisSigne() ? "Oui" : "Non" );
		}
	}

	/**
	 * Mise en forme des données dans le panneau des photos
	 */
	private void setFormPhotos() {
		String rapportPhotoFilePath = getActivity().getFilesDir().getPath() + "/rapports/photos/" + intervention.getFkDevis() + "/";
		File rapportDirectory = new File( rapportPhotoFilePath );

		if ( !rapportDirectory.exists() ) {
			rapportDirectory.mkdirs();
		}

		String rapportPhotoFilePathAvant = rapportPhotoFilePath + "avant/";
		File rapportAvantDirectory = new File( rapportPhotoFilePathAvant );

		if ( !rapportAvantDirectory.exists() ) {
			rapportAvantDirectory.mkdirs();
		}

		String rapportPhotoFilePathRue = rapportPhotoFilePath + "rue/";
		File rapportRueDirectory = new File( rapportPhotoFilePathRue );

		if ( !rapportRueDirectory.exists() ) {
			rapportRueDirectory.mkdirs();
		}

		String rapportPhotoFilePathApres = rapportPhotoFilePath + "apres/";
		File rapportApresDirectory = new File( rapportPhotoFilePathApres );

		if ( !rapportApresDirectory.exists() ) {
			rapportApresDirectory.mkdirs();
		}

		int numPhoto = 0;
		if ( null != rapportChantier.getListePhotosAvant() && !rapportChantier.getListePhotosAvant().isEmpty() ) {
			this.listePhotosAvant.clear();
			for ( CRMPhoneRapportChantierPhotoDTO photoAvant : rapportChantier.getListePhotosAvant() ) {
				numPhoto++;
				byte[] decodedBytes = Base64.getMimeDecoder().decode( photoAvant.getContentThumbnail() );
				try ( FileOutputStream fos = new FileOutputStream( rapportPhotoFilePathAvant + numPhoto + ".jpg" ) ) {
					fos.write( decodedBytes );
					this.listePhotosAvant.add( rapportPhotoFilePathAvant + numPhoto + ".jpg" );
				} catch ( IOException ex ) {
					ex.printStackTrace();
				}
			}
			handlePhotosAvantString();
		}
		if ( null != rapportChantier.getListePhotosRue() && !rapportChantier.getListePhotosRue().isEmpty() ) {
			this.listePhotosRue.clear();
			for ( CRMPhoneRapportChantierPhotoDTO photoRue : rapportChantier.getListePhotosRue() ) {
				numPhoto++;
				byte[] decodedBytes = Base64.getMimeDecoder().decode( photoRue.getContentThumbnail() );
				try ( FileOutputStream fos = new FileOutputStream( rapportPhotoFilePathRue + numPhoto + ".jpg" ) ) {
					fos.write( decodedBytes );
					this.listePhotosRue.add( rapportPhotoFilePathRue + numPhoto + ".jpg" );
				} catch ( IOException ex ) {
					ex.printStackTrace();
				}
			}
			handlePhotosRueString();
		}
		if ( null != rapportChantier.getListePhotosApres() && !rapportChantier.getListePhotosApres().isEmpty() ) {
			this.listePhotosApres.clear();
			for ( CRMPhoneRapportChantierPhotoDTO photoApres : rapportChantier.getListePhotosApres() ) {
				numPhoto++;
				byte[] decodedBytes = Base64.getMimeDecoder().decode( photoApres.getContentThumbnail() );
				try ( FileOutputStream fos = new FileOutputStream( rapportPhotoFilePathApres + numPhoto + ".jpg" ) ) {
					fos.write( decodedBytes );
					this.listePhotosApres.add( rapportPhotoFilePathApres + numPhoto + ".jpg" );
				} catch ( IOException ex ) {
					ex.printStackTrace();
				}
			}
			handlePhotosApresString();
		}
		// Injecter les photos sous forme de fichier dans le rpeertoire en cache
		// Injecter le nom des fichiers créés dans les listes des noms de photos et
		// appeler les methodes handlePhotoAvantString( List<String> listeFichiers)
	}

	/**
	 * Mise en forme des données dans le panneau avant
	 */
	private void setFormAvant() {
		if ( null != rapportChantier.isControleKit() ) {
			if ( rapportChantier.isControleKit() ) {
				rbControleKitOui.setChecked( true );
			} else {
				rbControleKitNon.setChecked( true );
			}
		}

		if ( null != rapportChantier.isDevisReajuste() ) {
			if ( rapportChantier.isDevisReajuste() ) {
				rbDevisReajusteOui.setChecked( true );
				layoutDevisReajuste.setVisibility( View.VISIBLE );
			} else {
				rbDevisReajusteNon.setChecked( true );
				layoutDevisReajuste.setVisibility( View.GONE );
			}
		}

		if ( null != rapportChantier.getDevisReajusteVolume() ) {
			etDevisReajusteVolume.setText( rapportChantier.getDevisReajusteVolume().toString() );
		}

		if ( null != rapportChantier.getDevisReajusteValeur() ) {
			etDevisReajusteValeur.setText( rapportChantier.getDevisReajusteValeur().toString() );
		}
		if ( null != rapportChantier.getDevisReajusteTotal() ) {
			etDevisReajusteTotal.setText( rapportChantier.getDevisReajusteTotal().toString() );
		}

		if ( null != rapportChantier.isRetard() ) {
			if ( rapportChantier.isRetard() ) {
				rbRetardOui.setChecked( true );
				layoutRetard.setVisibility( View.VISIBLE );
			} else {
				rbRetardNon.setChecked( true );
				layoutRetard.setVisibility( View.GONE );
			}
		}

		if ( null != rapportChantier.isRetardClientPrevenu() ) {
			if ( rapportChantier.isRetardClientPrevenu() ) {
				rbRetardClientPrevenuOui.setChecked( true );
			} else {
				rbRetardClientPrevenuNon.setChecked( true );
			}
		}

		if ( null != rapportChantier.isDeterioration() ) {
			if ( rapportChantier.isDeterioration() ) {
				rbDeteriorationOui.setChecked( true );
				layoutDeterioration.setVisibility( View.VISIBLE );
			} else {
				rbDeteriorationNon.setChecked( true );
				layoutDeterioration.setVisibility( View.GONE );
			}
		}
		if ( null != rapportChantier.isDechetPartic() ) {
			if ( rapportChantier.isDechetPartic() ) {
				rbDechetParticOui.setChecked( true );
				layoutDechetPartic.setVisibility( View.VISIBLE );
			} else {
				rbDechetParticNon.setChecked( true );
				layoutDechetPartic.setVisibility( View.GONE );
			}
		}

		if ( null != rapportChantier.isDechetPro() ) {
			if ( rapportChantier.isDechetPro() ) {
				rbDechetProOui.setChecked( true );
				layoutDechetPro.setVisibility( View.VISIBLE );
			} else {
				rbDechetProNon.setChecked( true );
				layoutDechetPro.setVisibility( View.GONE );
			}
		}

		if ( null != rapportChantier.isBennes() ) {
			if ( rapportChantier.isBennes() ) {
				rbBennesOui.setChecked( true );
				layoutBennes.setVisibility( View.VISIBLE );
			} else {
				rbBennesNon.setChecked( true );
				layoutBennes.setVisibility( View.GONE );
			}
		}

		if ( null != rapportChantier.isDepotVente() ) {
			if ( rapportChantier.isDepotVente() ) {
				rbDepotVenteOui.setChecked( true );
				layoutDepotVente.setVisibility( View.VISIBLE );
			} else {
				rbDepotVenteNon.setChecked( true );
				layoutDepotVente.setVisibility( View.GONE );
			}
		}

		if ( null != rapportChantier.isAssociation() ) {
			if ( rapportChantier.isAssociation() ) {
				rbAssociationOui.setChecked( true );
				layoutAssociation.setVisibility( View.VISIBLE );
			} else {
				rbAssociationNon.setChecked( true );
				layoutAssociation.setVisibility( View.GONE );
			}
		}

		if ( null != rapportChantier.isRamene() ) {
			if ( rapportChantier.isRamene() ) {
				rbRameneOui.setChecked( true );
				layoutRamene.setVisibility( View.VISIBLE );

			} else {
				rbRameneNon.setChecked( true );
				layoutRamene.setVisibility( View.GONE );
			}

		}

		if ( null != rapportChantier.isMenage() ) {
			if ( rapportChantier.isMenage() ) {
				rbMenageOui.setChecked( true );
				layoutMenage.setVisibility( View.VISIBLE );
			} else {
				rbMenageNon.setChecked( true );
				layoutMenage.setVisibility( View.GONE );
			}
		}
		if ( null != rapportChantier.isRespectConsignes() ) {
			if ( rapportChantier.isRespectConsignes() ) {
				rbRespectConsignesOui.setChecked( true );
				layoutRespectConsignes.setVisibility( View.GONE );
			} else {
				rbRespectConsignesNon.setChecked( true );
				layoutRespectConsignes.setVisibility( View.VISIBLE );
			}
		}
		if ( null != rapportChantier.isReglementRecuOuIndemnisation() ) {
			if ( rapportChantier.isReglementRecuOuIndemnisation() ) {
				rbReglementOui.setChecked( true );
				layoutReglements.setVisibility( View.VISIBLE );
				layoutReglementsInfos.setVisibility( View.GONE );
			} else {
				rbReglementNon.setChecked( true );
				layoutReglements.setVisibility( View.GONE );
				layoutReglementsInfos.setVisibility( View.VISIBLE );
			}
		}
		if ( null != rapportChantier.getRemarques() && !"".equals( rapportChantier.getRemarques() ) ) {
			rbRemarquesOui.setChecked( true );
			layoutRemarques.setVisibility( View.VISIBLE );
			etRemarques.setText( rapportChantier.getRemarques() );
		} else {
			rbRemarquesNon.setChecked( true );
			layoutRemarques.setVisibility( View.GONE );
		}

		if ( null != rapportChantier.getHeureArriveeChantier() ) {
			etHeureArrivee.setText( rapportChantier.getHeureArriveeChantier() );
		}

		if ( null != rapportChantier.getHeureDepartChantier() ) {
			etHeureDepart.setText( rapportChantier.getHeureDepartChantier() );
		}

		if ( null != rapportChantier.getRetardDetails() ) {
			etRetardRaison.setText( rapportChantier.getRetardDetails() );
		}
		if ( null != rapportChantier.getDeteriorationDetails() ) {
			etDeteriorationDetails.setText( rapportChantier.getDeteriorationDetails() );
		}

		if ( null != rapportChantier.getVolumeTraiteDechetPartic() ) {
			etDechetParticVolume.setText( rapportChantier.getVolumeTraiteDechetPartic().toString() );
		}

		if ( null != rapportChantier.getVolumeTraiteDechetProDib() ) {
			etDechetProVolumeDib.setText( rapportChantier.getVolumeTraiteDechetProDib().toString() );
		}
		if ( null != rapportChantier.getVolumeTraiteDechetProBois() ) {
			etDechetProVolumeBois.setText( rapportChantier.getVolumeTraiteDechetProBois().toString() );
		}
		if ( null != rapportChantier.getVolumeTraiteDechetProFerraille() ) {
			etDechetProVolumeFerraille.setText( rapportChantier.getVolumeTraiteDechetProFerraille().toString() );
		}
		if ( null != rapportChantier.getVolumeTraiteDechetProPapier() ) {
			etDechetProVolumePapier.setText( rapportChantier.getVolumeTraiteDechetProPapier().toString() );
		}
		if ( null != rapportChantier.getVolumeTraiteDechetProAutres() ) {
			etDechetProVolumeAutres.setText( rapportChantier.getVolumeTraiteDechetProAutres().toString() );
		}

		if ( null != rapportChantier.getVolumeTraiteBennesDib() ) {
			etBennesVolumeDib.setText( rapportChantier.getVolumeTraiteBennesDib().toString() );
		}
		if ( null != rapportChantier.getVolumeTraiteBennesBois() ) {
			etBennesVolumeBois.setText( rapportChantier.getVolumeTraiteBennesBois().toString() );
		}
		if ( null != rapportChantier.getVolumeTraiteBennesFerraille() ) {
			etBennesVolumeFerraille.setText( rapportChantier.getVolumeTraiteBennesFerraille().toString() );
		}
		if ( null != rapportChantier.getVolumeTraiteBennesPapier() ) {
			etBennesVolumePapier.setText( rapportChantier.getVolumeTraiteBennesPapier().toString() );
		}
		if ( null != rapportChantier.getVolumeTraiteBennesAutres() ) {
			etBennesVolumeAutres.setText( rapportChantier.getVolumeTraiteBennesAutres().toString() );
		}

		if ( null != rapportChantier.getVolumeTraiteDepotVente() ) {
			etDepotVenteVolume.setText( rapportChantier.getVolumeTraiteDepotVente().toString() );
		}

		if ( null != rapportChantier.getVolumeTraiteAssociation() ) {
			etAssociationVolume.setText( rapportChantier.getVolumeTraiteAssociation().toString() );
		}

		if ( null != rapportChantier.getVolumeTraiteRamene() ) {
			etRameneVolume.setText( rapportChantier.getVolumeTraiteRamene().toString() );
		}
		if ( null != rapportChantier.getVolumeCamionRamene() ) {
			etVolumeCamionRamene.setText( rapportChantier.getVolumeCamionRamene().toString() );
		}
		if ( null != rapportChantier.getVolumeCamionFerraille() ) {
			etVolumeCamionFerraille.setText( rapportChantier.getVolumeCamionFerraille().toString() );
		}
		if ( null != rapportChantier.getVolumeCamionPapier() ) {
			etVolumeCamionPapier.setText( rapportChantier.getVolumeCamionPapier().toString() );
		}
		if ( null != rapportChantier.getVolumeCamionDecheterie() ) {
			etVolumeCamionDecheterie.setText( rapportChantier.getVolumeCamionDecheterie().toString() );
		}

		if ( null != rapportChantier.getMenageTemps() ) {
			etMenageTemps.setText( rapportChantier.getMenageTemps() );
		}
		if ( null != rapportChantier.getRespectConsignesDetails() ) {
			etRespectConsignesDetails.setText( rapportChantier.getRespectConsignesDetails() );
		}
		if ( null != rapportChantier.getDetailsReglement() ) {
			etRespectConsignesDetails.setText( rapportChantier.getDetailsReglement() );
		}
		if ( null != rapportChantier.getMontantRecuOuIndemnise() ) {
			etReglementMontant.setText( rapportChantier.getMontantRecuOuIndemnise().toString() );
		}
		if ( null != rapportChantier.getRemarques() ) {
			etRemarques.setText( rapportChantier.getRemarques() );
		}
	}

	/**
	 * Mise en forme des éléments de la journée
	 */
	private void setFormJournee() {
		// Selection du chef de chantier:
		// Soit en fonction de ce qu'il y a de selectionné dans le rapport de chantier
		if ( null != rapportChantier.getFkChefDeChantier() ) {
			for ( SalarieLightDTO salarie : listeSalaries ) {
				if ( salarie.getId().equals( rapportChantier.getFkChefDeChantier() ) ) {
					int positionToSelect = ( (ArrayAdapter<SalarieLightDTO>) spChefDeChantier.getAdapter() ).getPosition( salarie );
					spChefDeChantier.setSelection( positionToSelect );
				}
			}
			// Soit en fonction de l'utilisateur identifié dans l'application
		} else {
			for ( SalarieLightDTO salarie : listeSalaries ) {
				if ( salarie.getId().equals( SessionPhone.getInstance().getSalarie().getId() ) ) {
					int positionToSelect = ( (ArrayAdapter<SalarieLightDTO>) spChefDeChantier.getAdapter() ).getPosition( salarie );
					spChefDeChantier.setSelection( positionToSelect );
				}
			}
		}
	}

	/**
	 * Mise en forme des données dans le panneau de la journée
	 */
	private void setFormEquipe() {
		mapEquipe.clear();

		layoutEquipeContent.removeAllViews();

		// Si la liste des membres de l'equipe est vide, on l'initialise
		if ( 0 == this.rapportChantier.getListeEquipe().size() ) {
			this.rapportChantier.getListeEquipe().add( new CRMPhoneRapportChantierEquipeDTO() );
		}

		// Insertion de l'equipe dans la map reliant les positions dans l'ecran aux
		// membres
		int position = 0;
		for ( CRMPhoneRapportChantierEquipeDTO membreDeLEquipeDansLeDTO : this.rapportChantier.getListeEquipe() ) {
			mapEquipe.put( position, membreDeLEquipeDansLeDTO );
			position++;
		}

		for ( int i2 = 0 ; i2 < mapEquipe.size() ; i2++ ) {
			LinearLayout ligneSalarie;

			if ( i2 < mapEquipe.size() - 1 ) {
				ligneSalarie = createLigneEquipeMoins( i2, mapEquipe.get( i2 ) );
			} else {
				ligneSalarie = createLigneEquipePlus( i2, mapEquipe.get( i2 ) );
			}
			// }
			layoutEquipeContent.addView( ligneSalarie );
		}
	}

	/**
	 * Creation d'une ligne de membre de l'equipe sue le chantier avec un bouton
	 * ajout associé afin de créer une nouvelle ligne si besoin
	 *
	 * @param position
	 *            La position du membre de l'equipe inséré
	 * @return La ligne de membre de l'equipe avec un bouton "+"
	 */
	private LinearLayout createLigneEquipePlus( int position, CRMPhoneRapportChantierEquipeDTO membre ) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );

		LinearLayout ligne = new LinearLayout( getContext() );
		ligne.setOrientation( LinearLayout.HORIZONTAL );
		ligne.setLayoutParams( params );

		Spinner spinnerSalarie = new Spinner( getContext() );
		spinnerSalarie.setId( View.generateViewId() );
		ArrayAdapter<SalarieLightDTO> adapter = new ArrayAdapter<>( this.getContext(), android.R.layout.simple_spinner_item, listeSalaries );
		adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		spinnerSalarie.setAdapter( adapter );

		LinearLayout.LayoutParams paramsSp = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 4f );
		paramsSp.weight = 4f;
		spinnerSalarie.setLayoutParams( paramsSp );

		if ( null != membre ) {
			for ( int positionInSpinner = 0 ; positionInSpinner < adapter.getCount() ; positionInSpinner++ ) {
				if ( adapter.getItem( positionInSpinner ).getId().equals( membre.getFkSalarie() ) ) {
					spinnerSalarie.setSelection( positionInSpinner );
				}
			}
		}

		spinnerSalarie.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected( AdapterView<?> parent, View view, int positionDontOnSEnTape, long id ) {
				mapEquipe.get( position ).setFkSalarie( ( (SalarieLightDTO) spinnerSalarie.getSelectedItem() ).getId() );
				updateRapportEquipe();
			}

			@Override
			public void onNothingSelected( AdapterView<?> parent ) {
				updateRapportEquipe();
			}
		} );

		Button btAdd = new Button( getContext() );
		btAdd.setId( View.generateViewId() );
		btAdd.setText( "+" );
		LinearLayout.LayoutParams btAddParams = new LinearLayout.LayoutParams( 100, 100 );
		btAdd.setLayoutParams( btAddParams );

		btAdd.setOnClickListener( ( listener ) -> {
			CRMPhoneRapportChantierEquipeDTO newMembre = new CRMPhoneRapportChantierEquipeDTO();
			this.rapportChantier.getListeEquipe().add( newMembre );
			setFormEquipe();
		} );

		ligne.addView( spinnerSalarie );
		ligne.addView( btAdd );

		return ligne;
	}

	/**
	 * Creation d'une ligne de membre de l'equipe sue le chantier avec un bouton
	 * suppression associé afin de supprimer la ligne si besoin
	 *
	 * @param position
	 *            La position du membre de l'equipe
	 * @return La ligne de membre de l'equipe avec un bouton "-"
	 */
	private LinearLayout createLigneEquipeMoins( int position, CRMPhoneRapportChantierEquipeDTO membre ) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT );

		LinearLayout ligne = new LinearLayout( getContext() );
		ligne.setOrientation( LinearLayout.HORIZONTAL );
		ligne.setLayoutParams( params );

		Spinner spinnerSalarie = new Spinner( getContext() );
		spinnerSalarie.setId( View.generateViewId() );
		ArrayAdapter<SalarieLightDTO> adapter = new ArrayAdapter<>( this.getContext(), android.R.layout.simple_spinner_item, listeSalaries );
		adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
		spinnerSalarie.setAdapter( adapter );

		if ( null != membre ) {
			for ( int positionInSpinner = 0 ; positionInSpinner < adapter.getCount() ; positionInSpinner++ ) {
				if ( adapter.getItem( positionInSpinner ).getId().equals( membre.getFkSalarie() ) ) {
					spinnerSalarie.setSelection( positionInSpinner );
				}
			}
		}

		spinnerSalarie.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected( AdapterView<?> parent, View view, int positionDontOnSEnTape, long id ) {
				mapEquipe.get( position ).setFkSalarie( ( (SalarieLightDTO) spinnerSalarie.getSelectedItem() ).getId() );
				updateRapportEquipe();
			}

			@Override
			public void onNothingSelected( AdapterView<?> parent ) {
				updateRapportEquipe();
			}
		} );

		LinearLayout.LayoutParams paramsSp = new LinearLayout.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 4f );
		paramsSp.weight = 4f;
		spinnerSalarie.setLayoutParams( paramsSp );

		Button btRemove = new Button( getContext() );
		btRemove.setId( View.generateViewId() );
		btRemove.setText( "-" );
		LinearLayout.LayoutParams btAddParams = new LinearLayout.LayoutParams( 100, 100 );
		btRemove.setLayoutParams( btAddParams );

		btRemove.setOnClickListener( ( listener ) -> {
			CRMPhoneRapportChantierEquipeDTO membreToDelete;
			List<CRMPhoneRapportChantierEquipeDTO> listeMembresAfterDelete = new ArrayList<>();
			for ( int i = 0 ; i < mapEquipe.size() ; i++ ) {
				membreToDelete = null;
				if ( position == i ) {
					membreToDelete = mapEquipe.get( i );
				}
				if ( null == membreToDelete ) {
					listeMembresAfterDelete.add( mapEquipe.get( i ) );
				}
			}

			// Création de la nouvelle liste des membres
			int newPos = 0;
			this.mapEquipe.clear();
			this.rapportChantier.getListeEquipe().clear();
			for ( CRMPhoneRapportChantierEquipeDTO membreToKeep : listeMembresAfterDelete ) {
				this.rapportChantier.getListeEquipe().add( membreToKeep );
				mapEquipe.put( newPos, membreToKeep );
				newPos++;
			}

			setFormEquipe();
		} );

		ligne.addView( spinnerSalarie );
		ligne.addView( btRemove );

		return ligne;
	}

	/**
	 * Mise en forme des données dans le panneau de fin de chantier
	 */
	private void setFormFinDeChantier() {

	}

	/**
	 * Envoi du rapport de de chantier
	 */
	private void tryToSendRapport() {
		try {
			// Chef d'equipe auto selectionné -> pas d'event pour la mise a jour du DTO
			SalarieLightDTO chefDeChantier = (SalarieLightDTO) spChefDeChantier.getSelectedItem();
			rapportChantier.setFkChefDeChantier( chefDeChantier.getId() );

			WsUtil.saveRapportChantier( this, null, mapPhotosThumbOrigin, rapportChantier );
		} catch ( Exception ex ) {
			Log.e( "ERROR", "Erreur lors de l'enregistrement du rapport de chantier" );
			ex.printStackTrace();
			FirebaseCrashlytics.getInstance().recordException(ex);
		}
	}

	/**
	 * Modification du rapport par le salarié = Sauvegarde locale des modifications
	 * à la volée.
	 * Methode déclenchée pour chaque saisie/selection utilisateur
	 */

	private void saveReportInternal() {
		try {
			rapportChantier.setDateRapport( DateUtils.localDateToCalendar( dateEnCours ) );
			rapportChantier.setFkUser( SessionPhone.getInstance().getUserDto().getId() );
			rapportChantier.setFkIntervention( intervention.getFkDevis() );
			rapportChantier.setFkChefDeChantier( ( (SalarieLightDTO) spChefDeChantier.getSelectedItem() ).getId() );
			if ( null != listePhotosAvant && !listePhotosAvant.isEmpty() ) {
				rapportChantier.getListePhotosAvant().clear();
				for ( String photoFile : listePhotosAvant ) {
					CRMPhoneRapportChantierPhotoDTO photo = new CRMPhoneRapportChantierPhotoDTO();
					photo.setFileName( photoFile );
					photo.setTypePhoto( "AVANT" );
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inPreferredConfig = Bitmap.Config.RGB_565;
					// if ( photoFile.toLowerCase().contains( "dcim" ) ) {
					// options.inSampleSize = 4;
					// }
					Bitmap original = BitmapFactory.decodeFile( photoFile, options );
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					if ( photoFile.toLowerCase().endsWith( "jpg" ) || photoFile.toLowerCase().endsWith( "jpeg" ) ) {
						original.compress( Bitmap.CompressFormat.JPEG, 100, out );
					} else if ( photoFile.toLowerCase().endsWith( "png" ) ) {
						original.compress( Bitmap.CompressFormat.PNG, 100, out );
					}
					photo.setContentThumbnail( android.util.Base64.encodeToString( out.toByteArray(), android.util.Base64.DEFAULT ) );
					rapportChantier.getListePhotosAvant().add( photo );
				}
			}
			if ( null != listePhotosRue && !listePhotosRue.isEmpty() ) {
				rapportChantier.getListePhotosRue().clear();
				for ( String photoFile : listePhotosRue ) {
					CRMPhoneRapportChantierPhotoDTO photo = new CRMPhoneRapportChantierPhotoDTO();
					photo.setFileName( photoFile );
					photo.setTypePhoto( "RUE" );
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inPreferredConfig = Bitmap.Config.RGB_565;
					if ( photoFile.toLowerCase().contains( "dcim" ) ) {
						options.inSampleSize = 4;
					}
					Bitmap original = BitmapFactory.decodeFile( photoFile, options );
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					if ( photoFile.toLowerCase().endsWith( "jpg" ) || photoFile.toLowerCase().endsWith( "jpeg" ) ) {
						original.compress( Bitmap.CompressFormat.JPEG, 100, out );
					} else if ( photoFile.toLowerCase().endsWith( "png" ) ) {
						original.compress( Bitmap.CompressFormat.PNG, 100, out );
					}
					photo.setContentThumbnail( android.util.Base64.encodeToString( out.toByteArray(), android.util.Base64.DEFAULT ) );
					rapportChantier.getListePhotosRue().add( photo );
				}
			}
			if ( null != listePhotosApres && !listePhotosApres.isEmpty() ) {
				rapportChantier.getListePhotosApres().clear();
				for ( String photoFile : listePhotosApres ) {
					CRMPhoneRapportChantierPhotoDTO photo = new CRMPhoneRapportChantierPhotoDTO();
					photo.setFileName( photoFile );
					photo.setTypePhoto( "APRES" );
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inPreferredConfig = Bitmap.Config.RGB_565;
					if ( photoFile.toLowerCase().contains( "dcim" ) ) {
						options.inSampleSize = 4;
					}
					Bitmap original = BitmapFactory.decodeFile( photoFile, options );
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					if ( photoFile.toLowerCase().endsWith( "jpg" ) || photoFile.toLowerCase().endsWith( "jpeg" ) ) {
						original.compress( Bitmap.CompressFormat.JPEG, 100, out );
					} else if ( photoFile.toLowerCase().endsWith( "png" ) ) {
						original.compress( Bitmap.CompressFormat.PNG, 100, out );
					}
					photo.setContentThumbnail( android.util.Base64.encodeToString( out.toByteArray(), android.util.Base64.DEFAULT ) );
					rapportChantier.getListePhotosApres().add( photo );
				}
			}

			StorageUtil.saveDtoIntoFileInJSONFormat( rapportChantier, fileNameReportAbsolutePath );

		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
	}

	/**
	 * Enregistrement des appuis sur les radiobuttons
	 *
	 * @param group
	 *            Le groupe de radiobutton
	 * @param checkedId
	 *            L'identifiant du radiobutton selectionné par l'utilisateur
	 */
	private void updateRapport( RadioGroup group, int checkedId ) {

		// Controle du kit
		if ( group.equals( rgControleKit ) ) {
			if ( checkedId == rbControleKitOui.getId() ) {
				this.rapportChantier.setControleKit( true );
			} else {
				this.rapportChantier.setControleKit( false );
			}

			// Reajustement du devis
		} else if ( group.equals( rgDevisReajuste ) ) {
			if ( checkedId == rbDevisReajusteOui.getId() ) {
				this.rapportChantier.setDevisReajuste( true );
				this.layoutDevisReajuste.setVisibility( View.VISIBLE );
			} else if ( checkedId == rbDevisReajusteNon.getId() ) {
				this.rapportChantier.setDevisReajuste( false );
				this.layoutDevisReajuste.setVisibility( View.GONE );
			} else {
				this.rapportChantier.setDevisReajuste( false );
				this.layoutDevisReajuste.setVisibility( View.GONE );
			}
		} else if ( group.equals( rgRetard ) ) {
			if ( checkedId == rbRetardOui.getId() ) {
				this.rapportChantier.setRetard( true );
				this.layoutRetard.setVisibility( View.VISIBLE );
			} else if ( checkedId == rbRetardNon.getId() ) {
				this.rapportChantier.setRetard( false );
				this.layoutRetard.setVisibility( View.GONE );
			} else {
				this.rapportChantier.setRetard( false );
				this.layoutRetard.setVisibility( View.GONE );
			}
		} else if ( group.equals( rgRetardClientPrevenu ) ) {
			if ( checkedId == rbRetardClientPrevenuOui.getId() ) {
				this.rapportChantier.setRetardClientPrevenu( true );
			} else if ( checkedId == rbRetardClientPrevenuNon.getId() ) {
				this.rapportChantier.setRetardClientPrevenu( false );
			}
		} else if ( group.equals( rgDeterioration ) ) {
			if ( checkedId == rbDeteriorationOui.getId() ) {
				this.rapportChantier.setDeterioration( true );
				this.layoutDeterioration.setVisibility( View.VISIBLE );
			} else if ( checkedId == rbDeteriorationNon.getId() ) {
				this.rapportChantier.setDeterioration( false );
				this.layoutDeterioration.setVisibility( View.GONE );
			} else {
				this.rapportChantier.setDeterioration( false );
				this.layoutDeterioration.setVisibility( View.GONE );
			}
		} else if ( group.equals( rgDechetPartic ) ) {
			if ( checkedId == rbDechetParticOui.getId() ) {
				this.rapportChantier.setDechetPartic( true );
				this.layoutDechetPartic.setVisibility( View.VISIBLE );
			} else if ( checkedId == rbDechetParticNon.getId() ) {
				this.rapportChantier.setDechetPartic( false );
				this.layoutDechetPartic.setVisibility( View.GONE );
			} else {
				this.rapportChantier.setDechetPartic( false );
				this.layoutDechetPartic.setVisibility( View.GONE );
			}
		} else if ( group.equals( rgDechetPro ) ) {
			if ( checkedId == rbDechetProOui.getId() ) {
				this.rapportChantier.setDechetPro( true );
				this.layoutDechetPro.setVisibility( View.VISIBLE );
			} else if ( checkedId == rbDechetProNon.getId() ) {
				this.rapportChantier.setDechetPro( false );
				this.layoutDechetPro.setVisibility( View.GONE );
			} else {
				this.rapportChantier.setDechetPro( false );
				this.layoutDechetPro.setVisibility( View.GONE );
			}
		} else if ( group.equals( rgBennes ) ) {
			if ( checkedId == rbBennesOui.getId() ) {
				this.rapportChantier.setBennes( true );
				this.layoutBennes.setVisibility( View.VISIBLE );
			} else if ( checkedId == rbBennesNon.getId() ) {
				this.rapportChantier.setBennes( false );
				this.layoutBennes.setVisibility( View.GONE );
			} else {
				this.rapportChantier.setBennes( false );
				this.layoutBennes.setVisibility( View.GONE );
			}
		} else if ( group.equals( rgDepotVente ) ) {
			if ( checkedId == rbDepotVenteOui.getId() ) {
				this.rapportChantier.setDepotVente( true );
				this.layoutDepotVente.setVisibility( View.VISIBLE );
			} else if ( checkedId == rbDepotVenteNon.getId() ) {
				this.rapportChantier.setDepotVente( false );
				this.layoutDepotVente.setVisibility( View.GONE );
			} else {
				this.rapportChantier.setDepotVente( false );
				this.layoutDepotVente.setVisibility( View.GONE );
			}
		} else if ( group.equals( rgAssociation ) ) {
			if ( checkedId == rbAssociationOui.getId() ) {
				this.rapportChantier.setAssociation( true );
				this.layoutAssociation.setVisibility( View.VISIBLE );
			} else if ( checkedId == rbAssociationNon.getId() ) {
				this.rapportChantier.setAssociation( false );
				this.layoutAssociation.setVisibility( View.GONE );
			} else {
				this.rapportChantier.setAssociation( false );
				this.layoutAssociation.setVisibility( View.GONE );
			}
		} else if ( group.equals( rgRamene ) ) {
			if ( checkedId == rbRameneOui.getId() ) {
				this.rapportChantier.setRamene( true );
				this.layoutRamene.setVisibility( View.VISIBLE );
			} else if ( checkedId == rbRameneNon.getId() ) {
				this.rapportChantier.setRamene( false );
				this.layoutRamene.setVisibility( View.GONE );
			} else {
				this.rapportChantier.setRamene( false );
				this.layoutRamene.setVisibility( View.GONE );
			}
		} else if ( group.equals( rgMenage ) ) {
			if ( checkedId == rbMenageOui.getId() ) {
				this.rapportChantier.setMenage( true );
				this.layoutMenage.setVisibility( View.VISIBLE );
			} else if ( checkedId == rbMenageNon.getId() ) {
				this.rapportChantier.setMenage( false );
				this.layoutMenage.setVisibility( View.GONE );
			} else {
				this.rapportChantier.setMenage( false );
				this.layoutMenage.setVisibility( View.GONE );
			}
		} else if ( group.equals( rgRespectConsignes ) ) {
			if ( checkedId == rbRespectConsignesOui.getId() ) {
				this.rapportChantier.setRespectConsignes( true );
				this.layoutRespectConsignes.setVisibility( View.GONE );
			} else if ( checkedId == rbRespectConsignesNon.getId() ) {
				this.rapportChantier.setRespectConsignes( false );
				this.layoutRespectConsignes.setVisibility( View.VISIBLE );
			} else {
				this.rapportChantier.setRespectConsignes( false );
				this.layoutRespectConsignes.setVisibility( View.GONE );
			}
		} else if ( group.equals( rgReglement ) ) {
			if ( checkedId == rbReglementOui.getId() ) {
				this.rapportChantier.setReglementRecuOuIndemnisation( true );
				this.layoutReglements.setVisibility( View.VISIBLE );
				this.layoutReglementsInfos.setVisibility( View.GONE );
			} else if ( checkedId == rbReglementNon.getId() ) {
				this.rapportChantier.setReglementRecuOuIndemnisation( false );
				this.layoutReglements.setVisibility( View.GONE );
				this.layoutReglementsInfos.setVisibility( View.VISIBLE );
			} else {
				this.rapportChantier.setReglementRecuOuIndemnisation( null );
				this.layoutReglements.setVisibility( View.GONE );
				this.layoutReglementsInfos.setVisibility( View.GONE );
			}
		} else if ( group.equals( rgRemarques ) ) {
			if ( checkedId == rbRemarquesOui.getId() ) {
				this.layoutRemarques.setVisibility( View.VISIBLE );
			} else if ( checkedId == rbRemarquesNon.getId() ) {
				this.layoutRemarques.setVisibility( View.GONE );
			} else {
				this.layoutRemarques.setVisibility( View.GONE );
			}
		} else if ( null != spChefDeChantier.getSelectedItem() ) {
			this.rapportChantier.setFkChefDeChantier( ( (SalarieLightDTO) spChefDeChantier.getSelectedItem() ).getId() );
		}
		saveReportInternal();
	}

	/**
	 * Enregistrement des actions utilisateurs sur l'equipe
	 */
	private void updateRapportEquipe() {

		this.rapportChantier.getListeEquipe().clear();

		for ( Map.Entry<Integer, CRMPhoneRapportChantierEquipeDTO> entryMembre : mapEquipe.entrySet() ) {
			CRMPhoneRapportChantierEquipeDTO equipe = new CRMPhoneRapportChantierEquipeDTO();
			equipe.setFkRapport( this.rapportChantier.getId() );
			equipe.setFkSalarie( entryMembre.getValue().getFkSalarie() );
			this.rapportChantier.getListeEquipe().add( equipe );
		}

		saveReportInternal();
	}

	/**
	 * Enregistrement des actions utilisateurs sur le rapport de la journée
	 */
	private void updateRapportJournee() {

	}

	/**
	 * Injection de la saisie utilisateur dans le DTO du rapport de chantier
	 */
	private void updateRapport() {

		// Devis réajusté
		if ( null != etDevisReajusteVolume.getText() && !"".equals( etDevisReajusteVolume.getText().toString() ) ) {
			this.rapportChantier.setDevisReajusteVolume( NumericUtils.stringToBigDecimal( etDevisReajusteVolume.getText().toString() ) );
		}
		if ( null != etDevisReajusteValeur.getText() && !"".equals( etDevisReajusteValeur.getText().toString() ) ) {
			this.rapportChantier.setDevisReajusteValeur( NumericUtils.stringToBigDecimal( etDevisReajusteValeur.getText().toString() ) );
		}
		if ( null != etDevisReajusteTotal.getText() && !"".equals( etDevisReajusteTotal.getText().toString() ) ) {
			this.rapportChantier.setDevisReajusteTotal( NumericUtils.stringToBigDecimal( etDevisReajusteTotal.getText().toString() ) );
			updateMontantRestant();
		}

		// Retards
		if ( null != etRetardRaison.getText() && !"".equals( etRetardRaison.getText().toString() ) ) {
			this.rapportChantier.setRetardDetails( etRetardRaison.getText().toString() );
		}

		// Heures
		if ( null != etHeureDepart.getText() && !"".equals( etHeureDepart.getText().toString() ) ) {
			this.rapportChantier.setHeureDepartChantier( etHeureDepart.getText().toString() );
		}
		if ( null != etHeureArrivee.getText() && !"".equals( etHeureArrivee.getText().toString() ) ) {
			this.rapportChantier.setHeureArriveeChantier( etHeureArrivee.getText().toString() );
		}

		// Deterioration
		if ( null != etDeteriorationDetails.getText() && !"".equals( etDeteriorationDetails.getText().toString() ) ) {
			this.rapportChantier.setDeteriorationDetails( etDeteriorationDetails.getText().toString() );
		}

		// VOLUMES TRAITES
		// -- Dechet partic
		if ( null != etDechetParticVolume.getText() && !"".equals( etDechetParticVolume.getText().toString() ) ) {
			this.rapportChantier.setVolumeTraiteDechetPartic( NumericUtils.stringToBigDecimal( etDechetParticVolume.getText().toString() ) );
		}
		// -- Dechet pro
		if ( null != etDechetProVolumeDib.getText() && !"".equals( etDechetProVolumeDib.getText().toString() ) ) {
			this.rapportChantier.setVolumeTraiteDechetProDib( NumericUtils.stringToBigDecimal( etDechetProVolumeDib.getText().toString() ) );
		}
		if ( null != etDechetProVolumeBois.getText() && !"".equals( etDechetProVolumeBois.getText().toString() ) ) {
			this.rapportChantier.setVolumeTraiteDechetProBois( NumericUtils.stringToBigDecimal( etDechetProVolumeBois.getText().toString() ) );
		}
		if ( null != etDechetProVolumeFerraille.getText() && !"".equals( etDechetProVolumeFerraille.getText().toString() ) ) {
			this.rapportChantier.setVolumeTraiteDechetProFerraille( NumericUtils.stringToBigDecimal( etDechetProVolumeFerraille.getText().toString() ) );
		}
		if ( null != etDechetProVolumePapier.getText() && !"".equals( etDechetProVolumePapier.getText().toString() ) ) {
			this.rapportChantier.setVolumeTraiteDechetProPapier( NumericUtils.stringToBigDecimal( etDechetProVolumePapier.getText().toString() ) );
		}
		if ( null != etDechetProVolumeAutres.getText() && !"".equals( etDechetProVolumeAutres.getText().toString() ) ) {
			this.rapportChantier.setVolumeTraiteDechetProAutres( NumericUtils.stringToBigDecimal( etDechetProVolumeAutres.getText().toString() ) );
		}
		// -- Bennes
		if ( null != etBennesVolumeDib.getText() && !"".equals( etBennesVolumeDib.getText().toString() ) ) {
			this.rapportChantier.setVolumeTraiteBennesDib( NumericUtils.stringToBigDecimal( etBennesVolumeDib.getText().toString() ) );
		}
		if ( null != etBennesVolumeBois.getText() && !"".equals( etBennesVolumeBois.getText().toString() ) ) {
			this.rapportChantier.setVolumeTraiteBennesBois( NumericUtils.stringToBigDecimal( etBennesVolumeBois.getText().toString() ) );
		}
		if ( null != etBennesVolumeFerraille.getText() && !"".equals( etBennesVolumeFerraille.getText().toString() ) ) {
			this.rapportChantier.setVolumeTraiteBennesFerraille( NumericUtils.stringToBigDecimal( etBennesVolumeFerraille.getText().toString() ) );
		}
		if ( null != etBennesVolumePapier.getText() && !"".equals( etBennesVolumePapier.getText().toString() ) ) {
			this.rapportChantier.setVolumeTraiteBennesPapier( NumericUtils.stringToBigDecimal( etBennesVolumePapier.getText().toString() ) );
		}
		if ( null != etBennesVolumeAutres.getText() && !"".equals( etBennesVolumeAutres.getText().toString() ) ) {
			this.rapportChantier.setVolumeTraiteBennesAutres( NumericUtils.stringToBigDecimal( etBennesVolumeAutres.getText().toString() ) );
		}
		// -- Depots vente
		if ( null != etDepotVenteVolume.getText() && !"".equals( etDepotVenteVolume.getText().toString() ) ) {
			this.rapportChantier.setVolumeTraiteDepotVente( NumericUtils.stringToBigDecimal( etDepotVenteVolume.getText().toString() ) );
		}
		// -- Association
		if ( null != etAssociationVolume.getText() && !"".equals( etAssociationVolume.getText().toString() ) ) {
			this.rapportChantier.setVolumeTraiteAssociation( NumericUtils.stringToBigDecimal( etAssociationVolume.getText().toString() ) );
		}
		// -- Ramene
		if ( null != etRameneVolume.getText() && !"".equals( etRameneVolume.getText().toString() ) ) {
			this.rapportChantier.setVolumeTraiteRamene( NumericUtils.stringToBigDecimal( etRameneVolume.getText().toString() ) );
		}
		// VOLUMES CAMIONS
		if ( null != etVolumeCamionRamene.getText() && !"".equals( etVolumeCamionRamene.getText().toString() ) ) {
			this.rapportChantier.setVolumeCamionRamene( NumericUtils.stringToBigDecimal( etVolumeCamionRamene.getText().toString() ) );
		}
		if ( null != etVolumeCamionFerraille.getText() && !"".equals( etVolumeCamionFerraille.getText().toString() ) ) {
			this.rapportChantier.setVolumeCamionFerraille( NumericUtils.stringToBigDecimal( etVolumeCamionFerraille.getText().toString() ) );
		}
		if ( null != etVolumeCamionPapier.getText() && !"".equals( etVolumeCamionPapier.getText().toString() ) ) {
			this.rapportChantier.setVolumeCamionPapier( NumericUtils.stringToBigDecimal( etVolumeCamionPapier.getText().toString() ) );
		}
		if ( null != etVolumeCamionDecheterie.getText() && !"".equals( etVolumeCamionDecheterie.getText().toString() ) ) {
			this.rapportChantier.setVolumeCamionDecheterie( NumericUtils.stringToBigDecimal( etVolumeCamionDecheterie.getText().toString() ) );
		}

		// Temps de menage
		if ( null != etMenageTemps.getText() && !"".equals( etMenageTemps.getText().toString() ) ) {
			this.rapportChantier.setMenageTemps( etMenageTemps.getText().toString() );
		}

		// Respect des consignes
		if ( null != etRespectConsignesDetails.getText() && !"".equals( etRespectConsignesDetails.getText().toString() ) ) {
			this.rapportChantier.setRespectConsignesDetails( etRespectConsignesDetails.getText().toString() );
		}

		// Reglements
		if ( null != etReglementInfos.getText() && !"".equals( etReglementInfos.getText().toString() ) ) {
			this.rapportChantier.setDetailsReglement( etReglementInfos.getText().toString() );
		}
		if ( null != etReglementMontant.getText() && !"".equals( etReglementMontant.getText().toString() ) ) {
			this.rapportChantier.setMontantRecuOuIndemnise( NumericUtils.stringToBigDecimal( etReglementMontant.getText().toString() ) );
		}

		// Remarques
		if ( null != etRemarques.getText() && !"".equals( etRemarques.getText().toString() ) ) {
			this.rapportChantier.setRemarques( etRemarques.getText().toString() );
		}

		saveReportInternal();
	}

	/**
	 * Mise a jour du montant restant à régler
	 */
	private void updateMontantRestant() {
		if ( null != etDevisReajusteTotal.getText() && !"".equals( etDevisReajusteTotal.getText().toString() ) ) {

			BigDecimal montantRestant = rapportChantier.getDevisReajusteTotal();
			if ( null != detailsInter.getAccompte() ) {
				montantRestant = montantRestant.subtract( detailsInter.getAccompte() );
			}
			lbMontantRestant.setText( montantRestant.toString() + " €" );

		} else {
			BigDecimal montantRestant = detailsInter.getTotal();
			if ( null != detailsInter.getAccompte() ) {
				montantRestant = montantRestant.subtract( detailsInter.getAccompte() );
				lbMontantRestant.setText( montantRestant.toString() + " €" );
			}
		}
	}

	/**
	 * Récupération de l'identité du client
	 *
	 * @return L'identité du client
	 */
	private String extractIdentiteClient() {
		StringBuilder sb = new StringBuilder();

		if ( null != client ) {
			if ( null != client.getCivilite() && !"".equals( client.getCivilite() ) ) {
				sb.append( client.getCivilite() ).append( " " );
			}
			if ( null != client.getNom() && !"".equals( client.getNom() ) ) {
				sb.append( client.getNom() );
			}
			if ( null != client.getPrenom() && !"".equals( client.getPrenom() ) ) {
				sb.append( " " ).append( client.getPrenom() );
			}
		}

		return sb.toString();
	}

	/**
	 * Récupération de l'adresse du chantier
	 *
	 * @return L'adresse du chantier
	 */
	private String extractAdresseChantier() {
		StringBuilder sb = new StringBuilder();

		if ( null != detailsInter ) {
			if ( null != detailsInter.getAdresse() && !"".equals( detailsInter.getAdresse() ) ) {
				sb.append( detailsInter.getAdresse() );
			}
			if ( null != detailsInter.getAdresseComplement() && !"".equals( detailsInter.getAdresseComplement() ) ) {
				sb.append( " " ).append( detailsInter.getAdresseComplement() );
			}
			if ( null != detailsInter.getCodePostal() && !"".equals( detailsInter.getCodePostal() ) ) {
				sb.append( " " ).append( detailsInter.getCodePostal() );
			}
			if ( null != detailsInter.getVille() && !"".equals( detailsInter.getVille() ) ) {
				sb.append( " " ).append( detailsInter.getVille() );
			}
		}

		return sb.toString();
	}

	@Override
	public void notifyResponse( WsName wsName, Map<String, Object> callbackResources, Object response ) {

		switch ( wsName ) {

			case UPLOAD_PHOTO :
				System.out.println( "wsName = " + wsName );
				break;

			case GET_INTERVENTION_DETAIL_BY_ID_DEVIS :
				if ( null != response ) {
					detailsInter = (CRMPhoneInterventionDetailsDTO) response;
					if ( null != detailsInter ) {
						setFormIntervention();
					}
				}
				break;

			case GET_LISTE_SALARIES_ACTIFS :
				if ( null != response ) {
					listeSalaries = (List<SalarieLightDTO>) response;
					if ( null != listeSalaries ) {
						setInputs();
					}
				}
				break;

			case GET_RAPPORT_CHANTIER :
				if ( null != response ) {
					rapportChantier = (CRMPhoneRapportChantierDTO) response;

					if ( !DateUtils.calendarToLocalDate( rapportChantier.getDateRapport() ).equals( dateEnCours ) ) {
						// On conserve les données d'ouverture de chantier et les photos
						// Par contre on efface les données du rapport de la journée dans l'instance en
						// mémoire afin d'initialiser
						// un rapport de chantier prérempli pour la journée
						copyOldReport();
					}

				} else {
					rapportChantier = new CRMPhoneRapportChantierDTO();
				}
				break;

			case SAVE_RAPPORT_CHANTIER :
				if ( null != response ) {
					// Retour à la page d'accueil avec un message de confirmation indiquant que tout
					// s'est déroulée correctement
					new AlertDialog.Builder( getContext() ).setIcon( android.R.drawable.ic_dialog_alert ).setTitle( "Rapport de chantier" ).setMessage( "Rapport de chantier enregistré" )
							.setPositiveButton( "OK", ( dialog, which ) -> ( (MainActivity) getActivity() ).showAccueil() ).show();

					rapportChantier = (CRMPhoneRapportChantierDTO) response;
					StorageUtil.saveDtoIntoFileInJSONFormat( rapportChantier, fileNameReportAbsolutePath );
					( (MainActivity) this.getActivity() ).removeWaitingScreen();
				} else {
					new AlertDialog.Builder( getContext() ).setIcon( android.R.drawable.ic_dialog_alert ).setTitle( "ERREUR" ).setMessage( "Erreur lors de l'envoi du rapport de chantier" )
							.setPositiveButton( "OK", null ).show();
				}
				break;

			default :
				throw new IllegalStateException( "Reception d'une reponse de webservice provenant de : " + wsName + ". Les données associées sont : " + response );
		}

		if ( !wsName.equals( WsName.SAVE_RAPPORT_CHANTIER ) ) {
			if ( null != client && null != detailsInter && null != listeSalaries && null != rapportChantier ) {
				setForm();
			} else {
				( (MainActivity) this.getActivity() ).showWaitingScreen();
			}
		}
	}

	@Override
	public Fragment getFragmentSource() {
		return this;
	}
}