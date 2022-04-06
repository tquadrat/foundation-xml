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

package org.tquadrat.foundation.xml.builder.spi.namespacesupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.xml.builder.Namespace;
import org.tquadrat.foundation.xml.builder.spi.NamespaceSupport;
import org.tquadrat.foundation.xml.helper.XMLTestBase;

/**
 *  Tests for the class
 *  {@link NamespaceSupport}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestSetNamespace.java 1030 2022-04-06 13:42:02Z tquadrat $
 */
@ClassVersion( sourceVersion = "$Id: TestSetNamespace.java 1030 2022-04-06 13:42:02Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.xml.builder.spi.namespacesupport.TestSetNamespace" )
public class TestSetNamespace extends XMLTestBase
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test for
     *  {@link NamespaceSupport#setNamespace(Namespace...)}
     */
    @Test
    final void testSetNamespace()
    {
        skipThreadTest();

        final var element = mockElement( "element" );
        replayAll();

        final var namespace1 = new Namespace( "prefix1", URI.create( "tquadrat.org/foundation" ) );
        final var namespace2 = new Namespace( "prefix2", URI.create( "tquadrat.org/tools" ) );

        final var candidate = new NamespaceSupport( element );
        assertNotNull( candidate );

        candidate.setNamespace( namespace1, null, namespace2 );
        assertEquals( 2, candidate.getNamespaces().size() );

        assertEquals( " xmlns:prefix1=\"tquadrat.org/foundation\" xmlns:prefix2=\"tquadrat.org/tools\"", candidate.toString( 0, false ) );
    }   //  testSetNamespace()
}
//  class TestSetNamespace

/*
 *  End of File
 */