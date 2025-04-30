package fr.artemis.phone.webservices.mapper.impl;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import android.util.Log;

import fr.artemis.phone.dto.CRMPhoneHorairesDTO;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.AbstractDefaultMapper;

/**
 * Classe de mapping d'horaire de salarié
 */
public class HorairesMapper extends AbstractDefaultMapper<CRMPhoneHorairesDTO> {

	@Override
	public CRMPhoneHorairesDTO map( SoapObject objectToMap ) {
		CRMPhoneHorairesDTO dto = null;

		if ( null != objectToMap && objectToMap.getPropertyCount() > 0 ) {
			dto = new CRMPhoneHorairesDTO();

			PropertyInfo pi;
			for ( int index = 0 ; index < objectToMap.getPropertyCount() ; index++ ) {
				try {
					pi = new PropertyInfo();
					objectToMap.getPropertyInfo( index, null, pi );
					dto.setProperty( pi.name, objectToMap.getProperty( index ) );
				} catch ( Exception ex ) {
					ex.printStackTrace();
					FirebaseCrashlytics.getInstance().recordException( ex );
					Log.d( "TAG", "Erreur lors du mapping d'horaire de salairé." );
				}
			}
		}

		return dto;
	}
}