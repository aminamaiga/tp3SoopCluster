package clustering;

import java.util.Collection;

import utility.Linkage;

public class WeightedLinkage implements Linkage {

    @Override
    public Distance calculateDistance(Collection<Distance> distances) {
        double sum = 0;
        double weightTotal = 0;
        for (Distance distance : distances) {
            weightTotal += distance.getWeight();
            sum += distance.getDistance() * distance.getWeight();
        }

		return new Distance(sum / weightTotal, weightTotal);
    }
}
