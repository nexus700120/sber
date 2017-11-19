package ru.sber.util;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

/**
 * Created by Vitaly on 19.11.2017.
 */

public class CustomDoubleTransformer implements Converter<Double> {
    @Override
    public Double read(InputNode node) throws Exception {
        return Double.valueOf(node.getValue().replaceAll(",", "."));
    }

    @Override
    public void write(OutputNode node, Double value) throws Exception {
        node.setValue(String.valueOf(value));
    }
}
