package se.acode.openehr.parser.builder;

import org.openehr.rm.common.resource.ResourceDescription;
import org.openehr.rm.common.resource.ResourceDescriptionItem;
import org.openehr.rm.datatypes.text.CodePhrase;
import org.openehr.rm.support.terminology.TerminologyService;
import se.acode.openehr.parser.errors.ArchetypeADLErrorListener;
import se.acode.openehr.parser.errors.ArchetypeBuilderError;
import se.acode.openehr.parser.v1_4.ArchetypeParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static se.acode.openehr.parser.builder.BuilderUtils.*;

/**
 * Created by verhees on 25-5-17.
 */
public class DescriptionSectionBuilder {

    public static DescriptionSectionBuilder getInstance(){
        return new DescriptionSectionBuilder();
    }
    private ArchetypeADLErrorListener errorListener;

    public ResourceDescription getDescription(ArchetypeParser.Arch_descriptionContext descriptionSection, TerminologyService terminologyService, String rmRelease, String purpose, ArchetypeADLErrorListener errorListener) {
        this.errorListener = errorListener;
        try {
            ArchetypeParser.Dadl_textContext textContext = descriptionSection.dadl_text();
            Map<String, String> originalAuthor = null;
            List<String> otherContributors = null;
            String lifeCycleState = null;
            Map<String, ResourceDescriptionItem> details = null;
            String resourcePackageUri = null;
            Map<String, String> otherDetails = null;
            String parentResource = null;
            if (textContext != null) {
                for (ArchetypeParser.Attr_valContext attrValContext : textContext.attr_vals().attr_val()) {
                    String key = attrValContext.ALPHA_LC_ID().getText();
                    //values which return a single string/codephrase
                    if((attrValContext.object_block()!=null)&&
                            (attrValContext.object_block().object_value_block()!=null)&&
                            (attrValContext.object_block().object_value_block().primitive_object()!=null)&&
                            (attrValContext.object_block().object_value_block().primitive_object().primitive_value()!=null)&&
                            (attrValContext.object_block().object_value_block().primitive_object().primitive_value().string_value()!=null)) {
                        String item = attrValContext.object_block().object_value_block().primitive_object().primitive_value().string_value().STRING().getText();
                        item = item.substring(1, item.length() - 1);
                        if ("lifecycle_state".equals(key)) {
                            lifeCycleState = handleSingleStringItem(lifeCycleState, item, attrValContext, "lifecycle_state", errorListener);
                        }else if ("parent_resource".equals(key)) {
                            parentResource = handleSingleStringItem(parentResource, item, attrValContext, "parent_resource", errorListener);
                        }else if ("resource_package_uri".equals(key)) {
                            resourcePackageUri = handleSingleStringItem(resourcePackageUri, item, attrValContext, "resource_package_uri", errorListener);
                        }else if ("other_contributors".equals(key)) {
                            otherContributors = handleSingleStringItemList(otherContributors, item, attrValContext, "keywords", errorListener);
                        }
                        //values which return a list
                    }else if((attrValContext.object_block()!=null)&&
                                (attrValContext.object_block().object_value_block()!=null)&&
                                (attrValContext.object_block().object_value_block().primitive_object()!=null)&&
                                (attrValContext.object_block().object_value_block().primitive_object().primitive_list_value()!=null)&&
                                (attrValContext.object_block().object_value_block().primitive_object().primitive_list_value().primitive_value()!=null)) {

                        if ("other_contributors".equals(key)) {
                            otherContributors = handleStringItemList(otherContributors, attrValContext, "other_contributors", errorListener);
                        }
                        //values which return a map
                    }else if((attrValContext.object_block()!=null)&&
                            (attrValContext.object_block().object_value_block()!=null)&&
                            (attrValContext.object_block().object_value_block().keyed_object()!=null)){
                        if ("original_author".equals(key)){
                            originalAuthor = handleStringStringMap(originalAuthor, attrValContext, "original_author", errorListener );
                        } else if ("other_details".equals(key)) {
                            otherDetails = handleStringStringMap(otherDetails, attrValContext, "other_details", errorListener );
                        }
                    }else if ("details".equals(key)) {
                        if (details == null) {
                            details = new HashMap<>();
                        } else {
                            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(attrValContext, "There can be only one details-section per description."));
                        }
                        int j = 0;
                        if (attrValContext.object_block() != null) {
                            if (attrValContext.object_block().object_value_block() != null) {
                                for (ArchetypeParser.Keyed_objectContext keyedObjectContext : attrValContext.object_block().object_value_block().keyed_object()) {
                                    System.out.println(attrValContext.getText());
                                    String keyLanguage = keyedObjectContext.primitive_value().string_value().STRING().getText();
                                    keyLanguage = keyLanguage.substring(1, keyLanguage.length() - 1);
                                    ResourceDescriptionItem resourceDescriptionItem = buildResourceDescriptionItem(keyedObjectContext.object_block(), terminologyService, purpose);
                                    details.put(keyLanguage, resourceDescriptionItem);
                                }
                            }
                        }
                    }
                }
                return new ResourceDescription(originalAuthor, otherContributors, lifeCycleState, details, resourcePackageUri, otherDetails, null);
            }
        }catch(Exception e){
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(descriptionSection,  e.getMessage()));
        }
        return null;
    }

    private ResourceDescriptionItem buildResourceDescriptionItem(ArchetypeParser.Object_blockContext blockContext, TerminologyService terminologyService, String purposeParam) {
        CodePhrase language = null;
        List<String> keywords = null;
        String purpose = null;
        String use = null;
        String misuse = null;
        String copyRight = null;
        Map<String, String> originalResourceUri = null;
        Map<String, String> otherDetails = null;
        try {
            for (ArchetypeParser.Attr_valContext attrValContext : blockContext.object_value_block().attr_vals().attr_val()) {
                String key = attrValContext.ALPHA_LC_ID().getText();
                //values which return a single string
                if((attrValContext.object_block()!=null)&&
                        (attrValContext.object_block().object_value_block()!=null)&&
                        (attrValContext.object_block().object_value_block().primitive_object()!=null)&&
                        (attrValContext.object_block().object_value_block().primitive_object().primitive_value()!=null)&&
                        (attrValContext.object_block().object_value_block().primitive_object().primitive_value().string_value()!=null)) {
                    String item = attrValContext.object_block().object_value_block().primitive_object().primitive_value().string_value().STRING().getText();
                    item = item.substring(1, item.length() - 1);
                    if ("purpose".equals(key)) {
                        if (purposeParam != null) {
                            purpose = purposeParam;
                            purposeParam = null;       //set null for renewed test, purposeParam may only be tested once
                        }
                        purpose = handleSingleStringItem(purpose, item, attrValContext, "purpose", errorListener);
                    } else if ("use".equals(key)) {
                        use = handleSingleStringItem(use, item, attrValContext, "use", errorListener);
                    } else if ("misuse".equals(key)) {
                        misuse = handleSingleStringItem(misuse, item, attrValContext, "misuse", errorListener);
                    } else if ("copyright".equals(key)) {
                        copyRight = handleSingleStringItem(copyRight, item, attrValContext, "copyright", errorListener);
                    } else if ("keywords".equals(key)) {
                        keywords = handleSingleStringItemList(keywords, item, attrValContext, "keywords", errorListener);
                    }
                    //values which return a single codephrase
                }else if((attrValContext.object_block()!=null)&&
                        (attrValContext.object_block().object_value_block()!=null)&&
                        (attrValContext.object_block().object_value_block().primitive_object()!=null)&&
                        (attrValContext.object_block().object_value_block().primitive_object().primitive_value()!=null)&&
                        (attrValContext.object_block().object_value_block().primitive_object().primitive_value().term_code_value()!=null)) {
                    if ("language".equals(key)) {
                        language = handleSingleCodePhraseItem(language, attrValContext.object_block().object_value_block().primitive_object().primitive_value().term_code_value().TERM_CODE_REF().getText(), attrValContext, "language", errorListener);
                    }
                    //values which return a list
                }else if((attrValContext.object_block()!=null)&&
                        (attrValContext.object_block().object_value_block()!=null)&&
                        (attrValContext.object_block().object_value_block().primitive_object()!=null)&&
                        (attrValContext.object_block().object_value_block().primitive_object().primitive_list_value()!=null)&&
                        (attrValContext.object_block().object_value_block().primitive_object().primitive_list_value().primitive_value()!=null)){

                    if ("keywords".equals(key)){
                        keywords = handleStringItemList(keywords, attrValContext, "keywords", errorListener);
                    }
                    //values which return a map
                }else if((attrValContext.object_block()!=null)&&
                        (attrValContext.object_block().object_value_block()!=null)&&
                        (attrValContext.object_block().object_value_block().keyed_object()!=null)){
                    if ("original_resource_uri".equals(key)){
                        originalResourceUri = handleStringStringMap(originalResourceUri, attrValContext, "keywords", errorListener);
                    } else if ("other_details".equals(key)) {
                        otherDetails = handleStringStringMap(otherDetails, attrValContext, "other_details", errorListener );
                    }
                }
            }
            //Object creation does not accept empty values
            if ("".equals(use)) {
                use = null;
            }
            if ("".equals(misuse)) {
                misuse = null;
            }
            if ("".equals(copyRight)) {
                copyRight = null;
            }
            return new ResourceDescriptionItem(language, purpose, keywords, use, misuse, copyRight, originalResourceUri, otherDetails, terminologyService);
        }catch(Exception e){
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(blockContext,  e.getMessage()));
            return null;
        }
    }
}
