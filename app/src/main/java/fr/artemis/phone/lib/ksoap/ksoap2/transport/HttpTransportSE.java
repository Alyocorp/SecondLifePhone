/**
 * Copyright (c) 2003,2004, Stefan Haustein, Oberhausen, Rhld., Germany
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The  above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 * <p>
 * Contributor(s): John D. Beatty, Dave Dash, F. Hunter, Alexander Krebs,
 * Lars Mehrmann, Sean McDaniel, Thomas Strang, Renaud Tognelli
 */
package fr.artemis.phone.lib.ksoap.ksoap2.transport;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import fr.artemis.phone.utils.SessionPhone;

import fr.artemis.phone.lib.ksoap.ksoap2.SoapEnvelope;

/**
 * A J2SE based HttpTransport layer.
 */
public class HttpTransportSE extends Transport {

	/**
	 * Creates instance of HttpTransportSE with set url
	 *
	 * @param url the destination to POST SOAP data
	 */
	public HttpTransportSE( String url ) {
		super( url );
	}

	/**
	 * set the desired soapAction header field
	 *
	 * @param soapAction the desired soapAction
	 * @param envelope the envelope containing the information for the soap call.
	 */
	public void call( String soapAction, SoapEnvelope envelope ) throws IOException, XmlPullParserException {
		if ( soapAction == null ) {
			soapAction = "\"\"";
		}
		byte[] requestData = createRequestData( envelope );
		requestDump = debug ? new String( requestData ) : null;
		responseDump = null;
		ServiceConnection connection = getServiceConnection();
		connection.setRequestProperty( "User-Agent", "kSOAP/2.0" );
		connection.setRequestProperty( "SOAPAction", soapAction );
		connection.setRequestProperty( "Content-Type", "text/xml" );
		connection.setRequestProperty( "Connection", "close" );
		connection.setRequestProperty( "Content-Length", "" + requestData.length );
		connection.setRequestMethod( "POST" );
		connection.connect();
		OutputStream os = connection.openOutputStream();
		os.write( requestData, 0, requestData.length );
		os.flush();
		os.close();
		InputStream is;
		try {
			connection.connect();
			is = connection.openInputStream();
		} catch( IOException e ) {
			is = connection.getErrorStream();
			if ( is == null ) {
				connection.disconnect();
				throw ( e );
			}
		}
		if ( debug ) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[256];
			while ( true ) {
				int rd = is.read( buf, 0, 256 );
				if ( rd == -1 ) {
					break;
				}
				bos.write( buf, 0, rd );
			}
			bos.flush();
			buf = bos.toByteArray();
			responseDump = new String( buf );
			is.close();
			is = new ByteArrayInputStream( buf );
		}
		parseResponse( envelope, is );
	}

	/**
	 * set the desired soapAction header field
	 *
	 * @param soapAction the desired soapAction
	 * @param envelope the envelope containing the information for the soap call.
	 */
	public void callAuthenticated( String soapAction, SoapEnvelope envelope ) throws IOException, XmlPullParserException {
		if ( soapAction == null ) {
			soapAction = "\"\"";
		}
		byte[] requestData = createRequestData( envelope );
		requestDump = debug ? new String( requestData ) : null;
		responseDump = null;

		ServiceConnection connection = getServiceConnection();
		connection.setRequestProperty( "User-Agent", "kSOAP/2.0" );
		connection.setRequestProperty( "SOAPAction", soapAction );
		connection.setRequestProperty( "Content-Type", "text/xml;charset=utf-8" );
		connection.setRequestProperty( "Connection", "close" );
		connection.setRequestProperty( "Content-Length", "" + requestData.length );
		connection.setRequestProperty( "idSession", SessionPhone.getInstance().getUserDto().getIdSession() );
		connection.setRequestMethod( "POST" );
		connection.connect();
		OutputStream os = connection.openOutputStream();
		os.write( requestData, 0, requestData.length );
		os.flush();
		os.close();

		InputStream is;
		try {
			connection.connect();
			is = connection.openInputStream();
		} catch( IOException e ) {
			is = connection.getErrorStream();
			if ( is == null ) {
				connection.disconnect();
				throw ( e );
			}
		}
		if ( debug ) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[256];
			while ( true ) {
				int rd = is.read( buf, 0, 256 );
				if ( rd == -1 ) {
					break;
				}
				bos.write( buf, 0, rd );
			}
			bos.flush();
			buf = bos.toByteArray();
			responseDump = new String( buf );
			is.close();
			is = new ByteArrayInputStream( buf );
		}
		parseResponse( envelope, is );
		is.close();
	}

	/**
	 * set the desired soapAction header field
	 *
	 * @param soapAction the desired soapAction
	 * @param envelope the envelope containing the information for the soap call.
	 */
	public void callPropre( String soapAction, SoapEnvelope envelope, String content ) throws IOException, XmlPullParserException {
		if ( soapAction == null ) {
			soapAction = "\"\"";
		}
		byte[] requestData = content.getBytes();
		requestDump = debug ? new String( requestData ) : null;
		responseDump = null;
		ServiceConnection connection = getServiceConnection();
		connection.setRequestProperty( "User-Agent", "kSOAP/2.0" );
		connection.setRequestProperty( "SOAPAction", soapAction );
		connection.setRequestProperty( "Content-Type", "text/xml;charset=utf-8" );
		connection.setRequestProperty( "Connection", "close" );
		connection.setRequestProperty( "Content-Length", "" + requestData.length );
		connection.setRequestProperty( "idSession", SessionPhone.getInstance().getUserDto().getIdSession() );
		connection.setRequestMethod( "POST" );
		connection.connect();
		OutputStream os = connection.openOutputStream();
		os.write( requestData, 0, requestData.length );
		os.flush();
		os.close();
		InputStream is = null;
		try {
			connection.connect();
			is = connection.openInputStream();
		} catch( IOException e ) {
			is = connection.getErrorStream();
			if ( is == null ) {
				connection.disconnect();
				throw ( e );
			}
		}
		if ( debug ) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[256];
			while ( true ) {
				int rd = is.read( buf, 0, 256 );
				if ( rd == -1 ) {
					break;
				}
				bos.write( buf, 0, rd );
			}
			bos.flush();
			buf = bos.toByteArray();
			responseDump = new String( buf );
			is.close();
			is = new ByteArrayInputStream( buf );
		}
		parseResponse( envelope, is );
		is.close();
	}

	protected ServiceConnection getServiceConnection() throws IOException {
		return new ServiceConnectionSE( url );
	}
}