package fr.artemis.phone.webservices.mapper.impl;

import fr.artemis.phone.dto.CRMPhonePlanningRapportDTO;
import fr.artemis.phone.dto.CRMPhoneRapportChantierPhotoDTO;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.AbstractDefaultMapper;

/**
 * Classe de mapping d'une photo de rapport de chantier
 */
public class PlanningRapportMapper extends AbstractDefaultMapper<CRMPhonePlanningRapportDTO> {

	@Override
	public CRMPhonePlanningRapportDTO map( SoapObject objectToMap ) {
		CRMPhonePlanningRapportDTO dto = null;

		if ( null != objectToMap && objectToMap.getPropertyCount() > 0 ) {
			dto = new CRMPhonePlanningRapportDTO();

			PropertyInfo pi;
			for ( int index = 0 ; index < objectToMap.getPropertyCount() ; index++ ) {
				pi = new PropertyInfo();
				objectToMap.getPropertyInfo( index, null, pi );
				dto.setProperty( pi.name, objectToMap.getProperty( index ) );
			}
		}

		return dto;
	}
}