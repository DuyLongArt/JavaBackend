package backend.DataLayer.protocol.System;

import backend.DataLayer.protocol.Person.PersonEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class SystemScoringService {

    @Autowired
    private ScoreDAO scoreDAO;

    @Autowired
    private HealthMetricsDAO healthMetricsDAO;

    /**
     * Initializes a new score record for a person.
     */
    @Transactional
    public void initializeScore(PersonEntity person) {
        if (!scoreDAO.existsById(person.getId())) {
            ScoreEntity score = new ScoreEntity(person);
            scoreDAO.save(score);
        }
    }

    /**
     * Recalculates the scores for a person on a specific date based on health metrics.
     */
    @Transactional
    public void updateScoresFromHealthMetrics(Integer personId, LocalDate date) {
        healthMetricsDAO.findByPersonIdAndDate(personId, date).ifPresent(metrics -> {
            ScoreEntity score = scoreDAO.findById(personId).orElseGet(() -> {
                ScoreEntity newScore = new ScoreEntity();
                newScore.setId(personId);
                return newScore;
            });

            // Calculate points
            double healthPoints = (metrics.getSteps() / 500.0) + (metrics.getExerciseMinutes() / 20.0);
            double careerPoints = (metrics.getFocusMinutes() / 10.0);
            
            // Assume 10 points for meeting calorie goal (this is simplified)
            if (metrics.getCaloriesConsumed() > 0 && metrics.getCaloriesConsumed() < 2000) {
                healthPoints += 10.0;
            }

            score.setHealthGlobalScore(score.getHealthGlobalScore() + healthPoints);
            score.setCareerGlobalScore(score.getCareerGlobalScore() + careerPoints);

            scoreDAO.save(score);

            // Update Quests progress if any
            updateQuestsProgress(personId, metrics);
        });
    }

    private void updateQuestsProgress(Integer personId, HealthMetricsEntity metrics) {
        List<QuestEntity> activeQuests = questDAO.findByPersonIdAndIsCompleted(personId, false);
        for (QuestEntity quest : activeQuests) {
            boolean changed = false;
            // Example quest logic: If it's a steps quest
            if (quest.getTitle().contains("Steps") && quest.getCategory() == QuestCategory.HEALTH) {
                quest.setCurrentValue((double) metrics.getSteps());
                changed = true;
            }
            
            if (quest.getCurrentValue() >= quest.getTargetValue()) {
                quest.setIsCompleted(true);
                addPoints(personId, quest.getCategory(), quest.getRewardExp());
                changed = true;
            }
            
            if (changed) {
                questDAO.save(quest);
            }
        }
    }

    /**
     * Directly adds points to a specific category.
     */
    @Transactional
    public void addPoints(Integer personId, QuestCategory category, double points) {
        scoreDAO.findById(personId).ifPresent(score -> {
            switch (category) {
                case HEALTH:
                    score.setHealthGlobalScore(score.getHealthGlobalScore() + points);
                    break;
                case SOCIAL:
                    score.setSocialGlobalScore(score.getSocialGlobalScore() + points);
                    break;
                case FINANCE:
                    score.setFinancialGlobalScore(score.getFinancialGlobalScore() + points);
                    break;
                case PROJECT:
                case FEAT:
                    score.setCareerGlobalScore(score.getCareerGlobalScore() + points);
                    break;
            }
            scoreDAO.save(score);
        });
    }
}
