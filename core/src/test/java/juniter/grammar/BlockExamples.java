package juniter.grammar;

//@formatter:off
public interface BlockExamples {

    String WRONG_SIGNATURE = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 50\n" +
            "PoWMin: 1\n" +
            "Time: 1411775999\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 2\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String VALID_ROOT = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: duniter_unit_test_currency\n" +
            "Number: 0\n" +
            "PoWMin: 0\n" +
            "Time: 1483614905\n" +
            "MedianTime: 1483614905\n" +
            "UnitBase: 0\n" +
            "Issuer: DNann1Lh55eZMEDXeYt59bzHbA3NJR46DeQYCS2qQdLV\n" +
            "IssuersFrame: 1\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 0\n" +
            "Parameters: 0.007376575:3600:120:0:40:604800:31536000:1:604800:604800:0.9:31536000:3:20:960:10:0.6666666666666666:1483614905:1483614905:100\n"
            +
            "MembersCount: 2\n" +
            "Identities:\n" +
            "DNann1Lh55eZMEDXeYt59bzHbA3NJR46DeQYCS2qQdLV:1eubHHbuNfilHMM0G2bI30iZzebQ2cQ1PC7uPAw08FGMMmQCRerlF/3pc4sAcsnexsxBseA/3lY03KlONqJBAg==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tic\n"
            +
            "DKpQPUL4ckzXYdnDRvCRKAm1gNvSdmAXnTrJZ7LvM5Qo:lcekuS0eP2dpFL99imJcwvDAwx49diiDMkG8Lj7FLkC/6IJ0tgNjUzCIZgMGi7bL5tODRiWi9B49UMXb8b3MAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "DNann1Lh55eZMEDXeYt59bzHbA3NJR46DeQYCS2qQdLV:s2hUbokkibTAWGEwErw6hyXSWlWFQ2UWs2PWx8d/kkElAyuuWaQq4Tsonuweh1xn4AC1TVWt4yMR3WrDdkhnAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tic\n"
            +
            "DKpQPUL4ckzXYdnDRvCRKAm1gNvSdmAXnTrJZ7LvM5Qo:80pUx9YBk0RwqrVrQQA+PuxoNn21A8NwQ3824CQPU1ad9R1oDXc/pU6NVpQv92LM8gaWs/Pm1mLXNNVnr+m6BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "DKpQPUL4ckzXYdnDRvCRKAm1gNvSdmAXnTrJZ7LvM5Qo:DNann1Lh55eZMEDXeYt59bzHbA3NJR46DeQYCS2qQdLV:0:vMaYgBSnU+83AYOVQCZAx1XLpg/F1MmMztDfCnZvl8hPs4LE9tcDvCrrFogAwMEW2N7Y0gCH62/fBMgw4KrGCA==\n"
            +
            "DNann1Lh55eZMEDXeYt59bzHbA3NJR46DeQYCS2qQdLV:DKpQPUL4ckzXYdnDRvCRKAm1gNvSdmAXnTrJZ7LvM5Qo:0:RKIGMgYIhB9FmjPbmyo4egPufg/iTpBznYGZp5hjK1WZ1a9imQldLNUMe0eiPlSKJTK/JD3gOlCiynOEY2csBA==\n"
            +
            "Transactions:\n" +
            "InnerHash: 7A4E76A9A3410594AC9AED94B14AD9892426D76EDAF735CFE6C66432E422A63F\n" +
            "Nonce: 300000000001\n" +
            "WJourHkd6NnMxKDSEfrsiB7qE0mGbiFHSwy0cE8/q/is6hTd0mzlMNBPxDhoPkAiocfXJrQuIVeG0/ygxQrTBw==\n";

    String WRONG_PROOF_OF_WORK = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 1\n" +
            "PoWMin: 17\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 97E7DBF5D3AC27A4AFB797365304B7922917B6ADCDEFD736F20E38ADE982A47D\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String ROOT_WITHOUT_PARAMETERS = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 0\n" +
            "Time: 1411775999\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 1\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String NON_ROOT_WITH_PARAMETERS = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 5\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "Parameters: 0.7376575:100:157680000:0:40:7200:31536000:1:1000:1000:0.9:31536000:3:1:60:10:0.67:1511776000:1511776000:1511776000\n"
            +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String ROOT_WITH_PREVIOUS_HASH = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855\n" +
            "MembersCount: 0\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String ROOT_WITH_PREVIOUS_ISSUER = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String NON_ROOT_WITHOUT_PREVIOUS_HASH = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 1\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String NON_ROOT_WITHOUT_PREVIOUS_ISSUER = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 1\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 97E7DBF5D3AC27A4AFB797365304B7922917B6ADCDEFD736F20E38ADE982A47D\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String COLLIDING_UIDS = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:NJE8nYU4Im+KQDRdoAn5gcfic+Gjjzp0Pp0iji/Fzh9JIThoQeUDDew4Q5vJBEg/Aw7gPnIg+11TbLkIGa/ODQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String COLLIDING_PUBKEYS = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:NJE8nYU4Im+KQDRdoAn5gcfic+Gjjzp0Pp0iji/Fzh9JIThoQeUDDew4Q5vJBEg/Aw7gPnIg+11TbLkIGa/ODQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_DATE_LOWER = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 50\n" +
            "PoWMin: 1\n" +
            "Time: 1411775999\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 2\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_DATE_HIGHER_BUT_TOO_FEW = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 10\n" +
            "PoWMin: 1\n" +
            "Time: 1411776059\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_DATE_HIGHER_BUT_TOO_HIGH = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 10\n" +
            "PoWMin: 1\n" +
            "Time: 1411785481\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 2\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_ROOT_TIMES = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776001\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String GOOD_DATE_HIGHER = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 20\n" +
            "PoWMin: 1\n" +
            "Time: 1411776060\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_IDTY_MATCH_JOINS = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String MULTIPLE_JOINERS = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 1\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String MULTIPLE_ACTIVES = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 1\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String MULTIPLE_LEAVES = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String MULTIPLE_EXCLUDED = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "Certifications:\n" +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String MULTIPLE_OVER_ALL = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "Certifications:\n" +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String MULTIPLE_CERTIFICATIONS_FROM_SAME_ISSUER = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 2\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String IDENTICAL_CERTIFICATIONS = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String LEAVER_WITH_CERTIFICATIONS = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:I9kYCknNTp9zYS4CflAMk4Xh1yQIaP1H3PgPHJeQZQNkBavyqddXsq5DUzscsi2kRttJw6C/MATSD8KyZYPNAg==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String EXCLUDED_WITH_CERTIFICATIONS = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";


