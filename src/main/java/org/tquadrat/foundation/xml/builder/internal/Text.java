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
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.xml.builder.spi.SGMLPrinter.repeat;

import java.util.Optional;
import java.util.function.Function;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.builder.spi.Element;

/**
 *  This class defines the plain text that is the content of an SGML element as
 *  such an element.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: Text.java 1030 2022-04-06 13:42:02Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "NewClassNamingConvention" )
@ClassVersion( sourceVersion = "$Id: Text.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@API( status = INTERNAL, since = "0.0.5" )
public class Text implements Element
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The escape function.
     */
    private final Function<? super CharSequence, String> m_EscapeFunction;

    /**
     *  The flag that indicates if this text element is to be treated as a
     *  block element. This is usually {@code false}, but for predefined
     *  markup, it can be {@code true}.
     *
     *  @see org.tquadrat.foundation.xml.builder.spi.ChildSupport#addPredefinedMarkup(CharSequence)
     */
    private final boolean m_IsBlock;

    /**
     *  The parent element for this comment.
     */
    private Element m_Parent;

    /**
     *  The text.
     */
    private final CharSequence m_Text;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code Text} object.
     *
     *  @param  text    The text.
     *  @param  escapeFunction  The function that is used to escape special
     *      characters in the given text according to the target format.
     *  @param  isBlock The flag that indicates if this text element is to be
     *      treated as a block element. This is usually {@code false}, but for
     *      predefined markup, it can be {@code true}.
     *
     *  @see org.tquadrat.foundation.xml.builder.spi.ChildSupport#addPredefinedMarkup(CharSequence)
     */
    public Text( final CharSequence text, final Function<? super CharSequence, String> escapeFunction, final boolean isBlock )
    {
        requireNonNullArgument( escapeFunction, "escapeFunction" );
        m_Text = requireNonNullArgument( text, "text" ).toString();
        m_EscapeFunction = escapeFunction;
        m_IsBlock = isBlock;
    }   //  Text()

    /**
     *  Creates a new {@code Text} object.
     *
     *  @param  text    The text.
     *  @param  escapeFunction  The function that is used to escape special
     *      characters in the given text according to the target format.
     */
    public Text( final CharSequence text, final Function<? super CharSequence, String> escapeFunction )
    {
        this( text, escapeFunction, false );
    }   //  Text()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final String getElementName() { return "[TEXT]"; }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final Optional<Element> getParent() { return Optional.ofNullable( m_Parent ); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final boolean isBlock() { return m_IsBlock; }

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
        final String retValue;
        if( m_IsBlock )
        {
            //---* Calculate the indentation *---------------------------------
            /*
             * If the direct parent is an inline element, the block is false.
             */
            final var parent = getParent();
            final var block = parent.map( element -> element.isBlock() && isBlock() ).orElseGet( this::isBlock );
            final var filler = (prettyPrint && block) ? "\n" + repeat( indentationLevel ) : EMPTY_STRING;

            //---* Render the text *-------------------------------------------
            retValue = format( "%2$s%1$s", m_EscapeFunction.apply( m_Text ), filler );
        }
        else
        {
            retValue = m_EscapeFunction.apply( m_Text );
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
//  class TextElement

/*
 *  End of File
 */