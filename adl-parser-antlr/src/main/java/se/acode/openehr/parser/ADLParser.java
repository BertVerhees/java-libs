package se.acode.openehr.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.openehr.am.archetype.Archetype;
import org.openehr.am.archetype.assertion.Assertion;
import org.openehr.am.archetype.constraintmodel.CComplexObject;
import org.openehr.am.archetype.ontology.ArchetypeOntology;
import org.openehr.rm.common.generic.RevisionHistory;
import org.openehr.rm.common.resource.ResourceDescription;
import org.openehr.rm.common.resource.TranslationDetails;
import org.openehr.rm.datatypes.text.CodePhrase;
import org.openehr.rm.support.identification.HierObjectID;
import org.openehr.rm.support.measurement.MeasurementService;
import org.openehr.rm.support.measurement.SimpleMeasurementService;
import org.openehr.rm.support.terminology.TerminologyService;
import org.openehr.terminology.SimpleTerminologyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.acode.openehr.parser.builder.DefinitionSectionBuilder;
import se.acode.openehr.parser.builder.DescriptionSectionBuilder;
import se.acode.openehr.parser.builder.LanguagesSectionsBuilder;
import se.acode.openehr.parser.builder.OntologySectionBuilder;
import se.acode.openehr.parser.errors.ArchetypeADLErrorListener;
import se.acode.openehr.parser.errors.ArchetypeADLParserErrors;
import se.acode.openehr.parser.errors.ArchetypeADLParserMessage;

import java.io.*;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;


/**
 * Created by verhees on 19-5-17.
 */
public class ADLParser {
    private static final Logger logger = LoggerFactory.getLogger(ADLParser.class);
    /* static fields */
    private static final String CHARSET = "UTF-8";
    public static final String RM_VERSION = "1.0.3";
    private static final String ATTRIBUTE_UNKNOWN = "__unknown__";

    private InputStream inputStream;

    /* member flags for backwards compatibility */
    private boolean missingLanguageCompatible = false;
    private boolean emptyPurposeCompatible = false;

    /* member fields */
    private MeasurementService measureServ =
            SimpleMeasurementService.getInstance();

  /* =======================  public constructors  ======================== */

    /* Constructor that takes file as input */
    public ADLParser(File file) throws IOException {
        this(new FileInputStream(file), CHARSET);
    }

    /* Constructor that takes string as input */
    public ADLParser(String value) throws UnsupportedEncodingException {
        new ByteArrayInputStream(value.getBytes("UTF-8"));
    }

    /* Constructor that takes file as input */
    public ADLParser(File file, boolean missingLanguageCompatible,
                     boolean emptyPurposeCompatible) throws IOException {
        this(new FileInputStream(file), CHARSET);
        this.missingLanguageCompatible = missingLanguageCompatible;
        this.emptyPurposeCompatible = emptyPurposeCompatible;
    }

    /* Constructor that takes string as input */
    public ADLParser(String value, boolean missingLanguageCompatible,
                     boolean emptyPurposeCompatible) throws IOException {
        this(new ByteArrayInputStream(value.getBytes("UTF-8")));
        this.missingLanguageCompatible = missingLanguageCompatible;
        this.emptyPurposeCompatible = emptyPurposeCompatible;
    }

    /* Constructor that takes InputStream as input */
    public ADLParser(InputStream input, boolean missingLanguageCompatible,
                     boolean emptyPurposeCompatible) throws IOException {
        this(input);
        this.missingLanguageCompatible = missingLanguageCompatible;
        this.emptyPurposeCompatible = emptyPurposeCompatible;
    }

    public ADLParser(InputStream is) throws IOException {
        this(is, null);
    }

    public ADLParser(InputStream is, String encoding) throws IOException {
        this.inputStream = is;
    }

    public void ReInit(InputStream is){
        this.inputStream = is;
    }

  /* =========================  public interface ======================== */


