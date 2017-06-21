package se.acode.openehr.parser.builder;

import org.openehr.rm.common.resource.ResourceDescription;
import org.openehr.rm.common.resource.ResourceDescriptionItem;
import org.openehr.rm.datatypes.text.CodePhrase;
import org.openehr.rm.support.terminology.TerminologyService;
import se.acode.openehr.parser.ArchetypeParser;
import se.acode.openehr.parser.exception.ArchetypeBuilderException;

import java.util.ArrayList;
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

    public ResourceDescription getDescription(ArchetypeParser.Arch_descriptionContext descriptionSection, TerminologyService terminologyService, String rmRelease, String purpose) throws Exception {
        try {
            ArchetypeParser.Description_textContext textContext = descriptionSection.description_text();
            Map<String, String> originalAuthor = null;
            List<String> otherContributors = null;
            String lifeCycleState = null;
            Map<String, ResourceDescriptionItem> details = null;
            String resourcePackageUri = null;
            Map<String, String> otherDetails = null;
            String parentResource = null;
            if (textContext != null) {
                for (ArchetypeParser.Description_text_itemContext textItemContext : textContext.description_text_item()) {
                    if (textItemContext.original_author() != null) {
                        if (originalAuthor == null) {
                            originalAuthor = new HashMap<>();
                        } else {
                            throw new Exception("There can be only one original_author-section per description.");
                        }
                        ArchetypeParser.Original_authorContext original_authorContext = textItemContext.original_author();
                        int j = 0;
                        for (ArchetypeParser.String_valueContext sa : original_authorContext.string_value()) {
                            ArchetypeParser.Object_blockContext o = original_authorContext.object_block(j);
                            j++;
                            BuilderUtils.addEntry(sa, o, originalAuthor);
                        }
                    } else if (textItemContext.other_contributors() != null) {
                        if (otherContributors == null) {
                            if (textItemContext.other_contributors().string_value().size() > 0) {
                                otherContributors = new ArrayList<>();
                            }
                        } else {
                            throw new Exception("There can be only one other_contributors-section per description.");
                        }
                        if(textItemContext.other_contributors().string_value()!=null) {
                            for (ArchetypeParser.String_valueContext stringValueContext : textItemContext.other_contributors().string_value()) {
                                String otherContributor = stringValueContext.STRING().getText();
                                otherContributor = otherContributor.substring(1, otherContributor.length() - 1);
                                otherContributors.add(otherContributor);
                            }
                        }else if(textItemContext.other_contributors().string_list_value()!=null){
                            for (ArchetypeParser.String_valueContext iv : textItemContext.other_contributors().string_list_value().string_value()) {
                                String otherContributor = iv.STRING().getText();
                                otherContributor = otherContributor.substring(1, otherContributor.length() - 1);
                                otherContributors.add(otherContributor);
                            }
                        }
                    } else if (textItemContext.lifecycle_state() != null) {
                        if (lifeCycleState == null) {
                            lifeCycleState = textItemContext.lifecycle_state().string_value().STRING().getText();
                            lifeCycleState = lifeCycleState.substring(1, lifeCycleState.length() - 1);
                        } else {
                            throw new Exception("There can be only one lifecycle_state-section per description.");
                        }
                    } else if (textItemContext.details() != null) {
                        if (details == null) {
                            details = new HashMap<>();
                        } else {
                            throw new Exception("There can be only one details-section per description.");
                        }
                        ArchetypeParser.DetailsContext detailsContext = textItemContext.details();
                        int j = 0;
                        for (ArchetypeParser.String_valueContext sa : detailsContext.string_value()) {
                            ArchetypeParser.Details_object_blockContext o = detailsContext.details_object_block(j);
                            j++;
                            ResourceDescriptionItem resourceDescriptionItem = buildResourceDescriptionItem(o, terminologyService, purpose);
                            String key = sa.STRING().getText();
                            key = key.substring(1, key.length() - 1);
                            details.put(key, resourceDescriptionItem);
                        }
                    } else if (textItemContext.resource_package_uri() != null) {
                        if (resourcePackageUri == null) {
                            resourcePackageUri = textItemContext.resource_package_uri().string_value().STRING().getText();
                            resourcePackageUri = resourcePackageUri.substring(1, resourcePackageUri.length() - 1);
                        } else {
                            throw new Exception("There can be only one resource_package_uri-section per description.");
                        }
                    } else if (textItemContext.other_details() != null) {
                        if (otherDetails == null) {
                            otherDetails = new HashMap<>();
                        } else {
                            throw new Exception("There can be only one other_details-section per description.");
                        }
                        ArchetypeParser.Other_detailsContext otherDetailsContext = textItemContext.other_details();
                        int j = 0;
                        for (ArchetypeParser.String_valueContext sa : otherDetailsContext.string_value()) {
                            ArchetypeParser.Object_blockContext o = otherDetailsContext.object_block(j);
                            j++;
                            BuilderUtils.addEntry(sa, o, otherDetails);
                        }
                    } else if (textItemContext.parent_resource() != null) {
                        if (parentResource == null) {
                            parentResource = textItemContext.parent_resource().string_value().STRING().getText();
                            parentResource = parentResource.substring(1, parentResource.length() - 1);
                        } else {
                            throw new Exception("There can be only one parent_resource-section per description.");
                        }
                    }
                }
                return new ResourceDescription(originalAuthor, otherContributors, lifeCycleState, details, resourcePackageUri, otherDetails, null);
            }
        }catch(Exception e){
            throw new ArchetypeBuilderException(descriptionSection,  e.getMessage(), e);
        }
        return null;
    }

    private ResourceDescriptionItem buildResourceDescriptionItem(ArchetypeParser.Details_object_blockContext blockContext, TerminologyService terminologyService, String purposeParam) throws Exception {
        try {
            ResourceDescriptionItem resourceDescriptionItem = null;
            CodePhrase language = null;
            List<String> keywords = null;
            String purpose = null;
            String use = null;
            String misuse = null;
            String copyRight = null;
            Map<String, String> originalResourceUri = null;
            Map<String, String> otherDetails = null;
            for (ArchetypeParser.Resource_description_itemContext itemContext : blockContext.resource_description_item()) {
                if (itemContext.language() != null) {
                    if (language == null) {
                        language = BuilderUtils.returnCodePhraseFromTermCodeRefString(itemContext.language().TERM_CODE_REF().getText());
                    } else {
                        throw new Exception("There can be only one language-section per description.");
                    }
                } else if ((itemContext.purpose() != null)||(purposeParam!=null)) {
                    if (purpose == null) {
                        if(purposeParam!=null) {
                            purpose = purposeParam;
                            purposeParam = null;       //set null for renewed test, purposeParam may only be tested once
                        }else if(itemContext.purpose()!=null){
                            purpose = itemContext.purpose().string_value().STRING().getText();
                            purpose = purpose.substring(1, purpose.length() - 1);
                        }
                    } else {
                        throw new Exception("There can be only one purpose-section per description.");
                    }
                } else if (itemContext.keywords() != null) {
                    if (keywords == null) {
                        keywords = new ArrayList<>();
                    } else {
                        throw new Exception("There can be only one keywords-section per resource-description-item.");
                    }
                    for (ArchetypeParser.String_valueContext svc : itemContext.keywords().string_value()) {
                        String keyWord = svc.STRING().getText();
                        keyWord = keyWord.substring(1, keyWord.length() - 1);
                        keywords.add(keyWord);
                    }
                } else if (itemContext.use() != null) {
                    if (use == null) {
                        use = itemContext.use().string_value().STRING().getText();
                        use = use.substring(1, use.length() - 1);
                    } else {
                        throw new Exception("There can be only one use-section per resource-description-item.");
                    }
                } else if (itemContext.misuse() != null) {
                    if (misuse == null) {
                        misuse = itemContext.misuse().string_value().STRING().getText();
                        misuse = misuse.substring(1, misuse.length() - 1);
                    } else {
                        throw new Exception("There can be only one misuse-section per resource-description-item.");
                    }
                } else if (itemContext.copyright() != null) {
                    if (copyRight == null) {
                        copyRight = itemContext.copyright().string_value().STRING().getText();
                        copyRight = copyRight.substring(1, copyRight.length() - 1);
                    } else {
                        throw new Exception("There can be only one copyright-section per resource-description-item.");
                    }
                } else if (itemContext.original_resource_uri() != null) {
                    if (originalResourceUri == null) {
                        originalResourceUri = new HashMap<>();
                    } else {
                        throw new Exception("There can be only one original_resource_uri-section per resource-description-item.");
                    }
                    ArchetypeParser.Original_resource_uriContext originalResourceUriContext = itemContext.original_resource_uri();
                    int j = 0;
                    for (ArchetypeParser.String_valueContext sa : originalResourceUriContext.string_value()) {
                        ArchetypeParser.Object_blockContext o = originalResourceUriContext.object_block(j);
                        j++;
                        BuilderUtils.addEntry(sa, o, originalResourceUri);
                    }
                } else if (itemContext.other_details() != null) {
                    if (otherDetails == null) {
                        otherDetails = new HashMap<>();
                    } else {
                        throw new Exception("There can be only one other_details-section per resource-description-item.");
                    }
                    ArchetypeParser.Other_detailsContext otherDetailsContext = itemContext.other_details();
                    int j = 0;
                    for (ArchetypeParser.String_valueContext sa : otherDetailsContext.string_value()) {
                        ArchetypeParser.Object_blockContext o = otherDetailsContext.object_block(j);
                        j++;
                        BuilderUtils.addEntry(sa, o, otherDetails);
                    }
                }
            }
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
            throw new ArchetypeBuilderException(blockContext,  e.getMessage(), e);
        }
    }
}
