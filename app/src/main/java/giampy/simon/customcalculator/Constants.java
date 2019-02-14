package giampy.simon.customcalculator;

import java.math.BigDecimal;

public class Constants {

    public boolean enabledThousands = true;
    public String pattern;
    public String point;
    public char cPoint;
    public String separator = "";
    public char cSeparator;

    public static final BigDecimal e = new BigDecimal(2.71828182845904523536028747135266249775724709369995);

    public Constants(boolean enables, String thouStyle) {
        if (!enables) {
            enabledThousands = false;
            point = ".";
            cPoint = point.charAt(0);
            pattern =  "###.#############";
        } else {
            switch (thouStyle) {
                case "US":
                    point = ".";
                    separator = ",";
                    break;
                case "FR":
                    point = ",";
                    separator = " ";
                    break;
                default:
                    point = ",";
                    separator = ".";
                    break;
            }

            cPoint = point.charAt(0);
            cSeparator = separator.charAt(0);
            pattern =  "###,###.#############";
        }
    }

    public String getPoint() {
        return point;
    }

    public char getcPoint() {
        return cPoint;
    }

    public String getSeparator() {
        return separator;
    }

    public char getcSeparator() {
        return cSeparator;
    }

    public boolean isEnabledThousands() {
        return enabledThousands;
    }

    public String getPattern() {
        return pattern;
    }
}

