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

package org.tquadrat.foundation.xml.builder.xmlbuilderutils;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.createXMLDocument;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.createXMLElement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.builder.XMLBuilderUtils;
import org.tquadrat.foundation.xml.builder.XMLDocument;
import org.tquadrat.foundation.xml.builder.XMLElement;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Some tests for the class
 *  {@link XMLBuilderUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestCreateXMLElement.java 1030 2022-04-06 13:42:02Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestCreateXMLElement.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.xmlbuilderutils.TestCreateXMLElement" )
public class TestCreateXMLElement extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link XMLBuilderUtils#createXMLElement(String)},
     *  {@link XMLBuilderUtils#createXMLElement(String, CharSequence)},
     *  {@link XMLBuilderUtils#createXMLElement(String, XMLElement)},
     *  {@link XMLBuilderUtils#createXMLElement(String, XMLElement, CharSequence)}.
     *  {@link XMLBuilderUtils#createXMLElement(String, XMLDocument)},
     *  and
     *  {@link XMLBuilderUtils#createXMLElement(String, XMLDocument, CharSequence)}.
     */
    @Test
    final void testCreateXMLElement()
    {
        skipThreadTest();

        String actual, expected;
        XMLElement candidate;
        final XMLElement parent;
        parent = createXMLElement( "parent" );
        final var document = createXMLDocument();

        candidate = createXMLElement( "element" );
        expected = "\n<element/>";
        assertNotNull( candidate );
        actual = candidate.toString();
        assertEquals( expected, actual );

        candidate = createXMLElement( "element", parent );
        expected = "\n<element/>";
        assertNotNull( candidate );
        actual = candidate.toString();
        assertEquals( expected, actual );

        candidate = createXMLElement( "element", document );
        expected = "\n<element/>";
        assertNotNull( candidate );
        actual = candidate.toString();
        assertEquals( expected, actual );


        candidate = createXMLElement( "element", EMPTY_STRING );
        expected = "\n<element/>";
        assertNotNull( candidate );
        actual = candidate.toString();
        assertEquals( expected, actual );

        candidate = createXMLElement( "element", parent, EMPTY_STRING );
        expected = "\n<element/>";
        assertNotNull( candidate );
        actual = candidate.toString();
        assertEquals( expected, actual );

        candidate = createXMLElement( "element", document, EMPTY_STRING );
        expected = "\n<element/>";
        assertNotNull( candidate );
        actual = candidate.toString();
        assertEquals( expected, actual );


        candidate = createXMLElement( "element", "text" );
        expected = "\n<element>text</element>";
        assertNotNull( candidate );
        actual = candidate.toString();
        assertEquals( expected, actual );

        candidate = createXMLElement( "element", parent, "text" );
        expected = "\n<element>text</element>";
        assertNotNull( candidate );
        actual = candidate.toString();
        assertEquals( expected, actual );

        candidate = createXMLElement( "element", document, "text" );
        expected = "\n<element>text</element>";
        assertNotNull( candidate );
        actual = candidate.toString();
        assertEquals( expected, actual );
    }   //  testCreateXMLElement()

    /**
     *  Tests for
     *  {@link XMLBuilderUtils#createXMLElement(String)},
     *  {@link XMLBuilderUtils#createXMLElement(String, CharSequence)},
     *  {@link XMLBuilderUtils#createXMLElement(String, XMLElement)},
     *  {@link XMLBuilderUtils#createXMLElement(String, XMLElement, CharSequence)}.
     *  {@link XMLBuilderUtils#createXMLElement(String, XMLDocument)},
     *  and
     *  {@link XMLBuilderUtils#createXMLElement(String, XMLDocument, CharSequence)}.
     */
    @SuppressWarnings( "OverlyComplexMethod" )
    @Test
    final void testCreateXMLElementWithEmptyElement()
    {
        skipThreadTest();

        final var document = createXMLDocument();

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            createXMLElement( EMPTY_STRING );
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
            createXMLElement( EMPTY_STRING, EMPTY_STRING );
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
            createXMLElement( EMPTY_STRING, "text" );
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
            createXMLElement( EMPTY_STRING, createXMLElement( "parent" ) );
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
            createXMLElement( EMPTY_STRING, createXMLElement( "parent" ), EMPTY_STRING );
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
            createXMLElement( EMPTY_STRING, createXMLElement( "parent" ), "text" );
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
            createXMLElement( EMPTY_STRING, document );
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
            createXMLElement( EMPTY_STRING, document, EMPTY_STRING );
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
            createXMLElement( EMPTY_STRING, document, "text" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testCreateXMLElementWithEmptyElement()

    /**
     *  Tests for
     *  {@link XMLBuilderUtils#createXMLElement(String)},
     *  {@link XMLBuilderUtils#createXMLElement(String, CharSequence)},
     *  {@link XMLBuilderUtils#createXMLElement(String, XMLElement)},
     *  {@link XMLBuilderUtils#createXMLElement(String, XMLElement, CharSequence)}.
     *  {@link XMLBuilderUtils#createXMLElement(String, XMLDocument)},
     *  and
     *  {@link XMLBuilderUtils#createXMLElement(String, XMLDocument, CharSequence)}.
     */
    @SuppressWarnings( {"OverlyLongMethod", "OverlyComplexMethod"} )
    @Test
    final void testCreateXMLElementWithNullArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            createXMLElement( null );
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
            createXMLElement( null, EMPTY_STRING );
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
            createXMLElement( null, "text" );
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
            createXMLElement( "element", (CharSequence) null );
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
            createXMLElement( "element", (XMLElement) null );
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
            createXMLElement( "element", (XMLDocument) null );
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
            createXMLElement( null, createXMLElement( "parent" ) );
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
            createXMLElement( null, createXMLDocument() );
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
            createXMLElement( null, createXMLElement( "parent" ), EMPTY_STRING );
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
            createXMLElement( null, createXMLElement( "parent" ), "text" );
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
            createXMLElement( null, createXMLDocument(), EMPTY_STRING );
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
            createXMLElement( null, createXMLDocument(), "text" );
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
            createXMLElement( "element", (XMLElement) null, EMPTY_STRING );
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
            createXMLElement( "element", (XMLElement) null, "text" );
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
            createXMLElement( "element", (XMLDocument) null, EMPTY_STRING );
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
            createXMLElement( "element", (XMLDocument) null, "text" );
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
            createXMLElement( "element", createXMLElement( "parent" ), null );
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
            createXMLElement( "element", createXMLDocument(), null );
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
//  class TestCreateXMLElement

/*
 *  End of File
 */