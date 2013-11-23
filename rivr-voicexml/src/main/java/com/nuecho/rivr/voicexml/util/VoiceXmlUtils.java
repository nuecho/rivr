/*
 * Copyright (c) 2013 Nu Echo Inc. All rights reserved.
 */

package com.nuecho.rivr.voicexml.util;

import java.util.*;
import java.util.regex.*;

import com.nuecho.rivr.core.util.*;

/**
 * Utility class for VoiceXML-related aspects.
 * 
 * @author Nu Echo Inc.
 */
public final class VoiceXmlUtils {
    /**
     * As defined in http://www.w3.org/2004/03/voicexml20-errata.html#e8
     */
    private static final Pattern ALLOWED = Pattern.compile("([\\p{L}\\p{Nl}$]|[\\p{L}\\p{Nl}$]"
                                                           + "[\\p{L}\\p{Nl}\\p{Nd}\\p{Mn}\\p{Mc}\\p{Pc}$_]*"
                                                           + "[\\p{L}\\p{Nl}\\p{Nd}\\p{Mn}\\p{Mc}\\p{Pc}_])");

    private static final String[] WORDS = {"true",
                                           "false",
                                           "null",
                                           "undefined",
                                           "break",
                                           "else",
                                           "new",
                                           "var",
                                           "case",
                                           "finally",
                                           "return",
                                           "void",
                                           "catch",
                                           "for",
                                           "switch",
                                           "while",
                                           "continue",
                                           "function",
                                           "this",
                                           "with",
                                           "default",
                                           "if",
                                           "throw",
                                           "delete",
                                           "in",
                                           "try",
                                           "do",
                                           "instanceof",
                                           "typeof",
                                           "abstract",
                                           "enum",
                                           "int",
                                           "short",
                                           "boolean",
                                           "export",
                                           "interface",
                                           "static",
                                           "byte",
                                           "extends",
                                           "long",
                                           "super",
                                           "char",
                                           "final",
                                           "native",
                                           "synchronized",
                                           "class",
                                           "float",
                                           "package",
                                           "throws",
                                           "const",
                                           "goto",
                                           "private",
                                           "transient",
                                           "debugger",
                                           "implements",
                                           "protected",
                                           "volatile",
                                           "double",
                                           "import",
                                           "public"};

    private static final Set<String> RESERVED = new HashSet<String>(Arrays.asList(WORDS));

    /**
     * Checks if a name is a valid VoiceXML identifier as per VoiceXML 2.0
     * specification (section 5.1): <blockquote>The variable naming convention
     * is as in ECMAScript, but names beginning with the underscore character
     * ("_") and names ending with a dollar sign ("$") are reserved for internal
     * use. VoiceXML variables, including form item variables, must not contain
     * ECMAScript reserved words. They must also follow ECMAScript rules for
     * referential correctness. For example, variable names must be unique and
     * their declaration must not include a dot - "var x.y" is an illegal
     * declaration in ECMAScript. Variable names which violate naming
     * conventions or ECMAScript rules cause an 'error.semantic' event to be
     * thrown. </blockquote>
     * 
     * @param name the name to be tested
     * @return <code>true</code> if and only if the name is a valid VoiceXML
     *         identifier.
     */

    public static boolean isValidIdentifierName(String name) {
        Assert.notNull(name, "name");
        return ALLOWED.matcher(name).matches()
               && !RESERVED.contains(name)
               && !name.startsWith("_")
               && !name.startsWith("$");
    }

    private VoiceXmlUtils() {}
}