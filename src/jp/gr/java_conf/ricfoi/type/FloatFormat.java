package jp.gr.java_conf.ricfoi.type;

import java.text.NumberFormat;
import java.util.Locale;

public class FloatFormat {

    public static NumberFormat getInstance() {
		NumberFormat fmt = NumberFormat.getNumberInstance(Locale.US);
		fmt.setMaximumFractionDigits(5);
		fmt.setMinimumFractionDigits(0);
		return fmt;
    }

}