    public static TerminologyService terminologyService;
    static{
        try {
            terminologyService = SimpleTerminologyService.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MeasurementService measurementService;
    static{
        try {
            measurementService = SimpleMeasurementService.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArchetypeADLParserErrors errors;
    public ArchetypeADLErrorListener errorListener;

    private  ArchetypeParser  parser() {
        errors = new ArchetypeADLParserErrors();
        errorListener = new ArchetypeADLErrorListener(errors);
        CharStream input = null;
        try {
            input = CharStreams.fromStream(inputStream);
        }catch(Exception e){
            errorListener.getParserErrors().addError("ArchetypeParser: "+e.getMessage());
        }
        ArchetypeLexer lexer = new ArchetypeLexer(input);
        lexer.addErrorListener(errorListener);
        
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ArchetypeParser parser = new ArchetypeParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);
        return parser;
    }

    private String getADLVersion(ArchetypeParser.Arch_identificationContext identificationContext){
        if(identificationContext.arch_meta_data()==null) {
            String s = "Not Fatal: No arch_meta_data, no ADL-version, assuming 1.4";
            logger.warn(s);
            errorListener.getParserErrors().addWarning(s);
            return "1.4"; //probably old archetype
        }
        for(ArchetypeParser.Arch_meta_data_itemContext metaDataItemContext : identificationContext.arch_meta_data().arch_meta_data_item()){
            if(metaDataItemContext.adl_version()!=null)
                //this is because in ADL 1.4 the adl-version only has one dot, and then it cannot be distinguished from a REAL_VALUE
                if(metaDataItemContext.adl_version().VERSION_ID()!=null)
                    return metaDataItemContext.adl_version().VERSION_ID().getText();
                else
                    return metaDataItemContext.adl_version().REAL_VALUE().getText();
        }
        String s = "Not Fatal: No ADL-version, assuming 1.4";
        logger.warn(s);
        errorListener.getParserErrors().addWarning(s);
        return "1.4"; //probably old archetype
    }

    private String getUid(ArchetypeParser.Arch_identificationContext identificationContext){
        if(identificationContext.arch_meta_data()==null) {
            String s = "Not Fatal: No arch_meta_data, no uid";
            logger.warn(s);
            errorListener.getParserErrors().addWarning(s);
            return null;
        }
        for(ArchetypeParser.Arch_meta_data_itemContext metaDataItemContext : identificationContext.arch_meta_data().arch_meta_data_item()) {
            if (metaDataItemContext.uid() != null) {
                if(metaDataItemContext.uid().GUID()!=null)
                    return metaDataItemContext.uid().GUID().getText();
                else if(metaDataItemContext.uid().OID()!=null)
                    return metaDataItemContext.uid().OID().getText();
            }
        }
        String s = "Not Fatal: No uid";
        logger.warn(s);
        errorListener.getParserErrors().addWarning(s);
        return null;
    }

    private boolean isControlled(ArchetypeParser.Arch_identificationContext identificationContext){
        if(identificationContext.arch_meta_data()!=null)
            for(ArchetypeParser.Arch_meta_data_itemContext metaDataItemContext : identificationContext.arch_meta_data().arch_meta_data_item()){
                if(metaDataItemContext.is_controlled()!=null)
                    return true;
            }
        else {
            String s = "Not Fatal: No arch_meta_data, no is_controlled, returning false";
            logger.debug(s);
            errorListener.getParserErrors().addWarning(s);
        }
        return false;
    }

    private String getArchetypeId(ArchetypeParser.Arch_identificationContext identificationContext){
        if(identificationContext.ARCHETYPE_HRID()!=null){
            return identificationContext.ARCHETYPE_HRID().getText();
        }
        return null;
    }

    //for backwards compatibility
    public Archetype parse() throws Exception {
        return parseArchetype();
    }

    /* execute the parsing */
    public Archetype parseArchetype() {
        ArchetypeParser  parser = parser();
        ArchetypeParser.ArchetypeContext archetypeContext =  parser.archetype();
        if(missingLanguageCompatible){
            int index = -1;
            int i = 0;
            for(ArchetypeADLParserMessage s:errorListener.getParserErrors().getErrors()){
                if(s.getMessage().contains("expecting SYM_LANGUAGE")){
                    index = i;
                    break;
                }
                i++;
            }
            if(index>-1){
                errorListener.getParserErrors().getErrors().remove(index);
            }
        }
        ArchetypeParser.Arch_identificationContext identificationContext = archetypeContext.arch_identification();
        ArchetypeParser.Arch_specialisationContext specialisationContext = archetypeContext.arch_specialisation();
        ArchetypeParser.Arch_descriptionContext descriptionContext = archetypeContext.arch_description();
        ArchetypeParser.Arch_conceptContext conceptContext = archetypeContext.arch_concept();
        ArchetypeParser.Arch_languageContext languageContext = archetypeContext.arch_language();
        ArchetypeParser.Arch_definitionContext definitionContext = archetypeContext.arch_definition();
        ArchetypeParser.Arch_ontologyContext ontologyContext = archetypeContext.arch_ontology();
        String adlVersion = getADLVersion(identificationContext);
        String id = getArchetypeId(identificationContext);
        String parentId = null;
        if(specialisationContext!=null)
            if(specialisationContext.ARCHETYPE_HRID()!=null)
                parentId = specialisationContext.ARCHETYPE_HRID().getText();
        String concept = conceptContext.AT_CODE().getText();
        concept = concept.substring(1,concept.length()-1);
        CodePhrase originalLanguage = LanguagesSectionsBuilder.getInstance().getOriginalLanguage(languageContext, errorListener);
        Map<String, TranslationDetails> translations = LanguagesSectionsBuilder.getInstance().getTranslations(languageContext, terminologyService, errorListener);

        ResourceDescription description = DescriptionSectionBuilder.getInstance().getDescription(descriptionContext, terminologyService, RM_VERSION, null, errorListener);

        RevisionHistory revisionHistory = null;
        boolean isControlled = isControlled(identificationContext);
        String guid  = getUid(identificationContext);
        HierObjectID uid = null;
        if(guid!=null)
            uid = new HierObjectID(guid);
        CComplexObject definition = DefinitionSectionBuilder.getInstance().getDefinition(definitionContext, measurementService, errorListener);
        ArchetypeOntology ontology = OntologySectionBuilder.getInstance().getOntology(ontologyContext, terminologyService, errorListener);
        Set<Assertion> invariants = null;
        if(originalLanguage == null && missingLanguageCompatible) {
            String langCode = ontology.getPrimaryLanguage();
            originalLanguage = new CodePhrase("ISO_639-1", langCode);
        }
        if(description!=null){
            String purpose = description.getDetails().get(originalLanguage.getCodeString()).getPurpose();
            if ((purpose == null || purpose.length() == 0) && emptyPurposeCompatible) {
                purpose = ATTRIBUTE_UNKNOWN;
                description = DescriptionSectionBuilder.getInstance().getDescription(descriptionContext, terminologyService, RM_VERSION, purpose, errorListener);
            }
        }
        Archetype archetype = null;
        try {
            archetype = new Archetype(adlVersion,
                    id,
                    parentId,
                    concept,
                    originalLanguage,
                    translations,
                    description,
                    revisionHistory,
                    isControlled,
                    uid,
                    definition,
                    ontology,
                    invariants,
                    terminologyService);
        }catch(Exception e){
            Throwable t = e.getCause();
            if(t!=null) {
                while (t.getCause() != null) {
                    errorListener.getParserErrors().addError(t.getMessage());
                    t = t.getCause();
                }
            }
            errorListener.getParserErrors().addError(e.getMessage());
        }
        return archetype;
    }

    public void generatedParserException() throws Exception{
        if(errorListener.getParserErrors().getErrors().size()>0) {
            ListIterator <ArchetypeADLParserMessage> it = errorListener.getParserErrors().getErrors().listIterator();
            Exception e = null;
            while (it.hasNext()) {
                if (e != null)
                    e = new Exception(it.next().getMessage(), e);
                else
                    e = new Exception(it.next().getMessage());
            }
            if(e!=null)
                throw e;
        }
    }

    /* execute the parsing */
    private ArchetypeParser.ArchetypeContext parseArchetypeContext() {
        ArchetypeParser  parser = parser();
        ArchetypeParser.ArchetypeContext archetypeContext =  parser.archetype();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new ArchetypeBaseListener(), archetypeContext);
        return archetypeContext;
    }

    /* execute the parsing */
    public ArchetypeParser.Arch_descriptionContext parseDescriptionContext() {
        ArchetypeParser parser = parser();
        parser.arch_identification();
        parser.arch_specialisation();
        parser.arch_concept();
        parser.arch_language();
        ArchetypeParser.Arch_descriptionContext descriptionContext =  parser.arch_description();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new ArchetypeBaseListener(), descriptionContext);
        return descriptionContext;
    }

    public ArchetypeParser.Arch_definitionContext parseDefinitionContext() {
        ArchetypeParser  parser = parser();
        parser.arch_identification();
        parser.arch_specialisation();
        parser.arch_concept();
        parser.arch_language();
        parser.arch_description();
        ArchetypeParser.Arch_definitionContext definitionContext  =  parser.arch_definition();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new ArchetypeBaseListener(), definitionContext);
        return definitionContext;
    }

