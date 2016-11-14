package ru.efive.wf.test;

import org.junit.Test;

public class GroovyTest {
	
	public GroovyTest() {
		
	}
	
	@Test
	public void testGroovy() throws Exception {
		/*ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		BloodDonationRequestDAOImpl dao = (BloodDonationRequestDAOImpl) ctx.getBean(ApplicationHelper.DONATION_DAO);
		BloodDonationRequest donation = dao.get(411);
		
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("groovy");

		ScriptContext scriptContext = new SimpleScriptContext();
		Bindings engineScope = scriptContext.getBindings(ScriptContext.ENGINE_SCOPE);
		engineScope.put("context", ctx);
		engineScope.put("donation", donation);
		
		String script = "context.getBean(\"bloodComponentDao\").countQuarantinedDocumentByDonor(donation.getDonor(), \"\", false)";
		
		Object result = engine.eval(script, scriptContext);
		
		System.out.println("Result class: " + result.getClass());
		System.out.println("Is result long " + (result instanceof Long));
		System.out.println(result);
		
		ctx.close();*/
	}
	
}