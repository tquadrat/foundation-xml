/*
 * ============================================================================
 * Copyright © 2002-2024 by Thomas Thrien.
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

import static java.lang.String.format;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.hash;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.getPrefixValidator;

import java.io.Serial;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.builder.spi.InvalidXMLNameException;

/**
 *  The definition of an XML namespace entry.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: Namespace.java 1101 2024-02-18 00:18:48Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: Namespace.java 1101 2024-02-18 00:18:48Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class Namespace implements Serializable, Comparable<Namespace>
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  An empty array of {@code Namespace} objects.
     */
    public static final Namespace [] EMPTY_Namespace_ARRAY = new Namespace [0];

    /**
     *  The keyword for a namespace definition: {@value}.
     */
    public static final String KEYWORD = "xmlns";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The namespace identifier.
     *
     *  @serial
     */
    private final URI m_Identifier;

    /**
     *  The namespace prefix.
     *
     *  @serial
     */
    @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
    private final Optional<String> m_Prefix;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The serial version UID for objects of this class: {@value}.
     *
     *  @hidden
     */
    @Serial
    private static final long serialVersionUID = 1L;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code Namespace} instance without a prefix.
     *
     *  @param  identifier  The namespace identifier.
     */
    public Namespace( final URI identifier )
    {
        this( Optional.empty(), identifier );
    }   //  Namespace()

    /**
     *  Creates a new {@code Namespace} instance without a prefix.
     *
     *  @param  identifier  The namespace identifier.
     *  @throws URISyntaxException  The provided URI String is invalid.
     */
    public Namespace( final String identifier ) throws URISyntaxException
    {
        this( Optional.empty(), new URI( requireNotEmptyArgument( identifier, "identifier" ) ) );
    }   //  Namespace()

    /**
     *  Creates a new {@code Namespace} instance.<br>
     *  <br>The given prefix is validated using the method that is
     *  provided by
     *  {@link org.tquadrat.foundation.xml.builder.XMLBuilderUtils#getPrefixValidator()}.
     *
     *  @param  prefix  The namespace prefix.
     *  @param  identifier  The namespace identifier.
     */
    public Namespace( final String prefix, final URI identifier )
    {
        this( Optional.of( requireNotEmptyArgument( prefix, "prefix" ) ), identifier );
    }   //  Namespace()

    /**
     *  Creates a new {@code Namespace} instance.<br>
     *  <br>The given prefix is validated using the method that is
     *  provided by
     *  {@link org.tquadrat.foundation.xml.builder.XMLBuilderUtils#getPrefixValidator()}.
     *
     *  @param  prefix  The namespace prefix.
     *  @param  identifier  The namespace identifier.
     *  @throws URISyntaxException  The provided URI String is invalid.
     */
    public Namespace( final String prefix, final String identifier ) throws URISyntaxException
    {
        this( Optional.of( requireNotEmptyArgument( prefix, "prefix" ) ), new URI( requireNotEmptyArgument( identifier, "identifier" ) ) );
    }   //  Namespace()

    /**
     *  Creates a new {@code Namespace} instance.<br>
     *  <br>The given prefix is validated using the method that is
     *  provided by
     *  {@link org.tquadrat.foundation.xml.builder.XMLBuilderUtils#getPrefixValidator()}.
     *
     *  @param  prefix  The namespace prefix.
     *  @param  identifier  The namespace identifier.
     */
    @SuppressWarnings( "OptionalUsedAsFieldOrParameterType" )
    private Namespace( final Optional<String> prefix, final URI identifier )
    {
        if( prefix.isPresent() && !getPrefixValidator().test( prefix.get() ) )
        {
            throw new InvalidXMLNameException( prefix.get() );
        }

        m_Prefix = prefix;
        m_Identifier = requireNonNullArgument( identifier, "identifier" );
    }   //  Namespace()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @SuppressWarnings( "UseOfConcreteClass" )
    @Override
    public final int compareTo( final Namespace o )
    {
        final var retValue = m_Prefix.isPresent()
            ? (o.m_Prefix.isPresent() ? Integer.signum( m_Prefix.orElseThrow().compareTo( o.m_Prefix.orElseThrow() ) ) : 1)
            : (o.m_Prefix.isPresent() ? -1 : 0);

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  compareTo()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean equals( final Object obj )
    {
        var retValue = this == obj;
        if( !retValue && obj instanceof final Namespace other )
        {
            retValue = m_Prefix.equals( other.m_Prefix ) && m_Identifier.equals( other.m_Identifier );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }

    /**
     *  Returns the namespace identifier.
     *
     *  @return The namespace identifier.
     */
    public final URI getIdentifier() { return m_Identifier; }

    /**
     *  Returns the namespace prefix.
     *
     *  @return An instance of
     *      {@link Optional}
     *      that holds the namespace prefix.
     */
    public final Optional<String> getPrefix() { return m_Prefix; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final int hashCode() { return hash( m_Prefix, m_Identifier ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString()
    {
        final var retValue = m_Prefix
            .map( p -> format( "%s:%s=\"%s\"", KEYWORD, p, m_Identifier.toString() ) )
            .orElseGet( () -> format( "%s=\"%s\"", KEYWORD, m_Identifier.toString() ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class Namespace

/*
 *  End of File
 */