package fr.artemis.phone.webservices.mapper.impl;

import fr.artemis.phone.dto.CRMPhoneClientDTO;
import fr.artemis.phone.dto.CRMPhonePlanningDevisDTO;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.AbstractDefaultMapper;

/**
 * Classe de mapping d'un planning de devis
 */
public class PlanningDevisMapper extends AbstractDefaultMapper<CRMPhonePlanningDevisDTO> {

	@Override
	public CRMPhonePlanningDevisDTO map( SoapObject objectToMap ) {
		CRMPhonePlanningDevisDTO dto = null;

		if ( null != objectToMap && objectToMap.getPropertyCount() > 0 ) {
			dto = new CRMPhonePlanningDevisDTO();

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