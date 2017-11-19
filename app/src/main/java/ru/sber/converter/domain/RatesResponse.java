package ru.sber.converter.domain;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;

import java.util.List;

import ru.sber.util.CustomDoubleTransformer;

/**
 * Created by Vitaly on 19.11.2017.
 */
@Root(name = "ValCurs")
public class RatesResponse {

    @ElementList(inline = true, name = "Valute")
    public List<CurrencyInfo> currencyInfoList;

    @Root(name = "Valute")
    public static class CurrencyInfo {

        @Element(name = "Name")
        public String name;

        @Element(name = "CharCode")
        public String charCode;

        @Element(name = "Value", required = false)
        @Convert(CustomDoubleTransformer.class)
        public Double value;
    }

}
