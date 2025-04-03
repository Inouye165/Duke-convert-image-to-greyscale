# Java Image Grayscale and Inversion Tool

**Author:** Ron Inouye
**Date:** April 3, 2025

## Description

This Java application provides a graphical user interface (GUI) for selecting one or more image files and applying basic image processing effects:
1.  **Grayscale Conversion:** Converts images to grayscale using the RGB averaging method.
2.  **Color Inversion:** Creates a photographic negative of the images.

Processed images are displayed to the user and automatically saved as new PNG files in the original image directory.

## Important Note: Deviation from Course Instructions

This project was developed as part of a course assignment. However, it **intentionally deviates** from the specific instruction to use the `edu.duke` library.

The objective was to fulfill the core requirements of the assignment (image loading, processing for grayscale/inversion, saving) using only **standard Java SE libraries** (specifically `javax.swing`, `java.awt.image`, and `javax.imageio`). This approach was chosen to practice and demonstrate the functionality using widely available Java components.

## Features

* Choose between Grayscale or Invert operation via an initial dialog.
* Select multiple image files (`.jpg`, `.jpeg`, `.png`, `.gif`, `.bmp`) using a standard file chooser.
* Displays each processed image in its own window.
* Automatically saves processed images to the original file's directory:
    * Grayscale images saved as `grey-<original_base_name>.png`.
    * Inverted images saved as `inverted-<original_base_name>.png`.
* Basic error handling for file reading and writing issues.

## Technology Stack

* Java SE (Requires JRE/JDK 8 or later recommended)
* Java Swing (for the GUI components like `JOptionPane`, `JFileChooser`, `JFrame`)
* Java AWT (`BufferedImage` for image manipulation)
* Java ImageIO (for reading and writing image files)

## Setup and Usage

1.  **Prerequisites:** Ensure you have a Java Development Kit (JDK) or Java Runtime Environment (JRE) installed (Version 8 or higher is recommended).
2.  **Compile (if necessary):**
    ```bash
    javac MultiImageFileViewer.java
    ```
3.  **Run:**
    ```bash
    java MultiImageFileViewer
    ```
4.  **Interact:**
    * Upon running, a dialog box will ask whether you want to convert to grayscale or invert images. Make your selection.
    * A file chooser dialog will appear. Navigate to the directory containing your images.
    * Select one or more image files.
    * Click "Open".
    * The application will process each selected image. A new window will pop up displaying the result for each image.
    * The processed images will be saved automatically in the same folder as the originals, with the appropriate prefix (`grey-` or `inverted-`) and a `.png` extension. Check the console output for success or error messages regarding saving.

## What Else Might Be Needed for a README?

To make this README even more comprehensive, you could consider adding:

1.  **Specific Java Version:** Mention the exact Java version you developed and tested with (e.g., Java 11, Java 17).
2.  **Screenshots:** Include a screenshot of the initial choice dialog, the file chooser, and perhaps an example of an original vs. processed image. Visuals are very helpful.
3.  **Error Handling Details:** Briefly describe how errors are presented (e.g., "Error messages are printed to the console and shown in pop-up dialog boxes.").
4.  **License:** Specify how others can use your code. Adding a simple open-source license like MIT is common. You can add a `LICENSE` file and mention it here (e.g., "This project is licensed under the MIT License - see the LICENSE file for details").
5.  **Known Issues or Limitations (Optional):** Are there any image types that don't work well, or specific scenarios handled imperfectly?
6.  **Future Improvements (Optional):** Any ideas you have for extending the functionality (e.g., adding more filters, adjustable parameters, different save formats)?


