/*
 * ============================================================================
 * Copyright © 2002-2021 by Thomas Thrien.
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

package org.tquadrat.foundation.xml.parse;

import static org.apiguardian.api.API.Status.STABLE;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 *  <p>{@summary This implementation for a
 *  {@linkplain ErrorHandler XML Error handler}
 *  swallows the error messages.} The methods</p>
 *  <ul>
 *      <li>{@link #error(SAXParseException) error()}</li>
 *      <li>{@link #fatalError(SAXParseException) fatalError()}</li>
 *      <li>{@link #warning(SAXParseException) warning()}</li>
 *  </ul>
 *  <p>have an empty body.</p>
 *  <p>This error handler is useful when really no internal error handling is
 *  desired; calling
 *  {@link javax.xml.parsers.DocumentBuilder#setErrorHandler(ErrorHandler)}
 *  with {@code null} as argument will activate a built-in error handler with
 *  an unpredictable behaviour.</p>
 *  <p>The one and only instance for this class can be obtained using the
 *  {@link #INSTANCE}
 *  constant.</p>
 *  <p>This simple initialisation pattern was chosen instead of a full
 *  Singleton setup because the error handler does not maintain a state.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: NullErrorHandler.java 895 2021-04-05 12:40:34Z tquadrat $
 *  @since 0.1.0
 *
 *  @see    org.tquadrat.foundation.xml.stringconverter.DocumentStringConverter
 *
 *  @UMLGraph.link
 */
@ClassVersion( sourceVersion = "$Id: NullErrorHandler.java 895 2021-04-05 12:40:34Z tquadrat $" )
@API( status = STABLE, since = "0.1.0" )
public final class NullErrorHandler implements ErrorHandler
{
        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The one and only instance of this class.
     */
    public static final NullErrorHandler INSTANCE = new NullErrorHandler();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code NullErrorHandler} instance.
     */
    private NullErrorHandler() { /* Does nothing! */ }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  {@inheritDoc}
     */
    @Override
    public final void error( final SAXParseException exception ) { /* Does nothing */ }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final void fatalError( final SAXParseException exception ) { /* Does nothing */ }

    /**
     *  {@inheritDoc}
     */
    @Override
    public final void warning( final SAXParseException exception ) { /* Does nothing */ }
}
//  class NullErrorHandler

/*
 *  End of File
 */