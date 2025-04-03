import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.IOException;
// Unused imports can be removed if desired, but leaving them doesn't hurt
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO; // Needed for reading and saving
// import java.awt.FlowLayout; // Not currently used

/**
 * A Java Swing application that allows a user to select one or more image files.
 * It can convert each selected image to grayscale using the RGB averaging method
 * or invert the image colors to create a photographic negative.
 * It displays each processed image in a separate window and saves the processed
 * version to the original directory with a prefix and a .png extension.
 */
public class MultiImageFileViewer { // Keeping the class name

    public static void main(String[] args) {
        // Run the GUI operations on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Create an array of options for the dialog
            String[] options = {"Convert to Grayscale", "Invert Images", "Cancel"};
            
            // Show option dialog
            int choice = JOptionPane.showOptionDialog(
                null,
                "What would you like to do?",
                "Image Processing Options",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );
            
            // Process the user's choice
            if (choice == 0) {
                // User chose grayscale conversion
                selectAndDisplayAllImages();
            } else if (choice == 1) {
                // User chose image inversion
                selectAndInvertImages();
            }
            // If choice is 2 (Cancel) or dialog was closed, do nothing
        });
    }

    /**
     * Converts a BufferedImage to grayscale using the averaging method.
     * Calculates gray = (R + G + B) / 3 for each pixel.
     *
     * @param originalImage The original BufferedImage.
     * @return A new BufferedImage in grayscale (TYPE_BYTE_GRAY).
     */
    public static BufferedImage convertToGrayscaleAverage(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // Create a new blank image with the same dimensions, in grayscale format
        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        // Iterate through each pixel of the original image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the color of the pixel in the original image
                int rgb = originalImage.getRGB(x, y);

                // Extract the Red, Green, and Blue components
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                // Calculate the average (integer division truncates)
                int grayLevel = (red + green + blue) / 3;

                // Create the new grayscale pixel value (packed into RGB format)
                int grayRgb = (grayLevel << 16) | (grayLevel << 8) | grayLevel;

                // Set the pixel in the new grayscale image
                grayImage.setRGB(x, y, grayRgb);
            }
        }
        return grayImage;
    }

    /**
     * Converts a BufferedImage to its inverted (negative) form.
     * For each RGB component, it calculates newValue = 255 - originalValue
     *
     * @param originalImage The original BufferedImage.
     * @return A new BufferedImage with inverted colors.
     */
    public static BufferedImage convertToInverted(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // Create a new blank image with the same dimensions and type
        BufferedImage invertedImage = new BufferedImage(width, height, originalImage.getType());

        // Iterate through each pixel of the original image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the color of the pixel in the original image
                int rgb = originalImage.getRGB(x, y);

                // Extract the Red, Green, and Blue components
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                int alpha = (rgb >> 24) & 0xFF; // Preserve alpha channel

                // Invert each component by subtracting from 255
                int invertedRed = 255 - red;
                int invertedGreen = 255 - green;
                int invertedBlue = 255 - blue;

                // Create the new inverted pixel value (packed into RGB format)
                int invertedRgb = (alpha << 24) | (invertedRed << 16) | (invertedGreen << 8) | invertedBlue;

                // Set the pixel in the new inverted image
                invertedImage.setRGB(x, y, invertedRgb);
            }
        }
        return invertedImage;
    }

    /**
     * Opens a file chooser, allows selection of multiple image files,
     * converts each to grayscale, displays it, and saves the grayscale version.
     */
    public static void selectAndDisplayAllImages() {
        JFileChooser fileChooser = new JFileChooser();

        // --- Configure the File Chooser ---
        fileChooser.setDialogTitle("Select Image File(s) to Convert to Grayscale");
        fileChooser.setMultiSelectionEnabled(true); // Allow multiple files
        fileChooser.setFileFilter(new ImageFilter()); // Use custom image filter
        fileChooser.setAcceptAllFileFilterUsed(false); // Hide "All Files" option

        // Set default directory (using the path you provided)
        File defaultDirectory = new File("C:\\Users\\inouy\\OneDrive\\Pictures\\duke-grey");
        // Optional: Check if the directory exists before setting it
        if (defaultDirectory.isDirectory()) {
            fileChooser.setCurrentDirectory(defaultDirectory);
        } else {
            // Print a message if the custom directory wasn't found
            System.out.println("Default directory not found, using system default: " + defaultDirectory.getAbsolutePath());
        }
        // --- End File Chooser Configuration ---

        // Show the file selection dialog
        int result = fileChooser.showOpenDialog(null); // null parent frame

        // --- Process User Action ---
        if (result == JFileChooser.APPROVE_OPTION) { // User clicked "Open"
            File[] selectedFilesArray = fileChooser.getSelectedFiles(); // Get all selected files

            if (selectedFilesArray.length > 0) {
                System.out.println("Attempting to display and save " + selectedFilesArray.length + " selected image(s) in grayscale...");

                // --- Loop Through Each Selected File ---
                for (File imageFile : selectedFilesArray) {
                    System.out.println("Processing: " + imageFile.getAbsolutePath());
                    try {
                        // --- 1. Load Original Image ---
                        BufferedImage loadedImage = ImageIO.read(imageFile);

                        if (loadedImage != null) { // Check if loading was successful
                            // --- 2. Convert to Grayscale ---
                            BufferedImage grayImage = convertToGrayscaleAverage(loadedImage);

                            // --- 3. Display Grayscale Image (in a new window) ---
                            ImageIcon imageIcon = new ImageIcon(grayImage);
                            JLabel imageLabel = new JLabel(imageIcon);
                            // Create a separate window for each image
                            JFrame imageFrame = new JFrame("Grayscale Viewer: " + imageFile.getName());
                            imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
                            imageFrame.getContentPane().add(imageLabel); // Add image label to window
                            imageFrame.pack(); // Size window to fit the image
                            imageFrame.setLocationByPlatform(true); // Ask OS to position window
                            imageFrame.setVisible(true); // Show the window

                            // --- 4. Save the Grayscale Image ---
                            try {
                                String originalFilename = imageFile.getName();
                                String baseName;
                                // Find the last dot to separate base name from extension
                                int dotIndex = originalFilename.lastIndexOf('.');
                                if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
                                    // Get the part before the last dot
                                    baseName = originalFilename.substring(0, dotIndex);
                                } else {
                                    // No extension found, use the whole filename
                                    baseName = originalFilename;
                                }

                                // Construct the new filename with "grey-" prefix and ".png" extension
                                String grayFilename = "grey-" + baseName + ".png";
                                // Get the directory where the original file resides
                                File originalDir = imageFile.getParentFile();
                                // Create the full path for the output file
                                File outputFile = new File(originalDir, grayFilename);
                                // Specify the desired output format
                                String format = "png";

                                // Attempt to write the grayscale image data to the file
                                boolean success = ImageIO.write(grayImage, format, outputFile);

                                // Report success or failure of the save operation
                                if (success) {
                                    System.out.println("   Successfully saved grayscale image to: " + outputFile.getAbsolutePath());
                                } else {
                                    System.err.println("   Failed to save grayscale image (no suitable writer found?) for: " + outputFile.getAbsolutePath());
                                    JOptionPane.showMessageDialog(null,"Failed to save " + grayFilename + "\n(No suitable writer found?)","Save Error",JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (IOException e_save) { // Catch errors during file writing
                                System.err.println("   Error saving grayscale image '" + ("grey-" + imageFile.getName()) + "': " + e_save.getMessage());
                                e_save.printStackTrace(); // Print detailed error for debugging
                                JOptionPane.showMessageDialog(null,"Error saving file 'grey-" + imageFile.getName()+"'\n" + e_save.getMessage(),"Save Error",JOptionPane.ERROR_MESSAGE);
                            }
                            // --- End of Saving Block ---

                        } else { // Handle case where ImageIO.read returned null
                            String errorMsg = "Could not load image: " + imageFile.getName() + ". Format unsupported or file corrupted.";
                            System.err.println(errorMsg);
                            JOptionPane.showMessageDialog(null, errorMsg, "Image Load Error", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (IOException e_load) { // Catch errors during file reading
                        String errorMsg = "Error reading image file '" + imageFile.getName() + "': " + e_load.getMessage();
                        System.err.println(errorMsg);
                        e_load.printStackTrace(); // Print detailed error for debugging
                        JOptionPane.showMessageDialog(null, errorMsg, "File Read Error", JOptionPane.ERROR_MESSAGE);
                    }
                } // --- End of Loop Through Files ---

            } else { // Handle case where user clicked "Open" but selected no files
                System.out.println("No files were selected.");
            }
        } else if (result == JFileChooser.CANCEL_OPTION) { // User clicked "Cancel" or closed dialog
            System.out.println("File selection was cancelled by the user.");
        } else if (result == JFileChooser.ERROR_OPTION) { // An error occurred in JFileChooser
            System.err.println("An error occurred in the file chooser.");
        }
        // --- End Processing User Action ---
    } // --- End selectAndDisplayAllImages Method ---

    /**
     * Opens a file chooser, allows selection of multiple image files,
     * inverts each one, displays it, and saves the inverted version.
     */
    public static void selectAndInvertImages() {
        JFileChooser fileChooser = new JFileChooser();

        // --- Configure the File Chooser ---
        fileChooser.setDialogTitle("Select Image File(s) to Invert");
        fileChooser.setMultiSelectionEnabled(true); // Allow multiple files
        fileChooser.setFileFilter(new ImageFilter()); // Use custom image filter
        fileChooser.setAcceptAllFileFilterUsed(false); // Hide "All Files" option

        // Set default directory (using the path you provided)
        File defaultDirectory = new File("C:\\Users\\inouy\\OneDrive\\Pictures\\duke-grey");
        // Optional: Check if the directory exists before setting it
        if (defaultDirectory.isDirectory()) {
            fileChooser.setCurrentDirectory(defaultDirectory);
        } else {
            // Print a message if the custom directory wasn't found
            System.out.println("Default directory not found, using system default: " + defaultDirectory.getAbsolutePath());
        }

        // Show the file selection dialog
        int result = fileChooser.showOpenDialog(null); // null parent frame

        // --- Process User Action ---
        if (result == JFileChooser.APPROVE_OPTION) { // User clicked "Open"
            File[] selectedFilesArray = fileChooser.getSelectedFiles(); // Get all selected files

            if (selectedFilesArray.length > 0) {
                System.out.println("Attempting to display and save " + selectedFilesArray.length + " inverted image(s)...");

                // --- Loop Through Each Selected File ---
                for (File imageFile : selectedFilesArray) {
                    System.out.println("Processing: " + imageFile.getAbsolutePath());
                    try {
                        // --- 1. Load Original Image ---
                        BufferedImage loadedImage = ImageIO.read(imageFile);

                        if (loadedImage != null) { // Check if loading was successful
                            // --- 2. Convert to Inverted ---
                            BufferedImage invertedImage = convertToInverted(loadedImage);

                            // --- 3. Display Inverted Image (in a new window) ---
                            ImageIcon imageIcon = new ImageIcon(invertedImage);
                            JLabel imageLabel = new JLabel(imageIcon);
                            // Create a separate window for each image
                            JFrame imageFrame = new JFrame("Inverted Image Viewer: " + imageFile.getName());
                            imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
                            imageFrame.getContentPane().add(imageLabel); // Add image label to window
                            imageFrame.pack(); // Size window to fit the image
                            imageFrame.setLocationByPlatform(true); // Ask OS to position window
                            imageFrame.setVisible(true); // Show the window

                            // --- 4. Save the Inverted Image ---
                            try {
                                String originalFilename = imageFile.getName();
                                String baseName;
                                // Find the last dot to separate base name from extension
                                int dotIndex = originalFilename.lastIndexOf('.');
                                if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
                                    // Get the part before the last dot
                                    baseName = originalFilename.substring(0, dotIndex);
                                } else {
                                    // No extension found, use the whole filename
                                    baseName = originalFilename;
                                }

                                // Construct the new filename with "inverted-" prefix and ".png" extension
                                String invertedFilename = "inverted-" + baseName + ".png";
                                // Get the directory where the original file resides
                                File originalDir = imageFile.getParentFile();
                                // Create the full path for the output file
                                File outputFile = new File(originalDir, invertedFilename);
                                // Specify the desired output format
                                String format = "png";

                                // Attempt to write the inverted image data to the file
                                boolean success = ImageIO.write(invertedImage, format, outputFile);

                                // Report success or failure of the save operation
                                if (success) {
                                    System.out.println("   Successfully saved inverted image to: " + outputFile.getAbsolutePath());
                                } else {
                                    System.err.println("   Failed to save inverted image (no suitable writer found?) for: " + outputFile.getAbsolutePath());
                                    JOptionPane.showMessageDialog(null,"Failed to save " + invertedFilename + "\n(No suitable writer found?)","Save Error",JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (IOException e_save) { // Catch errors during file writing
                                System.err.println("   Error saving inverted image '" + ("inverted-" + imageFile.getName()) + "': " + e_save.getMessage());
                                e_save.printStackTrace(); // Print detailed error for debugging
                                JOptionPane.showMessageDialog(null,"Error saving file 'inverted-" + imageFile.getName()+"'\n" + e_save.getMessage(),"Save Error",JOptionPane.ERROR_MESSAGE);
                            }
                            // --- End of Saving Block ---

                        } else { // Handle case where ImageIO.read returned null
                            String errorMsg = "Could not load image: " + imageFile.getName() + ". Format unsupported or file corrupted.";
                            System.err.println(errorMsg);
                            JOptionPane.showMessageDialog(null, errorMsg, "Image Load Error", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (IOException e_load) { // Catch errors during file reading
                        String errorMsg = "Error reading image file '" + imageFile.getName() + "': " + e_load.getMessage();
                        System.err.println(errorMsg);
                        e_load.printStackTrace(); // Print detailed error for debugging
                        JOptionPane.showMessageDialog(null, errorMsg, "File Read Error", JOptionPane.ERROR_MESSAGE);
                    }
                } // --- End of Loop Through Files ---

            } else { // Handle case where user clicked "Open" but selected no files
                System.out.println("No files were selected.");
            }
        } else if (result == JFileChooser.CANCEL_OPTION) { // User clicked "Cancel" or closed dialog
            System.out.println("File selection was cancelled by the user.");
        } else if (result == JFileChooser.ERROR_OPTION) { // An error occurred in JFileChooser
            System.err.println("An error occurred in the file chooser.");
        }
        // --- End Processing User Action ---
    }

    /**
     * Inner class defining a FileFilter for JFileChooser.
     * Accepts directories and common image file extensions (case-insensitive).
     */
    static class ImageFilter extends FileFilter {
        // Allowed image file extensions (lowercase)
        private final String[] okFileExtensions = new String[] {"jpg", "jpeg", "png", "gif", "bmp"};

        /**
         * Checks if a file should be accepted by the filter.
         * @param f The file to check.
         * @return True if the file is a directory or has an accepted image extension, false otherwise.
         */
        @Override
        public boolean accept(File f) {
            // Always accept directories so the user can navigate
            if (f.isDirectory()) {
                return true;
            }
            // Check file extensions (case-insensitive)
            String fileName = f.getName().toLowerCase();
            for (String extension : okFileExtensions) {
                if (fileName.endsWith("." + extension)) {
                    return true; // Accept if extension matches
                }
            }
            return false; // Reject if it's not a directory and extension doesn't match
        }

        /**
         * Provides the description text shown in the file chooser's "Files of type" dropdown.
         * @return The description string.
         */
        @Override
        public String getDescription() {
            return "Image Files (*.jpg, *.jpeg, *.png, *.gif, *.bmp)";
        }
    } // --- End ImageFilter Inner Class ---

} // --- End MultiImageFileViewer Class ---