    String WRONGLY_SIGNED_IDENTITIES = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 52DC8A585C5D89571C511BB83F7E7D3382F0041452064B1272E65F0B42B82D57\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:Die9lYNW1u/w50AfuaXwb4MJc3aKA3WfJwiy531TqHIGC+VNnRKjMmrwMptN+a+dL6INjLrhMrPqoK60IkTlDQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONGLY_SIGNED_JOIN = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV3Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONGLY_SIGNED_ACTIVE = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV3Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONGLY_SIGNED_LEAVE = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:I9kYCknNTp9zYS4CflAMk4Xh1yQIaP1H3PgPHJeQZQNkBavyqddXsq5DUzscsi2kRttJw6C/MATSD8KyZYPNAg==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String CORRECTLY_SIGNED_LEAVE = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:I9kYCknNTp9zYS4CflAMk4Xh1yQIaP1H3PgPHJeQZQNkBavyqddXsq5DUzscsi2kRttJw6C/MATSD8KyZYPNAg==:1411850496\n"
            +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONGLY_SIGNED_CERTIFICATION = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 29890\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:vTvKYvjTYUT30t/9h7uNE/2LFJiYuA4YleIetFkb62XxDoxGizKC9VvVs7WRNArcfHvJ+RLyOoawQzpmw2DyCw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String UNKNOWN_CERTIFIER = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:vTvKYvjTYUT30t/9h7uNE/2LFJiYuA4YleIetFkb62XxDoxGizKC9VvVs7WRNArcfHvJ+RLyOoawQzpmw2DyCw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String UNKNOWN_CERTIFIED = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC:1411844654:vTvKYvjTYUT30t/9h7uNE/2LFJiYuA4YleIetFkb62XxDoxGizKC9VvVs7WRNArcfHvJ+RLyOoawQzpmw2DyCw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String EXISTING_UID = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:Die9lYNW1u/w50AfuaXwb4MJc3aKA3WfJwiy+31TqHIGC+VNnRKjMmrwMptN+a+dL6INjLrhMrPqoK60IkTlDQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:EXISTING\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String EXISTING_PUBKEY = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH:Die9lYNW1u/w50AfuaXwb4MJc3aKA3WfJwiy+31TqHIGC+VNnRKjMmrwMptN+a+dL6INjLrhMrPqoK60IkTlDQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String TOO_EARLY_CERTIFICATION_REPLAY = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC:Die9lYNW1u/w50AfuaXwb4MJc3aKA3WfJwiy+31TqHIGC+VNnRKjMmrwMptN+a+dL6INjLrhMrPqoK60IkTlDQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC:1411844654:vTvKYvjTYUT30t/9h7uNE/2LFJiYuA4YleIetFkb62XxDoxGizKC9VvVs7WRNArcfHvJ+RLyOoawQzpmw2DyCw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC:1411844658:2KmmmIL8eK/TACjOqTqO5ZG/tgMYWWV8zRICWFQJuqWyYVg/y5wzXyHrgfpdMYhwYMRBhwbMk1sPNLo/kzp0AA==\n"
            +
            "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:DU4JlHxJtIb2Z7Ag4Jy+z0qjNNo5jzN5EvTUWOTRRzeb6LbOClw2X+pmb0mV/wpVKd/lJrUHAWeKMDHG4MukCA==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:kr2JA6wCGfbNKGpyM86BscsFk22aA9oiAon8mWRPl4G8UpJKZs3tjuPRAw5+04KLCRWl/TT1TumDCkeEjev7DA==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String EXPIRED_CERTIFICATIONS = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 90\n" +
            "PoWMin: 1\n" +
            "Time: 1443333600\n" +
            "MedianTime: 1443333600\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "Joiners:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:70:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String EXPIRED_MEMBERSHIP = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 90\n" +
            "PoWMin: 1\n" +
            "Time: 1443333600\n" +
            "MedianTime: 1443333600\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "Joiners:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:70-5918CE7F40186F8E0BD8F239986A723FCC329927B999885B32DAAE40EA8BEDB6:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String REVOKED_JOINER = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 113\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 52DC8A585C5D89571C511BB83F7E7D3382F0041452064B1272E65F0B42B82D57\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String NOT_ENOUGH_CERTIFICATIONS_JOINER = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 3\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 52DC8A585C5D89571C511BB83F7E7D3382F0041452064B1272E65F0B42B82D57\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String NOT_ENOUGH_CERTIFICATIONS_JOINER_BLOCK_0 = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String OUTDISTANCED_JOINER = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 3\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 52DC8A585C5D89571C511BB83F7E7D3382F0041452064B1272E65F0B42B82D57\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-0C1C080CA81F6D763C6C42B7D043DF40DDABC2E27D47E0FE27DEF0E65D795280:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String VALID_NEXT = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 3\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "PreviousHash: 52DC8A585C5D89571C511BB83F7E7D3382F0041452064B1272E65F0B42B82D57\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 5\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_PREVIOUS_HASH = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 51\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 97E7DBF5D3AC27A4AFB797365304B7922917B6ADCDEFD736F20E38ADE982A47D\n" +
            "PreviousIssuer: G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_PREVIOUS_ISSUER = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 51\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 4C8800825C44A22F230AFC0D140BF1930331A686899D16EBE4C58C9F34C609E8\n" +
            "PreviousIssuer: G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_DIFFERENT_ISSUERS_COUNT_FOLLOWING_V2 = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 51\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 2\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 4C8800825C44A22F230AFC0D140BF1930331A686899D16EBE4C58C9F34C609E8\n" +
            "PreviousIssuer: G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_DIFFERENT_ISSUERS_COUNT_FOLLOWING_V3 = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 51\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 2\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 4C8800825C44A22F230AFC0D140BF1930331A686899D16EBE4C58C9F34C609E8\n" +
            "PreviousIssuer: G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_ISSUERS_FRAME_FOLLOWING_V2 = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 51\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 2\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 101\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 4C8800825C44A22F230AFC0D140BF1930331A686899D16EBE4C58C9F34C609E8\n" +
            "PreviousIssuer: G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_ISSUERS_FRAME_FOLLOWING_V3 = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 51\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 2\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 101\n" +
            "IssuersFrameVar: 11\n" +
            "DifferentIssuersCount: 4\n" +
            "PreviousHash: 4C8800825C44A22F230AFC0D140BF1930331A686899D16EBE4C58C9F34C609E8\n" +
            "PreviousIssuer: G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";


