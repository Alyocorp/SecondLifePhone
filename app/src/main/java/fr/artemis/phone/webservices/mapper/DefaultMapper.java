package fr.artemis.phone.webservices.mapper;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;

/**
 * Interface de mapping d'objet SOAP en DTO
 *
 * @param <DTO> Classe du DTO retourné
 */
public interface DefaultMapper<DTO extends KvmSerializable> {

	/**
	 * Mapping d'un objet SOAP en DTO
	 *
	 * @param objectToMap L'objet à mapper
	 * @return Le DTO mappé
	 */
	DTO map( SoapObject objectToMap );
}
