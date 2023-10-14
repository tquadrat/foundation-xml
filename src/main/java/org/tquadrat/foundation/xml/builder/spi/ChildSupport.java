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

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableCollection;
import static org.apiguardian.api.API.Status.MAINTAINED;
import static org.tquadrat.foundation.lang.CommonConstants.CDATA_LEADIN;
import static org.tquadrat.foundation.lang.CommonConstants.CDATA_LEADOUT;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.StringUtils.isEmpty;
import static org.tquadrat.foundation.util.StringUtils.isNotEmpty;
import static org.tquadrat.foundation.util.StringUtils.isNotEmptyOrBlank;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.getElementNameValidator;
import static org.tquadrat.foundation.xml.builder.spi.SGMLPrinter.composeChildrenString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.IllegalOperationException;
import org.tquadrat.foundation.util.LazyList;
import org.tquadrat.foundation.xml.builder.internal.Comment;
import org.tquadrat.foundation.xml.builder.internal.Text;

/**
 *  This class provides the support for child elements and text to elements.
 *  As comments are also considered to be child elements, an element must have
 *  an instance of {@code ChildSupport} when it should take comments. If only
 *  comments should be allowed, the instance can be instantiated with
 *  {@link #ChildSupport(Element,boolean,boolean,boolean,Function) ChildSupport( parent, false, false, false, null );}.<br>
 *  The flag {@code checkValid} that applies to the constructors
 *  {@link #ChildSupport(Element,boolean)}
 *  and
 *  {@link #ChildSupport(Element,boolean,boolean,boolean,Function)}
 *  affects only child elements that are added through
 *  {@link #addChild(Element)}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: ChildSupport.java 1071 2023-09-30 01:49:32Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: ChildSupport.java 1071 2023-09-30 01:49:32Z tquadrat $" )
@API( status = MAINTAINED, since = "0.0.5" )
public final class ChildSupport
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The message indicating that no children are allowed: {@value}.
     */
    private static final String MSG_NoChildrenAllowed = "No children allowed for element '%1$s'";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  Flag that indicates whether child elements other than text are allowed.
     */
    private final boolean m_AllowChildren;

    /**
     *  Flag that indicates whether text is allowed.
     */
    private final boolean m_AllowText;

    /**
     *  Flag that indicates whether the validity of children should be
     *  checked.
     */
    private final boolean m_CheckValid;

    /**
     *  The list with the element's children.
     */
    private final List<Element> m_Children;

    /**
     *  The escape function that is used for text elements.
     */
    private final Function<CharSequence,String> m_EscapeFunction;

    /**
     *  The element that owns this {@code ChildSupport} instance.
     */
    private final Element m_Owner;

    /**
     *  The element names of valid children for a given element; the key for
     *  this map is the element name for the parent element.
     */
    private final Collection<String> m_ValidChildren;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code ChildSupport} instance for comments only.
     *
     *  @param  owner   The element that owns this {@code ChildSupport}
     *      instance.
     */
    public ChildSupport( final Element owner )
    {
        this( owner, false, false, false, null );
    }   //  ChildSupport()

    /**
     *  Creates a new {@code ChildSupport} instance that allows text, but no
     *  child elements.
     *
     *  @param  owner   The element that owns this {@code ChildSupport}
     *      instance.
     *  @param  escapeFunction  The escape function that is used to convert
     *      special characters in texts; only required when {@code allowText}
     *      is {@code true}.
     */
    public ChildSupport( final Element owner, final Function<CharSequence,String> escapeFunction )
    {
        this( owner, false, false, true, escapeFunction );
    }   //  ChildSupport()

    /**
     *  Creates a new {@code ChildSupport} instance that allows child elements,
     *  but no text.
     *
     *  @param  owner   The element that owns this {@code ChildSupport}
     *      instance.
     *  @param  checkValid  {@code true} whether children are checked to be
     *      allowed before they are added.
     */
    public ChildSupport( final Element owner, final boolean checkValid )
    {
        this( owner, checkValid, true, false, null );
    }   //  ChildSupport()

    /**
     *  Creates a new {@code ChildSupport} instance.
     *
     *  @param  owner   The element that owns this {@code ChildSupport}
     *      instance.
     *  @param  checkValid  {@code true} whether children are checked to be
     *      allowed before they are added.
     *  @param  allowChildren   {@code true} if other elements could be added
     *      as children, {@code false} otherwise.
     *  @param  allowText   {@code true} it text could be added to the element,
     *      {@code false} if not.
     *  @param  escapeFunction  The escape function that is used to convert
     *      special characters in texts; only required when {@code allowText}
     *      is {@code true}.
     */
    @SuppressWarnings( "BooleanParameter" )
    public ChildSupport( final Element owner, final boolean checkValid, final boolean allowChildren, final boolean allowText, final Function<CharSequence,String> escapeFunction )
    {
        m_Owner = requireNonNullArgument( owner, "owner" );
        m_CheckValid = checkValid;
        m_AllowChildren = allowChildren;
        m_AllowText = allowText;
        m_EscapeFunction = m_AllowText ? requireNonNullArgument( escapeFunction, "escapeFunction" ) : null;

        m_Children = LazyList.use( ArrayList::new );
        m_ValidChildren = m_CheckValid ? new HashSet<>() : null;
    }   //  ChildSupport()

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Adds a {@code CDATA} element. As {@code CDATA} is basically text, this
     *  is controlled by the same flag as text, too.
     *
     *  @param  text    The text for the {@code CDATA} sequence.
     *  @throws IllegalOperationException    No text is allowed for the owner.
     */
    public final void addCDATA( final CharSequence text ) throws IllegalOperationException
    {
        addText( requireNonNullArgument( text, "text" ), ChildSupport::toCDATA, true );
    }   //  addCDATA()

    /**
     *  Adds a child.
     *
     *  @param  <E> The implementation type for the {@code child}.
     *  @param  child   The child to add.
     *  @throws IllegalArgumentException    The child is not allowed for the
     *      owner of this instance of {@code ChildSupport}.
     *  @throws IllegalStateException   The child has already a parent that is
     *      not the owner of this instance of {@code ChildSupport}.
     *  @throws IllegalOperationException   No children allowed for this
     *      element.
     */
    public final <E extends Element> void addChild( final E child ) throws IllegalArgumentException, IllegalStateException, IllegalOperationException
    {
        addChildElement( "addChild()", child );
    }   //  addChild()

    /**
     *  Adds a child element.
     *
     *  @param  <E> The implementation type for the {@code child}.
     *  @param  operationName   The name of the operation that was originally
     *      called.
     *  @param  child   The child to add.
     *  @throws IllegalArgumentException    The child is not allowed for the
     *      owner of this instance of {@code ChildSupport}.
     *  @throws IllegalStateException   The child has already a parent that is
     *      not the owner of this instance of {@code ChildSupport}.
     *  @throws IllegalOperationException   No children allowed for this
     *      element.
     */
    private final <E extends Element> void addChildElement( final String operationName, final E child ) throws IllegalArgumentException, IllegalStateException, IllegalOperationException
    {
        //---* Check if valid ... *--------------------------------------------
        checkValid( requireNonNullArgument( child, "child" ), requireNotEmptyArgument( operationName, "operationName" ) );

        //---* Add the child *-------------------------------------------------
        m_Children.add( child );
        child.setParent( m_Owner );
    }   //  addChild()

    /**
     *  Adds a comment.
     *
     *  @param  comment The comment text.
     */
    public final void addComment( final CharSequence comment )
    {
        if( isNotEmptyOrBlank( comment ) ) addChildElement( "addComment()", new Comment( comment ) );
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
     *  @throws IllegalArgumentException    The child is not allowed for the
     *      owner of this instance of {@code ChildSupport}.
     *  @throws IllegalOperationException   No children allowed for this
     *      element.
     */
    public final void addPredefinedMarkup( final CharSequence markup ) throws IllegalArgumentException, IllegalOperationException
    {
        requireNonNullArgument( markup, "markup" );

        final var operationName = "addPredefinedMarkup()";
        if( !allowsChildren() ) throw new IllegalOperationException( operationName, format( MSG_NoChildrenAllowed, m_Owner.getElementName() ) );
        addChildElement( operationName, new Text( markup, CharSequence::toString, true ) );
    }   //  addPredefinedMarkup()

    /**
     *  Adds text. Special characters will be escaped by the escape function
     *  given with the
     *  {@linkplain #ChildSupport(Element, boolean, boolean, boolean, Function) constructor}.
     *
     *  @param  text    The text.
     *  @throws IllegalArgumentException    No text is allowed for the owner.
     */
    public final void addText( final CharSequence text ) throws IllegalArgumentException
    {
        addText( requireNonNullArgument( text, "text" ), m_EscapeFunction, false );
    }   //  addText()

    /**
     *  Adds text.
     *
     *  @param  text    The text.
     *  @param  escapeFunction  The function the escapes the text in compliance
     *      with the type.
     *  @param  addEmpty    If {@code true} a new
     *      {@link Text} instance will be added even when the given
     *      {@code text} is empty, {@code false} means that empty {@code text}
     *      will be omitted.
     *  @throws IllegalOperationException    No text is allowed for the owner.
     */
    private final void addText( final CharSequence text, final Function<? super CharSequence, String> escapeFunction, final boolean addEmpty ) throws IllegalOperationException
    {
        assert nonNull( text ) : "text is null";
        assert !m_AllowText || nonNull( escapeFunction ) : "escapeFunction is null";

        if( !allowsText() ) throw new IllegalOperationException( "addText()", "No text allowed for element '%1$s'".formatted( m_Owner.getElementName() ) );
        if( addEmpty || isNotEmpty( text ) ) addChild( new Text( text, escapeFunction ) );
    }   //  addText()

    /**
     *  Returns the flag that indicates whether this instance of
     *  {@code ChildSupport} allows other
     *  {@linkplain Element elements}
     *  to be added as children.
     *
     *  @return {@code true} when child elements are allowed, {@code false} if
     *      not.
     */
    @SuppressWarnings( "BooleanMethodNameMustStartWithQuestion" )
    public final boolean allowsChildren() { return m_AllowChildren; }

    /**
     *  Returns the flag that indicates whether this instance of
     *  {@code ChildSupport} allows that text and {@code CDATA} elements can
     *  be added.
     *
     *  @return {@code true} if it is allowed to add text and {@code CDATA},
     *      {@code false} otherwise.
     */
    @SuppressWarnings( "BooleanMethodNameMustStartWithQuestion" )
    public final boolean allowsText() { return m_AllowText; }

    /**
     *  <p>{@summary Checks whether a child is valid for the element that owns
     *  this {@code ChildSupport} instance.}</p>
     *  <p>The child is valid either when
     *  {@link #m_CheckValid checkValid}
     *  is {@code false},
     *  the child is a
     *  {@link Comment}
     *  or
     *  {@link Text},
     *  {@link #m_ValidChildren}
     *  does not contain an entry for the
     *  {@linkplain #m_Owner owner's}
     *  {@linkplain Element#getElementName() element name},
     *  or the child's element name is explicitly configured. Obviously, it is
     *  not valid, when no children (other then text or comments) are allowed
     *  at all.</p>
     *
     *  @param  child   The child to check for.
     *  @param  operationName   The name of the attempted operation.
     *  @throws IllegalArgumentException    The child is not allowed for the
     *      owner.
     *  @throws IllegalOperationException   No children allowed for the owner.
     */
    private final void checkValid( final Element child, @SuppressWarnings( "SameParameterValue" ) final String operationName ) throws IllegalArgumentException, IllegalOperationException
    {
        if( !(child instanceof Comment) && !(child instanceof Text) )
        {
            if( !allowsChildren() ) throw new IllegalOperationException( operationName, format( MSG_NoChildrenAllowed, m_Owner.getElementName() ) );
            if( checksIfValid() )
            {
                if( !m_ValidChildren.contains( child.getElementName() ) )
                {
                    throw new IllegalArgumentException( "A child with name '%2$s' is not allowed for element '%1$s'".formatted( m_Owner.getElementName(), child.getElementName() ) );
                }
            }
        }

        final Optional<? extends Element> parent = child.getParent();
        if( parent.isPresent() )
        {
            if( parent.get() != m_Owner ) throw new IllegalStateException( "The child has already a parent" );
            throw new IllegalStateException( "The child was already added to this parent" );
        }
    }   //  checkValid()

    /**
     *  Returns a flag that indicates whether an extended validity check is
     *  performed on child elements before adding them.
     *
     *  @return {@code true} if extended validation are performed,
     *      {@code false} if any instance of
     *      {@link Element}
     *      can be added. Also {@code false} if no children are allowed at all.
     *
     *  @see #addChild(Element)
     *  @see #allowsChildren()
     */
    @SuppressWarnings( "BooleanMethodNameMustStartWithQuestion" )
    public final boolean checksIfValid() { return m_AllowChildren && m_CheckValid; }

    /**
     *  Provides access to the children for this element; the returned
     *  collection is not modifiable.
     *
     *  @return A reference the children of this element; if the element does
     *      not have children, an empty collection will be returned.
     */
    public final Collection<? extends Element> getChildren() { return unmodifiableCollection( m_Children ); }

    /**
     *  Returns {@code true} if the element has children, {@code false}
     *  otherwise.
     *
     *  @return {@code true} if the element has children.
     */
    public final boolean hasChildren() { return !m_Children.isEmpty(); }

    /**
     *  Registers the element names of valid child elements for the owning
     *  element.
     *
     *  @note   The given children will be <i>added</i> to the already existing
     *      ones!
     *
     *  @param  children    The element names of the valid children.
     */
    public final void registerChildren( final String... children )
    {
        if( m_CheckValid )
        {
            for( final var child : requireNonNullArgument( children, "children" ) )
            {
                if( !getElementNameValidator().test( child ) ) throw new InvalidXMLNameException( child );
                m_ValidChildren.add( child );
            }
        }
    }   //  registerChildren()

    /**
     *  Returns the list of the registered children.
     *
     *  @return The registered children.
     */
    public final Collection<String> retrieveValidChildren()
    {
        final Collection<String> retValue = checksIfValid() ? List.copyOf( m_ValidChildren ) : emptyList();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  retrieveValidChildren()

    /**
     *  {@summary &quot;Escapes&quot; the given String to a {@code CDATA}
     *  sequence.}
     *
     *  @param  text    The text.
     *  @return The {@code CDATA} sequence.
     */
    private static final String toCDATA( final CharSequence text )
    {
        final var retValue = new StringBuilder();

        if( isEmpty( text ) )
        {
            retValue.append( CDATA_LEADIN )
                .append( CDATA_LEADOUT );
        }
        else
        {
            final var str = text.toString();
            var start = 0;
            int pos;
            //noinspection NestedAssignment
            while( (pos = str.indexOf( "]", start )) >= 0 )
            {
                if( pos == start )
                {
                    retValue.append( ']' );
                    ++start;
                }
                else
                {
                    retValue.append( CDATA_LEADIN )
                        .append( str, start, pos )
                        .append( CDATA_LEADOUT )
                        .append( ']' );
                    start = pos + 1;
                }
            }
            if( start < text.length() )
            {
                retValue.append( CDATA_LEADIN )
                    .append( str.substring( start ) )
                    .append( CDATA_LEADOUT );
            }
        }

        //---* Done *----------------------------------------------------------
        return retValue.toString();
    }   //  toCDATA()

    /**
     *  Returns the children as a single formatted string.
     *
     *  @param  indentationLevel    The indentation level.
     *  @param  prettyPrint The pretty print flag.
     *  @return The children string.
     */
    public final String toString( final int indentationLevel, final boolean prettyPrint )
    {
        final var retValue = composeChildrenString( indentationLevel, prettyPrint, m_Owner, getChildren() );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  toString()
}
//  class ChildSupport

/*
 *  End of File
 */