import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MultiImageFileViewer { // Renamed class for clarity

    public static void main(String[] args) {
        // Ensure GUI operations run on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            selectAndDisplayAllImages(); // Renamed method
        });
    }

    public static void selectAndDisplayAllImages() {
        JFileChooser fileChooser = new JFileChooser();

        // Configure the File Chooser
        fileChooser.setDialogTitle("Select Image File(s)");
        fileChooser.setMultiSelectionEnabled(true); // Essential for selecting multiple files
        fileChooser.setFileFilter(new ImageFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        // Show the open dialog
        int result = fileChooser.showOpenDialog(null);

        // Process the result
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] selectedFilesArray = fileChooser.getSelectedFiles();

            if (selectedFilesArray.length > 0) {
                System.out.println("Attempting to display " + selectedFilesArray.length + " selected image(s)...");

                // --- Loop through ALL selected files ---
                for (File imageFile : selectedFilesArray) {
                    System.out.println("Processing: " + imageFile.getAbsolutePath());

                    // --- Try to load and display EACH image ---
                    // Put try-catch inside the loop so one bad file doesn't stop others
                    try {
                        // Read the image data
                        BufferedImage loadedImage = ImageIO.read(imageFile);

                        if (loadedImage != null) {
                            // --- Create components and window FOR EACH IMAGE ---
                            ImageIcon imageIcon = new ImageIcon(loadedImage);
                            JLabel imageLabel = new JLabel(imageIcon);
                            JFrame imageFrame = new JFrame("Viewer: " + imageFile.getName()); // Unique title

                            // Close only this specific window
                            imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            imageFrame.getContentPane().add(imageLabel);
                            imageFrame.pack(); // Size window to fit this image
                            imageFrame.setLocationByPlatform(true); // Let OS position windows initially
                            // imageFrame.setLocationRelativeTo(null); // Alternative: center each new window
                            imageFrame.setVisible(true); // Show this window

                        } else {
                            // Handle case where ImageIO.read returns null (unsupported format, etc.)
                            String errorMsg = "Could not load image: " + imageFile.getName() + ". Format unsupported or file corrupted.";
                            System.err.println(errorMsg);
                            // Show a non-blocking error message for this specific file
                            JOptionPane.showMessageDialog(null, errorMsg, "Image Load Error", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (IOException e) {
                        // Handle file reading errors for this specific file
                        String errorMsg = "Error reading image file '" + imageFile.getName() + "': " + e.getMessage();
                        System.err.println(errorMsg);
                        e.printStackTrace(); // Print detailed stack trace to console
                        JOptionPane.showMessageDialog(null, errorMsg, "File Read Error", JOptionPane.ERROR_MESSAGE);
                    }
                    // --- End of processing for one file ---
                }
                // --- End of loop ---

            } else {
                System.out.println("No files were selected.");
            }
        } else if (result == JFileChooser.CANCEL_OPTION) {
            System.out.println("File selection was cancelled by the user.");
        } else if (result == JFileChooser.ERROR_OPTION) {
            System.err.println("An error occurred in the file chooser.");
        }
    }

    // Inner class for the custom File Filter (same as before)
    static class ImageFilter extends FileFilter {
        private final String[] okFileExtensions = new String[] {"jpg", "jpeg", "png", "gif", "bmp"};

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) return true;
            String fileName = f.getName().toLowerCase();
            for (String extension : okFileExtensions) {
                if (fileName.endsWith("." + extension)) return true;
            }
            return false;
        }

        @Override
        public String getDescription() {
            return "Image Files (*.jpg, *.jpeg, *.png, *.gif, *.bmp)";
        }
    }
}