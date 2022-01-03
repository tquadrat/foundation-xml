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

import static org.easymock.EasyMock.expect;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.escapeXML;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
 *  @version $Id: TestAddText.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestAddText.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.spi.childsupport.TestAddText" )
public class TestAddText extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link ChildSupport#addText(CharSequence)}.
     *
     *  @param  value   The text to add.
     */
    @ParameterizedTest
    @ValueSource( strings =
    {
        "", "text", "[[text]]", "[[<<text>>]]", "<!-- Comment -->"
    })
    final void testAddText( final String value )
    {
        skipThreadTest();

        final var element = mockElement( "element" );
        expect( element.getParent() )
            .andReturn( Optional.empty() )
            .anyTimes();
        replayAll();

        final var candidate = new ChildSupport( element, XMLBuilderUtils::escapeXML );
        assertTrue( candidate.allowsText() );

        candidate.addText( value );
        final var expected = escapeXML( value );
        final var actual = candidate.toString( 0, false );
        assertEquals( expected, actual );
    }   //  testAddText()

    /**
     *  Tests for
     *  {@link ChildSupport#addText(CharSequence)}.
     */
    @Test
    final void testAddTextNotAllowed()
    {
        skipThreadTest();

        final var element = mockElement( "element" );
        replayAll();

        final var candidate = new ChildSupport( element );
        assertFalse( candidate.allowsText() );

        final Class<? extends Throwable> expectedException = IllegalOperationException.class;
        try
        {
            candidate.addText( "text" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testAddTextNotAllowed()

    /**
     *  Tests for
     *  {@link ChildSupport#addText(CharSequence)}.
     */
    @Test
    final void testAddTextWithNullArgument()
    {
        skipThreadTest();

        final var element = mockElement( "element" );
        replayAll();

        final var candidate = new ChildSupport( element, XMLBuilderUtils::escapeXML );
        assertTrue( candidate.allowsText() );

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate.addText( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testAddTextWithNullArgument()
}
//  class TestAddText

/*
 *  End of File
 */