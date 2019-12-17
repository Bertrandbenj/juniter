package juniter.user;

import juniter.core.crypto.SecretBox;
import lombok.Data;

import com.google.common.collect.Lists;
import java.util.List;

@Data
public class UserSettings {


    private SecretBox nodeKey;

    private List<String> wallets = Lists.newArrayList(
            "4weakHxDBMJG9NShULG1g786eeGh7wwntMeLZBDhJFni",
            "77UVGVmbBLyh5gM51X8tbMtQSvnMwps2toB67qHn32aC",
            "3LJRrLQCio4GL7Xd48ydnYuuaeWAgqX4qXYFbXDTJpAa");

    private List<String> bookmarks = Lists.newArrayList(
            "https://g1-monit.librelois.fr/",
            "https://g1.le-sou.org/#/app/currency/lg",
            "https://forum.monnaie-libre.fr/",
            "https://duniter.normandie-libre.fr/wotmap/");

    private String LANG;

    private UnitDisplay unit;

}
