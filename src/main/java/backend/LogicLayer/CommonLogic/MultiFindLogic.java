package backend.LogicLayer.CommonLogic;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
@Service
public class MultiFindLogic
{
    public String buildMultiFindQuery(String[] cell1, String[] cell2, String[] cell3, Integer maxLength)
    {
        String finalQuery = "SELECT * FROM TABLE WHERE ";
        String tempQuery = null;
        for (int i = 0; i < maxLength; i++)
        {
            String cell1Value = cell1[i];
            String cell2Value = cell2[i];
            String cell3Value = cell3[i];

            tempQuery = "(VALUE1='" + cell1Value + "'AND" + "VALUE2='" + cell2Value + "'" + "AND" + "VALUE3=" + "'" + cell3Value + "')";

        }
        return finalQuery + tempQuery;
    }

    public void initValues(String cell1String, String cell2String, String cell3String, Integer maxLength,
                           AtomicReference<String[]> cell1Value, AtomicReference<String[]> cell2Value, AtomicReference<String[]> cell3Value
    )
    {
        String[] tempMaxLength = new String[maxLength];
        System.out.println(Arrays.toString(tempMaxLength));
        String[] cell1Split = cell1String != null ? cell1String.split(",") : tempMaxLength;
        String[] cell2Split = cell2String != null ? cell2String.split(",") : tempMaxLength;
        String[] cell3Split = cell1String != null ? cell3String.split(",") : tempMaxLength;

        String[] cell1Values = Arrays.copyOf(cell1Split, maxLength);
        String[] cell2Values = Arrays.copyOf(cell2Split, maxLength);
        String[] cell3Values = Arrays.copyOf(cell3Split, maxLength);

        cell1Values = Arrays.stream(cell1Values).map(element -> element == null ? "_" : element).toArray(String[]::new);
        cell2Values = Arrays.stream(cell2Values).map(element -> element == null ? "_" : element).toArray(String[]::new);
        cell3Values = Arrays.stream(cell3Values).map(element -> element == null ? "_" : element).toArray(String[]::new);
        cell1Value.set(cell1Values);
        cell2Value.set(cell2Values);
        cell3Value.set(cell3Values);

    }
    public String handleEmptyValues(String value,Boolean isUnderScoreToEmpty ){
        if(value.equals("_") &&isUnderScoreToEmpty){
            return "";
        }
        else if(value.equals("_") && !isUnderScoreToEmpty){
            return null;
        }
        return value;
    }
    public int countUnderScore(String[] array){
        int count = 0;
        for (String s : array) {
            if (s.equals("_")) {
                count++;
            }
        }
        return count;
    }
}
