/*
 * ============================================================================
 * Copyright © 2002-2024 by Thomas Thrien.
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

import org.tquadrat.foundation.xml.stringconverter.DocumentStringConverter;

/**
 *  The Foundation library module for XML handling.
 *
 *  @version $Id: module-info.java 990 2022-01-14 23:34:24Z tquadrat $
 *
 *  @todo task.list
 */
module org.tquadrat.foundation.xml
{
    requires java.base;
    requires transitive java.xml;
    requires transitive org.tquadrat.foundation.util;

    //---* Common Use *--------------------------------------------------------
    exports org.tquadrat.foundation.xml.builder;
    exports org.tquadrat.foundation.xml.parse;
    exports org.tquadrat.foundation.xml.parse.spi;
    exports org.tquadrat.foundation.xml.stringconverter;

    //---* Restricted Use *----------------------------------------------------
    exports org.tquadrat.foundation.xml.builder.spi
        to org.tquadrat.foundation.svg;

    provides org.tquadrat.foundation.lang.StringConverter with
        DocumentStringConverter;
}

/*
 *  End of File
 */