package bussinesLogic.statistics;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bussinesLogic.AnswerDTO;
import bussinesLogic.PlayerAnswerDTO;
import bussinesLogic.QuestionDTO;

/**
 * Service class for computing quiz statistics.
 * 
 * This class contains all logic for:
 *  - Filtering answers by period
 *  - Computing accuracy for questions and themes
 *  - Counting answered questions
 *
 * The UI panels should only call these methods and not contain logic.
 */
public class StatisticsService {

    /**
     * Enum for selecting the time period filter.
     */
    public enum Period {
        ALL_TIME, LAST_MONTH, TODAY
    }

    private final ZoneId zone = ZoneId.systemDefault();

    /**
     * Filters player answers based on the given period.
     * 
     * @param allAnswers all available player answers
     * @param period selected time filter
     * @return filtered list of answers
     */
    public List<PlayerAnswerDTO> filterAnswersByPeriod(List<PlayerAnswerDTO> allAnswers, Period period) {
        if (period == Period.ALL_TIME) {
            return allAnswers;
        }

        LocalDate today = LocalDate.now();
        List<PlayerAnswerDTO> result = new ArrayList<>();

        if (period == Period.TODAY) {
            for (PlayerAnswerDTO pa : allAnswers) {
                LocalDate d = pa.getCreatedAt().atZone(zone).toLocalDate();
                if (d.isEqual(today)) {
                    result.add(pa);
                }
            }
        } else if (period == Period.LAST_MONTH) {
            LocalDate monthAgo = today.minusDays(30);
            for (PlayerAnswerDTO pa : allAnswers) {
                LocalDate d = pa.getCreatedAt().atZone(zone).toLocalDate();
                if (!d.isBefore(monthAgo) && !d.isAfter(today)) {
                    result.add(pa);
                }
            }
        }
        return result;
    }

    /**
     * Builds a map of answers grouped by question ID.
     *
     * @param answers list of player answers (already filtered by period)
     * @return map questionId -> list of answers
     */
    public Map<Integer, List<PlayerAnswerDTO>> buildAnswersByQuestion(List<PlayerAnswerDTO> answers) {
        Map<Integer, List<PlayerAnswerDTO>> answersByQuestion = new HashMap<>();
        for (PlayerAnswerDTO pa : answers) {
            answersByQuestion.computeIfAbsent(pa.getQuestionId(), k -> new ArrayList<>()).add(pa);
        }
        return answersByQuestion;
    }

    /**
     * Computes the global accuracy for all questions.
     */
    public double computeGlobalAccuracy(List<QuestionDTO> allQuestions,
                                        Map<Integer, List<PlayerAnswerDTO>> answersByQuestion,
                                        Map<Integer, List<AnswerDTO>> correctAnswersByQuestion) {
        return computeAccuracyForQuestions(allQuestions, answersByQuestion, correctAnswersByQuestion);
    }

    /**
     * Computes accuracy for a list of questions.
     */
    public double computeAccuracyForQuestions(List<QuestionDTO> questions,
                                              Map<Integer, List<PlayerAnswerDTO>> answersByQuestion,
                                              Map<Integer, List<AnswerDTO>> correctAnswersByQuestion) {
        long totalAnswers = 0;
        long totalCorrect = 0;

        for (QuestionDTO question : questions) {
            List<PlayerAnswerDTO> list = answersByQuestion.get(question.getId());
            if (list == null || list.isEmpty()) continue;

            Integer correctAnswerId = getCorrectAnswerId(question.getId(), correctAnswersByQuestion);
            if (correctAnswerId == null) continue;

            long correct = list.stream().filter(pa -> pa.getAnswerId() == correctAnswerId).count();
            totalAnswers += list.size();
            totalCorrect += correct;
        }

        if (totalAnswers == 0) return 0.0;
        return (double) totalCorrect / totalAnswers;
    }

    /**
     * Computes accuracy for a single question.
     */
    public double computeAccuracyForQuestion(QuestionDTO q,
                                             Map<Integer, List<PlayerAnswerDTO>> answersByQuestion,
                                             Map<Integer, List<AnswerDTO>> correctAnswersByQuestion) {
        List<PlayerAnswerDTO> list = answersByQuestion.get(q.getId());
        if (list == null || list.isEmpty()) return 0.0;

        Integer correctAnswerId = getCorrectAnswerId(q.getId(), correctAnswersByQuestion);
        if (correctAnswerId == null) return 0.0;

        long correctCount = list.stream().filter(pa -> pa.getAnswerId() == correctAnswerId).count();
        return (double) correctCount / list.size();
    }

    /**
     * Counts how many answers exist for a question.
     */
    public int computeAnsweredCount(QuestionDTO q, Map<Integer, List<PlayerAnswerDTO>> answersByQuestion) {
        List<PlayerAnswerDTO> list = answersByQuestion.get(q.getId());
        return list == null ? 0 : list.size();
    }

    /**
     * Counts how many answers exist for a theme (sum of all its questions).
     */
    public int computeAnsweredCountForTheme(List<QuestionDTO> questions,
                                            Map<Integer, List<PlayerAnswerDTO>> answersByQuestion) {
        int sum = 0;
        for (QuestionDTO question : questions) {
            List<PlayerAnswerDTO> list = answersByQuestion.get(question.getId());
            if (list != null) sum += list.size();
        }
        return sum;
    }

    /**
     * Retrieves the first correct answer ID for a question.
     */
    private Integer getCorrectAnswerId(int questionId,
                                       Map<Integer, List<AnswerDTO>> correctAnswersByQuestion) {
        List<AnswerDTO> corrects = correctAnswersByQuestion.get(questionId);
        if (corrects == null || corrects.isEmpty()) return null;
        return corrects.get(0).getId();
    }
}
