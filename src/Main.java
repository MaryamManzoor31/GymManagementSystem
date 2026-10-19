import database.DatabaseSetup;
import gui.MainFrame;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    DatabaseSetup.initializeDatabase();
                    new MainFrame();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null,
                        "Could not connect to MySQL. Check that MySQL is running, "
                        + "the Connector/J driver is available, and your database settings are correct.\n\n"
                        + e.getMessage(), "Database startup failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