    String WRONG_ISSUER = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 51\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 4C8800825C44A22F230AFC0D140BF1930331A686899D16EBE4C58C9F34C609E8\n" +
            "PreviousIssuer: G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_JOIN_BLOCK_TARGET_ROOT = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 51\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 4C8800825C44A22F230AFC0D140BF1930331A686899D16EBE4C58C9F34C609E8\n" +
            "PreviousIssuer: G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:44-9F118AC87B062F23F193761E081082BB661547CF9A0C243CD3994E99C78A74C3:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_JOIN_ROOT_NUMBER = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:44-9F118AC87B062F23F193761E081082BB661547CF9A0C243CD3994E99C78A74C3:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_JOIN_ROOT_HASH = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-9F118AC87B062F23F193761E081082BB661547CF9A0C243CD3994E99C78A74C3:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_JOIN_NUMBER_TOO_LOW = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 12\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:2-65DDE908DC06D42EC8AAAE4AB716C299ECD4891740349BCF50EF3D70C947CBE0:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_JOIN_BLOCK_TARGET = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 51\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 4C8800825C44A22F230AFC0D140BF1930331A686899D16EBE4C58C9F34C609E8\n" +
            "PreviousIssuer: G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:44-9F118AC87B062F23F193761E081082BB661547CF9A0C243CD3994E99C78A74C3:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_JOIN_ALREADY_MEMBER = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 51\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 4C8800825C44A22F230AFC0D140BF1930331A686899D16EBE4C58C9F34C609E8\n" +
            "PreviousIssuer: G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:3-65DDE908DC06D42EC8AAAE4AB716C299ECD4891740349BCF50EF3D70C947CBE0:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_ACTIVE_BLOCK_TARGET = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 51\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 4C8800825C44A22F230AFC0D140BF1930331A686899D16EBE4C58C9F34C609E8\n" +
            "PreviousIssuer: G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:44-9F118AC87B062F23F193761E081082BB661547CF9A0C243CD3994E99C78A74C3:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String KICKED_NOT_EXCLUDED = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 4\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 1\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String KICKED_EXCLUDED = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 4\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 1\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_MEMBERS_COUNT = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 4\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String NO_LEADING_ZERO = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 3\n" +
            "PoWMin: 8\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 52DC8A585C5D89571C511BB83F7E7D3382F0041452064B1272E65F0B42B82D57\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 10\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String REQUIRES_4_LEADING_ZEROS = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 60\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String REQUIRES_7_LEADING_ZEROS = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 61\n" +
            "PoWMin: 6\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 11\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String REQUIRES_6_LEADING_ZEROS = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 67\n" +
            "PoWMin: 8\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 4\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String REQUIRES_5_LEADING_ZEROS = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 63\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String REQUIRES_7_LEADING_ZEROS_AGAIN = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 64\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String FIRST_BLOCK_OF_NEWCOMER = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 65\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 2\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String SECOND_BLOCK_OF_NEWCOMER = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 66\n" +
            "PoWMin: 160\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: AbCCJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 10\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_ROOT_DATES = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776002\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_MEDIAN_TIME_ODD = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 101\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_MEDIAN_TIME_EVEN = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 102\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String GOOD_MEDIAN_TIME_ODD = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 103\n" +
            "PoWMin: 1\n" +
            "Time: 161\n" +
            "MedianTime: 161\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String GOOD_MEDIAN_TIME_EVEN = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 104\n" +
            "PoWMin: 1\n" +
            "Time: 162\n" +
            "MedianTime: 162\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String WRONG_CONFIRMED_DATE_MUST_CONFIRM = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 72\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String ROOT_BLOCK_WITH_UD = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:CTmlh3tO4B8f8IbL8iDy5ZEr3jZDcxkPmDmRPQY74C39MRLXi0CKUP+oFzTZPYmyUC7fZrUXrb3LwRKWw1jEBQ==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:s9DSBEAIbLi1HjBvXaHySApHo0HBkE9Ibxm10k/zUvmP49+FLg1IbXGj0JK6Y5SsywTcPWyQXseqXGSeGiIjAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:Te6R+OlWveuHKkegVDpg2hyse2ojBRfCAdCjMwtqMZx662qdgbMD9+xdZTXIjLRMtdsQ973EEZtwMc3hJkcdBw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:25xK7+ph7IYeN9Hu8PvuIBjYdVURYtvKayPHZg7zrrYTs6ii2fMtk5J65a3bT/NKr2Qsd7I5TCL29QyiAXa7BA==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:tac\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:ze+ftHWFLYmjfvXyrx4a15N2VQjf6oen8kkMiYNYrVllbpb5IUcb28CenlOQbVd9cZCNGSkTP7xP5bt8KAqUAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:toc\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:CK6UDDJM3d0weE1RVtzFJnw/+J507lPAtspleHc59T4+N1tzQj1RRGWrzPiTknCjnCO6SxBSJX0B+MIUWrpNAw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:0:a7SFapoVaXq27NU+wZj4afmxp0SbwLGqLJih8pfX6TRKPvNp/V93fbKixbqg10cwa1CadNenztxq3ZgOivqADw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:bJyoM2Tz4hltVXkLvYHOOmLP4qqh2fx7aMLkS5q0cMoEg5AFER3iETj13uoFyhz8yiAKESyAZSDjjQwp8A1QDw==\n"
            +
            "F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:0:h8D/dx/z5K2dx06ktp7fnmLRdxkdV5wRkJgnmEvKy2k55mM2RyREpHfD7t/1CC5Ew+UD0V9N27PfaoLxZc1KCQ==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:F5PtTpt8QFYMGtpZaETygB2C2yxCSxH1UW1VopBNZ6qg:0:eefk9Gg0Ijz0GvrNnRc55CCCBd4yk8j0fNzWzVZFKR3kZ7lsKav6dWyAsaVhlNG5S6XwEwvPoMwKJq1Vn7OjBg==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String UD_BLOCK_WIHTOUT_UD = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 80\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String UD_BLOCK_WIHTOUT_BASE = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 80\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String V3_ROOT_BLOCK_NOBASE = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String V3_ROOT_BLOCK_POSITIVE_BASE = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 1\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 101\n" +
            "IssuersFrameVar: 11\n" +
            "DifferentIssuersCount: 4\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String BLOCK_WITH_WRONG_UD = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 81\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String BLOCK_WITH_WRONG_UD_V3 = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 81\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UniversalDividend: 100\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 101\n" +
            "IssuersFrameVar: 11\n" +
            "DifferentIssuersCount: 4\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String BLOCK_WITH_WRONG_UNIT_BASE = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 160\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411778000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "UnitBase: 1\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String BLOCK_WITH_WRONG_UNIT_BASE_NO_UD = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 160\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 8\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 101\n" +
            "IssuersFrameVar: 11\n" +
            "DifferentIssuersCount: 4\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String BLOCK_UNLEGITIMATE_UD = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 82\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String BLOCK_UNLEGITIMATE_UD_2 = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 83\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String FIRST_UD_BLOCK_WITH_UD_THAT_SHOULDNT = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 20\n" +
            "PoWMin: 1\n" +
            "Time: 1411773099\n" +
            "MedianTime: 1411773099\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String FIRST_UD_BLOCK_WITH_UD_THAT_SHOULD = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 20\n" +
            "PoWMin: 1\n" +
            "Time: 1411773100\n" +
            "MedianTime: 1411773100\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String BLOCK_WITHOUT_TRANSACTIONS = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 83\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String BLOCK_WITH_GOOD_TRANSACTIONS = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 83\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:1:3:6:6:3:0:0\n" +
            "HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY\n" +
            "CYYjHsNyg3HMRMpTHqCJAN9McjH5BwFLmDKGV3PmCuKp\n" +
            "9WYHTavL1pmhunFCzUwiiq4pXwvgGG5ysjZnjz9H8yB\n" +
            "T:6991C993631BED4733972ED7538E41CCC33660F554E3C51963E2A0AC4D6453D3:4\n" +
            "T:3A09A20E9014110FD224889F13357BAB4EC78A72F95CA03394D8CCA2936A7435:10\n" +
            "D:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:46\n" +
            "T:A0D9B4CDC113ECE1145C5525873821398890AE842F4B318BD076095A23E70956:66\n" +
            "T:67F2045B5318777CC52CD38B424F3E40DDA823FA0364625F124BABE0030E7B5B:176\n" +
            "D:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:46\n" +
            "0:SIG(0)\n" +
            "1:SIG(0)\n" +
            "2:SIG(0)\n" +
            "3:SIG(0)\n" +
            "4:SIG(0)\n" +
            "5:SIG(0)\n" +
            "30:4:SIG(BYfWYFrsyjpvpFysgu19rGK3VHBkz4MqmQbNyEuVU64g)\n" +
            "156:4:SIG(DSz4rgncXCytsUMW2JU2yhLquZECD2XpEkpP9gG5HyAx)\n" +
            "49:4:SIG(6DyGr5LFtFmbaJYRvcs9WmBsr4cbJbJ1EV9zBbqG7A6i)\n" +
            "42yQm4hGTJYWkPg39hQAUgP6S6EQ4vTfXdJuxKEHL1ih6YHiDL2hcwrFgBHjXLRgxRhj2VNVqqc6b4JayKqTE14r\n" +
            "2D96KZwNUvVtcapQPq2mm7J9isFcDCfykwJpVEZwBc7tCgL4qPyu17BT5ePozAE9HS6Yvj51f62Mp4n9d9dkzJoX\n" +
            "2XiBDpuUdu6zCPWGzHXXy8c4ATSscfFQG9DjmqMZUxDZVt1Dp4m2N5oHYVUfoPdrU9SLk4qxi65RNrfCVnvQtQJk\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String BLOCK_WITH_WRONG_TRANSACTION_SUMS = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 83\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:1:3:6:6:3:0:0\n" +
            "HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY\n" +
            "CYYjHsNyg3HMRMpTHqCJAN9McjH5BwFLmDKGV3PmCuKp\n" +
            "9WYHTavL1pmhunFCzUwiiq4pXwvgGG5ysjZnjz9H8yB\n" +
            "T:6991C993631BED4733972ED7538E41CCC33660F554E3C51963E2A0AC4D6453D3:4\n" +
            "T:3A09A20E9014110FD224889F13357BAB4EC78A72F95CA03394D8CCA2936A7435:10\n" +
            "D:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:46\n" +
            "T:A0D9B4CDC113ECE1145C5525873821398890AE842F4B318BD076095A23E70956:66\n" +
            "T:67F2045B5318777CC52CD38B424F3E40DDA823FA0364625F124BABE0030E7B5B:176\n" +
            "D:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:46\n" +
            "0:SIG(0)\n" +
            "1:SIG(0)\n" +
            "2:SIG(0)\n" +
            "3:SIG(0)\n" +
            "4:SIG(0)\n" +
            "5:SIG(0)\n" +
            "30:4:SIG(BYfWYFrsyjpvpFysgu19rGK3VHBkz4MqmQbNyEuVU64g)\n" +
            "156:4:SIG(DSz4rgncXCytsUMW2JU2yhLquZECD2XpEkpP9gG5HyAx)\n" +
            "50:4:SIG(6DyGr5LFtFmbaJYRvcs9WmBsr4cbJbJ1EV9zBbqG7A6i)\n" +
            "42yQm4hGTJYWkPg39hQAUgP6S6EQ4vTfXdJuxKEHL1ih6YHiDL2hcwrFgBHjXLRgxRhj2VNVqqc6b4JayKqTE14r\n" +
            "2D96KZwNUvVtcapQPq2mm7J9isFcDCfykwJpVEZwBc7tCgL4qPyu17BT5ePozAE9HS6Yvj51f62Mp4n9d9dkzJoX\n" +
            "2XiBDpuUdu6zCPWGzHXXy8c4ATSscfFQG9DjmqMZUxDZVt1Dp4m2N5oHYVUfoPdrU9SLk4qxi65RNrfCVnvQtQJk\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String BLOCK_WITH_WRONG_TRANSACTION_UNIT_BASES = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 83\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:1:3:6:6:3:0:0\n" +
            "HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY\n" +
            "CYYjHsNyg3HMRMpTHqCJAN9McjH5BwFLmDKGV3PmCuKp\n" +
            "9WYHTavL1pmhunFCzUwiiq4pXwvgGG5ysjZnjz9H8yB\n" +
            "T:6991C993631BED4733972ED7538E41CCC33660F554E3C51963E2A0AC4D6453D3:4\n" +
            "T:3A09A20E9014110FD224889F13357BAB4EC78A72F95CA03394D8CCA2936A7435:10\n" +
            "D:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:46\n" +
            "T:A0D9B4CDC113ECE1145C5525873821398890AE842F4B318BD076095A23E70956:66\n" +
            "T:67F2045B5318777CC52CD38B424F3E40DDA823FA0364625F124BABE0030E7B5B:176\n" +
            "D:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:46\n" +
            "0:SIG(0)\n" +
            "1:SIG(0)\n" +
            "2:SIG(0)\n" +
            "3:SIG(0)\n" +
            "4:SIG(0)\n" +
            "5:SIG(0)\n" +
            "30:4:SIG(BYfWYFrsyjpvpFysgu19rGK3VHBkz4MqmQbNyEuVU64g)\n" +
            "156:3:SIG(DSz4rgncXCytsUMW2JU2yhLquZECD2XpEkpP9gG5HyAx)\n" +
            "49:4:SIG(6DyGr5LFtFmbaJYRvcs9WmBsr4cbJbJ1EV9zBbqG7A6i)\n" +
            "42yQm4hGTJYWkPg39hQAUgP6S6EQ4vTfXdJuxKEHL1ih6YHiDL2hcwrFgBHjXLRgxRhj2VNVqqc6b4JayKqTE14r\n" +
            "2D96KZwNUvVtcapQPq2mm7J9isFcDCfykwJpVEZwBc7tCgL4qPyu17BT5ePozAE9HS6Yvj51f62Mp4n9d9dkzJoX\n" +
            "2XiBDpuUdu6zCPWGzHXXy8c4ATSscfFQG9DjmqMZUxDZVt1Dp4m2N5oHYVUfoPdrU9SLk4qxi65RNrfCVnvQtQJk\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String BLOCK_WITH_WRONG_UD_SOURCE = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 83\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:1:1:1:1:0\n" +
            "HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY\n" +
            "D:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:33\n" +
            "40:0:SIG(BYfWYFrsyjpvpFysgu19rGK3VHBkz4MqmQbNyEuVU64g)\n" +
            "42yQm4hGTJYWkPg39hQAUgP6S6EQ4vTfXdJuxKEHL1ih6YHiDL2hcwrFgBHjXLRgxRhj2VNVqqc6b4JayKqTE14r\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String BLOCK_WITH_WRONG_TX_SOURCE = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 83\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:1:1:1:1:0\n" +
            "HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY\n" +
            "T:A0D9B4CDC113ECE1145C5525873821398890AE842F4B318BD076095A23E70956:44\n" +
            "40:0:SIG(BYfWYFrsyjpvpFysgu19rGK3VHBkz4MqmQbNyEuVU64g)\n" +
            "42yQm4hGTJYWkPg39hQAUgP6S6EQ4vTfXdJuxKEHL1ih6YHiDL2hcwrFgBHjXLRgxRhj2VNVqqc6b4JayKqTE14r\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String BLOCK_WITH_UNAVAILABLE_UD_SOURCE = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 83\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:1:1:1:1:1:0:0\n" +
            "HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY\n" +
            "D:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:55\n" +
            "0:SIG(0)\n" +
            "40:0:SIG(BYfWYFrsyjpvpFysgu19rGK3VHBkz4MqmQbNyEuVU64g)\n" +
            "42yQm4hGTJYWkPg39hQAUgP6S6EQ4vTfXdJuxKEHL1ih6YHiDL2hcwrFgBHjXLRgxRhj2VNVqqc6b4JayKqTE14r\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String BLOCK_WITH_UNAVAILABLE_TX_SOURCE = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 83\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:1:1:1:1:1:0:0\n" +
            "HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY\n" +
            "T:2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326:88\n" +
            "0:SIG(0)\n" +
            "40:0:SIG(BYfWYFrsyjpvpFysgu19rGK3VHBkz4MqmQbNyEuVU64g)\n" +
            "42yQm4hGTJYWkPg39hQAUgP6S6EQ4vTfXdJuxKEHL1ih6YHiDL2hcwrFgBHjXLRgxRhj2VNVqqc6b4JayKqTE14r\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String TRANSACTION_WITHOUT_ISSUERS = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 83\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:10:0:2:2:1:0:0\n" +
            "3-2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "4500:3:T:2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326:88\n" +
            "4500:3:T:2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326:88\n" +
            "0:SIG(0)\n" +
            "1:SIG(0)\n" +
            "40:0:SIG(BYfWYFrsyjpvpFysgu19rGK3VHBkz4MqmQbNyEuVU64g)\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String TRANSACTION_WITHOUT_SOURCES = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 83\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:10:2:0:0:1:0:0\n" +
            "3-2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "40:0:SIG(BYfWYFrsyjpvpFysgu19rGK3VHBkz4MqmQbNyEuVU64g)\n" +
            "42yQm4hGTJYWkPg39hQAUgP6S6EQ4vTfXdJuxKEHL1ih6YHiDL2hcwrFgBHjXLRgxRhj2VNVqqc6b4JayKqTE14r\n" +
            "I6gJkJIQJ9vwDRXZ6kdBsOArQ3zzMYPmFxDbJqseBVq5NWlmJ7l7oY9iWtqhPF38rp7/iitbgyftsRR8djOGDg==\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String TRANSACTION_WITHOUT_RECIPIENT = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 83\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:10:2:2:2:0:0:0\n" +
            "3-2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "300:3:T:2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326:88\n" +
            "300:3:T:2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326:88\n" +
            "0:SIG(0)\n" +
            "1:SIG(0)\n" +
            "42yQm4hGTJYWkPg39hQAUgP6S6EQ4vTfXdJuxKEHL1ih6YHiDL2hcwrFgBHjXLRgxRhj2VNVqqc6b4JayKqTE14r\n" +
            "I6gJkJIQJ9vwDRXZ6kdBsOArQ3zzMYPmFxDbJqseBVq5NWlmJ7l7oY9iWtqhPF38rp7/iitbgyftsRR8djOGDg==\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String TRANSACTION_WITH_DUPLICATED_SOURCE_SINGLE_TX = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 83\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:10:2:3:3:1:0:0\n" +
            "3-2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "400:5:T:2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326:88\n" +
            "200:4:T:2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326:88\n" +
            "200:4:T:2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326:88\n" +
            "0:SIG(0)\n" +
            "1:SIG(0)\n" +
            "2:SIG(0)\n" +
            "120:0:SIG(BYfWYFrsyjpvpFysgu19rGK3VHBkz4MqmQbNyEuVU64g)\n" +
            "42yQm4hGTJYWkPg39hQAUgP6S6EQ4vTfXdJuxKEHL1ih6YHiDL2hcwrFgBHjXLRgxRhj2VNVqqc6b4JayKqTE14r\n" +
            "I6gJkJIQJ9vwDRXZ6kdBsOArQ3zzMYPmFxDbJqseBVq5NWlmJ7l7oY9iWtqhPF38rp7/iitbgyftsRR8djOGDg==\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String TRANSACTION_WITH_EMPTY_TX_CONDITIONS = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 83\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:10:2:2:2:2:0:0\n" +
            "3-2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "200:1:T:2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326:88\n" +
            "200:1:T:2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326:88\n" +
            "0:SIG(0)\n" +
            "1:SIG(0)\n" +
            "40:0:()\n" +
            "40:0:  \n" +
            "42yQm4hGTJYWkPg39hQAUgP6S6EQ4vTfXdJuxKEHL1ih6YHiDL2hcwrFgBHjXLRgxRhj2VNVqqc6b4JayKqTE14r\n" +
            "I6gJkJIQJ9vwDRXZ6kdBsOArQ3zzMYPmFxDbJqseBVq5NWlmJ7l7oY9iWtqhPF38rp7/iitbgyftsRR8djOGDg==\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String TRANSACTION_WRONG_TOTAL = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 83\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 101\n" +
            "IssuersFrameVar: 11\n" +
            "DifferentIssuersCount: 4\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:10:2:2:2:2:0:0\n" +
            "5-2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326\n" +
            "HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "2:2:T:2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326:88\n" +
            "1:2:T:2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326:88\n" +
            "0:SIG(0)\n" +
            "1:SIG(0)\n" +
            "40:2:SIG(BYfWYFrsyjpvpFysgu19rGK3VHBkz4MqmQbNyEuVU64g)\n" +
            "70:2:SIG(BYfWYFrsyjpvpFysgu19rGK3VHBkz4MqmQbNyEuVU64g)\n" +
            "42yQm4hGTJYWkPg39hQAUgP6S6EQ4vTfXdJuxKEHL1ih6YHiDL2hcwrFgBHjXLRgxRhj2VNVqqc6b4JayKqTE14r\n" +
            "I6gJkJIQJ9vwDRXZ6kdBsOArQ3zzMYPmFxDbJqseBVq5NWlmJ7l7oY9iWtqhPF38rp7/iitbgyftsRR8djOGDg==\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String TRANSACTION_V3_GOOD_AMOUNTS = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 83\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 101\n" +
            "IssuersFrameVar: 11\n" +
            "DifferentIssuersCount: 4\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:10:1:3:3:17:1:0\n" +
            "33089-00004C8D3A7EAA34ADD20E36268F7A141A45B8D47C0872EFDB00187810E0BBFD\n" +
            "TENGx7WtzFsTXwnbrPEvb6odX2WnqYcnnrjiiLvp1mS\n" +
            "17602605:0:T:E4E8486E20D521AAB329376431BD633A59FE847EF533C38CDD2C9F6E820FF786:14\n" +
            "800:1:T:E9BDCC49762D17757B1AA8C52744CD182DF3FFC8EA9B237267A1B3B33ABE089B:0\n" +
            "1897916:1:T:04A9E034116E436E581C01B9B58150A38615C06D12AE434614D5A08F6B409B1C:0\n" +
            "0:SIG(0)\n" +
            "1:SIG(0)\n" +
            "2:SIG(0)\n" +
            "5:0:SIG(TENGx7WtzFsTXwnbrPEvb6odX2WnqYcnnrjiiLvp1mS)\n" +
            "6:1:SIG(TENGx7WtzFsTXwnbrPEvb6odX2WnqYcnnrjiiLvp1mS)\n" +
            "7:2:SIG(TENGx7WtzFsTXwnbrPEvb6odX2WnqYcnnrjiiLvp1mS)\n" +
            "60:3:SIG(DesHja7gonANRJB7YUkfCgQpnDjgGeDXAeArdhcbXPmJ)\n" +
            "170:3:SIG(HGYV5C16mrdvE9vpb1S9nMDHkVPsubBgANs9pSb6HWCV)\n" +
            "210:3:SIG(HxWwfnVXpfec957STN97q6cMSzefkbXdApRTwVuUFUyc)\n" +
            "110:3:SIG(9bZEATXBGPUSsk8oAYi4KAChg3rHKwNt67hVdErbNGCW)\n" +
            "200:3:SIG(8Fi1VSTbjkXguwThF4v2ZxC5whK7pwG2vcGTkPUPjPGU)\n" +
            "100:3:SIG(2eijdARyo6FuTVX6kZPbVziRB8wq87sGXpCth9eaUNX4)\n" +
            "160:3:SIG(HnFcSms8jzwngtVomTTnzudZx7SHUQY8sVE1y8yBmULk)\n" +
            "90:3:SIG(8KTEFQS78HwEz1NK627rNsYwENxNXJyvtyMAyfKPXZRB)\n" +
            "180:3:SIG(XeBpJwRLkF5J4mnwyEDriEcNB13iFpe1MAKR4mH3fzN)\n" +
            "130:3:SIG(5ocqzyDMMWf1V8bsoNhWb1iNwax1e9M7VTUN6navs8of)\n" +
            "130:3:SIG(4eDissnRvTq7JSgbmnL62ogQcrCLzw2zdrxA4q1yuczs)\n" +
            "100:3:SIG(2pyPsXM8UCB88jP2NRM4rUHxb63qm89JMEWbpoRrhyDK)\n" +
            "40:3:SIG(6KXBjAFceD1gp8RBVZfy5YQyKFXG8GaX8tKaLAyPWHrj)\n" +
            "34909:3:SIG(TENGx7WtzFsTXwnbrPEvb6odX2WnqYcnnrjiiLvp1mS)\n" +
            "REMU:22684:22851\n" +
            "Iu9NripkuUAeYhhUyeiW9PSTkYFu4kM8ZEtFUgfh44qsuF5PaM38zy3/IszWtJ1i6HGQsbrxDiOVem6Gu+tyDA==\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String TRANSACTION_TOO_LONG = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: test_net\n" +
            "Number: 33520\n" +
            "PoWMin: 77\n" +
            "Time: 1472124036\n" +
            "MedianTime: 1472124036\n" +
            "UnitBase: 3\n" +
            "Issuer: HnFcSms8jzwngtVomTTnzudZx7SHUQY8sVE1y8yBmULk\n" +
            "IssuersFrame: 52\n" +
            "IssuersFrameVar: -2\n" +
            "DifferentIssuersCount: 9\n" +
            "PreviousHash: 00001E78E26DD813024EEFBC8F5C50BBA8B30DD1DB5C4F492D9606EE12C06BB4\n" +
            "PreviousIssuer: 5ocqzyDMMWf1V8bsoNhWb1iNwax1e9M7VTUN6navs8of\n" +
            "MembersCount: 128\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:10:1:48:48:2:0:0\n" +
            "33518-00001D9E2EA1F967667528817FAF17ECBACE6150EBB875A2196B4457C0366D2A\n" +
            "HnFcSms8jzwngtVomTTnzudZx7SHUQY8sVE1y8yBmULk\n" +
            "60:3:T:2A74F44780925586EA4C01BEE9DA5042FE8BDB0FB0506B9D69990750B555AC91:15\n" +
            "1829442:3:T:FAC74B122B951F99C28B75587CFE002DC8340B053281050747D480E1F9E9193D:1\n" +
            "1000:3:T:0C4C6D1321698DA41084A84C1DCD34769EB1C744933885CA3BF4A73BACB5C348:7\n" +
            "1500:3:T:060BDC4B0E1D9BB01BF91F5082700C51BBDB9BDA1051BD13F6DCF1F6C5064664:8\n" +
            "6000:3:T:B514D9CAF277F4DBE98B8ECC61014DB1609BA924903A884ABF969FFBB1E1C3F6:8\n" +
            "4500:3:T:B4A735DE1C5860C3B02AD68420BC5949B26B905F83C2E6E1F88D7D4490350C6F:8\n" +
            "4500:3:T:A2407DBDA4F687DB4B6CE5020EB64DD8F6CA71D761FB80F0D379B9E574CF134E:5\n" +
            "5750:3:T:120AA878EEB49D33C64C87661BADBE154B5ECAAC05502C478AC79F4E465E4EBC:3\n" +
            "16805:3:T:0562B378C254D538E4B73B928AD25CD3EACE2C2C8D662D1D48AF4B7860620B4D:0\n" +
            "7250:3:T:C0D99A2E3271606CB99384DD881CF2DE5F36180728464A4B88B33A0AFDDCA587:9\n" +
            "9750:3:T:180E64F8C76416455B4B34029F05E3C87D1F0E65AF7128358F7A6D874AFAA492:6\n" +
            "14000:3:T:CFBA4FB960BBDB9DD56BEE0ABCF68C04F71A51910F148FC219A72D2DD5F24F58:3\n" +
            "6750:3:T:570BD788DE59A5A99564026FAAEF7F2F1A398E858FE745D3A0ABE7C71040A6A3:4\n" +
            "6250:3:T:6ABBA329813201CBA66D0A8A682BE7986F1F80918FF2A5809B864EA0E134ABA4:3\n" +
            "7500:3:T:3B34FC6AD7543CE3671E6E302576A92A6B97BE1C15DEA1DBAB2D327A2911306B:3\n" +
            "5000:3:T:443B53D39DC3551CB3E49A1AE856E115F0571C154C432094D472D1C70BA368A8:8\n" +
            "6750:3:T:BDA9E8104D8458C65628A647086A3331C724E895E0EF24B9BB3B6144B27AC554:5\n" +
            "8250:3:T:A67E6D09AD60339255297A05ECA04E3E20AB290AB9CA1A5DE787BD67FE64F544:4\n" +
            "7250:3:T:DD2C145C4F2940EC1FAF1CAC830A52F01F0AA0A3AB9D88DD075BFEAEEBDAC27A:3\n" +
            "6000:3:T:2B1B033D6E56C67C251C24F11941BAE1D3CBE7B71FB9E910018461CD9E4C6C65:6\n" +
            "5500:3:T:75B5FC3419CEC4553FEBED80437CC8816041757932DC9803FF061E77B77A86F6:8\n" +
            "8000:3:T:1DBC2228962C9154AD37993F67F497FC51B38768A4DCCD8872B8DF9B8AB7A5D8:7\n" +
            "5750:3:T:D3E329ED2003A86876EBAC64F8727459D9F9478A5E4D614BA439786D49CFE51E:5\n" +
            "7250:3:T:956457195A5C5700CF8F7A89597F90A5A60F3124E3F4ED9D6D4D58DB1B7E8C3B:6\n" +
            "6000:3:T:EA45AFC115379BF663DA6BB517409ACC3D45E66291FF7D1767CA1030334A124B:5\n" +
            "4750:3:T:A7664F399173A3EC98519DC5F1428416ED962FFEF7AA44075F0AAAA64A6E5880:5\n" +
            "5750:3:T:D25272F1D778B52798B7A51CF0CE21F7C5812F841374508F4367872D4A47F0F7:3\n" +
            "4750:3:T:1556A77A6A1019484770F5AE2905EA58A7D9B434552B85772AE6EF1C96C138C6:7\n" +
            "4000:3:T:C9E4360176A5908DFB1F550D8295FC74DA062851ED087FE3F24275BC9E7D1E82:8\n" +
            "4000:3:T:C519A4D03F4725EF97B2D9EB88D13C3AAB4F1F0F30B84D6D8B4E8849506BAF4D:8\n" +
            "4500:3:T:D4B87D5FF3A517C622C75662F0D2746CD94AAC6365FAA3D41670B0320FA7F89E:5\n" +
            "5500:3:T:5918DE9FB72A329F20684F12E69BC3FE01CEE638A9FA7E3080F5FA025FA8F480:4\n" +
            "5250:3:T:DC32942E881ED8221CAD5A4DD55C2CB672F827407773064B4E8D8E8327CBC578:3\n" +
            "6250:3:T:9E01B679E2B9BD4E06FA8787EDBEEB77EA99E73CD90CCD5011D24DE68BFFBD08:3\n" +
            "5250:3:T:FDF089AA99BA44E0A2C66F6389DD4BBDB2200DF07C993146305353A9BD290ADC:4\n" +
            "6250:3:T:392409C3FBC2F72C23841089A7A4258F2915D4B09C963EC309F49072DD686E89:4\n" +
            "6000:3:T:F3DC72E0BC3FE36F0C4055831D66ED13FA90875335D12648B8ABA777B0966EB1:4\n" +
            "5250:3:T:ADB9EC0BE8B20D33DAC2EADDE29C67F3931F23A3856855F42704C72360619027:6\n" +
            "5000:3:T:6D7C4808CD9D8158DBDD69FEED6C29E6743E8ED075FD1C8D1BDEC2456C63A910:9\n" +
            "6000:3:T:49500A1D9CAB37244C33F8EACFF449907790BC32D7323D748373B1D8F02C2FBC:3\n" +
            "10500:3:T:F4651339A9C1D29A78F06F30FD05819F4F9DF5C599867D4278170DDB9A22AA4E:6\n" +
            "8000:3:T:999C1E9631F1F5DB1EF5DBB7933C97A3D215FC7D12538C353D9740DF2A49D61E:4\n" +
            "6500:3:T:B7B629039D1B87D478C3008AB743F71FE69F40E373A8279A6BDD06460BC88803:6\n" +
            "5500:3:T:F51FC58E86630A00C8CE9E6EE633B6DAB2AB824C818F6A89DE2ABDFE82B2B07F:6\n" +
            "6500:3:T:0C8A317ED208529660DDA43B1688489C7EE6EE5470BC6EA24C683180D55B9B64:6\n" +
            "7500:3:T:DA6F7ABDE903366FD1BB92A025459F0D3A1C39D40D9AAF9DEA1015AAEBB112D3:5\n" +
            "5250:3:T:7197A1785D81F4AA3C179E86E2D4DA32D36999B49E8BCDAACE38B5B100AECAB8:6\n" +
            "2305438:3:T:0FC57133A2CA3A1413E2FC2606F5004A9A6F107DC6E72A3348B5C7A8039ED852:1\n" +
            "0:SIG(0)\n" +
            "1:SIG(0)\n" +
            "2:SIG(0)\n" +
            "3:SIG(0)\n" +
            "4:SIG(0)\n" +
            "5:SIG(0)\n" +
            "6:SIG(0)\n" +
            "7:SIG(0)\n" +
            "8:SIG(0)\n" +
            "9:SIG(0)\n" +
            "10:SIG(0)\n" +
            "11:SIG(0)\n" +
            "12:SIG(0)\n" +
            "13:SIG(0)\n" +
            "14:SIG(0)\n" +
            "15:SIG(0)\n" +
            "16:SIG(0)\n" +
            "17:SIG(0)\n" +
            "18:SIG(0)\n" +
            "19:SIG(0)\n" +
            "20:SIG(0)\n" +
            "21:SIG(0)\n" +
            "22:SIG(0)\n" +
            "23:SIG(0)\n" +
            "24:SIG(0)\n" +
            "25:SIG(0)\n" +
            "26:SIG(0)\n" +
            "27:SIG(0)\n" +
            "28:SIG(0)\n" +
            "29:SIG(0)\n" +
            "30:SIG(0)\n" +
            "31:SIG(0)\n" +
            "32:SIG(0)\n" +
            "33:SIG(0)\n" +
            "34:SIG(0)\n" +
            "35:SIG(0)\n" +
            "36:SIG(0)\n" +
            "37:SIG(0)\n" +
            "38:SIG(0)\n" +
            "39:SIG(0)\n" +
            "40:SIG(0)\n" +
            "41:SIG(0)\n" +
            "42:SIG(0)\n" +
            "43:SIG(0)\n" +
            "44:SIG(0)\n" +
            "45:SIG(0)\n" +
            "46:SIG(0)\n" +
            "47:SIG(0)\n" +
            "2706:3:SIG(TENGx7WtzFsTXwnbrPEvb6odX2WnqYcnnrjiiLvp1mS)\n" +
            "4417789:3:SIG(HnFcSms8jzwngtVomTTnzudZx7SHUQY8sVE1y8yBmULk)\n" +
            "ue7NjWPfPXxPNqvlGgr/K4jZjyHlUdYVl6dq+RXJ5RMuu8xBoo6bKhTKOyz/vXZz2clnwV/FqPDdDsWImxCmAg==\n" +
            "InnerHash: 1C9EF3BCCBB14DA060F172855D18F7D979FBA752E55BB288E253806E4980E5AC\n" +
            "Nonce: 0\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String OUTPUT_TOO_LONG = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: test_net\n" +
            "Number: 33520\n" +
            "PoWMin: 77\n" +
            "Time: 1472124036\n" +
            "MedianTime: 1472124036\n" +
            "UnitBase: 3\n" +
            "Issuer: HnFcSms8jzwngtVomTTnzudZx7SHUQY8sVE1y8yBmULk\n" +
            "IssuersFrame: 52\n" +
            "IssuersFrameVar: -2\n" +
            "DifferentIssuersCount: 9\n" +
            "PreviousHash: 00001E78E26DD813024EEFBC8F5C50BBA8B30DD1DB5C4F492D9606EE12C06BB4\n" +
            "PreviousIssuer: 5ocqzyDMMWf1V8bsoNhWb1iNwax1e9M7VTUN6navs8of\n" +
            "MembersCount: 128\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:10:1:1:1:1:0:0\n" +
            "33518-00001D9E2EA1F967667528817FAF17ECBACE6150EBB875A2196B4457C0366D2A\n" +
            "HnFcSms8jzwngtVomTTnzudZx7SHUQY8sVE1y8yBmULk\n" +
            "60:3:T:2A74F44780925586EA4C01BEE9DA5042FE8BDB0FB0506B9D69990750B555AC91:15\n" +
            "0:SIG(0)\n" +
            "60:3:(SIG(EA7Dsw39ShZg4SpURsrgMaMqrweJPUFPYHwZA8e92e3D) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4)|| XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4)  || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4) || XHX(03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4))\n"
            +
            "ue7NjWPfPXxPNqvlGgr/K4jZjyHlUdYVl6dq+RXJ5RMuu8xBoo6bKhTKOyz/vXZz2clnwV/FqPDdDsWImxCmAg==\n" +
            "InnerHash: 1C9EF3BCCBB14DA060F172855D18F7D979FBA752E55BB288E253806E4980E5AC\n" +
            "Nonce: 0\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String UNLOCK_TOO_LONG = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: test_net\n" +
            "Number: 33520\n" +
            "PoWMin: 77\n" +
            "Time: 1472124036\n" +
            "MedianTime: 1472124036\n" +
            "UnitBase: 3\n" +
            "Issuer: HnFcSms8jzwngtVomTTnzudZx7SHUQY8sVE1y8yBmULk\n" +
            "IssuersFrame: 52\n" +
            "IssuersFrameVar: -2\n" +
            "DifferentIssuersCount: 9\n" +
            "PreviousHash: 00001E78E26DD813024EEFBC8F5C50BBA8B30DD1DB5C4F492D9606EE12C06BB4\n" +
            "PreviousIssuer: 5ocqzyDMMWf1V8bsoNhWb1iNwax1e9M7VTUN6navs8of\n" +
            "MembersCount: 128\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:10:1:1:1:1:0:0\n" +
            "33518-00001D9E2EA1F967667528817FAF17ECBACE6150EBB875A2196B4457C0366D2A\n" +
            "HnFcSms8jzwngtVomTTnzudZx7SHUQY8sVE1y8yBmULk\n" +
            "60:3:T:2A74F44780925586EA4C01BEE9DA5042FE8BDB0FB0506B9D69990750B555AC91:15\n" +
            "0:SIG(0) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789) XHX(123456789)\n"
            +
            "60:3:SIG(EA7Dsw39ShZg4SpURsrgMaMqrweJPUFPYHwZA8e92e3D)\n" +
            "ue7NjWPfPXxPNqvlGgr/K4jZjyHlUdYVl6dq+RXJ5RMuu8xBoo6bKhTKOyz/vXZz2clnwV/FqPDdDsWImxCmAg==\n" +
            "InnerHash: 1C9EF3BCCBB14DA060F172855D18F7D979FBA752E55BB288E253806E4980E5AC\n" +
            "Nonce: 0\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String TRANSACTION_WRONG_TRANSFORM = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 83\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 101\n" +
            "IssuersFrameVar: 11\n" +
            "DifferentIssuersCount: 4\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:10:2:3:3:3:0:0\n" +
            "5-2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326\n" +
            "HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "1000:1:T:2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326:88\n" +
            "200:2:T:2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326:88\n" +
            "30:3:T:2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326:88\n" +
            "0:SIG(0)\n" +
            "1:SIG(0)\n" +
            "2:SIG(0)\n" +
            "900:1:SIG(BYfWYFrsyjpvpFysgu19rGK3VHBkz4MqmQbNyEuVU64g)\n" +
            "220:2:SIG(BYfWYFrsyjpvpFysgu19rGK3VHBkz4MqmQbNyEuVU64g)\n" +
            "29:3:SIG(BYfWYFrsyjpvpFysgu19rGK3VHBkz4MqmQbNyEuVU64g)\n" +
            "42yQm4hGTJYWkPg39hQAUgP6S6EQ4vTfXdJuxKEHL1ih6YHiDL2hcwrFgBHjXLRgxRhj2VNVqqc6b4JayKqTE14r\n" +
            "I6gJkJIQJ9vwDRXZ6kdBsOArQ3zzMYPmFxDbJqseBVq5NWlmJ7l7oY9iWtqhPF38rp7/iitbgyftsRR8djOGDg==\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String TRANSACTION_WRONG_TRANSFORM_LOW_BASE = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 83\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 101\n" +
            "IssuersFrameVar: 11\n" +
            "DifferentIssuersCount: 4\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:10:1:6:6:8:1:0\n" +
            "33753-0000054FC8AC7B450BA7D8BA7ED873FEDD5BF1E98D5D3B0DEE38DED55CB80CB3\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "150605:3:T:01B1AB40E7C1021712FF40D5605037C0ACEECA547BF519ABDCB6473A9F6BDF45:1\n" +
            "297705:3:D:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:33530\n" +
            "2244725:3:T:507CBE120DB654645B55431A9967789ACB7CD260EA962B839F1708834D1E5491:0\n" +
            "972091:2:D:G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:30324\n" +
            "3808457:2:T:657229C5433FB9FFE64BF2E795E79DA796E0B1AF536DC740ECC26CCBBE104C33:1\n" +
            "4:2:T:507CBE120DB654645B55431A9967789ACB7CD260EA962B839F1708834D1E5491:1\n" +
            "0:SIG(0)\n" +
            "1:SIG(0)\n" +
            "2:SIG(0)\n" +
            "3:SIG(0)\n" +
            "4:SIG(0)\n" +
            "5:SIG(0)\n" +
            "3171064:3:SIG(5ocqzyDMMWf1V8bsoNhWb1iNwax1e9M7VTUN6navs8of)\n" +
            "3:2:SIG(5ocqzyDMMWf1V8bsoNhWb1iNwax1e9M7VTUN6navs8of)\n" +
            "4:1:SIG(5ocqzyDMMWf1V8bsoNhWb1iNwax1e9M7VTUN6navs8of)\n" +
            "8:0:SIG(5ocqzyDMMWf1V8bsoNhWb1iNwax1e9M7VTUN6navs8of)\n" +
            "25:3:SIG(G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU)\n" +
            "8:2:SIG(G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU)\n" +
            "5:1:SIG(G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU)\n" +
            "2:0:SIG(G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU)\n" +
            "all 10.6517\n" +
            "42yQm4hGTJYWkPg39hQAUgP6S6EQ4vTfXdJuxKEHL1ih6YHiDL2hcwrFgBHjXLRgxRhj2VNVqqc6b4JayKqTE14r\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String BLOCK_TX_V3_TOO_HIGH_OUTPUT_BASE = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 83\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 101\n" +
            "IssuersFrameVar: 11\n" +
            "DifferentIssuersCount: 4\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:10:1:1:1:1:0:0\n" +
            "5-2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326\n" +
            "HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY\n" +
            "10:3:T:2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326:88\n" +
            "0:SIG(0)\n" +
            "1:4:SIG(BYfWYFrsyjpvpFysgu19rGK3VHBkz4MqmQbNyEuVU64g)\n" +
            "I6gJkJIQJ9vwDRXZ6kdBsOArQ3zzMYPmFxDbJqseBVq5NWlmJ7l7oY9iWtqhPF38rp7/iitbgyftsRR8djOGDg==\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String TRANSACTION_WITH_DUPLICATED_SOURCE_MULTIPLE_TX = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 83\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:10:2:2:2:1:0:0\n" +
            "3-2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "200:1:T:2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326:88\n" +
            "100:1:T:2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326:88\n" +
            "0:SIG(0)\n" +
            "1:SIG(0)\n" +
            "80:0:SIG(BYfWYFrsyjpvpFysgu19rGK3VHBkz4MqmQbNyEuVU64g)\n" +
            "42yQm4hGTJYWkPg39hQAUgP6S6EQ4vTfXdJuxKEHL1ih6YHiDL2hcwrFgBHjXLRgxRhj2VNVqqc6b4JayKqTE14r\n" +
            "I6gJkJIQJ9vwDRXZ6kdBsOArQ3zzMYPmFxDbJqseBVq5NWlmJ7l7oY9iWtqhPF38rp7/iitbgyftsRR8djOGDg==\n" +
            "TX:10:1:1:1:1:0:0\n" +
            "3-2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY\n" +
            "200:1:T:2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326:88\n" +
            "0:SIG(0)\n" +
            "40:0:SIG(BYfWYFrsyjpvpFysgu19rGK3VHBkz4MqmQbNyEuVU64g)\n" +
            "42yQm4hGTJYWkPg39hQAUgP6S6EQ4vTfXdJuxKEHL1ih6YHiDL2hcwrFgBHjXLRgxRhj2VNVqqc6b4JayKqTE14r\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String TRANSACTION_WITH_WRONG_SIGNATURES = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 83\n" +
            "PoWMin: 1\n" +
            "Time: 1411777000\n" +
            "MedianTime: 1411777000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "PreviousHash: 2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "PreviousIssuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "TX:10:2:2:2:1:0:0\n" +
            "3-2A27BD040B16B7AF59DDD88890E616987F4DD28AA47B9ABDBBEE46257B88E945\n" +
            "HsLShAtzXTVxeUtQd7yi5Z5Zh4zNvbu8sTEZ53nfKcqY\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU\n" +
            "600:5:T:2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326:88\n" +
            "600:5:T:2C31D8915801E759F6D4FF3DA8DA983D7D56DCF4F8D94619FCFAD4B128362326:88\n" +
            "0:SIG(0)\n" +
            "1:SIG(0)\n" +
            "80:0:SIG(BYfWYFrsyjpvpFysgu19rGK3VHBkz4MqmQbNyEuVU64g)\n" +
            "42yQm4hGTJYWkPg39hQAUgP6S6EQ4vTfXdJuxKEHL1ih6YHiDL2hcwrFgBHjXLRgxRhj2VNVqqc6b4JayKqTE14r\n" +
            "I6gJkJIQJ9vwDRXZ6kdBsOArQ3zzMYPmFxDbJqseBVq5NWlmJ7l7oY9iWtqhPF38rp7/iitbgyftsRR8djOGDg==\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String CERT_BASED_ON_NON_ZERO_FOR_ROOT = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:1:vTvKYvjTYUT30t/9h7uNE/2LFJiYuA4YleIetFkb62XxDoxGizKC9VvVs7WRNArcfHvJ+RLyOoawQzpmw2DyCw==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String CERT_BASED_ON_NON_EXISTING_BLOCK = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 1\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "Excluded:\n" +
            "Certifications:\n" +
            "G2CBgZBPLe6FSFUgpx2Jf1Aqsgta6iib3vmDRA1yLiqU:CCCCJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:111:vTvKYvjTYUT30t/9h7uNE/2LFJiYuA4YleIetFkb62XxDoxGizKC9VvVs7WRNArcfHvJ+RLyOoawQzpmw2DyCw==\n"
            +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String REVOKED_WITH_MEMBERSHIPS = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:0-E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855:cat\n"
            +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==\n"
            +
            "Excluded:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String REVOKED_WITH_DUPLICATES = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==\n"
            +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==\n"
            +
            "Excluded:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String REVOKED_NOT_IN_EXCLUDED = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==\n"
            +
            "Excluded:\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String BLOCK_UNKNOWN_REVOKED = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "HATTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==\n"
            +
            "Excluded:\n" +
            "HATTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String BLOCK_WITH_YET_REVOKED = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "BBTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==\n"
            +
            "Excluded:\n" +
            "BBTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";

