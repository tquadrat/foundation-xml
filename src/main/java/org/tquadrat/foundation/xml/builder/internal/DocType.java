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

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static org.apiguardian.api.API.Status.INTERNAL;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.StringUtils.isNotEmpty;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.getElementNameValidator;
import static org.tquadrat.foundation.xml.builder.spi.SGMLPrinter.repeat;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.builder.Namespace;
import org.tquadrat.foundation.xml.builder.spi.Element;
import org.tquadrat.foundation.xml.builder.spi.InvalidXMLNameException;

/**
 *  The definition for the XML DocType element.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: DocType.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
*
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: DocType.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public class DocType implements Element
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The name of the DTD; may be {@code null}.
     */
    private final String m_DTDName;

    /**
     *  The name of the element that defines the document type.
     */
    private final String m_ElementName;

    /**
     *  The parent for this {@code DOCTYPE}.
     */
    private Element m_Parent = null;

    /**
     *  The URI for the DTD.
     */
    private final URI m_URI;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code DocType} instance.<br>
     *  <br>The given element name is validated using the method that is
     *  provided by
     *  {@link org.tquadrat.foundation.xml.builder.XMLBuilderUtils#getElementNameValidator()}.
     *
     *  @param  elementName The name of the element that defines the document
     *      type.
     *  @param  uri The URI for the DTD.
     */
    public DocType( final String elementName, final URI uri )
    {
        if( !getElementNameValidator().test( requireNotEmptyArgument( elementName, "elementName" ) ) )
        {
            throw new InvalidXMLNameException( elementName );
        }
        m_ElementName = elementName;
        m_URI = requireNonNullArgument( uri, "uri" );
        m_DTDName = null;
    }   //  DocType()

    /**
     *  Creates a new {@code DocType} instance.
     *
     *  @param  elementName The name of the element that defines the document
     *      type.
     *  @param  dtdName    The name of the DTD.
     *  @param  uri The URI for the DTD.
     */
    public DocType( final String elementName, final String dtdName, final URI uri )
    {
        if( !getElementNameValidator().test( requireNotEmptyArgument( elementName, "elementName" ) ) )
        {
            throw new InvalidXMLNameException( elementName );
        }
        m_ElementName = elementName;
        m_URI = requireNonNullArgument( uri, "uri" );
        m_DTDName = requireNotEmptyArgument( dtdName, "dtdName" );
    }   //  DocType()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final Optional<String> getAttribute( final String name ) { return Optional.empty(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Map<String,String> getAttributes() { return emptyMap(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Collection<? extends Element> getChildren() { return emptyList(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String getElementName() { return "DOCTYPE"; }

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
     *  Checks if the doc type is based on a public or a local (system) DTD.
     *
     *  @return {@code true} if the DTD is public, {@code false} if
     *      it is a local DTD.
     */
    @SuppressWarnings( "PublicMethodNotExposedInInterface" )
    public final boolean isPublic() { return isNotEmpty( m_DTDName ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final <E extends Element> void setParent( final E parent ) { m_Parent = requireNonNullArgument( parent, "parent" ); }

    /**
     *  {@inheritDoc}
     *
     *  @param  indentationLevel    Ignored, should be always 0.
     *  @param  prettyPrint Ignored.
     */
    @Override
    public final String toString( final int indentationLevel, final boolean prettyPrint )
    {
        //---* Calculate the indentation *-------------------------------------
        final var filler = prettyPrint ? "\n" + repeat( indentationLevel ) : EMPTY_STRING;

        final var retValue = new StringBuilder( filler ).append( "<!DOCTYPE " ).append( m_ElementName );
        if( isPublic() )
        {
            retValue.append( " PUBLIC \"" ).append( m_DTDName ).append( "\" \"" ).append( m_URI.toString() );
        }
        else
        {
            retValue.append( " SYSTEM \"" ).append( m_URI.toString() );
        }
        retValue.append( "\"" ).append( ">" );

        //---* Done *----------------------------------------------------------
        return retValue.toString();
    }   //  toString()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString() { return toString( 0, true ); }
}
//  class DocType

/*
 *  End of File
 */