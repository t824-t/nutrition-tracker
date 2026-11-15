package com.t824t.nutritiontracker.service;

import com.t824t.nutritiontracker.entity.WeightRecord;
import com.t824t.nutritiontracker.repository.WeightRecordRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class WeightRecordService {

    private final WeightRecordRepository weightRecordRepository;

    public WeightRecordService(
            WeightRecordRepository weightRecordRepository) {

        this.weightRecordRepository = weightRecordRepository;
    }


    // 同じ日付なら更新、なければ新規登録
    public void saveWeight(WeightRecord weightRecord) {

        WeightRecord existingRecord =
                weightRecordRepository.findFirstByUserIdAndRecordDate(
                        weightRecord.getUser().getId(),
                        weightRecord.getRecordDate()
                );

        if (existingRecord != null) {

            existingRecord.setWeight(
                    weightRecord.getWeight()
            );

            weightRecordRepository.save(existingRecord);

        } else {

            weightRecordRepository.save(weightRecord);
        }
    }






    // 削除
    public void deleteWeight(Long id, Long userId) {

        WeightRecord weightRecord =
                getWeightByIdAndUser(id, userId);

        weightRecordRepository.delete(weightRecord);
    }

    public List<WeightRecord> getWeightsByUser(Long userId) {
        return weightRecordRepository
                .findByUserIdOrderByRecordDateDesc(userId);
    }

    public WeightRecord getWeightByIdAndUser(Long id, Long userId) {
        return weightRecordRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "体重記録が見つかりません"
                ));
    }
}