    public ArchetypeParser.Arch_ontologyContext parseOntologyContext() {
        ArchetypeParser  parser = parser();
        ArchetypeParser.Arch_ontologyContext ontologyContext  =  parser.arch_ontology();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new ArchetypeBaseListener(), ontologyContext);
        return ontologyContext;
    }

    public ArchetypeParser.Arch_identificationContext parseIdentification() {
        ArchetypeParser parser = parser();
        int numberOfErrors = parser.getNumberOfSyntaxErrors();
        ArchetypeParser.Arch_identificationContext identificationContext  =  parser.arch_identification();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new ArchetypeBaseListener(), identificationContext);
        return identificationContext;
    }

    public List<ArchetypeADLParserMessage> getErrors(){
        return  errors.getErrors();
    }

    public List<ArchetypeADLParserMessage> getWarnings(){
        return  errors.getWarnings();
    }

    public ArchetypeParser.Arch_specialisationContext parseSpecialisationContext() {
        ArchetypeParser  parser = parser();
        parser.arch_identification();
        ArchetypeParser.Arch_specialisationContext specialisationContext  =  parser.arch_specialisation();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new ArchetypeBaseListener(), specialisationContext);
        return specialisationContext;
    }

    public ArchetypeParser.Arch_conceptContext parseConceptContext() {
        ArchetypeParser  parser = parser();
        parser.arch_identification();
        parser.arch_specialisation();
        ArchetypeParser.Arch_conceptContext conceptContext  =  parser.arch_concept();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new ArchetypeBaseListener(), conceptContext);
        return conceptContext;
    }

    public ArchetypeParser.Arch_languageContext parseLanguageContext() {
        ArchetypeParser  parser = parser();
        parser.arch_identification();
        parser.arch_specialisation();
        parser.arch_concept();
        ArchetypeParser.Arch_languageContext languageContext =  parser.arch_language();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new ArchetypeBaseListener(), languageContext);
        return languageContext;
    }

    public Archetype parseInvariantContext() {
        ArchetypeParser  parser = parser();
        ArchetypeParser.Arch_invariantContext invariantContext =  parser.arch_invariant();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(new ArchetypeBaseListener(), invariantContext);
        return null;
    }

    /* ===================  entry point from command-line  ================ */
    public static void main(String args[]) throws IOException {
        ADLParser parser = null;
        String title = "ADL 1.4 Parser: ";

// read from stdin disabled
//    if (args.length == 0) {
//      System.out.println(title + "  Reading from standard input . . .");
//      parser = new ADLParser(System.in);
//    }

        if (args.length == 1) {
            System.out.println(title + "  Reading from file " + args[0] + " . . .");
            try {
                // be permissive in console mode
                parser = new ADLParser(new File(args[0]), true, true);
            } catch (IOException e) {
                System.out.println(title + "  File " + args[0] + " not found.");
                return;
            }
        } else if(args.length == 2 && "-d".equals(args[0])) {
            System.out.println(title + "  Reading from directory " + args[1] + " . . .");
            File dir = new File(args[1]);
            if( ! dir.isDirectory()) {
                System.out.println(args[1] + " not a directory.. aborted");
                return;
            }
            File[] files = dir.listFiles();
            if(files == null || files.length == 0) {
                System.out.println(args[1] + " has no file.. aborted");
                return;
            }
            int passed = 0;
            int total = 0;
            for(int i = 0; i < files.length; i++) {
                if( ! files[i].getName().endsWith(".adl")) {
                    continue;
                }
                if(parser == null) {
                    parser = new ADLParser(files[i], true, true);
                }
                total++;
                try {
//                    Archetype a = parser.parse();
                    System.out.println(files[i] + "  parsed successfully");
                    passed ++;
                } catch (Throwable e) {
                    e.printStackTrace();
                    System.out.println(files[i] + "  failed in parsing");
                }
            }
            System.out.println("Total files parsed: " + total);
            System.out.println("Parsed successfully: " + passed);
            System.out.println("Failed in parsing: " + (total - passed));

            return;
        } else {
            System.out.println(title + "  Usage is one of:");
            System.out.println("         java ADLParser < inputfile");
            System.out.println("OR");
            System.out.println("         java ADLParser inputfile");
            System.out.println("OR");
            System.out.println("         java ADLParser -d directory");
            return;
        }
        try {
//            Archetype a = parser.parse();
            // System.out.println(a.toString());
            System.out.println(title + "  ADL file parsed successfully.");
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println(title + "  Encountered errors during parsing " + args[0]);
        }
    }
}
