package util;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

// Module M4: Utility class for file operations.
// This class demonstrates character stream and byte stream I/O for saving reports and backups.
public class FileManager {

    // Character stream: writes a report in plain text format to a .txt file.
    public static void saveReport(String report) {

        try {

            FileWriter writer =
                    new FileWriter("gym_report.txt");

            writer.write(report);

            writer.close();

            System.out.println("Report saved successfully.");

        } catch (IOException e) {

            System.out.println(
                    "Error while saving report."
            );
        }
    }

    // Character stream: reads the saved report back from the text file.
    public static void readReport() {

        try {

            FileReader reader =
                    new FileReader("gym_report.txt");

            int character;

            while ((character = reader.read()) != -1) {

                System.out.print((char) character);
            }

            reader.close();

        } catch (IOException e) {

            System.out.println(
                    "Error while reading report."
            );
        }
    }

    // Byte stream: saves a binary-style backup of the report using file bytes.
    public static void saveBackup(String data) {

        try {

            FileOutputStream output =
                    new FileOutputStream("gym_backup.dat");

            byte[] bytes = data.getBytes();

            output.write(bytes);

            output.close();

            System.out.println("Backup created.");

        } catch (IOException e) {

            System.out.println(
                    "Error while creating backup."
            );
        }
    }

    // Byte stream: reads the backup file and prints the original content.
    public static void readBackup() {

        try {

            FileInputStream input =
                    new FileInputStream("gym_backup.dat");

            int data;

            while ((data = input.read()) != -1) {

                System.out.print((char) data);
            }

            input.close();

        } catch (IOException e) {

            System.out.println(
                    "Error while reading backup."
            );
        }
    }
}
