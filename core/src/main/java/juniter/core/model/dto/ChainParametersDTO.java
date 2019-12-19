package juniter.core.model.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.technical.DUPComponent;
import lombok.Data;

/**
 * DUPComponent ChainParameters initialized using ğ1 variable
 *
 * @see <a href="https://git.duniter.org/nodes/typescript/duniter/blob/dev/doc/Protocol.md#protocol-parameters"></a>
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChainParametersDTO implements DUPComponent {


    private String currency = "g1";
    /**
     * The %growth of the UD every [dt] period
     */
    private double c = 0.0488; // growth rate

    /**
     * Time period between two UD.
     */
    private long dt = 86400; // 1 day

    /**
     * UD(0), i.e. initial Universal Dividend
     */
    private long ud0 = 1000; // 10.00 g1

    /**
     * Minimum delay between 2 certifications of a same issuer, in seconds. Must be positive or zero.
     */
    private long sigPeriod = 432000; // 5 days

    /**
     * Maximum quantity of active certifications made by member.
     */
    private long sigStock = 100;

    /**
     * Maximum delay a cert can wait before being expired for non-writing.
     */
    private long sigWindow = 5259600; // 60 days, 21 hours

    /**
     * Maximum age of an active signature (in seconds)
     */
    private long sigValidity = 63115200; // 730 days, 12 hours,

    /**
     * Minimum quantity of signatures to be part of the WoT
     */
    private long sigQty = 5;

    /**
     * Maximum delay an identity can wait before being expired for non-writing.
     */
    private long idtyWindow = 5259600; // 60 days, 21 hours

    /**
     * Maximum delay a membership can wait before being expired for non-writing.
     */
    private long msWindow = 5259600; // 60 days, 21 hours

    /**
     * Minimum percent of sentries to reach to match the distance rule
     */
    private double xpercent = 0.8;

    /**
     * Maximum age of an active membership (in seconds)
     */
    private long msValidity = 31557600; // 365 days, 6 hours,

    /**
     * Maximum distance between each WoT member and a newcomer
     */
    private long stepMax = 5;

    /**
     * Number of blocks used for calculating median time.
     */
    private long medianTimeBlocks = 24;

    /**
     * The average time for writing 1 node (wished time)
     */
    private long avgGenTime = 300;

    /**
     * The number of blocks required to evaluate again PoWMin value
     */
    private long dtDiffEval = 12;

    /**
     * The percent of previous issuers to reach for personalized difficulty
     */
    private double percentRot = 0.67;

    /**
     * Time of first UD.
     */
    private long udTime0 = 1488970800; // GMT: Wednesday, March 8, 2017 11:00:00 AM

    /**
     * Time of first reevaluation of the UD.
     */
    private long udReevalTime0 = 1490094000; // GMT: Tuesday, March 21, 2017 11:00:00 AM ??

    /**
     * Time period between two re-evaluation of the UD.
     */
    private long dtReeval = 15778800; // 182 days, 15 hours


    /**
     * Minimum delay between 2 memberships of a same issuer, in seconds. Must be positive or zero.
     *
     * @apiNote New parameter
     */
    public long msPeriod = msWindow;


    /**
     * Minimum delay between 2 certifications of a same issuer to a same receiver, in seconds. Equals to msPeriod.Minimum delay between 2 certifications of a same issuer to a same receiver, in seconds. Equals to msPeriod.
     *
     * @apiNote New parameter
     */
    public long sigReplay = msWindow;


    /**
     * <p>
     * c:dt:ud0:sigPeriod:sigStock:sigWindow:sigValidity:sigQty:idtyWindow:msWindow:xpercent:msValidity:stepMax:medianTimeBlocks:avgGenTime:dtDiffEval:percentRot:udTime0:udReevalTime0:dtReeval
     * <p>
     * 0.0488:86400:1000:432000:100:5259600:63115200:5:5259600:5259600:0.8:31557600:5:24:300:12:0.67:1488970800:1490094000:15778800:0.007376575:3600:1200:0:40:604800:31536000:1:604800:604800:0.9:31536000:3:20:960:10:0.6666666666666666:1489675722:1489675722:3600",
     *
     * @param rawParamString:
     */
    public void accept(String rawParamString) {
        final var params = rawParamString.split(":");
        c = Double.parseDouble(params[0]);
        dt = Long.parseLong(params[1]);
        ud0 = Long.parseLong(params[2]);
        sigPeriod = Long.parseLong(params[3]);
        sigStock = Long.parseLong(params[4]);
        sigWindow = Long.parseLong(params[5]);
        sigValidity = Long.parseLong(params[6]);
        sigQty = Long.parseLong(params[7]);
        idtyWindow = Long.parseLong(params[8]);
        msWindow = Long.parseLong(params[9]);
        xpercent = Double.parseDouble(params[10]);
        msValidity = Long.parseLong(params[11]);
        stepMax = Long.parseLong(params[12]);
        medianTimeBlocks = Long.parseLong(params[13]);
        avgGenTime = Long.parseLong(params[14]);
        dtDiffEval = Long.parseLong(params[15]);
        percentRot = Double.parseDouble(params[16]);
        udTime0 = Long.parseLong(params[17]);
        udReevalTime0 = Long.parseLong(params[18]);
        dtReeval = Long.parseLong(params[19]);

        // New parameter
        if (params.length > 20) {
            msPeriod = Long.parseLong(params[20]);
            sigReplay = Long.parseLong(params[21]);
        }

    }

    @Override
    public String toDUP(){
        return  c+":"+dt+":"+ud0+":"+sigPeriod+":"+sigStock+":"+sigWindow+":"+sigValidity+":"+sigQty+":"+idtyWindow+":"+msWindow+":"+xpercent+":"+msValidity+":"+stepMax+":"+medianTimeBlocks+":"+avgGenTime+":"+dtDiffEval+":"+percentRot+":"+udTime0+":"+udReevalTime0+":"+dtReeval;
    }

    public double maxAcceleration() {
        return Math.ceil(maxGenTime() * medianTimeBlocks);
    }

    private double maxGenTime() {
        return Math.ceil(avgGenTime * 1.189);
    }

    public double maxSpeed() {
        return 1 / minGenTime();
    }

    private double minGenTime() {
        return Math.floor(avgGenTime / 1.189);
    }

    public double minSpeed() {
        return 1 / maxGenTime();
    }

}