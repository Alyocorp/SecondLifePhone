package fr.artemis.phone.webservices.mapper.impl;

import fr.artemis.phone.dto.CRMPhoneCaracteristiqueDTO;
import fr.artemis.phone.dto.CRMPhoneCaracteristiqueNoteDTO;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.AbstractDefaultMapper;

/**
 * Classe de mapping d'une note de caractéristique d'intervention
 */
public class CaracteristiqueMapper extends AbstractDefaultMapper<CRMPhoneCaracteristiqueDTO> {

	@Override
	public CRMPhoneCaracteristiqueDTO map( SoapObject objectToMap ) {
		CRMPhoneCaracteristiqueDTO dto = null;

		if ( null != objectToMap && objectToMap.getPropertyCount() > 0 ) {
			dto = new CRMPhoneCaracteristiqueDTO();

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