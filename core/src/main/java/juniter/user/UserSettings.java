package juniter.user;

import juniter.core.crypto.SecretBox;
import lombok.Data;

import java.util.List;

@Data
public class UserSettings {

    public enum UnitDisplay{
        RELATIVE, QUANTITATIVE
    }

    private SecretBox nodeKey;

    private List<String> wallets;

    private List<String> bookmarks;

    private String LANG;

    private UnitDisplay unit;

}
