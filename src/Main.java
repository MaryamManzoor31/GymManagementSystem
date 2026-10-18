import database.DatabaseSetup;
import database.DatabaseConnection;
import gui.MainFrame;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.sql.SQLException;

// Module M1: Application entry point.
// This class starts the gym management system by launching the GUI window.
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    DatabaseSetup.initializeDatabase();
                    DatabaseConnection.displaySavedMembers();
                    new MainFrame();
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(
                            null,
                            "Could not connect to or initialize MySQL.\n"
                                    + "Check that MySQL is running, the JDBC driver is available, "
                                    + "and the configured credentials are correct.\n\n"
                                    + e.getMessage(),
                            "Database startup failed",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }
}
