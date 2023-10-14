/*
 * ============================================================================
 *  Copyright © 2002-2021 by Thomas Thrien.
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

package org.tquadrat.foundation.xml.builder.processinginstruction;

import static java.lang.String.format;
import static java.lang.System.out;
import static org.apiguardian.api.API.Status.STABLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;

import org.apiguardian.api.API;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.EmptyArgumentException;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.builder.ProcessingInstruction;
import org.tquadrat.foundation.xml.builder.internal.ProcessingInstructionImpl;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Test for the method
 *  {@link org.tquadrat.foundation.xml.builder.ProcessingInstruction#addData(CharSequence)}.
 *  from class
 *  {@link org.tquadrat.foundation.xml.builder.ProcessingInstruction}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestAddData.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestAddData.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
@DisplayName( "org.tquadrat.foundation.xml.builder.processinginstruction.TestAddData" )
public class TestAddData extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link org.tquadrat.foundation.xml.builder.ProcessingInstruction#addData(CharSequence)}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testAddData() throws Exception
    {
        skipThreadTest();

        ProcessingInstruction candidate;
        String actual, expected;
        final var elementName = "processingInstruction";

        candidate = new ProcessingInstructionImpl( elementName );
        assertNotNull( candidate );

        expected =
            """
            <?processingInstruction?>""";
        actual = candidate.toString();
        assertEquals( expected, actual );

        candidate.addData( "data1" );
        expected =
            """
            <?processingInstruction data1?>""";
        actual = candidate.toString();
        assertEquals( expected, actual );

        candidate.addData( "data2" );
        expected =
            """
            <?processingInstruction data1
                                    data2?>""";
        actual = candidate.toString();
        assertEquals( expected, actual );

        candidate.setAttribute( "attribute1", "value1" );
        candidate.setAttribute( "attribute2", "value2" );
        expected =
            """
            <?processingInstruction data1
                                    data2
                                    attribute1='value1'
                                    attribute2='value2'?>""";
        actual = candidate.toString();
        assertEquals( expected, actual );

        candidate = new ProcessingInstructionImpl( elementName );
        candidate.setAttribute( "attribute1", "value1" );
        expected =
            """
            <?processingInstruction attribute1='value1'?>""";
        actual = candidate.toString();
        assertEquals( expected, actual );

        candidate.setAttribute( "attribute2", "value2" );
        expected =
            """
            <?processingInstruction attribute1='value1'
                                    attribute2='value2'?>""";
        actual = candidate.toString();
        assertEquals( expected, actual );

        candidate.addData( "data1" );
        candidate.addData( "data2" );
        expected =
            """
            <?processingInstruction data1
                                    data2
                                    attribute1='value1'
                                    attribute2='value2'?>""";
        actual = candidate.toString();
        assertEquals( expected, actual );
    }   //  testAddData()

    /**
     *  Tests for
     *  {@link org.tquadrat.foundation.xml.builder.ProcessingInstruction#addData(CharSequence)}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testAddDataWithEmptyArgument() throws Exception
    {
        skipThreadTest();

        final var elementName = "processingInstruction";
        final ProcessingInstruction candidate = new ProcessingInstructionImpl( elementName );
        assertNotNull( candidate );

        final Class<? extends Throwable> expectedException = EmptyArgumentException.class;
        try
        {
            candidate.addData( EMPTY_STRING );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e )
        {
            throw e;
        }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testAddDataWithEmptyArgument()

    /**
     *  Tests for
     *  {@link org.tquadrat.foundation.xml.builder.ProcessingInstruction#addData(CharSequence)}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testAddDataWithNullArgument() throws Exception
    {
        skipThreadTest();

        final var elementName = "processingInstruction";
        final ProcessingInstruction candidate = new ProcessingInstructionImpl( elementName );
        assertNotNull( candidate );

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            candidate.addData( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e )
        {
            throw e;
        }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testtestAddDataWithNullArgumentAddData()
}
//  class TestAddData

/*
 *  End of File
 */