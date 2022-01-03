/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 *
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

package org.tquadrat.foundation.xml.parse;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import javax.xml.stream.Location;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.xml.sax.Locator;

/**
 *  An implementation of
 *  {@link org.xml.sax.Locator}
 *  that is based on an instance of
 *  {@link javax.xml.stream.Location}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: LocationLocator.java 820 2020-12-29 20:34:22Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: LocationLocator.java 820 2020-12-29 20:34:22Z tquadrat $" )
@API( status = EXPERIMENTAL, since = "0.0.5" )
public final class LocationLocator implements Locator
{
        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The location that provides the data.
     */
    private final Location m_Location;

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code LocationLocator} instance from the given instance
     *  of
     *  {@link Location}.
     *
     *  @param  location    The location.
     */
    public LocationLocator( final Location location ) { m_Location = requireNonNullArgument( location, "location" ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final int getColumnNumber() {return m_Location.getColumnNumber(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final int getLineNumber() {return m_Location.getLineNumber(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String getPublicId() {return m_Location.getPublicId(); }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final String getSystemId()  {return m_Location.getSystemId(); }
}
//  class LocationLocator

/*
 *  End of File
 */