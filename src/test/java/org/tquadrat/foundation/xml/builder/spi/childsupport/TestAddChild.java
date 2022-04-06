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
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.IllegalOperationException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.builder.spi.ChildSupport;
import org.tquadrat.foundation.xml.builder.spi.Element;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Some tests for the class
 *  {@link ChildSupport}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestAddChild.java 1030 2022-04-06 13:42:02Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestAddChild.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.spi.childsupport.TestAddChild" )
public class TestAddChild extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link ChildSupport#addChild(Element)}
     */
    @SuppressWarnings( "JUnitTestMethodWithNoAssertions" )
    @Test
    final void testAddChild()
    {
        skipThreadTest();
    }   //  testAddChild()

    /**
     *  Tests for
     *  {@link ChildSupport#addChild(Element)}
     */
    @Test
    final void testAddChildHasParent()
    {
        skipThreadTest();

        final var parent = mockElement( "parent" );
        final var otherParent = mockElement( "otherParent" );
        final var child = mockElement( "child1" );
        expect( child.getParent() )
            .andReturn( Optional.of( otherParent ) )
            .anyTimes();
        replayAll();

        final var candidate = new ChildSupport( parent, false );
        assertTrue( candidate.allowsChildren() );
        assertFalse( candidate.checksIfValid() );

        final Class<? extends Throwable> expectedException = IllegalStateException.class;
        try
        {
            candidate.addChild( child );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testAddChildHasParent()

    /**
     *  Tests for
     *  {@link ChildSupport#addChild(Element)}
     */
    @Test
    final void testAddChildNotAllowed()
    {
        skipThreadTest();

        final var parent = mockElement( "parent" );
        final var child = mockElement( "child" );
        replayAll();

        final var candidate = new ChildSupport( parent );
        assertFalse( candidate.allowsChildren() );

        final Class<? extends Throwable> expectedException = IllegalOperationException.class;
        try
        {
            candidate.addChild( child );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testAddChildNotAllowed()

    /**
     *  Tests for
     *  {@link ChildSupport#addChild(Element)}
     */
    @Test
    final void testAddChildWithInvalidArgument()
    {
        skipThreadTest();

        final var parent = mockElement( "parent" );
        final var child = mockElement( "child1" );
        expect( child.getParent() )
            .andReturn( Optional.empty() )
            .anyTimes();
        child.setParent( parent );
        expectLastCall().anyTimes();
        final var invalidChild = mockElement( "child2" );
        expect( invalidChild.getParent() )
            .andReturn( Optional.empty() )
            .anyTimes();
        invalidChild.setParent( parent );
        expectLastCall().anyTimes();
        replayAll();

        ChildSupport candidate;

        /*
         *  Usually, a child cannot be assigned to another parent. But as the
         *  mocks do not store the parents, it works for this test.
         */

        //---* Should work *---------------------------------------------------
        candidate = new ChildSupport( parent, false );
        assertTrue( candidate.allowsChildren() );
        assertFalse( candidate.checksIfValid() );

        candidate.addChild( child );
        candidate.addChild( invalidChild );

        //---* Should still work *---------------------------------------------
        candidate = new ChildSupport( parent, false );
        candidate.registerChildren( "an_arbitrary_name" );
        assertTrue( candidate.allowsChildren() );
        assertFalse( candidate.checksIfValid() );

        candidate.addChild( child );
        candidate.addChild( invalidChild );

        candidate = new ChildSupport( parent, true );
        candidate.registerChildren( child.getElementName() );
        assertTrue( candidate.allowsChildren() );
        assertTrue( candidate.checksIfValid() );

        candidate.addChild( child );
        final Class<? extends Throwable> expectedException = IllegalArgumentException.class;
        try
        {
            candidate.addChild( invalidChild );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }

        candidate.registerChildren( invalidChild.getElementName() );
        candidate.addChild( invalidChild );
    }   //  testAddChildWithInvalidArgument()

    /**
     *  Tests for
     *  {@link ChildSupport#addChild(Element)}
     */
    @Test
    final void testAddChildWithNullArgument()
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
            candidate.addChild( null );
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
            candidate.addChild( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testAddChildWithNullArgument()
}
//  class TestAddChild

/*
 *  End of File
 */