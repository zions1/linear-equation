package assignment.controller;

import assignment.exception.MatrixException;
import assignment.exception.VectorException;
import assignment.model.LinearEquation;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * The main controller of the application.
 *
 * @author Wiktor Florencki
 * @version 1.0
 */
public class Controller {

    /**
     * Text area for matrix A.
     *
     * @see TextArea
     */
    @FXML
    private TextArea aTextArea;
    /**
     * Text area for vector B.
     *
     * @see TextArea
     */
    @FXML
    private TextArea bTextArea;
    /**
     * Display area for the results.
     *
     * @see TextArea
     */
    @FXML
    private TextArea displayTextArea;
    /**
     * Text field for save or load message.
     *
     * @see Text
     */
    @FXML
    public Text savedOrLoadedText;
    /**
     * Text field for error message.
     *
     * @see Text
     */
    @FXML
    public Text errorText;

    /**
     * Default constructor.
     */
    public Controller() {
    }

    /**
     * Computes the LU-decomposition of the matrix with pivoting.
     * <br>If matrix is singular and not exist LU-decomposition, it shows the original matrix and communities.
     * <br>If matrix is non-singular and exists LU-decomposition, it shows the original, lower and upper matrix, original vector, solution and determinant.
     * <br>Clears {@link Controller#errorText} and {@link Controller#savedOrLoadedText}.
     */
    @FXML
    void computeLUWithPivoting() {
        try {
            clearErrorText();
            clearSavedOrLoadedText();
            LinearEquation linearEquation = new LinearEquation(aTextArea.getText(), bTextArea.getText());
            StringBuffer stringBuff = new StringBuffer();

            if (!linearEquation.isLUDecomposition() && linearEquation.isSingularMatrix()) {
                errorText.setText("No LU decomposition singular matrix");
                stringBuff.append("No LU decomposition singular matrix\n");
                stringBuff.append("Original matrix\n").append(prepareMatrix(linearEquation.getMatrix())).append("\n");
                stringBuff.append("Original vector\n").append(prepareVector(linearEquation.getVector())).append("\n");
                stringBuff.append("Singular matrix");
            } else {
                stringBuff.append("LU Decomposition with scaled partial pivoting\n");
                stringBuff.append("Original matrix\n").append(prepareMatrix(linearEquation.getMatrix())).append("\n");
                stringBuff.append("Original vector\n").append(prepareVector(linearEquation.getVector())).append("\n");
                stringBuff.append("Lower matrix\n").append(prepareMatrix(linearEquation.getLowerMatrix())).append("\n");
                stringBuff.append("Upper matrix\n").append(prepareMatrix(linearEquation.getUpperMatrix())).append("\n");
                stringBuff.append("Solution\n").append(prepareVector(linearEquation.getSolution())).append("\n");
                stringBuff.append("Determinant = ").append(linearEquation.getDeterminant());
            }
            displayTextArea.setText(stringBuff.toString());
        } catch (MatrixException ex) {
            errorText.setText("Error in matrix input.");
        } catch (VectorException ex) {
            errorText.setText("Error in vector input.");
        } catch (Exception ex) {
            errorText.setText("Error in input data.");
        }
    }

    /**
     * Computes the inverse of the matrix.
     * <br>If matrix is singular and not exist LU-decomposition, it shows the original matrix and communities.
     * <br>If matrix is non-singular and exists LU-decomposition, it shows the original, lower, upper and inverse matrix and determinant.
     * <br>Clears {@link Controller#errorText} and {@link Controller#savedOrLoadedText}.
     */
    @FXML
    public void computeInverse() {
        try {
            clearErrorText();
            clearSavedOrLoadedText();
            LinearEquation linearEquation = new LinearEquation(aTextArea.getText());
            StringBuffer stringBuff = new StringBuffer();
            if (!linearEquation.isLUDecomposition() && linearEquation.isSingularMatrix()) {
                errorText.setText("No LU decomposition singular matrix");
                stringBuff.append("No LU decomposition singular matrix\n");
                stringBuff.append("Original matrix\n").append(prepareMatrix(linearEquation.getMatrix())).append("\n");
                stringBuff.append("Singular matrix");
            } else {
                stringBuff.append("Matrix Inversion\n");
                stringBuff.append("Original matrix\n").append(prepareMatrix(linearEquation.getMatrix())).append("\n");
                stringBuff.append("Lower matrix\n").append(prepareMatrix(linearEquation.getLowerMatrix())).append("\n");
                stringBuff.append("Upper matrix\n").append(prepareMatrix(linearEquation.getUpperMatrix())).append("\n");
                stringBuff.append("Inverse matrix\n").append(prepareMatrix(linearEquation.getInverseMatrix())).append("\n");
                stringBuff.append("Determinant = ").append(linearEquation.getDeterminant());
            }
            displayTextArea.setText(stringBuff.toString());
        } catch (Exception ex) {
            errorText.setText("Error in matrix input.");
        }
    }

