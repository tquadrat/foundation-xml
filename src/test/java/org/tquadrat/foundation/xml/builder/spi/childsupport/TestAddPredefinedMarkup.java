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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.StringUtils.format;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.IllegalOperationException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.builder.spi.ChildSupport;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Some tests for the class
 *  {@link ChildSupport}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestAddPredefinedMarkup.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestAddPredefinedMarkup.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.spi.childsupport.TestAddPredefinedMarkup" )
public class TestAddPredefinedMarkup extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link ChildSupport#addPredefinedMarkup(CharSequence)}
     */
    @Test
    final void testAddPredefinedMarkup()
    {
        skipThreadTest();
    }   //  testAddPredefinedMarkup()

    /**
     *  Tests for
     *  {@link ChildSupport#addPredefinedMarkup(CharSequence)}
     */
    @Test
    final void testAddPredefinedMarkupNotAllowed()
    {
        skipThreadTest();

        final var parent = mockElement( "parent" );
        replayAll();

        final var candidate = new ChildSupport( parent );
        assertFalse( candidate.allowsChildren() );

        final Class<? extends Throwable> expectedException = IllegalOperationException.class;
        try
        {
            candidate.addPredefinedMarkup( "<element />" );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testAddPredefinedMarkupNotAllowed()

    /**
     *  Tests for
     *  {@link ChildSupport#addPredefinedMarkup(CharSequence)}
     */
    @Test
    final void testAddPredefinedMarkupWithNullArgument()
    {
        skipThreadTest();

        final var parent = mockElement( "parent" );
        replayAll();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;

        ChildSupport candidate;

        candidate = new ChildSupport( parent, false );
        assertTrue( candidate.allowsChildren() );
        assertFalse( candidate.checksIfValid() );

        try
        {
            candidate.addPredefinedMarkup( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        candidate = new ChildSupport( parent, true );
        assertTrue( candidate.allowsChildren() );
        assertTrue( candidate.checksIfValid() );

        try
        {
            candidate.addPredefinedMarkup( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testAddPredefinedMarkupWithNullArgument()
}
//  class TestAddPredefinedMarkup

/*
 *  End of File
 */