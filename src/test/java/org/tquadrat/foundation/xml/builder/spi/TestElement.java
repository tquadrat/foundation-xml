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

package org.tquadrat.foundation.xml.builder.spi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.StringUtils.format;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Some tests for the default methods in the interface
 *  {@link Element}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestElement.java 1030 2022-04-06 13:42:02Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestElement.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.spi.TestElement" )
public class TestElement extends XMLTestBase
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  An implementation of
     *  {@link Element}.
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: TestElement.java 1030 2022-04-06 13:42:02Z tquadrat $
     */
    @ClassVersion( sourceVersion = "$Id: TestElement.java 1030 2022-04-06 13:42:02Z tquadrat $" )
    private static final class CandidateElement implements Element
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code CandidateElement} instance.
         */
        public CandidateElement() { /* Just exists */ }

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  {@inheritDoc}
         */
        @Override
        public final String getElementName() { return "element"; }

        /**
         *  {@inheritDoc}
         */
        @Override
        public final Optional<Element> getParent() { return Optional.empty(); }

        /**
         *  {@inheritDoc}
         */
        @Override
        public final <E extends Element> void setParent( final E parent ) { /* Does nothing */ }
    }
    //  class CandidateElement

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for the default methods in the interface
     *  {@link Element}.
     */
    @Test
    final void testElement()
    {
        skipThreadTest();

        final var candidate = new CandidateElement();

        final var attribute = candidate.getAttribute( "attribute" );
        assertNotNull( attribute );
        assertFalse( attribute.isPresent() );

        final var attributes = candidate.getAttributes();
        assertNotNull( attributes );
        assertTrue( attributes.isEmpty() );

        final var children = candidate.getChildren();
        assertNotNull( children );
        assertTrue( children.isEmpty() );

        final var namespaces = candidate.getNamespaces();
        assertNotNull( namespaces );
        assertTrue( namespaces.isEmpty() );

        assertFalse( candidate.hasChildren() );

        assertEquals( "<element/>", candidate.toString( 0, false ) );
    }   //  testElement()

    /**
     *  Tests for the default methods in the interface
     *  {@link Element}.
     */
    @Test
    final void testElementWithEmptyArgument()
    {
        skipThreadTest();

        final var candidate = new CandidateElement();

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            candidate.getAttribute( EMPTY_STRING );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testElementWithEmptyArgument()

    /**
     *  Tests for the default methods in the interface
     *  {@link Element}.
     */
    @Test
    final void testElementWithNullArgument()
    {
        skipThreadTest();

        final var candidate = new CandidateElement();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate.getAttribute( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testElementWithNullArgument()
}
//  class TestElement

/*
 *  End of File
 */