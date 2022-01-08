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
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.getElementNameValidator;
import static org.tquadrat.foundation.xml.builder.XMLElement.Flags.ALLOWS_CHILDREN;
import static org.tquadrat.foundation.xml.builder.XMLElement.Flags.ALLOWS_TEXT;
import static org.tquadrat.foundation.xml.builder.XMLElement.Flags.VALIDATES_ATTRIBUTES;
import static org.tquadrat.foundation.xml.builder.XMLElement.Flags.VALIDATES_CHILDREN;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.builder.Namespace;
import org.tquadrat.foundation.xml.builder.XMLBuilderUtils;
import org.tquadrat.foundation.xml.builder.XMLElement;
import org.tquadrat.foundation.xml.builder.spi.AttributeSupport;
import org.tquadrat.foundation.xml.builder.spi.ChildSupport;
import org.tquadrat.foundation.xml.builder.spi.Element;
import org.tquadrat.foundation.xml.builder.spi.InvalidXMLNameException;

/**
 *  An implementation of
 *  {@link XMLElement}
 *  that supports attributes, namespaces, children, text, {@code CDATA} and
 *  comments.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: XMLElementImpl.java 980 2022-01-06 15:29:19Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "removal" )
@ClassVersion( sourceVersion = "$Id: XMLElementImpl.java 980 2022-01-06 15:29:19Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public sealed class XMLElementImpl implements XMLElement
    permits org.tquadrat.foundation.xml.builder.spi.XMLElementAdapter, org.tquadrat.foundation.xml.builder.spi.XMLElementBase
    /*
     * org.tquadrat.foundation.xml.builder.spi.XMLElementBase is deprecated now
     * and will be removed from the permits list once the class will be
     * removed.
     *
     * org.tquadrat.foundation.xml.builder.spi.XMLElementAdapter is the
     * replacement with the same functionality.
     */
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The attribute support.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    private final AttributeSupport m_Attributes;

    /**
     *  The child support.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    private final ChildSupport m_Children;

    /**
     *  The element name.
     */
    private final String m_ElementName;

    /**
     *  The parent element.
     */
    private Element m_Parent;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  <p>{@summary Creates a new {@code XMLElementImpl} instance.}</p>
     *  <p>The given element name is validated using the method that is
     *  provided by
     *  {@link XMLBuilderUtils#getElementNameValidator()}.</p>
     *  <p>The new element allows attributes and children, but will not
     *  validate them. It also allows text.</p>
     *
     *  @note   This constructor is mainly used by the factory methods in
     *      {@link XMLBuilderUtils}
     *      for on-the-fly XML generation.
     *
     *  @param  elementName The element name.
     */
    public XMLElementImpl( final String elementName )
    {
        m_ElementName = requireNotEmptyArgument( elementName, "elementName" );
        if( !getElementNameValidator().test( elementName ) ) throw new InvalidXMLNameException( elementName );

        m_Attributes = new AttributeSupport( this, false );
        m_Children = new ChildSupport( this, false, true, true, XMLBuilderUtils::escapeXML );
    }   //  XMLElementImpl()

    /**
     *  <p>{@summary Creates a new {@code XMLElementImpl} instance.}</p>
     *  <p>The given element name is validated using the method that is
     *  provided by
     *  {@link XMLBuilderUtils#getElementNameValidator()}.</p>
     *
     *  @note   This constructor is used for the implementation of XML
     *      specialisations, like SVG or HTML (although this not really XML).
     *      It is made accessible through
     *      {@link org.tquadrat.foundation.xml.builder.spi.XMLElementBase}
     *
     *  @param  elementName The element name.
     *  @param  flags   The configuration flags for the new element.
     *
     *  @see org.tquadrat.foundation.xml.builder.XMLElement.Flags
     *  @see AttributeSupport#registerAttributes(String...)
     *  @see AttributeSupport#registerSequence(String...)
     *  @see ChildSupport#registerChildren(String...)
     */
    protected XMLElementImpl( final String elementName, final Set<Flags> flags )
    {
        m_ElementName = requireNotEmptyArgument( elementName, "elementName" );
        if( !getElementNameValidator().test( elementName ) ) throw new InvalidXMLNameException( elementName );

        final var checkAttributes = requireNonNullArgument( flags, "flags" ).contains( VALIDATES_ATTRIBUTES );
        final var checkChildren = flags.contains( VALIDATES_CHILDREN );
        final var allowChildren = checkChildren || flags.contains( ALLOWS_CHILDREN );
        final var allowText = flags.contains( ALLOWS_TEXT );

        m_Attributes = new AttributeSupport( this, checkAttributes );
        m_Children = new ChildSupport( this, checkChildren, allowChildren, allowText, XMLBuilderUtils::escapeXML );
    }   //  XMLElementImpl()

    /**
     *  <p>{@summary Creates a new {@code XMLElementImpl} instance.}</p>
     *  <p>The given element name is validated using the method that is
     *  provided by
     *  {@link XMLBuilderUtils#getElementNameValidator()}.</p>
     *
     *  @param  elementName The element name.
     *  @param  validChildren   The list of the names for valid children; if
     *      {@code null}, no children are allowed, if empty, children are
     *      allowed, but they will not be validated.
     *  @param  validAttributes The list of the valid attributes; if empty or
     *      {@code null}, the attributes will not be validated.
     *  @param  attributeSequence   The sequence for the attributes; if empty
     *      or {@code null}, the attributes will be sorted alphabetically.
     *  @param  allowText   {@code true} if the element allows text,
     *      {@code false} if not.
     *
     *  @see AttributeSupport#registerAttributes(String...)
     *  @see AttributeSupport#registerSequence(String...)
     *  @see ChildSupport#registerChildren(String...)
     *
     *  @deprecated Use
     *      {@link #XMLElementImpl(String, Set)}
     *      instead.
     */
    @Deprecated( forRemoval = true )
    public XMLElementImpl( final String elementName, final String [] validChildren, final String [] validAttributes, final String [] attributeSequence, final boolean allowText )
    {
        m_ElementName = requireNotEmptyArgument( elementName, "elementName" );
        if( !getElementNameValidator().test( elementName ) ) throw new InvalidXMLNameException( elementName );

        final var checkAttributes = nonNull( validAttributes ) && (validAttributes.length > 0);
        final var allowChildren = nonNull( validChildren );
        final var checkChildren = allowChildren && (validChildren.length > 0);

        m_Attributes = new AttributeSupport( this, checkAttributes );
        m_Children = new ChildSupport( this, checkChildren, allowChildren, allowText, XMLBuilderUtils::escapeXML );

        if( checkAttributes ) registerValidAttributes( validAttributes );
        if( nonNull( attributeSequence ) && (attributeSequence.length > 0) ) registerAttributeSequence( attributeSequence );
        if( checkChildren ) m_Children.registerChildren( validChildren );
    }   //  XMLElementImpl()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final XMLElement addCDATA( final CharSequence text ) throws IllegalArgumentException
    {
        m_Children.addCDATA( text );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  addCDATA()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final <E extends XMLElement> XMLElement addChild( final E child ) throws IllegalArgumentException, IllegalStateException
    {
        m_Children.addChild( child );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  addChild()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final XMLElement addComment( final CharSequence comment ) throws IllegalArgumentException
    {
        m_Children.addComment( comment );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  addComment()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final XMLElement addPredefinedMarkup( final CharSequence markup ) throws IllegalArgumentException
    {
        m_Children.addPredefinedMarkup( markup );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  addPredefinedMarkup()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final XMLElement addText( final CharSequence text ) throws IllegalArgumentException
    {
        m_Children.addText( text );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  addText()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Optional<String> getAttribute( final String name ) { return m_Attributes.getAttribute( name ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public Map<String,String> getAttributes() { return m_Attributes.getAttributes(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public Collection<? extends Element> getChildren()
    {
        final var retValue = m_Children.getChildren();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getChildren()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String getElementName() { return m_ElementName; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Set<Flags> getFlags()
    {
        final var retValue = EnumSet.noneOf( Flags.class );
        if( nonNull( m_Children ) )
        {
            if( m_Children.allowsChildren() ) retValue.add( ALLOWS_CHILDREN );
            if( m_Children.allowsText() ) retValue.add( ALLOWS_TEXT );
            if( m_Children.checksIfValid() ) retValue.add( VALIDATES_CHILDREN );
        }

        if( m_Attributes.checksIfValid() ) retValue.add( VALIDATES_ATTRIBUTES );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getFlags()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Collection<Namespace> getNamespaces() { return m_Attributes.getNamespaces(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Optional<Element> getParent() { return Optional.ofNullable( m_Parent ); }

    /**
     *  Returns the attribute sort order.
     *
     *  @return The comparator that determines the attribute's sequence.
     */
    @SuppressWarnings( "PublicMethodNotExposedInInterface" )
    public final Comparator<String> getSortOrder() { return m_Attributes.getSortOrder(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public boolean hasChildren() { return m_Children.hasChildren(); }

    /**
     *  <p>{@summary Registers an attribute sequence for this element}; this
     *  modifies any sort order that was previously set.</p>
     *  <p>The names for the attributes are not validated; in particular, it
     *  is not checked whether an attribute is listed as valid.</p>
     *
     *  @param  attributes  The names of the attributes in the desired
     *      sequence.
     */
    @SuppressWarnings( "PublicMethodNotExposedInInterface" )
    public final void registerAttributeSequence( final String... attributes )
    {
        m_Attributes.registerSequence( attributes );
    }   //  registerAttributeSequence()

    /**
     *  Registers an attribute sequence for this element; this modifies any
     *  sort order that was previously set.
     *
     *  @param  sortOrder  The sort order for the attributes.
     */
    @SuppressWarnings( "PublicMethodNotExposedInInterface" )
    public final void registerAttributeSequence( final Comparator<String> sortOrder )
    {
        m_Attributes.setSortOrder( sortOrder );
    }   //  registerAttributeSequence()

    /**
     *  Registers the valid attributes for this element.<br>
     *  <br>Nothing happens if
     *  {@link AttributeSupport#checksIfValid()}
     *  returns {@code false}, although a call to this method is obsolete then.
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
        m_Attributes.registerAttributes( attributes );
    }   //  registerValidAttributes()

    /**
     *  Registers the element names of valid child elements for this element.
     *
     *  @note   The given children will be <i>added</i> to the already existing
     *      ones!
     *
     *  @param  children    The element names of the valid children.
     */
    @SuppressWarnings( "PublicMethodNotExposedInInterface" )
    public final void registerValidChildren( final String... children )
    {
        if( nonNull( m_Children ) ) m_Children.registerChildren( children );
    }   //  registerValidChildren()

    /**
     *  Returns the list of the registered attributes.
     *
     *  @return The registered attributes.
     */
    @SuppressWarnings( "PublicMethodNotExposedInInterface" )
    public final Collection<String> retrieveValidAttributes() { return m_Attributes.retrieveValidAttributes(); }

    /**
     *  Returns the list of the registered children.
     *
     *  @return The registered children.
     */
    @SuppressWarnings( "PublicMethodNotExposedInInterface" )
    public final Collection<String> retrieveValidChildren() { return m_Children.retrieveValidChildren(); }

    /**
     *  {@inheritDoc}
     *  <p>The given attribute name is validated using the method that is
     *  provided by
     *  {@link XMLBuilderUtils#getAttributeNameValidator()}.</p>
     */
    @Override
    public final XMLElement setAttribute( final String name, final CharSequence value, final Optional<? extends CharSequence> append ) throws IllegalArgumentException
    {
        m_Attributes.setAttribute( name, value, append );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setAttribute()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final XMLElement setNamespace( final String identifier ) throws IllegalArgumentException, URISyntaxException
    {
        m_Attributes.setNamespace( identifier );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setNamespace()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final XMLElement setNamespace( final URI identifier ) throws IllegalArgumentException
    {
        m_Attributes.setNamespace( identifier );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setNamespace()

    /**
     *  {@inheritDoc}
     *  <p>The given prefix is validated using the method that is
     *  provided by
     *  {@link XMLBuilderUtils#getPrefixValidator()}.</p>
     */
    @Override
    public final XMLElement setNamespace( final String prefix, final String identifier ) throws IllegalArgumentException, URISyntaxException
    {
        m_Attributes.setNamespace( prefix, identifier );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setNamespace()

    /**
     *  {@inheritDoc}
     *  <p>The given prefix is validated using the method that is
     *  provided by
     *  {@link XMLBuilderUtils#getPrefixValidator()}.</p>
     */
    @Override
    public final XMLElement setNamespace( final String prefix, final URI identifier ) throws IllegalArgumentException
    {
        m_Attributes.setNamespace( prefix, identifier );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setNamespace()

    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public final XMLElement setNamespace( final Namespace namespace ) throws IllegalArgumentException
    {
        m_Attributes.setNamespace( namespace );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setNamespace()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final <E extends Element> void setParent( final E parent )
    {
        m_Parent = requireNonNullArgument( parent, "parent" );
    }   //  setParent()

    /**
     *  {@inheritDoc}
     */
    @Override
    public String toString() { return toString( 0, true ); }
}
//  class XMLElementImpl

/*
 *  End of File
 */