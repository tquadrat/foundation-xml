/*
 * ============================================================================
 *  Copyright © 2002-2021 by Thomas Thrien.
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

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.isNotEmpty;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.getElementNameValidator;
import static org.tquadrat.foundation.xml.builder.spi.SGMLPrinter.repeat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.util.LazyList;
import org.tquadrat.foundation.xml.builder.Namespace;
import org.tquadrat.foundation.xml.builder.ProcessingInstruction;
import org.tquadrat.foundation.xml.builder.spi.AttributeSupport;
import org.tquadrat.foundation.xml.builder.spi.Element;
import org.tquadrat.foundation.xml.builder.spi.InvalidXMLNameException;

/**
 *  The implementation for the interface
 *  {@link ProcessingInstruction}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ProcessingInstructionImpl.java 840 2021-01-10 21:37:03Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: ProcessingInstructionImpl.java 840 2021-01-10 21:37:03Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public final class ProcessingInstructionImpl implements ProcessingInstruction
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The attributes for the processing instruction.
     */
    @SuppressWarnings( "InstanceVariableOfConcreteClass" )
    private final AttributeSupport m_Attributes;

    /**
     *  The data for the processing instruction.
     */
    private final LazyList<String> m_Data = LazyList.use( false, ArrayList::new );

    /**
     *  The name for this processing instruction.
     */
    private final String m_ElementName;

    /**
     *  The parent for this processing instruction.
     */
    private Element m_Parent = null;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code ProcessingInstruction} instance.
     *
     *  @param  name    The name for this processing instruction.
     */
    public ProcessingInstructionImpl( final String name ) { this( name, null ); }

    /**
     *  Creates the {@code ProcessingInstruction} instance for the XML header.
     */
    public ProcessingInstructionImpl()
    {
        m_ElementName = "xml";

        final var attributeSequence = new String [] {"version", "encoding", "standalone"};

        m_Attributes = new AttributeSupport( this, true );
        m_Attributes.registerAttributes( attributeSequence );
        m_Attributes.registerSequence( attributeSequence );
    }   //  ProcessingInstruction()

    /**
     *  Creates a new {@code ProcessingInstruction} instance.
     *
     *  @param  name    The name for this processing instruction.
     *  @param  data    The data for the processing instruction; can be
     *      {@code null}.
     */
    public ProcessingInstructionImpl( final String name, final CharSequence data )
    {
        if( !getElementNameValidator().test( requireNotEmptyArgument( name, "name" ) ) )
        {
            throw new InvalidXMLNameException( name );
        }
        m_ElementName = name;
        if( isNotEmpty( data  ) ) m_Data.add( data.toString() );

        m_Attributes = new AttributeSupport( this, false );
    }   //  ProcessingInstruction()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final ProcessingInstruction addData( final CharSequence data )
    {
        m_Data.add( requireNotEmptyArgument( data, "data" ).toString() );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  addData()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Optional<String> getAttribute( final String name ) { return m_Attributes.getAttribute( name ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Map<String,String> getAttributes() { return m_Attributes.getAttributes(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Collection<? extends Element> getChildren() { return emptyList(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String getElementName() { return m_ElementName; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Collection<Namespace> getNamespaces() { return emptySet(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Optional<Element> getParent() { return Optional.ofNullable( m_Parent ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean hasChildren() { return false; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean isBlock() { return true; }

    /**
     *  {@inheritDoc}<br>
     *  <br>The given attribute name is validated using the method that is
     *  provided by
     *  {@link org.tquadrat.foundation.xml.builder.XMLBuilderUtils#getAttributeNameValidator()}.
     */
    @Override
    public final ProcessingInstruction setAttribute( final String name, final CharSequence value, final Optional<? extends CharSequence> append ) throws IllegalArgumentException
    {
        m_Attributes.setAttribute( name, value, append );

        //---* Done *----------------------------------------------------------
        return this;
    }   //  setAttribute()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final <E extends Element> void setParent( final E parent ) { m_Parent = requireNonNullArgument( parent, "parent" ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString( final int indentationLevel, final boolean prettyPrint )
    {
        final var text = new StringBuilder( 256 );

        //---* Determine the filler *------------------------------------------
        final var filler = prettyPrint ? "\n" + repeat( indentationLevel, m_ElementName.length() + 2 ) : EMPTY_STRING;

        //---* Add the data *--------------------------------------------------
        if( m_Data.isPresent() )
        {
            final var joiner = new StringJoiner( filler + " ", " ", EMPTY_STRING );
            for( final var data : m_Data ) joiner.add( data );
            text.append( joiner.toString() );
        }

        //---* Add the attributes *--------------------------------------------
        getAttributes().forEach( (k,v) ->
        {
            if( !text.isEmpty() ) text.append( filler );
            text.append( ' ' )
                .append( k )
                .append( "='")
                .append( v )
                .append( '\'' );
        } );

        //---* Calculate the indentation *-------------------------------------
        final var indentation = prettyPrint && (indentationLevel > 0) ? repeat( indentationLevel ) : EMPTY_STRING;

        //---* Render the element *--------------------------------------------
        final var retValue = format( "%3$s<?%1$s%2$s?>", m_ElementName, text, indentation );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString() { return toString( 0, true ); }
}
//  class ProcessingInstruction

/*
 *  End of File
 */