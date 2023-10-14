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

package org.tquadrat.foundation.xml.builder.spi;

import static java.lang.Integer.signum;
import static java.lang.String.format;
import static java.util.Collections.unmodifiableSortedMap;
import static java.util.Comparator.naturalOrder;
import static org.apiguardian.api.API.Status.MAINTAINED;
import static org.tquadrat.foundation.lang.CommonConstants.XMLATTRIBUTE_Id;
import static org.tquadrat.foundation.lang.CommonConstants.XMLATTRIBUTE_Language;
import static org.tquadrat.foundation.lang.CommonConstants.XMLATTRIBUTE_Whitespace;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.Comparators.listBasedComparator;
import static org.tquadrat.foundation.util.StringUtils.isNotEmptyOrBlank;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.getAttributeNameValidator;
import static org.tquadrat.foundation.xml.builder.spi.SGMLPrinter.composeAttributesString;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.util.LazyMap;

/**
 *  <p>{@summary This class provides the support for attributes to
 *  elements.}</p>
 *  <p>For some SGML elements, their attributes should be ordered in a given
 *  sequence, either because of convenience or because of deficits of the
 *  parser processing them.</p>
 *  <p>This class provides a specific comparator for each named element that
 *  can be configured by the user.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: AttributeSupport.java 1071 2023-09-30 01:49:32Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: AttributeSupport.java 1071 2023-09-30 01:49:32Z tquadrat $" )
@API( status = MAINTAINED, since = "0.0.5" )
public final class AttributeSupport extends NamespaceSupport
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The default comparator that is used for attribute ordering if no other
     *  comparator is provided.
     */
    private static final Comparator<String> DEFAULT_COMPARATOR = naturalOrder();

    /**
     *  A
     *  {@link Comparator}
     *  that ensures that
     *  {@value org.tquadrat.foundation.lang.CommonConstants#XMLATTRIBUTE_Id}
     *  is always the first attribute.
     */
    @SuppressWarnings( {"IfStatementWithTooManyBranches", "OverlyLongLambda"} )
    public static final Comparator<String> ID_ALWAYS_FIRST_COMPARATOR = (a1,a2) ->
    {
        var retValue = 0;
        if( a1.equals( a2 ) )
        {
            retValue = 0;
        }
        else if( a1.equals( XMLATTRIBUTE_Id ) )
        {
            retValue = -1;
        }
        else if( a2.equals( XMLATTRIBUTE_Id ) )
        {
            retValue = 1;
        }
        else
        {
            retValue = signum( a1.compareTo( a2 ) );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    };

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The attributes for the element.
     */
    private final Map<String,String> m_Attributes;

    /**
     *  Flag that indicates whether the validity of attributes should be
     *  checked.
     */
    private final boolean m_CheckValid;

    /**
     *  The comparator that determines the sequence for the attributes of the
     *  owning element.
     */
    private Comparator<String> m_Comparator;

    /**
     *  The valid attributes for owning element.
     */
    private final Collection<String> m_ValidAttributes;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code AttributeSupport} instance that checks whether
     *  attributes are valid to be added.
     *
     *  @param  owner   The element that owns this {@code AttributeSupport}
     *      instance.
     */
    public AttributeSupport( final Element owner )
    {
        this( owner, true, DEFAULT_COMPARATOR );
    }   //  AttributeSupport()

    /**
     *  Creates a new {@code AttributeSupport} instance.
     *
     *  @param  owner   The element that owns this {@code AttributeSupport}
     *      instance.
     *  @param  checkValid  {@code true} when the validity of attributes should
     *      be checked, {@code false} if all attributes can be added.
     */
    public AttributeSupport( final Element owner, final boolean checkValid )
    {
        this( owner, checkValid, DEFAULT_COMPARATOR );
    }   //  AttributeSupport()

    /**
     *  Creates a new {@code AttributeSupport} instance that checks whether
     *  attributes are valid to be added.
     *
     *  @param  owner   The element that owns this {@code AttributeSupport}
     *      instance.
     *  @param  sortOrder   The comparator that determines the sort order for
     *      the attribute of the owning element.
     */
    public AttributeSupport( final Element owner, final Comparator<String> sortOrder )
    {
        this( owner, true, sortOrder );
    }   //  AttributeSupport()

    /**
     *  Creates a new {@code AttributeSupport} instance.
     *
     *  @param  owner   The element that owns this {@code AttributeSupport}
     *      instance.
     *  @param  checkValid  {@code true} when the validity of attributes should
     *      be checked, {@code false} if all attributes can be added.
     *  @param  sortOrder   The comparator that determines the sort order for
     *      the attribute of the owning element.
     */
    public AttributeSupport( final Element owner, final boolean checkValid, final Comparator<String> sortOrder )
    {
        super( owner );
        m_CheckValid = checkValid;
        setSortOrder( sortOrder );
        m_Attributes = LazyMap.use( HashMap::new );
        m_ValidAttributes = m_CheckValid ? new HashSet<>() : null;
        if( m_CheckValid )
        {
            //---* The reserved attributes that are always valid *-------------
            m_ValidAttributes.add( XMLATTRIBUTE_Id );
            m_ValidAttributes.add( XMLATTRIBUTE_Language );
            m_ValidAttributes.add( XMLATTRIBUTE_Whitespace );
        }
    }   //  AttributeSupport()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  <p>{@summary Checks whether an attribute with the given name is valid
     *  for the owning element.}</p>
     *  <p>The attribute is valid if there is a respective entry in the list
     *  of valid attributes, or when
     *  {@link #checksIfValid()}
     *  returns {@code false}.</p>
     *
     *  @param  attribute   The name of the attribute.
     *  @return {@code true} if the attribute is valid for the given element,
     *      {@code false} otherwise.
     *  @throws InvalidXMLNameException The attribute name is invalid.
     */
    public final boolean checkValid( final String attribute ) throws InvalidXMLNameException
    {
        if( !getAttributeNameValidator().test( requireNotEmptyArgument( attribute, "attribute" ) ) ) throw new InvalidXMLNameException( attribute );

        final var retValue = !checksIfValid() || m_ValidAttributes.contains( attribute );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  checkValid()

    /**
     *  Returns a flag that indicates whether an extended validity check is
     *  performed on attributes before adding them.
     *
     *  @return {@code true} if extended validation are performed,
     *      {@code false} if attribute can be added.
     *
     *  @see #setAttribute(String, CharSequence, Optional)
     */
    @SuppressWarnings( "BooleanMethodNameMustStartWithQuestion" )
    public final boolean checksIfValid() { return m_CheckValid; }

    /**
     *  Returns the value for the attribute with the given name.
     *
     *  @param  name    The attribute name.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the value for that attribute.
     */
    public final Optional<String> getAttribute( final String name )
    {
        final var retValue = Optional.ofNullable( m_Attributes.get( requireNotEmptyArgument( name, "name" ) ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getAttribute()

    /**
     *  Provides read access to the attributes.
     *
     *  @return A reference to the attributes.
     */
    public final Map<String,String> getAttributes()
    {
        final SortedMap<String,String> map = new TreeMap<>( m_Comparator );
        map.putAll( m_Attributes );
        final var retValue =  unmodifiableSortedMap( map );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getAttributes()

    /**
     *  Returns the attribute sort order.
     *
     *  @return The comparator that determines the attribute's sequence.
     */
    @SuppressWarnings( "SuspiciousGetterSetter" )
    public final Comparator<String> getSortOrder() { return m_Comparator; }

    /**
     *  <p>{@summary Registers the valid attributes for the owning
     *  element.}</p>
     *  <p>Nothing happens if
     *  {@link #checksIfValid()}
     *  returns {@code false}, although a call to this method is obsolete
     *  then.</p>
     *
     *  @note   The given attributes will be <i>added</i> to the already
     *      existing ones!
     *
     *  @param  attributes  The names of the valid attributes.
     *  @throws InvalidXMLNameException One of the attribute names is invalid.
     */
    public final void registerAttributes( final String... attributes )
    {
        if( m_CheckValid )
        {
            for( final var attribute : requireNonNullArgument( attributes, "attributes" ) )
            {
                if( !getAttributeNameValidator().test( attribute ) ) throw new InvalidXMLNameException( attribute );
                m_ValidAttributes.add( attribute );
            }
        }
    }   //  registerAttributes()

    /**
     *  <p>{@summary Registers an attribute sequence for the owning element;
     *  this modifies any sort order that was previously set.}</p>
     *  <p>The names for the attributes are not validated; in particular, it
     *  is not checked whether an attribute is listed as valid.</p>
     *
     *  @param  attributes  The names of the attributes in the desired
     *      sequence.
     */
    public final void registerSequence( final String... attributes )
    {
        if( requireNonNullArgument( attributes, "attributes" ).length > 0 )
        {
            final Comparator<String> comparator = listBasedComparator( s -> s, naturalOrder(), attributes );
            setSortOrder( comparator );
        }
    }   //  registerSequence()

    /**
     *  Returns the list of the registered attributes.
     *
     *  @return The registered attributes.
     */
    public final Collection<String> retrieveValidAttributes() { return List.copyOf( m_ValidAttributes ); }

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The given attribute name is validated using the method that is
     *  provided by
     *  {@link org.tquadrat.foundation.xml.builder.XMLBuilderUtils#getAttributeNameValidator()}.</p>
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  value   The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @param  append  If not
     *      {@linkplain Optional#empty() empty}, the new value will be appended
     *      on an already existing one, and this sequence is used as the
     *      separator.
     *  @return An instance of
     *      {@link Optional}
     *      that holds the former value of the attribute; will be
     *      {@link Optional#empty()}
     *      if the element did not have an attribute with the given name
     *      before.
     *  @throws IllegalArgumentException    The attribute name is invalid or
     *      the attribute is not valid for the element that owns this instance
     *      of {@code AttributeSupport}.
     */
    public final Optional<String> setAttribute( final String name, final CharSequence value, @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" ) final Optional<? extends CharSequence> append ) throws IllegalArgumentException
    {
        requireNonNullArgument( append, "append" );
        if( !checkValid( name ) ) throw new IllegalArgumentException( "Invalid attribute name: %s".formatted( name ) );

        //---* Get the current value for the given name *----------------------
        final var retValue = Optional.ofNullable( m_Attributes.get( name ) );

        if( nonNull( value ) )
        {
            //---* Set the new value *-----------------------------------------
            if( retValue.isEmpty() || append.isEmpty() )
            {
                m_Attributes.put( name, value.toString() );
            }
            else
            {
                final var oldValue = retValue.get();
                final var newValue = isNotEmptyOrBlank( oldValue ) ? format( "%1$s%3$s%2$s", oldValue, value, append.get() ) : value.toString();
                m_Attributes.replace( name, newValue );
            }
        }
        else
        {
            //---* Remove the value *------------------------------------------
            m_Attributes.remove( name );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  setAttribute()

    /**
     *  Sets the comparator that determines the sequence of the attributes for
     *  the owning element.
     *
     *  @param  sortOrder  The comparator.
     */
    public final void setSortOrder( final Comparator<String> sortOrder )
    {
        m_Comparator = requireNonNullArgument( sortOrder, "sortOrder" );
    }   //  setSortOrder()

    /**
     *  Returns the attributes and their values, together with the namespaces,
     *  as a single formatted string.
     *
     *  @param  indentationLevel    The indentation level.
     *  @param  prettyPrint The pretty print flag.
     *  @return The attributes string.
     */
    @Override
    public final String toString( final int indentationLevel, final boolean prettyPrint )
    {
        final var retValue = composeAttributesString( indentationLevel, prettyPrint, getOwner().getElementName(), getAttributes(), getNamespaces() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class AttributeSupport

/*
 *  End of File
 */