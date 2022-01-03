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

package org.tquadrat.foundation.xml.parse.spi;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.isNotEmptyOrBlank;

import javax.xml.stream.Location;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.parse.LocationLocator;
import org.tquadrat.foundation.xml.parse.XMLParseEventHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *  <p>{@summary The abstract base class for StAX based XML parsers.}</p>
 *  <p>An implementation of this class will parse an XML stream to an object
 *  of type {@code T} that is either provided with the constructor
 *  {@link #StAXParserBase(Object)}
 *  or will be created by an instance of
 *  {@link XMLParseEventHandler}.</p>
 *  <p>The parse event handler can be provided either programmatically, as
 *  shown in
 *  {@link org.tquadrat.foundation.xml.parse.StAXParser},
 *  or as methods in an implementation of this class.</p>
 *
 *  @param  <T> The type of the target data structure.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: StAXParserBase.java 840 2021-01-10 21:37:03Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "AbstractClassWithoutAbstractMethods" )
@ClassVersion( sourceVersion = "$Id: StAXParserBase.java 840 2021-01-10 21:37:03Z tquadrat $" )
@API( status = EXPERIMENTAL, since = "0.0.5" )
public abstract class StAXParserBase<T>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An empty array of {@code StAXParserBase} objects.
     */
    @SuppressWarnings( "rawtypes" )
    public static final StAXParserBase [] EMPTY_StAXParserBase_ARRAY = new StAXParserBase [0];

    /**
     *  The message for missing handlers: {@value}.
     */
    public static final String MSG_NoHandler = "No handler was registered for element '%s'";

    /**
     *  The message for an unexpected tag: {@value}.
     */
    public static final String MSG_UnexpectedTag = "The element tag '%s' is unexpected here";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The document element tag.
     */
    @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
    private Optional<String> m_DocumentTag = Optional.empty();

    /**
     *  The event handlers.
     */
    private final Map<String,XMLParseEventHandler<?>> m_Handlers = new HashMap<>();

    /**
     *  The target data structure.
     */
    private T m_Target;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code StAXParserBase} instance.
     */
    protected StAXParserBase()
    {
        m_Target = null;
    }   //  StAXParserBase()

    /**
     *  Creates a new {@code StAXParser} instance.
     *
     *  @param  target  The target data structure.
     */
    protected StAXParserBase( final T target )
    {
        setTarget( target );
    }   //  StAXParserBase()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Processes the given
     *  {@linkplain XMLEventReader event reader}.
     *
     *  @param  eventReader The XML stream.
     *  @return The target data structure.
     *  @throws SAXException    Something went wrong.
     */
    @SuppressWarnings( "unchecked" )
    @API( status = EXPERIMENTAL, since = "0.0.7" )
    protected T parse( final XMLEventReader eventReader ) throws SAXException
    {
        final var documentTag = m_DocumentTag.orElseThrow( () -> new SAXException( "Undefined document", new IllegalStateException( "No document tag provided" ) ) );
        try
        {
            ScanLoop: while( eventReader.hasNext() )
            {
                String elementName = null;
                var xmlEvent = eventReader.nextEvent();
                if( xmlEvent.isStartDocument() )
                {
                    elementName = documentTag;
                    xmlEvent = eventReader.nextTag();
                }
                else if( xmlEvent.isEndDocument() )
                {
                    elementName = documentTag;
                    if( eventReader.hasNext() ) xmlEvent = eventReader.nextTag();
                }
                else continue ScanLoop;

                final var handler = isNotEmptyOrBlank( elementName ) ? (XMLParseEventHandler<T>) retrieveHandler( elementName ) : null;
                setTarget( handler.process( eventReader, xmlEvent, m_Target, this::retrieveHandler ) );
            }   //  ScanLoop:
        }
        catch( final XMLStreamException e )
        {
            final var message = "XML parse failed";
            if( nonNull( e.getLocation() ) )
            {
                throw new SAXParseException( message, new LocationLocator( e.getLocation() ), e );
            }
            throw new SAXException( message, e );
        }

        final var retValue = m_Target;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  parse()

    /**
     *  Registers an element handler.
     *
     *  @param  elementName The element name.
     *  @param  isDocument  {@code true} if the element name is the document
     *      name.
     *  @param  handler The parse event handler.
     */
    protected final void registerElementHandler( final String elementName, final boolean isDocument, final XMLParseEventHandler<?> handler )
    {
        if( isDocument && m_DocumentTag.isPresent() ) throw new IllegalStateException( format( "Document Tag was already set: %s", m_DocumentTag.get() ) );
        m_Handlers.put( requireNotEmptyArgument( elementName, "elementName" ), requireNonNullArgument( handler, "handler" ) );
        if( isDocument ) m_DocumentTag = Optional.of( elementName );
    }   //  registerElementHandler()

    /**
     *  Throws an
     *  {@link XMLStreamException}
     *  that indicates an unexpected tag at the given location.
     *
     *  @param  elementName The encountered tag.
     *  @param  location    The location.
     *  @throws XMLStreamException  Always.
     */
    @API( status = EXPERIMENTAL, since = "0.0.7" )
    protected static final void reportUnexpectedTag( final String elementName, final Location location ) throws XMLStreamException
    {
        throw new XMLStreamException( format( MSG_UnexpectedTag, requireNotEmptyArgument( elementName, "elementName" ) ), requireNonNullArgument( location, "location" ) );
    }   //  reportUnexpectedTag()

    /**
     *  Retrieves the XML parse event handler for the given element name.
     *
     *  @param  elementName The name of the element to handle.
     *  @return The requested instance of
     *      {@link XMLParseEventHandler}.
     *  @throws XMLStreamException  There is no registered handler for the
     *      given element name.
     */
    private final XMLParseEventHandler<?> retrieveHandler( final String elementName ) throws XMLStreamException
    {
        final var retValue = m_Handlers.get( requireNotEmptyArgument( elementName, "elementName" ) );
        if( isNull( retValue ) )
        {
            throw new XMLStreamException( format( MSG_NoHandler, elementName ) );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveHandler()

    /**
     *  Sets the target data structure.
     *
     *  @param  target  The target data structure.
     */
    private final void setTarget( final T target )
    {
        m_Target = requireNonNullArgument( target, "target" );
    }   //  setTarget()
}
//  class StAXParserBase

/*
 *  End of File
 */