    String BLOCK_WITH_WRONG_REVOCATION_SIG = "Version: 10\n" +
            "Type: Block\n" +
            "Currency: beta_brousouf\n" +
            "Number: 0\n" +
            "PoWMin: 1\n" +
            "Time: 1411776000\n" +
            "MedianTime: 1411776000\n" +
            "UnitBase: 0\n" +
            "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "IssuersFrame: 100\n" +
            "IssuersFrameVar: 0\n" +
            "DifferentIssuersCount: 3\n" +
            "MembersCount: 3\n" +
            "Identities:\n" +
            "Joiners:\n" +
            "Actives:\n" +
            "Leaver:\n" +
            "Revoked:\n" +
            "CCTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd:iSQvl1VVc6+b1AUaBJ/VTTurGGHgaIcjASBhIlzI7M/7KVQV2Wi3oGUZUzLWqCAtGUsPcsj1HCV2/sRyxHmqAw==\n"
            +
            "Excluded:\n" +
            "CCTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" +
            "Certifications:\n" +
            "Transactions:\n" +
            "InnerHash: DE837CA3F49C423A6A6C124819ABA31A41C1C4A4E2728B5721DF891B98FA8D0D\n" +
            "Nonce: 1\n" +
            "kNsKdC8eH0d4zdHh1djyMzRXjFrwk3Bc3M8wo4DV/7clE9J66K/U0FljyS79SI78ZZUPaVmrImKJ9SNiubCiBg==\n";
}
//@formatter:on
