package ru.efive.wf.core.util;

import org.springframework.context.ApplicationContext;
import ru.efive.medicine.niidg.trfu.context.ApplicationContextHelper;

import javax.script.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroovyProcessor {
	
    public static Object processScript(String script, String alias, Object object) throws Exception {
    	Object result = null;
    	//FacesContext context = FacesContext.getCurrentInstance();
    	//if (context != null) {
    		//Object indexManagement = context.getELContext().getELResolver().getValue(context.getELContext(), null, "#{indexManagement}");
    		//if (indexManagement != null) {
				try {
					//Object applicationContext = PropertyUtils.getProperty(indexManagement, "context");
					//if (applicationContext != null) {
					ApplicationContext applicationContext = ApplicationContextHelper.getApplicationContext();
						ScriptEngineManager factory = new ScriptEngineManager();
						ScriptEngine engine = factory.getEngineByName("groovy");

						ScriptContext scriptContext = new SimpleScriptContext();
						Bindings engineScope = scriptContext.getBindings(ScriptContext.ENGINE_SCOPE);
						engineScope.put("context", applicationContext);
						engineScope.put(alias, object);
						
						result = engine.eval(script, scriptContext);
						System.out.println(result);
    				//}
    			}
    			catch (Exception e) {
    				e.printStackTrace();
                    throw e;
    			}
    		//}
    		//else {
    		//	System.out.println("Can't obtain spring context");
    		//}
    	//}
    	return result;
    }

    public static String processTemplate(String template, Map<String, Object> context) throws Exception {
        Pattern p = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher = p.matcher(template);
        StringBuffer sb = new StringBuffer(template.length());
        while (matcher.find()){
            String script = matcher.group(1);
            matcher.appendReplacement(sb, processScript(script,"localContext", context).toString());
        }
        matcher.appendTail(sb);
        return sb.toString();

    }
    
}