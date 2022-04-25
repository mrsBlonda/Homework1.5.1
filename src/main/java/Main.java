import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.*;
import com.opencsv.bean.*;


public class Main {

    public static List<Employee> parseCSV (String[] columnMapping, String fileName) {
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new
                    ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();

            List<Employee> text = csv.parse();
            return text;

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;

    }

    public static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String fileNameJson, String json) {
        try(FileWriter file = new FileWriter(fileNameJson)) {
            file.write(json);
            file.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        String[] text = "1,John,Smith,USA,25".split(",");
        String[] text2 = "2,Ivan,Petrov,RU,23".split(",");
        try (CSVWriter writer = new CSVWriter(new FileWriter("data.csv", true))) {
            writer.writeNext(text);
            writer.writeNext(text2);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        list.forEach(System.out::println);
        String json = listToJson(list);
        String fileNameJson = "data.json";
        writeString(fileNameJson, json);



    }
}
