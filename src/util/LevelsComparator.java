package util;

import java.io.File;
import java.util.Comparator;

public class LevelsComparator implements Comparator<File> {
    @Override
    public int compare(File file1, File file2) {
        String name1 = file1.getName();
        String name2 = file2.getName();
        if (name1.length() != name2.length())
            return name1.length() - name2.length();
        for (int i = 0; i < name1.length(); i++)
            if (name1.charAt(i) != name2.charAt(i))
                return name1.charAt(i) - name2.charAt(i);
        return 0;
    }
}