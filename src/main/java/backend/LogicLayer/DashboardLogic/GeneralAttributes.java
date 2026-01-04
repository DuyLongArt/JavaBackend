package backend.LogicLayer.DashboardLogic;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor

public class GeneralAttributes
{

    private float xAxis;
    private float yAxis;
    private float calculateSinWave(float frequency, float amplitude, float phaseShift,float verticalShift)
    {
        this.yAxis = (float)(amplitude*Math.sin(frequency*xAxis+phaseShift)+verticalShift);
        return this.yAxis;
    }

    private float calculateCosWave(float frequency, float amplitude, float phaseShift,float verticalShift)
    {
        this.yAxis = (float)(amplitude*Math.cos(frequency*xAxis+phaseShift)+verticalShift);
        return this.yAxis;
    }

}
