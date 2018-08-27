package com.edu.cqu.parser;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class DialogUtil {
public static File dialogOpenFile(final String[] fileTypes, String title, String defaultDir){

    JFileChooser jFileChooser = new JFileChooser();
    jFileChooser.setDialogTitle(title);
    jFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
    if (defaultDir != null && defaultDir.length() > 0){
        jFileChooser.setCurrentDirectory(new File(defaultDir));
    }
    jFileChooser.setFileFilter(new FileFilter() {
        @Override
        public boolean accept(File file) {
            // TODO Auto-generated method stub
            if(file.isDirectory()==true)
            {
                return true;
            }
            String fileName=file.getName().toLowerCase();
            if(fileTypes[0].equals(".*")==true)
            {
                return true;
            }
            for(int i=0;i<fileTypes.length;i++)
            {
                if(fileName.endsWith(fileTypes[i])==true)
                {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getDescription() {
            String desc = "";
            for (int i = 0; i < fileTypes.length; i++) {
                desc += fileTypes[i] + ";";
            }
            return desc;
        }
    });
    if(jFileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
    {
        return jFileChooser.getSelectedFile();
    }
    return null;
}

    public static File dialogOpenDir(String title, String defaultDir){
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setDialogTitle(title);
        if (defaultDir!=null && defaultDir.length() > 0){
            jFileChooser.setCurrentDirectory(new File(defaultDir));
        }
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = jFileChooser.showOpenDialog(jFileChooser);
        if (returnVal == jFileChooser.APPROVE_OPTION){
            return jFileChooser.getSelectedFile();
        }
        return null;
    }

    public static void dialogWaring(String message){
        JOptionPane.showMessageDialog(null,message,"Warning",JOptionPane.WARNING_MESSAGE);
    }

    public static void dialogError(String message)
    {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void dialogInformation(String message)
    {
        JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

}
