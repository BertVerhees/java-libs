package org.openehr.terminology;

import org.openehr.rm.common.resource.TranslationDetails;
import org.openehr.rm.datatypes.text.CodePhrase;
import org.openehr.rm.support.terminology.TerminologyService;

import java.util.HashMap;
import java.util.Map;

public class TranslationDetailsTest  {
	
	public void testCreateTranslationDetails() throws Exception {
		CodePhrase language = new CodePhrase("ISO_639-1", "en");
		Map<String, String> author = new HashMap<String, String>();
		author.put("rong", "openehr");
		String accreditation = null;
		Map<String, String> otherDetails = null; 
		TerminologyService terminologyService = 
			SimpleTerminologyService.getInstance();
		TranslationDetails td = new TranslationDetails(language,
				author, accreditation, otherDetails, terminologyService);
	}
	
}
