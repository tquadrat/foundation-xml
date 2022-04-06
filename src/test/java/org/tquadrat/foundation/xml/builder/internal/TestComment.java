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

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_CHARSEQUENCE;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.createXMLElement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Tests for the class
 *  {@link Comment}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestComment.java 1030 2022-04-06 13:42:02Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestComment.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.internal.TestComment" )
public class TestComment extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for the constructor
     *  {@link Comment#Comment(CharSequence)}.
     */
    @Test
    final void testConstructor()
    {
        skipThreadTest();

        Comment candidate;

        candidate = new Comment( EMPTY_CHARSEQUENCE );
        assertNotNull( candidate );
        assertEquals( "<!-- -->", candidate.toString( 0, false ) );
        assertEquals( "\n<!-- -->", candidate.toString() );

        candidate = new Comment( "one line" );
        assertNotNull( candidate );
        assertEquals( "<!-- one line -->", candidate.toString( 0, false ) );
        assertEquals( "\n<!--\none line\n-->", candidate.toString() );

        candidate = new Comment( "one line\ntwo lines" );
        assertNotNull( candidate );
        assertEquals( "<!-- one line two lines -->", candidate.toString( 0, false ) );
        assertEquals( "\n<!--\none line\ntwo lines\n-->", candidate.toString() );

        candidate = new Comment( "---" );
        assertNotNull( candidate );
        assertEquals( "<!-- &#x2010;&#x2010;&#x2010; -->", candidate.toString( 0, false ) );
        assertEquals( "\n<!--\n&#x2010;&#x2010;&#x2010;\n-->", candidate.toString() );

        assertEquals( "[COMMENT]", candidate.getElementName() );
        var parent = candidate.getParent();
        assertNotNull( parent );
        assertFalse( parent.isPresent() );
        candidate.setParent( createXMLElement( "element" ) );
        parent = candidate.getParent();
        assertNotNull( parent );
        assertTrue( parent.isPresent() );
        assertTrue( candidate.isBlock() );
    }   //  testConstructor()

    /**
     *  Tests for the constructor
     *  {@link Comment#Comment(CharSequence)}.
     */
    @Test
    final void testConstructorWithNullArgument()
    {
        skipThreadTest();

        @SuppressWarnings( "unused" )
        Comment candidate = null;

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate = new Comment( null );
            assertNotNull( candidate );
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
    }   //  testConstructorWithNullArgument()
}
//  class TestComment

/*
 *  End of File
 */