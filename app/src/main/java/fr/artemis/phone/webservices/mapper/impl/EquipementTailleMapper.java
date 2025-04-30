package fr.artemis.phone.webservices.mapper.impl;

import fr.artemis.phone.dto.CRMPhoneEquipementTailleDTO;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.AbstractDefaultMapper;

/**
 * Classe de mapping d'une taille d'equipement
 */
public class EquipementTailleMapper extends AbstractDefaultMapper<CRMPhoneEquipementTailleDTO> {

	@Override
	public CRMPhoneEquipementTailleDTO map( SoapObject objectToMap ) {
		CRMPhoneEquipementTailleDTO dto = null;

		if ( null != objectToMap && objectToMap.getPropertyCount() > 0 ) {
			dto = new CRMPhoneEquipementTailleDTO();

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