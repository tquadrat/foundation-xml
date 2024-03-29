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

package org.tquadrat.foundation.xml.builder.xmlbuilderutils;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.stripXMLComments;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.xml.builder.XMLBuilderUtils;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Some tests for the class
 *  {@link XMLBuilderUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestStripXMLComments.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestStripXMLComments.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.xmlbuilderutils.TestStripXMLComments" )
public class TestStripXMLComments extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link XMLBuilderUtils#stripXMLComments(CharSequence)}.
     */
    @Test
    final void testStripXMLComments()
    {
        skipThreadTest();

        String actual, candidate, expected;

        candidate = EMPTY_STRING;
        expected = candidate;
        actual = stripXMLComments( candidate );
        assertNotNull( actual );
        assertEquals( expected, actual );

        candidate = "No Comment";
        expected = candidate;
        actual = stripXMLComments( candidate );
        assertNotNull( actual );
        assertEquals( expected, actual );

        candidate = "<!-- Comment only -->";
        expected = EMPTY_STRING;
        actual = stripXMLComments( candidate );
        assertNotNull( actual );
        assertEquals( expected, actual );

        candidate = """
                    <!--
                        Comment only
                    -->""";
        expected = EMPTY_STRING;
        actual = stripXMLComments( candidate );
        assertNotNull( actual );
        assertEquals( expected, actual );

        candidate = "Text <!-- The comment --> with a comment";
        expected = "Text  with a comment";
        actual = stripXMLComments( candidate );
        assertNotNull( actual );
        assertEquals( expected, actual );

        candidate = """
                    Text <!--
                         The comment 
                         --> with a comment""";
        expected = """
                   Text  with a comment""";
        actual = stripXMLComments( candidate );
        assertNotNull( actual );
        assertEquals( expected, actual );

        candidate = """
                    Multi-line text
                    <!-- The comment -->
                    with a comment""";
        expected = """
                   Multi-line text
                   
                   with a comment""";
        actual = stripXMLComments( candidate );
        assertNotNull( actual );
        assertEquals( expected, actual );
    }   //  testStripXMLComments()

    /**
     *  Tests for
     *  {@link XMLBuilderUtils#stripXMLComments(CharSequence)}.
     */
    @Test
    final void testStripXMLCommentsWithNullArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            stripXMLComments( null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testStripXMLCommentsWithNullArgument()
}
//  class TestStripXMLComments

/*
 *  End of File
 */