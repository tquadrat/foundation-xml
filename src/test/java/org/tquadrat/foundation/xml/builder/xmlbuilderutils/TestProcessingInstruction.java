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

package org.tquadrat.foundation.xml.builder.xmlbuilderutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.createProcessingInstruction;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.createXMLDocument;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.builder.ProcessingInstruction;
import org.tquadrat.foundation.xml.builder.XMLBuilderUtils;
import org.tquadrat.foundation.xml.builder.XMLDocument;
import org.tquadrat.foundation.xml.builder.internal.ProcessingInstructionImpl;
import org.tquadrat.foundation.xml.builder.spi.InvalidXMLNameException;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Some tests for the methods of the interface
 *  {@link ProcessingInstruction}
 *  and the class
 *  {@link ProcessingInstructionImpl}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestProcessingInstruction.java 1030 2022-04-06 13:42:02Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestProcessingInstruction.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.xmlbuilderutils.TestProcessingInstruction" )
public class TestProcessingInstruction extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link XMLBuilderUtils#createProcessingInstruction(String)},
     *  {@link XMLBuilderUtils#createProcessingInstruction(String, CharSequence)},
     *  {@link XMLBuilderUtils#createProcessingInstruction(XMLDocument, String)},
     *  and
     *  {@link XMLBuilderUtils#createProcessingInstruction(XMLDocument, String, CharSequence)}.
     */
    @Test
    final void testCreateProcessingInstruction()
    {
        skipThreadTest();

        final var elementName = "processingInstruction";
        final var attribute = "attribute";
        final var parent = createXMLDocument();
        final var data = "data";
        final var candidate = createProcessingInstruction( parent, elementName, data );
        assertNotNull( candidate );

        String value;
        String suffix;

        value = "3.14";
        suffix = "Double";
        candidate.setAttribute( attribute + suffix, Double.parseDouble( value ) );
        assertEquals( value, candidate.getAttribute( attribute + suffix ).get() );

        value = "3";
        suffix = "Integer";
        candidate.setAttribute( attribute + suffix, Integer.parseInt( value ) );
        assertEquals( value, candidate.getAttribute( attribute + suffix ).get() );

        value = "3";
        suffix = "Long";
        candidate.setAttribute( attribute + suffix, Long.parseLong( value ) );
        assertEquals( value, candidate.getAttribute( attribute + suffix ).get() );

        value = "true";
        suffix = "Boolean";
        candidate.setAttribute( attribute + suffix, Boolean.valueOf( value ) );
        assertEquals( value, candidate.getAttribute( attribute + suffix ).get() );
        candidate.setAttribute( attribute + suffix, (Boolean) null );
        assertFalse( candidate.getAttribute( attribute + suffix ).isPresent() );

        value = Instant.now().toString();
        suffix = "Instant";
        candidate.setAttribute( attribute + suffix, Instant.parse( value ) );
        assertEquals( value, candidate.getAttribute( attribute + suffix ).get() );
        candidate.setAttribute( attribute + suffix, (Instant) null );
        assertFalse( candidate.getAttribute( attribute + suffix ).isPresent() );

        value = LocalDate.now().toString();
        suffix = "LocalDate";
        candidate.setAttribute( attribute + suffix, LocalDate.parse( value ) );
        assertEquals( value, candidate.getAttribute( attribute + suffix ).get() );
        candidate.setAttribute( attribute + suffix, (LocalDate) null );
        assertFalse( candidate.getAttribute( attribute + suffix ).isPresent() );

        value = LocalDateTime.now().toString();
        suffix = "LocalDateTime";
        candidate.setAttribute( attribute + suffix, LocalDateTime.parse( value ) );
        assertEquals( value, candidate.getAttribute( attribute + suffix ).get() );
        candidate.setAttribute( attribute + suffix, (LocalDateTime) null );
        assertFalse( candidate.getAttribute( attribute + suffix ).isPresent() );

        value = ZonedDateTime.now().toString();
        suffix = "ZonedDateTime";
        candidate.setAttribute( attribute + suffix, ZonedDateTime.parse( value ) );
        assertEquals( value, candidate.getAttribute( attribute + suffix ).get() );
        candidate.setAttribute( attribute + suffix, (ZonedDateTime) null );
        assertFalse( candidate.getAttribute( attribute + suffix ).isPresent() );

        value = "3.14";
        suffix = "Number";
        candidate.setAttribute( attribute + suffix, Double.valueOf( value ) );
        assertEquals( value, candidate.getAttribute( attribute + suffix ).get() );
        candidate.setAttribute( attribute + suffix, (Number) null );
        assertFalse( candidate.getAttribute( attribute + suffix ).isPresent() );

        final var enumValue = Thread.State.BLOCKED;
        suffix = "Enum";
        candidate.setAttribute( attribute + suffix, enumValue );
        assertEquals( enumValue.name(), candidate.getAttribute( attribute + suffix ).get() );
        candidate.setAttribute( attribute + suffix, (Thread.State) null );
        assertFalse( candidate.getAttribute( attribute + suffix ).isPresent() );

        value = EMPTY_STRING;
        suffix = "CharSequence";
        candidate.setAttributeIfNotEmpty( attribute + suffix, value );
        assertFalse( candidate.getAttribute( attribute + suffix ).isPresent() );
        value = "value";
        candidate.setAttributeIfNotEmpty( attribute + suffix, value );
        assertEquals( value, candidate.getAttribute( attribute + suffix ).get() );
        candidate.setAttribute( attribute + suffix, (CharSequence) null );
        assertFalse( candidate.getAttribute( attribute + suffix ).isPresent() );

        candidate.setAttributeIfNotEmpty( attribute + suffix, Optional.empty() );
        assertFalse( candidate.getAttribute( attribute + suffix ).isPresent() );
        candidate.setAttributeIfNotEmpty( attribute + suffix, Optional.of( value ) );
        assertEquals( value, candidate.getAttribute( attribute + suffix ).get() );
        candidate.setAttribute( attribute + suffix, (CharSequence) null );
        assertFalse( candidate.getAttribute( attribute + suffix ).isPresent() );
    }   //  testCreateProcessingInstruction()

    /**
     *  Tests for
     *  {@link XMLBuilderUtils#createProcessingInstruction(String)},
     *  {@link XMLBuilderUtils#createProcessingInstruction(String, CharSequence)},
     *  {@link XMLBuilderUtils#createProcessingInstruction(XMLDocument, String)},
     *  and
     *  {@link XMLBuilderUtils#createProcessingInstruction(XMLDocument, String, CharSequence)}.
     */
    @Test
    final void testCreateProcessingInstructionWithEmptyArgument()
    {
        skipThreadTest();

        ProcessingInstruction candidate;
        final var parent = createXMLDocument();
        final var data = "data";

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            candidate = createProcessingInstruction( EMPTY_STRING );
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
            candidate = createProcessingInstruction( parent, EMPTY_STRING );
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
            candidate = createProcessingInstruction( EMPTY_STRING, data );
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
            candidate = createProcessingInstruction( parent, EMPTY_STRING, data );
            assertNotNull( candidate );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testCreateProcessingInstructionWithEmptyArgument()

    /**
     *  Tests for
     *  {@link XMLBuilderUtils#createProcessingInstruction(String)},
     *  {@link XMLBuilderUtils#createProcessingInstruction(String, CharSequence)},
     *  {@link XMLBuilderUtils#createProcessingInstruction(XMLDocument, String)},
     *  and
     *  {@link XMLBuilderUtils#createProcessingInstruction(XMLDocument, String, CharSequence)}.
     *
     *  @param  elementName The name of the new element.
     */
    @ParameterizedTest
    @ValueSource( strings =
    {
        "xml", "1", " ", "With a blank", "xmlElement", "?", "|", " LeadingBlank", "Two:many:colons", ":", ":LeadingColon", "TrailingColon:"
    })
    final void testCreateProcessingInstructionWithInvalidArgument( final String elementName)
    {
        skipThreadTest();

        ProcessingInstruction candidate;
        final var parent = createXMLDocument();
        final var data = "data";

        final Class<? extends Throwable> expectedException = InvalidXMLNameException.class;
        try
        {
            candidate = createProcessingInstruction( elementName );
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
            candidate = createProcessingInstruction( parent, elementName );
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
            candidate = createProcessingInstruction( elementName, data );
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
            candidate = createProcessingInstruction( parent, elementName, data );
            assertNotNull( candidate );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testCreateProcessingInstructionWithInvalidArgument()

    /**
     *  Tests for
     *  {@link XMLBuilderUtils#createProcessingInstruction(String)},
     *  {@link XMLBuilderUtils#createProcessingInstruction(String, CharSequence)},
     *  {@link XMLBuilderUtils#createProcessingInstruction(XMLDocument, String)},
     *  and
     *  {@link XMLBuilderUtils#createProcessingInstruction(XMLDocument, String, CharSequence)}.
     */
    @SuppressWarnings( "OverlyComplexMethod" )
    @Test
    final void testCreateProcessingInstructionWithNullArgument()
    {
        skipThreadTest();

        ProcessingInstruction candidate;
        final var parent = createXMLDocument();
        final var elementName = "element";
        final var data = "data";

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate = createProcessingInstruction( null );
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
            candidate = createProcessingInstruction( (XMLDocument) null, elementName );
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
            candidate = createProcessingInstruction( parent, null );
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
            candidate = createProcessingInstruction( (String) null, data );
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
            candidate = createProcessingInstruction( null, elementName, data );
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
            candidate = createProcessingInstruction( parent, null, data );
            assertNotNull( candidate );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testCreateProcessingInstructionWithNullArgument()
}
//  class TestProcessingInstruction

/*
 *  End of File
 */