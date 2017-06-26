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
import se.acode.openehr.parser.v1_4.ArchetypeParser;
import se.acode.openehr.parser.errors.ArchetypeADLErrorListener;
import se.acode.openehr.parser.errors.ArchetypeBuilderError;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by verhees on 9-1-16.
 */
public class LanguagesSectionsBuilder {

    public static LanguagesSectionsBuilder getInstance() {
        return new LanguagesSectionsBuilder();
    }

    public CodePhrase getOriginalLanguage(ArchetypeParser.Arch_languageContext languageSection, ArchetypeADLErrorListener errorListener) {
        try {
            ArchetypeParser.Language_textContext languageTextContext = languageSection.language_text();
            if (languageTextContext == null) return null;
            ArchetypeParser.Original_languageContext originalLanguageContext = languageTextContext.original_language();
            return BuilderUtils.returnCodePhraseFromTermCodeRefString(languageTextContext.original_language().TERM_CODE_REF().getText(), errorListener);
        } catch (Exception e) {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(languageSection, e.getMessage()));
            return null;
        }
    }


    public Map<String, TranslationDetails> getTranslations(ArchetypeParser.Arch_languageContext languageSection, TerminologyService terminologyService, ArchetypeADLErrorListener errorListener) {
        try {
            ArchetypeParser.Language_textContext languageTextContext = languageSection.language_text();
            if (languageTextContext == null) return null;
            ArchetypeParser.TranslationsContext translations = languageSection.language_text().translations();
            if (translations == null)
                return null;
            Map<String, TranslationDetails> translationDetailsHashMap = new HashMap<String, TranslationDetails>();
            int i = 0;
            for (ArchetypeParser.String_valueContext s : translations.string_value()) {

                String keyString = s.STRING().getText();
                keyString = keyString.substring(1, keyString.length() - 1);
                ArchetypeParser.Language_object_blockContext lob = translations.language_object_block(i);
                i++;
                String accreditation = null;
                CodePhrase language = null;
                Map<String, String> author = null;
                Map<String, String> otherDetails = null;
                for (ArchetypeParser.Language_object_block_itemContext lobItem : lob.language_object_block_item()) {
                    if (lobItem.author() != null) {
                        if (author == null) {
                            author = new HashMap<>();
                        } else {
                            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(lobItem.author(), "There can be only one author-section per translation."));
                            return null;
                        }
                        ArchetypeParser.AuthorContext authorContext = lobItem.author();
                        int j = 0;
                        for (ArchetypeParser.String_valueContext sa : authorContext.string_value()) {
                            ArchetypeParser.Object_blockContext o = authorContext.object_block(j);
                            j++;
                            BuilderUtils.addEntry(sa, o, author, errorListener);
                        }
                    } else if (lobItem.accreditation() != null) {
                        if (accreditation != null) {
                            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(lobItem.accreditation(), "There can be only one accreditation-section per translation."));
                            return null;
                        }
                        accreditation = lobItem.accreditation().string_value().STRING().getText();
                        accreditation = accreditation.substring(1, accreditation.length() - 1);
                    } else if (lobItem.language() != null) {
                        if (language != null) {
                            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(lobItem.language(), "There can be only one language-section per translation."));
                            return null;
                        }
                        language = BuilderUtils.returnCodePhraseFromTermCodeRefString(lobItem.language().TERM_CODE_REF().getText(), errorListener);
                    } else if (lobItem.other_details() != null) {
                        if (otherDetails == null) {
                            otherDetails = new HashMap<>();
                        } else {
                            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(lobItem.other_details(), "There can be only one otherDetails-section per translation."));
                            return null;
                        }
                        ArchetypeParser.Other_detailsContext otherDetailsContext = lobItem.other_details();
                        int j = 0;
                        for (ArchetypeParser.String_valueContext sa : otherDetailsContext.string_value()) {
                            ArchetypeParser.Object_blockContext o = otherDetailsContext.object_block(j);
                            j++;
                            BuilderUtils.addEntry(sa, o, otherDetails,errorListener);
                        }
                    }
                }
                TranslationDetails td = new TranslationDetails(
                        language,
                        author,
                        accreditation,
                        otherDetails,
                        terminologyService
                );
                translationDetailsHashMap.put(keyString, td);
            }
            return translationDetailsHashMap;
        } catch (Exception e) {
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(languageSection, e.getMessage()));
            return null;
        }
    }
}
