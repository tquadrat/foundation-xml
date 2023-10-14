/*
 * ============================================================================
 * Copyright © 2002-2023 by Thomas Thrien.
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

import static java.lang.Math.max;
import static java.lang.String.format;
import static org.apiguardian.api.API.Status.MAINTAINED;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;

import java.util.Collection;
import java.util.Map;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.tquadrat.foundation.xml.builder.Namespace;

/**
 *  Helper method for the conversion of SGML elements into a String.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: SGMLPrinter.java 1071 2023-09-30 01:49:32Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@UtilityClass
@ClassVersion( sourceVersion = "$Id: SGMLPrinter.java 1071 2023-09-30 01:49:32Z tquadrat $" )
@API( status = MAINTAINED, since = "0.0.5" )
public final class SGMLPrinter
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The tabulator size for pretty printing: {@value}
     */
    public static final int TAB_SIZE = 4;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance of this class allowed.
     */
    private SGMLPrinter() { throw new PrivateConstructorForStaticClassCalledError( SGMLPrinter.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Returns the attributes and their values, together with the namespaces,
     *  as a single formatted string.
     *
     *  @param  indentationLevel    The indentation level.
     *  @param  prettyPrint The pretty print flag.
     *  @param  elementName The name of the owning element.
     *  @param  attributes  The attributes.
     *  @param  namespaces  The namespaces.
     *  @return The attributes string.
     */
    @API( status = MAINTAINED, since = "0.0.5" )
    public static final String composeAttributesString( final int indentationLevel, final boolean prettyPrint, final String elementName, final Map<String,String> attributes, final Collection<Namespace> namespaces )
    {
        requireNotEmptyArgument( elementName, "elementName" );
        requireNonNullArgument( attributes, "attributes" );
        requireNonNullArgument( namespaces, "namespaces" );

        var retValue = EMPTY_STRING;
        if( !attributes.isEmpty() || !namespaces.isEmpty() )
        {
            //---* Determine the filler *--------------------------------------
            final var filler = prettyPrint ? "\n" + repeat( indentationLevel, elementName.length() + 1 ) : EMPTY_STRING;

            //---* Create the buffer *-----------------------------------------
            final var len = (filler.length() + 16) * (attributes.size() + namespaces.size());
            final var buffer = new StringBuilder( len );

            //---* Add the namespaces *----------------------------------------
            for( final var namespace : namespaces )
            {
                if( !buffer.isEmpty() ) buffer.append( filler );
                buffer.append( " " ).append( namespace.toString() );
            }

            //---* Add the attributes *----------------------------------------
            attributes.forEach( (key,value) ->
            {
                if( !buffer.isEmpty() ) buffer.append( filler );
                buffer.append( ' ' )
                    .append( key )
                    .append( "='")
                    .append( value )
                    .append( '\'' );
            });

            retValue = buffer.toString();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  composeAttributesString()

    /**
     *  Returns the children as a single formatted string.
     *
     *  @param  indentationLevel    The indentation level.
     *  @param  prettyPrint The pretty print flag.
     *  @param  parent  The parent element.
     *  @param  children    The children.
     *  @return The children string.
     */
    @SuppressWarnings( "OverlyComplexMethod" )
    @API( status = MAINTAINED, since = "0.0.5" )
    public static final String composeChildrenString( final int indentationLevel, final boolean prettyPrint, final Element parent, final Collection<? extends Element> children )
    {
        requireNonNullArgument( parent, "parent" );

        var retValue = EMPTY_STRING;
        if( !requireNonNullArgument( children, "children" ).isEmpty() )
        {
            //---* Calculate the indentation *---------------------------------
            /*
             * If the direct parent is an inline element, the block is false.
             */
            final var grandParent = parent.getParent();
            final var block = grandParent.map( element -> element.isBlock() && parent.isBlock() ).orElseGet( parent::isBlock ).booleanValue();
            var filler = (prettyPrint && block) && (indentationLevel > 0) ? "\n" + repeat( indentationLevel ) : EMPTY_STRING;

            //---* Render the children *---------------------------------------
            final var buffer = new StringBuilder( 1024 );

            final var newIndentationLevel = block ? indentationLevel + 1 : indentationLevel;
            Element lastChild = null;
            for( final var child : children )
            {
                buffer.append( child.toString( newIndentationLevel, prettyPrint ) );
                lastChild = child;
            }
            if( nonNull( lastChild ) && lastChild.isBlock() )
            {
                if( prettyPrint && block && (indentationLevel == 0) )
                {
                    buffer.append( "\n" );
                }
                else
                {
                    if( (block != parent.isBlock()) && prettyPrint && (indentationLevel > 0) )
                    {
                        filler = "\n" + repeat( indentationLevel - 1 );
                    }
                    buffer.append( filler );
                }
            }
            retValue = buffer.toString();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  composeChildrenString()

    /**
     *  Returns the given document as a single formatted string.
     *
     *  @param  prettyPrint The pretty print flag.
     *  @param  document    The document.
     *  @return The element string.
     */
    @SuppressWarnings( "Convert2streamapi" )
    @API( status = MAINTAINED, since = "0.0.5" )
    public static final String composeDocumentString( final boolean prettyPrint, final Document<? extends Element> document )
    {
        final var retValue = new StringBuilder();
        for( final var child : requireNonNullArgument( document, "document" ).getChildren() )
        {
            retValue.append( child.toString( 0, prettyPrint ) );
        }

        //---* Done *----------------------------------------------------------
        return retValue.toString();
    }   //  composeElementString()

    /**
     *  <p>{@summary Returns the given element as a single formatted
     *  string.}</p>
     *  <p>The argument {@code selfClosing} exists for some HTML elements
     *  like {@code <script>}; in pure XML, all elements are self-closing when
     *  empty, while other flavours may define elements that always need a
     *  closing tag. Therefore</p>
     *  <pre><code>  &hellip;
     *  &lt;script/&gt;
     *  &hellip;</code></pre>
     *  <p>is valid in pure XML, but not in HTML where it has to be</p>
     *  <pre><code>  &hellip;
     *  &lt;script&gt;&lt;/script&gt;
     *  &hellip;</code></pre>
     *
     *  @param  indentationLevel    The indentation level.
     *  @param  prettyPrint The pretty print flag.
     *  @param  element The element.
     *  @param  selfClosing {@code true} if an empty element is self-closing or
     *      {@code false} if an empty element still needs a closing tag.
     *  @return The element string.
     */
    @SuppressWarnings( "BooleanParameter" )
    @API( status = MAINTAINED, since = "0.0.5" )
    public static final String composeElementString( final int indentationLevel, final boolean prettyPrint, final Element element, final boolean selfClosing )
    {
        String retValue = null;

        //---* Calculate the indentation *-------------------------------------
        /*
         * If the direct parent is an inline element, the block is false.
         */
        final var parent = requireNonNullArgument( element, "element" ).getParent();
        final var block = parent.map( value -> value.isBlock() && element.isBlock() ).orElseGet( element::isBlock ).booleanValue();
        final var filler = (prettyPrint && block) ? "\n" + repeat( indentationLevel ) : EMPTY_STRING;

        //---* Render the element *--------------------------------------------
        final var elementName = element.getElementName();
        if( !selfClosing || element.hasChildren() )
        {
            final var buffer = new StringBuilder( 1024 );

            //---* The opening tag *-------------------------------------------
            buffer.append( format( "%3$s<%1$s%2$s>", elementName, composeAttributesString( indentationLevel, prettyPrint, elementName, element.getAttributes(), element.getNamespaces() ), filler ) );

            //---* The children *----------------------------------------------
            if( element.hasChildren() )
            {
                buffer.append( composeChildrenString( indentationLevel, prettyPrint, element, element.getChildren() ) );
            }

            //---* The closing tag *-------------------------------------------
            buffer.append( format( "</%1$s>", elementName ) );
            retValue = buffer.toString();
        }
        else
        {
            retValue = format( "%3$s<%1$s%2$s/>", elementName, composeAttributesString( indentationLevel, prettyPrint, elementName, element.getAttributes(), element.getNamespaces() ), filler );
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  composeElementString()

    /**
     *  Returns the namespaces as a single formatted string.
     *
     *  @param  indentationLevel    The indentation level.
     *  @param  prettyPrint The pretty print flag.
     *  @param  elementName The name of the owning element.
     *  @param  namespaces  The namespaces.
     *  @return The namespaces string.
     */
    @API( status = MAINTAINED, since = "0.0.5" )
    public static final String composeNamespaceString( final int indentationLevel, final boolean prettyPrint, final String elementName, final Collection<Namespace> namespaces )
    {
        requireNotEmptyArgument( elementName, "elementName" );

        var retValue = EMPTY_STRING;
        if( !requireNonNullArgument( namespaces, "namespaces" ).isEmpty() )
        {
            //---* Determine the filler *--------------------------------------
            final var filler = prettyPrint ? "\n" + repeat( indentationLevel, elementName.length() + 1 ) : EMPTY_STRING;

            //---* Create the buffer *-----------------------------------------
            final var len = (filler.length() + 16) * namespaces.size();
            final var buffer = new StringBuilder( len );

            //---* Add the namespaces *----------------------------------------
            for( final var namespace : namespaces )
            {
                if( !buffer.isEmpty() ) buffer.append( filler );
                buffer.append( " " ).append( namespace.toString() );
            }
            retValue = buffer.toString();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  composeNamespaceString()

    /**
     *  <p>{@summary Returns a String, consisting only of blanks, with the
     *  length that is determined by the given indentation level, multiplied
     *  by the
     *  {@link #TAB_SIZE}
     *  (= {@value #TAB_SIZE}), plus the given number of additional
     *  blanks.}</p>
     *  <p>Negative values for either the indentation level or the number of
     *  additional blanks are treated as 0.</p>
     *
     *  @param  indentationLevel    The indentation level.
     *  @param  additionalBlanks    The number of additional blanks.
     *  @return The resulting String.
     */
    @API( status = MAINTAINED, since = "0.0.5" )
    public static final String repeat( final int indentationLevel, final int additionalBlanks )
    {
        final var count = max( 0, indentationLevel ) * TAB_SIZE + max( 0, additionalBlanks );
        final var retValue = count > 0 ? " ".repeat( count ) : EMPTY_STRING;

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  repeat()

    /**
     *  Returns a String, consisting only of blanks, with the length that is
     *  determined by the given indentation level, multiplied by the
     *  {@link #TAB_SIZE}
     *  (= {@value #TAB_SIZE}).
     *
     *  @param  indentationLevel    The indentation level; a negative value is
     *      treated as 0.
     *  @return The resulting String.
     */
    @API( status = MAINTAINED, since = "0.0.5" )
    public static final String repeat( final int indentationLevel )
    {
        return repeat( indentationLevel, 0 );
    }   //  repeat()
}
//  class SGMLPrinter

/*
 *  End of File
 */