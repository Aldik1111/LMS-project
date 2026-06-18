package com.example.lms.backend.application.service;

import com.example.lms.backend.application.dto.QuestionDto;
import com.example.lms.backend.application.dto.TestDto;
import com.example.lms.backend.application.dto.TestResultDto;
import com.example.lms.backend.application.dto.TestSubmitRequest;
import com.example.lms.backend.domain.entity.*;
import com.example.lms.backend.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class TestService {
    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final TestResultRepository testResultRepository;
    private final AnswerRepository answerRepository;
    private final AssignmentRepository assignmentRepository;
    private final StudentAnswerRepository studentAnswerRepository;

    public List<TestDto> getAllTests(){
        return testRepository.findAll()
                .stream()
                .map(this::toDtoShort)
                .collect(Collectors.toList());
    }

    public TestDto getTestById(Long testId, boolean isStudent){
        Test test = testRepository.findByIdWithQuestions(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));

         List<Question> questionWithAnswers = questionRepository.findByTestIdWithAnswers(testId);
        test.getQuestions().clear();
        test.getQuestions().addAll(questionWithAnswers);

        return toDtoFull(test, isStudent);
    }

    @Transactional // упал на середине - откат полностью
    public TestDto createTest(TestDto dto, Long managerId){
        User manager = userRepository.findById(managerId).orElseThrow(() -> new RuntimeException("Manager not found"));

        Test test = Test.builder() // Save test
                .title(dto.getTitle())
                .description(dto.getDescription())
                .createdBy(manager)
                .build();

        Test savedTest = testRepository.save(test);

        if (dto.getQuestions() !=null) { // save questions
            for (int i = 0;i < dto.getQuestions().size();i++){
                QuestionDto qDto = dto.getQuestions().get(i);

                Question question = Question.builder()
                        .questionText(qDto.getQuestionText())
                        .test(savedTest)
                        .orderIndex(i+1)
                        .build();
                Question savedQuestion = questionRepository.save(question);

                if(qDto.getAnswers() != null) { // save abcd
                    for(QuestionDto.AnswerDto aDto : qDto.getAnswers()) {
                        Answer answer = Answer.builder()
                                .answerText(aDto.getAnswerText())
                                .label(aDto.getLabel())
                                .correct(Boolean.TRUE.equals(aDto.getCorrect()))
                                .question(savedQuestion)
                                .build();
                        answerRepository.save(answer);
                    }
                }
            }
        }

        return new TestDto(
                savedTest.getId(),
                savedTest.getTitle(),
                savedTest.getDescription(),
                manager.getFullName(),
                savedTest.getCreatedAt(),
                null
        );
    }

    @Transactional
    public void deleteTest(Long testId) {
        if (!testRepository.existsById(testId)) {
            throw new RuntimeException("Test not found" + testId);
        }
        testRepository.deleteById(testId);
    }

    @Transactional
    public TestResultDto submitTest(TestSubmitRequest request, Long studentId){
        // Load test (через новый безопасный метод)
        Test test = testRepository.findByIdWithQuestions(request.getTestId())
                .orElseThrow(() -> new RuntimeException("Test not found"));

        User studentUser = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (!(studentUser instanceof Student student)) {
            throw new RuntimeException("Only student can complete tests");
        }

        // Проверка дедлайна
        if (student.getGroupNumber() != null) {
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            List<Assignment> groupAssignments = assignmentRepository.findAllByTargetGroup(student.getGroupNumber());
            List<Assignment> forThisTest = groupAssignments.stream()
                    .filter(a -> a.getTest() != null && a.getTest().getId().equals(request.getTestId()))
                    .collect(Collectors.toList());

            if (forThisTest.isEmpty()) {
                throw new RuntimeException("Этот тест не назначен вашей группе");
            }

            boolean hasActive = forThisTest.stream()
                    .anyMatch(a -> a.getDeadline() == null || !a.getDeadline().isBefore(now));
            if (!hasActive) {
                throw new RuntimeException("Deadline time is out");
            }
        }

        if (testResultRepository.existsByStudentIdAndTestId(studentId, request.getTestId())) {
            throw new RuntimeException("You arleady complete test");
        }


        // Догружаем ответы отдельным запросом
        List<Question> questionsWithAnswers = questionRepository.findByTestIdWithAnswers(request.getTestId());
        test.getQuestions().clear();
        test.getQuestions().addAll(questionsWithAnswers);


        // Create result of test
        TestResult result = TestResult.builder()
                .student(studentUser)
                .test(test)
                .score(0)
                .totalPoints(questionsWithAnswers.size())
                .build();

        TestResult savedResult = testResultRepository.save(result);

        // check student's answers
        int correctCount = 0;
        List<TestResultDto.AnswerDetailDto> details = new ArrayList<>();

        for (TestSubmitRequest.StudentAnswerDto answerDto : request.getAnswers()) {

            Question question = test.getQuestions().stream()
                    .filter(q -> q.getId().equals(answerDto.getQuestionId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            Answer selectedAnswer = question.getAnswers().stream()
                    .filter(a -> a.getId().equals(answerDto.getSelectedAnswerId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Answer not found"));

            Answer correctAnswer = question.getAnswers().stream()
                    .filter(Answer::isCorrect)
                    .findFirst()
                    .orElse(null);

            boolean isCorrect = selectedAnswer.isCorrect(); // используем selectedAnswer, не correctAnswer

            if(isCorrect) correctCount++;

            StudentAnswer studentAnswer = StudentAnswer.builder()
                    .testResult(savedResult)
                    .question(question)
                    .selectedAnswer(selectedAnswer)
                    .correct(isCorrect)
                    .build();

            studentAnswerRepository.save(studentAnswer);

            details.add(new TestResultDto.AnswerDetailDto(
                    question.getQuestionText(),
                    selectedAnswer.getLabel() + ". " + selectedAnswer.getAnswerText(),
                    correctAnswer != null
                            ? correctAnswer.getLabel() + ". " + correctAnswer.getAnswerText()
                            : "-",
                    isCorrect
            ));
        }

        savedResult.setScore(correctCount);
        testResultRepository.save(savedResult);

        // Завершить все задания группы с этим тестом — один сабмит покрывает все
        if (student.getGroupNumber() != null) {
            assignmentRepository.findAllByTargetGroup(student.getGroupNumber()).stream()
                    .filter(a -> a.getTest() != null && a.getTest().getId().equals(test.getId()))
                    .filter(a -> a.getStatus() == AssignmentStatus.PENDING)
                    .forEach(a -> {
                        a.setStatus(AssignmentStatus.COMPLETED);
                        assignmentRepository.save(a);
                    });
        }

        return new TestResultDto(
                savedResult.getId(),
                test.getTitle(),
                studentUser.getFullName(),
                correctCount,
                questionsWithAnswers.size(),
                questionsWithAnswers.size() - correctCount,
                savedResult.getCompletedAt(),
                details
        );
    }


    private TestDto toDtoShort(Test test){ //without questions
        return new  TestDto(
            test.getId(),
            test.getTitle(),
            test.getDescription(),
            test.getCreatedBy() != null ? test.getCreatedBy().getFullName() : "-",
            test.getCreatedAt(),
            null
        );
    }

    private TestDto toDtoFull(Test test, boolean isStudent){
        List<QuestionDto> questionDtos = test.getQuestions().stream()
                .map(q -> {
                    List<QuestionDto.AnswerDto> answerDtos = q.getAnswers()
                            .stream()
                            .map(a -> new QuestionDto.AnswerDto(
                                    a.getId(),
                                    a.getLabel(),
                                    a.getAnswerText(),
                                    isStudent ? null : a.isCorrect()
                            ))
                            .collect(Collectors.toList());
                    return new QuestionDto(
                            q.getId(),
                            q.getQuestionText(),
                            q.getOrderIndex(),
                            answerDtos
                    );
                })
                .collect(Collectors.toList());
        return new TestDto(
                test.getId(),
                test.getTitle(),
                test.getDescription(),
                test.getCreatedBy() != null ? test.getCreatedBy().getFullName() : "-",
                test.getCreatedAt(),
                questionDtos
        );
    }
}