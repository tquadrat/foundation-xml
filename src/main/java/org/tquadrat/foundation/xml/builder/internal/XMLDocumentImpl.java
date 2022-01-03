/*
 * ============================================================================
 *  Copyright © 2002-2020 by Thomas Thrien.
 *  All Rights Reserved.
 * ============================================================================
 *  Licensed to the public under the agreements of the GNU Lesser General Public
 *  License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

package org.tquadrat.foundation.xml.builder.internal;

import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.lang.CommonConstants.UTF8;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.StringUtils.isNotEmptyOrBlank;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.composeXMLHeader;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.builder.ProcessingInstruction;
import org.tquadrat.foundation.xml.builder.XMLDocument;
import org.tquadrat.foundation.xml.builder.XMLElement;
import org.tquadrat.foundation.xml.builder.spi.Element;
import org.tquadrat.foundation.xml.builder.spi.InvalidXMLNameException;

/**
 *  The implementation for the interface
 *  {@link XMLDocument}.<br>
 *  <br>It allows document comments and processing instructions to be added.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: XMLDocumentImpl.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "ClassWithTooManyConstructors" )
@ClassVersion( sourceVersion = "$Id: XMLDocumentImpl.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public final class XMLDocumentImpl implements XMLDocument
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The child elements for this document.
     */
    @SuppressWarnings( "TypeMayBeWeakened" )
    private final List<Element> m_Children = new ArrayList<>();

    /**
     *  The root element for this document.
     */
    @SuppressWarnings( "InstanceVariableOfConcreteClass" )
    private final XMLElementImpl m_RootElement;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code XMLDocumentImpl} instance.<br>
     *  <br>The resulting document will do not have an explicit doc type, the
     *  root element will be {@code <root>}. The encoding is defined as
     *  UTF-8.<br>
     *  <br>Basically, this document would have the DTD
     *  <pre><code>&lt;!ELEMENT root ANY&gt;</code></pre>.
     */
    public XMLDocumentImpl() { this( "root" ); }

    /**
     *  Creates a new {@code XMLDocumentImpl} instance.<br>
     *  <br>The resulting document will do not have an explicit doc type, the
     *  encoding is defined as UTF-8.<br>
     *  <br>The given root element name is validated using the method that is
     *  provided by
     *  {@link org.tquadrat.foundation.xml.builder.XMLBuilderUtils#getElementNameValidator()}.
     *
     *  @param  rootElementName The name of the root element for this document.
     */
    public XMLDocumentImpl( final String rootElementName )
    {
        this( new XMLElementImpl( requireNotEmptyArgument( rootElementName, "rootElementName" ) ), true );
    }   //  XMLDocumentImpl()

    /**
     *  Creates a new {@code XMLDocumentImpl} instance.<br>
     *  <br>The resulting document will do not have an explicit doc type, the
     *  encoding is defined as UTF-8.
     *
     *  @param  rootElement The root element for this document.
     *  @param  standalone  {@code true} if the XML document is standalone,
     *      {@code false} if not.
     */
    @SuppressWarnings( "CastToConcreteClass" )
    public XMLDocumentImpl( final XMLElement rootElement, final boolean standalone )
    {
        m_RootElement = (XMLElementImpl) requireNonNullArgument( rootElement, "rootElement" );
        addProcessingInstruction( composeXMLHeader( UTF8, standalone ) );
    }   //  XMLDocumentBase()

    /**
     *  Creates a new {@code XMLDocumentImpl} instance.<br>
     *  <br>The given root element name is validated using the method that is
     *  provided by
     *  {@link org.tquadrat.foundation.xml.builder.XMLBuilderUtils#getElementNameValidator()}.
     *
     *  @param  rootElementName The name for the root element for this
     *      document.
     *  @param  encoding    The encoding for the new XML document.
     *  @param  name    The name for the DTD.
     *  @param  uri The URI for the DTD.
     */
    public XMLDocumentImpl( final String rootElementName, final Charset encoding, final String name, final URI uri )
    {
        this( new XMLElementImpl( requireNotEmptyArgument( rootElementName, "rootElementName" ) ), encoding, name, uri );
    }   //  XMLDocumentImpl()

    /**
     *  Creates a new {@code XMLDocumentImpl} instance.
     *
     *  @param  rootElement The root element for this document.
     *  @param  encoding    The encoding for the new XML document.
     *  @param  name    The name for the DTD.
     *  @param  uri The URI for the DTD.
     */
    @SuppressWarnings( "CastToConcreteClass" )
    public XMLDocumentImpl( final XMLElement rootElement, final Charset encoding, final String name, final URI uri )
    {
        m_RootElement = (XMLElementImpl) requireNonNullArgument( rootElement, "rootElement" );
        addProcessingInstruction( composeXMLHeader( encoding, false ) );
        final var docType = new DocType( m_RootElement.getElementName(), requireNotEmptyArgument( name, "name" ), requireNonNullArgument( uri, "uri" ) );
        addDocumentChild( docType );
    }   //  XMLDocumentBase()

    /**
     *  Creates a new {@code XMLDocumentImpl} instance.<br>
     *  <br>The given element name is validated using the method that is
     *  provided by
     *  {@link org.tquadrat.foundation.xml.builder.XMLBuilderUtils#getElementNameValidator()}.
     *
     *  @param  rootElementName The name of the root element for this document.
     *  @param  encoding    The encoding for the new XML document.
     *  @param  uri The URI for the DTD.
     */
    public XMLDocumentImpl( final String rootElementName, final Charset encoding, final URI uri )
    {
        this( new XMLElementImpl( requireNotEmptyArgument( rootElementName, "rootElementName" ) ), encoding, uri );
    }   //  XMLDocumentImpl()

    /**
     *  Creates a new {@code XMLDocumentImpl} instance.
     *
     *  @param  rootElement The root element for this document.
     *  @param  encoding    The encoding for the new XML document.
     *  @param  uri The URI for the DTD.
     */
    @SuppressWarnings( "CastToConcreteClass" )
    public XMLDocumentImpl( final XMLElement rootElement, final Charset encoding, final URI uri )
    {
        m_RootElement = (XMLElementImpl) requireNonNullArgument( rootElement, "rootElement" );
        addProcessingInstruction( composeXMLHeader( encoding, false ) );
        final var docType = new DocType( rootElement.getElementName(), requireNonNullArgument( uri, "uri" ) );
        addDocumentChild( docType );
    }   //  XMLDocumentImpl()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Adds a child to the document itself, <i>not</i> to the root element.
     *
     *  @param  <E> The type of the child to add.
     *  @param  child   The element to add.
     *  @return This instance.
     *  @throws IllegalStateException   The child has already a parent that is
     *      not this document.
     */
    @SuppressWarnings( "PublicMethodNotExposedInInterface" )
    public final <E extends Element> XMLDocument addDocumentChild( final E child ) throws IllegalStateException
    {
        if( child.getParent().isPresent() && (child.getParent().get() != m_RootElement) )
        {
            throw new IllegalStateException( "The child has already a parent" );
        }
        m_Children.add( child );
        child.setParent( m_RootElement );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  addDocumentChild()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final XMLDocument addDocumentComment( final CharSequence comment ) throws IllegalArgumentException
    {
        if( isNotEmptyOrBlank( comment ) ) addDocumentChild( new Comment( comment ) );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  addDocumentComment()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final XMLDocument addProcessingInstruction( final ProcessingInstruction processingInstruction ) throws IllegalArgumentException, IllegalStateException
    {
        return addDocumentChild( processingInstruction );
    }   //  addProcessingInstruction()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Collection<? extends Element> getChildren()
    {
        final Collection<Element> list = new ArrayList<>( m_Children.size() + 1 );
        list.addAll( m_Children );
        list.add( m_RootElement );

        final Collection<Element> retValue = List.copyOf( list );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getChildren()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final XMLElement getRootElement() { return m_RootElement; }

    /**
     *  Registers an attribute sequence for the root element of this document;
     *  this modifies any sort order that was previously set.<br>
     *  <br>The names for the attributes are not validated; in particular, it
     *  is not checked whether an attribute is listed as valid.
     *
     *  @param  attributes  The names of the attributes in the desired
     *      sequence.
     */
    @SuppressWarnings( "PublicMethodNotExposedInInterface" )
    public final void registerAttributeSequence( final String... attributes )
    {
        m_RootElement.registerAttributeSequence( attributes );
    }   //  registerAttributeSequence()

    /**
     *  Registers an attribute sequence for the root element of this document;
     *  this modifies any sort order that was previously set.
     *
     *  @param  sortOrder  The sort order for the attributes.
     */
    @SuppressWarnings( "PublicMethodNotExposedInInterface" )
    public final void registerAttributeSequence( final Comparator<String> sortOrder )
    {
        m_RootElement.registerAttributeSequence( sortOrder );
    }   //  registerAttributeSequence()

    /**
     *  Registers the valid attributes for the root element of this
     *  document.
     *
     *  @note   The given attributes will be <i>added</i> to the already
     *      existing ones!
     *
     *  @param  attributes  The names of the valid attributes.
     *  @throws InvalidXMLNameException One of the attribute names is invalid.
     */
    @SuppressWarnings( "PublicMethodNotExposedInInterface" )
    public final void registerValidAttributes( final String... attributes )
    {
        m_RootElement.registerValidAttributes( attributes );
    }   //  registerValidAttributes()

    /**
     *  Registers the element names of valid child elements for this document.
     *
     *  @note   The given children will be <i>added</i> to the already existing
     *      ones!
     *
     *  @param  children    The element names of the valid children.
     */
    @SuppressWarnings( "PublicMethodNotExposedInInterface" )
    public final void registerValidChildren( final String... children )
    {
        m_RootElement.registerValidChildren( children );
    }   //  registerValidChildren()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString() { return toString( true ); }
}
//  class XMLDocumentImpl

/*
 *  End of File
 */