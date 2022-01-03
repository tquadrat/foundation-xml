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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.CommonConstants.UTF8;
import static org.tquadrat.foundation.lang.CommonConstants.XMLATTRIBUTE_Id;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.builder.Namespace;
import org.tquadrat.foundation.xml.builder.XMLElement;
import org.tquadrat.foundation.xml.builder.spi.InvalidXMLNameException;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Tests for
 *  {@link XMLDocumentImpl}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestXMLDocumentImpl.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestXMLDocumentImpl.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.internal.TestXMLDocumentImpl" )
public class TestXMLDocumentImpl extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link XMLDocumentImpl#XMLDocumentImpl()}.
     *
     *  @throws URISyntaxException   The URI is invalid.
     */
    @Test
    final void testConstructor() throws URISyntaxException
    {
        skipThreadTest();

        XMLDocumentImpl candidate;

        final var prefix = "prefix";
        final var identifier = "tquadrat.org/foundation";
        final var uri = URI.create( identifier );

        candidate = new XMLDocumentImpl();
        assertNotNull( candidate );
        candidate.setNamespace( identifier );

        candidate = new XMLDocumentImpl();
        assertNotNull( candidate );
        candidate.setNamespace( uri );

        candidate = new XMLDocumentImpl();
        assertNotNull( candidate );
        candidate.setNamespace( prefix, identifier );

        candidate = new XMLDocumentImpl();
        assertNotNull( candidate );
        candidate.setNamespace( prefix, uri );

        var namespace = new Namespace( identifier );
        candidate = new XMLDocumentImpl();
        assertNotNull( candidate );
        candidate.setNamespace( namespace );

        namespace = new Namespace( prefix, identifier );
        candidate = new XMLDocumentImpl();
        assertNotNull( candidate );
        candidate.setNamespace( namespace );

        candidate = new XMLDocumentImpl();
        assertNotNull( candidate );

        candidate = new XMLDocumentImpl( "root", UTF8, "dtd", uri );
        assertNotNull( candidate );

        candidate = new XMLDocumentImpl( "root", UTF8, uri );
        assertNotNull( candidate );

        candidate = new XMLDocumentImpl( "root" );
        assertNotNull( candidate );

        candidate.registerAttributeSequence( XMLATTRIBUTE_Id, "attribute1",
            "attribute2", "attribute3", "attribute4", "attribute5",
            "attribute6", "attribute7", "attribute8", "attribute9",
            "attribute10", "attribute11", "attribute12", "attribute13",
            "attribute14", "empty", "notEmpty1", "notEmpty2" );

        candidate.setId( "id" );
        candidate.setAttribute( "attribute1", false );
        candidate.setAttribute( "attribute1", true );
        candidate.setAttribute( "attribute2", TRUE );
        candidate.setAttribute( "attribute2", FALSE );
        candidate.setAttribute( "attribute3", '+' );
        candidate.setAttribute( "attribute3", '&' );
        candidate.setAttribute( "attribute4", Character.valueOf( '>' ) );
        candidate.setAttribute( "attribute4", Character.valueOf( '<' ) );
        candidate.setAttribute( "attribute5", "Dies ist ein Text[[mit zwei öffnenden eckigen Klammern" );
        candidate.setAttribute( "attribute5", "Dies ist ein Text]]mit zwei schließenden eckigen Klammern" );
        candidate.setAttribute( "attribute6", 3.15D );
        candidate.setAttribute( "attribute6", 3.14D );
        candidate.setAttribute( "attribute7", Thread.State.RUNNABLE );
        candidate.setAttribute( "attribute7", Thread.State.TERMINATED );
        candidate.setAttribute( "attribute8", Instant.ofEpochMilli( Long.MIN_VALUE ) );
        candidate.setAttribute( "attribute8", Instant.ofEpochMilli( 0L ) );
        candidate.setAttribute( "attribute9", 24 );
        candidate.setAttribute( "attribute9", 42 );
        candidate.setAttribute( "attribute10", LocalDate.of( 1900, 1, 1 ) );
        candidate.setAttribute( "attribute10", LocalDate.of( 1970, 1, 1 ) );
        candidate.setAttribute( "attribute11", LocalDateTime.of( 1900, 1, 1, 0, 0, 0 ) );
        candidate.setAttribute( "attribute11", LocalDateTime.of( 1970, 1, 1, 0, 0, 0 ) );
        candidate.setAttribute( "attribute12", 0L );
        candidate.setAttribute( "attribute12", Long.MAX_VALUE );
        candidate.setAttribute( "attribute13", Double.valueOf( 2.0 ) );
        candidate.setAttribute( "attribute13", Double.valueOf( 1.4142 ) );
        candidate.setAttribute( "attribute14", ZonedDateTime.of( 1970, 12, 1, 0, 0, 0, 0, ZoneId.of( "Europe/Berlin" ) ) );
        candidate.setAttribute( "attribute14", ZonedDateTime.of( 1970, 12, 1, 0, 0, 0, 0, ZoneId.of( "UTC" ) ) );
        candidate.setAttributeIfNotEmpty( "empty", EMPTY_STRING );
        assertFalse( candidate.getAttribute( "empty" ).isPresent() );
        candidate.setAttributeIfNotEmpty( "notEmpty1", "value" );
        assertTrue( candidate.getAttribute( "notEmpty1" ).isPresent() );
        candidate.setAttributeIfNotEmpty( "empty", Optional.empty() );
        candidate.setAttributeIfNotEmpty( "notEmpty2", Optional.of( "value" ) );

        assertEquals( """
                      <?xml version='1.0'
                            encoding='UTF-8'
                            standalone='yes'?>
                      <root xml:id='id'
                            attribute1='true'
                            attribute2='false'
                            attribute3='38'
                            attribute4='60'
                            attribute5='Dies ist ein Text]]mit zwei schließenden eckigen Klammern'
                            attribute6='3.14'
                            attribute7='TERMINATED'
                            attribute8='1970-01-01T00:00:00Z'
                            attribute9='42'
                            attribute10='1970-01-01'
                            attribute11='1970-01-01T00:00'
                            attribute12='9223372036854775807'
                            attribute13='1.4142'
                            attribute14='1970-12-01T00:00Z[UTC]'
                            notEmpty1='value'
                            notEmpty2='value'/>""", candidate.toString() );

        candidate = new XMLDocumentImpl();
        assertNotNull( candidate );

        candidate.addDocumentComment( "documentComment" );
        candidate.addComment( "comment" );
        candidate.addPredefinedMarkup( "<child id='id' />" );

        assertEquals( """
                      <?xml version='1.0'
                            encoding='UTF-8'
                            standalone='yes'?>
                      <!--
                      documentComment
                      -->
                      <root>
                          <!--
                          comment
                          -->
                          <child id='id' />
                      </root>""", candidate.toString() );
    }   //  testConstructor()

    /**
     *  Tests for
     *  {@link XMLDocumentImpl#XMLDocumentImpl(String)},
     *  {@link XMLDocumentImpl#XMLDocumentImpl(String, java.nio.charset.Charset, URI)},
     *  {@link XMLDocumentImpl#XMLDocumentImpl(String, java.nio.charset.Charset, String, URI)},
     *  and
     *  {@link XMLDocumentImpl#XMLDocumentImpl(org.tquadrat.foundation.xml.builder.XMLElement, java.nio.charset.Charset, String, URI)}.
     */
    @Test
    final void testConstructorWithEmptyArgument()
    {
        skipThreadTest();

        final var elementName = "root";
        final var element = new XMLElementImpl( elementName );
        final var dtdName = "dtd";
        final var uri = URI.create( "tquadrat.org/foundation" );
        XMLDocumentImpl candidate;

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            candidate = new XMLDocumentImpl( EMPTY_STRING );
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
            candidate = new XMLDocumentImpl( EMPTY_STRING, UTF8, uri );
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
            candidate = new XMLDocumentImpl( EMPTY_STRING, UTF8, dtdName, uri );
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
            candidate = new XMLDocumentImpl( elementName, UTF8, EMPTY_STRING, uri );
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
            candidate = new XMLDocumentImpl( element, UTF8, EMPTY_STRING, uri );
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
     *  Tests for
     *  {@link XMLDocumentImpl#XMLDocumentImpl(String)},
     *  {@link XMLDocumentImpl#XMLDocumentImpl(String, java.nio.charset.Charset, URI)},
     *  and
     *  {@link XMLDocumentImpl#XMLDocumentImpl(String, java.nio.charset.Charset, String, URI)}.
     *
     *  @param  isValid {@code true} if the given element name is valid,
     *      {@code false} otherwise.
     *  @param  elementName The name of the root element.
     */
    @ParameterizedTest
    @CsvFileSource( resources = "/org/tquadrat/foundation/xml/builder/xmlbuilderutils/DefaultElementNameValidator.csv", delimiter = ';', numLinesToSkip = 1)
    final void testConstructorWithInvalidArgument( final boolean isValid, final String elementName )
    {
        skipThreadTest();

        assumeFalse( isValid ); // We test for failure

        final var dtdName = "dtd";
        final var uri = URI.create( "tquadrat.org/foundation" );
        XMLDocumentImpl candidate;

        final Class<? extends Throwable> expectedException = InvalidXMLNameException.class;
        try
        {
            candidate = new XMLDocumentImpl( elementName );
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
            candidate = new XMLDocumentImpl( elementName, UTF8, uri );
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
            candidate = new XMLDocumentImpl( elementName, UTF8, dtdName, uri );
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
     *  Tests for
     *  {@link XMLDocumentImpl#XMLDocumentImpl(String)},
     *  {@link XMLDocumentImpl#XMLDocumentImpl(XMLElement, boolean)},
     *  {@link XMLDocumentImpl#XMLDocumentImpl(String, java.nio.charset.Charset, URI)},
     *  {@link XMLDocumentImpl#XMLDocumentImpl(org.tquadrat.foundation.xml.builder.XMLElement, java.nio.charset.Charset, URI)},
     *  {@link XMLDocumentImpl#XMLDocumentImpl(String, java.nio.charset.Charset, String, URI)},
     *  and
     *  {@link XMLDocumentImpl#XMLDocumentImpl(org.tquadrat.foundation.xml.builder.XMLElement, java.nio.charset.Charset, String, URI)}.
     */
    @Test
    final void testConstructorWithNullArgument()
    {
        skipThreadTest();

        final var elementName = "root";
        final var element = new XMLElementImpl( elementName );
        final var dtdName = "dtd";
        final var uri = URI.create( "tquadrat.org/foundation" );
        @SuppressWarnings( "unused" )
        XMLDocumentImpl candidate;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate = new XMLDocumentImpl( null );
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
            candidate = new XMLDocumentImpl( null, false );
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
            candidate = new XMLDocumentImpl( (String) null, UTF8, uri );
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
            candidate = new XMLDocumentImpl( elementName, null, uri );
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
            candidate = new XMLDocumentImpl( elementName, UTF8, null );
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
            candidate = new XMLDocumentImpl( (XMLElement) null, UTF8, uri );
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
            candidate = new XMLDocumentImpl( element, null, uri );
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
            candidate = new XMLDocumentImpl( element, UTF8, null );
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
            candidate = new XMLDocumentImpl( (String) null, UTF8, dtdName, uri );
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
            candidate = new XMLDocumentImpl( elementName, null, dtdName, uri );
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
            candidate = new XMLDocumentImpl( elementName, UTF8, null, uri );
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
            candidate = new XMLDocumentImpl( elementName, UTF8, dtdName, null );
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
            candidate = new XMLDocumentImpl( (XMLElement) null, UTF8, dtdName, uri );
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
            candidate = new XMLDocumentImpl( element, null, dtdName, uri );
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
            candidate = new XMLDocumentImpl( element, UTF8, null, uri );
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
            candidate = new XMLDocumentImpl( element, UTF8, dtdName, null );
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
}
//  class TestXMLDocumentImpl

/*
 *  End of File
 */