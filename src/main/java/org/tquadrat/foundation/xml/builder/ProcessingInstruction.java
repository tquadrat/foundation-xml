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

package org.tquadrat.foundation.xml.builder;

import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.StringUtils.isNotEmpty;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.builder.internal.ProcessingInstructionImpl;
import org.tquadrat.foundation.xml.builder.spi.Element;

/**
 *  <p>{@summary The definition for a processing instruction.}</p>
 *  <p>According to the specification, an XML processing instruction have this
 *  general structure</p>
 *  <pre><code>&lt;?<i>name</i> <i>data</i> ?&gt;</code></pre>
 *  <p>with <i>{@code data}</i> being arbitrary text as defined by the target
 *  processor that responds to the respective processing instruction.</p>
 *  <p>But in many cases, this <i>{@code data}</i> will be structured like
 *  regular XML attributes.</p>
 *  <p>Therefore we provide both API: with
 *  {@link #addData(CharSequence)}
 *  plain text can be added, with the various
 *  {@link #setAttribute(String, CharSequence) setAttribute()}
 *  methods the data will be formatted as attributes.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ProcessingInstruction.java 840 2021-01-10 21:37:03Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "preview" )
@ClassVersion( sourceVersion = "$Id: ProcessingInstruction.java 840 2021-01-10 21:37:03Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public sealed interface ProcessingInstruction extends Element
    permits ProcessingInstructionImpl
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Adds data to the processing instruction.
     *
     *  @param  data    The data to add.
     *  @return This instance.
     */
    public ProcessingInstruction addData( final CharSequence data );

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The method uses
     *  {@link Boolean#toString(boolean)}
     *  to convert the provided flag to a {@code String}.</p>
     *
     *  @param  name    The name of the attribute; the name is case sensitive.
     *  @param  flag    The attribute's value.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element, or no attributes are allowed at all.
     */
    public default ProcessingInstruction setAttribute( final String name, final boolean flag ) throws IllegalArgumentException
    {
        return setAttribute( name, Boolean.toString( flag ) );
    }   //  setAttribute()

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The method uses
     *  {@link Boolean#toString()}
     *  to convert the provided flag to a {@code String}.</p>
     *
     *  @param  name    The name of the attribute; the name is case sensitive.
     *  @param  flag    The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element, or no attributes are allowed at all.
     */
    public default ProcessingInstruction setAttribute( final String name, final Boolean flag ) throws IllegalArgumentException
    {
        return setAttribute( name, nonNull( flag ) ? Boolean.toString( flag ) : null );
    }   //  setAttribute()

    /**
     *  Sets the attribute with the given name.
     *
     *  @param  name    The name of the attribute; the name is case sensitive.
     *  @param  value   The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element, or no attributes are allowed at all.
     */
    public default ProcessingInstruction setAttribute( final String name, final CharSequence value ) throws IllegalArgumentException
    {
        return setAttribute( requireNotEmptyArgument( name, "name" ), value, Optional.empty() );
    }   //  setAttribute()

    /**
     *  Sets the attribute with the given name.
     *
     *  @param  name    The name of the attribute; the name is case sensitive.
     *  @param  value   The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @param  append  If not
     *      {@linkplain Optional#empty() empty}, the new value will be appended
     *      on an already existing one, and this sequence is used as the
     *      separator.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element.
     */
    @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
    public ProcessingInstruction setAttribute( final String name, final CharSequence value, final Optional<? extends CharSequence> append ) throws IllegalArgumentException;

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The method uses
     *  {@link Double#toString(double)}
     *  to convert the provided number to a {@code String}.</p>
     *
     *  @param  name    The name of the attribute; the name is case sensitive.
     *  @param  number   The attribute's value.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element, or no attributes are allowed at all.
     */
    public default ProcessingInstruction setAttribute( final String name, final double number ) throws IllegalArgumentException
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
     *  @param  name    The name of the attribute; the name is case sensitive.
     *  @param  enumValue   The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element, or no attributes are allowed at all.
     */
    public default <E extends Enum<E>> ProcessingInstruction setAttribute( final String name, final E enumValue ) throws IllegalArgumentException
    {
        return setAttribute( name, nonNull( enumValue ) ? enumValue.name() : null );
    }   //  setAttribute()

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The method uses
     *  {@link Instant#toString()}
     *  to convert the provided number to a {@code String}.</p>
     *
     *  @param  name    The name of the attribute; the name is case sensitive.
     *  @param  date    The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element, or no attributes are allowed at all.
     */
    public default ProcessingInstruction setAttribute( final String name, final Instant date ) throws IllegalArgumentException
    {
        return setAttribute( name, nonNull( date ) ? date.toString() : null );
    }   //  setAttribute()

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The method uses
     *  {@link Integer#toString(int)}
     *  to convert the provided number to a {@code String}.</p>
     *
     *  @param  name    The name of the attribute; the name is case sensitive.
     *  @param  number  The attribute's value.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element, or no attributes are allowed at all.
     */
    public default ProcessingInstruction setAttribute( final String name, final int number ) throws IllegalArgumentException
    {
        return setAttribute( name, Integer.toString( number ) );
    }   //  setAttribute()

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The method uses
     *  {@link LocalDate#toString()}
     *  to convert the provided number to a {@code String}.</p>
     *
     *  @param  name    The name of the attribute; the name is case sensitive.
     *  @param  date    The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element, or no attributes are allowed at all.
     */
    public default ProcessingInstruction setAttribute( final String name, final LocalDate date ) throws IllegalArgumentException
    {
        return setAttribute( name, nonNull( date ) ? date.toString() : null );
    }   //  setAttribute()

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The method uses
     *  {@link LocalDateTime#toString()}
     *  to convert the provided number to a {@code String}.</p>
     *
     *  @param  name    The name of the attribute; the name is case sensitive.
     *  @param  date    The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element, or no attributes are allowed at all.
     */
    public default ProcessingInstruction setAttribute( final String name, final LocalDateTime date ) throws IllegalArgumentException
    {
        return setAttribute( name, nonNull( date ) ? date.toString() : null );
    }   //  setAttribute()

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The method uses
     *  {@link Long#toString(long)}
     *  to convert the provided number to a {@code String}.</p>
     *
     *  @param  name    The name of the attribute; the name is case sensitive.
     *  @param  number   The attribute's value.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element, or no attributes are allowed at all.
     */
    public default ProcessingInstruction setAttribute( final String name, final long number ) throws IllegalArgumentException
    {
        return setAttribute( name, Long.toString( number ) );
    }   //  setAttribute()

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The method uses
     *  {@link Number#toString()}
     *  to convert the provided number to a {@code String}.</p>
     *
     *  @param  name    The name of the attribute; the name is case sensitive.
     *  @param  number   The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element, or no attributes are allowed at all.
     */
    public default ProcessingInstruction setAttribute( final String name, final Number number ) throws IllegalArgumentException
    {
        return setAttribute( name, nonNull( number ) ? number.toString() : null );
    }   //  setAttribute()

    /**
     *  <p>{@summary Sets the attribute with the given name.}</p>
     *  <p>The method uses
     *  {@link ZonedDateTime#toString()}
     *  to convert the provided number to a {@code String}.</p>
     *
     *  @param  name    The name of the attribute; the name is case sensitive.
     *  @param  date    The attribute's value; if {@code null} the
     *      attribute will be removed.
     *  @return This instance.
     *  @throws IllegalArgumentException    An attribute with the given name is
     *      not valid for the element, or no attributes are allowed at all.
     */
    public default ProcessingInstruction setAttribute( final String name, final ZonedDateTime date ) throws IllegalArgumentException
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
     *      not valid for the element, or no attributes are allowed at all.
     */
    @SuppressWarnings( "UnusedReturnValue" )
    public default ProcessingInstruction setAttributeIfNotEmpty( final String name, final CharSequence value ) throws IllegalArgumentException
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
     *      not valid for the element, or no attributes are allowed at all.
     */
    @SuppressWarnings( {"UnusedReturnValue", "OptionalUsedAsFieldOrParameterType"} )
    public default ProcessingInstruction setAttributeIfNotEmpty( final String name, final Optional<? extends CharSequence> optional ) throws IllegalArgumentException
    {
        requireNonNullArgument( optional, "optional" ).ifPresent( value -> setAttribute( name, value ) );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setAttributeIfNotEmpty()
}
//  interface ProcessingInstruction

/*
 *  End of File
 */