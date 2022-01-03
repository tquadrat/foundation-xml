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

import static java.lang.Integer.max;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tquadrat.foundation.xml.builder.spi.SGMLPrinter.TAB_SIZE;
import static org.tquadrat.foundation.xml.builder.spi.SGMLPrinter.repeat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.builder.spi.SGMLPrinter;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Tests for the class
 *  {@link SGMLPrinter}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestRepeat.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestRepeat.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.spi.sgmlprinter.TestRepeat" )
public class TestRepeat extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for the methods
     *  {@link SGMLPrinter#repeat(int)}
     *  and
     *  {@link SGMLPrinter#repeat(int,int)}.
     */
    @Test
    final void testRepeat()
    {
        skipThreadTest();

        int expectedSize;
        String result;
        for( var i = -2; i < 5; ++i )
        {
            for( var j = -2; j < 10; ++j )
            {
                final var indentation = max( i, 0 );
                final var additionalBlanks = max( j, 0 );
                expectedSize = indentation * TAB_SIZE + additionalBlanks;
                result = repeat( indentation, additionalBlanks );
                assertNotNull( result );
                assertEquals( expectedSize, result.length() );
                if( expectedSize > 0 )
                {
                    assertTrue( result.chars().allMatch( c -> c == ' ' ) );
                }

                if( additionalBlanks == 0 )
                {
                    result = repeat( indentation );
                    assertNotNull( result );
                    assertEquals( expectedSize, result.length() );
                    if( expectedSize > 0 )
                    {
                        assertTrue( result.chars().allMatch( c -> c == ' ' ) );
                    }
                }
            }
        }
    }   //  testRepeat()
}
//  class TestRepeat

/*
 *  End of File
 */