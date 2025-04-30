package fr.artemis.phone.webservices.mapper.impl;

import fr.artemis.phone.dto.SalarieLightDTO;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.PropertyInfo;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;
import fr.artemis.phone.webservices.mapper.AbstractDefaultMapper;

/**
 * Classe de mapping de salarié
 */
public class SalarieMapper extends AbstractDefaultMapper<SalarieLightDTO> {

	@Override
	public SalarieLightDTO map( SoapObject objectToMap ) {
		SalarieLightDTO dto = null;

		if ( null != objectToMap && objectToMap.getPropertyCount() > 0 ) {
			dto = new SalarieLightDTO();

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