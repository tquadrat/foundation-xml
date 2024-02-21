/*
 * ============================================================================
 * Copyright © 2002-2024 by Thomas Thrien.
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

package org.tquadrat.foundation.xml.builder;

import static java.util.Locale.ROOT;
import static java.util.regex.Pattern.compile;
import static org.apiguardian.api.API.Status.STABLE;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.lang.Objects.isNull;
import static org.tquadrat.foundation.lang.Objects.nonNull;
import static org.tquadrat.foundation.lang.Objects.requireNonNullArgument;
import static org.tquadrat.foundation.lang.Objects.requireNotEmptyArgument;
import static org.tquadrat.foundation.util.StringUtils.isNotEmptyOrBlank;
import static org.tquadrat.foundation.util.StringUtils.splitString;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.Validator.VALIDATOR_AttributeName;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.Validator.VALIDATOR_ElementName;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.Validator.VALIDATOR_NMToken;
import static org.tquadrat.foundation.xml.builder.XMLBuilderUtils.Validator.VALIDATOR_Prefix;

import java.io.IOException;
import java.io.Serial;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apiguardian.api.API;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.annotation.UtilityClass;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.exception.PrivateConstructorForStaticClassCalledError;
import org.tquadrat.foundation.util.StringUtils;
import org.tquadrat.foundation.xml.builder.internal.ProcessingInstructionImpl;
import org.tquadrat.foundation.xml.builder.internal.XMLDocumentImpl;
import org.tquadrat.foundation.xml.builder.internal.XMLElementImpl;

/**
 *  A collection of XML related utility methods and factory methods for XML
 *  elements.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: XMLBuilderUtils.java 1101 2024-02-18 00:18:48Z tquadrat $
 *  @since 0.0.5
 *
 *  @UMLGraph.link
 */
@SuppressWarnings( "ClassWithTooManyMethods" )
@UtilityClass
@ClassVersion( sourceVersion = "$Id: XMLBuilderUtils.java 1101 2024-02-18 00:18:48Z tquadrat $" )
@API( status = STABLE, since = "0.0.5" )
public final class XMLBuilderUtils
{
        /*---------------*\
    ====** Inner Classes **====================================================
        \*---------------*/
    /**
     *  The (default) validators.
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: XMLBuilderUtils.java 1101 2024-02-18 00:18:48Z tquadrat $
     *  @since 0.0.5
     *
     *  @UMLGraph.link
     */
    @ClassVersion( sourceVersion = "$Id: XMLBuilderUtils.java 1101 2024-02-18 00:18:48Z tquadrat $" )
    @API( status = STABLE, since = "0.0.5" )
    public enum Validator
    {
            /*------------------*\
        ====** Enum Declaration **=============================================
            \*------------------*/
        /**
         *  The attribute name validator.
         */
        @SuppressWarnings( "synthetic-access" )
        VALIDATOR_AttributeName( XMLBuilderUtils::isValidAttributeName, XMLBuilderUtils::getAttributeNameValidator ),

        /**
         *  The element name validator.
         */
        @SuppressWarnings( "synthetic-access" )
        VALIDATOR_ElementName( XMLBuilderUtils::isValidElementName, XMLBuilderUtils::getElementNameValidator ),

        /**
         *  The nmtoken validator.
         */
        @SuppressWarnings( "synthetic-access" )
        VALIDATOR_NMToken( XMLBuilderUtils::isValidNMToken, XMLBuilderUtils::getNMTokenValidator ),

        /**
         *  The namespace prefix validator.
         */
        @SuppressWarnings( "synthetic-access" )
        VALIDATOR_Prefix( XMLBuilderUtils::isValidPrefix, XMLBuilderUtils::getPrefixValidator );

            /*------------*\
        ====** Attributes **===================================================
            \*------------*/
        /**
         *  The method that retrieves the current validator.
         */
        @SuppressWarnings( {"NonFinalFieldInEnum", "FieldMayBeFinal"} )
        private Supplier<? extends Predicate<CharSequence>> m_CurrentValidatorSupplier;

        /**
         *  The default validator.
         */
        @SuppressWarnings( {"FieldMayBeFinal", "NonFinalFieldInEnum"} )
        private Predicate<CharSequence> m_DefaultValidator;

            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code Validator} instance.
         *
         *  @param  defaultValidator    The default validator.
         *  @param  currentValidatorSupplier    The method that retrieves the
         *      current validator.
         */
        private Validator( final Predicate<CharSequence> defaultValidator, final Supplier<? extends Predicate<CharSequence>> currentValidatorSupplier )
        {
            m_CurrentValidatorSupplier = currentValidatorSupplier;
            m_DefaultValidator = defaultValidator;
        }   //  Validator()

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  Returns the current validator.
         *
         *  @return The current validator.
         */
        public final Predicate<CharSequence> getCurrent() { return m_CurrentValidatorSupplier.get(); }

