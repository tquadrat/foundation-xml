/*
 * ============================================================================
 * Copyright © 2002-2023 by Thomas Thrien.
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

import static java.lang.String.format;
import static org.apiguardian.api.API.Status.MAINTAINED;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.StringUtils.isEmptyOrBlank;
import static org.tquadrat.foundation.util.StringUtils.isNotEmptyOrBlank;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.IntStream;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.MountPoint;
import org.tquadrat.foundation.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.LocatorImpl;

/**
 *  <p>{@summary This class implements the interface
 *  {@link ContentHandler ContentHandler}
 *  as a base class for more advanced versions of the
 *  {@link DefaultHandler DefaultHandler class}
 *  or for stand-alone use.}</p>
 *  <p>Instead of implementing the three methods
 *  {@link org.xml.sax.helpers.DefaultHandler#characters(char[],int,int) characters()},
 *  {@link org.xml.sax.helpers.DefaultHandler#endElement(String,String,String) endElement()},
 *  and
 *  {@link org.xml.sax.helpers.DefaultHandler#startElement(String,String,String,Attributes) startElement()}
 *  only handlers for the elements have to implemented; after registration of
 *  these handlers using
 *  {@link #registerElementHandler(String, HandlerMethod)}
 *  these handler methods will be called automatically by the default
 *  implementations of
 *  {@link #processElement(Element) processElement()}
 *  and
 *  {@link #openElement(Element) openElement()}.</p>
 *  <p>These method can still be overwritten if a different processing is
 *  desired. When
 *  {@link #processElement(Element) processElement()}
 *  is called after the element is terminated, the attributes together with the
 *  character data after closing the element is provided. The method
 *  {@link #openElement(Element) openElement()}
 *  is called each time an element will be opened, providing the attributes
 *  only.</p>
 *  <p>Some convenience methods have been implemented that will give access
 *  to the parent element and to the path down to the current element.</p>
 *
 *  <p><b>Note</b>: Unfortunately, this class do not work for XML streams
 *  that has elements embedded into text, as it is usual for HTML. The
 *  snippet</p>
 *  <pre><code>&lt;p&gt;First Text &lt;b&gt;Bold Text&lt;/b&gt; Second Text&lt;/p&gt;</code></pre>
 *  <p>will be parsed as &quot;First Text Second Text&quot; for the {@code p}
 *  element and &quot;Bold Text&quot; for the {@code b} element; the
 *  information that the b element was embedded in between is lost.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: AdvancedContentHandler.java 1086 2024-01-05 23:18:33Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "AbstractClassWithoutAbstractMethods" )
@ClassVersion( sourceVersion = "$Id: AdvancedContentHandler.java 1086 2024-01-05 23:18:33Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public abstract class AdvancedContentHandler implements ContentHandler
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  This class serves a container for the name, the data and the attributes
     *  of an XML element.
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: AdvancedContentHandler.java 1086 2024-01-05 23:18:33Z tquadrat $
     *  @since 0.0.5
     *
     *  @UMLGraph.link
     */
    @SuppressWarnings( {"InnerClassMayBeStatic", "ProtectedInnerClass"} )
    @ClassVersion( sourceVersion = "$Id: AdvancedContentHandler.java 1086 2024-01-05 23:18:33Z tquadrat $" )
    @API( status = STABLE, since = "0.1.0" )
    protected static final class Element
    {
            /*------------*\
        ====** Attributes **===================================================
            \*------------*/
        /**
         *  The attributes.
         */
        private final Map<String,Attribute> m_Attributes;

        /**
         *  The data.
         */
        @SuppressWarnings( "StringBufferField" )
        private final StringBuilder m_Data;

        /**
         *  The element's local name.
         */
        private final String m_LocalName;

        /**
         *  The parent for this element.
         */
        @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
        private final Optional<Element> m_Parent;

        /**
         *  The path to the element.
         */
        private final String m_Path;

        /**
         *  The element's qualified name.
         */
        private final String m_QName;

        /**
         *  The namespace for this element; if {@code null}, the
         *  {@linkplain #m_QName qualified name}
         *  and the
         *  {@linkplain #m_LocalName local name}
         *  are the same.
         */
        private final URI m_URI;

            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Create a new object of this class from an element's name and its
         *  attributes.
         *
         *  @param  qName   The element's qualified name.
         *  @param  localName   The element's local name.
         *  @param  uri The namespace for the element; can be {@code null}.
         *  @param  attributes  The element's attributes.
         *  @param  path    The path to the element; this is a string, compiled
         *      from the element's name, separated by slashes ("/").
         *  @param  parent  The parent element for this element; may be
         *      {@code null}.
         */
        @SuppressWarnings( "ConstructorWithTooManyParameters" )
        Element( final String qName, final String localName, final URI uri, final Map<String,Attribute>  attributes, final String path, @SuppressWarnings( "UseOfConcreteClass" ) final Element parent )
        {
            m_QName = requireNotEmptyArgument( qName, "qName" );
            m_LocalName = requireNotEmptyArgument( localName, "localName" );
            m_URI = uri;
            m_Attributes = Map.copyOf( attributes );
            m_Path = path;
            m_Data = new StringBuilder();
            m_Parent = Optional.ofNullable( parent );
        }   //  Element

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  Adds another data chunk to the data block for the current element.
         *
         *  @param  characters  The characters.
         *  @param  start   The start position inside the characters array.
         *  @param  end The ending position inside the array.
         */
        public final void appendData( final char [] characters, final int start, final int end )
        {
            m_Data.append( characters, start, end );
        }   //  appendData()

        /**
         *  Returns the attributes of the element.
         *
         *  @return The attributes.
         */
        public final Map<String,Attribute> getAttributes() { return m_Attributes; }

        /**
         *  Returns the data block for this element.
         *
         *  @return The data block.
         */
        public final String getData() { return m_Data.toString().trim(); }

        /**
         *  Returns the local name of the element.
         *
         *  @return The local name of the element.
         */
        public final String getLocalName() { return m_LocalName; }

        /**
         *  Returns the parent element.
         *
         *  @return An instance of
         *      {@link Optional}
         *      that holds the parent element; will be
         *      {@linkplain Optional#empty() empty}
         *      if this element does not have a parent.
         */
        public final Optional<Element> getParent() { return m_Parent; }

        /**
         *  Returns to XML path to this element.
         *
         *  @return The element's path.
         */
        public final String getPath() { return m_Path; }

        /**
         *  Returns the prefix from the element's qualified name.
         *
         *  @return The prefix; if there is no prefix, the empty String will be
         *      returned.
         */
        public final String getPrefix()
        {
            final var pos = m_QName.indexOf( ":" );
            final var retValue = pos > 0 ? m_QName.substring( 0, pos ) : EMPTY_STRING;

            //---* Done *------------------------------------------------------
            return retValue;
        }   //  getPrefix()

        /**
         *  Returns the qualified name of the element.
         *
         *  @return The qualified name of the element.
         */
        public final String getQName() { return m_QName; }

        /**
         *  Returns the namespace URI of the element.
         *
         *  @return An instance of
         *      {@link Optional}
         *      that holds the namespace URI of the element.
         */
        public final Optional<URI> getURI() { return Optional.ofNullable( m_URI ); }
    }
    //  class Element

    /**
     *  The functional interface describing a method that processes an XML
     *  element.
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: AdvancedContentHandler.java 1086 2024-01-05 23:18:33Z tquadrat $
     *  @since 0.1.0
     *
     *  @UMLGraph.link
     */
    @SuppressWarnings( {"ProtectedInnerClass"} )
    @FunctionalInterface
    @ClassVersion( sourceVersion = "$Id: AdvancedContentHandler.java 1086 2024-01-05 23:18:33Z tquadrat $" )
    @API( status = MAINTAINED, since = "0.1.0" )
    protected interface HandlerMethod
    {
            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  <p>{@summary Processes an XML element.}</p>
         *  <p>As each element should have its own handler, the tag is not
         *  provided as an argument. If necessary, the tag can be derived from
         *  the {@code path} argument.</p>
         *
         *  @param  terminateElement {@code true} if called by
         *      {@link #processElement(Element)},
         *      indicating that the element processing will be terminated,
         *      {@code false} when called by
         *      {@link #openElement(Element)}.
         *  @param  data    The element data; will be {@code null} if called
         *      by
         *      {@link #openElement(Element)}.
         *  @param  attributes  The element attributes.
         *  @param  path    The element path.
         *  @throws SAXException    The element cannot be handled properly.
         *
         *  @since  0.1.0
         */
        public void process( final boolean terminateElement, final String data, final Map<String,Attribute> attributes, final String path ) throws SAXException;
    }
    //  interface HandlerMethod

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An empty array of Element objects.
     */
    private static final Element [] EMPTY_Element_ARRAY = new Element [0];

    /**
     *  The message indicating an invalid URI: {@value}.
     */
    public static final String MSG_InvalidURI = "Invalid namespace URI: %s";

    /**
     *  The message indicating that there is no element handler for the given
     *  element: {@value}.
     */
    public static final String MSG_NoHandler = "No handler for element '%1$s'";

    /**
     *  The message indicating that there is no element on the stack: {@value}.
     */
    public static final String MSG_NoElementOnStack = "No element on stack";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The document type.
     */
    private String m_DocumentType = null;

    /**
     *  This stack contains the open elements, stored as instances of
     *  {@link Element}.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    private final Stack<Element> m_ElementStack = new Stack<>();

    /**
     *  The element handler methods. The key for this map is the qualified
     *  name of the element.
     */
    private final Map<String,HandlerMethod> m_HandlerMethods = new TreeMap<>();

    /**
     *  The locator.
     */
    private Locator m_Locator;

    /**
     *  The name spaces. The prefix is the key to the map, while the URI is the
     *  value.
     */
    private final Map<String,URI> m_Namespaces = new HashMap<>();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  The default constructor.
     */
    protected AdvancedContentHandler() { /* Does nothing! */ }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Receives notification of character data inside an element.
     *
     *  @param  ch  The characters.
     *  @param  start   The start position inside the characters array.
     *  @param  length  The length of the subset to process.
     *  @throws SAXException    Something has gone wrong.
     */
    @Override
    public final void characters( final char [] ch, final int start, final int length ) throws SAXException
    {
        final var element = m_ElementStack
            .peek()
            .orElseThrow( () -> new SAXParseException( MSG_NoElementOnStack, getLocator() ) );
        element.appendData( ch, start, length );
    }   //  characters()

    /**
     *  Composes an
     *  {@link Attribute}
     *  instance from the data of the given
     *  {@link Attributes}
     *  instance at the given index.
     *
     *  @param  attributes  The attributes.
     *  @param  index   The index.
     *  @return The attribute.
     *  @throws URISyntaxException  The URI for the attribute's namespace
     *      cannot be parsed correctly.
     *  @throws IllegalArgumentException    The attribute type is invalid.
     */
    private static final Attribute composeAttribute( final Attributes attributes, final int index ) throws IllegalArgumentException, URISyntaxException
    {
        final var qName = attributes.getQName( index );
        final var attributesLocalName = attributes.getLocalName( index );
        final Optional<String> localName = isEmptyOrBlank( attributesLocalName ) ? Optional.empty() : Optional.of( attributesLocalName );
        final var attributesURI = attributes.getURI( index );
        final Optional<URI> uri = isEmptyOrBlank( attributesURI ) ? Optional.empty() : Optional.of( new URI( attributesURI ) );
        final var type = Attribute.Type.valueOf( attributes.getType( index ) );
        final var value = attributes.getValue( index );

        final var retValue = new Attribute( qName, localName, uri, type, value, index );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  composeAttribute()

    /**
     *  Receives the notification about the end of the document.<br>
     *  <br>This implementation does nothing by default. Application writers
     *  may override this method in a subclass to take specific actions at the
     *  end of a document (such as finalising a tree or closing an output
     *  file).
     *
     *  @throws SAXException    Any SAX exception, possibly wrapping another
     *      exception.
     */
    @SuppressWarnings( "NoopMethodInAbstractClass" )
    @Override
    @MountPoint
    public void endDocument() throws SAXException { /* Does nothing! */ }

    /**
     *  {@summary Receives the notification about the end of an element.} This
     *  method will call the
     *  {@link #processElement(Element) processElement()}
     *  method and afterwards it will remove the element from the stack - in
     *  exactly that order, otherwise the
     *  {@link #getPath() getPath()}
     *  method would return wrong results.
     *
     *  @param  uri The URI for the namespace of this element; can be empty.
     *  @param  localName   The local name of the element.
     *  @param  qName   The element's qualified name.
     *  @throws SAXException    The element was not correct according to the
     *      DTD.
     */
    @Override
    public final void endElement( final String uri, final String localName, final String qName ) throws SAXException
    {
        final var element = m_ElementStack.peek().orElseThrow( () -> new SAXParseException( "No element '%1$s' on Stack".formatted( qName ), getLocator() ) );
        if( !element.getQName().equals( qName ) )
        {
            throw new SAXParseException( "Closing element '%1$s' does not match open element '%2$s'".formatted( qName, element.getQName() ), getLocator() );
        }
        processElement( element );

        //---* Remove element from stack *-------------------------------------
        m_ElementStack.pop();
    }   //  endElement()

    /**
     *  Receives the notification of the end for a name space mapping.
     *
     *  @param  prefix  The Namespace prefix being declared.
     *  @throws SAXException    Any SAX exception, possibly wrapping another
     *      exception.
     */
    @Override
    public final void endPrefixMapping( final String prefix ) throws SAXException
    {
        //---* Delete the namespace *------------------------------------------
        m_Namespaces.remove( requireNonNullArgument( prefix, "prefix" ) );
    }   //  endPrefixMapping()

    /**
     *  Returns the name of the document type.
     *
     *  @return The document type.
     */
    public final String getDocumentType() { return m_DocumentType; }

    /**
     *  Returns a copy of the locator.
     *
     *  @return A copy of the locator object or {@code null} if there was
     *      none provided by the parser.
     */
    protected final Locator getLocator() { return nonNull( m_Locator ) ? new LocatorImpl( m_Locator ) : null; }

    /**
     *  Returns the path for the element as an array, with the qualified
     *  element names as the entries in the array. The array is ordered in the
     *  way that the current element is at position {@code [0]}, while the root
     *  element (the document element) is at {@code [length - 1]}.
     *
     *  @return The list of element names that build the path to the current
     *      element.
     */
    protected final String [] getPath()
    {
        final var elements = m_ElementStack.toArray( EMPTY_Element_ARRAY );
        final var retValue = IntStream.range( 0, m_ElementStack.size() )
            .mapToObj( i -> elements [i].getQName() )
            .toArray( String[]::new );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getPath()

    /**
     *  Returns the path depth for the element.
     *
     *  @return The number of nodes on the path to the current element. 0 means
     *      that the current element is the document.
     */
    protected final int getPathDepth() { return m_ElementStack.size() - 1; }

    /**
     *  The default element handling; it does nothing.
     *
     *  @param  element The element.
     *  @param  terminateElement {@code true} if called by
     *      {@link #processElement(Element)},
     *      indicating that the element processing will be terminated,
     *      {@code false} when called by
     *      {@link #openElement(Element)}.
     *  @throws SAXException    The element cannot be handled properly.
     *
     *  @since 0.1.0
     */
    @SuppressWarnings( {"unused", "NoopMethodInAbstractClass"} )
    @MountPoint
    @API( status = MAINTAINED, since = "0.1.0" )
    protected void handleElement( @SuppressWarnings( "UseOfConcreteClass" ) final Element element, final boolean terminateElement ) throws SAXException
    {
        //---* Does nothing *--------------------------------------------------
    }   //  handleElement()

    /**
     *  Receives the notification of ignorable whitespace in element
     *  content.<br>
     *  <br>This implementation does nothing by default. Application writers
     *  may override this method to take specific actions for each chunk of
     *  ignorable whitespace (such as adding data to a node or buffer, or
     *  printing it to a file).
     *
     *  @param  ch  The whitespace characters.
     *  @param  start   The start position in the character array.
     *  @param  length  The number of characters to use from the character
     *      array.
     *  @throws SAXException    Any SAX exception, possibly wrapping another
     *      exception.
     */
    @SuppressWarnings( "NoopMethodInAbstractClass" )
    @Override
    @MountPoint
    public void ignorableWhitespace( final char [] ch, final int start, final int length ) throws SAXException { /* Does nothing! */ }

    /**
     *  This method is called every time a new element was encountered by the
     *  parser. It should be overwritten if it is necessary to perform any
     *  activities for a specific element.<br>
     *  <br>The default implementation looks up a method handler in the map of
     *  element handlers and calls that, or throws an exception if no handler
     *  was registered for that element.
     *
     *  @param  element The element.
     *  @throws SAXException    Something has gone wrong.
     *
     *  @since 0.1.0
     */
    @MountPoint
    @API( status = MAINTAINED, since = "0.1.0" )
    protected void openElement( @SuppressWarnings( "UseOfConcreteClass" ) final Element element ) throws SAXException
    {
        final var method = m_HandlerMethods.get( element.getQName() );
        if( isNull(method ) ) throw new SAXException( format( MSG_NoHandler, element ) );

        //---* Process the element *-------------------------------------------
        method.process( false, null, element.getAttributes(), element.getPath() );
    }   //  openElement()

    /**
     *  Processing of an element of the XML file. This method will be called
     *  by
     *  {@link #endElement(String,String,String) endElement()}
     *  any time an element was closed.<br>
     *  <br>The default implementation looks up a method handler in the map of
     *  element handlers and calls that, or throws an exception if no handler
     *  was registered for that element.
     *
     *  @param  element The element.
     *  @throws SAXException    Something has gone wrong.
     *
     *  @since 0.1.0
     */
    @MountPoint
    @API( status = MAINTAINED, since = "0.1.0" )
    protected void processElement( @SuppressWarnings( "UseOfConcreteClass" ) final Element element ) throws SAXException
    {
        final var method = m_HandlerMethods.get( element.getQName() );
        if( isNull(method ) ) throw new SAXException( format( MSG_NoHandler, element.getQName() ) );

        //---* Process the element *-------------------------------------------
        method.process( true, element.getData(), element.getAttributes(), element.getPath() );
    }   //  processElement()

    /**
     *  Receives notification of a processing instruction.<br>
     *  <br>This implementation does nothing by default. Application writers
     *  may override this method in a subclass to take specific actions for
     *  each processing instruction, such as setting status variables or
     *  invoking other methods.
     *
     *  @param  target  The processing instruction target.
     *  @param  data    The processing instruction data, or {@code null}
     *      if none is supplied.
     *  @throws SAXException    Any SAX exception, possibly wrapping another
     *      exception.
     */
    @SuppressWarnings( "NoopMethodInAbstractClass" )
    @Override
    @MountPoint
    public void processingInstruction( final String target, final String data ) throws SAXException { /* Does nothing! */ }

    /**
     *  Adds an element handler to the map of handler methods.
     *
     *  @param  qName   The qualified name of the elements that should be
     *      processed by the handler .
     *  @param  method  The method reference for the handler.
     */
    protected final void registerElementHandler( final String qName, final HandlerMethod method )
    {
        m_HandlerMethods.put( requireNotEmptyArgument( qName, "qName" ), requireNonNullArgument( method, "method" ) );
    }   //  addElementHandler()

    /**
     *  Returns the current column number in the XML file. A negative value
     *  indicates that the column is unknown.
     *
     *  @return The current column number.
     */
    protected final int retrieveCurrentColumn() { return nonNull( m_Locator ) ? m_Locator.getColumnNumber() : -1; }

    /**
     *  Returns the current line number in the XML file. A negative value
     *  indicates that the line is unknown.
     *
     *  @return The current line number.
     */
    protected final int retrieveCurrentLine() { return nonNull( m_Locator ) ? m_Locator.getLineNumber() : -1; }

    /**
     *  Returns the namespace for the current element (that one that is on top
     *  of the element stack).
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the namespace for the current element. Will be
     *      {@linkplain Optional#empty() empty}
     *      if there is no namespace for the current element.
     *  @throws SAXException    An error occurred while retrieving the
     *      namespace information.
     *
     *  @since  0.1.0
     */
    @API( status = MAINTAINED, since = "0.1.0" )
    protected final Optional<URI> retrieveCurrentNamespace() throws SAXException
    {
        final var element = m_ElementStack.peek().orElseThrow( () -> new SAXParseException( MSG_NoElementOnStack, getLocator() ) );
        final var retValue = element.getURI();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveCurrentNamespace()

    /**
     *  Returns the URI of the namespace for the given prefix.
     *
     *  @param  prefix  The prefix.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the namespace for the prefix. Will be
     *      {@linkplain Optional#empty() empty}
     *      if there is no namespace for the given prefix.
     *
     *  @since 0.1.0
     */
    @API( status = MAINTAINED, since = "0.1.0" )
    protected final Optional<URI> retrieveNamespace( final String prefix )
    {
        final var retValue = Optional.ofNullable( m_Namespaces.get( requireNotEmptyArgument( prefix, "prefix" ) ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveNamespace()

    /**
     *  Returns the registered prefix for the given namespace. If more than one
     *  prefix is registered for the same namespace, only that one that is
     *  alphabetically the first one will be returned.
     *
     *  @param  namespace   The URI for the namespace.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the registered prefix.
     *  @since 0.1.0
     */
    @API( status = MAINTAINED, since = "0.1.0" )
    protected final Optional<String> retrievePrefix( final URI namespace )
    {
        requireNonNullArgument( namespace, "namespace" );
        final var retValue = m_Namespaces
            .entrySet()
            .stream()
            .filter( entry -> entry.getValue().equals( namespace ) )
            .map( Map.Entry::getKey )
            .findFirst();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrievePrefix()

    /**
     *  Receives an object for locating the origin of SAX document
     *  events.<br>
     *  <br>SAX parsers are strongly encouraged (though not absolutely
     *  required) to supply a locator: if it does so, it must supply the
     *  locator to the application by invoking this method before invoking any
     *  of the other methods in the ContentHandler interface.<br>
     *  <br>The locator allows the application to determine the end position
     *  of any document-related event, even if the parser is not reporting an
     *  error. Typically, the application will use this information for
     *  reporting its own errors (such as character content that does not match
     *  an application's business rules). The information returned by the
     *  locator is probably not sufficient for use with a search engine.<br>
     *  <br>Note that the locator will return correct information only during
     *  the invocation SAX event callbacks after startDocument returns and
     *  before endDocument is called. The application should not attempt to use
     *  it at any other time.
     *
     *  @param  locator An object that can return the location of any SAX
     *      document event.
     */
    @Override
    public final void setDocumentLocator( final Locator locator ) { m_Locator = requireNonNullArgument( locator, "locator" ); }

    /**
     *  Receives notification of a skipped entity.<br>
     *  <br>This implementation does nothing by default. Application writers
     *  may override this method in a subclass to take specific actions for
     *  each processing instruction, such as setting status variables or
     *  invoking other methods.
     *
     *  @param  name    The name of the skipped entity.
     *  @throws SAXException    Any SAX exception, possibly wrapping another
     *      exception.
     */
    @SuppressWarnings( "NoopMethodInAbstractClass" )
    @Override
    @MountPoint
    public void skippedEntity( final String name ) throws SAXException { /* Does nothing! */ }

    /**
     *  Receives the notification about the start of an element.
     *
     *  @param  uri The URI for the namespace of this element; can be empty.
     *  @param  localName   The local name of the element.
     *  @param  qName   The element's qualified name.
     *  @param  attributes  The element's attributes.
     *  @throws SAXException    The element was not correct according to the
     *      DTD.
     */
    @SuppressWarnings( "OverlyComplexMethod" )
    @Override
    public final void startElement( final String uri, final String localName, final String qName, final Attributes attributes ) throws SAXException
    {
        if( isNull( localName ) && isNull( qName ) )
        {
            throw new SAXParseException( "No name for element", getLocator() );
        }

        //---* Store the document type *---------------------------------------
        if( isNull( m_DocumentType ) ) m_DocumentType = qName;

        //---* Build the path *------------------------------------------------
        Element parent = null;
        final var path = new StringBuilder();
        if( !m_ElementStack.isEmpty() )
        {
            parent = m_ElementStack.peek().orElseThrow( () -> new SAXParseException( MSG_NoElementOnStack, getLocator() ) );
            path.append( parent.getPath() );
        }
        path.append( '/' )
            .append( qName.trim() );

        //---* Build the attributes map *--------------------------------------
        final Map<String,Attribute> attributesMap = new HashMap<>();
        try
        {
            for( var i = 0; i < attributes.getLength(); ++i )
            {
                attributesMap.put( attributes.getQName( i ), composeAttribute( attributes, i ) );
            }
        }
        catch( final IllegalArgumentException | URISyntaxException e )
        {
            throw new SAXParseException( "Invalid Argument data", getLocator(), e );
        }

        //---* Build the element *---------------------------------------------
        var effectiveQName = qName;
        var effectiveLocalName = localName;
        URI namespace = null;
        if( isNotEmptyOrBlank( uri ) )
        {
            try
            {
                namespace = new URI( uri );
            }
            catch( final URISyntaxException e )
            {
                throw new SAXParseException( format( MSG_InvalidURI, uri), getLocator(), e );
            }

            if( isNull( effectiveLocalName ) )
            {
                final var pos = effectiveQName.indexOf( ':' );
                effectiveLocalName = pos == -1 ? effectiveQName : effectiveQName.substring( pos + 1 );
            }
            if( isNull( effectiveQName ) )
            {
                final var prefix = retrievePrefix( namespace ).orElseThrow( () -> new SAXParseException( "Unknown Namespace: %s".formatted( uri ), getLocator() ) );
                effectiveQName = format( "%s:%s", prefix, effectiveLocalName );
            }
        }
        else
        {
            if( isNull( effectiveLocalName ) ) effectiveLocalName = effectiveQName;
            if( isNull( effectiveQName ) ) effectiveQName = effectiveLocalName;
        }

        final var element = new Element( effectiveQName, effectiveLocalName, namespace, attributesMap, path.toString(), parent );
        m_ElementStack.push( element );
        openElement( element );
    }  //  startElement()

    /**
     *  Receives the notification of the beginning of the document.<br>
     *  <br>This implementation does nothing by default. Application writers
     *  may override this method in a subclass to take specific actions at the
     *  beginning of a document (such as allocating the root node of a tree or
     *  creating an output file).
     *
     *  @throws SAXException    Any SAX exception, possibly wrapping another
     *      exception.
     */
    @SuppressWarnings( "NoopMethodInAbstractClass" )
    @Override
    @MountPoint
    public void startDocument() throws SAXException { /* Does nothing! */ }

    /**
     *  Receives the notification of the start of a Namespace mapping.
     *
     *  @param  prefix  The Namespace prefix being declared.
     *  @param  uri The Namespace URI mapped to the prefix.
     *  @throws SAXException    Any SAX exception, possibly wrapping another
     *      exception.
     */
    @Override
    public final void startPrefixMapping( final String prefix, final String uri ) throws SAXException
    {
        final URI namespace;
        try
        {
            namespace = new URI( requireNonNullArgument( uri, "uri" ) );
        }
        catch( final URISyntaxException e )
        {
            throw new SAXParseException( format( MSG_InvalidURI, uri), getLocator(), e );
        }

        //---* Store the mapping *---------------------------------------------
        m_Namespaces.put( requireNonNullArgument( prefix, "prefix" ), namespace );
    }   //  startPrefixMapping()
}
//  class AdvancedContentHandler

/*
 *  End of File
 */