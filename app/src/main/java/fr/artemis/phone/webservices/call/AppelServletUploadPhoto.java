package fr.artemis.phone.webservices.call;

import java.io.File;
import java.util.Calendar;
import java.util.Map;

import fr.artemis.phone.dto.CRMPhoneRapportChantierDTO;
import fr.artemis.phone.dto.CRMPhoneRapportChantierPhotoDTO;
import fr.artemis.phone.utils.ConstantesWS;
import fr.artemis.phone.utils.MultipartUtility;
import fr.artemis.phone.utils.SessionPhone;
import fr.artemis.phone.webservices.AbstractCaller;
import fr.artemis.phone.webservices.WsCaller;
import fr.artemis.phone.webservices.WsName;

public class AppelServletUploadPhoto extends AbstractCaller<Void> {

	private CRMPhoneRapportChantierDTO rapportChantier;

	private final Map<String, String> mapPhotos;

	public AppelServletUploadPhoto( WsCaller source, Map<String, String> mapResources, WsName name ) {
		super( source, null, name );
		this.mapPhotos = mapResources;
	}

	public void setRapportChantier( CRMPhoneRapportChantierDTO rapportChantier ) {
		this.rapportChantier = rapportChantier;
	}

	@Override
	protected Void executionRequete() {
		try {
			if ( null != rapportChantier ) {

				String urlUploader;
				if ( SessionPhone.getInstance().isDevMode() ) {
					urlUploader = ConstantesWS.URL_CRM_UPLOAD_PHOTO_DEVMODE;
				} else {
					urlUploader = ConstantesWS.URL_CRM_UPLOAD_PHOTO_SERVER;
				}

				int numPhoto = 1;
				if ( null != rapportChantier.getListePhotosAvant() && !rapportChantier.getListePhotosAvant().isEmpty() ) {
					for ( CRMPhoneRapportChantierPhotoDTO photo : rapportChantier.getListePhotosAvant() ) {
						if ( null != mapPhotos.get( photo.getFileName() ) ) {
							File photoToUpload = new File( mapPhotos.get( photo.getFileName() ) );
							MultipartUtility multipart = new MultipartUtility( urlUploader, ConstantesWS.CHARSET_UTF8 );

							multipart.addHeaderField( "idSession", SessionPhone.getInstance().getUserDto().getIdSession() );
							multipart.addFormField( "typePhoto", "AVANT" );
							multipart.addFormField( "dateInter",
									rapportChantier.getDateRapport().get( Calendar.YEAR ) + "-" + rapportChantier.getDateRapport().get( Calendar.MONTH ) + "-" + rapportChantier.getDateRapport().get( Calendar.DAY_OF_MONTH ) );
							multipart.addFormField( "idInter", String.valueOf( rapportChantier.getFkIntervention() ) );
							multipart.addFormField( "photoName", String.valueOf( numPhoto ) );
							multipart.addFilePart( "fileUpload", photoToUpload );

							multipart.finish();
							numPhoto++;
						}
					}
				}

				if ( null != rapportChantier.getListePhotosRue() && !rapportChantier.getListePhotosRue().isEmpty() ) {
					numPhoto = 1;
					for ( CRMPhoneRapportChantierPhotoDTO photo : rapportChantier.getListePhotosRue() ) {
						if ( null != mapPhotos.get( photo.getFileName() ) ) {
							File photoToUpload = new File( mapPhotos.get( photo.getFileName() ) );
							MultipartUtility multipart = new MultipartUtility( urlUploader, ConstantesWS.CHARSET_UTF8 );

							multipart.addHeaderField( "idSession", SessionPhone.getInstance().getUserDto().getIdSession() );
							multipart.addFormField( "typePhoto", "RUE" );
							multipart.addFormField( "dateInter",
									rapportChantier.getDateRapport().get( Calendar.YEAR ) + "-" + rapportChantier.getDateRapport().get( Calendar.MONTH ) + "-" + rapportChantier.getDateRapport().get( Calendar.DAY_OF_MONTH ) );
							multipart.addFormField( "idInter", String.valueOf( rapportChantier.getFkIntervention() ) );
							multipart.addFormField( "photoName", String.valueOf( numPhoto ) );
							multipart.addFilePart( "fileUpload", photoToUpload );

							multipart.finish();
							numPhoto++;
						}
					}
				}

				if ( null != rapportChantier.getListePhotosApres() && !rapportChantier.getListePhotosApres().isEmpty() ) {
					numPhoto = 1;
					for ( CRMPhoneRapportChantierPhotoDTO photo : rapportChantier.getListePhotosApres() ) {
						if ( null != mapPhotos.get( photo.getFileName() ) ) {
							File photoToUpload = new File( mapPhotos.get( photo.getFileName() ) );
							MultipartUtility multipart = new MultipartUtility( urlUploader, ConstantesWS.CHARSET_UTF8 );

							multipart.addHeaderField( "idSession", SessionPhone.getInstance().getUserDto().getIdSession() );
							multipart.addFormField( "typePhoto", "APRES" );
							multipart.addFormField( "dateInter",
									rapportChantier.getDateRapport().get( Calendar.YEAR ) + "-" + rapportChantier.getDateRapport().get( Calendar.MONTH ) + "-" + rapportChantier.getDateRapport().get( Calendar.DAY_OF_MONTH ) );
							multipart.addFormField( "idInter", String.valueOf( rapportChantier.getFkIntervention() ) );
							multipart.addFormField( "photoName", String.valueOf( numPhoto ) );
							multipart.addFilePart( "fileUpload", photoToUpload );

							multipart.finish();
							numPhoto++;
						}
					}
				}
			}
		} catch( Exception ex ) {
			ex.printStackTrace();
		}
		return null;
	}
}