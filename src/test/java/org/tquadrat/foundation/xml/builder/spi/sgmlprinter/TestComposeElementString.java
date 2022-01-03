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

package org.tquadrat.foundation.xml.builder.spi.sgmlprinter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.createXMLElement;
import static org.tquadrat.foundation.xml.builder.spi.SGMLPrinter.composeElementString;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.builder.XMLElement;
import org.tquadrat.foundation.xml.builder.spi.Element;
import org.tquadrat.foundation.xml.builder.spi.SGMLPrinter;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Tests for the new version of
 *  {@link SGMLPrinter#composeElementString(int, boolean, Element, boolean)}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestComposeElementString.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestComposeElementString.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.spi.sgmlprinter.TestComposeElementString" )
public class TestComposeElementString extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for the method
     *  {@link SGMLPrinter#composeElementString(int, boolean, Element, boolean)}.
     */
    @SuppressWarnings( "MisorderedAssertEqualsArguments" )
    @DisplayName( "SGMLPrinter.composeElementString()" )
    @Test
    final void testComposeElementString()
    {
        skipThreadTest();

        XMLElement element;
        final XMLElement child;
        String actual, expected;

        element = createXMLElement( "element" );
        expected = "<element/>";
        actual = composeElementString( 0, false, element, true );
        assertEquals( expected, actual );
        expected = "<element></element>";
        actual = composeElementString( 0, false, element, false );
        assertEquals( expected, actual );

        expected = "\n<element/>";
        actual = composeElementString( 0, true, element, true );
        assertEquals( expected, actual );
        expected = "\n<element></element>";
        actual = composeElementString( 0, true, element, false );
        assertEquals( expected, actual );

        element.setId( "id1" );
        expected = "<element xml:id='id1'/>";
        actual = composeElementString( 0, false, element, true );
        assertEquals( expected, actual );
        expected = "<element xml:id='id1'></element>";
        actual = composeElementString( 0, false, element, false );
        assertEquals( expected, actual );

        element.setId( "id1" );
        expected = "\n<element xml:id='id1'/>";
        actual = composeElementString( 0, true, element, true );
        assertEquals( expected, actual );
        expected = "\n<element xml:id='id1'></element>";
        actual = composeElementString( 0, true, element, false );
        assertEquals( expected, actual );

        element = createXMLElement( "element" );
        child = createXMLElement( "child", element );
        expected = "<element><child/></element>";
        actual = composeElementString( 0, false, element, true );
        assertEquals( expected, actual );
        actual = composeElementString( 0, false, element, false );
        assertEquals( expected, actual );

        expected = "\n<element>\n    <child/>\n</element>";
        actual = composeElementString( 0, true, element, true );
        assertEquals( expected, actual );
        actual = composeElementString( 0, true, element, false );
        assertEquals( expected, actual );

        element.setId( "id1" );
        child.setId( "id2" );
        expected = "<element xml:id='id1'><child xml:id='id2'/></element>";
        actual = composeElementString( 0, false, element, true );
        assertEquals( expected, actual );
        actual = composeElementString( 0, false, element, false );
        assertEquals( expected, actual );

        expected = "\n<element xml:id='id1'>\n    <child xml:id='id2'/>\n</element>";
        actual = composeElementString( 0, true, element, true );
        assertEquals( expected, actual );
        actual = composeElementString( 0, true, element, false );
        assertEquals( expected, actual );
    }   //  testComposeElementString()

    /**
     *  Tests for the method
     *  {@link SGMLPrinter#composeElementString(int, boolean, Element, boolean)}.
     */
    @DisplayName( "SGMLPrinter.composeElementString() with null argument" )
    @Test
    final void testComposeChildrenStringNull()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            composeElementString( 0, true, null, true );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testComposeElementStringNull()
}
//  class TestComposeElementString

/*
 *  End of File
 */