

package clustering;

import java.util.Collection;

import utility.Linkage;

public class AverageLinkage implements Linkage {

	@Override
	public Distance calculateDistance(Collection<Distance> distances) {
		double sum = 0;
		double result;

		for (Distance dist : distances) {
			sum += dist.getDistance();
		}
		if (distances.size() > 0) {
			result = sum / distances.size();
		} else {
			result = 0.0;
		}
		return  new Distance(result);
	}
}