        /**
         *  Returns the default validator.
         *
         *  @return The default validator.
         */
        @SuppressWarnings( "SuspiciousGetterSetter" )
        public final Predicate<CharSequence> getDefault() { return m_DefaultValidator; }
    }
    //  enum Validator

    /**
     *  The
     *  {@link EventObject}
     *  for changes to the validator configuration.
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: XMLBuilderUtils.java 1101 2024-02-18 00:18:48Z tquadrat $
     *  @since 0.0.5
     *
     *  @UMLGraph.link
     */
    @SuppressWarnings( "PublicInnerClass" )
    @ClassVersion( sourceVersion = "$Id: XMLBuilderUtils.java 1101 2024-02-18 00:18:48Z tquadrat $" )
    @API( status = STABLE, since = "0.0.5" )
    public static class ValidatorChangeEvent extends EventObject
    {
            /*------------------------*\
        ====** Static Initialisations **=======================================
            \*------------------------*/
        /**
         *  The serial version UID for objects of this class: {@value}.
         *
         *  @hidden
         */
        @Serial
        private static final long serialVersionUID = 1L;

            /*------------*\
        ====** Attributes **===================================================
            \*------------*/
        /**
         *  The new validator.
         *
         *  @serial
         */
        private final Predicate<CharSequence> m_NewValidator;

        /**
         *  The previous validator.
         *
         *  @serial
         */
        private final Predicate<CharSequence> m_OldValidator;

        /**
         *  The validator that changed.
         *
         *  @serial
         */
        private final Validator m_Validator;

            /*--------------*\
        ====** Constructors **=================================================
            \*--------------*/
        /**
         *  Creates a new {@code ValidatorChangeEvent} instance.
         *
         *  @param  validator   The validator that changed.
         *  @param  oldValidator    The previous validator.
         *  @param  newValidator    The new validator.
         */
        ValidatorChangeEvent( final Validator validator, final Predicate<CharSequence> oldValidator, final Predicate<CharSequence> newValidator )
        {
            super( requireNonNullArgument( validator, "validator" ) );
            m_Validator = validator;
            m_OldValidator = oldValidator;
            m_NewValidator = newValidator;
        }   //  ValidatorChangeEvent()

            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  Returns the new validator.
         *
         *  @return The new validator.
         */
        public final Predicate<CharSequence> getNewValidator() { return m_NewValidator; }

        /**
         *  Returns the previous validator.
         *
         *  @return The old validator.
         */
        public final Predicate<CharSequence> getOldValidator() { return m_OldValidator; }

        /**
         *  Gets the validator that was changed.
         *
         *  @return The changed validator.
         */
        public final Validator getValidator() { return m_Validator; }
    }
    //  class ValidatorChangeEvent

    /**
     *  The interface for listeners to
     *  {@link ValidatorChangeEvent}s
     *
     *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
     *  @version $Id: XMLBuilderUtils.java 1101 2024-02-18 00:18:48Z tquadrat $
     *  @since 0.0.5
     *
     *  @UMLGraph.link
     */
    @FunctionalInterface
    @ClassVersion( sourceVersion = "$Id: XMLBuilderUtils.java 1101 2024-02-18 00:18:48Z tquadrat $" )
    @API( status = STABLE, since = "0.0.5" )
    public static interface ValidatorChangeListener
    {
            /*---------*\
        ====** Methods **======================================================
            \*---------*/
        /**
         *  This method gets called each time a validator changes.
         *
         *  @param  event   The change event.
         */
        @SuppressWarnings( "UseOfConcreteClass" )
        public void validatorChanged( final ValidatorChangeEvent event );
    }
    //  interface ValidatorChangeListener

        /*-----------*\
    ====** Constants **========================================================
        \*-----------*/
    /**
     *  The regular expression for a valid start character of an XML name.
     *  Usually, the colon (':') is also allowed, but for namespace
     *  aware parsers, this is used as the separator between the namespace
     *  prefix and the name itself.
     */
    private static final String XML_NAME_FirstChar = """
        A-Z_a-z\
        \\u00C0-\\u00D6\\u00D8-\\u00F6\\u00F8-\\u02FF\\u0370-\\u037D\
        \\u037F-\\u1FFF\\u200C-\\u200D\\u2070-\\u218F\\u2C00-\\u2FEF\
        \\u3001-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFFD\\x{10000}-\\x{EFFFF}""";

    /**
     *  The regular expression for a character that is valid for an XML after
     *  the first character. Usually, the colon (':') is also allowed, but for
     *  namespace aware parsers, this is used as the separator between the
     *  namespace prefix and the name itself.
     */
    @SuppressWarnings( "ConstantExpression" )
    private static final String XML_NAME_OtherChar = "-"
        + XML_NAME_FirstChar
        + ".0-9\\u00B7\\u0300-\\u036F\\u203F-\\u2040";

        /*------------*\
    ====** Attributes **=======================================================
        \*------------*/
    /**
     *  The validator change listeners.
     */
    @SuppressWarnings( "StaticCollection" )
    private static final Collection<WeakReference<ValidatorChangeListener>> m_ValidatorChangeListeners = new ArrayList<>();

        /*------------------------*\
    ====** Static Initialisations **===========================================
        \*------------------------*/
    /**
     *  The method that validates an XML attribute name.
     */
    private static final AtomicReference<Predicate<CharSequence>> m_AttributeNameValidator = new AtomicReference<>( XMLBuilderUtils::isValidAttributeName );

    /**
     *  The method that validates an XML element name.
     */
    private static final AtomicReference<Predicate<CharSequence>> m_ElementNameValidator = new AtomicReference<>( XMLBuilderUtils::isValidElementName );

    /**
     *  The pattern that is used to validate a nmtoken.
     */
    private static final Pattern m_NMTokenPattern;

    /**
     *  The method that validates an XML nmtoken.
     */
    private static final AtomicReference<Predicate<CharSequence>> m_NMTokenValidator =  new AtomicReference<>( XMLBuilderUtils::isValidNMToken );

    /**
     *  The method that validates an XML namespace prefix.
     */
    private static final AtomicReference<Predicate<CharSequence>> m_PrefixValidator =  new AtomicReference<>( XMLBuilderUtils::isValidPrefix );

    /**
     *  The pattern that is used to validate an XML name.
     */
    private static final Pattern m_XMLNamePattern;

    static
    {
        //---* Defines the patterns for the validation *-----------------------
        try
        {
            //noinspection RegExpUnnecessaryNonCapturingGroup,ConstantExpression
            m_NMTokenPattern = compile( "(?:[" + XML_NAME_FirstChar + "])(?:[" + XML_NAME_OtherChar + ":])*" );
            //noinspection RegExpUnnecessaryNonCapturingGroup,ConstantExpression
            m_XMLNamePattern = compile( "(?:[" + XML_NAME_FirstChar + "])(?:[" + XML_NAME_OtherChar + "])*" );
        }
        catch( final PatternSyntaxException e )
        {
            throw new ExceptionInInitializerError( e );
        }
    }

        /*--------------*\
    ====** Constructors **=====================================================
        \*--------------*/
    /**
     *  No instance allowed for this class.
     */
    private XMLBuilderUtils() { throw new PrivateConstructorForStaticClassCalledError( XMLBuilderUtils.class ); }

        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Adds a validator change listener.
     *
     *  @param  listener    The listener.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final void addValidatorChangeListener( final ValidatorChangeListener listener )
    {
        final var reference = new WeakReference<>( requireNonNullArgument( listener, "listener" ) );
        synchronized( m_ValidatorChangeListeners )
        {
            m_ValidatorChangeListeners.add( reference );
        }
    }   //  addValidatorChangeListener()

    /**
     *  Composes the
     *  {@link ProcessingInstruction}
     *  for the XML file header.
     *
     *  @param  encoding    The encoding for the resulting document.
     *  @param  standalone  {@code true} if the XML document is standalone,
     *      {@code false} if not.
     *  @return The new processing instruction.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final ProcessingInstruction composeXMLHeader( final Charset encoding, final boolean standalone )
    {
        final var retValue = new ProcessingInstructionImpl();
        retValue.setAttribute( "version", "1.0" );
        retValue.setAttribute( "encoding", requireNonNullArgument( encoding, "encoding" ).name() );
        retValue.setAttribute( "standalone", standalone ? "yes" : "no" );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  composeXMLHeader()

    /**
     *  Creates a
     *  {@link ProcessingInstruction}.
     *
     *  @param  elementName The name for the processing instruction.
     *  @return The new processing instruction.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final ProcessingInstruction createProcessingInstruction( final String elementName )
    {
        final var retValue = new ProcessingInstructionImpl( elementName );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createProcessingInstruction()

    /**
     *  Creates a
     *  {@link ProcessingInstruction}.
     *
     *  @param  parent  The document that owns the new procession instruction.
     *  @param  elementName The name for the processing instruction.
     *  @return The new processing instruction.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final ProcessingInstruction createProcessingInstruction( final XMLDocument parent, final String elementName )
    {
        final var retValue = createProcessingInstruction( elementName );
        requireNonNullArgument( parent, "parent" ).addProcessingInstruction( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createProcessingInstruction()

    /**
     *  Creates a
     *  {@link ProcessingInstruction}.
     *
     *  @param  elementName The name for the processing instruction.
     *  @param  data    The data for the processing instruction.
     *  @return The new processing instruction.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final ProcessingInstruction createProcessingInstruction( final String elementName, final CharSequence data )
    {
        final var retValue = new ProcessingInstructionImpl( elementName, data );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createProcessingInstruction()

    /**
     *  Creates a
     *  {@link ProcessingInstruction}.
     *
     *  @param  parent  The document that owns the new procession instruction.
     *  @param  elementName The name for the processing instruction.
     *  @param  data    The data for the processing instruction.
     *  @return The new processing instruction.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final ProcessingInstruction createProcessingInstruction( final XMLDocument parent, final String elementName, final CharSequence data )
    {
        final var retValue = createProcessingInstruction( elementName, data );
        requireNonNullArgument( parent, "parent" ).addProcessingInstruction( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createProcessingInstruction()

    /**
     *  <p>{@summary Creates an XML document that will not have an explicit doc
     *  type, the root element will be {@code <root>}}. The encoding is defined
     *  as UTF-8.</p>
     *  <p>Basically, this document would have the DTD</p>
     *  <pre><code>&lt;!ELEMENT root ANY&gt;</code></pre>.
     *  <p>The root element allows attributes and children, but will not
     *  validate them. It also allows text.</p>
     *
     *  @return The new XML document.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final XMLDocument createXMLDocument() { return new XMLDocumentImpl(); }

    /**
     *  <p>{@summary Creates an XML document that uses the given element name for the root
      *  element.}</p>
     *  <p>The given element name is validated using the method that is
     *  provided by
     *  {@link #getElementNameValidator()}.</p>
     *  <p>The created root element allows attributes and children, but will
     *  not validate them. It also allows text.</p>
     *
     *  @param  elementName The element name.
     *  @return The new XML document.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final XMLDocument createXMLDocument( final String elementName )
    {
        return new XMLDocumentImpl( requireNotEmptyArgument( elementName, "elementName" ) );
    }   //  createXMLDocument()

    /**
     *  Creates an XML document that uses the given element for the root
     *  element.
     *
     *  @param  rootElement The root element.
     *  @return The new XML document.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final XMLDocument createXMLDocument( final XMLElement rootElement )
    {
        return createXMLDocument( requireNonNullArgument( rootElement, "rootElement" ), true );
    }   //  createXMLDocument()

    /**
     *  Creates an XML document that uses the given element for the root
     *  element.
     *
     *  @param  rootElement The root element.
     *  @param  standalone  {@code true} for a standalone document,
     *      {@code false} otherwise.
     *  @return The new XML document.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final XMLDocument createXMLDocument( final XMLElement rootElement, final boolean standalone )
    {
        return new XMLDocumentImpl( requireNonNullArgument( rootElement, "rootElement" ), standalone );
    }   //  createXMLDocument()

    /**
     *  Creates an XML document that uses the given element for the root
     *  element with the given encoding and DTD.
     *
     *  @param  rootElement The root element for this document.
     *  @param  encoding    The encoding for the new XML document.
     *  @param  name    The name for the DTD.
     *  @param  uri The URI for the DTD.
     *  @return The new XML document.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final XMLDocument createXMLDocument( final XMLElement rootElement, final Charset encoding, final String name, final URI uri )
    {
        return new XMLDocumentImpl( requireNonNullArgument( rootElement, "rootElement" ), requireNonNullArgument( encoding, "encoding" ), requireNotEmptyArgument( name, "name" ), requireNonNullArgument( uri, "uri" ) );
    }   //  createXMLDocument()

    /**
     *  Creates an XML document that uses the given element for the root
     *  element with the given encoding and DTD.
     *
     *  @param  rootElement The root element for this document.
     *  @param  encoding    The encoding for the new XML document.
     *  @param  uri The URI for the DTD.
     *  @return The new XML document.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final XMLDocument createXMLDocument( final XMLElement rootElement, final Charset encoding, final URI uri )
    {
        return new XMLDocumentImpl( requireNonNullArgument( rootElement, "rootElement" ), requireNonNullArgument( encoding, "encoding" ), requireNonNullArgument( uri, "uri" ) );
    }   //  createXMLDocument()

    /**
     *  Creates an XML element for the given element name that supports
     *  attributes, namespaces, children, text, {@code CDATA} and comments.<br>
     *  <br>The given element name is validated using the method that is
     *  provided by
     *  {@link #getElementNameValidator()}.<br>
     *  <br>The new element allows attributes and children, but will not
     *  validate them. It also allows text.
     *
     *  @param  elementName The element name.
     *  @return The new XML element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final XMLElement createXMLElement( final String elementName )
    {
        return new XMLElementImpl( requireNotEmptyArgument( elementName, "elementName" ) );
    }   //  createXMLElement()

    /**
     *  <p>{@summary Creates an XML element for the given element name that
     *  supports attributes, namespaces, children, text, {@code CDATA} and
     *  comments, and add the given text.}</p>
     *  <p>The given element name is validated using the method that is
     *  provided by
     *  {@link #getElementNameValidator()}.</p>
     *  <p>The new element allows attributes and children, but will not
     *  validate them. It also allows text (obviously).</p>
     *
     *  @param  elementName The element name.
     *  @param  text    The text for the new element.
     *  @return The new XML element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final XMLElement createXMLElement( final String elementName, final CharSequence text )
    {
        final XMLElement retValue = new XMLElementImpl( requireNotEmptyArgument( elementName, "elementName" ) );
        retValue.addText( requireNonNullArgument( text, "text" ) );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createXMLElement()

    /**
     *  Creates an XML element for the given element name that supports
     *  attributes, namespaces, children, text, {@code CDATA} and comments, and
     *  adds it as child to the given parent.<br>
     *  <br>The given element name is validated using the method that is
     *  provided by
     *  {@link #getElementNameValidator()}.<br>
     *  <br>The new element allows attributes and children, but will not
     *  validate them. It also allows text.
     *
     *  @param  elementName The element name.
     *  @param  parent  The parent element.
     *  @return The new XML element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final XMLElement createXMLElement( final String elementName, final XMLElement parent )
    {
        final XMLElement retValue = new XMLElementImpl( requireNotEmptyArgument( elementName, "elementName" ) );
        requireNonNullArgument( parent, "parent" ).addChild( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createXMLElement()

    /**
     *  Creates an XML element for the given element name that supports
     *  attributes, namespaces, children, text, {@code CDATA} and comments,
     *  adds the given text, and adds it as child to the given parent.<br>
     *  <br>The given element name is validated using the method that is
     *  provided by
     *  {@link #getElementNameValidator()}.<br>
     *  <br>The new element allows attributes and children, but will not
     *  validate them. It also allows text (obviously).
     *
     *  @param  elementName The element name.
     *  @param  parent  The parent element.
     *  @param  text    The text for the new element.
     *  @return The new XML element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final XMLElement createXMLElement( final String elementName, final XMLElement parent, final CharSequence text )
    {
        final XMLElement retValue = new XMLElementImpl( requireNotEmptyArgument( elementName, "elementName" ) );
        retValue.addText( requireNonNullArgument( text, "text" ) );
        requireNonNullArgument( parent, "parent" ).addChild( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createXMLElement()

    /**
     *  Creates an XML element for the given tag and adds it as child to the
     *  given document.<br>
     *  <br>The given element name is validated using the method that is
     *  provided by
     *  {@link #getElementNameValidator()}.<br>
     *  <br>The new element allows attributes and children, but will not
     *  validate them. It also allows text.
     *
     *  @param  elementName The element name.
     *  @param  parent  The document.
     *  @return The new XML element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final XMLElement createXMLElement( final String elementName, final XMLDocument parent )
    {
        final XMLElement retValue = new XMLElementImpl( requireNotEmptyArgument( elementName, "elementName" ) );
        requireNonNullArgument( parent, "parent" ).addChild( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createXMLElement()

    /**
     *  Creates an XML element for the given tag and with the given text, and
     *  adds it as child to the given document.<br>
     *  <br>The given element name is validated using the method that is
     *  provided by
     *  {@link #getElementNameValidator()}.<br>
     *  <br>The new element allows attributes and children, but will not
     *  validate them. It also allows text (obviously).
     *
     *  @param  elementName The element name.
     *  @param  parent  The document.
     *  @param  text    The text for the new element.
     *  @return The new XML element.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final XMLElement createXMLElement( final String elementName, final XMLDocument parent, final CharSequence text )
    {
        final XMLElement retValue = new XMLElementImpl( requireNotEmptyArgument( elementName, "elementName" ) );
        retValue.addText( requireNonNullArgument( text, "text" ) );
        requireNonNullArgument( parent, "parent" ).addChild( retValue );

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  createXMLElement()

    /**
     *  Escapes the characters in a {@code String} using XML entities.<br>
     *  <br>For example:<br>
     *  <br>{@code "bread" & "butter"}<br>
     *  <br>becomes:<br>
     *  <br><code>&amp;quot;bread&amp;quot; &amp;amp;
     *  &amp;quot;butter&amp;quot;</code>.<br>
     *  <br>Delegates to
     *  {@link StringUtils#escapeXML(CharSequence)}.
     *
     *  @param  input   The {@code String} to escape, may be null.
     *  @return A new escaped {@code String}, or {@code null} if the
     *      argument was already {@code null}.
     *
     *  @see #unescapeXML(CharSequence)
     */
    @API( status = STABLE, since = "0.0.5" )
    public static String escapeXML( final CharSequence input ) { return StringUtils.escapeXML( input ); }

    /**
     *  Escapes the characters in a {@code String} using XML entities and
     *  writes them to an
     *  {@link Appendable}.<br>
     *  <br>For example:<br>
     *  <br>{@code "bread" & "butter"}<br>
     *  <br>becomes:<br>
     *  <br><code>&amp;quot;bread&amp;quot; &amp;amp;
     *  &amp;quot;butter&amp;quot;</code>.<br>
     *  <br>Delegates to
     *  {@link StringUtils#escapeXML(Appendable,CharSequence)}.
     *
     *  @param  appendable  The appendable object receiving the escaped string.
     *  @param  input   The {@code String} to escape, may be {@code null}.
     *  @throws NullArgumentException   The appendable is {@code null}.
     *  @throws IOException when {@code Appendable} passed throws the exception
     *      from calls to the
     *      {@link Appendable#append(char)}
     *      method.
     *
     *  @see #escapeXML(CharSequence)
     *  @see #unescapeXML(CharSequence)
     */
    @API( status = STABLE, since = "0.0.5" )
    public static void escapeXML( final Appendable appendable, final CharSequence input ) throws IOException
    {
        StringUtils.escapeXML( appendable, input );
    }   //  escapeXML()

    /**
     *  Returns the method to validate attribute names.
     *
     *  @return The validator method.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Predicate<CharSequence> getAttributeNameValidator() { return m_AttributeNameValidator.get(); }

    /**
     *  Returns the method to validate element names.
     *
     *  @return The validator method.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Predicate<CharSequence> getElementNameValidator() { return m_ElementNameValidator.get(); }

    /**
     *  Returns the method to validate {@code nmtoken}s.
     *
     *  @return The validator method.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Predicate<CharSequence> getNMTokenValidator() { return m_NMTokenValidator.get(); }

    /**
     *  Returns the method to validate prefixes.
     *
     *  @return The validator method.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final Predicate<CharSequence> getPrefixValidator() { return m_PrefixValidator.get(); }

    /**
     *  The default implementation for an attribute name validator.
     *
     *  @param  attributeName   The name to test.
     *  @return {@code true} if the given name is valid, {@code false}
     *  otherwise.
     *
     *  @see #getAttributeNameValidator()
     */
    private static final boolean isValidAttributeName( final CharSequence attributeName )
    {
        var retValue = isNotEmptyOrBlank( attributeName );
        if( retValue )
        {
            var attribute = EMPTY_STRING;
            final var parts = splitString( attributeName, ':' );
            switch( parts.length )
            {
                //  No namespace prefix.
                case 1 -> attribute = parts[0];
                //  Namespace prefix.
                case 2 ->
                {
                    attribute = parts[1];
                    retValue = getPrefixValidator().test( parts[0] );
                }
                // More than one colon
                default -> retValue = false;
            }
            if( retValue && (attribute.length() >= 3) ) retValue = !attribute.toLowerCase( ROOT ).startsWith( "xml" );
            if( retValue ) retValue = m_XMLNamePattern.matcher( attribute ).matches();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isValidAttributeName()

    /**
     *  The default implementation for an element name validator.
     *
     *  @param  elementName   The name to test.
     *  @return {@code true} if the given name is valid, {@code false}
     *  otherwise.
     *
     *  @see #getElementNameValidator()
     */
    private static final boolean isValidElementName( final CharSequence elementName )
    {
        var retValue = isNotEmptyOrBlank( elementName );
        if( retValue )
        {
            var element = EMPTY_STRING;
            final var parts = splitString( elementName, ':' );
            switch( parts.length )
            {
                //  No namespace prefix.
                case 1 -> element = parts[0];
                //  Namespace prefix.
                case 2 ->
                {
                    element = parts[1];
                    retValue = getPrefixValidator().test( parts[0] );
                }
                // More than one colon
                default -> retValue = false;
            }
            if( retValue && (element.length() >= 3) ) retValue = !element.toLowerCase( ROOT ).startsWith( "xml" );
            if( retValue ) retValue = m_XMLNamePattern.matcher( element ).matches();
        }

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isValidElementName()

    /**
     *  The default implementation for an NMToken validator.
     *
     *  @param  nmtoken   The NMToken to test.
     *  @return {@code true} if the given NMToke is valid, {@code false}
     *  otherwise.
     *
     *  @see #getNMTokenValidator()
     */
    private static final boolean isValidNMToken( final CharSequence nmtoken )
    {
        var retValue = isNotEmptyOrBlank( nmtoken );
        if( retValue ) retValue = m_NMTokenPattern.matcher( nmtoken ).matches();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isValidNMToken()

    /**
     *  The default implementation for a prefix validator.
     *
     *  @param  prefix  The prefix to test.
     *  @return {@code true} if the given prefix is valid, {@code false}
     *  otherwise.
     *
     *  @see #getPrefixValidator()
     */
    private static final boolean isValidPrefix( final CharSequence prefix )
    {
        var retValue = isNotEmptyOrBlank( prefix );
        if( retValue ) retValue = m_XMLNamePattern.matcher( prefix ).matches();

        //---* Done *----------------------------------------------------------
        return retValue;
    }   //  isValidPrefix()

    /**
     *  Removes the given validator change listener.
     *
     *  @param  listener    The listener to remove.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final void removeValidatorChangeListener( final ValidatorChangeListener listener )
    {
        if( nonNull( listener ) )
        {
            synchronized( m_ValidatorChangeListeners )
            {
                var removed = false;
                for( final var reference : m_ValidatorChangeListeners )
                {
                    final var current = reference.get();
                    if( isNull( current ) )
                    {
                        removed = true;
                    }
                    else if( current == listener )
                    {
                        reference.clear();
                        removed = true;
                    }
                }

                //---* Housekeeping: get rid of the dead references *----------
                if( removed ) m_ValidatorChangeListeners.removeIf( r -> isNull( r.get() ) );
            }
        }
    }   //  removeValidatorChangeListener()

    /**
     *  Sets the validators back to the default configuration. The current
     *  validators are abandoned.
     *
     *  @see #getAttributeNameValidator()
     *  @see #getElementNameValidator()
     *  @see #getNMTokenValidator()
     *  @see #getPrefixValidator()
     *  @see #setAttributeNameValidator(Predicate)
     *  @see #setElementNameValidator(Predicate)
     *  @see #setNMTokenValidator(Predicate)
     *  @see #setPrefixValidator(Predicate)
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final void restoreDefaultValidators()
    {
        //---* Set the validator methods *-------------------------------------
        setAttributeNameValidator( XMLBuilderUtils::isValidAttributeName );
        setElementNameValidator( XMLBuilderUtils::isValidElementName );
        setNMTokenValidator( XMLBuilderUtils::isValidNMToken );
        setPrefixValidator( XMLBuilderUtils::isValidPrefix );
    }   //  restoreDefaultValidators()

    /**
     *  Sets the method to validate attribute names.
     *
     *  @param  validator The validator method.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final void setAttributeNameValidator( final Predicate<CharSequence> validator )
    {
        final var oldValidator = m_AttributeNameValidator.getAndSet( requireNonNullArgument( validator, "validator" ) );
        synchronized( m_ValidatorChangeListeners )
        {
            if( !m_ValidatorChangeListeners.isEmpty() )
            {
                final var event = new ValidatorChangeEvent( VALIDATOR_AttributeName, oldValidator, validator );
                for( final var reference : m_ValidatorChangeListeners )
                {
                    final var listener = reference.get();
                    if( nonNull( listener ) ) listener.validatorChanged( event );
                }
            }
        }
    }   //  setAttributeNameValidator()

    /**
     *  Sets the method to validate element names.
     *
     *  @param  validator The validator method.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final void setElementNameValidator( final Predicate<CharSequence> validator )
    {
        final var oldValidator = m_ElementNameValidator.getAndSet( requireNonNullArgument( validator, "validator" ) );
        synchronized( m_ValidatorChangeListeners )
        {
            if( !m_ValidatorChangeListeners.isEmpty() )
            {
                final var event = new ValidatorChangeEvent( VALIDATOR_ElementName, oldValidator, validator );
                for( final var reference : m_ValidatorChangeListeners )
                {
                    final var listener = reference.get();
                    if( nonNull( listener ) ) listener.validatorChanged( event );
                }
            }
        }
    }   //  setElementNameValidator()

    /**
     *  Sets the method to validate {@code nmtoken}s.
     *
     *  @param  validator The validator method.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final void setNMTokenValidator( final Predicate<CharSequence> validator )
    {
        final var oldValidator = m_NMTokenValidator.getAndSet( requireNonNullArgument( validator, "validator" ) );
        synchronized( m_ValidatorChangeListeners )
        {
            if( !m_ValidatorChangeListeners.isEmpty() )
            {
                final var event = new ValidatorChangeEvent( VALIDATOR_NMToken, oldValidator, validator );
                for( final var reference : m_ValidatorChangeListeners )
                {
                    final var listener = reference.get();
                    if( nonNull( listener ) ) listener.validatorChanged( event );
                }
            }
        }
    }   //  setNMTokenValidator()

    /**
     *  Sets the method to validate prefixes.
     *
     *  @param  validator The validator method.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final void setPrefixValidator( final Predicate<CharSequence> validator )
    {
        final var oldValidator = m_PrefixValidator.getAndSet( requireNonNullArgument( validator, "validator" ) );
        synchronized( m_ValidatorChangeListeners )
        {
            if( !m_ValidatorChangeListeners.isEmpty() )
            {
                final var event = new ValidatorChangeEvent( VALIDATOR_Prefix, oldValidator, validator );
                for( final var reference : m_ValidatorChangeListeners )
                {
                    final var listener = reference.get();
                    if( nonNull( listener ) ) listener.validatorChanged( event );
                }
            }
        }
    }   //  setPrefixValidator()

    /**
     *  <p>{@summary Strips HTML or XML comments from the given String.}</p>
     *  <p>Delegates to
     *  {@link StringUtils#stripXMLComments(CharSequence)}.</p>
     *
     *  @param  input   The HTML/XML string.
     *  @return The string without the comments.
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String stripXMLComments( final CharSequence input ) { return StringUtils.stripXMLComments( input ); }

    /**
     *  Unescapes an XML string containing XML entity escapes to a string
     *  containing the actual Unicode characters corresponding to the
     *  escapes.<br>
     *  <br>If an entity is unrecognised, it is left alone, and inserted
     *  verbatim into the result string. e.g. &quot;&amp;gt;&amp;zzzz;x&quot;
     *  will become &quot;&gt;&amp;zzzz;x&quot;.<br>
     *  <br>Delegates to
     *  {@link StringUtils#unescapeXML(CharSequence)}.
     *
     *  @param  input The {@code String} to unescape, may be {@code null}.
     *  @return A new unescaped {@code String}, {@code null} if the given
     *      string was already {@code null}.
     *
     *  @see #escapeXML(CharSequence)
     *  @see #escapeXML(Appendable,CharSequence)
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final String unescapeXML( final CharSequence input ) { return StringUtils.unescapeXML( input ); }

    /**
     *  Unescapes an XML String containing XML entity escapes to a String
     *  containing the actual Unicode characters corresponding to the escapes
     *  and writes it to the given
     *  {@link Appendable}.<br>
     *  <br>If an entity is unrecognised, it is left alone, and inserted
     *  verbatim into the result string. e.g. &quot;&amp;gt;&amp;zzzz;x&quot;
     *  will become &quot;&gt;&amp;zzzz;x&quot;.<br>
     *  <br>Delegates to
     *  {@link StringUtils#unescapeXML(Appendable,CharSequence)}.
     *
     *  @param  appendable  The appendable receiving the unescaped string.
     *  @param  input The {@code String} to unescape, may be {@code null}.
     *  @throws NullArgumentException   The writer is {@code null}.
     *  @throws IOException An IOException occurred.
     *
     *  @see #escapeXML(CharSequence)
     */
    @API( status = STABLE, since = "0.0.5" )
    public static final void unescapeXML( final Appendable appendable, final CharSequence input ) throws IOException
    {
        StringUtils.unescapeXML( appendable, input );
    }   //  unescapeXML()
}
//  class XMLBuilderUtils

/*
 *  End of File
 */