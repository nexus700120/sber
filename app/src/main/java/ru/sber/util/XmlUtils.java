package ru.sber.util;

import android.support.annotation.Nullable;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

/**
 * Created by Vitaly on 19.11.2017.
 */

public class XmlUtils {

    private final Persister persister = new Persister(new AnnotationStrategy());
    private static XmlUtils sInstance;

    private XmlUtils() {}

    public static XmlUtils get() {
        if (sInstance == null) {
            sInstance = new XmlUtils();
        }
        return sInstance;
    }

    public @Nullable <T> T deserialize(String xml, Class<T> clazz) {
        if (xml == null || xml.isEmpty()) {
            return null;
        }

        try {
            return persister.read(clazz, xml, false);
        } catch (Exception e) {
            return null;
        }
    }
}
