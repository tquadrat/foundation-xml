/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
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

import static org.apiguardian.api.API.Status.DEPRECATED;

import java.util.Set;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.builder.XMLBuilderUtils;
import org.tquadrat.foundation.xml.builder.XMLElement;
import org.tquadrat.foundation.xml.builder.internal.XMLElementImpl;

/**
 *  The abstract base class for specialised implementations of
 *  {@link XMLElement}. Because it is derived from
 *  {@link XMLElementImpl},
 *  it supports attributes, namespaces, children, text, {@code CDATA} and
 *  comments.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: XMLElementBase.java 1030 2022-04-06 13:42:02Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 *
 *  @deprecated Use
 *      {@link XMLElementAdapter}
 *      instead.
 */
@SuppressWarnings( {"AbstractClassExtendsConcreteClass", "DeprecatedIsStillUsed"} )
@ClassVersion( sourceVersion = "$Id: XMLElementBase.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@API( status = DEPRECATED, since = "0.0.5" )
@Deprecated( since = "0.1.0", forRemoval = true )
public abstract non-sealed class XMLElementBase extends XMLElementImpl
{
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code XMLElementBase} instance.<br>
     *  <br>The given element name is validated using the method that is
     *  provided by
     *  {@link org.tquadrat.foundation.xml.builder.XMLBuilderUtils#getElementNameValidator()}.<br>
     *  <br>The new element allows attributes and children, but will not
     *  validate them. It also allows text.
     *
     *  @param  elementName The element name.
     */
    protected XMLElementBase( final String elementName ) { super( elementName ); }

    /**
     *  Creates a new {@code XMLElementImpl} instance.<br>
     *  <br>The given element name is validated using the method that is
     *  provided by
     *  {@link XMLBuilderUtils#getElementNameValidator()}.
     *
     *  @note   This constructor is used for the implementation of XML
     *      specialisations, like SVG or HTML (although this not really XML).
     *
     *  @param  elementName The element name.
     *  @param  flags   The configuration flags for the new element.
     *
     *  @see org.tquadrat.foundation.xml.builder.XMLElement.Flags
     *  @see AttributeSupport#registerAttributes(String...)
     *  @see AttributeSupport#registerSequence(String...)
     *  @see ChildSupport#registerChildren(String...)
     */
    protected XMLElementBase( final String elementName, final Set<Flags> flags )
    {
        super( elementName, flags );
    }   //  XMLElementBase()

    /**
     *  Creates a new {@code XMLElementImpl} instance.<br>
     *  <br>The given element name is validated using the method that is
     *  provided by
     *  {@link XMLBuilderUtils#getElementNameValidator()}.
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
     *      {@link #XMLElementBase(String, Set)}
     *      instead.
     */
    @SuppressWarnings( "removal" )
    @Deprecated( forRemoval = true )
    protected XMLElementBase( final String elementName, final String [] validChildren, final String [] validAttributes, final String [] attributeSequence, final boolean allowText )
    {
        super( elementName, validChildren, validAttributes, attributeSequence, allowText );
    }   //  XMLElementBase()
}
//  class XMLElementBase

/*
 *  End of File
 */