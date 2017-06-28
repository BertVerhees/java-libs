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
                    if ("original_author".equals(key)) {
                        if (originalAuthor == null) {
                            originalAuthor = new HashMap<>();
                        } else {
                            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(attrValContext, "There can be only one original_author-section per description."));
                            return null;
                        }
                        if (attrValContext.object_block() != null) {
                            if (attrValContext.object_block().object_value_block() != null) {
                                for (ArchetypeParser.Keyed_objectContext keyedObjectContext : attrValContext.object_block().object_value_block().keyed_object()) {
                                    ArchetypeParser.String_valueContext stringValueContext = keyedObjectContext.primitive_value().string_value();
                                    BuilderUtils.addEntry(stringValueContext, keyedObjectContext.object_block().object_value_block(), originalAuthor, errorListener);
                                }
                            }
                        }
                    } else if ("other_contributors".equals(key)){
                        System.out.println(textContext.getText());

                    } else if ("lifecycle_state".equals(key)){

                    } else if ("details".equals(key)) {
                        System.out.println(textContext.getText());
                        if (details == null) {
                            details = new HashMap<>();
                        } else {
                            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(attrValContext, "There can be only one details-section per description."));
                            return null;
                        }
                        int j = 0;
                        if (attrValContext.object_block() != null) {
                            if (attrValContext.object_block().object_value_block() != null) {
//                                for (ArchetypeParser.Keyed_objectContext keyedObjectContext : textItemContext.object_block().object_value_block().keyed_object()) {
//                                    System.out.println(textItemContext.getText());
//                                    String keyS = keyedObjectContext.key().primitive_value().string_value().STRING().getText();
//                                    keyS = keyS.substring(1, key.length() - 1);
//                                    ResourceDescriptionItem resourceDescriptionItem = buildResourceDescriptionItem(textItemContext.object_block(), terminologyService, purpose);
//                                    details.put(keyS, resourceDescriptionItem);
//
//                                }
                            }
                        }
//                        for (ArchetypeParser.String_valueContext sa : detailsContext.string_value()) {
//                            ArchetypeParser.Description__details_object_blockContext o = detailsContext.description__details_object_block(j);
//                            j++;
//                            ResourceDescriptionItem resourceDescriptionItem = buildResourceDescriptionItem(o, terminologyService, purpose);
//                            String key = sa.STRING().getText();
//                            key = key.substring(1, key.length() - 1);
//                            details.put(key, resourceDescriptionItem);
//                        }

                    } else if ("resource_package_uri".equals(key)) {

                    } else if ("parent_resource".equals(key)) {

                    } else if ("other_details".equals(key)) {

                    }
                            System.out.println(textContext.getText());
                }
            }
