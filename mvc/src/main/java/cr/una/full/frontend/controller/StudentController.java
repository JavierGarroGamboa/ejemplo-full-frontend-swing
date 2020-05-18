/*
 * Copyright (C) 2016 mguzmana
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Universidad Nacional de Costa Rica, Prof: Maikol Guzman Alan.
 */
package cr.una.full.frontend.controller;

import cr.una.full.frontend.Constants;
import cr.una.full.frontend.model.Student;
import cr.una.full.frontend.service.StudentService;
import cr.una.full.frontend.view.StudentListView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Student Controller
 *
 * @author mguzmana
 */
public class StudentController {

    // Using logger for project
    final Logger logger = LogManager.getLogger(StudentController.class);

    // Student Model
    private List<Student> listStudentModel;
    // Student List View
    private StudentListView studentListView;
    // Student Service
    private StudentService studentService;
    // Vector for the Data Table
    private Vector<Vector<String>> dataVector;

    /**
     * Default Constructor
     */
    public StudentController() {
        logger.debug("Design Pattern MVC: [CONTROLLER]");

        studentListView = new StudentListView("List of Students (MVC Demo)");
        studentService = new StudentService();
    }

    /**
     * Public method to init the controller
     */
    public void initController() {
        logger.debug("Controller Init");

        dataVector = loadDataFromService("");
        studentListView.getTableModel().setDataVector(dataVector, Constants.TABLE_HEADER);

        // Different ways to implement the action performed
        studentListView.getFilterButton().addActionListener(e -> searchText());
        studentListView.getSaveButton().addActionListener(e -> saveStudent());
    }

    /**
     * Method to filter the data from the service depending of the search value
     * As soon the user click the button search
     */
    private void searchText() {
        String searchTerm = studentListView.getSearchTermTextField().getText();
        logger.debug("Searching the information of: " + searchTerm);

        dataVector = loadDataFromService(searchTerm);

        logger.debug("Data found: " + dataVector.size());

        studentListView.getTableModel().setDataVector(dataVector, Constants.TABLE_HEADER);
    }

    /**
     * Method to save the data using the service
     */
    private void saveStudent() {
        Student student = new Student();
        Student studentSaved = null;
        student.setName(studentListView.getNameTextField().getText());
        student.setCourse(studentListView.getCourseTextField().getText());
        student.setRating(studentListView.getRatingTextField().getText());

        studentSaved = studentService.saveStudent(student);
        if (studentSaved != null) {
            showMessageDialog(null, "Se almacenó correctamente los datos del estudiante con " +
                    "el id [" + studentSaved.getId() + "]");
        } else {
            showMessageDialog(null, "Hubo un error al almacenar los datos del estudiante");
        }

        // Refresh data
        searchText();
    }

    /**
     * Method to load data from the service
     *
     * @param searchTerm filter the data with this term
     * @return vector of students
     */
    private Vector<Vector<String>> loadDataFromService(String searchTerm) {

        dataVector = new Vector();

        try {
            if (!"".equals(searchTerm) && searchTerm.length() > 0) {
                listStudentModel = studentService.searchStudentsByTerm(searchTerm);

            } else {
                listStudentModel = studentService.loadAllStudents();
            }

            if (listStudentModel != null && listStudentModel.size() > 0) {
                int index = 0;
                Vector<String> studentVector = null;
                for (Student student : listStudentModel) {
                    studentVector = new Vector();
                    studentVector.add(checkIfNull(student.getId()));
                    studentVector.add(checkIfNull(student.getName()));
                    studentVector.add(checkIfNull(student.getCourse()));
                    studentVector.add(checkIfNull(student.getRating()));

                    dataVector.add(studentVector);
                }
            }
        } catch (IOException e) {
            logger.error("Error general al traer datos del Service: ", e);
        }
        return dataVector;
    }

    /**
     * Check if the value is null
     *
     * @param obj the value
     * @return Empty value if it's null
     */
    private String checkIfNull(Object obj) {
        String text;
        if (obj == null) {
            text = "";
        } else {
            text = obj.toString();
        }
        return text;
    }

    public StudentListView getStudentListView() {
        return studentListView;
    }

    public void setStudentListView(StudentListView studentListView) {
        this.studentListView = studentListView;
    }

}
