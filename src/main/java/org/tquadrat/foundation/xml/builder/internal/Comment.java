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
import static org.tquadrat.foundation.lang.CommonConstants.CHAR_HYPHEN;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.isEmpty;
import static org.tquadrat.foundation.util.StringUtils.isEmptyOrBlank;
import static org.tquadrat.foundation.util.StringUtils.splitString;
import static org.tquadrat.foundation.xml.builder.spi.SGMLPrinter.repeat;

import java.util.Optional;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.builder.spi.Element;

/**
 *  This class defines a SGML comment.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: Comment.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: Comment.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public class Comment implements Element
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The parent element for this comment.
     */
    private Element m_Parent;

    /**
     *  The text.
     */
    private final String m_Text;

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The hyphen replacement (escaped).
     */
    private static final String HYPHEN_REPLACEMENT;

    static
    {
        HYPHEN_REPLACEMENT = format( "&#x%x;", (int) CHAR_HYPHEN );
    }
        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code Comment} object.
     *
     *  @param  text    The text.
     */
    public Comment( final CharSequence text )
    {
        m_Text = requireNonNullArgument( text, "text" ).toString().intern();
    }   //  Comment()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final String getElementName() { return "[COMMENT]"; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Optional<Element> getParent() { return Optional.ofNullable( m_Parent ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean isBlock() { return true; }

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
        final var filler1 = prettyPrint ? "\n" + repeat( indentationLevel ) : EMPTY_STRING;
        final String retValue;
        if( isEmptyOrBlank( m_Text ) )
        {
            retValue = format( "%s<!-- -->", filler1 );
        }
        else
        {
            final var filler2 = isEmpty( filler1 ) ? " " : filler1;
            final var buffer = new StringBuilder( m_Text.length() * 2 );
            char c;
            for( var i = 0; i < m_Text.length(); ++i )
            {
                c = m_Text.charAt( i );
                if( c == '\u002D' )
                {
                    /*
                     * A hyphen or minus sign (\u002D) in the comment text may
                     * cause an issue when the resulting document is parsed.
                     * Therefore, we replace it by another character that looks
                     * similar, but is not interpreted by XML parsers.
                     */
                    buffer.append( HYPHEN_REPLACEMENT );
                }
                else
                {
                    buffer.append( c );
                }
            }

            final var lines = splitString( buffer, '\n' );
            buffer.setLength( 0 );
            buffer.append( filler1 ).append( "<!--" );
            for( final var line : lines )
            {
                buffer.append( filler2 ).append( line.trim() );
            }
            buffer.append( filler2 ).append( "-->" );
            retValue = buffer.toString();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String toString() { return toString( 0, true ); }
}
//  class Comment

/*
 *  End of File
 */