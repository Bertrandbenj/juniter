package juniter.core.validation;

import juniter.core.model.Block;

public class StaticValid implements BlockLocalValid{

    static StaticValid singletons = make();

    private static StaticValid make() {
        return new StaticValid();
    }

    private StaticValid (){

    }

    public static void assertBlock(Block b ) throws AssertionError{
        singletons.assertBlockLocalValid(b);
    }
}
