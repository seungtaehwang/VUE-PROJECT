package flink.spring_service.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import flink.spring_service.model.ChartData;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
public class ChartController {

    @GetMapping("/api/chart-data")
    public Map<String, Object> getChartData() {
        Map<String, Object> data = new HashMap<>();
        List<String> categories = Arrays.asList("월", "화", "수", "목", "금", "토", "일");
        List<Integer> values = Arrays.asList(120, 200, 150, 80, 70, 110, 130);
        
        data.put("categories", categories);
        data.put("yvalue", values);
        return data; // JSON 형태로 반환됩니다.
    }

    @GetMapping("/api/sales-data")
    public Map<String, Object> getSalesData() {
        List<String> categories = Arrays.asList("월", "화", "수", "목", "금", "토", "일");
        List<Integer> sales = Arrays.asList(120, 200, 150, 80, 70, 110, 130);

        Map<String, Object> data = new HashMap<>();
        data.put("categories", categories);
        data.put("yvalue", sales);

        return data; // JSON 형태로 반환됩니다.
    }    

    @GetMapping("/api/xy-data")
    public ResponseEntity<List<ChartData>> getItems(@RequestParam("chartIndex") String chartIndex) {
        List<ChartData> dataList = new ArrayList<>();
       	Random random = new Random();
		String[] paramValues = {"A", "B", "C", "D", "E"};
		for (String paramValue : paramValues) {
			for (int i = 0; i < 10; i++) {
				String name = String.format("C-%s series %s", chartIndex, paramValue);
				float xvalue = random.nextFloat() * 100;
				float yvalue = random.nextFloat() * 100;
				dataList.add(new ChartData(name, xvalue, yvalue));
			}	
		}
		ResponseEntity<List<ChartData>> response = ResponseEntity.ok(dataList);
        return response;
    }    
}
