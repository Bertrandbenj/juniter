package juniter.user;

import juniter.core.crypto.SecretBox;
import lombok.Data;

import java.util.List;

@Data
public class UserSettings {

    enum UnitDisplay{
        RELATIVE, QUANTITATIVE
    }

    private SecretBox nodeKey;

    private List<String> wallets;

    private String LANG;

    private UnitDisplay unit;


}
