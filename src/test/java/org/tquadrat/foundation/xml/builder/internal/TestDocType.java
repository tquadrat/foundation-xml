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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.net.URI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.builder.spi.InvalidXMLNameException;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Tests for the class
 *  {@link DocType}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestDocType.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestDocType.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.internal.TextDocType" )
public class TestDocType extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Calls some methods from
     *  {@link DocType}
     *  to reach the coverage ratio.
     */
    @Test
    final void cover()
    {
        skipThreadTest();

        DocType candidate = null;
        final var elementName = "root";
        final var dtdName = "dtd";
        final var uri = URI.create( "tquadrat.org/foundation" );

        candidate = new DocType( elementName, dtdName, uri );
        assertNotNull( candidate );

        assertTrue( candidate.getChildren().isEmpty() );
        assertTrue( candidate.getNamespaces().isEmpty() );
        assertFalse( candidate.hasChildren() );
        assertTrue( candidate.isBlock() );
        assertFalse( candidate.getAttribute( "attribute" ).isPresent() );
        assertTrue( candidate.getAttributes().isEmpty() );
    }   //  cover(()

    /**
     *  Tests for the constructors
     *  {@link DocType#DocType(String,URI)}
     *  and
     *  {@link DocType#DocType(String,String,URI)}
     */
    @Test
    final void testConstructor()
    {
        skipThreadTest();

        DocType candidate = null;
        final var elementName = "root";
        final var dtdName = "dtd";
        final var uri = URI.create( "tquadrat.org/foundation" );

        candidate = new DocType( elementName, uri );
        assertNotNull( candidate );
        assertEquals( "DOCTYPE", candidate.getElementName() );

        candidate = new DocType( elementName, dtdName, uri );
        assertNotNull( candidate );
        assertEquals( "DOCTYPE", candidate.getElementName() );
    }   //  testConstructor()

    /**
     *  Tests for the constructors
     *  {@link DocType#DocType(String,URI)}
     *  and
     *  {@link DocType#DocType(String,String,URI)}
     */
    @Test
    final void testConstructorWithEmptyArgument()
    {
        skipThreadTest();

        @SuppressWarnings( "unused" )
        DocType candidate = null;
        final var elementName = "root";
        final var dtdName = "dtd";
        final var uri = URI.create( "tquadrat.org/foundation" );

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            candidate = new DocType( EMPTY_STRING, uri );
            assertNotNull( candidate );
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
            candidate = new DocType( EMPTY_STRING, dtdName, uri );
            assertNotNull( candidate );
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
            candidate = new DocType( elementName, EMPTY_STRING, uri );
            assertNotNull( candidate );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testConstructorWithEmptyArgument()

    /**
     *  Tests for the constructors
     *  {@link DocType#DocType(String,URI)}
     *  and
     *  {@link DocType#DocType(String,String,URI)}
     *
     *  @param  elementName The element name for the doc type, used for the
     *      element name AND the dtd name.
     */
    @ParameterizedTest
    @ValueSource( strings =
    {
        "xml", "1", " ", "With a blank", "xmlElement", "?", "|"
    })
    final void testConstructorWithInvalidArgument( final String elementName )
    {
        skipThreadTest();

        @SuppressWarnings( "unused" )
        DocType candidate = null;
        final var uri = URI.create( "tquadrat.org/foundation" );

        final Class<? extends Throwable> expectedException = InvalidXMLNameException.class;
        try
        {
            candidate = new DocType( elementName, uri );
            assertNotNull( candidate );
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
            candidate = new DocType( elementName, "dtd", uri );
            assertNotNull( candidate );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testConstructorWithInvalidArgument()

    /**
     *  Tests for the constructors
     *  {@link DocType#DocType(String,URI)}
     *  and
     *  {@link DocType#DocType(String,String,URI)}
     */
    @Test
    final void testConstructorWithNullArgument()
    {
        skipThreadTest();

        @SuppressWarnings( "unused" )
        DocType candidate = null;
        final var elementName = "root";
        final var dtdName = "dtd";
        final var uri = URI.create( "tquadrat.org/foundation" );

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate = new DocType( null, uri );
            assertNotNull( candidate );
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
            candidate = new DocType( elementName, null );
            assertNotNull( candidate );
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
            candidate = new DocType( null, dtdName, uri );
            assertNotNull( candidate );
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
            candidate = new DocType( elementName, null, uri );
            assertNotNull( candidate );
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
            candidate = new DocType( elementName, dtdName, null );
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
     *  Tests for the methods
     *  {@link DocType#toString(int, boolean)}
     *  and
     *  {@link DocType#toString()}.
     */
    @Test
    final void testToString()
    {
        skipThreadTest();

        DocType candidate = null;
        final var elementName = "root";
        final var dtdName = "dtd";
        final var uri = URI.create( "tquadrat.org/foundation" );
        String actual, expected;

        candidate = new DocType( elementName, uri );
        assertNotNull( candidate );
        expected = "\n<!DOCTYPE root SYSTEM \"tquadrat.org/foundation\">";
        actual = candidate.toString();
        assertEquals( expected, actual );
        expected = "<!DOCTYPE root SYSTEM \"tquadrat.org/foundation\">";
        actual = candidate.toString( 0, false );
        assertEquals( expected, actual );
        expected = "\n    <!DOCTYPE root SYSTEM \"tquadrat.org/foundation\">";
        actual = candidate.toString( 1, true );
        assertEquals( expected, actual );

        candidate = new DocType( elementName, dtdName, uri );
        assertNotNull( candidate );
        expected = "\n<!DOCTYPE root PUBLIC \"dtd\" \"tquadrat.org/foundation\">";
        actual = candidate.toString();
        assertEquals( expected, actual );
        expected = "<!DOCTYPE root PUBLIC \"dtd\" \"tquadrat.org/foundation\">";
        actual = candidate.toString( 0, false );
        assertEquals( expected, actual );
        expected = "\n    <!DOCTYPE root PUBLIC \"dtd\" \"tquadrat.org/foundation\">";
        actual = candidate.toString( 1, true );
        assertEquals( expected, actual );
    }   //  testToString()
}
//  class TestDocType

/*
 *  End of File
 */