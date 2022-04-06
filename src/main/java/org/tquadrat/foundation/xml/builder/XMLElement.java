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

package org.tquadrat.foundation.xml.builder;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.CommonConstants.XMLATTRIBUTE_Id;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.isNotEmpty;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.getNMTokenValidator;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.IllegalOperationException;
import org.tquadrat.foundation.xml.builder.internal.XMLElementImpl;
import org.tquadrat.foundation.xml.builder.spi.Element;

/**
 *  <p>{@summary The definition of an XMLElement.}</p>
 *  <p>The default implementation of the methods
 *  {@link #addCDATA(CharSequence)},
 *  {@link #addChild(XMLElement)},
 *  {@link #addComment(CharSequence)},
 *  {@link #addText(CharSequence)},
 *  {@link #setAttribute(String, CharSequence, Optional)},
 *  {@link #setNamespace(String)},
 *  {@link #setNamespace(URI)},
 *  {@link #setNamespace(String,String)},
 *  {@link #setNamespace(String,URI)},
 *  and
 *  {@link #setNamespace(Namespace)}
 *  will always throw an
 *  {@link IllegalOperationException};
 *  classes that implement this interface will have to provide appropriate
 *  implementations for these methods if they want to support the respective
 *  feature.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: XMLElement.java 1030 2022-04-06 13:42:02Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "ClassWithTooManyMethods" )
@ClassVersion( sourceVersion = "$Id: XMLElement.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public sealed interface XMLElement extends Element
    permits XMLElementImpl
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  The flags that are used to configure a new instance of
     *  {@code XMLElement} (respectively an instance of an implementation of
     *  this interface.
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: XMLElement.java 1030 2022-04-06 13:42:02Z tquadrat $
     *  @since 0.0.5
     *
     *  @UMLGraph.link
     */
    @SuppressWarnings( "NewClassNamingConvention" )
    @ClassVersion( sourceVersion = "$Id: XMLElement.java 1030 2022-04-06 13:42:02Z tquadrat $" )
    @API( status = STABLE, since = "0.1.0" )
    public static enum Flags
    {
        /**
         *  The element allows children.
         */
        ALLOWS_CHILDREN,

        /**
         *  The element allows text.
         */
        ALLOWS_TEXT,

        /**
         *  Only valid attributes can be added to the element (if not set, any
         *  attribut can be added).
         */
        VALIDATES_ATTRIBUTES,

        /**
         *  Only valid children can be added; this implies that
         *  {@link #ALLOWS_CHILDREN}
         *  is set; if the further is set, but this one not, any child can be
         *  added.
         */
        VALIDATES_CHILDREN
    }
    //  enum Flags

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An empty array of {@code XMLElement} objects.
     */
    public static final XMLElement [] EMPTY_XMLElement_ARRAY = new XMLElement [0];

    /**
     *  The indicator that values for an attribute should not be appended.
     *
     *  @see #setAttribute(String, CharSequence, Optional)
     */
    @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
    public static final Optional<? extends CharSequence> NO_APPEND = Optional.empty();

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  <p>{@summary Adds the provided {@code boolean} value as a {@code CDATA}
     *  element to this XML element.}</p>
     *  <p>To convert the boolean to text, the method
     *  {@link Boolean#toString(boolean)}
     *  is used.</p>
     *
     *  @param  flag    The value.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addCDATA( final boolean flag ) throws IllegalOperationException { return addCDATA( Boolean.toString( flag ) ); }

    /**
     *  <p>{@summary Adds the provided
     *  {@link Boolean}
     *  instance as a {@code CDATA} element to this XML element.}</p>
     *  <p>To convert the boolean to text, the method
     *  {@link Boolean#toString()}
     *  is used.</p>
     *
     *  @param  flag    The value.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addCDATA( final Boolean flag ) throws IllegalOperationException { return addCDATA( requireNonNullArgument( flag, "flag" ).toString() ); }

    /**
     *  <p>{@summary Adds the provided {@code char} value as a {@code CDATA}
     *  element to this XML element.}</p>
     *  <p>To convert the character to text, the method
     *  {@link Character#toString(char)}
     *  is used.</p>
     *
     *  @param  c   The character.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addCDATA( final char c ) throws IllegalOperationException { return addCDATA( Character.toString( c ) ); }

    /**
     *  <p>{@summary Adds the provided
     *  {@link Character}
     *  instance as a {@code CDATA} element to this XML element.}</p>
     *  <p>To convert the character to text, the method
     *  {@link Character#toString()}
     *  is used.</p>
     *
     *  @param  c   The character.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addCDATA( final Character c ) throws IllegalOperationException { return addCDATA( requireNonNullArgument( c, "c" ).toString() ); }

    /**
     *  Adds a {@code CDATA} element to this XML element.
     *
     *  @param  text    The text.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    public default XMLElement addCDATA( final CharSequence text ) throws IllegalOperationException
    {
        throw new IllegalOperationException( format( "No text allowed for element '%1$s'", getElementName() ) );
    }   //  addCDATA()

    /**
     *  <p>{@summary Adds the provided {@code double} value as a {@code CDATA}
     *  element to this XML element.}</p>
     *  <p>To convert the number to text, the method
     *  {@link Double#toString(double)}
     *  is used.</p>
     *
     *  @param  number  The number.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addCDATA( final double number ) throws IllegalOperationException { return addCDATA( Double.toString( number ) ); }

    /**
     *  <p>{@summary Adds the provided {@code enum} instance as a {@code CDATA}
     *  element to this XML element.}</p>
     *  <p>To convert the value to text, the method
     *  {@link Enum#name()}
     *  is used.</p>
     *
     *  @param  <E> The type of the {@code enum} value.
     *  @param  value   The value.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default <E extends Enum<E>> XMLElement addCDATA( final E value ) throws IllegalOperationException { return addCDATA( requireNonNullArgument( value, "value" ).name() ); }

    /**
     *  <p>{@summary Adds the provided
     *  {@link Instant}
     *  instance as a {@code CDATA} element to this XML element.}</p>
     *  <p>To convert the date to text, the method
     *  {@link Instant#toString()}
     *  is used.</p>
     *
     *  @param  date    The date.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addCDATA( final Instant date ) throws IllegalOperationException { return addCDATA( requireNonNullArgument( date, "date" ).toString() ); }

    /**
     *  <p>{@summary Adds the provided {@code int} value as a {@code CDATA}
     *  element to this XML element.}</p>
     *  <p>To convert the number to text, the method
     *  {@link Integer#toString(int)}
     *  is used.</p>
     *
     *  @param  number  The number.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addCDATA( final int number ) throws IllegalOperationException { return addCDATA( Integer.toString( number ) ); }

    /**
     *  <p>{@summary Adds the provided
     *  {@link LocalDate}
     *  instance as a {@code CDATA} element to this XML element.}</p>
     *  <p>To convert the date to text, the method
     *  {@link LocalDate#toString()}
     *  is used.</p>
     *
     *  @param  date    The date.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addCDATA( final LocalDate date ) throws IllegalOperationException { return addCDATA( requireNonNullArgument( date, "date" ).toString() ); }

    /**
     *  <p>{@summary Adds the provided
     *  {@link LocalDateTime}
     *  instance as a {@code CDATA} element to this XML element.}</p>
     *  <p>To convert the date to text, the method
     *  {@link LocalDateTime#toString()}
     *  is used.</p>
     *
     *  @param  date    The date.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addCDATA( final LocalDateTime date ) throws IllegalOperationException { return addCDATA( requireNonNullArgument( date, "date" ).toString() ); }

    /**
     *  <p>{@summary Adds the provided {@code long} value as a {@code CDATA}
     *  element to this XML element.}</p>
     *  <p>To convert the number to text, the method
     *  {@link Long#toString(long)}
     *  is used.</p>
     *
     *  @param  number  The number.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addCDATA( final long number ) throws IllegalOperationException { return addCDATA( Long.toString( number ) ); }

    /**
     *  <p>{@summary Adds the provided
     *  {@link Number}
     *  instance as a {@code CDATA} element to this XML element.}</p>
     *  <p>To convert the number to text, the method
     *  {@link Number#toString()}
     *  is used.</p>
     *
     *  @param  number  The number.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addCDATA( final Number number ) throws IllegalOperationException { return addCDATA( requireNonNullArgument( number, "number" ).toString() ); }

    /**
     *  <p>{@summary Adds the provided
     *  {@link ZonedDateTime}
     *  instance as a {@code CDATA} element to this XML element.}</p>
     *  <p>To convert the date to text, the method
     *  {@link ZonedDateTime#toString()}
     *  is used.</p>
     *
     *  @param  date    The date.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addCDATA( final ZonedDateTime date ) throws IllegalOperationException { return addCDATA( requireNonNullArgument( date, "date" ).toString() ); }

    /**
     *  Adds a child to this element.
     *
     *  @param  <E> The implementation type for the {@code children}.
     *  @param  child   The child to add.
     *  @return This instance.
     *  @throws IllegalArgumentException    The given child is not valid for
     *      this element.
     *  @throws IllegalOperationException   No children are allowed for this
     *      element.
     *  @throws IllegalStateException   The child has already a parent that is
     *      not this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default <E extends XMLElement> XMLElement addChild( final E child ) throws IllegalArgumentException, IllegalOperationException, IllegalStateException
    {
        throw new IllegalOperationException( format( "No children allowed for element '%1$s'", getElementName() ) );
    }   //  addChild()

    /**
     *  Adds a comment.
     *
     *  @param  comment The comment text.
     *  @return This instance.
     *  @throws IllegalOperationException    No comment allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addComment( final CharSequence comment ) throws IllegalOperationException
    {
        throw new IllegalOperationException( format( "No comment allowed for element '%1$s'", getElementName() ) );
    }   //  addComment()

    /**
     *  <p>{@summary Adds predefined markup.}</p>
     *  <p>The given markup will not be validated, it just may not be
     *  {@code null}. So the caller is responsible that it will be proper
     *  markup.</p>
     *  <p>As the markup may be formatted differently (or not formatted at
     *  all), the pretty printed output may be distorted when this is used.</p>
     *
     *  @param  markup  The predefined markup.
     *  @return This instance.
     *  @throws IllegalOperationException    No children are allowed for this
     *      element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addPredefinedMarkup( final CharSequence markup ) throws IllegalOperationException
    {
        throw new IllegalOperationException( format( "No children allowed for element '%1$s'", getElementName() ) );
    }   //  addPredefinedMarkup()

    /**
     *  <p>{@summary Adds the provided {@code boolean} value as text to this
     *  element.}</p>
     *  <p>To convert the boolean to text, the method
     *  {@link Boolean#toString(boolean)}
     *  is used.</p>
     *
     *  @param  flag    The value.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addText( final boolean flag ) throws IllegalOperationException { return addText( Boolean.toString( flag ) ); }

    /**
     *  <p>{@summary Adds the provided
     *  {@link Boolean}
     *  instance as text to this element.}</p>
     *  <p>To convert the boolean to text, the method
     *  {@link Boolean#toString()}
     *  is used.</p>
     *
     *  @param  flag    The value.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addText( final Boolean flag ) throws IllegalOperationException { return addText( requireNonNullArgument( flag, "flag" ).toString() ); }

    /**
     *  <p>{@summary Adds the provided {@code char} value as text to this
     *  element.}</p>
     *  <p>To convert the character to text, the method
     *  {@link Character#toString(char)}
     *  is used.</p>
     *
     *  @param  c   The character.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addText( final char c ) throws IllegalOperationException { return addText( Character.toString( c ) ); }

    /**
     *  <p>{@summary Adds the provided
     *  {@link Character}
     *  instance as text to this element.}</p>
     *  <p>To convert the character to text, the method
     *  {@link Character#toString()}
     *  is used.</p>
     *
     *  @param  c   The character.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addText( final Character c ) throws IllegalOperationException { return addText( requireNonNullArgument( c, "c" ).toString() ); }

    /**
     *  Adds text to this element. Special characters will be escaped
     *  according to the rules for the respective SGML flavour.
     *
     *  @param  text    The text.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    public default XMLElement addText( final CharSequence text ) throws IllegalOperationException
    {
        throw new IllegalOperationException( format( "No text allowed for element '%1$s'", getElementName() ) );
    }   //  addText()

    /**
     *  <p>{@summary Adds the provided {@code double} value as text to this
     *  element.}</p>
     *  <p>To convert the number to text, the method
     *  {@link Double#toString(double)}
     *  is used.</p>
     *
     *  @param  number  The number.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addText( final double number ) throws IllegalOperationException { return addText( Double.toString( number ) ); }

    /**
     *  <p>{@summary Adds the provided {@code enum} instance as text element to this
     *  element.}</p>
     *  <p>To convert the value to text, the method
     *  {@link Enum#name()}
     *  is used.</p>
     *
     *  @param  <E> The type of the {@code enum} value.
     *  @param  value   The value.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default <E extends Enum<E>> XMLElement addText( final E value ) throws IllegalOperationException { return addText( requireNonNullArgument( value, "value" ).name() ); }

    /**
     *  <p>{@summary Adds the provided
     *  {@link Instant}
     *  instance as text to this element.}</p>
     *  <p>To convert the date to text, the method
     *  {@link Instant#toString()}
     *  is used.</p>
     *
     *  @param  date    The date.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addText( final Instant date ) throws IllegalOperationException { return addText( requireNonNullArgument( date, "date" ).toString() ); }

    /**
     *  <p>{@summary Adds the provided {@code int} value as text to this
     *  element.}</p>
     *  <p>To convert the number to text, the method
     *  {@link Integer#toString(int)}
     *  is used.</p>
     *
     *  @param  number  The number.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addText( final int number ) throws IllegalOperationException { return addText( Integer.toString( number ) ); }

    /**
     *  <p>{@summary Adds the provided
     *  {@link LocalDate}
     *  instance as text to this element.}</p>
     *  <p>To convert the date to text, the method
     *  {@link LocalDate#toString()}
     *  is used.</p>
     *
     *  @param  date    The date.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addText( final LocalDate date ) throws IllegalOperationException { return addText( requireNonNullArgument( date, "date" ).toString() ); }

    /**
     *  <p>{@summary Adds the provided
     *  {@link LocalDateTime}
     *  instance as text to this element.}</p>
     *  <p>To convert the date to text, the method
     *  {@link LocalDateTime#toString()}
     *  is used.</p>
     *
     *  @param  date    The date.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addText( final LocalDateTime date ) throws IllegalOperationException { return addText( requireNonNullArgument( date, "date" ).toString() ); }

    /**
     *  <p>{@summary Adds the provided {@code long} value as text to this
     *  element.}</p>
     *  <p>To convert the number to text, the method
     *  {@link Long#toString(long)}
     *  is used.</p>
     *
     *  @param  number  The number.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addText( final long number ) throws IllegalOperationException { return addText( Long.toString( number ) ); }

    /**
     *  <p>{@summary Adds the provided
     *  {@link Number}
     *  instance as text to this element.}</p>
     *  <p>To convert the number to text, the method
     *  {@link Number#toString()}
     *  is used.</p>
     *
     *  @param  number  The number.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addText( final Number number ) throws IllegalOperationException { return addText( requireNonNullArgument( number, "number" ).toString() ); }

    /**
     *  <p>{@summary Adds the provided
     *  {@link ZonedDateTime}
     *  instance as text to this element.}</p>
     *  <p>To convert the date to text, the method
     *  {@link ZonedDateTime#toString()}
     *  is used.</p>
     *
     *  @param  date    The date.
     *  @return This instance.
     *  @throws IllegalOperationException    No text allowed for this element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement addText( final ZonedDateTime date ) throws IllegalOperationException { return addText( requireNonNullArgument( date, "date" ).toString() ); }

    /**
     *  Returns the flags for this element.
     *
     *  @return The flags.
     *
     *  @see Flags
     */
    public Set<Flags> getFlags();

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The method uses
     *  {@link Boolean#toString(boolean)}
     *  to convert the provided flag to a {@code String}.</p>
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  flag    The attribute's value.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element
     *  @throws IllegalOperationException   No attributes are allowed for this
     *      element.
     */
    public default XMLElement setAttribute( final String name, final boolean flag ) throws IllegalArgumentException, IllegalOperationException
    {
        return setAttribute( name, Boolean.toString( flag ) );
    }   //  setAttribute()

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The method uses
     *  {@link Boolean#toString()}
     *  to convert the provided flag to a {@code String}.</p>
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  flag    The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element
     *  @throws IllegalOperationException   No attributes are allowed for this
     *      element.
     */
    public default XMLElement setAttribute( final String name, final Boolean flag ) throws IllegalArgumentException, IllegalOperationException
    {
        return setAttribute( name, nonNull( flag ) ? Boolean.toString( flag ) : null );
    }   //  setAttribute()

    /**
     *  Sets the attribute with the given name.
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  value   The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element
     *  @throws IllegalOperationException   No attributes are allowed for this
     *      element.
     */
    public default XMLElement setAttribute( final String name, final CharSequence value ) throws IllegalArgumentException, IllegalOperationException
    {
        return setAttribute( requireNotEmptyArgument( name, "name" ), value, NO_APPEND );
    }   //  setAttribute()

    /**
     *  Sets the attribute with the given name.
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  value   The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @param  append  If not
     *      {@linkplain Optional#empty() empty}
     *      ({@link #NO_APPEND}),
     *      the new value will be appended to an already existing one, and the
     *      provided char sequence is used as the separator.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element
     *  @throws IllegalOperationException   No attributes are allowed for this
     *      element.
     */
    @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
    public default XMLElement setAttribute( final String name, final CharSequence value, final Optional<? extends CharSequence> append ) throws IllegalArgumentException, IllegalOperationException
    {
        throw new IllegalArgumentException( format( "No attributes allowed for element '%1$s'", getElementName() ) );
    }   //  setAttribute()

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The method uses
     *  {@link Double#toString(double)}
     *  to convert the provided number to a {@code String}.</p>
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  number   The attribute's value.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element
     *  @throws IllegalOperationException   No attributes are allowed for this
     *      element.
     */
    public default XMLElement setAttribute( final String name, final double number ) throws IllegalArgumentException, IllegalOperationException
    {
        return setAttribute( name, Double.toString( number ) );
    }   //  setAttribute()

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The method uses
     *  {@link Enum#name()}
     *  to convert the provided value to a {@code String}.</p>
     *
     *  @param  <E> The concrete enum type of {@code value}.
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  enumValue   The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element
     *  @throws IllegalOperationException   No attributes are allowed for this
     *      element.
     */
    public default <E extends Enum<E>> XMLElement setAttribute( final String name, final E enumValue ) throws IllegalArgumentException, IllegalOperationException
    {
        return setAttribute( name, nonNull( enumValue ) ? enumValue.name() : null );
    }   //  setAttribute()

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The method uses
     *  {@link Instant#toString()}
     *  to convert the provided number to a {@code String}.</p>
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  date    The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element
     *  @throws IllegalOperationException   No attributes are allowed for this
     *      element.
     */
    public default XMLElement setAttribute( final String name, final Instant date ) throws IllegalArgumentException, IllegalOperationException
    {
        return setAttribute( name, nonNull( date ) ? date.toString() : null );
    }   //  setAttribute()

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The method uses
     *  {@link Integer#toString(int)}
     *  to convert the provided number to a {@code String}.</p>
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  number  The attribute's value.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element
     *  @throws IllegalOperationException   No attributes are allowed for this
     *      element.
     */
    public default XMLElement setAttribute( final String name, final int number ) throws IllegalArgumentException, IllegalOperationException
    {
        return setAttribute( name, Integer.toString( number ) );
    }   //  setAttribute()

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The method uses
     *  {@link LocalDate#toString()}
     *  to convert the provided number to a {@code String}.</p>
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  date    The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element
     *  @throws IllegalOperationException   No attributes are allowed for this
     *      element.
     */
    public default XMLElement setAttribute( final String name, final LocalDate date ) throws IllegalArgumentException, IllegalOperationException
    {
        return setAttribute( name, nonNull( date ) ? date.toString() : null );
    }   //  setAttribute()

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The method uses
     *  {@link LocalDateTime#toString()}
     *  to convert the provided number to a {@code String}.</p>
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  date    The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element
     *  @throws IllegalOperationException   No attributes are allowed for this
     *      element.
     */
    public default XMLElement setAttribute( final String name, final LocalDateTime date ) throws IllegalArgumentException, IllegalOperationException
    {
        return setAttribute( name, nonNull( date ) ? date.toString() : null );
    }   //  setAttribute()

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The method uses
     *  {@link Long#toString(long)}
     *  to convert the provided number to a {@code String}.</p>
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  number   The attribute's value.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element
     *  @throws IllegalOperationException   No attributes are allowed for this
     *      element.
     */
    public default XMLElement setAttribute( final String name, final long number ) throws IllegalArgumentException, IllegalOperationException
    {
        return setAttribute( name, Long.toString( number ) );
    }   //  setAttribute()

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The method uses
     *  {@link Number#toString()}
     *  to convert the provided number to a {@code String}.</p>
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  number   The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element
     *  @throws IllegalOperationException   No attributes are allowed for this
     *      element.
     */
    public default XMLElement setAttribute( final String name, final Number number ) throws IllegalArgumentException, IllegalOperationException
    {
        return setAttribute( name, nonNull( number ) ? number.toString() : null );
    }   //  setAttribute()

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The method uses
     *  {@link ZonedDateTime#toString()}
     *  to convert the provided number to a {@code String}.</p>
     *
     *  @param  name    The name of the attribute; the name is case-sensitive.
     *  @param  date    The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element
     *  @throws IllegalOperationException   No attributes are allowed for this
     *      element.
     */
    public default XMLElement setAttribute( final String name, final ZonedDateTime date ) throws IllegalArgumentException, IllegalOperationException
    {
        return setAttribute( name, nonNull( date ) ? date.toString() : null );
    }   //  setAttribute()

    /**
     *  <p>{@summary Sets the attribute with the given name if the provided
     *  value is not empty.}</p>
     *  <p>The method uses
     *  {@link org.tquadrat.foundation.util.StringUtils#isNotEmpty(CharSequence)}
     *  to test if the given value is empty.</p>
     *
     *  @param  name    The name of the attribute.
     *  @param  value   The value for the attribute; can be {@code null}.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element
     *  @throws IllegalOperationException   No attributes are allowed for this
     *      element.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default XMLElement setAttributeIfNotEmpty( final String name, final CharSequence value ) throws IllegalArgumentException, IllegalOperationException
    {
        if( isNotEmpty( value ) ) setAttribute( name, value );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setAttributeIfNotEmpty()

    /**
     *  Sets the attribute with the given name if the provided value is not
     *  empty.
     *
     *  @param  name    The name of the attribute.
     *  @param  optional   The value for the attribute.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element
     *  @throws IllegalOperationException   No attributes are allowed for this
     *      element.
     */
    @SuppressWarnings( {"OptionalUsedAsFieldOrParameterType", "UnusedReturnValue"} )
    public default XMLElement setAttributeIfNotEmpty( final String name, final Optional<? extends CharSequence> optional ) throws IllegalArgumentException, IllegalOperationException
    {
        requireNonNullArgument( optional, "optional" ).ifPresent( value -> setAttribute( name, value ) );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setAttributeIfNotEmpty()

    /**
     *  <p>{@summary Sets the id for the element.}</p>
     *  <p>The value will be validated using the method that is provided by a
     *  call to
     *  {@link XMLBuilderUtils#getNMTokenValidator()}.</p>
     *
     *  @param  id  The id.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element or the value is not a valid NMToken.
     *  @throws IllegalOperationException   No attributes are allowed for this
     *      element.
     *
     *  @see org.tquadrat.foundation.lang.CommonConstants#XMLATTRIBUTE_Id
     */
    public default XMLElement setId( final String id ) throws IllegalArgumentException, IllegalOperationException
    {
        if( !getNMTokenValidator().test( requireNotEmptyArgument( id, "id" ) ) ) throw new IllegalArgumentException( format( "Invalid id: %s", id ) );
        final var retValue = setAttribute( XMLATTRIBUTE_Id, id );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  setId()

    /**
     *  Sets the given namespace.
     *
     *  @param  identifier  The namespace identifier.
     *  @return This instance.
     *  @throws IllegalOperationException    Namespaces are not allowed for this
     *      element.
     *  @throws URISyntaxException  The provided URI String is invalid.
     */
    public default XMLElement setNamespace( final String identifier ) throws IllegalOperationException, URISyntaxException
    {
        final var namespace = new Namespace( identifier );
        final var retValue = setNamespace( namespace );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  setNamespace()

    /**
     *  Sets the given namespace.
     *
     *  @param  identifier  The namespace identifier.
     *  @return This instance.
     *  @throws IllegalOperationException    Namespaces are not allowed for this
     *      element.
     */
    public default XMLElement setNamespace( final URI identifier ) throws IllegalOperationException
    {
        final var namespace = new Namespace( identifier );
        final var retValue = setNamespace( namespace );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  setNamespace()

    /**
     *  <p>{@summary Sets the given namespace.}</p>
     *  <p>The given prefix is validated using the method that is
     *  provided by
     *  {@link org.tquadrat.foundation.xml.builder.XMLBuilderUtils#getPrefixValidator()}.</p>
     *
     *  @param  prefix  The namespace prefix.
     *  @param  identifier  The namespace identifier.
     *  @return This instance.
     *  @throws IllegalOperationException    Namespaces are not allowed for this
     *      element.
     *  @throws URISyntaxException  The provided URI String is invalid.
     */
    public default XMLElement setNamespace( final String prefix, final String identifier ) throws IllegalOperationException, URISyntaxException
    {
        final var namespace = new Namespace( prefix, identifier );
        final var retValue = setNamespace( namespace );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  setNamespace()

    /**
     *  <p>{@summary Sets the given namespace.}</p>
     *  <p>The given prefix is validated using the method that is
     *  provided by
     *  {@link org.tquadrat.foundation.xml.builder.XMLBuilderUtils#getPrefixValidator()}.</p>
     *
     *  @param  prefix  The namespace prefix.
     *  @param  identifier  The namespace identifier.
     *  @return This instance.
     *  @throws IllegalOperationException    Namespaces are not allowed for this
     *      element.
     */
    public default XMLElement setNamespace( final String prefix, final URI identifier ) throws IllegalOperationException
    {
        final var namespace = new Namespace( prefix, identifier );
        final var retValue = setNamespace( namespace );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  setNamespace()

    /**
     *  Sets the given namespace.
     *
     *  @param  namespace   The namespace.
     *  @return This instance.
     *  @throws IllegalOperationException    Namespaces are not allowed for this
     *      element.
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    public default XMLElement setNamespace( final Namespace namespace ) throws IllegalOperationException
    {
        throw new IllegalOperationException( format( "No namespace allowed for element '%1$s'", getElementName() ) );
    }   //  setNamespace()

    /**
     *  {@inheritDoc}
     */
    @Override
    public <E extends Element> void setParent( final E parent );
}
//  interface XMLElement

/*
 *  End of File
 */