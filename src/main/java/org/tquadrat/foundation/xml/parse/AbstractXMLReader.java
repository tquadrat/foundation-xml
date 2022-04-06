/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
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

package org.tquadrat.foundation.xml.parse;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.CommonConstants.UTF8;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.LocatorImpl;

/**
 *  This class is an abstract base implementation for a XMLReader. Use this
 *  class as a base class for SAX based parsers that will parse other formats
 *  than XML. This is quite useful in combination with XSLT. Refer to the
 *  description for the abstract method
 *  {@link #process(BufferedReader) process()}
 *  for a brief run-through how to use this class.
 *
 *  @see "'Java and XSLT' from Eric M. Burke, O'Reilly 2001"
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: AbstractXMLReader.java 1030 2022-04-06 13:42:02Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: AbstractXMLReader.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public abstract class AbstractXMLReader implements XMLReader
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An empty attribute set.
     */
    public static final Attributes NO_ATTRIBUTES = new AttributesImpl();

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The content handler that provides the data to parse.
     */
    private ContentHandler m_ContentHandler;

    /**
     *  The DTD handler used by this XMLReader; usually {@code null}.
     */
    private DTDHandler m_DTDHandler;

    /**
     *  The encoding that is used for the input source. If {@code null},
     *  the platform specific encoding will be used.
     */
    private String m_Encoding = null;

    /**
     *  The entity resolver that is used by this XMLReader. It is especially
     *  used by
     *  {@link #parse(InputSource) parse( InputSource )}.
     */
    private EntityResolver m_EntityResolver;

    /**
     *  The error handler that is used by this XMlReader.
     */
    private ErrorHandler m_ErrorHandler;

    /**
     *  The features that are supported by this XMLReader.
     */
    private final Map<String,Boolean> m_Features = new HashMap<>();

    /**
     *  The locator that is maintained by this XML reader.
     */
    private final LocatorImpl m_Locator = new LocatorImpl();

    /**
     *  The properties that are set for this XMLReader.
     */
    private final Map<String,Object> m_Properties = new HashMap<>();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  The default constructor.
     */
    protected AbstractXMLReader()
    {
        setErrorHandler( null );
    }   //  AbstractXMLReader()

    /**
     *  Creates a new instance of AbstractXMLReader and assigns the given
     *  content handler.
     *
     *  @param  contentHandler  The content handler to use with this XMLReader.
     */
    protected AbstractXMLReader( final ContentHandler contentHandler )
    {
        this();

        setContentHandler( requireNonNullArgument( contentHandler, "contentHandler" ) );
    }   //  AbstractXMLReader()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Creates a buffered reader from the given input source.
     *
     *  @param  input   The input source.
     *  @return The buffered reader for the input data.
     *  @throws IOException Unable to create a reader from the input source.
     *  @throws SAXException    Unable to interpret the data provided with the
     *      input source.
     */
    private BufferedReader createReader( final InputSource input ) throws IOException, SAXException
    {
        //---* Create a buffered reader from the input source *----------------
        BufferedReader retValue = null;
        //noinspection IfStatementWithTooManyBranches
        if( nonNull( input.getCharacterStream() ) )
        {
            retValue = new BufferedReader( input.getCharacterStream() );
        }
        else if( nonNull( input.getByteStream() ) )
        {
            retValue = new BufferedReader( nonNull( m_Encoding ) ? new InputStreamReader( input.getByteStream(), m_Encoding ) : new InputStreamReader( input.getByteStream(), UTF8 ) );
        }
        else if( nonNull( input.getSystemId() ) )
        {
            final var entityResolver = getEntityResolver();
            if( nonNull( entityResolver ) )
            {
                //---* Use the entity resolver to get the reader *-------------
                retValue = createReader( entityResolver.resolveEntity( input.getPublicId(), input.getSystemId() ) );
            }
            else
            {
                /*
                 * If no entity resolver is set, the system id that is stored
                 * in an InputSource will be taken directly as a fully
                 * qualified URL to a stream somewhere. Otherwise, it will be
                 * translated using that entity resolver.
                 */
                final var url = new URL( input.getSystemId() );
                retValue = new BufferedReader( nonNull( m_Encoding ) ? new InputStreamReader( url.openStream(), m_Encoding ) : new InputStreamReader( url.openStream(), UTF8 ) );
            }
        }
        else
        {
            //---* Something is weird with this input source *-----------------
            throw new SAXException( "Invalid Input Source" );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createReader()

    /**
     *  Returns the current content handler.
     *
     *  @return A reference to the current content handler.
     */
    @Override
    public final ContentHandler getContentHandler() { return m_ContentHandler; }

    /**
     *  Returns the current DTD handler.
     *
     *  @return A reference to the current DTD handler.
     */
    @Override
    public final DTDHandler getDTDHandler() { return m_DTDHandler; }

    /**
     *  Returns the current entity resolver.
     *
     *  @return A reference to the current entity resolver.
     */
    @Override
    public final EntityResolver getEntityResolver() { return m_EntityResolver; }

    /**
     *  Returns the current error handler. It will never return
     *  {@code null}; in case no handler was set, a reference to an
     *  instance of
     *  {@link DefaultErrorHandler DefaultErrorHandler}
     *  will be returned.
     *
     *  @return A reference to the current error handler.
     */
    @Override
    public final ErrorHandler getErrorHandler() { return m_ErrorHandler; }

    /**
     *  {@inheritDoc}
     *  As this is not meant as a base for an <i>XML</i> parser, this
     *  implementation does not recognise the required namespaces. If this is
     *  needed, the derived class has to provide another implementation  for
     *  {@code getFeature()}
     *  and
     *  {@link #setFeature(String, boolean) setFeature()}.
     *
     *  @param  name    The name of the feature.
     *  @return {@code true} if the feature is supported,
     *      {@code false} if not or if the name is unknown.
     *  @throws SAXNotRecognizedException   The feature value cannot be
     *      retrieved.
     *  @throws SAXNotSupportedException    The XMLReader recognizes the
     *      feature name but cannot determine its value at this time.
     */
    @Override
    public boolean getFeature( final String name ) throws SAXNotRecognizedException, SAXNotSupportedException
    {
        final var feature = m_Features.get( requireNonNullArgument( name, "name" ) );
        final var retValue = nonNull( feature ) && feature.booleanValue();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getFeature()

    /**
     *  Returns the value for the property with the given name. Usually, this
     *  property name is any fully-qualified URI. It is possible for an
     *  XMLReader to recognise a property name but temporarily be unable to
     *  return its value. Some property values may be available only in
     *  specific contexts, such as before, during, or after a parse.<br>
     *  <br>XMLReaders are not required to recognise any specific property
     *  names, though an initial core set is documented for SAX2. But even that
     *  is not supported by this specific implementation; if this is a
     *  requirement, a derived class has to provide its own implementation of
     *  {@code getProperty()}
     *  and
     *  {@link #setProperty(String, Object) setProperty()}.
     *
     *  @param  name    The property name, which is a fully-qualified URI.
     *  @return The current value of the property. If the name is not known,
     *      {@code null} will be returned instead of throwing an
     *      exception.
     *
     *  @throws SAXNotRecognizedException   The property value can't be
     *      retrieved.
     *  @throws SAXNotSupportedException    The XMLReader recognizes the
     *      property name but cannot determine its value at this time.
     *
     *  @see #setProperty(String, Object) setProperty()
     */
    @Override
    public Object getProperty( final String name ) throws SAXNotRecognizedException, SAXNotSupportedException
    {
        return m_Properties.get( requireNonNullArgument( name, "name" ) );
    }   //  getProperty()

    /**
     *  Returns a reference to the locator object provided by this base
     *  implementation.
     *
     *  @return The reference to the locator object.
     */
    protected final Locator getLocator() { return m_Locator; }

    /**
     *  Parses an input data source.<br>
     *  <br>The application can use this method to instruct the XML reader to
     *  begin parsing a document from any valid input source (a character
     *  stream, a byte stream, or a URI).<br>
     *  <br>Applications may not invoke this method while a parse is in
     *  progress (they should create a new XMLReader instead for each nested
     *  document). Once a parse is complete, an application may reuse the same
     *  XMLReader object, possibly with a different input source. Configuration
     *  of the XMLReader object (such as handler bindings and values
     *  established for feature flags and properties) is unchanged by
     *  completion of a parse, unless the definition of that aspect of the
     *  configuration explicitly specifies other behavior (For example,
     *  feature flags or properties exposing characteristics of the document
     *  being parsed).<br>
     *  <br>During the parse, the XMLReader will provide information about the
     *  document through the registered event handlers.<br>
     *  <br>This method is synchronous: it will not return until parsing has
     *  ended. If a client application wants to terminate parsing early, it
     *  should throw an exception.<br>
     *  <br>This implementation calls
     *  {@link #process(BufferedReader) process()}
     *  which is the user provided implementation for the parser.<br>
     *  <br>If no content handler is set, this method returns immediately
     *  without any error message.
     *
     *  @param  input   The input source for the top-level of the document.
     *  @throws IOException An IO exception from the parser, possibly from a
     *      byte stream or character stream supplied by the application.
     *  @throws SAXException    Any SAX exception, possibly wrapping another
     *      exception.
     *
     *  @see org.xml.sax.InputSource
     *  @see #parse(java.lang.String) parse( String )
     *  @see #setEntityResolver(EntityResolver) setEntityResolver()
     *  @see #setDTDHandler(DTDHandler) setDTDHandler()
     *  @see #setContentHandler(ContentHandler) setContentHandler()
     *  @see #setErrorHandler(ErrorHandler) setErrorHandler()
     */
    @Override
    public final void parse( final InputSource input ) throws IOException, SAXException
    {
        final var contentHandler = getContentHandler();
        if( nonNull( contentHandler ) )
        {
            //---* Sets the locator *------------------------------------------
            /*
             * m_Locator will never be null.
             */
            contentHandler.setDocumentLocator( m_Locator );

            //---* Obtain a reader from the input source *---------------------
            try( final var reader = createReader( requireNonNullArgument( input, "input" ) ) )
            {
                //---* Let someone else do the work *--------------------------
                process( reader );
            }
        }
    }   //  parse()

    /**
     *  Parses an XML document from a system identifier (URI).<br>
     *  <br>This method is a shortcut for the common case of reading a
     *  document from a system identifier. It is the exact equivalent of the
     *  following:<br>
     *  {@code parse( new InputSource( systemId ) );}<br>
     *  <br>If the system identifier is a URL, it must be fully resolved by
     *  the application before it is passed to the parser.
     *
     *  @param  systemId    The system identifier (URI).
     *  @throws IOException An IO exception from the parser, possibly from a
     *      byte stream or character stream supplied by the application.
     *  @throws SAXException    Any SAX exception, possibly wrapping another
     *      exception.
     *
     *  @see #parse(org.xml.sax.InputSource) parse( InputSource )
     */
    @Override
    public final void parse( final String systemId ) throws IOException, SAXException { parse( new InputSource( systemId ) ); }

    /**
     *  This method has to be implemented in order to perform the parsing. It
     *  will be called either from
     *  {@link #parse(String) parse( String )}
     *  or
     *  {@link #parse(InputSource) parse( InputSource )}.<br>
     *  <br>The implementation of this method should update the locator by
     *  appropriate calls to
     *  {@link #setPublicId(String) setPublicId()},
     *  {@link #setSystemId(String) setSystemId()},
     *  and
     *  {@link #setLocation(int, int) setLocation()} -
     *  if possible ...<br>
     *  <br>The input is not meant to be XML, so it is difficult to describe
     *  here how to parse the input. But assuming that the input stream is a
     *  Java properties file, the implementation for {@code process()} might
     *  look like this:
     *  <pre><code>
     *  protected void process( BufferedReader input ) throws IOException, SAXException
     *  {
     *      ContentHandler handler = getContentHandler();
     *
     *      //---* Load the properties *---------------------------------------
     *      ExtendedProperties properties = new ExtendedProperties();
     *      properties.load( input );
     *
     *      //---* Create the document *---------------------------------------
     *      handler.startDocument();
     *      handler.startElement( null, null, "properties", new AttributesImpl() );
     *
     *      //---* Process the properties *------------------------------------
     *      AttributesImpl attributes;
     *      char [] value;
     *      // Each property will be treated as a value with the key as its
     *      // attribute.
     *      for( String name : properties.stringPropertyNames() )
     *      {
     *          //---* Start the element *-------------------------------------
     *          attributes = new AttributesImpl();
     *          attributes.addAttribute( null, null, "name", "ID", name );
     *          handler.startElement( null, null, "property", attributes );
     *
     *          //---* The element contents *----------------------------------
     *          value = properties.getProperty( name ).toCharArray();
     *          handler.characters( value, 0, value.length );
     *
     *          //---* End the element *---------------------------------------
     *          handler.endElement( null, null, "property" );
     *      }
     *
     *      //---* Finish the document *---------------------------------------
     *      handler.endElement( null, null, "properties" );
     *      handler.endDocument();
     *  }   //  process()
     *  </code></pre>
     *
     *  @param  input   The input stream.
     *  @throws IOException Problems reading the input stream.
     *  @throws SAXException    Something has gone wrong.
     */
    protected abstract void process( BufferedReader input ) throws IOException, SAXException;

    /**
     *  Sets the content handler used by this XMLReader. It allows an
     *  application to register a content event handler.<br>
     *  <br>If the application does not register a content handler, all
     *  content events reported by the SAX parser will be silently
     *  ignored.<br>
     *  <br>Applications may register a new or different handler in the middle
     *  of a parse, and the SAX parser must begin using the new handler
     *  immediately.
     *
     *  @param  handler  The content handler; may be {@code null}.
     */
    @Override
    public final void setContentHandler( final ContentHandler handler )  { m_ContentHandler = handler; }

    /**
     *  Sets the DTD handler used by this XMLReader. Allows an application to
     *  register a DTD event handler.<br>
     *  <br>If the application does not register a DTD handler, all DTD events
     *  reported by the SAX parser will be silently ignored.<br>
     *  <br>Applications may register a new or different handler in the middle
     *  of a parse, and the SAX parser must begin using the new handler
     *  immediately.
     *
     *  @param  handler The DTD handler; may be {@code null}.
     */
    @Override
    public final void setDTDHandler( final DTDHandler handler ) { m_DTDHandler = handler; }

    /**
     *  Sets the encoding for the input source. {@code null} means that
     *  the platform specific encoding is used.
     *
     *  @param  encoding    The encoding to use; may be {@code null}.
     */
    public final void setEncoding( final String encoding ) { m_Encoding = encoding; }

    /**
     *  Sets the entity resolver that is used by this XMLReader. Allows an
     *  application to register an entity resolver.<br>
     *  <br>If the application does not register an entity resolver, the
     *  XMLReader will perform its own default resolution.<br>
     *  <br>Applications may register a new or different resolver in the
     *  middle of a parse, and the SAX parser must begin using the new resolver
     *  immediately.
     *
     *  @param  resolver    The entity resolver; may be {@code null}.
     */
    @Override
    public final void setEntityResolver( final EntityResolver resolver ) { m_EntityResolver = resolver; }

    /**
     *  Sets the error handler that is used by this XMLReader. Allows an
     *  application to register an error event handler.<br>
     *  <br>If the application does not register an error handler, all error
     *  events reported by the SAX parser will be written to
     *  {@link System#err System.err}
     *  and otherwise silently ignored; however, normal processing may not
     *  continue. It is highly recommended that all SAX applications implement
     *  an error handler to avoid unexpected bugs.<br>
     *  <br>Applications may register a new or different handler in the middle
     *  of a parse, and the SAX parser must begin using the new handler
     *  immediately.
     *
     *  @param  handler The error handler; may be {@code null}.
     *
     *  @see DefaultErrorHandler
     */
    @Override
    public final void setErrorHandler( final ErrorHandler handler )
    {
        m_ErrorHandler = nonNull( handler ) ? handler : DefaultErrorHandler.INSTANCE;
    }   //  setErrorHandler()

    /**
     *  Sets the feature flag.
     *
     *  @param  name    The name of the feature.
     *  @param  value   {@code true} if the feature should be supported
     *      by this implementation, {@code false} if not.
     *  @throws SAXNotRecognizedException   The feature value cannot be
     *      assigned.
     *  @throws SAXNotSupportedException    The XMLReader recognises the
     *      feature name but cannot set the requested value.
     *
     *  @see #getFeature(String) getFeature()
     */
    @Override
    public void setFeature( final String name, final boolean value ) throws SAXNotRecognizedException, SAXNotSupportedException
    {
        m_Features.put( requireNonNullArgument( name, "name" ), Boolean.valueOf( value ) );
    }   //  setFeature()

    /**
     *  Sets the current location to the locator.
     *
     *  @param  lineNumber   The current line number.
     *  @param  columnNumber    The current column number.
     */
    protected final void setLocation( final int lineNumber, final int columnNumber )
    {
        m_Locator.setLineNumber( lineNumber );
        m_Locator.setColumnNumber( columnNumber );
    }   //  setLocation()

    /**
     *  Sets the value of a property. Usually, the property name is any
     *  fully-qualified URI. It is possible for an XMLReader to recognize a
     *  property name but to be unable to change the current value. Some
     *  property values may be immutable or mutable only in specific contexts,
     *  such as before, during, or after a parse.<br>
     *  <br>XMLReaders are not required to recognize setting any specific
     *  property names, though a core set is defined by SAX2. But only this is
     *  not implemented by this implementation.<br>
     *  <br>This method is also the standard mechanism for setting extended
     *  handlers.
     *
     *  @param name The property name, which is a fully-qualified URI.
     *  @param value    The requested value for the property.
     *  @throws SAXNotRecognizedException   The property value can't be
     *      assigned or retrieved.
     *  @throws SAXNotSupportedException    The XMLReader recognises the
     *      property name but cannot set the requested value.
     *
     *  @see #getProperty(String) getProperty()
     */
    @Override
    public void setProperty( final String name, final Object value ) throws SAXNotRecognizedException, SAXNotSupportedException
    {
        requireNonNullArgument( name, "name" );
        if( nonNull( value ) )
        {
            m_Properties.put( name, value );
        }
        else
        {
            m_Properties.remove( name );
        }
    }   //  setProperty()

    /**
     *  Sets the public id to the locator.
     *
     *  @param  publicId    The value for the public id; may be {@code null}.
     */
    protected final void setPublicId( final String publicId ) { m_Locator.setPublicId( publicId ); }

    /**
     *  Sets the system id to the locator.
     *
     *  @param  systemId    The value for the system id; may be {@code null}.
     */
    protected final void setSystemId( final String systemId ) { m_Locator.setSystemId( systemId ); }
}
//  class AbstractXMLReader

/*
 *  End of File
 */