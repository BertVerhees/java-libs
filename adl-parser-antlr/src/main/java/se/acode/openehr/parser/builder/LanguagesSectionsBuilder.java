/*
 *   ***** BEGIN LICENSE BLOCK *****
 *   Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 *   The contents of this file are subject to the Mozilla Public License Version
 *   1.1 (the 'License'); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *   http://www.mozilla.org/MPL/
 *
 *   Software distributed under the License is distributed on an 'AS IS' basis,
 *   WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 *   for the specific language governing rights and limitations under the
 *   License.
 *
 *   Portions created by the Initial Developer are Copyright (C) 2016
 *   the Initial Developer. All Rights Reserved.
 *
 *   Contributor(s): Bert Verhees (bert.verhees@rosa.nl)
 *
 *  Software distributed under the License is distributed on an 'AS IS' basis,
 *  WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 *  for the specific language governing rights and limitations under the
 *  License.
 *
 *   ***** END LICENSE BLOCK *****
 *
 */

package se.acode.openehr.parser.builder;

import org.openehr.rm.common.resource.TranslationDetails;
import org.openehr.rm.datatypes.text.CodePhrase;
import org.openehr.rm.support.terminology.TerminologyService;
import se.acode.openehr.parser.errors.ArchetypeADLErrorListener;
import se.acode.openehr.parser.errors.ArchetypeBuilderError;
import se.acode.openehr.parser.v1_4.ArchetypeParser;

import java.util.HashMap;
import java.util.Map;

import static se.acode.openehr.parser.builder.BuilderUtils.handleSingleCodePhraseItem;
import static se.acode.openehr.parser.builder.BuilderUtils.handleSingleStringItem;
import static se.acode.openehr.parser.builder.BuilderUtils.handleStringStringMap;

/**
 * Created by verhees on 9-1-16.
 * This code follows the grammar, that is why it has this structure. To understand, keep the grammar for reference.
 */
public class LanguagesSectionsBuilder {

    public static LanguagesSectionsBuilder getInstance() {
        return new LanguagesSectionsBuilder();
    }

    public CodePhrase getOriginalLanguage(ArchetypeParser.Arch_languageContext languageSection, ArchetypeADLErrorListener errorListener) {
        try {
            CodePhrase originalLanguage = null;
            if( (languageSection.dadl_text()!=null)&&
                    (languageSection.dadl_text().attr_vals()!=null)&&
                    (languageSection.dadl_text().attr_vals().attr_val()!=null)) {
                for (ArchetypeParser.Attr_valContext attrValContext : languageSection.dadl_text().attr_vals().attr_val()) {
                    if(attrValContext.ALPHA_LC_ID() != null){
                        if ("original_language".equals(attrValContext.ALPHA_LC_ID().getText())) {
                            if ((attrValContext.object_block() != null) &&
                                (attrValContext.object_block().primitive_object() != null) &&
                                (attrValContext.object_block().primitive_object().primitive_value() != null) &&
                                (attrValContext.object_block().primitive_object().primitive_value().term_code_value() != null) &&
                                (attrValContext.object_block().primitive_object().primitive_value().term_code_value().TERM_CODE_REF() != null)) {
                                originalLanguage = handleSingleCodePhraseItem(originalLanguage, attrValContext.object_block().primitive_object().primitive_value().term_code_value().TERM_CODE_REF().getText(), attrValContext, "language", errorListener);
                            }
                            break;
                        }
                    }
                }
            }
            return originalLanguage;
        } catch (Exception e) {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(languageSection, e.getMessage()));
        }
        return null;
    }


    public Map<String, TranslationDetails> getTranslations(ArchetypeParser.Arch_languageContext languageSection, TerminologyService terminologyService, ArchetypeADLErrorListener errorListener) {
        try {
            Map<String, TranslationDetails> translationDetailsHashMap = null;
            if( (languageSection.dadl_text()!=null)&&
                    (languageSection.dadl_text().attr_vals()!=null)&&
                    (languageSection.dadl_text().attr_vals().attr_val()!=null)) {
                for (ArchetypeParser.Attr_valContext attrValContext : languageSection.dadl_text().attr_vals().attr_val()) {
                    if(attrValContext.ALPHA_LC_ID() != null){
                        if ("translations".equals(attrValContext.ALPHA_LC_ID().getText())) {
                            translationDetailsHashMap = new HashMap<>();
                            if( (attrValContext.object_block()!=null)&&
                                (attrValContext.object_block().keyed_object()!=null)){
                                for(ArchetypeParser.Keyed_objectContext keyedObjectContext:attrValContext.object_block().keyed_object()){
                                    if(     (keyedObjectContext.primitive_value()!=null)&&
                                            (keyedObjectContext.primitive_value().string_value()!=null)&&
                                            (keyedObjectContext.object_block()!=null)&&
                                            (keyedObjectContext.object_block().attr_vals()!=null)&&
                                            keyedObjectContext.object_block().attr_vals().attr_val()!=null) {
                                        String language = keyedObjectContext.primitive_value().string_value().getText();
                                        if(language.startsWith("\"")&&language.endsWith("\""))
                                            language = language.substring(1, language.length() - 1);
                                        CodePhrase languageCode = null;
                                        Map<String, String> author = null;
                                        String accreditation = null;
                                        Map<String, String> otherDetails = null;
                                        for(ArchetypeParser.Attr_valContext languageAttrValContext : keyedObjectContext.object_block().attr_vals().attr_val()){
                                            String key = languageAttrValContext.ALPHA_LC_ID().getText();
                                            if("language".equals(key)){
                                                languageCode = handleSingleCodePhraseItem(languageCode, languageAttrValContext.object_block().primitive_object().primitive_value().term_code_value().TERM_CODE_REF().getText(), languageAttrValContext, "language", errorListener);
                                            }else if("author".equals(key)){
                                                author = handleStringStringMap(author, languageAttrValContext, "author", errorListener);
                                            } else if("accreditation".equals(key)){
                                                accreditation = handleSingleStringItem(accreditation, languageAttrValContext.object_block().primitive_object().primitive_value().string_value().STRING().getText(), languageAttrValContext, "accreditation", errorListener);
                                            }else if("other_details".equals(key)){
                                                otherDetails = handleStringStringMap(otherDetails, languageAttrValContext, "other_details", errorListener);
                                            }
                                        }
                                        TranslationDetails td = new TranslationDetails(
                                                languageCode,
                                                author,
                                                accreditation,
                                                otherDetails,
                                                terminologyService
                                        );
                                        translationDetailsHashMap.put(language, td);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return translationDetailsHashMap;
        } catch (Exception e) {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(languageSection, e.getMessage()));
        }
        return null;
    }
}
