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

package org.tquadrat.foundation.xml.builder.spi.childsupport;

import static java.lang.String.format;
import static org.easymock.EasyMock.expect;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.IllegalOperationException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.builder.XMLBuilderUtils;
import org.tquadrat.foundation.xml.builder.spi.ChildSupport;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Some tests for the class
 *  {@link ChildSupport}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestAddCDATA.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestAddCDATA.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.spi.childsupport.TestAddCDATA" )
public class TestAddCDATA extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link ChildSupport#addCDATA(CharSequence)}.
     */
    @Test
    final void testAddCDATA()
    {
        skipThreadTest();

        final var element = mockElement( "element" );
        expect( element.getParent() )
            .andReturn( Optional.empty() )
            .anyTimes();
        replayAll();

        ChildSupport candidate;
        String actual, expected, value;

        candidate = new ChildSupport( element, XMLBuilderUtils::escapeXML );
        assertTrue( candidate.allowsText() );
        value = EMPTY_STRING;
        candidate.addCDATA( value );
        expected = "<![CDATA[]]>";
        actual = candidate.toString( 0, false );
        assertEquals( expected, actual );

        candidate = new ChildSupport( element, XMLBuilderUtils::escapeXML );
        assertTrue( candidate.allowsText() );
        value = "text";
        candidate.addCDATA( value );
        expected = "<![CDATA[text]]>";
        actual = candidate.toString( 0, false );
        assertEquals( expected, actual );

        candidate = new ChildSupport( element, XMLBuilderUtils::escapeXML );
        assertTrue( candidate.allowsText() );
        value = "[[text]]";
        candidate.addCDATA( value );
        expected = "<![CDATA[[[text]]>]]";
        actual = candidate.toString( 0, false );
        assertEquals( expected, actual );

        candidate = new ChildSupport( element, XMLBuilderUtils::escapeXML );
        assertTrue( candidate.allowsText() );
        value = "[[text]]text";
        candidate.addCDATA( value );
        expected = "<![CDATA[[[text]]>]]<![CDATA[text]]>";
        actual = candidate.toString( 0, false );
        assertEquals( expected, actual );
    }   //  testAddCDATA()

    /**
     *  Tests for
     *  {@link ChildSupport#addCDATA(CharSequence)}.
     */
    @Test
    final void testAddCDATANotAllowed()
    {
        skipThreadTest();

        final var element = mockElement( "element" );
        replayAll();

        final var candidate = new ChildSupport( element );
        assertFalse( candidate.allowsText() );

        final Class<? extends Throwable> expectedException = IllegalOperationException.class;
        try
        {
            candidate.addCDATA( "text" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testAddCDATANotAllowed()

    /**
     *  Tests for
     *  {@link ChildSupport#addCDATA(CharSequence)}.
     */
    @Test
    final void testAddCDATAWithNullArgument()
    {
        skipThreadTest();

        final var element = mockElement( "element" );
        replayAll();

        final var candidate = new ChildSupport( element, XMLBuilderUtils::escapeXML );
        assertTrue( candidate.allowsText() );

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate.addCDATA( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testAddCDATAWithNullArgument()
}
//  class TestAddCDATA

/*
 *  End of File
 */