    /**
     * Prepares the matrix data.
     *
     * @param matrix input vector.
     * @return matrix data in string buffer.
     */
    private StringBuffer prepareMatrix(RealMatrix matrix) {
        StringBuffer stringBuff = new StringBuffer();
        Arrays.stream(matrix.getData())
                .forEach(
                        (row) -> {
                            Arrays.stream(row)
                                    .forEach((el) -> stringBuff.append("\t").append(String.format("% 3.7f", el == -0 ? 0 : el)));
                            stringBuff.append("\n");
                        }
                );
        return stringBuff;
    }

    /**
     * Prepares the vector data.
     *
     * @param vector input vector.
     * @return vector data in string buffer.
     */
    private StringBuffer prepareVector(RealVector vector) {
        StringBuffer stringBuff = new StringBuffer();
        Arrays.stream(vector.toArray())
                .forEach(
                        (el) -> stringBuff.append("\t").append(String.format("% .7f", el == -0 ? 0 : el))
                );
        stringBuff.append("\n");
        return stringBuff;
    }

    /**
     * Clears {@link Controller#displayTextArea}.
     * <br>Clears {@link Controller#errorText} and {@link Controller#savedOrLoadedText}.
     */
    public void clearOutputs() {
        clearErrorText();
        clearSavedOrLoadedText();
        displayTextArea.clear();
    }

    /**
     * Saves results form the {@link Controller#displayTextArea} to the file chosen by the user.
     * <br>Prints communicates in {@link Controller#savedOrLoadedText}.
     * <br>Clears {@link Controller#errorText}.
     */
    public void saveResults() {
        clearErrorText();
        clearSavedOrLoadedText();
        if (!displayTextArea.getText().isEmpty()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Text File (*.txt)", "*.txt"));
            File selectedFile = fileChooser.showSaveDialog(null);
            if (selectedFile != null) {
                saveTextToFile(displayTextArea.getText(), selectedFile.getAbsolutePath());
                savedOrLoadedText.setText("Saved!");
            }
        } else{
            savedOrLoadedText.setText("No results! Compute LU factorisation of the matrix with pivoting or inverse of the matrix before saving the results.");
        }
    }

    /**
     * Loads results form the file chosen by the user and prints they in {@link Controller#displayTextArea}.
     * <br>Prints communicates in {@link Controller#savedOrLoadedText}.
     * <br>Clears {@link Controller#errorText} and {@link Controller#savedOrLoadedText}.
     */
    public void loadResults() {
        clearErrorText();
        clearSavedOrLoadedText();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            displayTextArea.setText(loadTextFromFile(selectedFile));
            savedOrLoadedText.setText("Loaded!");
        } else {
            savedOrLoadedText.setText("Error! Loaded failed.");
        }
    }

    /**
     * Loads data form the file.
     *
     * @param file the file.
     * @return the data in {@code String} format.
     */
    private String loadTextFromFile(File file) {
        try {
            return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        } catch (IOException e) {
            savedOrLoadedText.setText("Error! Loaded failed.");
        }
        return null;
    }

    /**
     * Saves data to the file.
     *
     * @param content  data to save in {@code String} format.
     * @param fileName the name of the file.
     */
    private void saveTextToFile(String content, String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
            byte[] data=content.getBytes("UTF-8");
            outStream.write(data);
            outStream.close();
        } catch (IOException ex) {
            savedOrLoadedText.setText("Error! Saved failed.");
        }
    }

    /**
     * Clears {@link Controller#savedOrLoadedText} field.
     */
    private void clearSavedOrLoadedText() {
        savedOrLoadedText.setText(null);
    }

    /**
     * Clears {@link Controller#errorText} field.
     */
    private void clearErrorText() {
        errorText.setText(null);
    }
}