package juniter.core.model.meta;

import juniter.core.model.dbo.tx.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface DUPOutCondition extends DUPComponent {
    Logger LOG = LogManager.getLogger(DUPOutCondition.class);


    static DUPOutCondition parse(String cond) throws Exception {

        DUPOutCondition res = null;

        // Simple cases SIG first because its the most frequent
        if ("SIG".equals(cond.substring(0, 3))) {
            res = new SIG(cond.substring(4, cond.length() - 1));
            LOG.debug("Parsed SIG " + cond);
        }
        if ("XHX".equals(cond.substring(0, 3))) {
            res = new XHX(cond.substring(4, cond.length() - 1));
            LOG.debug("Parsed XHX " + cond);
        }
        if ("CSV".equals(cond.substring(0, 3))) {
            res = new CSV(Long.valueOf(cond.substring(4, cond.length() - 1)));
            LOG.debug("Parsed CSV " + cond);
        }
        if ("CLTV".equals(cond.substring(0, 4))) {
            res = new CLTV(Long.valueOf(cond.substring(5, cond.length() - 1)));
            LOG.debug("Parsed CLTV " + cond);
        }

        // recursive cases
        if (cond.startsWith("(")) {
            final String subcond = cond.substring(1, cond.length() - 1);
            LOG.debug(" - subcond " + subcond + "\nwhen parsing " + cond);
            var i = 0;

            while ((i = subcond.indexOf("&&", i)) != -1) { // for each occurence of operator

                final var right = subcond.substring(i + 2).trim(); // take right
                final var left = subcond.substring(0, i).trim(); // take left
                LOG.info(" - tryAnd " + i + " " + left + " ___________ " + right);
                AND parsed = null;
                try {
                    parsed = new AND(parse(left), parse(right));
                    LOG.info("Parsed And " + parsed);

                    return parsed;
                } catch (final Exception e) {
                    i++;
                    LOG.error(e.getMessage());
                }
            }

            while ((i = subcond.indexOf("||", i)) != -1) { // for each occurence of operator

                final var right = subcond.substring(i + 2).trim(); // take right
                final var left = subcond.substring(0, i).trim(); // take left
                LOG.info(" - tryOr " + i + " " + left + " - " + right);

                OR parsed = null;
                try {
                    parsed = new OR(parse(left), parse(right));
                    LOG.info("Parsed Or " + parsed);

                    return parsed;
                } catch (final Exception e) {
                    i++;
                    LOG.error(e.getMessage());
                }

            }

        }
        if (res == null)
            throw new Exception("while Parsing " + cond);

        return res;
    }

    default String getCondition() {
        return toString();
    }

}
