/*
 * ============================================================================
 * Copyright © 2002-2023 by Thomas Thrien.
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

package org.tquadrat.foundation.xml.builder;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.getNMTokenValidator;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.builder.internal.XMLDocumentImpl;
import org.tquadrat.foundation.xml.builder.spi.Document;

/**
 *  The definition for an XML document.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: XMLDocument.java 1071 2023-09-30 01:49:32Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( {"ClassWithTooManyMethods"} )
@ClassVersion( sourceVersion = "$Id: XMLDocument.java 1071 2023-09-30 01:49:32Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public sealed interface XMLDocument extends Document<XMLElement>
    permits XMLDocumentImpl
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Adds a child to the root element of this document.
     *
     *  @param  child    The child to add.
     *  @return This instance.
     *  @throws IllegalArgumentException    The child is not allowed for the
     *      root element of this document, or the root element does not allow
     *      adding children at all.
     *  @throws IllegalStateException   The child has already a parent that is
     *      not the root XML element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLDocument addChild( final XMLElement child ) throws IllegalArgumentException, IllegalStateException
    {
        getRootElement().addChild( child );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  addChild()

    /**
     *  Adds a comment to the root element of this document.
     *
     *  @param  comment The comment to add.
     *  @return This instance.
     *  @throws IllegalArgumentException    The root element does not allow
     *      adding comments.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLDocument addComment( final CharSequence comment ) throws IllegalArgumentException
    {
        getRootElement().addComment( comment );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  //  addComment()

    /**
     *  Adds a comment to the document itself.
     *
     *  @param  comment The comment to add.
     *  @return This instance.
     *  @throws IllegalArgumentException    The document does not allow adding
     *      comments.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLDocument addDocumentComment( final CharSequence comment ) throws IllegalArgumentException
    {
        throw new IllegalArgumentException( "No comment allowed for this document" );
    }   //  addDocumentComment()

    /**
     *  Adds predefined XML markup to the root element of this document. The
     *  given markup will not be validated, it just may not be {@code null}. So
     *  the caller is responsible that it will be proper XML.<br>
     *  <br>As the markup may be formatted differently (or not formatted at
     *  all), the pretty printed output may be distorted when this is used.
     *
     *  @param  markup  The XML markup.
     *  @return This instance.
     *  @throws IllegalArgumentException    The root element does not allow
     *      adding children at all.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLDocument addPredefinedMarkup( final CharSequence markup ) throws IllegalArgumentException
    {
        getRootElement().addPredefinedMarkup( markup );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  addPredefinedMarkup()

    /**
     *  Adds a processing instruction to this document.
     *
     *  @param  processingInstruction   The procession instruction to add.
     *  @return This instance.
     *  @throws IllegalArgumentException    This document does not allow adding
     *      processing instructions.
     *  @throws IllegalStateException   The processing instruction has already
     *      a parent.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLDocument addProcessingInstruction( final ProcessingInstruction processingInstruction ) throws IllegalArgumentException, IllegalStateException
    {
        throw new IllegalArgumentException( "No processing instructions allowed for this document" );
    }   //  addProcessingInstruction()

    /**
     *  Sets the attribute with the given name to the root element of this
     *  document.<br>
     *  <br>The method uses
     *  {@link Boolean#toString(boolean)}
     *  to convert the provided flag to a {@code String}.
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  flag    The attribute's value.
     *  @return This instance.
     *  @throws IllegalArgumentException    The attribute is not allowed for
     *      the root element, or the root element does not allow attributes at
     *      all.
     */
    public default XMLDocument setAttribute( final String name, final boolean flag ) throws IllegalArgumentException
    {
        getRootElement().setAttribute( name, flag );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setAttribute()

    /**
     *  Sets the attribute with the given name to the root element of this
     *  document.<br>
     *  <br>The method uses
     *  {@link Boolean#toString()}
     *  to convert the provided flag to a {@code String}.
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  flag    The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    The attribute is not allowed for
     *      the root element, or the root element does not allow attributes at
     *      all.
     */
    public default XMLDocument setAttribute( final String name, final Boolean flag ) throws IllegalArgumentException
    {
        getRootElement().setAttribute( name, flag );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setAttribute()

    /**
     *  Sets the attribute with the given name to the root element of this
     *  document.
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  value   The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    The attribute is not allowed for
     *      the root element, or the root element does not allow attributes at
     *      all.
     */
    public default XMLDocument setAttribute( final String name, final CharSequence value ) throws IllegalArgumentException
    {
        getRootElement().setAttribute( name, value );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setAttribute()

    /**
     *  Sets the attribute with the given name to the root element of this
     *  document.
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  value   The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @param  append  If not
     *      {@linkplain Optional#empty() empty}, the new value will be appended
     *      on an already existing one, and this sequence is used as the
     *      separator.
     *  @return This instance.
     *  @throws IllegalArgumentException    The attribute is not allowed for
     *      the root element, or the root element does not allow attributes at
     *      all.
     */
    @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
    public default XMLDocument setAttribute( final String name, final CharSequence value, final Optional<? extends CharSequence> append ) throws IllegalArgumentException
    {
        getRootElement().setAttribute( name, value, append );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setAttribute()

    /**
     *  Sets the attribute with the given name to the root element of this
     *  document.<br>
     *  <br>The method uses
     *  {@link Double#toString(double)}
     *  to convert the provided number to a {@code String}.
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  number   The attribute's value.
     *  @return This instance.
     *  @throws IllegalArgumentException    The attribute is not allowed for
     *      the root element, or the root element does not allow attributes at
     *      all.
     */
    public default XMLDocument setAttribute( final String name, final double number ) throws IllegalArgumentException
    {
        getRootElement().setAttribute( name, number );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setAttribute()

    /**
     *  Sets the attribute with the given name to the root element of this
     *  document.<br>
     *  <br>The method uses
     *  {@link Enum#name()}
     *  to convert the provided value to a {@code String}.
     *
     *  @param  <E> The concrete enum type of {@code value}.
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  enumValue   The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    The attribute is not allowed for
     *      the root element, or the root element does not allow attributes at
     *      all.
     */
    public default <E extends Enum<E>> XMLDocument setAttribute( final String name, final E enumValue ) throws IllegalArgumentException
    {
        getRootElement().setAttribute( name, enumValue );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setAttribute()

    /**
     *  Sets the attribute with the given name to the root element of this
     *  document.<br>
     *  <br>The method uses
     *  {@link Instant#toString()}
     *  to convert the provided number to a {@code String}.
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  date    The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    The attribute is not allowed for
     *      the root element, or the root element does not allow attributes at
     *      all.
     */
    public default XMLDocument setAttribute( final String name, final Instant date ) throws IllegalArgumentException
    {
        getRootElement().setAttribute( name, date );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setAttribute()

    /**
     *  Sets the attribute with the given name to the root element of this
     *  document.<br>
     *  <br>The method uses
     *  {@link Integer#toString(int)}
     *  to convert the provided number to a {@code String}.
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  number  The attribute's value.
     *  @return This instance.
     *  @throws IllegalArgumentException    The attribute is not allowed for
     *      the root element, or the root element does not allow attributes at
     *      all.
     */
    public default XMLDocument setAttribute( final String name, final int number ) throws IllegalArgumentException
    {
        getRootElement().setAttribute( name, number );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setAttribute()

    /**
     *  Sets the attribute with the given name to the root element of this
     *  document.<br>
     *  <br>The method uses
     *  {@link LocalDate#toString()}
     *  to convert the provided number to a {@code String}.
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  date    The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    The attribute is not allowed for
     *      the root element, or the root element does not allow attributes at
     *      all.
     */
    public default XMLDocument setAttribute( final String name, final LocalDate date ) throws IllegalArgumentException
    {
        getRootElement().setAttribute( name, date );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setAttribute()

    /**
     *  Sets the attribute with the given name to the root element of this
     *  document.<br>
     *  <br>The method uses
     *  {@link LocalDateTime#toString()}
     *  to convert the provided number to a {@code String}.
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  date    The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    The attribute is not allowed for
     *      the root element, or the root element does not allow attributes at
     *      all.
     */
    public default XMLDocument setAttribute( final String name, final LocalDateTime date ) throws IllegalArgumentException
    {
        getRootElement().setAttribute( name, date );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setAttribute()

    /**
     *  Sets the attribute with the given name to the root element of this
     *  document.<br>
     *  <br>The method uses
     *  {@link Long#toString(long)}
     *  to convert the provided number to a {@code String}.
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  number   The attribute's value.
     *  @return This instance.
     *  @throws IllegalArgumentException    The attribute is not allowed for
     *      the root element, or the root element does not allow attributes at
     *      all.
     */
    public default XMLDocument setAttribute( final String name, final long number ) throws IllegalArgumentException
    {
        getRootElement().setAttribute( name, number );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setAttribute()

    /**
     *  Sets the attribute with the given name to the root element of this
     *  document.<br>
     *  <br>The method uses
     *  {@link Number#toString()}
     *  to convert the provided number to a {@code String}.
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  number   The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    The attribute is not allowed for
     *      the root element, or the root element does not allow attributes at
     *      all.
     */
    public default XMLDocument setAttribute( final String name, final Number number ) throws IllegalArgumentException
    {
        getRootElement().setAttribute( name, number );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setAttribute()

    /**
     *  Sets the attribute with the given name to the root element of this
     *  document.<br>
     *  <br>The method uses
     *  {@link ZonedDateTime#toString()}
     *  to convert the provided number to a {@code String}.
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  date    The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    The attribute is not allowed for
     *      the root element, or the root element does not allow attributes at
     *      all.
     */
    public default XMLDocument setAttribute( final String name, final ZonedDateTime date ) throws IllegalArgumentException
    {
        getRootElement().setAttribute( name, date );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setAttribute()

    /**
     *  Sets the attribute with the given name to the root element of this
     *  document if the provided value is not empty.<br>
     *  <br>The method uses
     *  {@link org.tquadrat.foundation.util.StringUtils#isNotEmpty(CharSequence)}
     *  to test if the given value is empty.
     *
     *  @param  name    The name of the attribute.
     *  @param  value   The value for the attribute; can be {@code null}.
     *  @return This instance.
     *  @throws IllegalArgumentException    The attribute is not allowed for
     *      the root element, or the root element does not allow attributes at
     *      all.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLDocument setAttributeIfNotEmpty( final String name, final CharSequence value ) throws IllegalArgumentException
    {
        getRootElement().setAttributeIfNotEmpty( name, value );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setAttributeIfNotEmpty()

    /**
     *  Sets the attribute with the given name to the root element of this
     *  document if the provided value is not empty.
     *
     *  @param  name    The name of the attribute.
     *  @param  optional   The value for the attribute.
     *  @return This instance.
     *  @throws IllegalArgumentException    The attribute is not allowed for
     *      the root element, or the root element does not allow attributes at
     *      all.
     */
    @SuppressWarnings( {"UnusedReturnValue", "OptionalUsedAsFieldOrParameterType"} )
    public default XMLDocument setAttributeIfNotEmpty( final String name, final Optional<? extends CharSequence> optional ) throws IllegalArgumentException
    {
        getRootElement().setAttributeIfNotEmpty( name, optional );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setAttributeIfNotEmpty()

    /**
     *  Sets the id for the root element of this document.<br>
     *  <br>The value will be validated using the method that is provided by a
     *  call to
     *  {@link XMLBuilderUtils#getNMTokenValidator()}.
     *
     *  @param  id  The id.
     *  @return This instance.
     *  @throws IllegalArgumentException    The attribute is not allowed for
     *      the root element, or the root element does not allow attributes at
     *      all.
     *
     *  @see org.tquadrat.foundation.lang.CommonConstants#XMLATTRIBUTE_Id
     */
    public default XMLDocument setId( final String id ) throws IllegalArgumentException
    {
        if( !getNMTokenValidator().test( requireNotEmptyArgument( id, "id" ) ) ) throw new IllegalArgumentException( "Invalid id: %s".formatted( id ) );
        getRootElement().setId( id );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setId()

    /**
     *  Sets the given namespace to the root element of this document.
     *
     *  @param  identifier  The namespace identifier.
     *  @return This instance.
     *  @throws IllegalArgumentException    Namespaces are not allowed for this
     *      element.
     *  @throws URISyntaxException  The provided URI String is invalid.
     */
    public default XMLDocument setNamespace( final String identifier ) throws IllegalArgumentException, URISyntaxException
    {
        getRootElement().setNamespace( new Namespace( identifier ) );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setNamespace()

    /**
     *  Sets the given namespace to the root element of this document.
     *
     *  @param  identifier  The namespace identifier.
     *  @return This instance.
     *  @throws IllegalArgumentException    Namespaces are not allowed for this
     *      element.
     */
    public default XMLDocument setNamespace( final URI identifier ) throws IllegalArgumentException
    {
        getRootElement().setNamespace( new Namespace( identifier ) );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setNamespace()

    /**
     *  Sets the given namespace to the root element of this document.<br>
     *  <br>The given prefix is validated using the method that is
     *  provided by
     *  {@link XMLBuilderUtils#getPrefixValidator()}.
     *
     *  @param  prefix  The namespace prefix.
     *  @param  identifier  The namespace identifier.
     *  @return This instance.
     *  @throws IllegalArgumentException    Namespaces are not allowed for this
     *      element.
     *  @throws URISyntaxException  The provided URI String is invalid.
     */
    public default XMLDocument setNamespace( final String prefix, final String identifier ) throws IllegalArgumentException, URISyntaxException
    {
        getRootElement().setNamespace( new Namespace( prefix, identifier ) );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setNamespace()

    /**
     *  Sets the given namespace to the root element of this document.<br>
     *  <br>The given prefix is validated using the method that is
     *  provided by
     *  {@link XMLBuilderUtils#getPrefixValidator()}.
     *
     *  @param  prefix  The namespace prefix.
     *  @param  identifier  The namespace identifier.
     *  @return This instance.
     *  @throws IllegalArgumentException    Namespaces are not allowed for this
     *      element.
     */
    public default XMLDocument setNamespace( final String prefix, final URI identifier ) throws IllegalArgumentException
    {
        getRootElement().setNamespace( new Namespace( prefix, identifier ) );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setNamespace()

    /**
     *  Sets the given namespace to the root element of this document.
     *
     *  @param  namespace   The namespace.
     *  @return This instance.
     *  @throws IllegalArgumentException    Namespaces are not allowed for this
     *      element.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public default XMLDocument setNamespace( final Namespace namespace ) throws IllegalArgumentException
    {
        getRootElement().setNamespace( namespace );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setNamespace()
}
//  interface XMLDocument

/*
 *  End of File
 */