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

import static java.lang.String.format;
import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.createXMLDocument;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.createXMLElement;

import java.net.URI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.builder.spi.InvalidXMLNameException;
import org.tquadrat.foundation.xml.builder.spi.XMLElementFactoryBase;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Some tests for the interface
 *  {@link org.tquadrat.foundation.xml.builder.XMLElementFactory}
 *  and the class
 *  {@link XMLElementFactoryBase}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestXMLElementFactory.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestXMLElementFactory.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.TestXMLElementFactory" )
public class TestXMLElementFactory extends XMLTestBase
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  A basic implementation of
     *  {@link XMLElementFactory}
     *  for testing purposes; it uses only the default capabilities.
     */
    @SuppressWarnings( "PublicInnerClass" )
    public static final class SimpleXMLElementFactory extends XMLElementFactoryBase
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code SimpleXMLElementFactory} instance that does
         *  not use a namespace.
         */
        public SimpleXMLElementFactory() { super(); }

        /**
         *  Creates a new {@code SimpleXMLElementFactory} instance that uses
         *  the given namespace.
         *
         *  @param  namespace   The namespace that is used by this XML element
         *      factory.
         */
        @SuppressWarnings( "UseOfConcreteClass" )
        public SimpleXMLElementFactory( final Namespace namespace ) { super( namespace ); }
    }
    //  class SimpleXMLElementFactory

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Creates an instance of
     *  {@link SimpleXMLElementFactory}.
     *
     *  @return The XML element factory.
     */
    @SuppressWarnings( "static-method" )
    private final XMLElementFactory createXMLElementFactory() { return new SimpleXMLElementFactory(); }

    /**
     *  Creates an instance of
     *  {@link SimpleXMLElementFactory}
     *  that uses the given namespace.
     *
     *  @param  namespace   The namespace to use.
     *  @return The XML element factory.
     */
    @SuppressWarnings( {"static-method", "UseOfConcreteClass"} )
    private final XMLElementFactory createXMLElementFactory( final Namespace namespace ) { return new SimpleXMLElementFactory( namespace ); }

    /**
     *  Tests for
     *  {@link XMLElementFactory#composeElementName(String)}.
     *
     *  @param  elementName The name of the new element.
     */
    @SuppressWarnings( "OverlyComplexMethod" )
    @ParameterizedTest
    @ValueSource( strings =
    {
        "xml", "1", " ", "With a blank", "xmlElement", "?", "|", " LeadingBlank", "Two:many:colons", ":", "with:prefix", "colonAtTheEnd:", ":colonAtTheBeginning"
    })
    final void testComposeElementNameWithInvalidArgument( final String elementName)
    {
        skipThreadTest();

        XMLElementFactory candidate;
        Namespace namespace;
        final var prefix = "prefix";
        final var uri = URI.create( "tquadrat.org/foundation" );

        final var pos = elementName.indexOf( ":" );
        final var posLast = elementName.lastIndexOf( ":" );
        final var validWithoutPrefix = pos > 0 && pos == posLast && !elementName.endsWith( ":" );

        final Class<? extends Throwable> expectedException = InvalidXMLNameException.class;

        candidate = createXMLElementFactory();
        if( !validWithoutPrefix )
        {
            try
            {
                candidate.composeElementName( elementName );
                fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
            }
            catch( final AssertionError e ) { throw e; }
            catch( final Throwable t )
            {
                final var isExpectedException = expectedException.isInstance( t );
                assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
            }
        }

        namespace = new Namespace( uri );
        candidate = createXMLElementFactory( namespace );
        if( !validWithoutPrefix )
        {
            try
            {
                candidate.composeElementName( elementName );
                fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
            }
            catch( final AssertionError e ) { throw e; }
            catch( final Throwable t )
            {
                final var isExpectedException = expectedException.isInstance( t );
                assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
            }
        }

        namespace = new Namespace( prefix, uri );
        candidate = createXMLElementFactory( namespace );
        try
        {
            candidate.composeElementName( elementName );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testComposeElementNameWithInvalidArgument()

    /**
     *  Tests for
     *  {@link XMLElementFactoryBase#XMLElementFactoryBase(Namespace)}.
     */
    @SuppressWarnings( "JUnitTestMethodWithNoAssertions" )
    @Test
    final void testConstructor()
    {
        skipThreadTest();
    }   //  testConstructor()

    /**
     *  Tests for
     *  {@link XMLElementFactoryBase#XMLElementFactoryBase(Namespace)}.
     */
    @Test
    final void testConstructorWithNullArgument()
    {
        skipThreadTest();

        @SuppressWarnings( "unused" ) final XMLElementFactoryBase candidate;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate = new SimpleXMLElementFactory( null );
            assertNotNull( candidate );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testConstructorWithNullArgument()

    /**
     *  Tests for
     *  {@link XMLElementFactory#createXMLElement(String)},
     *  {@link XMLElementFactory#createXMLElement(String, CharSequence)},
     *  {@link XMLElementFactory#createXMLElement(String, XMLElement)},
     *  {@link XMLElementFactory#createXMLElement(String, XMLElement, CharSequence)}.
     *  {@link XMLElementFactory#createXMLElement(String, XMLDocument)},
     *  and
     *  {@link XMLElementFactory#createXMLElement(String, XMLDocument, CharSequence)}.
     */
    @SuppressWarnings( "OverlyLongMethod" )
    @Test
    final void testCreateXMLElement()
    {
        skipThreadTest();

        XMLElementFactory candidate;
        String actual;
        String expected;
        final String prefix;
        Namespace namespace;

        XMLElement element;
        final XMLElement parent;
        parent = createXMLElement( "parent" );
        final var document = createXMLDocument();
        final var uri = URI.create( "tquadrat.org/foundation" );

        //---------------------------------------------------------------------
        candidate = createXMLElementFactory();

        element = candidate.createXMLElement( "element" );
        expected = "\n<element/>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        element = candidate.createXMLElement( "element", parent );
        expected = "\n<element/>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        element = candidate.createXMLElement( "element", document );
        expected = "\n<element/>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        element = candidate.createXMLElement( "element", EMPTY_STRING );
        expected = "\n<element/>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        element = candidate.createXMLElement( "element", parent, EMPTY_STRING );
        expected = "\n<element/>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        element = candidate.createXMLElement( "element", document, EMPTY_STRING );
        expected = "\n<element/>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );


        element = candidate.createXMLElement( "element", "text" );
        expected = "\n<element>text</element>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        element = candidate.createXMLElement( "element", parent, "text" );
        expected = "\n<element>text</element>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        element = candidate.createXMLElement( "element", document, "text" );
        expected = "\n<element>text</element>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        //---------------------------------------------------------------------
        namespace = new Namespace( uri );
        candidate = createXMLElementFactory( namespace );

        element = candidate.createXMLElement( "element" );
        expected = "\n<element/>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        element = candidate.createXMLElement( "element", parent );
        expected = "\n<element/>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        element = candidate.createXMLElement( "element", document );
        expected = "\n<element/>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        element = candidate.createXMLElement( "element", EMPTY_STRING );
        expected = "\n<element/>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        element = candidate.createXMLElement( "element", parent, EMPTY_STRING );
        expected = "\n<element/>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        element = candidate.createXMLElement( "element", document, EMPTY_STRING );
        expected = "\n<element/>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );


        element = candidate.createXMLElement( "element", "text" );
        expected = "\n<element>text</element>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        element = candidate.createXMLElement( "element", parent, "text" );
        expected = "\n<element>text</element>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        element = candidate.createXMLElement( "element", document, "text" );
        expected = "\n<element>text</element>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        //---------------------------------------------------------------------
        prefix = "prefix";
        namespace = new Namespace( prefix, uri );
        candidate = createXMLElementFactory( namespace );

        element = candidate.createXMLElement( "element" );
        expected = "\n<prefix:element/>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        element = candidate.createXMLElement( "element", parent );
        expected = "\n<prefix:element/>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        element = candidate.createXMLElement( "element", document );
        expected = "\n<prefix:element/>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        element = candidate.createXMLElement( "element", EMPTY_STRING );
        expected = "\n<prefix:element/>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        element = candidate.createXMLElement( "element", parent, EMPTY_STRING );
        expected = "\n<prefix:element/>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        element = candidate.createXMLElement( "element", document, EMPTY_STRING );
        expected = "\n<prefix:element/>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );


        element = candidate.createXMLElement( "element", "text" );
        expected = "\n<prefix:element>text</prefix:element>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        element = candidate.createXMLElement( "element", parent, "text" );
        expected = "\n<prefix:element>text</prefix:element>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );

        element = candidate.createXMLElement( "element", document, "text" );
        expected = "\n<prefix:element>text</prefix:element>";
        assertNotNull( element );
        actual = element.toString();
        assertEquals( expected, actual );
    }   //  testCreateXMLElement()

    /**
     *  Tests for
     *  {@link XMLElementFactory#createXMLElement(String)},
     *  {@link XMLElementFactory#createXMLElement(String, CharSequence)},
     *  {@link XMLElementFactory#createXMLElement(String, XMLElement)},
     *  {@link XMLElementFactory#createXMLElement(String, XMLElement, CharSequence)}.
     *  {@link XMLElementFactory#createXMLElement(String, XMLDocument)},
     *  and
     *  {@link XMLElementFactory#createXMLElement(String, XMLDocument, CharSequence)}.
     */
    @SuppressWarnings( "OverlyComplexMethod" )
    @Test
    final void testCreateXMLElementWithEmptyArgument()
    {
        skipThreadTest();

        final var prefix = "prefix";
        final var uri = URI.create( "tquadrat.org/foundation" );
        final var namespace = new Namespace( prefix, uri );
        final var candidate = createXMLElementFactory( namespace );

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            candidate.createXMLElement( EMPTY_STRING );
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
            candidate.createXMLElement( EMPTY_STRING, EMPTY_STRING );
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
            candidate.createXMLElement( EMPTY_STRING, "text" );
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
            candidate.createXMLElement( EMPTY_STRING, createXMLElement( "parent" ) );
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
            candidate.createXMLElement( EMPTY_STRING, createXMLElement( "parent" ), EMPTY_STRING );
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
            candidate.createXMLElement( EMPTY_STRING, createXMLElement( "parent" ), "text" );
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
            candidate.createXMLElement( EMPTY_STRING, createXMLDocument() );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        try
        {
            candidate.createXMLElement( EMPTY_STRING, createXMLDocument(), EMPTY_STRING );
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
            candidate.createXMLElement( EMPTY_STRING, createXMLDocument(), "text" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testCreateXMLElementWithEmptyArgument()

    /**
     *  Tests for
     *  {@link XMLElementFactory#createXMLElement(String)},
     *  {@link XMLElementFactory#createXMLElement(String, CharSequence)},
     *  {@link XMLElementFactory#createXMLElement(String, XMLElement)},
     *  {@link XMLElementFactory#createXMLElement(String, XMLElement, CharSequence)}.
     *  {@link XMLElementFactory#createXMLElement(String, XMLDocument)},
     *  and
     *  {@link XMLElementFactory#createXMLElement(String, XMLDocument, CharSequence)}.
     */
    @SuppressWarnings( {"OverlyLongMethod", "OverlyComplexMethod"} )
    @Test
    final void testCreateXMLElementWithNullArgument()
    {
        skipThreadTest();

        final var prefix = "prefix";
        final var uri = URI.create( "tquadrat.org/foundation" );
        final var namespace = new Namespace( prefix, uri );
        final var candidate = createXMLElementFactory( namespace );

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate.createXMLElement( null );
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
            candidate.createXMLElement( null, EMPTY_STRING );
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
            candidate.createXMLElement( null, "text" );
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
            candidate.createXMLElement( "element", (CharSequence) null );
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
            candidate.createXMLElement( "element", (XMLElement) null );
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
            candidate.createXMLElement( "element", (XMLDocument) null );
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
            candidate.createXMLElement( null, createXMLElement( "parent" ) );
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
            candidate.createXMLElement( null, createXMLDocument() );
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
            candidate.createXMLElement( null, createXMLElement( "parent" ), EMPTY_STRING );
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
            candidate.createXMLElement( null, createXMLElement( "parent" ), "text" );
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
            candidate.createXMLElement( null, createXMLDocument(), EMPTY_STRING );
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
            candidate.createXMLElement( null, createXMLDocument(), "text" );
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
            candidate.createXMLElement( "element", (XMLElement) null, EMPTY_STRING );
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
            candidate.createXMLElement( "element", (XMLElement) null, "text" );
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
            candidate.createXMLElement( "element", (XMLDocument) null, EMPTY_STRING );
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
            candidate.createXMLElement( "element", (XMLDocument) null, "text" );
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
            candidate.createXMLElement( "element", createXMLElement( "parent" ), null );
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
            candidate.createXMLElement( "element", createXMLDocument(), null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testCreateXMLElementWithNullArgument()
}
//  class TestXMLElementFactory

/*
 *  End of File
 */