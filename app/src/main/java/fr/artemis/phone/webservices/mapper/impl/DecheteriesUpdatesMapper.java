package fr.artemis.phone.webservices.mapper.impl;

import fr.artemis.phone.dto.CRMPhoneDecheteriesUpdatesDTO;
import fr.artemis.phone.dto.CRMPhoneDepotsVenteDTO;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.AbstractDefaultMapper;

public class DecheteriesUpdatesMapper extends AbstractDefaultMapper<CRMPhoneDecheteriesUpdatesDTO> {

	@Override
	public CRMPhoneDecheteriesUpdatesDTO map( SoapObject objectToMap ) {
		CRMPhoneDecheteriesUpdatesDTO dto = null;

		if ( null != objectToMap && objectToMap.getPropertyCount() > 0 ) {
			dto = new CRMPhoneDecheteriesUpdatesDTO();

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