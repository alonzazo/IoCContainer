package containers;

import parsers.AnnotationParser;
import parsers.Parser;

import java.io.File;

public class BeanFactoryFromAnnotations extends BeanFactory {

    Parser parser;

    public BeanFactoryFromAnnotations()
    {
        parser = new AnnotationParser();
    }
    public void scan(String packa)
    {
        File[] classesToScan;
        classesToScan = openDirectory(packa);
        for (int i = 0; i < classesToScan.length; i++) {
            getBean(classesToScan[i].getName());
        }

    }

    private File[] openDirectory(String path)
    {
        File directory = new File(path);
        File[] contents = directory.listFiles();
        return contents;
    }

    public void getBean(String className) {

    }
}
