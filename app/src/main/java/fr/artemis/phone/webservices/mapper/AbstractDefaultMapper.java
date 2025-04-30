package fr.artemis.phone.webservices.mapper;

import java.util.ArrayList;
import java.util.List;

import fr.artemis.phone.lib.ksoap.ksoap2.serialization.KvmSerializable;
import fr.artemis.phone.lib.ksoap.ksoap2.serialization.SoapObject;

/**
 * Classe abstraite de mapping d'objets SOAP en DTO
 *
 * @param <DTO> La classe du DTO
 */
public abstract class AbstractDefaultMapper<DTO extends KvmSerializable> implements DefaultMapper<DTO> {

	/**
	 * Obligation d'implémenter cette méthode afin d'effectuer le mapping correctement
	 *
	 * @param objectToMap L'objet à mapper
	 * @return L'objet mappé sous forme de DTO
	 */
	public abstract DTO map( SoapObject objectToMap );

	/**
	 * Methode de mapping d'une liste d'objets SOAP en liste de DTO.
	 * Chaque objet est mappé dans les implémentations de la méthode #map.
	 * Cette methode construit une liste en appelant la méthode map de l'implémentation correspondante au DTO
	 *
	 * @param listSoapObjects La liste des objets SOAP à mapper
	 * @return La liste des DTO mappés
	 */
	public final List<DTO> mapList( SoapObject listSoapObjects ) {
		List<DTO> listeDtos = null;

		if ( null != listSoapObjects && listSoapObjects.getPropertyCount() > 0 ) {
			listeDtos = new ArrayList<>();
			for ( int index = 0 ; index < listSoapObjects.getPropertyCount() ; index++ ) {
				listeDtos.add( map( (SoapObject) listSoapObjects.getProperty( index ) ) );
			}
		}

		return listeDtos;
	}
}