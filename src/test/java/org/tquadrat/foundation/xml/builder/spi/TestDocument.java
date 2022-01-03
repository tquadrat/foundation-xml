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

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static org.easymock.EasyMock.expect;
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
 *  {@link Document}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestDocument.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestDocument.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.spi.TestDocument" )
public class TestDocument extends XMLTestBase
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  An implementation of
     *  {@link Document}.
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: TestDocument.java 820 2020-12-29 20:34:22Z tquadrat $
     */
    @ClassVersion( sourceVersion = "$Id: TestDocument.java 820 2020-12-29 20:34:22Z tquadrat $" )
    private final class CandidateDocument implements Document<Element>
    {
            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code CandidateDocument} instance.
         */
        @SuppressWarnings( "synthetic-access" )
        public CandidateDocument()
        {
            m_RootElement = mockElement( "rootElement" );
            expect( m_RootElement.getAttribute( "attribute" ) )
                .andReturn( Optional.empty() )
                .anyTimes();
            expect( m_RootElement.getAttribute( EMPTY_STRING ) )
                .andThrow( new EmptyArgumentException() )
                .anyTimes();
            expect( m_RootElement.getAttribute( null ) )
                .andThrow( new NullArgumentException() )
                .anyTimes();
            expect( m_RootElement.getAttributes() )
                .andReturn( emptyMap() )
                .anyTimes();
            expect( m_RootElement.getNamespaces() )
                .andReturn( emptySet() )
                .anyTimes();
            expect( m_RootElement.toString( 0, false ) )
                .andReturn( "<rootElement/>" )
                .anyTimes();
            replayAll();
        }

            /*------------*\
        ====** Attributes **===================================================
            \*------------*/
        /**
         *  The root element.
         */
        private final Element m_RootElement;

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  {@inheritDoc}
         */
        @Override
        public final Element getRootElement() { return m_RootElement; }
    }
    //  class CandidateDocument

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for the default methods in the interface
     *  {@link Document}.
     */
    @Test
    final void testDocument()
    {
        skipThreadTest();

        final var candidate = new CandidateDocument();

        final var attribute = candidate.getAttribute( "attribute" );
        assertNotNull( attribute );
        assertFalse( attribute.isPresent() );

        final var attributes = candidate.getAttributes();
        assertNotNull( attributes );
        assertTrue( attributes.isEmpty() );

        final var children = candidate.getChildren();
        assertNotNull( children );
        assertFalse( children.isEmpty() );

        assertEquals( "rootElement", candidate.getElementName() );

        final var namespaces = candidate.getNamespaces();
        assertNotNull( namespaces );
        assertTrue( namespaces.isEmpty() );

        assertEquals( "<rootElement/>", candidate.toString( false ) );
    }   //  testDocument()

    /**
     *  Tests for the default methods in the interface
     *  {@link Document}.
     */
    @Test
    final void testDocumentWithEmptyArgument()
    {
        skipThreadTest();

        final var candidate = new CandidateDocument();

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
    }   //  testDocumentWithEmptyArgument()

    /**
     *  Tests for the default methods in the interface
     *  {@link Document}.
     */
    @Test
    final void testDocumentWithNullArgument()
    {
        skipThreadTest();

        final var candidate = new CandidateDocument();

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
    }   //  testDocumentWithNullArgument()
}
//  class TestDocument

/*
 *  End of File
 */