package net.oldschoolminecraft.pm;

import java.io.File;

public class Util
{
    public static File getPluginFile(final String file) {
        return new File("plugins/PlayerMounts/" + file);
    }
}
