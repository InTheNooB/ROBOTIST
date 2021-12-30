package app.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dingl01
 */
public abstract class FileHandler {

     static List<String> readTextFile(String filepath) {
        ArrayList<String> lines = null;
        BufferedReader br = null;

        try {
            br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(filepath), "UTF8"));
            lines = new ArrayList();

            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }

            br.close();
            br = null;
        } catch (IOException e) {
            lines = null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                    br = null;
                } catch (IOException e) {
                }
            }
        }
        return lines;

    }

    public static boolean writeObjectToFile(String filepath, Object o) {
        boolean serialized = false;
        if ((filepath != null) && (!filepath.isEmpty())) {
            ObjectOutputStream objectOut = null;
            try {
                FileOutputStream fileOut = new FileOutputStream(filepath);
                objectOut = new ObjectOutputStream(fileOut);
                objectOut.writeObject(o);
                serialized = true;
            } catch (IOException ex) {
            } finally {
                try {
                    if (objectOut != null) {
                        objectOut.close();
                    }
                } catch (IOException ex) {
                }
            }
        }
        return serialized;
    }

    public static Object readObjectFromFile(String filepath) {
        Object o = null;
        if ((filepath != null) && (!filepath.isEmpty())) {
            try {
                FileInputStream fileIn = new FileInputStream(filepath);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                o = objectIn.readObject();
                objectIn.close();
            } catch (IOException | ClassNotFoundException ex) {
            }
        }
        return o;
    }

    public static boolean fileExists(String filepath) {
        boolean exists = false;
        if ((filepath != null) && (!filepath.isEmpty())) {
            exists = new File(filepath).exists();
        }
        return exists;
    }

    public static boolean writeTextFile(String filepath, List<String> linesToWrite) {
        boolean result = false;
        if ((filepath != null) && (!filepath.isEmpty())) {
            if (linesToWrite != null) {
                BufferedWriter bw = null;
                try {
                    bw = new BufferedWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(filepath, false), "UTF8"));

                    for (String line : linesToWrite) {
                        if (line != null) {
                            bw.write(line);
                            bw.newLine();
                        }
                    }
                    bw.flush();
                    bw.close();
                    bw = null;
                    result = true;
                } catch (IOException e) {
                } finally {
                    if (bw != null) {
                        try {
                            bw.close();
                            bw = null;
                            if (!result) {
                                new File(filepath).delete();
                            }
                        } catch (IOException e) {
                        }
                    }
                }
            }
        }
        return result;
    }

    public static boolean appendToTextFile(String filepath, String newLineContent) {
        boolean result = false;
        if ((filepath != null) && (!filepath.isEmpty())) {
            if (newLineContent != null) {
                BufferedWriter bw = null;
                try {
                    bw = new BufferedWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(filepath, true), "UTF8"));

                    bw.write(newLineContent);
                    bw.newLine();
                    bw.flush();
                    bw.close();
                    result = true;

                } catch (IOException e) {
                } finally {
                    if (bw != null) {
                        try {
                            bw.close();
                            bw = null;
                        } catch (IOException e) {

                        }
                    }
                }
            }
        }
        return result;
    }
}
