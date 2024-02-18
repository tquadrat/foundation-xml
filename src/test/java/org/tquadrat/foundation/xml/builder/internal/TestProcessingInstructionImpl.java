/*
 * ============================================================================
 *  Copyright © 2002-2024 by Thomas Thrien.
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

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_CHARSEQUENCE;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.builder.ProcessingInstruction;
import org.tquadrat.foundation.xml.builder.spi.InvalidXMLNameException;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Tests for the class
 *  {@link ProcessingInstructionImpl}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestProcessingInstructionImpl.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestProcessingInstructionImpl.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.internal.TestProcessingInstructionImpl" )
public class TestProcessingInstructionImpl extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Calls some methods from
     *  {@link ProcessingInstructionImpl}
     *  to reach the coverage ratio.
     */
    @Test
    final void cover()
    {
        skipThreadTest();

        final ProcessingInstruction candidate;
        final var elementName = "processingInstruction";

        candidate = new ProcessingInstructionImpl( elementName );
        assertNotNull( candidate );

        assertTrue( candidate.getChildren().isEmpty() );
        assertTrue( candidate.getNamespaces().isEmpty() );
        assertFalse( candidate.hasChildren() );
        assertTrue( candidate.isBlock() );
        assertFalse( candidate.getAttribute( "attribute" ).isPresent() );
    }   //  cover(()

    /**
     *  Tests for the constructors
     *  {@link ProcessingInstructionImpl#ProcessingInstructionImpl(String)}
     *  and
     *  {@link ProcessingInstructionImpl#ProcessingInstructionImpl(String, CharSequence)}.
     */
    @Test
    final void testConstructor()
    {
        skipThreadTest();

        ProcessingInstruction candidate;
        final var elementName = "processingInstruction";
        final CharSequence data = "data";

        candidate = new ProcessingInstructionImpl();
        assertNotNull( candidate );
        assertEquals( "xml", candidate.getElementName() );

        candidate = new ProcessingInstructionImpl( elementName );
        assertNotNull( candidate );
        assertEquals( elementName, candidate.getElementName() );

        candidate = new ProcessingInstructionImpl( elementName, null );
        assertNotNull( candidate );
        assertEquals( elementName, candidate.getElementName() );

        candidate = new ProcessingInstructionImpl( elementName, EMPTY_CHARSEQUENCE );
        assertNotNull( candidate );
        assertEquals( elementName, candidate.getElementName() );

        candidate = new ProcessingInstructionImpl( elementName, data );
        assertNotNull( candidate );
        assertEquals( elementName, candidate.getElementName() );
    }   //  testConstructor()

    /**
     *  Tests for the constructors
     *  {@link ProcessingInstructionImpl#ProcessingInstructionImpl(String)}
     *  and
     *  {@link ProcessingInstructionImpl#ProcessingInstructionImpl(String, CharSequence)}.
     */
    @Test
    final void testConstructorWithEmptyArgument()
    {
        skipThreadTest();

        @SuppressWarnings( "unused" )
        ProcessingInstruction candidate = null;

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            candidate = new ProcessingInstructionImpl( EMPTY_STRING );
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
            candidate = new ProcessingInstructionImpl( EMPTY_STRING, null );
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
     *  {@link ProcessingInstructionImpl#ProcessingInstructionImpl(String)}
     *  and
     *  {@link ProcessingInstructionImpl#ProcessingInstructionImpl(String, CharSequence)}.
     *
     *  @param  elementName The element name for the processing instruction.
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
        ProcessingInstruction candidate = null;

        final Class<? extends Throwable> expectedException = InvalidXMLNameException.class;
        try
        {
            candidate = new ProcessingInstructionImpl( elementName );
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
            candidate = new ProcessingInstructionImpl( elementName, null );
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
            candidate = new ProcessingInstructionImpl( elementName, EMPTY_STRING );
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
            candidate = new ProcessingInstructionImpl( elementName, "data" );
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
     *  {@link ProcessingInstructionImpl#ProcessingInstructionImpl(String)}
     *  and
     *  {@link ProcessingInstructionImpl#ProcessingInstructionImpl(String, CharSequence)}.
     */
    @Test
    final void testConstructorWithNullArgument()
    {
        skipThreadTest();

        @SuppressWarnings( "unused" )
        ProcessingInstruction candidate = null;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate = new ProcessingInstructionImpl( null );
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
            candidate = new ProcessingInstructionImpl( null, null );
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
            candidate = new ProcessingInstructionImpl( null, EMPTY_STRING );
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
            candidate = new ProcessingInstructionImpl( null, "data" );
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
     *  {@link ProcessingInstructionImpl#toString(int, boolean)}
     *  and
     *  {@link ProcessingInstructionImpl#toString()}.
     */
    @Test
    final void testToString()
    {
        skipThreadTest();

        ProcessingInstruction candidate;
        final var elementName = "processingInstruction";
        final CharSequence data = "data";

        candidate = new ProcessingInstructionImpl();
        assertNotNull( candidate );
        assertEquals( "xml", candidate.getElementName() );
        assertEquals( "<?xml?>\n", candidate.toString() );
        assertEquals( "<?xml?>", candidate.toString( 0, false ) );
        assertEquals( "    <?xml?>\n", candidate.toString( 1, true ) );

        candidate = new ProcessingInstructionImpl( elementName );
        assertNotNull( candidate );
        assertEquals( elementName, candidate.getElementName() );
        assertEquals( "<?" + elementName + "?>\n", candidate.toString() );
        assertEquals( "<?" + elementName + "?>", candidate.toString( 0, false ) );
        assertEquals( "    <?" + elementName + "?>\n", candidate.toString( 1, true ) );

        candidate = new ProcessingInstructionImpl( elementName, null );
        assertNotNull( candidate );
        assertEquals( elementName, candidate.getElementName() );
        assertEquals( "<?" + elementName + "?>\n", candidate.toString() );
        assertEquals( "    <?" + elementName + "?>\n", candidate.toString( 1, true ) );

        candidate = new ProcessingInstructionImpl( elementName, EMPTY_CHARSEQUENCE );
        assertNotNull( candidate );
        assertEquals( elementName, candidate.getElementName() );
        assertEquals( "<?" + elementName + "?>\n", candidate.toString() );
        assertEquals( "    <?" + elementName + "?>\n", candidate.toString( 1, true ) );

        candidate = new ProcessingInstructionImpl( elementName, data );
        assertNotNull( candidate );
        assertEquals( elementName, candidate.getElementName() );
        assertEquals( "<?" + elementName + " " + data + "?>\n", candidate.toString() );
        assertEquals( "    <?" + elementName + " " + data + "?>\n", candidate.toString( 1, true ) );

        candidate = new ProcessingInstructionImpl( elementName );
        assertNotNull( candidate );
        assertEquals( elementName, candidate.getElementName() );
        candidate.setAttribute( "attribute1", true );
        assertEquals( "<?" + elementName + " attribute1='true'?>\n", candidate.toString() );
        assertEquals( "<?" + elementName + " attribute1='true'?>", candidate.toString( 0, false ) );
        assertEquals( "    <?" + elementName + " attribute1='true'?>\n", candidate.toString( 1, true ) );

        candidate.setAttribute( "attribute2", true );
        assertEquals( "<?" + elementName + " attribute1='true'\n                        attribute2='true'?>\n", candidate.toString() );
        assertEquals( "<?" + elementName + " attribute1='true' attribute2='true'?>", candidate.toString( 0, false ) );
        assertEquals( "    <?" + elementName + " attribute1='true'\n                            attribute2='true'?>\n", candidate.toString( 1, true ) );
    }   //  testToString()
}
//  class TestProcessingInstructionImpl

/*
 *  End of File
 */