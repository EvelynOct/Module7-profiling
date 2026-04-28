package com.advpro.profiling.tutorial.service;

import com.advpro.profiling.tutorial.model.Student;
import com.advpro.profiling.tutorial.model.StudentCourse;
import com.advpro.profiling.tutorial.repository.StudentCourseRepository;
import com.advpro.profiling.tutorial.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    // =========================
    // OPTIMIZED METHOD 1
    // =========================
    public List<StudentCourse> getAllStudentsWithCourses() {

        List<Student> students = studentRepository.findAll();

        List<Long> studentIds = students.stream()
                .map(Student::getId)
                .collect(Collectors.toList());

        List<StudentCourse> allStudentCourses =
                studentCourseRepository.findByStudentIdIn(studentIds);

        Map<Long, Student> studentMap = students.stream()
                .collect(Collectors.toMap(Student::getId, s -> s));

        List<StudentCourse> result = new ArrayList<>();

        for (StudentCourse sc : allStudentCourses) {
            StudentCourse mapped = new StudentCourse();
            mapped.setStudent(studentMap.get(sc.getStudent().getId()));
            mapped.setCourse(sc.getCourse());
            result.add(mapped);
        }

        return result;
    }

    // =========================
    // OPTIMIZED METHOD 2
    // =========================
    public Optional<Student> findStudentWithHighestGpa() {
        return studentRepository.findTopByOrderByGpaDesc();
    }

    // =========================
    // OPTIMIZED METHOD 3
    // =========================
    public String joinStudentNames() {

        List<Student> students = studentRepository.findAll();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < students.size(); i++) {
            sb.append(students.get(i).getName());
            if (i < students.size() - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }
}