package project;

import xml.Xml;

import java.io.File;
import java.io.IOException;

/**
 * Created by svkreml on 26.02.2017.
 */
public class ProjectR {
    ProjFile projFile;
    File root;
    public ProjectR(File projectDirectory,boolean createNewProj) {
        if(projectDirectory.isFile()) projectDirectory=projectDirectory.getParentFile();
        if (!createNewProj) {
            projFile = (ProjFile) Xml.load(new File(projectDirectory,"project.rpro"), ProjFile.class);
            projFile.setPath(projectDirectory);
        } else {
            try {
                File input = new File(projectDirectory, "Входные данные");
                input.mkdir();
                File output = new File(projectDirectory, "Выходные данные");
                output.mkdir();
                File test = new File(projectDirectory, "Тестовые данные");
                test.mkdir();
                File program = new File(projectDirectory, "program.rtran");
                program.createNewFile();
                projFile = new ProjFile(projectDirectory);
                Xml.save(new File(projectDirectory, "project.rpro"), projFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ProjFile getProjFile() {
        return projFile;
    }

    public void setProjFile(ProjFile projFile) {
        this.projFile = projFile;
    }

    public void loadProjFile() {

    }

    public void createNewProject(File directory) {

    }
}
