/*
 * ============================================================================
 * Copyright © 2002-2023 by Thomas Thrien.
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

import static java.lang.String.format;
import static java.lang.System.err;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 *  <p>{@summary This implementation for a
 *  {@linkplain ErrorHandler XML Error handler}
 *  will write the error messages to
 *  {@link System#err System.err}.}
 *  The one and only instance for this class can be obtained using the
 *  {@link #INSTANCE}
 *  constant.</p>
 *  <p>This simple initialisation pattern was chosen instead of a full
 *  Singleton setup because the error handler does not maintain a state.</p>
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: DefaultErrorHandler.java 1071 2023-09-30 01:49:32Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "UseOfSystemOutOrSystemErr" )
@ClassVersion( sourceVersion = "$Id: DefaultErrorHandler.java 1071 2023-09-30 01:49:32Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class DefaultErrorHandler implements ErrorHandler
{
        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The Error Message.
     */
    public static final String MSG_XMLError = "XML Parsing Error: %5$s - SystemId %1$s, PublicId %2$s, Line %3$d, Column %4$d";

    /**
     *  The Fatal Error Message.
     */
    public static final String MSG_XMLFatal = "Fatal XML Parsing Error: %5$s - SystemId %1$s, PublicId %2$s, Line %3$d, Column %4$d";

    /**
     *  The Warning Message.
     */
    public static final String MSG_XMLWarning = "XML Parsing Warning: %5$s - SystemId %1$s, PublicId %2$s, Line %3$d, Column %4$d";

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The one and only instance of this class.
     */
    public static final DefaultErrorHandler INSTANCE = new DefaultErrorHandler();

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  Creates a new {@code DefaultErrorHandler} instance.
     */
    private DefaultErrorHandler() { /* Does nothing! */ }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Receives and processes notification of a recoverable parser error. This
     *  implementation will just print the error to {@code System.out}..
     *
     *  @param  exception   The error exception.
     */
    @Override
    public final void error( final SAXParseException exception )
    {
        final var publicId = requireNonNullArgument( exception, "exception" ).getPublicId();
        final var systemId = exception.getSystemId();
        final var column = exception.getColumnNumber();
        final var line = exception.getLineNumber();
        //noinspection RedundantStringFormatCall
        err.println( format( MSG_XMLError, systemId, publicId, line, column, exception.getMessage() ) );
    }   //  error()

    /**
     *  Receives and processes notification of a fatal parser error. This
     *  implementation will just print the error to {@code System.out}..
     *
     *  @param  exception   The error exception.
     */
    @Override
    public final void fatalError( final SAXParseException exception )
    {
        final var publicId = requireNonNullArgument( exception, "exception" ).getPublicId();
        final var systemId = exception.getSystemId();
        final var column = exception.getColumnNumber();
        final var line = exception.getLineNumber();
        //noinspection RedundantStringFormatCall
        err.println( format( MSG_XMLFatal, systemId, publicId, line, column, exception.getLocalizedMessage() ) );
    }   //  fatalError()

    /**
     *  Receives and processes notification of a parser warning. This
     *  implementation will just print the warning to {@code System.out}..
     *
     *  @param  exception   The error exception.
     */
    @Override
    public final void warning( final SAXParseException exception )
    {
        final var publicId = requireNonNullArgument( exception, "exception" ).getPublicId();
        final var systemId = exception.getSystemId();
        final var column = exception.getColumnNumber();
        final var line = exception.getLineNumber();
        //noinspection RedundantStringFormatCall
        err.println( format( MSG_XMLWarning, systemId, publicId, line, column, exception.getLocalizedMessage() ) );
    }  //  warning()
}
//  class DefaultErrorHandler

/*
 *  End of File
 */