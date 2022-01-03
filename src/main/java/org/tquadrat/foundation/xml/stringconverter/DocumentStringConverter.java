/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 *
 * Licensed to the public under the agreements of the GNU Lesser General Public
 * License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.tquadrat.foundation.xml.stringconverter;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.CommonConstants.UTF8;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.isNotEmptyOrBlank;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serial;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.UnexpectedExceptionError;
import org.tquadrat.foundation.lang.StringConverter;
import org.tquadrat.foundation.xml.parse.NullErrorHandler;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *  <p>{@summary An implementation of
 *  {@link StringConverter}
 *  for
 *  {@link Document}
 *  values.}</p>
 *  <p>The method
 *  {@link #fromString(CharSequence)}
 *  parses the input to a document, using the default
 *  {@link DocumentBuilderFactory}
 *  to get a
 *  {@link javax.xml.parsers.DocumentBuilder}.</p>
 *  <p>{@link #toString(Document)}
 *  uses an instance of
 *  {@link javax.xml.transform.Transformer}
 *  to get a String from the document.</p>
 *
 *  @note When converting a String to an instance of {@code Document} back to a
 *      String, the final String may be different from the input because the
 *      formatting can differ, and some implicit defaults will now provided as
 *      explicit setting. Only the semantics are guaranteed to be still the
 *      same.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: DocumentStringConverter.java 897 2021-04-06 21:34:01Z tquadrat $
 *  @since 0.1.0
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: DocumentStringConverter.java 897 2021-04-06 21:34:01Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
@SuppressWarnings( "exports" )
public final class DocumentStringConverter implements StringConverter<Document>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The error message for an invalid XML contents: {@value}.
     */
    public static final String MSG_InvalidXML = "Invalid XML: %1$s";

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     *  An instance of this class.
     */
    public static final DocumentStringConverter INSTANCE = new DocumentStringConverter();

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final Document fromString( final CharSequence source ) throws IllegalArgumentException
    {
        Document retValue = null;
        if( nonNull( source ) )
        {
            try
            {
                final var documentBuilderFactory = DocumentBuilderFactory.newInstance();
                documentBuilderFactory.setNamespaceAware( true );
                documentBuilderFactory.setValidating( false );
                documentBuilderFactory.setIgnoringComments( false );
                documentBuilderFactory.setIgnoringElementContentWhitespace( true );
                documentBuilderFactory.setCoalescing( false );
                documentBuilderFactory.setExpandEntityReferences( false );
                final var builder = documentBuilderFactory.newDocumentBuilder();
                builder.setErrorHandler( NullErrorHandler.INSTANCE );
                retValue = builder.parse( new InputSource( new StringReader( source.toString() ) ) );
                retValue.normalizeDocument();
            }
            catch( final ParserConfigurationException e )
            {
                throw new UnexpectedExceptionError( "Cannot instantiate DocumentBuilder", e );
            }
            catch( final SAXException e )
            {
                throw new IllegalArgumentException( format( MSG_InvalidXML, source ), e );
            }
            catch( final IOException e )
            {
                throw new UnexpectedExceptionError( e );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  fromString()

    /**
     *  Provides the subject class for this converter.
     *
     * @return The subject class.
     */
    @SuppressWarnings( "PublicMethodNotExposedInInterface" )
    public final Collection<Class<? extends Document>> getSubjectClass() { return List.of( Document.class ); }

    /**
     *  This method is used by the
     *  {@link java.util.ServiceLoader}
     *  to obtain the instance for this
     *  {@link org.tquadrat.foundation.lang.StringConverter}
     *  implementation.
     *
     *  @return The instance for this {@code StringConverter} implementation.
     */
    public static final DocumentStringConverter provider() { return INSTANCE; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final Document source )
    {
        String retValue = null;
        if( nonNull( source ) )
        {
            final var encoding = source.getXmlEncoding();
            try
            {
                //---* Obtain the transformer *--------------------------------
                final var transformerFactory = TransformerFactory.newInstance();
                final var transformer = transformerFactory.newTransformer();

                //---* Configure the transformer *-----------------------------
                transformer.setOutputProperty( "method", "xml" );
                transformer.setOutputProperty( "indent", "no" );
                transformer.setOutputProperty( "standalone", source.getXmlStandalone() ? "yes" : "no" );
                if( nonNull( encoding ) ) transformer.setOutputProperty( "encoding", encoding );
                final var doctype = source.getDoctype();
                if( nonNull( doctype) )
                {
                    final var systemId = doctype.getSystemId();
                    if( isNotEmptyOrBlank( systemId ) ) transformer.setOutputProperty( "doctype-system", systemId );
                    final var publicId = doctype.getPublicId();
                    if( isNotEmptyOrBlank( publicId ) ) transformer.setOutputProperty( "doctype-public", publicId );
                }

                final var documentSource = new DOMSource( source );
                final var outputStream = new ByteArrayOutputStream();
                final var result = new StreamResult( outputStream );
                transformer.transform( documentSource, result );
                retValue = outputStream.toString( isNull( encoding ) ? UTF8.name() : encoding );
            }
            catch( final TransformerConfigurationException e )
            {
                throw new UnexpectedExceptionError( "Cannot instantiate Transformer", e );
            }
            catch( final TransformerException e )
            {
                throw new IllegalArgumentException( "Unrecoverable error on transformation", e );
            }
            catch( final UnsupportedEncodingException e )
            {
                throw new UnexpectedExceptionError( format( "Invalid encoding: %s", encoding ), e );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class DocumentStringConverter

/*
 *  End of File
 */