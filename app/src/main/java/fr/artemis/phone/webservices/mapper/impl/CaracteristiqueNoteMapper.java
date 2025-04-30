package fr.artemis.phone.webservices.mapper.impl;

import fr.artemis.phone.dto.CRMPhoneCaracteristiqueNoteDTO;
import fr.artemis.phone.dto.CRMPhoneEquipementDTO;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.AbstractDefaultMapper;

/**
 * Classe de mapping d'une note de caractéristique d'intervention
 */
public class CaracteristiqueNoteMapper extends AbstractDefaultMapper<CRMPhoneCaracteristiqueNoteDTO> {

	@Override
	public CRMPhoneCaracteristiqueNoteDTO map( SoapObject objectToMap ) {
		CRMPhoneCaracteristiqueNoteDTO dto = null;

		if ( null != objectToMap && objectToMap.getPropertyCount() > 0 ) {
			dto = new CRMPhoneCaracteristiqueNoteDTO();

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