package ru.efive.medicine.niidg.trfu.test;

import org.junit.Test;

public class DonorDAOTest {
	
	public DonorDAOTest() {
		
	}
	
	@Test
	public void testDAO() throws Exception {
		/*ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		DictionaryDAOImpl dao = (DictionaryDAOImpl) ctx.getBean("dictionaryDao");
		List<BloodComponentType> list = dao.findComponentTypeByValue("01.03.002 Кровь консервированная фильтрованная");
		for (BloodComponentType type: list) {
			System.out.println("Type: " + type.toString());
		}
		DonorDAOImpl dao = (DonorDAOImpl) ctx.getBean("donorDao");
		List<Donor> list = dao.findAvailableDocuments("", false, -1, -1, null, true);
		System.out.println(list.size());
		//dao.index();
		List<Donor> list = dao.search("est", 0, 25);
		System.out.println("found " + list.size() + " entries");
		for (Donor donor: list) {
			System.out.println("Found: " + dao.getHighlightedText(donor.getId()));
		}
		ExaminationRequestDAOImpl dao = (ExaminationRequestDAOImpl) ctx.getBean(ApplicationHelper.EXAMINATION_DAO);
		System.out.println("prognose " + dao.countDocument(false));
		/*List<ExaminationRequest> list = dao.findDocuments(false, 0, 25, "number", false);
		System.out.println("found: " + list.size());
		BloodComponentDAOImpl dao = (BloodComponentDAOImpl) ctx.getBean(ApplicationHelper.BLOOD_COMPONENT_DAO);
		long count = dao.countDocument("Эр", 3, false);
		System.out.println("count: " + count);
		BloodDonationRequestDAOImpl dao = (BloodDonationRequestDAOImpl) ctx.getBean(ApplicationHelper.DONATION_DAO);
		BloodDonationRequest donation = dao.get(323);
		donation.setReport(new PheresisReport());
		((PheresisDAOImpl) ctx.getBean(ApplicationHelper.PHERESIS_DAO)).save(donation.getReport());
		dao.save(donation);
		
		long count = dao.countDocument("тромбоци", false);
		
		ExaminationRequestDAOImpl erdao = (ExaminationRequestDAOImpl) ctx.getBean(ApplicationHelper.EXAMINATION_DAO);
		ExaminationRequest examination = erdao.get(206);
		
		Set<ExaminationEntry> entryList = new HashSet<ExaminationEntry>();
		DictionaryDAOImpl ddao = (DictionaryDAOImpl) ctx.getBean(ApplicationHelper.DICTIONARY_DAO);
		ExaminationEntryDAOImpl dao = (ExaminationEntryDAOImpl) ctx.getBean(ApplicationHelper.EXAMINATION_ENTRY_DAO);
		List<ExaminationEntryType> list = ddao.findBaseExaminationEntryTypes(false);
		for (ExaminationEntryType type:list) {
			List<ExaminationEntryType> child = ddao.findChildExaminationEntryTypes(false, type.getId());
			ExaminationEntry entry = new ExaminationEntry();
			entry.setDeleted(false);
			entry.setExaminationRequest(examination);
			entry.setType(type);
			entry.setValue(type.getDefaultValue());
			entry = dao.save(entry);
			entryList.add(entry);
			for (ExaminationEntryType childType: child) {
				ExaminationEntry childEntry = new ExaminationEntry();
				childEntry.setDeleted(false);
				childEntry.setExaminationRequest(examination);
				childEntry.setType(childType);
				childEntry.setValue(childType.getDefaultValue());
				childEntry.setParentEntry(entry);
				childEntry = dao.save(childEntry);
				entryList.add(childEntry);
			}
		}
		System.out.println("count: " + entryList.size());
		
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(357);
		ids.add(358);
		ids.add(359);
		ids.add(360);
		ids.add(361);
		ids.add(362);
		BloodDonationRequestDAOImpl rdao = (BloodDonationRequestDAOImpl) ctx.getBean(ApplicationHelper.DONATION_DAO);
		DictionaryDAOImpl ddao = (DictionaryDAOImpl) ctx.getBean(ApplicationHelper.DICTIONARY_DAO);
		AnalysisDAOImpl adao = (AnalysisDAOImpl) ctx.getBean(ApplicationHelper.ANALYSIS_DAO);
		List<AnalysisType> types = ddao.findAnalysisTypes("Иммуносерология", false);
		List<AnalysisType> types_base = ddao.findAnalysisTypes("Донация", false);
		for (Integer id: ids) {
			BloodDonationRequest donation = rdao.get(id);
			Set<Analysis> analysisList = new HashSet<Analysis>();
			for (AnalysisType type: types) {
				Analysis analysis = new Analysis();
				analysis.setType(type);
				analysis = adao.save(analysis);
				analysisList.add(analysis);
			}
			donation.setTestsImmuno(analysisList);
			analysisList = new HashSet<Analysis>();
			for (AnalysisType type: types_base) {
				Analysis analysis = new Analysis();
				analysis.setType(type);
				analysis = adao.save(analysis);
				analysisList.add(analysis);
			}
			donation.setTests(analysisList);
			rdao.save(donation);
		}
		/*BloodDonationRequestDAOImpl dao = (BloodDonationRequestDAOImpl) ctx.getBean(ApplicationHelper.DONATION_DAO);
		AnalysisDAOImpl adao = (AnalysisDAOImpl) ctx.getBean(ApplicationHelper.ANALYSIS_DAO);
		List<BloodDonationRequest> list = dao.findDocuments(false, -1, -1, "id", true);
		for (BloodDonationRequest request: list) {
			System.out.println("Drop d-fenotype: " + request.getId());
			List<Analysis> tests = request.getTestImmunoList();
			Set<Analysis> newTests = new HashSet<Analysis>();
			Analysis delete = null;
			for (Analysis analysis: tests) {
				if (analysis.getType().getId() == 66) {
					delete = analysis;
				}
				else {
					newTests.add(analysis);
				}
			}
			request.setTestsImmuno(newTests);
			dao.save(request);
			if (delete != null) adao.delete(delete);
			return;
		}
		BloodComponentDAOImpl dao = (BloodComponentDAOImpl) ctx.getBean(ApplicationHelper.BLOOD_COMPONENT_DAO);
		List<BloodComponent> list = dao.findDocuments("200197", 3, false, false, -1, -1, "number", false);
		for (BloodComponent component: list) {
			System.out.println("Component " + component.getNumber() + ", expired " + component.getExpirationDate());
		}
		
		DonorDAOImpl dao = (DonorDAOImpl) ctx.getBean(ApplicationHelper.DONOR_DAO);
		List<Donor> donors = dao.findDocuments(false, -1, -1, null, false);
		for (Donor donor: donors) {
			String firstName = donor.getFirstName();
			if (StringUtils.isEmpty(firstName)) {
				donor.setFirstName("******");
			}
			else if (StringUtils.length(firstName) < 3) {
				donor.setFirstName(firstName + "****");
			}
			else {
				donor.setFirstName(StringUtils.left(firstName, 3) + "****");
			}
			
			String middleName = donor.getMiddleName();
			if (StringUtils.isEmpty(middleName)) {
				donor.setMiddleName("******");
			}
			else if (StringUtils.length(middleName) < 3) {
				donor.setMiddleName(middleName + "****");
			}
			else {
				donor.setMiddleName(StringUtils.left(middleName, 3) + "****");
			}
			
			String lastName = donor.getLastName();
			if (StringUtils.isEmpty(lastName)) {
				donor.setLastName("******");
			}
			else if (StringUtils.length(lastName) < 3) {
				donor.setLastName(lastName + "****");
			}
			else {
				donor.setLastName(StringUtils.left(lastName, 3) + "****");
			}
			
			dao.save(donor);
		}
		ctx.close();*/
	}
}