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

package org.tquadrat.foundation.xml.parse;

import static java.lang.System.out;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.io.IOException;
import java.io.StringReader;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.helper.PropertiesHandler;
import org.tquadrat.foundation.xml.helper.PropertiesReader;
import org.tquadrat.foundation.xml.helper.XMLTestBase;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *  Test class for
 *  {@link AdvancedContentHandler}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestAdvancedContentHandler.java 1030 2022-04-06 13:42:02Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestAdvancedContentHandler.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.parse.TestAdvancedContentHandler" )
public class TestAdvancedContentHandler extends XMLTestBase
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The number of properties: {@value}.
     */
    public static final int NUMBER_OF_PROPERTIES = 15;

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The properties "Stream".
     */
    @SuppressWarnings( "StaticVariableMayNotBeInitialized" )
    private static String m_Properties;

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Performs the setup for all tests in the class.
     *
     *  @throws Exception   Something has gone wrong during the setup.
     */
    @BeforeAll
    static final void setUpAll() throws Exception
    {
        //---* Setup the test data *-------------------------------------------
        m_Properties = IntStream.range( 0, NUMBER_OF_PROPERTIES )
            .mapToObj( i -> format( "Key%1$d=Value%1$d\n", i + 1 ) )
            .collect( joining() );
    }   //  setUpAll()

    /**
     *  This method mainly performs the test for the class
     *  {@link AdvancedContentHandler}
     *  as far as this is possible for an abstract class.
     *
     *  @throws SAXException    Something went wrong unexpectedly.
     */
    @Test
    final void testParse() throws SAXException
    {
        skipThreadTest();

        //---* Create the content handler *------------------------------------
        final var contentHandler = new PropertiesHandler();

        //---* Create the input source *---------------------------------------
        final var stringReader = new StringReader( m_Properties );
        final var inputSource = new InputSource( stringReader );

        //---* Create the PropertiesReader object *----------------------------
        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        PropertiesReader xmlReader;
        try
        {
            xmlReader = new PropertiesReader( null );
            assertNotNull( xmlReader );
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

        xmlReader = new PropertiesReader( contentHandler );
        assertNotNull( xmlReader );

        //---* Parse *---------------------------------------------------------
        try
        {
            xmlReader.parse( inputSource );
        }
        catch( final IOException e )
        {
            fail( "IOException was thrown: " + e.getLocalizedMessage() );
        }
        catch( final SAXException e )
        {
            fail( "SAXException was thrown: " + e.getLocalizedMessage() );
        }
    }   //  testParse()
}
//  class TestAdvancedContentHandler

/*
 *  End of File
 */