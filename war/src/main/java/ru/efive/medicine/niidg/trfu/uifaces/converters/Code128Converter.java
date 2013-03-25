package ru.efive.medicine.niidg.trfu.uifaces.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("Code128Converter")
public class Code128Converter implements Converter
{
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		return arg2;
	}
	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		 int ind = 1;
	        int checksum = 104;
	        int dummy;
	        String code128; 
	        String chaine; 
	        int longueur;     

	        code128 = "";
	        chaine = arg2.toString();
	        longueur = chaine.length();
	        if (longueur == 0)
	        {
	            System.out.println("\n chaine vide");
	        }
	        else
	        {
	            for (ind = 0; ind < longueur; ind++)
	            {
	                if ((chaine.charAt(ind) < 32) || (chaine.charAt(ind)) > 126)
	                {
	                    System.out.println("\n chaine invalide");
	                }
	            }
	        }
	        ind = 0;
	        for (ind = 0; ind <= chaine.length()-1; ind++)
	        {
	            dummy = chaine.charAt(ind);
	            if (dummy < 127)
	            {
	                dummy = dummy - 32;
	            }
	            else
	            {
	                dummy = dummy - 100;
	            }

	            checksum = (checksum + (ind+1) * dummy);
	        }
	      checksum = (checksum) % 103;
	      if (checksum > 94)
		{checksum = checksum +100;}
	      else
		{checksum = checksum +32;}

			
	      code128 = String.valueOf((char) 204)+chaine + String.valueOf((char) checksum) + String.valueOf((char) 206);
	      return code128;
	}
}