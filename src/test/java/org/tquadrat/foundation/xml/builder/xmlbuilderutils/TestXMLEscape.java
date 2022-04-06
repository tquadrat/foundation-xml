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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.escapeXML;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.unescapeXML;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.builder.XMLBuilderUtils;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Some tests for the class
 *  {@link XMLBuilderUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestXMLEscape.java 1030 2022-04-06 13:42:02Z tquadrat $
 */
@SuppressWarnings( "UnnecessaryUnicodeEscape" )
@ClassVersion( sourceVersion = "$Id: TestXMLEscape.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.xmlbuilderutils.TestXMLEscape" )
public class TestXMLEscape extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test for the methods
     *  {@link XMLBuilderUtils#escapeXML(CharSequence)}
     *  and
     *  {@link XMLBuilderUtils#unescapeXML(CharSequence)}.
     *
     *  @param  candidate   The data for the test.
     */
    @ParameterizedTest
    @ValueSource( strings = {
        "",
        " ",
        "&",
        "&amp;",
        "Zügig fährt Jörg über die Brücke, um nicht naß zu werden",
        "Erste Zeile\nZweite Zeile\nDritte Zeile\n",
        "<data key='key'>Record</data>",
        "\u5B8C\u5907\u7684\u4E16\u754C\u5927\u540C\u7684\u5B9E\u65BD\u89C4\u5212\u548C\u5168\u7403\u7EDF\u4E00\u884C\u653F\u4F53\u7CFB\u7684\u67B6\u6784\u84DD\u56FE"
    })
    final void testXMLEscapeUnescape( final String candidate )
    {
        skipThreadTest();

        final var actual = escapeXML( candidate );
        if( nonNull( candidate  ) )
        {
            assertNotNull( actual );
        }
        else
        {
            assertNull( actual );
        }
        final var revert = unescapeXML( actual );
        if( nonNull( candidate  ) )
        {
            assertNotNull( revert );
        }
        else
        {
            assertNull( revert );
        }
        assertEquals( candidate, revert );
    }   //  testXMLEscapeUnescape()

    /**
     *  Test for the methods
     *  {@link XMLBuilderUtils#escapeXML(Appendable,CharSequence)}
     *  and
     *  {@link XMLBuilderUtils#unescapeXML(Appendable,CharSequence)}.
     *
     *  @param  candidate   The data for the test.
     *  @throws IOException Unexpected &hellip;
     */
    @ParameterizedTest
    @ValueSource( strings = {
        "",
        " ",
        "&",
        "&amp;",
        "Zügig fährt Jörg über die Brücke, um nicht naß zu werden",
        "Erste Zeile\nZweite Zeile\nDritte Zeile\n",
        "<data key='key'>Record</data>",
        "\u5B8C\u5907\u7684\u4E16\u754C\u5927\u540C\u7684\u5B9E\u65BD\u89C4\u5212\u548C\u5168\u7403\u7EDF\u4E00\u884C\u653F\u4F53\u7CFB\u7684\u67B6\u6784\u84DD\u56FE"
    })
    final void testXMLEscapeUnescapeAppendable( final String candidate ) throws IOException
    {
        skipThreadTest();

        final var appendable = new StringBuilder();
        escapeXML( appendable, candidate );
        final var actual = appendable.toString();
        assertNotNull( actual );
        appendable.setLength( 0 );

        unescapeXML( appendable, actual );
        final var revert = appendable.toString();
        assertNotNull( revert );
        if( nonNull( candidate ) ) assertEquals( candidate, revert );
    }   //  testXMLEscapeUnescapeAppendable()
}
//  class TestXMLEscape

/*
 *  End of File
 */