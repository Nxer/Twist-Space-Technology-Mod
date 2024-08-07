package com.Nxer.TwistSpaceTechnology.util;

import com.Nxer.TwistSpaceTechnology.common.machine.TST_BloodyHell;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Consumer;

@ApiStatus.Internal
public class InfoDataHelper {

    private InfoDataHelper() {
        throw new UnsupportedOperationException();
    }

    /**
     * Helper function to build the info data array for machines.
     *
     * @param superInfoData the info data array from parent
     * @param builder       the builder that puts info into the list
     * @return the built array of info data
     *
     * @see TST_BloodyHell#getInfoData() example
     */
    public static String[] buildInfoData(@Nullable String[] superInfoData, Consumer<ArrayList<String>> builder) {
        ArrayList<String> ret = superInfoData != null ? Lists.newArrayList(superInfoData) : new ArrayList<String>();
        builder.accept(ret);
        return ret.toArray(new String[0]);
    }

}