//                    } else if ("other_contributors".equals(key)){
//                        if (otherContributors == null) {
//                            if (textItemContext.object_block().object_value_block().primitive_object().primitive_list_value().primitive_value().size() > 0) {
//                                otherContributors = new ArrayList<>();
//                            }
//                        } else {
//                            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(textItemContext, "There can be only one other_contributors-section per description."));
//                            return null;
//                        }
//                        if (textItemContext.object_block().object_value_block().primitive_object().primitive_list_value().primitive_value() != null) {
//                            for (ArchetypeParser.Primitive_valueContext primitiveValueContext : textItemContext.object_block().object_value_block().primitive_object().primitive_list_value().primitive_value()) {
//                                String otherContributor = primitiveValueContext.string_value().STRING().getText();
//                                otherContributor = otherContributor.substring(1, otherContributor.length() - 1);
//                                otherContributors.add(otherContributor);
//                            }
//                        }
//                    }  else if ("lifecycle_state".equals(key)){
//                        if (lifeCycleState == null) {
//                            lifeCycleState = textItemContext.object_block().object_value_block().primitive_object().primitive_value().string_value().STRING().getText();
//                            lifeCycleState = lifeCycleState.substring(1, lifeCycleState.length() - 1);
//                        } else {
//                            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(textItemContext, "There can be only one lifecycle_state-section per description."));
//                            return null;
//                        }
//                    } else if ("details".equals(key)) {
//                    }
//                    } else if (textItemContext.description__resource_package_uri() != null) {
//                        if (resourcePackageUri == null) {
//                            resourcePackageUri = textItemContext.description__resource_package_uri().string_value().STRING().getText();
//                            resourcePackageUri = resourcePackageUri.substring(1, resourcePackageUri.length() - 1);
//                        } else {
//                            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(textItemContext.description__resource_package_uri(), "There can be only one resource_package_uri-section per description."));
//                            return null;
//                        }
//                    } else if (textItemContext.other_details() != null) {
//                        if (otherDetails == null) {
//                            otherDetails = new HashMap<>();
//                        } else {
//                            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(textItemContext.other_details(), "There can be only one other_details-section per description."));
//                            return null;
//                        }
//                        ArchetypeParser.Other_detailsContext otherDetailsContext = textItemContext.other_details();
//                        int j = 0;
//                        for (ArchetypeParser.String_valueContext sa : otherDetailsContext.string_value()) {
//                            ArchetypeParser.Object_blockContext o = otherDetailsContext.object_block(j);
//                            j++;
//                            BuilderUtils.addEntry(sa, o, otherDetails, errorListener);
//                        }
//                    } else if (textItemContext.description__parent_resource() != null) {
//                        if (parentResource == null) {
//                            parentResource = textItemContext.description__parent_resource().string_value().STRING().getText();
//                            parentResource = parentResource.substring(1, parentResource.length() - 1);
//                        } else {
//                            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(textItemContext.description__parent_resource(), "There can be only one parent_resource-section per description."));
//                            return null;
//                        }
//                    }
//                }
//                return new ResourceDescription(originalAuthor, otherContributors, lifeCycleState, details, resourcePackageUri, otherDetails, null);
//            }
        }catch(Exception e){
            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(descriptionSection,  e.getMessage()));
        }
        return null;
    }

    private ResourceDescriptionItem buildResourceDescriptionItem(ArchetypeParser.Object_blockContext blockContext, TerminologyService terminologyService, String purposeParam) {
        ResourceDescriptionItem resourceDescriptionItem = null;
        CodePhrase language = null;
        List<String> keywords = null;
        String purpose = null;
        String use = null;
        String misuse = null;
        String copyRight = null;
        Map<String, String> originalResourceUri = null;
        Map<String, String> otherDetails = null;
//        try {
//            for (ArchetypeParser.Resource_description_itemContext itemContext : blockContext.object_value_block().resource_description_item()) {

//            }
//            for (ArchetypeParser.Resource_description_itemContext itemContext : blockContext.resource_description_item()) {
//                if (itemContext.language() != null) {
//                    if (language == null) {
//                        language = BuilderUtils.returnCodePhraseFromTermCodeRefString(itemContext.language().TERM_CODE_REF().getText(), errorListener);
//                    } else {
//                        errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(itemContext.language(), "There can be only one language-section per description."));
//                        return null;
//                    }
//                } else if ((itemContext.resource_description_item__purpose() != null)||(purposeParam!=null)) {
//                    if (purpose == null) {
//                        if(purposeParam!=null) {
//                            purpose = purposeParam;
//                            purposeParam = null;       //set null for renewed test, purposeParam may only be tested once
//                        }else if(itemContext.resource_description_item__purpose()!=null){
//                            purpose = itemContext.resource_description_item__purpose().string_value().STRING().getText();
//                            purpose = purpose.substring(1, purpose.length() - 1);
//                        }
//                    } else {
//                        errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(itemContext.resource_description_item__purpose(), "There can be only one purpose-section per description."));
//                        return null;
//                    }
//                } else if (itemContext.resource_description_item__keywords() != null) {
//                    if (keywords == null) {
//                        keywords = new ArrayList<>();
//                    } else {
//                        errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(itemContext.resource_description_item__keywords(), "There can be only one keywords-section per resource-description-item."));
//                        return null;
//                    }
//                    if(itemContext.resource_description_item__keywords().string_value()!=null) {
//                        for (ArchetypeParser.String_valueContext svc : itemContext.resource_description_item__keywords().string_value()) {
//                            String keyword = svc.STRING().getText();
//                            keyword = keyword.substring(1, keyword.length() - 1);
//                            keywords.add(keyword);
//                        }
//                    }else if(itemContext.resource_description_item__keywords().string_list_value()!=null){
//                        for (ArchetypeParser.String_valueContext iv : itemContext.resource_description_item__keywords().string_list_value().string_value()) {
//                            String keyword = iv.STRING().getText();
//                            keyword = keyword.substring(1, keyword.length() - 1);
//                            keywords.add(keyword);
//                        }
//                    }
//                } else if (itemContext.resource_description_item__use() != null) {
//                    if (use == null) {
//                        use = itemContext.resource_description_item__use().string_value().STRING().getText();
//                        use = use.substring(1, use.length() - 1);
//                    } else {
//                        errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(itemContext.resource_description_item__use(), "There can be only one use-section per resource-description-item."));
//                        return null;
//                    }
//                } else if (itemContext.resource_description_item__misuse() != null) {
//                    if (misuse == null) {
//                        misuse = itemContext.resource_description_item__misuse().string_value().STRING().getText();
//                        misuse = misuse.substring(1, misuse.length() - 1);
//                    } else {
//                        errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(itemContext.resource_description_item__misuse(),"There can be only one misuse-section per resource-description-item."));
//                        return null;
//                    }
//                } else if (itemContext.resource_description_item__copyright() != null) {
//                    if (copyRight == null) {
//                        copyRight = itemContext.resource_description_item__copyright().string_value().STRING().getText();
//                        copyRight = copyRight.substring(1, copyRight.length() - 1);
//                    } else {
//                        errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(itemContext.resource_description_item__copyright(), "There can be only one copyright-section per resource-description-item."));
//                        return null;
//                    }
//                } else if (itemContext.resource_description_item__original_resource_uri() != null) {
//                    if (originalResourceUri == null) {
//                        originalResourceUri = new HashMap<>();
//                    } else {
//                        errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(itemContext.resource_description_item__original_resource_uri(), "There can be only one original_resource_uri-section per resource-description-item."));
//                        return null;
//                    }
//                    ArchetypeParser.Resource_description_item__original_resource_uriContext originalResourceUriContext = itemContext.resource_description_item__original_resource_uri();
//                    int j = 0;
//                    for (ArchetypeParser.String_valueContext sa : originalResourceUriContext.string_value()) {
//                        ArchetypeParser.Object_blockContext o = originalResourceUriContext.object_block(j);
//                        j++;
//                        BuilderUtils.addEntry(sa, o, originalResourceUri, errorListener);
//                    }
//                } else if (itemContext.other_details() != null) {
//                    if (otherDetails == null) {
//                        otherDetails = new HashMap<>();
//                    } else {
//                        errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(itemContext.other_details(), "There can be only one other_details-section per resource-description-item."));
//                        return null;
//                    }
//                    ArchetypeParser.Other_detailsContext otherDetailsContext = itemContext.other_details();
//                    int j = 0;
//                    for (ArchetypeParser.String_valueContext sa : otherDetailsContext.string_value()) {
//                        ArchetypeParser.Object_blockContext o = otherDetailsContext.object_block(j);
//                        j++;
//                        BuilderUtils.addEntry(sa, o, otherDetails, errorListener);
//                    }
//                }
//            }
//            if ("".equals(use)) {
//                use = null;
//            }
//            if ("".equals(misuse)) {
//                misuse = null;
//            }
//            if ("".equals(copyRight)) {
//                copyRight = null;
//            }
//            return new ResourceDescriptionItem(language, purpose, keywords, use, misuse, copyRight, originalResourceUri, otherDetails, terminologyService);
//        }catch(Exception e){
//            errorListener.getParserErrors().addError(ArchetypeBuilderError.buildMessage(blockContext,  e.getMessage()));
            return null;
//        }
    }
}
