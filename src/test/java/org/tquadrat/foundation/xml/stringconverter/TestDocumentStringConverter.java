/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
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

package org.tquadrat.foundation.xml.stringconverter;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.util.StringUtils.format;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.lang.StringConverter;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 *  Tests for the class
 *  {@link org.tquadrat.foundation.util.stringconverter.DocumentStringConverter}.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestDocumentStringConverter.java 895 2021-04-05 12:40:34Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestDocumentStringConverter.java 895 2021-04-05 12:40:34Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.stringconverter.TestDocumentStringConverter" )
public class TestDocumentStringConverter extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  The tests for
     *  {@link DocumentStringConverter}.
     */
    @Test
    final void testConversion()
    {
        skipThreadTest();

        final var candidate = DocumentStringConverter.INSTANCE;
        assertNotNull( candidate );

        assertNull( candidate.fromString( null ) );
        assertNull( candidate.toString( null ) );

        final Class<? extends Throwable> expectedException = IllegalArgumentException.class;
        try
        {
            candidate.fromString( EMPTY_STRING );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException )
            {
                t.printStackTrace( out );
            }
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
        try
        {
            candidate.fromString( " " );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException )
            {
                t.printStackTrace( out );
            }
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
        try
        {
            candidate.fromString( "Fußpilz" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException )
            {
                t.printStackTrace( out );
            }
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testConversion()

    /**
     *  The tests for
     *  {@link DocumentStringConverter}.
     *
     *  @param  value   The value for the tests.
     */
    @ParameterizedTest
    @MethodSource( "valueProvider" )
    final void testValueConversion( final Document value )
    {
        skipThreadTest();

        final var container = StringConverter.forClass( Document.class );
        assertNotNull( container );
        assertTrue( container.isPresent() );
        final var candidate = container.get();
        assertNotNull( candidate );

        out.println( candidate.toString( value ) );
        out.println( "-".repeat( 20 ) );

        if( isNull( value ) )
        {
            assertEquals( value, candidate.fromString( candidate.toString( value ) ) );
        }
        else
        {
            assertTrue( value.isEqualNode( candidate.fromString( candidate.toString( value ) ) ) );
        }
    }   //  testValueConversion()

    /**
     *  Provides test values for
     *  {@link #testValueConversion(Document)}.
     *
     *  @return The test values.
     *  @throws Exception   Something unexpected went wrong.
     */
    static final Stream<Document> valueProvider() throws Exception
    {
        final var documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware( true );
        documentBuilderFactory.setValidating( false );
        documentBuilderFactory.setIgnoringComments( false );
        documentBuilderFactory.setIgnoringElementContentWhitespace( true );
        documentBuilderFactory.setCoalescing( false );
        documentBuilderFactory.setExpandEntityReferences( false );
        final var documentBuilder = documentBuilderFactory.newDocumentBuilder();

        final var xml = List.of(
            """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <document name="Test">
              <title>Test Document</title>
              <chapter id="chapter_1">
                <title>Title Chapter 1</title>
                  <paragraph>Text</paragraph>
                  <paragraph><link url="#chapter_2">Chapter 2</link></paragraph>
              </chapter>
              <chapter id="chapter_2">
                <title>Title Chapter 2</title>
                  <paragraph>Text</paragraph>
              </chapter>
            </document>""",

            """
            <?xml version="1.0" encoding="UTF-8"?>
            <document name="Test">
              <title>Test Document</title>
              <chapter id="chapter_1">
                <title>Title Chapter 1</title>
                  <paragraph>Text</paragraph>
                  <paragraph><link url="#chapter_2">Chapter 2</link></paragraph>
              </chapter>
              <chapter id="chapter_2">
                <title>Title Chapter 2</title>
                  <paragraph>Text</paragraph>
              </chapter>
            </document>""",

            """
            <?xml version="1.0" encoding="UTF-8" standalone="no"?>
            <document name="Test">
              <title>Test Document</title>
              <chapter id="chapter_1">
                <title>Title Chapter 1</title>
                  <paragraph>Text</paragraph>
                  <paragraph><link url="#chapter_2">Chapter 2</link></paragraph>
              </chapter>
              <chapter id="chapter_2">
                <title>Title Chapter 2</title>
                  <paragraph>Text</paragraph>
              </chapter>
            </document>""",

            """
            <?xml version="1.0" ?>
            <document />
            """
        );

        final Stream.Builder<Document> builder = Stream.builder();
        builder.add( null );
        for( final var s : xml )
        {
            builder.add( documentBuilder.parse( new InputSource( new StringReader( s ) ) ) );
        }

        final var retValue = builder.build();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  valueProvider()
}
//  class TestStringConverter

/*
 *  End of File
 */