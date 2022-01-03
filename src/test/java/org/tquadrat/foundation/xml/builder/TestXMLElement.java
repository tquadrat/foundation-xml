/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
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

import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.xml.builder.XMLElement.NO_APPEND;

import java.net.URI;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.IllegalOperationException;
import org.tquadrat.foundation.xml.builder.spi.XMLElementBase;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Some tests for the methods in the interface
 *  {@link XMLElement}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestXMLElement.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "removal" )
@ClassVersion( sourceVersion = "$Id: TestXMLElement.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.TestXMLElement" )
public class TestXMLElement extends XMLTestBase
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  A quite stupid implementation for the interface
     *  {@link XMLElement}
     *  only for test purposes.
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: TestXMLElement.java 820 2020-12-29 20:34:22Z tquadrat $
     */
    @ClassVersion( sourceVersion = "$Id: TestXMLElement.java 820 2020-12-29 20:34:22Z tquadrat $" )
    private static final class SimpleXMLElement extends XMLElementBase
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code SimpleXMLElement} instance with the name
         *  &quot;{@code element}&quot;.
         */
        public SimpleXMLElement() { this( "element" ); }

        /**
         *  Creates a new {@code SimpleXMLElement} instance.<br>
         *  <br>The given element name is validated using the method that is
         *  provided by
         *  {@link XMLBuilderUtils#getElementNameValidator()}.
         *
         *  @param elementName The element name.
         */
        public SimpleXMLElement( final String elementName )
        {
            super( elementName );
        }   //  SimpleXMLElement()

        /**
         *  Creates a new {@code SimpleXMLElement} instance with the name
         *  &quot;{@code element}&quot;.<br>
         *  <br>The given element name is validated using the method that is
         *  provided by
         *  {@link XMLBuilderUtils#getElementNameValidator()}.
         *
         *  @param  validChildren   The list of the names for valid children;
         *      if {@code null}, no children are allowed, if empty, children are
         *      allowed, but they will not validated.
         *  @param  validAttributes The list of the valid attributes; if empty
         *      or {@code null}, the attributes will not validated.
         *  @param  attributeSequence   The sequence for the attributes; if
         *      empty or {@code null}, the attributes will be sorted
         *      alphabetically.
         *  @param  allowText   {@code true} if the element allows text,
         *      {@code false} if not.
         *
         * @see org.tquadrat.foundation.xml.builder.spi.AttributeSupport#registerAttributes(String...)
         * @see org.tquadrat.foundation.xml.builder.spi.AttributeSupport#registerSequence(String...)
         * @see org.tquadrat.foundation.xml.builder.spi.ChildSupport#registerChildren(String...)
         */
        public SimpleXMLElement( final String[] validChildren, final String[] validAttributes, final String[] attributeSequence, final boolean allowText )
        {
            this( "element", validChildren, validAttributes, attributeSequence, allowText );
        }   //  SimpleXMLElement()

        /**
         *  Creates a new {@code SimpleXMLElement} instance.<br>
         *  <br>The given element name is validated using the method that is
         *  provided by
         *  {@link XMLBuilderUtils#getElementNameValidator()}.
         *
         *  @param  elementName The element name.
         *  @param  validChildren   The list of the names for valid children;
         *      if {@code null}, no children are allowed, if empty, children are
         *      allowed, but they will not validated.
         *  @param  validAttributes The list of the valid attributes; if empty
         *      or {@code null}, the attributes will not validated.
         *  @param  attributeSequence   The sequence for the attributes; if
         *      empty or {@code null}, the attributes will be sorted
         *      alphabetically.
         *  @param  allowText   {@code true} if the element allows text,
         *      {@code false} if not.
         *
         * @see org.tquadrat.foundation.xml.builder.spi.AttributeSupport#registerAttributes(String...)
         * @see org.tquadrat.foundation.xml.builder.spi.AttributeSupport#registerSequence(String...)
         * @see org.tquadrat.foundation.xml.builder.spi.ChildSupport#registerChildren(String...)
         */
        public SimpleXMLElement( final String elementName, final String[] validChildren, final String[] validAttributes, final String[] attributeSequence, final boolean allowText )
        {
            super( elementName, validChildren, validAttributes, attributeSequence, allowText );
        }   //  SimpleXMLElement()

        /**
         *  Creates a new {@code SimpleXMLElement} instance with the name
         *  &quot;{@code element}&quot;.<br>
         *  <br>The given element name is validated using the method that is
         *  provided by
         *  {@link XMLBuilderUtils#getElementNameValidator()}.
         *
         *  @param  flags   The flags that are used to configure the new
         *      instance.
         *
         * @see org.tquadrat.foundation.xml.builder.spi.AttributeSupport#registerAttributes(String...)
         * @see org.tquadrat.foundation.xml.builder.spi.AttributeSupport#registerSequence(String...)
         * @see org.tquadrat.foundation.xml.builder.spi.ChildSupport#registerChildren(String...)
         */
        public SimpleXMLElement( Set<Flags> flags )
        {
            this( "element", flags );
        }   //  SimpleXMLElement()

        /**
         *  Creates a new {@code SimpleXMLElement} instance.<br>
         *  <br>The given element name is validated using the method that is
         *  provided by
         *  {@link XMLBuilderUtils#getElementNameValidator()}.
         *
         *  @param  elementName The element name.
         *  @param  flags   The flags that are used to configure the new
         *      instance.
         *
         * @see org.tquadrat.foundation.xml.builder.spi.AttributeSupport#registerAttributes(String...)
         * @see org.tquadrat.foundation.xml.builder.spi.AttributeSupport#registerSequence(String...)
         * @see org.tquadrat.foundation.xml.builder.spi.ChildSupport#registerChildren(String...)
         */
        public SimpleXMLElement( final String elementName, Set<Flags> flags )
        {
            super( elementName, flags );
        }   //  SimpleXMLElement()
    }
    //  class SimpleXMLElement

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test for the method
     *  {@link XMLElement#setId(String)}.
     *
     */
    @Test
    final void setIdFail()
    {
        skipThreadTest();

        final XMLElement candidate = new SimpleXMLElement();

        final Class<? extends Throwable> expectedException = IllegalArgumentException.class;
        try
        {
            candidate.setId( "1234" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
            assertTrue( t.getMessage().contains( "1234" ) );
        }
    }   //  setIdFail()

    /**
     *  Tries to add something to an element that does not allow that something
     *  is added to it.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testAddSomethingFail() throws Exception
    {
        skipThreadTest();

        /*
         * An element that does not allow children or attributes, not even
         * text.
         */
        final XMLElement candidate = new SimpleXMLElement( null, null, null, false );

        final Class<? extends Throwable> expectedException = IllegalOperationException.class;
        try
        {
            candidate.addCDATA( "CDATA" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
        try
        {
            candidate.addChild( new SimpleXMLElement() );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        //---* Comments can be added always *----------------------------------
        candidate.addComment( "comment" );

        try
        {
            candidate.addPredefinedMarkup( "<child />" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
        try
        {
            candidate.addText( "text" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        //---* Attributes are always allowed *---------------------------------
        candidate.setAttribute( "attribute", "value", NO_APPEND );

        //---* Namespaces are always allowed *---------------------------------
        candidate.setNamespace( new Namespace( URI.create( "tquadrat.org/foundation" ) ) );
        candidate.setNamespace( "prefix", "tquadrat.org/foundation" );
        candidate.setNamespace( "prefix", URI.create( "tquadrat.org/foundation" ) );
        candidate.setNamespace( "tquadrat.org/foundation" );
        candidate.setNamespace( URI.create( "tquadrat.org/foundation" ));
    }   //  testAddSomethingFail()

    /**
     *  Tries to add something to an element that does not allow that something
     *  is added to it.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testAddSomethingFailNew() throws Exception
    {
        skipThreadTest();

        /*
         * An element that does not allow children or attributes, not even
         * text.
         */
        final XMLElement candidate = new SimpleXMLElement( emptySet() );

        final Class<? extends Throwable> expectedException = IllegalOperationException.class;
        try
        {
            candidate.addCDATA( "CDATA" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
        try
        {
            candidate.addChild( new SimpleXMLElement() );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        //---* Comments can be added always *----------------------------------
        candidate.addComment( "comment" );

        try
        {
            candidate.addPredefinedMarkup( "<child />" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
        try
        {
            candidate.addText( "text" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        //---* Attributes are always allowed *---------------------------------
        candidate.setAttribute( "attribute", "value", NO_APPEND );

        //---* Namespaces are always allowed *---------------------------------
        candidate.setNamespace( new Namespace( URI.create( "tquadrat.org/foundation" ) ) );
        candidate.setNamespace( "prefix", "tquadrat.org/foundation" );
        candidate.setNamespace( "prefix", URI.create( "tquadrat.org/foundation" ) );
        candidate.setNamespace( "tquadrat.org/foundation" );
        candidate.setNamespace( URI.create( "tquadrat.org/foundation" ));
    }   //  testAddSomethingFailNew()
}
//  class TestXMLElement

/*
 *  End of File
 */