package com.t824t.nutritiontracker.service;

import com.t824t.nutritiontracker.entity.WeightRecord;
import com.t824t.nutritiontracker.repository.WeightRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
                weightRecordRepository.findFirstByRecordDate(
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


    // 日付の古い順で全履歴取得
    public List<WeightRecord> getAllWeights() {

        return weightRecordRepository
                .findAllByOrderByRecordDateAsc();
    }


    // 指定日の体重を1件取得
    public WeightRecord getWeightByDate(LocalDate date) {

        return weightRecordRepository
                .findFirstByRecordDate(date);
    }


    // 削除
    public void deleteWeight(Long id) {

        weightRecordRepository.deleteById(id);
    }
}