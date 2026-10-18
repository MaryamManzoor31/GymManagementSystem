package util;

// Module M4: Thread-based report task.
// This runnable class performs report generation in a separate thread for concurrent execution.
public class ReportTask implements Runnable {

    private String report;

    public ReportTask(String report) {
        this.report = report;
    }

    @Override
    public void run() {

        System.out.println(
                "Report generation started..."
        );

        FileManager.saveReport(report);

        System.out.println(
                "Report generation completed."
        );
    }
}
