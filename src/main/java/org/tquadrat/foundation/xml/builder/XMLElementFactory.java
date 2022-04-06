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

package org.tquadrat.foundation.xml.builder;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.getElementNameValidator;

import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.builder.spi.InvalidXMLNameException;
import org.tquadrat.foundation.xml.builder.spi.XMLElementFactoryBase;

/**
 *  The definition of a helper class for the creation of
 *  {@link XMLElement}
 *  instances with an element name belonging to a specified namespace.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: XMLElementFactory.java 1030 2022-04-06 13:42:02Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "InterfaceMayBeAnnotatedFunctional" )
@ClassVersion( sourceVersion = "$Id: XMLElementFactory.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public sealed interface XMLElementFactory
    permits XMLElementFactoryBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Composes the name of an XML element from the prefix and the given
     *  element name.<br>
     *  <br>The given element name is validated using the method that is
     *  provided by
     *  {@link XMLBuilderUtils#getElementNameValidator()}.
     *
     *  @param  elementName The name of an XML element without any namespace
     *      prefix.
     *  @return The final element name.
     */
    public default String composeElementName( final String elementName )
    {
        requireNotEmptyArgument( elementName, "elementName" );
        final var retValue = getPrefix().map( prefix -> format( "%s:%s", prefix, elementName ) ).orElse( elementName );
        if( !getElementNameValidator().test( retValue ) )
        {
            throw new InvalidXMLNameException( elementName );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  composeElementName()

    /**
     *  Creates an XML element for the given element name that supports
     *  attributes, namespaces, children, text, {@code CDATA} and comments.<br>
     *  <br>The given element name is validated using the method that is
     *  provided by
     *  {@link XMLBuilderUtils#getElementNameValidator()}.
     *
     *  @param  elementName The element name.
     *  @return The new XML element.
     */
    public default XMLElement createXMLElement( final String elementName )
    {
        return XMLBuilderUtils.createXMLElement( composeElementName( elementName ) );
    }   //  createXMLElement()

    /**
     *  <p>{@summary Creates an XML element for the given element name that
     *  supports attributes, namespaces, children, text, {@code CDATA} and
     *  comments, and adds the given text.}</p>
     *  <p>The given element name is validated using the method that is
     *  provided by
     *  {@link XMLBuilderUtils#getElementNameValidator()}.</p>
     *
     *  @param  elementName The element name.
     *  @param  text    The text for the new element.
     *  @return The new XML element.
     */
    public default XMLElement createXMLElement( final String elementName, final CharSequence text )
    {
        return XMLBuilderUtils.createXMLElement( composeElementName( elementName ), text );
    }   //  createXMLElement()

    /**
     *  Creates an XML element for the given element name that supports
     *  attributes, namespaces, children, text, {@code CDATA} and comments, and
     *  adds it as child to the given parent.<br>
     *  <br>The given element name is validated using the method that is
     *  provided by
     *  {@link XMLBuilderUtils#getElementNameValidator()}.
     *
     *  @param  elementName The element name.
     *  @param  parent  The parent element.
     *  @return The new XML element.
     */
    public default XMLElement createXMLElement( final String elementName, final XMLElement parent )
    {
        return XMLBuilderUtils.createXMLElement( composeElementName( elementName ), parent );
    }   //  createXMLElement()

    /**
     *  Creates an XML element for the given element name that supports
     *  attributes, namespaces, children, text, {@code CDATA} and comments,
     *  adds the given text, and adds it as child to the given parent.<br>
     *  <br>The given element name is validated using the method that is
     *  provided by
     *  {@link XMLBuilderUtils#getElementNameValidator()}.
     *
     *  @param  elementName The element name.
     *  @param  parent  The parent element.
     *  @param  text    The text for the new element.
     *  @return The new XML element.
     */
    public default XMLElement createXMLElement( final String elementName, final XMLElement parent, final CharSequence text )
    {
        return XMLBuilderUtils.createXMLElement( composeElementName( elementName ), parent, text );
    }   //  createXMLElement()

    /**
     *  Creates an XML element for the given tag and adds it as child to the
     *  given document.<br>
     *  <br>The given element name is validated using the method that is
     *  provided by
     *  {@link XMLBuilderUtils#getElementNameValidator()}.
     *
     *  @param  elementName The element name.
     *  @param  parent  The document.
     *  @return The new XML element.
     */
    public default XMLElement createXMLElement( final String elementName, final XMLDocument parent )
    {
        return XMLBuilderUtils.createXMLElement( composeElementName( elementName ), parent );
    }   //  createXMLElement()

    /**
     *  Creates an XML element for the given tag and with the given text, and
     *  adds it as child to the given document.<br>
     *  <br>The given element name is validated using the method that is
     *  provided by
     *  {@link XMLBuilderUtils#getElementNameValidator()}.
     *
     *  @param  elementName The element name.
     *  @param  parent  The document.
     *  @param  text    The text for the new element.
     *  @return The new XML element.
     */
    public default XMLElement createXMLElement( final String elementName, final XMLDocument parent, final CharSequence text )
    {
        return XMLBuilderUtils.createXMLElement( composeElementName( elementName ), parent, text );
    }   //  createXMLElement()

    /**
     *  Returns the namespace this XML element factory was created with.
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the namespace.
     */
    public Optional<Namespace> getNamespace();

    /**
     *  Returns the prefix from the namespace this XML element factory was
     *  created with.
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the prefix.
     */
    public default Optional<String> getPrefix()
    {
        final var retValue = getNamespace().flatMap( Namespace::getPrefix );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  getPrefix()
}
//  interface XMLElementFactory

/*
 *  End of File
 */