package Other;

import java.io.*;
import java.util.Objects;


public class FileWorker {

    public static void appendDump(String filePath, String dump) {
        File file = new File(filePath);
        FileWriter fr = null;
        BufferedWriter br = null;
        try {
            //для обновления файла нужно инициализировать FileWriter с помощью этого конструктора
            fr = new FileWriter(file, true);
            br = new BufferedWriter(fr);
            //теперь мы можем использовать метод write или метод append
            br.write(dump);
            br.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void appendData(String filePath, String memory, String value) throws IOException {
        File file = new File(filePath);
        // Сначала проверим, нет ли такой функциональной памяти уже в файле. Если есть, ихменим ее значение.
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String buffer = null;
        boolean found = false;
        while (true) {
            String line = bufferedReader.readLine();
            if(line != null) {
                if(line.contains(memory)) {
                    found = true;
                    String [] array = line.split(":");
                    buffer += array[0] + ":" + value + "\n";
                } else {
                    buffer += line;
                }
            } else break;
        }
        // Если нет, добавим ее
        FileWriter fr = null;
        BufferedWriter br = null;
        if (found) {
            fr = new FileWriter(file);
            fr.write(buffer);
            fr.close();
        } else {
            try {
                //для обновления файла нужно инициализировать FileWriter с помощью этого конструктора
                fr = new FileWriter(file, true);
                br = new BufferedWriter(fr);
                //теперь мы можем использовать метод write или метод append
                br.write(memory + ":" + value);
                br.newLine();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int searchMemory(String filePath) {
        File file = new File(filePath);
        FileReader fr = null;
        BufferedReader br = null;
        int index = 1;
        try {
            //для обновления файла нужно инициализировать FileWriter с помощью этого конструктора
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            //теперь мы можем использовать метод write или метод append

            while (true) {
                if (br.readLine() != null) {
                    index++;
                } else break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                br.close();
                fr.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return index;
    }

    public static String getByIndex(String filePath, int index) {
        File file = new File(filePath);
        FileReader fr = null;
        BufferedReader br = null;
        String line = null;
        try {
            //для обновления файла нужно инициализировать FileWriter с помощью этого конструктора
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            //теперь мы можем использовать метод write или метод append
//            System.out.println(index);
            while (true) {
                line = br.readLine();
                if (line != null) {
                    String [] array = line.split(":");
                    System.out.println(array[0]);
                    System.out.println(array[0].length());
                    System.out.println(array[1]);
                    System.out.println(array[1].length());
                    if (Objects.equals(array[0], String.valueOf(index))) return array[1];
                } else break;

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return line;
    }

    public static String searchValueByTarget(String filePath, String target) {
        File file = new File(filePath);
        FileReader fr = null;
        BufferedReader br = null;
        String line;
        try {
            //для обновления файла нужно инициализировать FileWriter с помощью этого конструктора
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            //теперь мы можем использовать метод write или метод append
            while (true) {
                line = br.readLine();
                if (line != null) {
                    String [] array = line.split(":");
                    if (Objects.equals(array[0], target)) return array[1];
                } else break;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                br.close();
                fr.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void createNewFile(String name) throws IOException {
        String path = "data/" + name + ".txt";
        File file = new File(path);
        file.createNewFile();
    }
}
