package ru.efive.uifaces.bean;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.TimeZone;

/**
 * <code>TimeBean</code> provides timezone information.
 * 
 * @author Pavel Porubov
 */
@Named("e5ui_timeBean")
@SessionScoped
public class TimeBean implements Serializable {

    /**
     * Class version identifier used during serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Returns default timezone.
     * 
     * @return default timezone.
     */
    public TimeZone getDefaultTimeZone() {
        return TimeZone.getDefault();
    }

    /**
     * Returns timezone for ID.
     * @param ID
     * @return timezone for ID.
     */
    public TimeZone getTimeZone(String ID) {
        return TimeZone.getTimeZone(ID);
    }
}
