package fr.artemis.phone.webservices.mapper.impl;

import fr.artemis.phone.dto.CRMPhoneDecheteriesTarifsDTO;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.AbstractDefaultMapper;

public class DecheterieTarifMapper extends AbstractDefaultMapper<CRMPhoneDecheteriesTarifsDTO> {

	@Override
	public CRMPhoneDecheteriesTarifsDTO map( SoapObject objectToMap ) {
		CRMPhoneDecheteriesTarifsDTO dto = null;

		if ( null != objectToMap && objectToMap.getPropertyCount() > 0 ) {
			dto = new CRMPhoneDecheteriesTarifsDTO();

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